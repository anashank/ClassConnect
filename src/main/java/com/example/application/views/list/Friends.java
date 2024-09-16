package com.example.application.views.list;

import com.example.application.services.UserDetailsServiceImpl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;


public class Friends {

    boolean friends;
    UserDetailsServiceImpl databaseService;
    List<UserForm> allusers;
    List<UserForm> friendList;
    boolean recondFriend;
    int score;
    HashMap<UserForm, Integer> similarity;
    HashMap<UserForm, Boolean> complete;

    public Friends(UserDetailsServiceImpl databaseService) {
        friends = false;
        recondFriend = false;
        this.databaseService = databaseService;
        allusers = this.databaseService.findAllUsers();
        friendList = new ArrayList<UserForm>();
        similarity = new HashMap<UserForm, Integer>();
        complete = new HashMap<UserForm,Boolean>();

        for(int x = 0; x<allusers.size();x++){
            similarity.put(allusers.get(x), 0);
            complete.put(allusers.get(x),false);
        }



    }

    public Friends(boolean friends, UserDetailsServiceImpl databaseService) {
        this.friends = friends;
        this.databaseService = databaseService;
        allusers = this.databaseService.findAllUsers();
        recondFriend = false;

    }

    public boolean recFriends(UserForm currentUser, UserForm compareUser){
        for(int x = 0; x<friendList.size();x++) {
            if (friendList.get(x).equals(compareUser)) {
                return false;
            }
        }
        score = 0;
        boolean classCompare = currentUser.getProfile().getSchedule().getClassName().equals(compareUser.getProfile().getSchedule().getClassName());
        boolean teacherCompare = currentUser.getProfile().getSchedule().getTeacherName().equals(compareUser.getProfile().getSchedule().getTeacherName());
        boolean periodCompare = currentUser.getProfile().getSchedule().getPeriod() == compareUser.getProfile().getSchedule().getPeriod();
        boolean schoolCompare = currentUser.getProfile().getSchool().equals(compareUser.getProfile().getSchool());

        if(classCompare && teacherCompare && periodCompare && schoolCompare){
            score += 40;

        }
        else if(classCompare && teacherCompare && schoolCompare){
            score += 30;
        }
        else if(classCompare && periodCompare && schoolCompare){
            score +=20;
        }
        else if(classCompare && schoolCompare){
            score += 10;
        }
        else if(classCompare){
            score +=5;
        }
        else if(schoolCompare){
            score += 2;
        }
        else{
            score +=0;
        }
        similarity.put(compareUser,score);
        return score > 0;
    }

    public boolean addFriend(UserForm wee) {
            friendList.add(wee);
            complete.put(wee,true);
            return true;
    }

    public boolean removeFriend(UserForm wee) {
        friendList.remove(wee);
        complete.put(wee,false);
        return true;
    }

    public boolean sendFriendRequestNotificaiton() {

        Button actionButton = new Button("@_________ sent you a friend request!", event -> {
            Notification.show("Friend Request received!");
        });

        // Create the Notification with custom content
        Notification notification = new Notification();

        // Create a layout to hold content inside the notification
        HorizontalLayout notificationLayout = new HorizontalLayout();
        notificationLayout.add(actionButton); // Add the button to the notification

        // Add the layout to the notification
        notification.add(notificationLayout);

        // Set notification properties (optional)
        notification.setPosition(Notification.Position.MIDDLE);
        notification.setDuration(10000); // Duration (e.g., 10 seconds)

        // Open the notification
        notification.open();
        return false;
    }

}