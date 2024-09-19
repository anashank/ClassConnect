package com.example.application.views.list;

import com.example.application.repositories.ProfileRepository;
import com.example.application.repositories.ScheduleRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@PermitAll
@Route("profile")
public class ProfileView extends VerticalLayout {

    private final ScheduleRepository scheduleRepository;
    private final ProfileRepository profileRepository;
    private final Grid<Schedule> scheduleGrid = new Grid<>(Schedule.class);
    private boolean isEditing = false;
    private Profile profile;

    // Declare fields as instance variables
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField emailField;
    private TextField schoolField;
    private ComboBox<String> gradeComboBox;

    @Autowired
    public ProfileView(ScheduleRepository scheduleRepository, ProfileRepository profileRepository) {
        this.scheduleRepository = scheduleRepository;
        this.profileRepository = profileRepository;

        profile = getCurrentProfile(); // Method to get the current profile

        H2 title = new H2("User Profile");
        firstNameField = new TextField("First Name");
        lastNameField = new TextField("Last Name");
        emailField = new TextField("Email");
        schoolField = new TextField("School");
        gradeComboBox = new ComboBox<>("Select Grade");
        gradeComboBox.setItems("7th Grade", "8th Grade", "9th Grade", "10th Grade", "11th Grade", "12th Grade");

        Button addScheduleButton = new Button("Add Schedule");
        Button editButton = new Button("Edit Profile");
        Button saveButton = new Button("Save");
        Button cancelButton = new Button("Cancel");

        addScheduleButton.addClickListener(event -> addNewSchedule());
        editButton.addClickListener(event -> toggleEditMode(true));
        saveButton.addClickListener(event -> saveProfile());
        cancelButton.addClickListener(event -> cancelEdit());

        // Set up grid columns
        scheduleGrid.setColumns("className", "teacherName", "period");

        // Define grid editor components
        TextField classField = new TextField("Subject");
        TextField teacherField = new TextField("Teacher's Full Name");

        classField.setPlaceholder("Enter subject");
        teacherField.setPlaceholder("Enter teacher's name");

        setupGridEditor(classField, teacherField);

        HorizontalLayout profileLayout = new HorizontalLayout(firstNameField, lastNameField);
        VerticalLayout formLayout = new VerticalLayout(
                title,
                profileLayout,
                emailField,
                schoolField,
                gradeComboBox,
                scheduleGrid,
                addScheduleButton,
                editButton,
                saveButton,
                cancelButton
        );

        add(formLayout);

        loadProfileData();
    }

    private Profile getCurrentProfile() {
        // Retrieve the current user's profile (e.g., from a session or security context)
        Profile profile = profileRepository.findById(1L).orElse(new Profile());
        if (profile.getId() == null) {
            // If profile is new and not yet saved, save it now
            profileRepository.save(profile);
        }
        return profile;
    }

    private void setupGridEditor(TextField classField, TextField teacherField) {
        // Set up grid editor binder
        Binder<Schedule> binder = new Binder<>(Schedule.class);
        binder.forField(classField)
                .asRequired("Subject is required")
                .withValidator(new StringLengthValidator("Subject name must be between 1 and 100 characters", 1, 100))
                .bind(Schedule::getClassName, Schedule::setClassName);
        binder.forField(teacherField)
                .asRequired("Teacher's name is required")
                .withValidator(new StringLengthValidator("Teacher's name must be between 1 and 100 characters", 1, 100))
                .bind(Schedule::getTeacherName, Schedule::setTeacherName);

        scheduleGrid.getEditor().setBinder(binder);

        // Configure the grid editor
        scheduleGrid.addItemClickListener(event -> {
            if (isEditing) {
                scheduleGrid.getEditor().editItem(event.getItem());
            }
        });

        scheduleGrid.getEditor().addSaveListener(event -> {
            scheduleRepository.save(event.getItem());
            Notification.show("Schedule saved");
        });

        scheduleGrid.getEditor().addCancelListener(event -> {
            Notification.show("Edit canceled");
        });
    }

    private void loadProfileData() {
        // Load and display profile data and schedules from the repository
        List<Schedule> schedules = scheduleRepository.findByProfile(profile);
        scheduleGrid.setItems(schedules);

        // Populate profile fields
        firstNameField.setValue(profile.getFirstName() != null ? profile.getFirstName() : "");
        lastNameField.setValue(profile.getLastName() != null ? profile.getLastName() : "");
        emailField.setValue(profile.getEmail() != null ? profile.getEmail() : "");
        schoolField.setValue(profile.getSchool() != null ? profile.getSchool() : "");
        gradeComboBox.setValue(profile.getGrade() != null ? profile.getGrade() : "");
    }

    private void addNewSchedule() {
        Schedule newSchedule = new Schedule(); // Create a new Schedule with default values
        newSchedule.setProfile(profile); // Link new schedule to the current profile
        scheduleRepository.save(newSchedule);
        loadProfileData(); // Refresh grid to show new schedule
        Notification.show("New schedule added");
    }

    private void saveProfile() {
        // Validate and set profile data
        String email = emailField.getValue();
        profile.setFirstName(firstNameField.getValue());
        profile.setLastName(lastNameField.getValue());
        profile.setEmail(email != null && !email.trim().isEmpty() ? email : null); // Handle null case
        profile.setSchool(schoolField.getValue());
        profile.setGrade(gradeComboBox.getValue());

        profileRepository.save(profile);
        Notification.show("Profile saved");
        toggleEditMode(false);
    }

    private void cancelEdit() {
        // Reset fields to their original values
        firstNameField.setValue(profile.getFirstName() != null ? profile.getFirstName() : "");
        lastNameField.setValue(profile.getLastName() != null ? profile.getLastName() : "");
        emailField.setValue(profile.getEmail() != null ? profile.getEmail() : "");
        schoolField.setValue(profile.getSchool() != null ? profile.getSchool() : "");
        gradeComboBox.setValue(profile.getGrade() != null ? profile.getGrade() : "");

        toggleEditMode(false);
        Notification.show("Edit canceled");
    }

    private void toggleEditMode(boolean isEditing) {
        this.isEditing = isEditing;
        scheduleGrid.getEditor().getBinder().getFields().forEach(field -> field.setReadOnly(!isEditing));
    }
}
