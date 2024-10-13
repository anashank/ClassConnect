package com.example.application.services;

import com.example.application.views.list.Profile; // Ensure you have the correct import
import com.example.application.repositories.ProfileRepository; // Add this import
import com.example.application.repositories.UserRepository;
import com.example.application.views.list.UserForm;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository; // Add this field

    @Autowired
    public UserService(UserRepository userRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository; // Initialize the ProfileRepository
    }

    @Transactional
    public UserForm getUserWithJoinedGroups(String username) {
        UserForm user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        Hibernate.initialize(user.getJoinedGroups()); // Initialize the collection
        return user;
    }

    @Transactional(readOnly = true)
    public List<Profile> getTopTwoScoringUsers() {
        List<Profile> users = profileRepository.findAll();
        return users.stream()
                .map(profile -> {
                    if (profile.getScore() == null) {
                        profile.setScore(0); // Set default score
                    }
                    return profile;
                })
                .sorted(Comparator.comparing(Profile::getScore).reversed())
                .limit(2)
                .collect(Collectors.toList());
    }

}
