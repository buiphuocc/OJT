package com.group1.FresherAcademyManagementSystem.services.Impl;

import com.group1.FresherAcademyManagementSystem.repositories.UserRepository;
import com.group1.FresherAcademyManagementSystem.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserImplement implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetailsService userDetailsServices() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
