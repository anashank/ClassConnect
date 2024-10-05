//package com.example.application.views.list;
//
//import com.example.application.services.UserDetailsServiceImpl;
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.notification.Notification;
//import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.HashMap;
//
//
//public class Friends extends UserForm {
//
//    boolean friends;
//    UserDetailsServiceImpl databaseService;
//    List<UserForm> allusers;
//    int score;
//    HashMap<UserForm, Integer> similarity;
//    HashMap<UserForm, Boolean> friendsList;
//
//
//    public Friends(UserDetailsServiceImpl databaseService) {
//        friends = false;
//        this.databaseService = databaseService;
//        allusers = this.databaseService.findAllUsers();
//        similarity = new HashMap<UserForm, Integer>();
//        friendsList = new HashMap<UserForm,Boolean>();
//
//        for(int x = 0; x<allusers.size();x++){
//            similarity.put(allusers.get(x), 0);
//        }
//    }
//    public boolean recFriends(UserForm currentUser, UserForm compareUser){
//        score = 0;
//        boolean classCompare = currentUser.getProfile().getSchedule().getClassName().equals(compareUser.getProfile().getSchedule().getClassName());
//        boolean teacherCompare = currentUser.getProfile().getSchedule().getTeacherName().equals(compareUser.getProfile().getSchedule().getTeacherName());
//        boolean periodCompare = currentUser.getProfile().getSchedule().getPeriod() == compareUser.getProfile().getSchedule().getPeriod();
//        boolean schoolCompare = currentUser.getProfile().getSchool().equals(compareUser.getProfile().getSchool());
//
//        if(classCompare && teacherCompare && periodCompare && schoolCompare){
//            score += 40;
//        }
//        else if(classCompare && teacherCompare && schoolCompare){
//            score += 30;
//        }
//        else if(classCompare && periodCompare && schoolCompare){
//            score +=20;
//        }
//        else if(classCompare && schoolCompare){
//            score += 10;
//        }
//        else if(classCompare){
//            score +=5;
//        }
//        else if(schoolCompare){
//            score += 2;
//        }
//        else{
//            score +=0;
//        }
//        similarity.put(compareUser,score);
//        return score > 0;
//    }
//
//    public boolean addFriend(UserForm wee) {
//        if(isAlreadyFriends(wee)){
//            return false;
//        }
//        else {
//            friendsList.put(wee, true);
//            return true;
//        }
//    }
//
//    public boolean removeFriend(UserForm wee) {
//        if(isAlreadyFriends(wee)) {
//            friendsList.put(wee, false);
//            return true;
//        }
//        return false;
//    }
//
//
//    public boolean sendFriendRequest(UserForm receiver) {
//
//        // Check if they're already friends or if there's a pending request
//        if (isAlreadyFriends(receiver)) {
//            Notification.show("You are already friends!", 3000, Notification.Position.MIDDLE);
//            return false;
//        }
//
//        if (isPendingRequest(receiver)) {
//            Notification.show("Friend request already sent!", 3000, Notification.Position.MIDDLE);
//            return false;
//        }
//
//
//        Notification.show("Friend request sent to " + receiver.getUsername(), 3000, Notification.Position.MIDDLE);
//        return true;
//    }
//
//    public boolean isAlreadyFriends(UserForm user2) {
//        return friendsList.getOrDefault(user2, false);
//    }
//
//    public boolean isPendingRequest(UserForm receiver) {
//        return false; //will figure out logic later
//    }
//
//
//    public boolean receiveFriendRequestNotificaiton(UserForm we) {
//        if(sendFriendRequest(we)) {
//            Notification notification = new Notification();
//            Button accept = new Button("Accept");
//
//            Button decline = new Button("Decline");
//
//            Button actionButton = new Button("@_________ sent you a friend request!", event -> {
//                Notification.show("Friend Request received!");
//            });
//
//
//            // Create a layout to hold content inside the notification
//            HorizontalLayout notificationLayout = new HorizontalLayout();
//            notificationLayout.add(actionButton, accept, decline); // Add the button to the notification
//
//            // Add the layout to the notification
//            notification.add(notificationLayout);
//
//            // Set notification properties (optional)
//            notification.setPosition(Notification.Position.MIDDLE);
//            notification.setDuration(1000);// Duration (e.g., 10 seconds)
//
//            // Open the notification
//            notification.open();
//            return true;
//        }
//        return false;
//    }
//
//}