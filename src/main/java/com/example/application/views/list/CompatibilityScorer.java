package com.example.application.views.list;

public class CompatibilityScorer {

//    public class UserScorer {
//        public static void main(String[] args) {
//
//            System.out.println("---------- TESTING ----------");
//
//            // Example User and Friends
//            Set<String> userSubjects = Set.of("Math", "Science", "History");
//            List<String> userFriends = List.of("Billy", "Bob", "Joe", "Ben", "Broccoli");
//            User user = new User(18, userSubjects, userFriends, "NYC");
//
//            Set<String> friend1Subjects = Set.of("Math", "Art");
//            List<String> friend1Friends = List.of("Billy", "Bob", "Joe");
//            Friend friend1 = new Friend(19, friend1Subjects, friend1Friends, "NYC");
//
//            Set<String> friend2Subjects = Set.of("Science", "History");
//            List<String> friend2Friends = List.of("Ben", "Thomas", "Ken");
//            Friend friend2 = new Friend(22, friend2Subjects, friend2Friends, "Boston");
//
//            // Scoring friends
//            int score1 = CompatibilityScorer.scoreFriendCompatibility(user, friend1);
//
//            System.out.println();
//            System.out.println();
//            System.out.println();
//
//            int score2 = CompatibilityScorer.scoreFriendCompatibility(user, friend2);
//
//            System.out.println("Friend 1 Compatibility Score: " + score1); // Lower score = more compatible
//            System.out.println("Friend 2 Compatibility Score: " + score2);
//            System.out.println("---------- END TESTING ----------");
//        }
//    }
//
//
//    // User class
//    class User {
//        private int age;
//        private Set<String> subjects;
//        private List<String> friends;
//        private String location;
//
//        // Constructor
//        public User(int age, Set<String> subjects, List<String> friends, String location) {
//            this.age = age;
//            this.subjects = subjects;
//            this.friends = friends;
//            this.location = location;
//        }
//
//        // Getters
//        public int getAge() {
//            return age;
//        }
//
//        public Set<String> getSubjects() {
//            return subjects;
//        }
//
//        public List<String> getFriends() {
//            return friends;
//        }
//
//        public String getLocation() {
//            return location;
//        }
//    }
//
//    // Friend class (extends User)
//    class Friend extends User {
//        public Friend(int age, Set<String> subjects, List<String> friends, String location) {
//            super(age, subjects, friends, location);
//        }
//    }
//
//    // CompatibilityScorer class
//    class CompatibilityScorer {
//
//        public static int scoreFriendCompatibility(User user, Friend friend) {
//            int score = 0;
//
//            // Check age difference (if difference is more than 1 year, add to score)
//            System.out.println("Testing age --- " + "User age: " + user.getAge() + " | Friend age: " + friend.getAge());
//            if (Math.abs(user.getAge() - friend.getAge()) > 1) {
//                score += 1;
//            }
//
//            // Check common subjects (if less than 50% common subjects, add to score)
//            System.out.println("Testing subjects --- " + "User subjects: ");
//
//            // TESTING ONLY
//            for (String subject : user.getSubjects()) System.out.println("    " + subject);
//            System.out.println("Testing subjects --- " + "Friend subjects: ");
//            for (String subject : friend.getSubjects()) System.out.println("    " + subject);
//
//            int commonSubjects = 0;
//            for (String subject : user.getSubjects()) {
//                if (friend.getSubjects().contains(subject)) {
//                    commonSubjects++;
//                }
//            }
//            if (commonSubjects < user.getSubjects().size() / 2) {
//                score += 1;
//            }
//
//
//            // TESTING ONLY
//            System.out.println("Testing friends --- " + "User friends: ");
//            for (String f : user.getFriends()) System.out.println("    " + f);
//            System.out.println("Testing friends --- " + "Friend friends: ");
//            for (String f : friend.getFriends()) System.out.println("    " + f);
//
//            // Check common friends (if less than 50% common friends, add to score)
//            int commonFriends = 0;
//            for (String f : user.getFriends()) {
//                if (friend.getFriends().contains(f)) {
//                    commonFriends++;
//                }
//            }
//            if (commonFriends < user.getFriends().size() / 2) {
//                score += 1;
//            }
//
//            System.out.println("Testing Location --- " + "User location: " + user.getLocation() + " | " + "Friend location: " + friend.getLocation());
//            // Check location proximity (if locations don't match, add to score)
//            if (!user.getLocation().equalsIgnoreCase(friend.getLocation())) {
//                score += 1;
//            }
//
//            return score; // Higher score = less compatible
//        }
//    }
}
