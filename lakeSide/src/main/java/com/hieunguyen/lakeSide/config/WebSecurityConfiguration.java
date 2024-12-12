package com.hieunguyen.lakeSide.config;

import com.hieunguyen.lakeSide.enums.UserRole;
import com.hieunguyen.lakeSide.service.admin.room.iml.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private final IUserService userService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection for stateless API
                .authorizeHttpRequests(request -> request
                        // Public endpoints (no authentication required)
                        .requestMatchers("/api/auth/**").permitAll()
                        // Admin-specific endpoints (only accessible by admins)
                        .requestMatchers("/api/admin/**").hasAuthority(UserRole.ADMIN.name())
                        // Customer-specific endpoints (only accessible by customers)
                        .requestMatchers("/api/customer/**").hasAuthority(UserRole.CUSTOMER.name())
                        // All other requests require authentication
                        .anyRequest().authenticated())
                .sessionManagement(manage -> manage.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless session management
                .authenticationProvider(authenticationProvider()) // Set custom authentication provider
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter before Spring Security filter

        return http.build(); // Return the configured HTTP security
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // Provides AuthenticationManager to Spring Security
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService.userDetailsService()); // Use custom UserDetailsService
        authenticationProvider.setPasswordEncoder(passwordEncoder()); // Use a common BCryptPasswordEncoder
        return authenticationProvider; // Register the authentication provider
    }

    // Reuse the same BCryptPasswordEncoder instance for efficiency
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Password encoding bean
    }
}
