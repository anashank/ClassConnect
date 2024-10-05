package com.example.application.services;

import com.example.application.views.list.UserForm;
import com.example.application.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserForm findUserByUsername(String username) {
        Optional<UserForm> userOpt = userRepository.findByUsername(username);
        return userOpt.orElse(null); // or throw an exception if user is not found
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserForm> userOpt = userRepository.findByUsername(username);
        UserForm user = userOpt.orElseThrow(() ->
                new UsernameNotFoundException("User not found with username: " + username)
        );

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .disabled(!user.isEnabled())
                .roles("USER") // Customize roles if needed
                .build();
    }

    public List<String> getNames() {
        return userRepository.findAll().stream()
                .map(UserForm::getUsername)
                .collect(Collectors.toList());
    }

    public List<UserForm> findAllUsers() {
        return userRepository.findAll();
    }

    public void createTestUsers() {
        // Using Optional to check for the user
        if (userRepository.findByUsername("user1").isEmpty()) {
            UserForm testUser = new UserForm();
            testUser.setUsername("user1");
            testUser.setPassword(passwordEncoder.encode("password1"));
            testUser.setEmail("email1@example.com");
            userRepository.save(testUser);
        }
    }

    public void saveUserDetails(UserForm user) {
        if (user == null) {
            System.err.println("User is null");
            return;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
