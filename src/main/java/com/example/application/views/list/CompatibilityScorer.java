package com.example.application.views.list;

import com.example.application.repositories.ProfileRepository;
import com.example.application.repositories.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CompatibilityScorer {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    // Method to fetch schedules by user_id using the manually coded method
    public List<Schedule> getSchedulesByUserId(Long userId) {
        return scheduleRepository.findAllByUserId(userId);
    }
    public Profile getProfileById(Long profileId) {
        return profileRepository.findById(profileId).orElseThrow();
    }

    // Method to score compatibility between two users' schedules
    public int scoreCompatibilityByUserId(Long userId1, Long userId2) {
        // Fetch schedules for both users
        List<Schedule> schedules1 = getSchedulesByUserId(userId1);
        List<Schedule> schedules2 = getSchedulesByUserId(userId2);
        Profile profile1 = getProfileById(userId1);
        Profile profile2 = getProfileById(userId2);

        int totalScore = 0;

        // Loop through the schedules and compute compatibility
        for (Schedule schedule1 : schedules1) {
            for (Schedule schedule2 : schedules2) {
                totalScore += scoreScheduleCompatibility(schedule1, schedule2);
            }
        }

        totalScore += scoreProfileCompatibility(profile1, profile2);

        return totalScore;
    }

    public static int scoreProfileCompatibility(Profile profile1, Profile profile2) {
        int score = 0;

        if (profile1.getGrade() == profile2.getGrade() && profile1.getSchool().equals(profile2.getSchool())) {
            score += 50;
        } else if (profile1.getSchool().equals(profile2.getSchool())) {
            score += 20;
        } else if (profile1.getGrade() == profile2.getGrade()) {
            score += 20;
        }

        return score;
    }

    // Helper method to compute compatibility between two schedules
    public static int scoreScheduleCompatibility(Schedule schedule1, Schedule schedule2) {
        int score = 0;

        // Compare classes
        if (schedule1.getClassName().equals(schedule2.getClassName())) {
            score += 20;

            // Compare periods
            if (schedule1.getPeriod() == schedule2.getPeriod()) {
                score += 10; // Add points if periods are the same
            }

            // Compare teachers
            if (schedule1.getTeacherName().equals(schedule2.getTeacherName())) {
                score += 5; // Add points if they have the same teacher
            }
        }

        return score;
    }
}
