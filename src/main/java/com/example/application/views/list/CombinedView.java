package com.example.application.views.list;

import com.example.application.repositories.GroupRepository;
import com.example.application.repositories.UserMatchRepository;
import com.example.application.repositories.UserRepository;
import com.example.application.services.UserDetailsServiceImpl;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

@PermitAll
@Route("groups")  // Defines URL for this merged view
public class CombinedView extends AppLayout {

    private final CompatibilityScorer compatibilityScorer;
    private final UserRepository userRepository;
    private final UserMatchRepository userMatchRepository;
    private final GroupRepository groupRepository;
    private final UserDetailsServiceImpl databaseService;
    private List<UserForm> allusers;
    private List<UserForm> selectedUsers = new ArrayList<>();

    private Grid<UserMatch> matchGrid;
    private VerticalLayout userListLayout = new VerticalLayout();

    // Fields for group creation
    private TextField groupNameField = new TextField("Group Name");
    private TextField subjectField = new TextField("Subject");
    private ComboBox<String> publicViewComboBox = new ComboBox<>("Group Visibility");
    private DatePicker datePicker = new DatePicker("Select Date");
    private TextField timeField = new TextField("Select Time");

    @Autowired
    public CombinedView(CompatibilityScorer compatibilityScorer, UserRepository userRepository, UserMatchRepository userMatchRepository, GroupRepository groupRepository, UserDetailsServiceImpl databaseService) {
        this.compatibilityScorer = compatibilityScorer;
        this.userRepository = userRepository;
        this.userMatchRepository = userMatchRepository;
        this.groupRepository = groupRepository;
        this.databaseService = databaseService;
        this.allusers = this.databaseService.findAllUsers();

        createContent();
        configureNavbar();
        configureDrawer();
    }

    private void createContent() {
        // Fetch the current logged-in user
        UserForm currentUser = getCurrentUser();

        // Matches Section (left side)
        VerticalLayout matchesLayout = new VerticalLayout();
        if (currentUser != null) {
            List<UserMatch> matches = compatibilityScorer.findBestMatches(currentUser.getId());
            matches.sort((match1, match2) -> Double.compare(match2.getScore(), match1.getScore()));
            // Initialize the grid for displaying matches
            matchGrid = new Grid<>(UserMatch.class);
            matchGrid.setColumns("recommendedUser.username", "score");  // Displaying username and score
            matchGrid.addColumn(userMatch -> matches.indexOf(userMatch) + 1).setHeader("Rank").setAutoWidth(true);
            matchGrid.setItems(matches);  // Add matches to the grid

            if (!matches.isEmpty()) {
                for (UserMatch match : matches) {
                    UserMatch userMatch = new UserMatch(currentUser, match.getUser(), match.getScore());
                    userMatchRepository.save(userMatch);  // Save each match to the database
                }
            }

            matchesLayout.add(matchGrid);
        }

        // Group Creation Section (right side)
        H2 title = new H2("Create Study Group");

        // Initialize public/private view options
        publicViewComboBox.setItems("Public", "Private");
        publicViewComboBox.setPlaceholder("Select Visibility");

        Button addUserButton = new Button("Add User", event -> openUserDialog());
        Button createGroupButton = new Button("Create Group", event -> createGroup());

        VerticalLayout groupCreationLayout = new VerticalLayout(title, groupNameField, subjectField, publicViewComboBox, datePicker, timeField, addUserButton, userListLayout, createGroupButton);

        // Use HorizontalLayout to display matches and group creation side by side
        HorizontalLayout mainLayout = new HorizontalLayout(groupCreationLayout, matchesLayout);
        mainLayout.setSizeFull();
        setContent(mainLayout);
    }

    private UserForm getCurrentUser() {
        // Get the currently logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        return userRepository.findByUsername(username).orElse(null);
    }

    private void configureNavbar() {
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("User Matches & Group Creation");
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
                new SideNavItem("Recommendations", "/rec", VaadinIcon.RECORDS.create()),
                new SideNavItem("Groups", "/creategroup", VaadinIcon.CALENDAR.create()),
                new SideNavItem("Location", "/location", VaadinIcon.LOCATION_ARROW.create()),
                new SideNavItem("Friends", "/friends", VaadinIcon.USER_HEART.create()),
                new SideNavItem("Messages", "/messages", VaadinIcon.MAILBOX.create()));
        return nav;
    }

    private void openUserDialog() {
        Dialog userDialog = new Dialog();
        TextField usernameField = new TextField("Username");

        Button confirmButton = new Button("Add User", event -> {
            String username = usernameField.getValue();
            UserForm user = loadUserByUsername(username);
            if (user != null) {
                selectedUsers.add(user);
                userListLayout.add(new Button(username));  // Show added username
                usernameField.clear();  // Clear input field after adding
            } else {
                Notification.show("User " + username + " not found!", 3000, Notification.Position.MIDDLE);
            }
        });

        Button cancelButton = new Button("Cancel", event -> userDialog.close());

        userDialog.add(usernameField, confirmButton, cancelButton);
        userDialog.open();
    }

    private void createGroup() {
        if (selectedUsers.isEmpty()) {
            Notification.show("No users added to the group!", 3000, Notification.Position.MIDDLE);
            return;
        }

        boolean isPublic = "Public".equals(publicViewComboBox.getValue());
        Groups newGroup = new Groups(selectedUsers, isPublic, groupNameField.getValue(), subjectField.getValue(), datePicker.getValue().toString(), timeField.getValue());
        groupRepository.save(newGroup);  // Save the group to the database
        Notification.show("Group created with users!", 3000, Notification.Position.MIDDLE);
    }

    public UserForm loadUserByUsername(String username) {
        for (UserForm user : allusers) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
