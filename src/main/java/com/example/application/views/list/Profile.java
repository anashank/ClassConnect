package com.example.application.views.list;

import jakarta.persistence.*;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String firstName;

    @Column(nullable = true)
    private String lastName;

    @Column(nullable = true)
    private String grade;

    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String school;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserForm user; // Link to UserForm

    // Default constructor
    public Profile() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSchool() { return school; }
    public void setSchool(String school) { this.school = school; }
    public UserForm getUser() { return user; }
    public void setUser(UserForm user) { this.user = user; }
}