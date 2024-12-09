package com.hieunguyen.component;

import com.hieunguyen.exception.DataNotFoundException;
import com.hieunguyen.exception.InvalidException;
import com.hieunguyen.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoder;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtTokenUtil {
    @Value("${jwt.expiration}")
    int expiration;  // Injected from application properties

    @Value("${jwt.signerKey}")
    String signerKey; // Injected from application properties

    public String generateToken(UserEntity userEntity) throws Exception{
        Map<String, Object> claims = new HashMap<>();
//        this.generrateSecretKey();
        claims.put("phoneNumber", userEntity.getPhoneNumber());
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userEntity.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration*1000L))
                    .signWith(getSingInKey(),SignatureAlgorithm.HS256)
                    .compact();
        }catch (Exception e){
//              // Log the error properly here (this is just a placeholder)
            throw new InvalidParameterException("Cannot create token "+ e.getMessage());
        }
    }
    // Method to generate a JWT token
    private Key getSingInKey(){
        byte[] bytes = Decoders.BASE64.decode(signerKey);
        return Keys.hmacShaKeyFor(bytes);
    }
    // Method to get the signing key
    private String generrateSecretKey(){
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32];
        random.nextBytes(keyBytes);
        String secretKey = Encoders.BASE64.encode(keyBytes);

        return secretKey;
    }
    // Method to extract all claims from a JWT token
    private Claims extractAllClaim (String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSingInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    // Generic method to extract a claim using a provided function
    public  <T> T extractClaim (String token, Function<Claims, T> claimsTFunction){
        final Claims claims =  this.extractAllClaim(token);
        return claimsTFunction.apply(claims);
    }

    // Method to check if the token has expired
    public boolean isTokenExprired (String token){
        Date expirationonDate  = this.extractClaim(token, Claims::getExpiration);
        return expirationonDate.before(new Date());
    }

    // Method to extract the phone number from the token
    public String extractPhoneNumber(String token) {
        return extractClaim(token, Claims::getSubject); // Phone number is stored in the subject
    }

    public boolean validateToken (String token, UserDetails userDetails){
        String phoneNumber = extractPhoneNumber(token);
        return (phoneNumber.equals(userDetails.getUsername())) && !isTokenExprired(token);
    }
}
