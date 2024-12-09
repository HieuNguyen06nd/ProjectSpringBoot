package com.hieunguyen.shopwq.service;

import com.hieunguyen.shopwq.model.USER_ROLE;
import com.hieunguyen.shopwq.model.User;
import com.hieunguyen.shopwq.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerUserDetailsService implements UserDetailsService {

    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Retrieve the user by email
        User user = userRepository.findByEmail(username);

        // Corrected logic: throw exception if the user is not found, not when it is found
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        // Retrieve the role from the user
        USER_ROLE role = user.getRole();

        // Create a list of authorities for the role
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.name()));  // Using role name directly

        // Return a Spring Security UserDetails object with email, password, and authorities
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }

}
