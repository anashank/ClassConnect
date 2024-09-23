package com.example.application.repositories;

import com.example.application.views.list.Profile;
import com.example.application.views.list.UserForm; // Import UserForm
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    // Custom query method to find a profile by the associated user
    Optional<Profile> findByUser(UserForm user); // Adjust method name and parameter as needed
}