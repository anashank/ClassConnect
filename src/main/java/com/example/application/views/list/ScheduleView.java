package com.example.application.views.list;

import com.example.application.repositories.GroupRepository;
import com.example.application.services.UserDetailsServiceImpl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.vaadin.stefan.fullcalendar.CalendarViewImpl;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;
import org.vaadin.stefan.fullcalendar.dataprovider.InMemoryEntryProvider;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@PermitAll
@Route("schedule")
public class ScheduleView extends VerticalLayout {
    private UserDetailsServiceImpl databaseService;
    private GroupRepository groupRepository;
    private List<UserForm> allusers;

    // Fields for group creation
    private TextField groupNameField = new TextField("Group Name");
    private TextField subjectField = new TextField("Subject");
    private ComboBox<String> publicViewComboBox = new ComboBox<>("Group Visibility");
    private DatePicker datePicker = new DatePicker("Select Date");
    private TextField timeField = new TextField("Select Time");

    private VerticalLayout userListLayout = new VerticalLayout();
    private List<UserForm> selectedUsers = new ArrayList<>();

    public ScheduleView(UserDetailsServiceImpl databaseService, GroupRepository groupRepository) {
        this.databaseService = databaseService;
        this.groupRepository = groupRepository;
        allusers = this.databaseService.findAllUsers();

        H2 title = new H2("Schedule Group");

        // Initialize public/private view options
        publicViewComboBox.setItems("Public", "Private");
        publicViewComboBox.setPlaceholder("Select Visibility");

        Button addUserButton = new Button("Add User", event -> openUserDialog());
        Button createGroupButton = new Button("Create Group", event -> createGroup());

        FullCalendar calendar = FullCalendarBuilder.create().build();
        calendar.setWidth("700px");
        calendar.setHeight("500px");
        calendar.changeView(CalendarViewImpl.DAY_GRID_MONTH);
        calendar.addClassName("full-calendar");

        Entry entry = new Entry();
        entry.setTitle("Sample Event");
        entry.setStart(LocalDate.of(2024, 8, 11));
        entry.setEnd(LocalDate.of(2024, 8, 12));

        InMemoryEntryProvider<Entry> entryProvider = new InMemoryEntryProvider<>();
        calendar.setEntryProvider(entryProvider);
        entryProvider.addEntry(entry);
        calendar.setSizeFull();

        VerticalLayout calendarContainer = new VerticalLayout(calendar);
        calendarContainer.setWidth("700px");
        calendarContainer.setHeight("500px");
        calendarContainer.getStyle().set("border", "1px solid #ccc");

        add(title, groupNameField, subjectField, publicViewComboBox, datePicker, timeField, addUserButton, userListLayout, createGroupButton, calendarContainer);
    }

    private void openUserDialog() {
        Dialog userDialog = new Dialog();
        TextField usernameField = new TextField("Username");

        Button confirmButton = new Button("Add User", event -> {
            String username = usernameField.getValue();
            UserForm user = loadUserByUsername(username);
            if (user != null) {
                selectedUsers.add(user);
                userListLayout.add(new Button(username)); // Show added username
                usernameField.clear(); // Clear input field after adding
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
        groupRepository.save(newGroup); // Save the group to the database
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
