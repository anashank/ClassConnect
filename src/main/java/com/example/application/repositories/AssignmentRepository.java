package com.example.application.repositories;

import com.example.application.views.list.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    // You can add custom queries here if needed

}
