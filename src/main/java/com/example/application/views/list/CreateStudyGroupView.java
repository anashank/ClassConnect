package com.example.application.views.list;

import com.example.application.services.StudyGroupService;
import com.example.application.repositories.UserRepository; // Import the UserRepository
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.Optional;

@PermitAll
@Route("create-group")
public class CreateStudyGroupView extends VerticalLayout {

    private final StudyGroupService studyGroupService;
    private final UserRepository userRepository; // Add UserRepository to fetch current user

    @Autowired
    public CreateStudyGroupView(StudyGroupService studyGroupService, UserRepository userRepository) {
        this.studyGroupService = studyGroupService;
        this.userRepository = userRepository; // Initialize the UserRepository

        TextField nameField = new TextField("Group Name");
        TextField subjectField = new TextField("Subject");
        DatePicker datePicker = new DatePicker("Date");
        TextField spotsField = new TextField("Spots Available");

        Button createButton = new Button("Create Group", event -> {
            String name = nameField.getValue();
            String subject = subjectField.getValue();
            LocalDate date = datePicker.getValue();
            String spotsText = spotsField.getValue();

            if (name.isEmpty() || subject.isEmpty() || date == null || spotsText.isEmpty()) {
                Notification.show("All fields are required!", 3000, Notification.Position.MIDDLE);
                return;
            }

            int spotsLeft;
            try {
                spotsLeft = Integer.parseInt(spotsText);
                if (spotsLeft <= 0) {
                    throw new NumberFormatException("Spots must be a positive number.");
                }
            } catch (NumberFormatException e) {
                Notification.show("Invalid number of spots! Please enter a valid positive integer.", 3000, Notification.Position.MIDDLE);
                return;
            }

            // Get the currently logged-in user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();


            Optional<UserForm> userOpt = userRepository.findByUsername(auth.getName());
            UserForm creator = userOpt.orElseThrow(() -> new RuntimeException("User not found: " + auth.getName()));

            // Create the new StudyGroup
            StudyGroup newGroup = new StudyGroup();
            newGroup.setName(name);
            newGroup.setSubject(subject);
            newGroup.setDate(date);
            newGroup.setSpotsLeft(spotsLeft);
            newGroup.setCreator(creator); // Set the creator of the study group

            // Save the new group
            studyGroupService.save(newGroup);
            Notification.show("Study group created successfully!", 3000, Notification.Position.MIDDLE);

            // Clear the form after successful creation
            nameField.clear();
            subjectField.clear();
            datePicker.clear();
            spotsField.clear();
        });

        FormLayout formLayout = new FormLayout(nameField, subjectField, datePicker, spotsField, createButton);
        add(formLayout);
    }
}
