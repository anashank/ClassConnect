package com.example.application.views.list;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.time.LocalDate;

@PermitAll
@Route("profile")
public class ProfileView extends VerticalLayout {

    private TextField firstNameField;
    private TextField lastNameField;
    private TextField emailField;
    private TextField schoolField;
    private TextField birthdayField;
    private Button saveButton;

    private Profile profile;

    public ProfileView() {
        profile = new Profile("Banana", "Master", "12th", "banana.master24@example.org", "password123", "Bellarmine", LocalDate.of(2006, 1, 1));

        firstNameField = new TextField("First Name");
        firstNameField.setValue(profile.getFirstName());

        lastNameField = new TextField("Last Name");
        lastNameField.setValue(profile.getLastName());

        emailField = new TextField("Email");
        emailField.setValue(profile.getEmail());

        schoolField = new TextField("School");
        schoolField.setValue(profile.getSchool());

        // birthdayField = new TextField("Birthday");
        // birthdayField.setValue(profile.getBirthday().toString());

        saveButton = new Button("Save");
        saveButton.addClickListener(e -> saveProfile());

        ProfilePictureView profile_picture_view = new ProfilePictureView();

        VerticalLayout contentLayout = new VerticalLayout (
                profile_picture_view,
                firstNameField,
                lastNameField,
                emailField,
                schoolField,
                saveButton
        );

        contentLayout.addClassName("profile-layout");

        add(contentLayout);
    }

    private void saveProfile() {
        profile.setFirstName(firstNameField.getValue());
        profile.setLastName(lastNameField.getValue());
        profile.setEmail(emailField.getValue());
        profile.setSchool(schoolField.getValue());
        // Add more fields as necessary
        // Implement saving logic (e.g., save to a database or update a list)
    }
}
