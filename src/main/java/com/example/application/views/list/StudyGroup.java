package com.example.application.views.list;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class StudyGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String subject;
    private LocalDate date;
    private int spotsLeft;

    // The user who created the study group
    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private UserForm creator;

    // Users who have joined this group
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_joined_groups",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<UserForm> members = new HashSet<>();

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public int getSpotsLeft() { return spotsLeft; }
    public void setSpotsLeft(int spotsLeft) { this.spotsLeft = spotsLeft; }

    public UserForm getCreator() { return creator; }
    public void setCreator(UserForm creator) { this.creator = creator; }

    public Set<UserForm> getMembers() { return members; }
    public void setMembers(Set<UserForm> members) { this.members = members; }

    // Method to add a user to the group, checking for duplicates
    public boolean addMember(UserForm user) {
        // Check if the user is already a member to avoid duplicate entries
        if (!members.contains(user)) {
            members.add(user);
            return true; // Successfully added
        }
        return false; // User is already a member
    }
}
