package com.example.application.views.list;

import com.example.application.repositories.AssignmentRepository;
import com.example.application.repositories.UserRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.List;

@PermitAll
@Route("assignments")
public class AssignmentsView extends VerticalLayout {

    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;  // Add this

    @Autowired
    public AssignmentsView(AssignmentRepository assignmentRepository, UserRepository userRepository) {
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;  // Initialize userRepository

        // Get the currently logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        // Fetch the user by username
        UserForm loggedInUser = userRepository.findByUsername(username);

        if (loggedInUser == null) {
            throw new RuntimeException("User not found: " + username); // Handle the case when the user is not found
        }

        // Fetch assignments for the logged-in user
        List<Assignment> assignments = assignmentRepository.findByUser(loggedInUser);
        Grid<Assignment> grid = new Grid<>(Assignment.class);

        // Form components (fields, buttons, etc.)
        TextField assignmentNameField = new TextField("Assignment Name: ");
        ComboBox<String> subjectComboBox = new ComboBox<>("Subject Name");
        subjectComboBox.setItems("Calculus", "English 2", "Physics C");
        DatePicker datePicker = new DatePicker("Date Due");
        NumberField pointsField = new NumberField("Points");
        pointsField.setMin(0);
        pointsField.setMax(100);

        // Submit button
        Button submitButton = new Button("Submit", event -> {
            String assignmentName = assignmentNameField.getValue();
            String subjectName = subjectComboBox.getValue();
            LocalDate dateDue = datePicker.getValue();
            Double points = pointsField.getValue();

            // Validation
            if (assignmentName.isEmpty() || subjectName == null || dateDue == null || points == null) {
                Notification.show("Please fill in all fields");
            } else {
                // Create and save new Assignment
                Assignment newAssignment = new Assignment(assignmentName, subjectName, dateDue, points.intValue(), loggedInUser);
                assignmentRepository.save(newAssignment);

                // Refresh grid with updated data
                grid.setItems(assignmentRepository.findByUser(loggedInUser));

                // Clear form
                clearForm(assignmentNameField, subjectComboBox, datePicker, pointsField);
                Notification.show("Assignment added");
            }
        });

        VerticalLayout formLayout = new VerticalLayout(assignmentNameField, subjectComboBox, datePicker, pointsField, submitButton);
        grid.setColumns("assignmentName", "subjectName", "dateDue", "points");
        grid.setItems(assignments);
        add(grid, formLayout);
    }

    private void clearForm(TextField assignmentNameField, ComboBox<String> subjectComboBox, DatePicker datePicker, NumberField pointsField) {
        assignmentNameField.clear();
        subjectComboBox.clear();
        datePicker.clear();
        pointsField.clear();
    }
}
