package com.hieunguyen.config;

import com.hieunguyen.filter.JwtTokenFilter;
import com.hieunguyen.model.RoleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;

    @Value("${api.prefix}")  // Inject api.prefix from application.properties
    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)  // Disable CSRF as it's not needed for stateless APIs
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)  // Add the JWT filter before the UsernamePasswordAuthenticationFilter
                .authorizeRequests(requests -> {  // `authorizeRequests` should be used here, not `authorizeHttpRequests`
                    requests
                            .requestMatchers(
                                    String.format("%s/users/register", apiPrefix),
                                    String.format("%s/users/login", apiPrefix)
                            ).permitAll()// Allow unauthenticated access to these endpoints

                            .requestMatchers(HttpMethod.POST, String.format("%s/categories/**", apiPrefix)).hasAnyRole(RoleEntity.ADMIN)
                            .requestMatchers(HttpMethod.GET, String.format("%s/categories/**", apiPrefix)).hasAnyRole(RoleEntity.USER, RoleEntity.ADMIN)
                            .requestMatchers(HttpMethod.PUT, String.format("%s/categories/**", apiPrefix)).hasRole(RoleEntity.ADMIN)  // Only allow PUT requests to the order endpoint for users with the ADMIN role
                            .requestMatchers(HttpMethod.DELETE, String.format("%s/categories/**", apiPrefix)).hasRole(RoleEntity.ADMIN)

                            .requestMatchers(HttpMethod.POST, String.format("%s/product/**", apiPrefix)).hasAnyRole(RoleEntity.ADMIN)
                            .requestMatchers(HttpMethod.GET, String.format("%s/product/**", apiPrefix)).hasAnyRole(RoleEntity.USER, RoleEntity.ADMIN)
                            .requestMatchers(HttpMethod.PUT, String.format("%s/product/**", apiPrefix)).hasRole(RoleEntity.ADMIN)  // Only allow PUT requests to the order endpoint for users with the ADMIN role
                            .requestMatchers(HttpMethod.DELETE, String.format("%s/product/**", apiPrefix)).hasRole(RoleEntity.ADMIN)

                            .requestMatchers(HttpMethod.POST, String.format("%s/order/**", apiPrefix)).hasAnyRole(RoleEntity.USER)
                            .requestMatchers(HttpMethod.GET, String.format("%s/order/**", apiPrefix)).hasAnyRole(RoleEntity.USER, RoleEntity.ADMIN)
                            .requestMatchers(HttpMethod.PUT, String.format("%s/order/**", apiPrefix)).hasRole(RoleEntity.ADMIN)  // Only allow PUT requests to the order endpoint for users with the ADMIN role
                            .requestMatchers(HttpMethod.DELETE, String.format("%s/order/**", apiPrefix)).hasRole(RoleEntity.ADMIN)

                            .requestMatchers(HttpMethod.POST, String.format("%s/orderdetails/**", apiPrefix)).hasAnyRole(RoleEntity.USER)
                            .requestMatchers(HttpMethod.GET, String.format("%s/orderdetails/**", apiPrefix)).hasAnyRole(RoleEntity.USER, RoleEntity.ADMIN)
                            .requestMatchers(HttpMethod.PUT, String.format("%s/orderdetails/**", apiPrefix)).hasRole(RoleEntity.ADMIN)  // Only allow PUT requests to the order endpoint for users with the ADMIN role
                            .requestMatchers(HttpMethod.DELETE, String.format("%s/orderdetails/**", apiPrefix)).hasRole(RoleEntity.ADMIN)


                            .anyRequest().authenticated();  // Require authentication for any other request
                });
        return http.build();
    }
}

