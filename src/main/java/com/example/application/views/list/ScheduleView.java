package com.example.application.views.list;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
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
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;
import org.vaadin.stefan.fullcalendar.dataprovider.InMemoryEntryProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@PermitAll
@Route("schedule")
public class ScheduleView extends AppLayout {
    private VerticalLayout contentLayout = new VerticalLayout();

    public ScheduleView() {
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("Schedule");
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

        createSchedConnect();

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

    private void createSchedConnect() {
        DatePicker datePicker = new DatePicker("Select Date");
        TextField timeField = new TextField("Select Time (HH:MM)");
        TextField eventNameField = new TextField("Event Name");

        // Creating the calendar
        FullCalendar calendar = FullCalendarBuilder.create().build();
        calendar.setWidth("90vw");
        calendar.setHeight("70vh");

        // Customize the calendar
        calendar.getElement().executeJs("this.calendar.setOption('locale', 'en');"); // Set locale to English
        calendar.getElement().executeJs(
                "this.calendar.setOption('dayHeaderFormat', { weekday: 'long' });" + // Long format for weekdays
                        "this.calendar.setOption('titleFormat', { year: 'numeric', month: 'long' });" + // Long format for months in title
                        "this.calendar.setOption('weekNumbers', false);" + // Hide week numbers
                        "this.calendar.setOption('dayCellContent', function(date, cell) { " +
                        "   cell.innerHTML = '<div style=\"text-align: left; padding: 2px;\">' + date.day + '</div>'; " +
                        "});" + // Position the dates in the top-left corner
                        "this.calendar.setOption('headerToolbar', {" +
                        "   left: 'prev,next today'," +
                        "   center: 'title'," +
                        "   right: 'dayGridMonth'" +
                        "});" // Enable month switching and display the month title above the calendar
        );

        // Initialize the entry provider
        InMemoryEntryProvider<Entry> entryProvider = new InMemoryEntryProvider<>();
        calendar.setEntryProvider(entryProvider);

        // Adding a sample event
        Entry sampleEntry = new Entry();
        sampleEntry.setTitle("Sample Event");
        sampleEntry.setStart(LocalDate.of(2024, 8, 11).atStartOfDay());
        sampleEntry.setEnd(LocalDate.of(2024, 8, 12).atStartOfDay());
        entryProvider.addEntry(sampleEntry);

        // Button to submit the new event
        Button submitButton = new Button("Submit", event -> {
            LocalDate selectedDate = datePicker.getValue();
            String selectedTime = timeField.getValue();
            String eventName = eventNameField.getValue();

            try {
                LocalTime time = LocalTime.parse(selectedTime);
                LocalDateTime startDateTime = LocalDateTime.of(selectedDate, time);

                Entry newEntry = new Entry();
                newEntry.setTitle(eventName);
                newEntry.setStart(startDateTime);
                newEntry.setEnd(startDateTime.plusHours(1)); // Adjust duration as needed

                entryProvider.addEntry(newEntry); // Add the new entry to the calendar

            } catch (Exception e) {
                timeField.setInvalid(true);
                timeField.setErrorMessage("Invalid time format. Please use HH:MM.");
            }
        });

        // Add the components to the layout
        VerticalLayout calendarContainer = new VerticalLayout(calendar);
        calendarContainer.setWidth("90vw");
        calendarContainer.setHeight("70vh");

        calendarContainer.addClassName("calendar-container");


        contentLayout.add(calendarContainer, datePicker, timeField, eventNameField, submitButton);
    }
    }


    // Creating the input fields
