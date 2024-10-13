package com.example.application.repositories;

import com.example.application.views.list.UserForm;
import com.example.application.views.list.UserMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMatchRepository extends JpaRepository<UserMatch, Long> {
    List<UserMatch> findByUser(UserForm user);

    // Method to find the top recommended users based on compatibility score
    List<UserMatch> findTopByUserOrderByScoreDesc(UserForm user);
}
