package com.example.application.repositories;

import com.example.application.views.list.UserForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserForm, Long> {
    UserForm findByUsername(String username);
    List<UserForm> findAll(); // This is already included in JpaRepository
}
