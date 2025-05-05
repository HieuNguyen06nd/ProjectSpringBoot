package com.hieunguyen.shopstorev2.security;

import com.hieunguyen.shopstorev2.entities.User;
import com.hieunguyen.shopstorev2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {
        User user = userRepository.findByEmailOrPhone(input, input)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email or phone: " + input));

        return new CustomUserDetails(user);
    }

}
