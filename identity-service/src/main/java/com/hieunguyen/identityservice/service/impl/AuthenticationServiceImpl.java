package com.hieunguyen.identityservice.service.impl;

import com.hieunguyen.identityservice.dto.request.AuthenticationRequest;
import com.hieunguyen.identityservice.dto.request.IntrospectRequest;
import com.hieunguyen.identityservice.dto.response.AuthenticationResponse;
import com.hieunguyen.identityservice.dto.response.IntrospectResponse;
import com.hieunguyen.identityservice.entity.User;
import com.hieunguyen.identityservice.exception.AppException;
import com.hieunguyen.identityservice.exception.ErrorCode;
import com.hieunguyen.identityservice.repository.UserRepository;
import com.hieunguyen.identityservice.service.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated =  passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.UnAUTHENTICATED);
        }
        var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var tokenInfo = request.getToken();

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(tokenInfo);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        var verifiedToken = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .valid(verifiedToken && expiryTime.after(new Date()))
                .build();
    }

    private String generateToken(User user) {

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("Identity Service")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("JWT serialization failed", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(",");

        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_"+role.getName());

                if (!CollectionUtils.isEmpty(role.getPermissions())){
                    role.getPermissions().forEach(permission -> {
                        stringJoiner.add(permission.getName());
                    });
                }
            });
        return stringJoiner.toString();
    }
}
