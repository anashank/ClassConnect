package com.example.application.views.list;

import com.example.application.repositories.ProfileRepository;
import com.example.application.repositories.ScheduleRepository;
import com.example.application.repositories.UserRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@PermitAll
@Route("profile")
public class ProfileView extends VerticalLayout {

    private final ScheduleRepository scheduleRepository;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private UserForm currentUser; // Make sure this is a class-level variable

    @Autowired
    public ProfileView(ScheduleRepository scheduleRepository, ProfileRepository profileRepository, UserRepository userRepository) {
        this.scheduleRepository = scheduleRepository;
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;

        // Get the currently logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        // Fetch the user by username and assign it to currentUser
        currentUser = userRepository.findByUsername(username);
        if (currentUser == null) {
            throw new RuntimeException("User not found: " + username);
        }

        // Initialize profile form components
        TextField firstNameField = new TextField("First Name");
        TextField lastNameField = new TextField("Last Name");
        TextField emailField = new TextField("Email");
        TextField schoolField = new TextField("School");
        ComboBox<String> gradeComboBox = new ComboBox<>("Select Grade");
        gradeComboBox.setItems("7th Grade", "8th Grade", "9th Grade", "10th Grade", "11th Grade", "12th Grade");

        // Load existing profile
        loadProfileData(firstNameField, lastNameField, emailField, schoolField, gradeComboBox);

        // Submit button for profile
        Button saveButton = new Button("Save Profile", event -> {
            saveProfile(firstNameField, lastNameField, emailField, schoolField, gradeComboBox);
            Notification.show("Profile saved");
        });

        // Initialize schedule grid
        Grid<Schedule> scheduleGrid = new Grid<>(Schedule.class);
        scheduleGrid.setColumns("className", "teacherName", "period");
        scheduleGrid.addComponentColumn(schedule -> createDeleteButton(schedule, scheduleGrid))
                .setHeader("Actions");

        loadSchedulesForUser(currentUser, scheduleGrid);

        // Form components for adding a new schedule
        TextField classNameField = new TextField("Class Name");
        TextField teacherNameField = new TextField("Teacher Name");
        IntegerField periodField = new IntegerField("Period");

        // Submit button for schedule
        Button addScheduleButton = new Button("Add Schedule", event -> {
            addSchedule(classNameField, teacherNameField, periodField, scheduleGrid); // Pass the scheduleGrid
            clearScheduleForm(classNameField, teacherNameField, periodField); // Clear form
        });

        // Layouts
        VerticalLayout profileLayout = new VerticalLayout(firstNameField, lastNameField, emailField, schoolField, gradeComboBox, saveButton);
        VerticalLayout scheduleLayout = new VerticalLayout(classNameField, teacherNameField, periodField, addScheduleButton, scheduleGrid);

        add(profileLayout, scheduleLayout);
    }

    private Button createDeleteButton(Schedule schedule, Grid<Schedule> scheduleGrid) {
        Button deleteButton = new Button("Delete");
        deleteButton.addClickListener(event -> {
            scheduleRepository.delete(schedule); // Delete the schedule from the repository
            Notification.show("Schedule deleted");
            loadSchedulesForUser(currentUser, scheduleGrid); // Refresh the grid
        });
        return deleteButton;
    }

    private void loadProfileData(TextField firstNameField, TextField lastNameField, TextField emailField, TextField schoolField, ComboBox<String> gradeComboBox) {
        Profile profile = profileRepository.findByUser(currentUser).orElse(null);
        if (profile != null) {
            firstNameField.setValue(profile.getFirstName());
            lastNameField.setValue(profile.getLastName());
            emailField.setValue(profile.getEmail());
            schoolField.setValue(profile.getSchool());
            gradeComboBox.setValue(profile.getGrade());
        }
    }

    private void loadSchedulesForUser(UserForm user, Grid<Schedule> scheduleGrid) {
        List<Schedule> schedules = scheduleRepository.findByUser(user);
        scheduleGrid.setItems(schedules);
    }

    private void addSchedule(TextField classNameField, TextField teacherNameField, IntegerField periodField, Grid<Schedule> scheduleGrid) {
        Schedule newSchedule = new Schedule();
        newSchedule.setClassName(classNameField.getValue());
        newSchedule.setTeacherName(teacherNameField.getValue());
        newSchedule.setPeriod(periodField.getValue());
        newSchedule.setUser(currentUser); // Set the current user directly

        scheduleRepository.save(newSchedule); // Save the schedule
        Notification.show("New schedule added!");

        // Refresh the schedule grid
        loadSchedulesForUser(currentUser, scheduleGrid);
    }

    private void saveProfile(TextField firstNameField, TextField lastNameField, TextField emailField, TextField schoolField, ComboBox<String> gradeComboBox) {
        Profile profile = profileRepository.findByUser(currentUser).orElse(new Profile());
        profile.setFirstName(firstNameField.getValue());
        profile.setLastName(lastNameField.getValue());
        profile.setEmail(emailField.getValue());
        profile.setSchool(schoolField.getValue());
        profile.setGrade(gradeComboBox.getValue());
        profile.setUser(currentUser); // Associate the profile with the user

        profileRepository.save(profile); // Save the profile
    }

    private void clearScheduleForm(TextField classNameField, TextField teacherNameField, IntegerField periodField) {
        classNameField.clear();
        teacherNameField.clear();
        periodField.clear();
    }
}
