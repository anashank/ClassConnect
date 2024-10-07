package com.example.application.views.list;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "study_groups") // Change table name to avoid confusion with reserved keywords
public class Groups {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int groupId; // Unique identifier for the group

    @ElementCollection
    private List<String> usernames = new ArrayList<>(); // Store usernames associated with the group

    private boolean publicView; // Indicates if the study group is public or private

    private String groupName; // Name of the study group
    private String subject;    // Subject of the study group
    private String date;       // Date of the study group
    private String time;       // Time of the study group

    public Groups() {
        this.publicView = false; // Default to private
    }

    // Constructor for creating a new group with users and visibility
    public Groups(List<UserForm> users, boolean isPublic, String groupName, String subject, String date, String time) {
        this.publicView = isPublic;
        this.groupName = groupName;
        this.subject = subject;
        this.date = date;
        this.time = time;
        for (UserForm user : users) {
            this.usernames.add(user.getUsername()); // Assuming UserForm has a getUsername method
        }
    }

    // Getters
    public int getGroupId() {
        return groupId;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public boolean isPublicView() {
        return publicView;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getSubject() {
        return subject;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    // Method to add a user to the group
    public void addUser(String username) {
        usernames.add(username);
    }

    // You can also implement other necessary methods like removeUser, etc.
}
