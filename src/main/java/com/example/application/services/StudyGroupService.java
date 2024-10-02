package com.example.application.services;

import com.example.application.repositories.StudyGroupRepository;
import com.example.application.repositories.UserRepository;
import com.example.application.views.list.StudyGroup;
import com.example.application.views.list.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudyGroupService {

    @Autowired
    private StudyGroupRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void joinGroup(StudyGroup studyGroup, UserForm user) {
        if (studyGroup == null || user == null) {
            throw new IllegalArgumentException("Study group and user must not be null.");
        }

        // Check if the user is already a member
        if (user.getJoinedGroups().contains(studyGroup)) {
            throw new IllegalStateException("You are already a member of this study group.");
        }

        // Check if the study group has available spots
        if (studyGroup.getSpotsLeft() > 0) {
            user.addJoinedGroup(studyGroup); // Add group to user's joined groups
            studyGroup.setSpotsLeft(studyGroup.getSpotsLeft() - 1); // Decrease spots left

            // Save the updated study group and user
            repository.save(studyGroup);
            userRepository.save(user);
        } else {
            throw new IllegalStateException("No available spots in the study group.");
        }
    }

    @Transactional(readOnly = true)
    public List<StudyGroup> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<StudyGroup> findBySubject(String subject) {
        return repository.findBySubjectContainingIgnoreCase(subject);
    }

    @Transactional
    public StudyGroup save(StudyGroup studyGroup) {
        return repository.save(studyGroup);
    }
}
