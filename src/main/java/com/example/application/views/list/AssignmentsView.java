package com.example.application.views.list;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@PermitAll
@Route("assignments")
public class AssignmentsView extends AppLayout {
    private VerticalLayout contentLayout = new VerticalLayout();

    public AssignmentsView() {
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("Assignments");
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

        createAssignConnect();

        setContent(contentLayout);



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
    private void createAssignConnect() {
        List<Assignment> assignments = new ArrayList<>();
        assignments.add(new Assignment("Math", "Calculus", LocalDate.of(2024, 7, 22), 10));
        assignments.add(new Assignment("English", "English 2", LocalDate.of(2024, 7, 22), 20));
        assignments.add(new Assignment("Physics", "Physics C", LocalDate.of(2024, 7, 22), 30));


        Grid<Assignment> grid = new Grid<>(Assignment.class);



        TextField assignmentNameField = new TextField("Assignment Name: ");

        ComboBox<String> subjectComboBox = new ComboBox<>("Subject Name");
        subjectComboBox.setItems("Calculus", "English 2", "Physics C");

        DatePicker datePicker = new DatePicker("Date Due");
        NumberField pointsField = new NumberField("Points");

        pointsField.setMin(0);
        pointsField.setMax(100);

        Button submitButton = new Button("Submit", event -> {
            String assignmentName = assignmentNameField.getValue();
            String subjectName = subjectComboBox.getValue();
            LocalDate dateDue = datePicker.getValue();
            Double points = pointsField.getValue();

            if (assignmentName.isEmpty() || subjectName == null || dateDue == null || points == null) {
                Notification.show("Please fill in all fields");
            } else {
                Assignment newAssignment = new Assignment(assignmentName, subjectName, dateDue, points.intValue());
                assignments.add(newAssignment);
                grid.setItems(assignments);
                clearForm(assignmentNameField, subjectComboBox, datePicker, pointsField);
                Notification.show("Assignment added");
            }
        });

        VerticalLayout formLayout = new VerticalLayout(assignmentNameField, subjectComboBox, datePicker, pointsField, submitButton);



        grid.setColumns("assignmentName", "subjectName", "dateDue", "points");
        grid.setItems(assignments);
        contentLayout.add(grid, formLayout);
    }

    private void clearForm(TextField assignmentNameField, ComboBox<String> subjectComboBox, DatePicker datePicker, NumberField pointsField) {
        assignmentNameField.clear();
        subjectComboBox.clear();
        datePicker.clear();
        pointsField.clear();
    }
}

