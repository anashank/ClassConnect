package com.example.application.views.list;

import com.example.application.services.UserDetailsServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class Friends {
    boolean friends;
    UserDetailsServiceImpl databaseService;
    List<UserForm> allusers;
    List<Friends> recondFriend;

    public Friends(UserDetailsServiceImpl databaseService) {
        friends = false;
        this.databaseService = databaseService;
        allusers = this.databaseService.findAllUsers();
        recondFriend = new ArrayList<>();
    }

    public Friends(boolean friends, UserDetailsServiceImpl databaseService) {
        this.friends = friends;
        this.databaseService = databaseService;
        allusers = this.databaseService.findAllUsers();
        recondFriend = new ArrayList<>();
    }

    public boolean recFriends(UserForm currentUser, UserForm compareUser){


    }

//    public List<Friends> recFriends() {
//        for (int x = 0; x < allusers.size(); x++) {
//            double [] score = new double [allusers.size()];
//            for (int y = 0; y < allusers.size(); y++) {
//                    boolean sameClass = allusers.get(y).getSchedule().getClassName().equals(allusers.get(x).getSchedule().getClassName());
//                    boolean sameTeacher = allusers.get(y).getSchedule().getTeacherName().equals(allusers.get(x).getSchedule().getTeacherName());
//                    boolean samePeriod = allusers.get(y).getSchedule().getPeriod().equals(allusers.get(x).getSchedule().getPeriod());
//                    boolean sameSchool = allusers.get(y).getSchool().equals(allusers.get(x).getSchool());
//
//                    if (sameClass && sameTeacher && samePeriod && sameSchool) {
//                        score[y]+= 1;
//
//                    } else if (sameClass && sameTeacher && sameSchool) {
//                        score[y]+= 0.83;
//                    }
//                    else if(sameClass && samePeriod && sameSchool){
//                        score[y]+= 0.66;
//                    }
//                    else if(sameClass && sameSchool){
//                        score[y]+= 0.49;
//                    }
//                    else if(sameClass){
//                        score[y]+= 0.32;
//
//                    }
//                    else if(sameSchool) {
//                        score[y] += 0.15;
//                    }
//            }
//        }
//
//        return recondFriend;
////    }

    public boolean addFriend() {
        friends = true;
        return friends;
    }

    public boolean removeFriend() {
        friends = false;
        return friends;
    }

    public boolean sendFriendRequest() {
        return false;
    }

    public boolean receiveFriendRequest() {
        return false;
    }
}