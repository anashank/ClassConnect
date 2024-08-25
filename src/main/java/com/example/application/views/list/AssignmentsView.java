package com.example.application.views.list;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@PermitAll

@Route("assignments")
public class AssignmentsView extends VerticalLayout {

    public AssignmentsView() {

        List<Assignment> assignments = new ArrayList<>();
        assignments.add(new Assignment("Math", "Calculus", LocalDate.of(2024, 7, 22), 10));
        assignments.add(new Assignment("English", "English 2", LocalDate.of(2024, 7, 22), 20));
        assignments.add(new Assignment("Physics", "Physics C", LocalDate.of(2024, 7, 22), 30));


        Grid<Assignment> grid = new Grid<>(Assignment.class);

        grid.setColumns("assignmentName", "subjectName", "dateDue", "points");
        grid.setItems(assignments);

        add(grid);
    }
}
