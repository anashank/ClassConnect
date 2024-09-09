package com.example.application.views.list;

import com.example.application.services.UserDetailsServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class Friends {
    boolean friends;
    UserDetailsServiceImpl databaseService;
    List<UserForm> allusers;
    List<UserForm> friendList;
    boolean recondFriend;
    int score;

    public Friends(UserDetailsServiceImpl databaseService) {
        friends = false;
        recondFriend = false;
        this.databaseService = databaseService;
        allusers = this.databaseService.findAllUsers();
        friendList = new ArrayList<UserForm>();

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
        return score > 0;
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

    public boolean addFriend(UserForm wee) {
            friendList.add(wee);
            friends = true;
            return true;
    }

    public boolean removeFriend(Friends wee) {
            friendList.remove(wee);
            friends = false;
            return true;
    }

    public boolean sendFriendRequest(UserForm user) {

        return false;
    }

    public boolean receiveFriendRequest() {
        
        return false;
    }

}