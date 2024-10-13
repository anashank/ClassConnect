package com.example.application.views.list;

import com.example.application.repositories.UserMatchRepository;
import com.example.application.repositories.UserRepository;
import com.example.application.views.list.CompatibilityScorer;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
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

import java.util.List;

@PermitAll
@Route("rec") // defines URL for this view
public class UserMatchView extends AppLayout { // Extend AppLayout

    private final CompatibilityScorer compatibilityScorer;
    private final UserRepository userRepository;
    private final UserMatchRepository userMatchRepository;

    private Grid<UserMatch> matchGrid;

    @Autowired
    public UserMatchView(CompatibilityScorer compatibilityScorer, UserRepository userRepository, UserMatchRepository userMatchRepository) {
        this.compatibilityScorer = compatibilityScorer;
        this.userRepository = userRepository;
        this.userMatchRepository = userMatchRepository;

        createContent();
        configureNavbar();
        configureDrawer();
    }

    private void createContent() {
        // Fetch the current logged-in user
        UserForm currentUser = getCurrentUser();

        if (currentUser != null) {
            List<UserMatch> matches = compatibilityScorer.findBestMatches(currentUser.getId());

            // Initialize the grid for displaying matches
            matchGrid = new Grid<>(UserMatch.class);
            matchGrid.setColumns("recommendedUser.username", "score"); // Displaying username and score

            // Add matches to the grid
            matchGrid.setItems(matches);

            // Save matches to the database if they are not already saved
            if (!matches.isEmpty()) {
                for (UserMatch match : matches) {
                    UserMatch userMatch = new UserMatch(currentUser, match.getUser(), match.getScore());
                    userMatchRepository.save(userMatch); // Save each match to the new database
                }
            }
        }

        // Add the grid to a layout and set it as the content
        VerticalLayout contentLayout = new VerticalLayout(matchGrid);
        setContent(contentLayout);
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
        H1 title = new H1("User Matches");
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
}
