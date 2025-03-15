package com.hieunguyen.service.impl;

import com.hieunguyen.config.JwtProvider;
import com.hieunguyen.dto.request.LoginRequest;
import com.hieunguyen.dto.request.SignupRequest;
import com.hieunguyen.dto.response.AuthResponse;
import com.hieunguyen.entity.Cart;
import com.hieunguyen.entity.Seller;
import com.hieunguyen.entity.User;
import com.hieunguyen.entity.VerificationCode;
import com.hieunguyen.repository.CartRepository;
import com.hieunguyen.repository.SellerRepository;
import com.hieunguyen.repository.UserRepository;
import com.hieunguyen.repository.VerificationCodeRepository;
import com.hieunguyen.service.AuthService;
import com.hieunguyen.service.EmailService;
import com.hieunguyen.utils.OtpUtil;
import com.hieunguyen.utils.USER_ROLE;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final CustomUserServiceImpl customUserService;
    private final SellerRepository sellerRepository;

    @Override
    public void sentLoginAndSignupOtp(String email, USER_ROLE role) throws Exception {
        String SIGNING_PREFIX= "signin_";

        if (email.startsWith(SIGNING_PREFIX)){
            email = email.substring(SIGNING_PREFIX.length());

            if (role.equals(USER_ROLE.ROLE_SELLER)){

                Seller seller = sellerRepository.findByEmail(email);
                if (seller == null){
                    throw new Exception("Seller not exist with provided email");
                }
            }else {
                User user = userRepository.findByEmail(email);
                if (user == null){
                    throw new Exception("User not exist with provided email");
                }
            }


        }

        VerificationCode isExist = verificationCodeRepository.findByEmail(email);

        if (isExist != null){
            verificationCodeRepository.delete(isExist);
        }

        String otp = OtpUtil.generateOtp();

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationCodeRepository.save(verificationCode);

        String subject = "hieu nguyen login/signup otp";
        String text = "your login/signup otp is: ";

        emailService.sendVerificationOtpEmail(email, otp, subject, text);
    }


    @Override
    public String createUser(SignupRequest request) throws Exception {

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(request.getEmail());

        if (verificationCode == null || !verificationCode.getOtp().equals(request.getOtp())){
            throw new Exception("Wrong otp ...");
        }

        User user = userRepository.findByEmail(request.getEmail());

        if (user ==null){
            User createUser = new User();
            createUser.setFullName(request.getFullName());
            createUser.setEmail(request.getEmail());
            createUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createUser.setMobile("01234567");
            createUser.setPassword(passwordEncoder.encode(request.getOtp()));

            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);

            user = userRepository.save(createUser);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

        Authentication authentication= new UsernamePasswordAuthenticationToken(request.getEmail(), null, authorities );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);
    }

    @Override
    public AuthResponse signing(LoginRequest request) {
        String username = request.getEmail();
        String otp = request.getOtp();

        Authentication authentication = authenticate(username, otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login success");

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName = authorities.isEmpty()?null:authorities.iterator().next().getAuthority();

        authResponse.setRole(USER_ROLE.valueOf(roleName));

        return authResponse;
    }

    private Authentication authenticate(String username, String otp) {
        UserDetails userDetails= customUserService.loadUserByUsername(username);

        String SELLER_PREFIX= "seller_";
        if (username.startsWith(SELLER_PREFIX)){
            username = username.substring(SELLER_PREFIX.length());
        }

        if (userDetails == null){
            throw new BadCredentialsException("Invalid username or password");
        }

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);

        if (verificationCode == null || !verificationCode.getOtp().equals(otp)){
            throw new BadCredentialsException("Wrong otp ...");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
