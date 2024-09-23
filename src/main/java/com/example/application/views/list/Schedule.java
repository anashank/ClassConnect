package com.example.application.views.list;

import jakarta.persistence.*;

@Entity
@Table(name = "schedules")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String className;
    private String teacherName;
    private int period;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserForm user; // Link to User

    // Default constructor
    public Schedule() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }
    public int getPeriod() { return period; }
    public void setPeriod(int period) { this.period = period; }
    public UserForm getUser() { return user; }
    public void setUser(UserForm user) { this.user = user; }
}
