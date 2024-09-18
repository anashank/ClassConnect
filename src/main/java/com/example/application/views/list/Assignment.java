package com.example.application.views.list;

import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;

import java.time.LocalDate;


public class Assignment {



    private String assignmentName;
    private String subjectName;
    private LocalDate dateDue;
    private int points;

    public Assignment(String assignmentName, String subjectName, LocalDate dateDue, int points) {


        this.assignmentName = assignmentName;
        this.subjectName = subjectName;
        this.dateDue = dateDue;
        this.points = points;
    }

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