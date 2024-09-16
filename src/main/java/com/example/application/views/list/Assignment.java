package com.example.application.views.list;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String assignmentName;
    private String subjectName;
    private LocalDate dateDue;
    private int points;

    public Assignment() {
    }

    public Assignment(String assignmentName, String subjectName, LocalDate dateDue, int points) {
        this.assignmentName = assignmentName;
        this.subjectName = subjectName;
        this.dateDue = dateDue;
        this.points = points;
    }

    public Long getId() { return id;}
    public void setId(Long id) { this.id = id;}

    public String getAssignmentName() {
        return assignmentName;
    }
    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public String getSubjectName() {
        return subjectName;
    }
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public LocalDate getDateDue() {
        return dateDue;
    }
    public void setDateDue(LocalDate dateDue) {
        this.dateDue = dateDue;
    }

    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }
}