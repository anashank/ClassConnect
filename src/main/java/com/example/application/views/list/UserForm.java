package com.example.application.views.list;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserForm implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 100)
    private String email;

    // Study groups created by this user
    @OneToMany(mappedBy = "creator")
    private Set<StudyGroup> createdGroups = new HashSet<>();

    // Study groups this user has joined
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_joined_groups",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<StudyGroup> joinedGroups = new HashSet<>();

    // Implementing UserDetails methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // Return roles/authorities if implemented
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Getters and Setters for user attributes
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // Getters and Setters for group relationships
    public Set<StudyGroup> getCreatedGroups() {
        return createdGroups;
    }

    public void setCreatedGroups(Set<StudyGroup> createdGroups) {
        this.createdGroups = createdGroups;
    }

    public Set<StudyGroup> getJoinedGroups() {
        return joinedGroups;
    }

    public void setJoinedGroups(Set<StudyGroup> joinedGroups) {
        this.joinedGroups = joinedGroups;
    }

    // Method to add a study group to the joinedGroups set
    public void addJoinedGroup(StudyGroup studyGroup) {
        // Check if the study group is already joined
        if (joinedGroups.contains(studyGroup)) {
            throw new IllegalStateException("User is already a member of this study group.");
        }
        joinedGroups.add(studyGroup);
    }
}
