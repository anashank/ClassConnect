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
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.List;

@PermitAll
@Route("friends")
public class FriendsView extends AppLayout {
    DrawerToggle toggle = new DrawerToggle();

    SideNav nav = getSideNav();

    Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

    addToDrawer(scroller);
    addToNavbar(toggle);
    //might make a page dedicated to just global variables but will keep them here for now
    String firstName, lastName, currentGrade, email, School;
    Grid<Schedule> grid = new Grid<>(Schedule.class);
    TextField filterText = new TextField();
    public List<Schedule> schedule = new ArrayList<>();
    private UserDetailsServiceImpl databaseService;

    public FriendsView(UserDetailsServiceImpl databaseService) {
        H2 title = new H2("User Profile");
        TextField Firstname = new TextField("First Name");
        TextField Lastname = new TextField("Last Name");
        EmailField Email = new EmailField("Email");
        TextField school = new TextField("School");
        Friendslide test = new Friendslide(databaseService);
        ProfilePictureView test2 = new ProfilePictureView();
        //        this.databaseService = new UserDetailsServiceImpl();

        Firstname.setReadOnly(true);
        Lastname.setReadOnly(true);
        Email.setReadOnly(true);
        school.setReadOnly(true);

        ComboBox<String> grade = new ComboBox<>("Select a grade: ");
        grade.setItems("7th Grade", "8th Grade", "9th Grade", "10th Grade", "11th Grade", "12th Grade");
        //THESE ARE EXAMPLES WILL PUT IN MORE OPTIONS LATER
        grade.setReadOnly(true);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(Firstname, Lastname);


        schedule.add(new Schedule(0, "Subject Name Goes Here", "Teacher's Name Goes here"));//example
        ScheduleForm example = new ScheduleForm(grid, schedule);
        //example.configureGrid();
        H3 gridLabel = new H3("Schedule");

        Button addSchedule = new Button("Add Schedule");
        Button edit = new Button("Edit Profile");
        Button done = new Button("Done");
        Button cancel = new Button("Cancel");
        Button addButton = new Button("Add New Row", e -> example.addNewRow());
        Button removeButton = new Button("Remove a Row", e -> example.removeRow());


        edit.addClickListener(event -> {
            Firstname.setReadOnly(false);
            Lastname.setReadOnly(false);
            Email.setReadOnly(false);
            school.setReadOnly(false);
            grade.setReadOnly(false);
            example.editGrid();

            add(addButton, removeButton, cancel, done);
            remove(edit);

        });

        done.addClickListener(event -> {//adds value to variables when clicked
            if (!schedule.isEmpty()) {
                firstName = Firstname.getValue();
                lastName = Lastname.getValue();
                email = Email.getValue();
                currentGrade = grade.getValue();
                School = school.getValue();
                add(edit);
                remove(addButton, removeButton, cancel, done);

                Firstname.setReadOnly(true);
                Lastname.setReadOnly(true);
                Email.setReadOnly(true);
                school.setReadOnly(true);
                grade.setReadOnly(true);
                example.saveGrid();
                example.noEditGrid();

                Notification.show("SAVED");
            } else {
                Notification.show("Need atleast one subject!");
            }
        });

        //Make this happened the second time --> double-check the first time
        cancel.addClickListener(event -> {
            Firstname.clear();
            Lastname.clear();
            Email.clear();
            grade.clear();
            school.clear();
            example.cancelGrid();
            add(edit);
            remove(addButton, removeButton, cancel, done);

            Firstname.setReadOnly(true);
            Lastname.setReadOnly(true);
            Email.setReadOnly(true);
            school.setReadOnly(true);
            grade.setReadOnly(true);
            example.noEditGrid();


            Notification.show("CLEARED");//Just using this to see if it clears everything
        });

        add(title,toggle, nav, horizontalLayout, Lastname, Email, school, grade, gridLabel, example, edit, test);
    }
    private SideNav getSideNav() {
        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Dashboard", "/dashboard",
                        VaadinIcon.DASHBOARD.create()),
                new SideNavItem("Profile", "/profile",
                        VaadinIcon.USER.create()),
                new SideNavItem("Assignments", "/assignments",
                        VaadinIcon.LIST.create()),
                new SideNavItem("Subjects", "/subjects",
                        VaadinIcon.RECORDS.create()),
                new SideNavItem("Schedule", "/schedule",
                        VaadinIcon.CALENDAR.create()),
                new SideNavItem("Location", "/location",
                        VaadinIcon.LIST.create()),
                new SideNavItem("Friends", "/friends",
                        VaadinIcon.USER_HEART.create()),
                new SideNavItem("Messages", "/messages",
                        VaadinIcon.MAILBOX.create()));
        return nav;
    }


}
