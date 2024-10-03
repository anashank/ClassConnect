package com.example.application.views.list;

import com.example.application.services.UserDetailsServiceImpl;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

@PermitAll
@Route("friends")
public class FriendsView extends AppLayout {

    // Variables
    private String firstName, lastName, currentGrade, email, school;
    private Grid<Schedule> grid = new Grid<>(Schedule.class);
    private TextField filterText = new TextField();
    private List<Schedule> scheduleList = new ArrayList<>();
    private UserDetailsServiceImpl databaseService;
    private VerticalLayout contentLayout = new VerticalLayout(); // Container for the content

    public FriendsView(UserDetailsServiceImpl databaseService) {
        this.databaseService = databaseService;

        // Create Navbar and Drawer
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("Profile");
        TextField loggedInUser = addLoggedInUser();
        Button logoutButton = addLogoutButton();
        loggedInUser.getStyle().set("margin-left", "auto");
        logoutButton.getStyle().set("margin-left", "auto");

        SideNav nav = getSideNav();
        Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        // Add to layout
        addToDrawer(scroller);
        addToNavbar(toggle, title, loggedInUser, logoutButton);

        // Create content for the Friends view
        createFriendsContent();

        // Set the content of the AppLayout
        setContent(contentLayout);
    }

    // Adds the current logged-in user to the navbar
    private TextField addLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        TextField loggedInUser = new TextField("Logged in as:");
        loggedInUser.setValue(currentUserName);
        loggedInUser.setReadOnly(true);
        return loggedInUser;
    }

    // Adds the logout button to the navbar
    private Button addLogoutButton() {
        Button logoutButton = new Button("Log Out", event -> {
            VaadinSession.getCurrent().getSession().invalidate();
            getUI().ifPresent(ui -> ui.getPage().setLocation("/login"));
        });
        return logoutButton;
    }

    // Navigation drawer items
    private SideNav getSideNav() {
        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Dashboard", "/dashboard", VaadinIcon.DASHBOARD.create()));
        nav.addItem(new SideNavItem("Profile", "/profile", VaadinIcon.USER.create()));
        nav.addItem(new SideNavItem("Assignments", "/assignments", VaadinIcon.LIST.create()));
        nav.addItem(new SideNavItem("Subjects", "/subjects", VaadinIcon.RECORDS.create()));
        nav.addItem(new SideNavItem("Schedule", "/schedule", VaadinIcon.CALENDAR.create()));
        nav.addItem(new SideNavItem("Location", "/location", VaadinIcon.MAP_MARKER.create()));
        nav.addItem(new SideNavItem("Friends", "/friends", VaadinIcon.USER_HEART.create()));
        nav.addItem(new SideNavItem("Messages", "/messages", VaadinIcon.MAILBOX.create()));
        return nav;
    }

    // The content of the Friends view
    private void createFriendsContent() {
        H2 title = new H2("User Profile");
        TextField firstNameField = new TextField("First Name");
        TextField lastNameField = new TextField("Last Name");
        EmailField emailField = new EmailField("Email");
        TextField schoolField = new TextField("School");

        firstNameField.setReadOnly(true);
        lastNameField.setReadOnly(true);
        emailField.setReadOnly(true);
        schoolField.setReadOnly(true);

        ComboBox<String> gradeComboBox = new ComboBox<>("Select a grade:");
        gradeComboBox.setItems("7th Grade", "8th Grade", "9th Grade", "10th Grade", "11th Grade", "12th Grade");
        gradeComboBox.setReadOnly(true);

        HorizontalLayout profileInfo = new HorizontalLayout(firstNameField, lastNameField, emailField, schoolField);

        // Initialize the Schedule grid and form
//        grid.setColumns("className", "teacherName", "period");
//        scheduleList.add(new Schedule("Subject Name Goes Here", "Teacher's Name Goes Here", 1));
//        ScheduleForm scheduleForm = new ScheduleForm(grid, scheduleList);

        H3 gridLabel = new H3("Schedule");

//        Button addSchedule = new Button("Add Schedule");
//        Button editProfile = new Button("Edit Profile");
//        Button done = new Button("Done");
//        Button cancel = new Button("Cancel");
//        Button addRowButton = new Button("Add New Row", e -> scheduleForm.addNewRow());
//        Button removeRowButton = new Button("Remove a Row", e -> scheduleForm.removeRow());

//        editProfile.addClickListener(event -> {
//            firstNameField.setReadOnly(false);
//            lastNameField.setReadOnly(false);
//            emailField.setReadOnly(false);
//            schoolField.setReadOnly(false);
//            gradeComboBox.setReadOnly(false);
//            contentLayout.add(addRowButton, removeRowButton, cancel, done); // Add buttons to the layout
//            contentLayout.remove(editProfile); // Remove the editProfile button
//        });
//
//        done.addClickListener(event -> {
//            firstName = firstNameField.getValue();
//            lastName = lastNameField.getValue();
//            email = emailField.getValue();
//            school = schoolField.getValue();
//            contentLayout.add(editProfile); // Add the editProfile button back
//            contentLayout.remove(addRowButton, removeRowButton, cancel, done); // Remove editing buttons
//            firstNameField.setReadOnly(true);
//            lastNameField.setReadOnly(true);
//            emailField.setReadOnly(true);
//            schoolField.setReadOnly(true);
//            gradeComboBox.setReadOnly(true);
//            Notification.show("Profile updated successfully");
//        });
//
//        cancel.addClickListener(event -> {
//            firstNameField.clear();
//            lastNameField.clear();
//            emailField.clear();
//            gradeComboBox.clear();
//            schoolField.clear();
//            contentLayout.add(editProfile); // Add the editProfile button back
//            contentLayout.remove(addRowButton, removeRowButton, cancel, done); // Remove editing buttons
//            firstNameField.setReadOnly(true);
//            lastNameField.setReadOnly(true);
//            emailField.setReadOnly(true);
//            schoolField.setReadOnly(true);
//            gradeComboBox.setReadOnly(true);
//            Notification.show("Profile changes canceled");
//        });

        // Add components to the layout
        //contentLayout.add(title, profileInfo, gridLabel, grid, editProfile);
        contentLayout.add(title, profileInfo, gridLabel, grid);
    }
}