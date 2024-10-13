package com.example.application.views.list;

import com.example.application.repositories.ProfileRepository;
import com.example.application.repositories.ScheduleRepository;
import com.example.application.repositories.UserRepository;  // Add UserRepository import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service // Register the class as a Spring-managed bean
public class CompatibilityScorer {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository; // Inject UserRepository

    public List<Schedule> getSchedulesByUserId(Long userId) {
        return scheduleRepository.findAllByUserId(userId);
    }

    public Profile getProfileById(Long profileId) {
        return profileRepository.findById(profileId).orElseThrow();
    }

    public int scoreCompatibilityByUserId(Long userId1, Long userId2) {
        List<Schedule> schedules1 = getSchedulesByUserId(userId1);
        List<Schedule> schedules2 = getSchedulesByUserId(userId2);
        Profile profile1 = getProfileById(userId1);
        Profile profile2 = getProfileById(userId2);

        int totalScore = 0;

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

        if (profile1.getGrade().equals(profile2.getGrade()) && profile1.getSchool().equals(profile2.getSchool())) {
            score += 50;
        } else if (profile1.getSchool().equals(profile2.getSchool())) {
            score += 20;
        } else if (profile1.getGrade().equals(profile2.getGrade())) {
            score += 20;
        }

        if (profile1.getCity().equals(profile2.getCity()) && profile1.getState().equals(profile2.getState())) {
            score += 20;
        }

        return score;
    }

    public static int scoreScheduleCompatibility(Schedule schedule1, Schedule schedule2) {
        int score = 0;

        if (schedule1.getClassName().equals(schedule2.getClassName())) {
            score += 20;

            if (schedule1.getPeriod() == schedule2.getPeriod()) {
                score += 10;
            }

            if (schedule1.getTeacherName().equals(schedule2.getTeacherName())) {
                score += 5;
            }
        }

        return score;
    }

//    // Helper class to store user match information
//    public static class UserMatch {
//        private UserForm user;
//        private int score;
//
//        public UserMatch(UserForm user, int score) {
//            this.user = user;
//            this.score = score;
//        }
//
//        public UserForm getUser() {
//            return user;
//        }
//
//        public void setUser(UserForm user) {
//            this.user = user;
//        }
//
//        public int getScore() {
//            return score;
//        }
//
//        public void setScore(int score) {
//            this.score = score;
//        }
//    }

    public List<UserMatch> findBestMatches(Long userId) {
        List<UserForm> allUsers = userRepository.findAll(); // Fetch all users
        List<UserMatch> matches = new ArrayList<>();

        for (UserForm otherUser : allUsers) {
            if (!otherUser.getId().equals(userId)) {
                int score = scoreCompatibilityByUserId(userId, otherUser.getId());
                // Create UserMatch with both user and recommendedUser
                UserMatch userMatch = new UserMatch();
                userMatch.setUser(userRepository.findById(userId).orElse(null)); // Set the current user
                userMatch.setRecommendedUser(otherUser); // Set the recommended user
                userMatch.setScore(score); // Set the compatibility score

                matches.add(userMatch);
            }
        }

        // Sort matches by score in descending order
        matches.sort((m1, m2) -> Integer.compare(m2.getScore(), m1.getScore()));

        return matches;
    }

}
