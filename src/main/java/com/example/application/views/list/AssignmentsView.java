package com.example.application.views.list;

import com.example.application.repositories.AssignmentRepository;
import com.example.application.repositories.UserRepository;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@PermitAll
@Route("assignments") // defines URL for this view
@CssImport("./styles/styles.css")
public class AssignmentsView extends AppLayout { // Extend AppLayout

    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    @Autowired
    public AssignmentsView(AssignmentRepository assignmentRepository, UserRepository userRepository) {
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;

        createContent();
        configureNavbar();
        configureDrawer();
    }

    private void createContent() {
        // Fetch logged-in user and their assignments
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<UserForm> userOpt = userRepository.findByUsername(username);
        UserForm loggedInUser = userOpt.orElseThrow(() -> new RuntimeException("User not found: " + username));

        List<Assignment> assignments = assignmentRepository.findByUser(loggedInUser);
        Grid<Assignment> grid = new Grid<>(Assignment.class);
        grid.setItems(assignments);

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

        // Layout for the form and grid
        VerticalLayout formLayout = new VerticalLayout(assignmentNameField, subjectComboBox, datePicker, pointsField, submitButton);
        grid.setColumns("assignmentName", "subjectName", "dateDue", "points");

        // Add grid and form to the layout
        setContent(new VerticalLayout(grid, formLayout));
    }

    private void configureNavbar() {
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("Assignments");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");

        TextField loggedInUser = addLoggedInUser();
        Button logoutButton = addLogoutButton();
        loggedInUser.getStyle().set("margin-left", "auto");
        logoutButton.getStyle().set("margin-left", "auto");

        addToNavbar(toggle, title, loggedInUser, logoutButton);
    }

    private TextField addLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        TextField loggedInUser = new TextField("Logged in as:");
        loggedInUser.setValue(currentUserName);
        loggedInUser.setReadOnly(true);
        return loggedInUser;
    }

    private Button addLogoutButton() {
        Button logoutButton = new Button("Log Out", event -> {
            VaadinSession.getCurrent().getSession().invalidate();
            getUI().ifPresent(ui -> ui.getPage().setLocation("/login"));
        });
        return logoutButton;
    }

    private void configureDrawer() {
        SideNav nav = getSideNav();
        Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        addToDrawer(scroller);
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

    private void clearForm(TextField assignmentNameField, ComboBox<String> subjectComboBox, DatePicker datePicker, NumberField pointsField) {
        assignmentNameField.clear();
        subjectComboBox.clear();
        datePicker.clear();
        pointsField.clear();
    }
}
