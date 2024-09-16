package com.example.application.views.list;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;

@PermitAll
@Route("friends")
public class ProfileView extends AppLayout {

    private TextField firstNameField;
    private TextField lastNameField;
    private TextField emailField;
    private TextField schoolField;
    private TextField birthdayField;
    private Button saveButton;
    private VerticalLayout contentLayout = new VerticalLayout();
    private Profile profile;

    public ProfileView() {

        // Create Navbar and Drawer
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("Friends");
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

        createFriendsConnect();

        setContent(contentLayout);
    }

    private void createFriendsConnect() {
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

        contentLayout.add(firstNameField, lastNameField, emailField, schoolField, saveButton);
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

    private void saveProfile() {
        profile.setFirstName(firstNameField.getValue());
        profile.setLastName(lastNameField.getValue());
        profile.setEmail(emailField.getValue());
        profile.setSchool(schoolField.getValue());
        // Add more fields as necessary
        // Implement saving logic (e.g., save to a database or update a list)
    }


}
