package com.example.application.repositories;

import com.example.application.views.list.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {
    List<StudyGroup> findBySubjectContainingIgnoreCase(String subject);
}

