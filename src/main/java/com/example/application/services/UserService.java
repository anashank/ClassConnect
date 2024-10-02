package com.example.application.services;

import com.example.application.repositories.UserRepository;
import com.example.application.views.list.UserForm;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserForm getUserWithJoinedGroups(String username) {
        UserForm user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        Hibernate.initialize(user.getJoinedGroups()); // Initialize the collection
        return user;
    }
}
