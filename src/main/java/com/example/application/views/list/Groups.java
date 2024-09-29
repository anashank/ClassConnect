package com.example.application.views.list;

import com.example.application.services.UserDetailsServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Groups {
   // UserDetailsServiceImpl databaseService;
    boolean publicView;
    List<UserForm> allusers;
    List<UserForm> friends;
    HashMap<Integer, List<UserForm>> groups;
    int temporyNumber;
    int groupId;


    public Groups() {
        publicView = false;;
        friends = new ArrayList<>();
        groups = new HashMap<>();
    }

    // Method to create a new group
    public void setNewGroup(List<UserForm> users) {
        groupId = groups.size() + 1;
        groups.put(groupId, users);
    }

    // Method to retrieve a group by a given user
    public List<UserForm> getCertainGroup(int groupId) {
        return groups.getOrDefault(groupId, null);
    }

    // Method to add a user to an existing group
    public void addUserToGroup(int groupId, UserForm newUser) {
        if (groups.containsKey(groupId)) {
            groups.get(groupId).add(newUser);
        }
    }
    public boolean getStudyGroupPublicity(){
        return publicView;
    }
    public void setStudyGroupPublicity(boolean publicView){
        this.publicView = publicView;
    }

}