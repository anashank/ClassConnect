package com.example.application.views.list;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@PermitAll
// Empty '@Route("") means it's the default class that gets loaded
@Route("dashboard") // defines url
// found at http://localhost:8080/app-layout-navbar-placement-side
@CssImport("./styles/styles.css")

// tag::snippet[]
public class AppLayoutNavbarPlacementSide extends AppLayout {

    public AppLayoutNavbarPlacementSide() {
        createContent();

        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("Dashboard");
        TextField luser = addLoggedInUser();
        Button logoutButton = addLogoutButton();
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");
        //move the luser field to the right corner of the layout
        luser.getStyle().set("margin-left", "auto");
        logoutButton.getStyle().set("margin-left", "auto");

        SideNav nav = getSideNav();

        Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        addToDrawer(scroller);
        addToNavbar(toggle, title, luser,logoutButton);

        setPrimarySection(Section.DRAWER);
    }

    private TextField addLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        TextField loggedInUser = new TextField("Logged in as:");
        loggedInUser.setValue(currentUserName);
        loggedInUser.setReadOnly(true);
        loggedInUser.setValue(currentUserName);
        loggedInUser.setReadOnly(true);
        return(loggedInUser);
        
    }

    private Button addLogoutButton(){
        Button logoutButton = new Button("Log Out", event -> {
            // Invalidate the session to log out the user
            VaadinSession.getCurrent().getSession().invalidate();
            // Redirect to login page
            getUI().ifPresent(ui -> ui.getPage().setLocation("/login"));
        });
        return(logoutButton);
    }


    // end::snippet[]

    private SideNav getSideNav() {
        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Dashboard", "/dashboard",
                        VaadinIcon.DASHBOARD.create()),
                new SideNavItem("Profile", "/profile",
                        VaadinIcon.USER.create()),
                new SideNavItem("Assignments", "/assignments",
                        VaadinIcon.LIST.create()),
                new SideNavItem("Recommendations", "/rec",
                        VaadinIcon.RECORDS.create()),
                new SideNavItem("Groups", "/groups",
                        VaadinIcon.CALENDAR.create()),
                new SideNavItem("Location", "/location",
                        VaadinIcon.LOCATION_ARROW.create()),
                new SideNavItem("Friends", "/friends",
                        VaadinIcon.USER_HEART.create()),
                new SideNavItem("Messages", "/messages",
                        VaadinIcon.MAILBOX.create()));
        return nav;
    }


    private void createContent() {
        VerticalLayout content = new VerticalLayout();

        Button locationButton = new Button("Location");
        Button friendsButton = new Button("Friends");
        Button profileButton = new Button("Profile");
        Button assignmentsButton = new Button("Assignments");
//        Button subjectsButton = new Button("Recommendations");
//        Button scheduleButton = new Button("Groups");
        Button groupButton = new Button("Groups");

        locationButton.addClassName("top-button");
        friendsButton.addClassName("top-button");

        profileButton.addClassName("huge-middle-button");
        assignmentsButton.addClassName("large-middle-button");
        groupButton.addClassName("large-middle-button");


//        subjectsButton.addClassName("bottom-button");
//        scheduleButton.addClassName("bottom-button");

        // Add click listeners to direct to pages
        locationButton.addClickListener(event -> UI.getCurrent().navigate("location"));
        friendsButton.addClickListener(event -> UI.getCurrent().navigate("friends"));
        assignmentsButton.addClickListener(event -> UI.getCurrent().navigate("assignments"));
        profileButton.addClickListener(event -> UI.getCurrent().navigate("profile"));
//        scheduleButton.addClickListener(event -> UI.getCurrent().navigate("creategroup"));
//        subjectsButton.addClickListener(event-> UI.getCurrent().navigate("rec"));
        groupButton.addClickListener(event -> UI.getCurrent().navigate("groups"));

        // Arrange the layout as per the second image
        HorizontalLayout topRow = new HorizontalLayout(locationButton, friendsButton);
//        HorizontalLayout bottomRow = new HorizontalLayout(subjectsButton, scheduleButton);

        content.add(topRow, profileButton, assignmentsButton, groupButton);
        setContent(content);
    }
    
    // tag::snippet[]
}
// end::snippet[]
