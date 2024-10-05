package com.example.application.repositories;

import com.example.application.views.list.Groups;
import com.example.application.views.list.UserForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Groups, Integer> {
    List<Groups> findByUsernamesContaining(String username);
}