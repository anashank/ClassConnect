package com.example.application.views.list;

import com.example.application.repositories.ProfileRepository;
import com.example.application.repositories.ScheduleRepository;
import com.example.application.services.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@PermitAll
@Route("profile")
public class ProfileView extends VerticalLayout {

    private final ScheduleRepository scheduleRepository;
    private final ProfileRepository profileRepository;
    private final UserService userService;
    private final UserForm currentUser;

    // Grid for displaying study groups and schedules
    private final Grid<StudyGroup> studyGroupGrid = new Grid<>(StudyGroup.class);
    private final Grid<Schedule> scheduleGrid = new Grid<>(Schedule.class);

    @Autowired
    public ProfileView(ScheduleRepository scheduleRepository, ProfileRepository profileRepository, UserService userService) {
        this.scheduleRepository = scheduleRepository;
        this.profileRepository = profileRepository;
        this.userService = userService;

        // Get the currently logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        // Use userService to get the current user with joined groups
        currentUser = userService.getUserWithJoinedGroups(username);

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
        setupScheduleGrid();

        // Form components for adding a new schedule
        TextField classNameField = new TextField("Class Name");
        TextField teacherNameField = new TextField("Teacher Name");
        IntegerField periodField = new IntegerField("Period");

        // Submit button for schedule
        Button addScheduleButton = new Button("Add Schedule", event -> {
            addSchedule(classNameField, teacherNameField, periodField);
            clearScheduleForm(classNameField, teacherNameField, periodField);
        });

        // Initialize study group grid
        setupStudyGroupGrid();

        // Layouts
        VerticalLayout profileLayout = new VerticalLayout(firstNameField, lastNameField, emailField, schoolField, gradeComboBox, saveButton);
        VerticalLayout scheduleLayout = new VerticalLayout(classNameField, teacherNameField, periodField, addScheduleButton, scheduleGrid);
        VerticalLayout studyGroupLayout = new VerticalLayout(studyGroupGrid);

        add(profileLayout, scheduleLayout, studyGroupLayout);
    }

    private void setupScheduleGrid() {
        scheduleGrid.setColumns("className", "teacherName", "period");
        scheduleGrid.addComponentColumn(schedule -> createDeleteButton(schedule))
                .setHeader("Actions");
        loadSchedulesForUser(currentUser);
    }

    private Button createDeleteButton(Schedule schedule) {
        Button deleteButton = new Button("Delete");
        deleteButton.addClickListener(event -> {
            scheduleRepository.delete(schedule);
            Notification.show("Schedule deleted");
            loadSchedulesForUser(currentUser);
        });
        return deleteButton;
    }

    private void loadProfileData(TextField firstNameField, TextField lastNameField, TextField emailField, TextField schoolField, ComboBox<String> gradeComboBox) {
        profileRepository.findByUser(currentUser).ifPresent(profile -> {
            firstNameField.setValue(profile.getFirstName());
            lastNameField.setValue(profile.getLastName());
            emailField.setValue(profile.getEmail());
            schoolField.setValue(profile.getSchool());
            gradeComboBox.setValue(profile.getGrade());
        });
    }

    private void loadSchedulesForUser(UserForm user) {
        List<Schedule> schedules = scheduleRepository.findByUser(user);
        scheduleGrid.setItems(schedules);
    }

    private void setupStudyGroupGrid() {
        studyGroupGrid.setColumns("name", "subject", "date");
        loadJoinedStudyGroups();
    }

    private void loadJoinedStudyGroups() {
        Set<StudyGroup> joinedGroupsSet = currentUser.getJoinedGroups();
        studyGroupGrid.setItems(new ArrayList<>(joinedGroupsSet)); // Convert Set to List and set items in the grid
    }

    private void addSchedule(TextField classNameField, TextField teacherNameField, IntegerField periodField) {
        Schedule newSchedule = new Schedule();
        newSchedule.setClassName(classNameField.getValue());
        newSchedule.setTeacherName(teacherNameField.getValue());
        newSchedule.setPeriod(periodField.getValue());
        newSchedule.setUser(currentUser);

        scheduleRepository.save(newSchedule);
        Notification.show("New schedule added!");
        loadSchedulesForUser(currentUser);
    }

    private void saveProfile(TextField firstNameField, TextField lastNameField, TextField emailField, TextField schoolField, ComboBox<String> gradeComboBox) {
        Profile profile = profileRepository.findByUser(currentUser).orElse(new Profile());
        profile.setFirstName(firstNameField.getValue());
        profile.setLastName(lastNameField.getValue());
        profile.setEmail(emailField.getValue());
        profile.setSchool(schoolField.getValue());
        profile.setGrade(gradeComboBox.getValue());
        profile.setUser(currentUser);

        profileRepository.save(profile);
    }

    private void clearScheduleForm(TextField classNameField, TextField teacherNameField, IntegerField periodField) {
        classNameField.clear();
        teacherNameField.clear();
        periodField.clear();
    }
}
