package com.hieunguyen.config;

import com.hieunguyen.model.User;
import com.hieunguyen.repository.UserRepository;
import com.hieunguyen.utils.Role;
import com.hieunguyen.utils.StatusUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // Lấy thông tin từ OAuth2 provider (Google hoặc Facebook)
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String provider = userRequest.getClientRegistration().getRegistrationId(); // "google" hoặc "facebook"
        String providerId = getProviderId(provider, attributes);
        String email = (String) attributes.get("email");
        String name = (String) attributes.getOrDefault("name", "Unknown");

        if (providerId == null) {
            throw new OAuth2AuthenticationException("Không lấy được ID từ " + provider);
        }

        User user = findOrCreateUser(provider, providerId, email, name);

        // Gán quyền theo role của user
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        return new DefaultOAuth2User(authorities, attributes, "email");
    }

    private String getProviderId(String provider, Map<String, Object> attributes) {
        if ("google".equals(provider)) {
            return (String) attributes.get("sub");
        } else if ("facebook".equals(provider)) {
            return (String) attributes.get("id");
        }
        return null;
    }

    private User findOrCreateUser(String provider, String providerId, String email, String name) {
        Optional<User> existingUser = findUserByProviderId(provider, providerId);

        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        // Nếu là Facebook và email null → tạo email giả
        if ("facebook".equals(provider) && (email == null || email.isEmpty())) {
            email = "fb_" + providerId + "@facebook.com";
        }

        // Kiểm tra user theo email
        User user = (email != null) ? userRepository.findByEmail(email).orElse(null) : null;

        if (user == null) {
            // Tạo user mới nếu chưa tồn tại
            user = User.builder()
                    .email(email) // Có thể là email thực hoặc email giả (Facebook)
                    .fullName(name)
                    .googleId("google".equals(provider) ? providerId : null)
                    .facebookId("facebook".equals(provider) ? providerId : null)
                    .role(Role.CUSTOMER)
                    .status(StatusUser.ACTIVE)
                    .emailVerified(email != null) // Đánh dấu đã xác minh nếu có email thực
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
        } else {
            // User có email nhưng chưa có Google ID hoặc Facebook ID → Cập nhật
            boolean updated = false;
            if ("google".equals(provider) && user.getGoogleId() == null) {
                user.setGoogleId(providerId);
                updated = true;
            }
            if ("facebook".equals(provider) && user.getFacebookId() == null) {
                user.setFacebookId(providerId);
                updated = true;
            }
            if (user.getFullName() == null) {
                user.setFullName(name);
                updated = true;
            }

            // Nếu có thay đổi thì cập nhật vào database
            if (updated) {
                user.setUpdatedAt(LocalDateTime.now());
            }
        }

        return userRepository.save(user);
    }


    private Optional<User> findUserByProviderId(String provider, String providerId) {
        if ("google".equals(provider)) {
            return userRepository.findByGoogleId(providerId);
        } else if ("facebook".equals(provider)) {
            return userRepository.findByFacebookId(providerId);
        }
        return Optional.empty();
    }
}
