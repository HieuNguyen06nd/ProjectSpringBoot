package com.hieunguyen.filter;

import com.hieunguyen.component.JwtTokenUtil;
import com.hieunguyen.model.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;  // Use Pair from Apache Commons Lang
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    @Value("${api.prefix}")  // Inject api.prefix from application.properties
    private String apiPrefix;

    private final UserDetailsService userDetailsService;

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // Check if the request path should bypass JWT validation
            if (isBypassToken(request)) {
                filterChain.doFilter(request, response);  // Proceed with the next filter
                return;  // Exit early if the token bypass condition is met
            }

            // Add JWT validation logic here (e.g., check Authorization header)
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer "))
            {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "SC_UNAUTHORIZED");
                return;
            }
            final String token = authHeader.substring(7);
            final String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
            if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() ==null){
                UserEntity userDetails = (UserEntity) userDetailsService.loadUserByUsername(phoneNumber);
                if (jwtTokenUtil.validateToken(token, userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken=
                            new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            // Continue processing the filter chain if no bypass
            filterChain.doFilter(request, response);

        }catch (Exception e){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "SC_UNAUTHORIZED");
        }
    }

    private boolean isBypassToken(HttpServletRequest request) {
        // List of endpoints that should bypass JWT validation
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of(apiPrefix + "/product", "GET"),
                Pair.of(apiPrefix + "/categories", "GET"),
                Pair.of(apiPrefix + "/users/register", "POST"),
                Pair.of(apiPrefix + "/users/login", "POST")
        );

        // Iterate over the list and check if the request should bypass
        for (Pair<String, String> bypassToken : bypassTokens) {
            if (request.getServletPath().contains(bypassToken.getLeft()) &&
                    request.getMethod().equalsIgnoreCase(bypassToken.getRight())) {
                return true;  // Bypass if the path and method match
            }
        }
        return false;  // No match found, continue with JWT validation
    }
}
