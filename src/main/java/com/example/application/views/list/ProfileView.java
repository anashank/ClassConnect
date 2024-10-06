package com.example.application.views.list;

import com.example.application.repositories.GroupRepository;
import com.example.application.repositories.ProfileRepository;
import com.example.application.repositories.ScheduleRepository;
import com.example.application.services.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@PermitAll
@Route("profile")
public class ProfileView extends AppLayout {

    private final ScheduleRepository scheduleRepository;
    private final ProfileRepository profileRepository;
    private final GroupRepository groupRepository;
    private final UserService userService;
    private final UserForm currentUser;

    // Grid for displaying study groups and schedules
    private final Grid<Groups> studyGroupGrid = new Grid<>(Groups.class);
    private final Grid<Schedule> scheduleGrid = new Grid<>(Schedule.class);

    @Autowired
    public ProfileView(ScheduleRepository scheduleRepository, ProfileRepository profileRepository,
                       GroupRepository groupRepository, UserService userService) {
        this.scheduleRepository = scheduleRepository;
        this.profileRepository = profileRepository;
        this.groupRepository = groupRepository;
        this.userService = userService;

        // Get the currently logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        // Use userService to get the current user with joined groups
        currentUser = userService.getUserWithJoinedGroups(username);

        // Set up the UI components
        createContent();
        createNavBar();
    }

    private void createNavBar() {
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("Profile");

        title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");

        SideNav nav = getSideNav();

        Scroller scroller = new Scroller(nav);
        addToDrawer(scroller);
        addToNavbar(toggle, title);
        setPrimarySection(Section.DRAWER);
    }

    private SideNav getSideNav() {
        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Dashboard", "/dashboard", VaadinIcon.DASHBOARD.create()),
                new SideNavItem("Profile", "/profile", VaadinIcon.USER.create()),
                new SideNavItem("Assignments", "/assignments", VaadinIcon.LIST.create()),
                new SideNavItem("Subjects", "/subjects", VaadinIcon.RECORDS.create()),
                new SideNavItem("Groups", "/creategroup", VaadinIcon.CALENDAR.create()),
                new SideNavItem("Location", "/location", VaadinIcon.LIST.create()),
                new SideNavItem("Friends", "/friends", VaadinIcon.USER_HEART.create()),
                new SideNavItem("Messages", "/messages", VaadinIcon.MAILBOX.create()));
        return nav;
    }

    private void createContent() {
        VerticalLayout profileLayout = createProfileLayout();
        VerticalLayout scheduleLayout = createScheduleLayout();
        VerticalLayout studyGroupLayout = createStudyGroupLayout();

        // Set main content layout
        VerticalLayout mainLayout = new VerticalLayout(profileLayout, scheduleLayout, studyGroupLayout);
        setContent(mainLayout);
    }

    private VerticalLayout createProfileLayout() {
        TextField firstNameField = new TextField("First Name");
        TextField lastNameField = new TextField("Last Name");
        TextField emailField = new TextField("Email");
        TextField schoolField = new TextField("School");
        ComboBox<String> gradeComboBox = new ComboBox<>("Select Grade");
        TextField city = new TextField("City");
        TextField state = new TextField("State");
        gradeComboBox.setItems("7th Grade", "8th Grade", "9th Grade", "10th Grade", "11th Grade", "12th Grade");

        loadProfileData(firstNameField, lastNameField, emailField, schoolField, gradeComboBox,city,state);

        Button saveButton = new Button("Save Profile", event -> {
            saveProfile(firstNameField, lastNameField, emailField, schoolField, gradeComboBox,city, state);
            Notification.show("Profile saved");
        });

        return new VerticalLayout(firstNameField, lastNameField, emailField, schoolField, gradeComboBox, city,state, saveButton);
    }

    private VerticalLayout createScheduleLayout() {
        setupScheduleGrid();

        TextField classNameField = new TextField("Class Name");
        TextField teacherNameField = new TextField("Teacher Name");
        IntegerField periodField = new IntegerField("Period");

        Button addScheduleButton = new Button("Add Schedule", event -> {
            addSchedule(classNameField, teacherNameField, periodField);
            clearScheduleForm(classNameField, teacherNameField, periodField);
        });

        return new VerticalLayout(classNameField, teacherNameField, periodField, addScheduleButton, scheduleGrid);
    }

    private VerticalLayout createStudyGroupLayout() {
        setupStudyGroupGrid();
        return new VerticalLayout(studyGroupGrid);
    }

    private void setupScheduleGrid() {
        scheduleGrid.setColumns("className", "teacherName", "period");
        scheduleGrid.addComponentColumn(this::createDeleteButton).setHeader("Actions");
        loadSchedulesForUser(currentUser);
    }

    private void setupStudyGroupGrid() {
        studyGroupGrid.setColumns("groupName", "subject", "date", "time");
        loadJoinedStudyGroups();
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

    private void loadProfileData(TextField firstNameField, TextField lastNameField, TextField emailField, TextField schoolField, ComboBox<String> gradeComboBox, TextField city, TextField state) {
        profileRepository.findByUser(currentUser).ifPresent(profile -> {
            firstNameField.setValue(profile.getFirstName());
            lastNameField.setValue(profile.getLastName());
            emailField.setValue(profile.getEmail());
            schoolField.setValue(profile.getSchool());
            gradeComboBox.setValue(profile.getGrade());
            city.setValue(profile.getCity());
            state.setValue(profile.getState());
        });
    }

    private void loadSchedulesForUser(UserForm user) {
        List<Schedule> schedules = scheduleRepository.findByUser(user);
        scheduleGrid.setItems(schedules);
    }

    private void loadJoinedStudyGroups() {
        List<Groups> joinedGroups = groupRepository.findByUsernamesContaining(currentUser.getUsername());
        studyGroupGrid.setItems(joinedGroups);
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

    private void saveProfile(TextField firstNameField, TextField lastNameField, TextField emailField, TextField schoolField, ComboBox<String> gradeComboBox,TextField city, TextField state) {
        Profile profile = profileRepository.findByUser(currentUser).orElse(new Profile());
        profile.setFirstName(firstNameField.getValue());
        profile.setLastName(lastNameField.getValue());
        profile.setEmail(emailField.getValue());
        profile.setSchool(schoolField.getValue());
        profile.setGrade(gradeComboBox.getValue());
        profile.setCity(city.getValue());
        profile.setState(state.getValue());
        profile.setUser(currentUser);

        profileRepository.save(profile);
    }

    private void clearScheduleForm(TextField classNameField, TextField teacherNameField, IntegerField periodField) {
        classNameField.clear();
        teacherNameField.clear();
        periodField.clear();
    }
}
