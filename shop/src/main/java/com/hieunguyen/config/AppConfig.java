package com.hieunguyen.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class AppConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Cấu hình session là stateless (không sử dụng session)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Cấu hình quyền truy cập vào các endpoint
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/**").authenticated()  // Cần xác thực cho tất cả các API bắt đầu với /api/
                        .requestMatchers("/api/product/*/review").permitAll()  // API cho phép không cần xác thực
                        .anyRequest().permitAll()  // Các request khác được phép truy cập
                )

                // Thêm bộ lọc JwtTokenValidator để kiểm tra JWT trước BasicAuthenticationFilter
                .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)

                // Tắt CSRF vì không sử dụng session (đối với API stateless)
                .csrf(csrf -> csrf.disable())

                // Cấu hình CORS
                .cors(cors -> cors.configurationSource(corsConfigrationSource()));

        return http.build();
    }


    private CorsConfigurationSource corsConfigrationSource() {
        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration cfg = new CorsConfiguration();
                cfg.setAllowedOrigins(Collections.singletonList("*"));
                cfg.setAllowedMethods(Collections.singletonList("*"));
                cfg.setAllowedHeaders(Collections.singletonList("*"));
                cfg.setAllowCredentials(true);
                cfg.setExposedHeaders(Collections.singletonList("Authorization"));
                cfg.setMaxAge(3600l);

                return cfg;
            }
        };
    }

    @Bean
    PasswordEncoder passwordEncoder (){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
