package com.hieunguyen.shopstorev2.security;

import com.hieunguyen.shopstorev2.entities.User;
import com.hieunguyen.shopstorev2.repository.UserRepository;
import com.hieunguyen.shopstorev2.utils.AuthProvider;
import com.hieunguyen.shopstorev2.utils.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // "google", "facebook"
        AuthProvider provider = AuthProvider.valueOf(registrationId.toUpperCase());

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String avatarUrl = oAuth2User.getAttribute("picture");
        String providerId = oAuth2User.getAttribute("sub"); // Google dùng "sub", Facebook dùng "id"

        if (providerId == null) {
            providerId = oAuth2User.getAttribute("id"); // fallback for Facebook
        }

        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        Optional<User> optionalUser = userRepository.findByProviderIdAndAuthProvider(providerId, provider);
        User user;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();

        } else {
            // fallback: tìm theo email nếu providerId chưa lưu
            Optional<User> emailUser = userRepository.findByEmail(email);
            if (emailUser.isPresent()) {
                user = emailUser.get();

                if (!user.getAuthProvider().equals(provider)) {
                    throw new OAuth2AuthenticationException("Please use your " + user.getAuthProvider() + " account to login.");
                }

                // nếu đúng provider nhưng chưa có providerId → update
                if (user.getProviderId() == null || !user.getProviderId().equals(providerId)) {
                    user.setProviderId(providerId);
                    userRepository.save(user);
                }

            } else {
                // không có user → tạo mới
                user = User.builder()
                        .email(email)
                        .username(email.split("@")[0])
                        .passwordHash("") // OAuth2 không cần mật khẩu
                        .authProvider(provider)
                        .providerId(providerId)
                        .avatarUrl(avatarUrl)
                        .emailVerified(true)
                        .role(Role.CUSTOMER)
                        .build();

                userRepository.save(user);
            }
        }

        return new CustomOAuth2User(user, oAuth2User.getAttributes());
    }
}
