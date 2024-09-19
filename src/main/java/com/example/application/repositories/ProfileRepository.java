package com.example.application.repositories;

import com.example.application.views.list.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    // You can define custom query methods here if needed
    // For example:
    // Optional<Profile> findByUsername(String username);
}
