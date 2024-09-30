package com.example.application.views.list;

import com.example.application.services.UserDetailsServiceImpl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.vaadin.stefan.fullcalendar.CalendarViewImpl;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;
import org.vaadin.stefan.fullcalendar.dataprovider.InMemoryEntryProvider;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@PermitAll
@Route("schedule")
public class ScheduleView extends VerticalLayout {
    UserDetailsServiceImpl databaseService;
    Groups group;
    List<UserForm> allusers;
    UserForm user1;
    UserForm user2;

    public ScheduleView(){
        //databaseService = new UserDetailsServiceImpl();
       group = new Groups();
       //allusers = this.databaseService.findAllUsers();
        H2 title = new H2("Schedule Group");
        TextField groupName = new TextField("Group Name");
        TextField subject = new TextField("Subject");

        DatePicker datePicker = new DatePicker("Select Date");
        TextField timeField = new TextField("Select Time");

        Button submitButton = new Button("Submit", event -> {
            String selectedDate = datePicker.getValue().toString();
            String selectedTime = timeField.getValue();
            // Handle selected date and time
        });

        FullCalendar calendar = FullCalendarBuilder.create().build();
        calendar.setWidth("700px");
        calendar.setHeight("500px");


        calendar.changeView(CalendarViewImpl.DAY_GRID_MONTH);

        calendar.addClassName("full-calendar");
        Entry entry = new Entry();
        entry.setTitle("Sample Event");
        entry.setStart(LocalDate.of(2024, 8, 11));
        entry.setEnd(LocalDate.of(2024, 8, 12));

        calendar.getElement().executeJs(
                "this.calendar.setOption('locale', 'en');" +
                "this.calendar.setOption('dayHeaderFormat', { weekday: 'long' });"
        );

        InMemoryEntryProvider<Entry> entryProvider = new InMemoryEntryProvider<>();
        calendar.setEntryProvider(entryProvider);
        entryProvider.addEntry(entry);
        calendar.setSizeFull();

        VerticalLayout calendarContainer = new VerticalLayout(calendar);
        calendarContainer.setWidth("700px");
        calendarContainer.setHeight("500px");
        calendarContainer.getStyle().set("border", "1px solid #ccc"); // Optional: Add border for better visibility

        Button createGroupButton = new Button("Create Group");
        createGroupButton.addClickListener(event -> openGroupCreationDialog());
        add(title, groupName, subject ,calendarContainer,datePicker,timeField,createGroupButton,submitButton);



    }

    private void openGroupCreationDialog() {
        // Create a dialog for group creation
        Dialog groupDialog = new Dialog();

        TextField usernameField = new TextField("Username");

        ComboBox<String> publicView = new ComboBox<>("Would you like your study group public or private?");
        publicView.setItems("Public", "Private");
        if(publicView.toString().equals("Public")){
            group.setStudyGroupPublicity(true);
        }
        else{
            group.setStudyGroupPublicity(false);
        }

        Button addUserButton = new Button("Add User");

        VerticalLayout userListLayout = new VerticalLayout();
        userListLayout.add(usernameField); // Add the first field

        // Add click listener to dynamically add more user input fields
        addUserButton.addClickListener(event -> {
            TextField newUsernameField = new TextField("Username");
            userListLayout.add(newUsernameField);
        });

        // Confirm button to create the group
        Button confirmButton = new Button("Confirm", event -> {
            List<UserForm> users = new ArrayList<UserForm>();
            userListLayout.getChildren().forEach(component -> {
                if (component instanceof TextField) {
                    TextField field = (TextField) component;
                    String username = field.getValue();

//                    UserForm user = loadUserByUsername(username);
//                    if (user != null) {
//                        users.add(user);
//                    }
                }
            });
            group.setNewGroup(users);
            groupDialog.close();
        });

        // Cancel button to close the dialog
        Button cancelButton = new Button("Cancel", event -> groupDialog.close());

        groupDialog.add(userListLayout, addUserButton, confirmButton, cancelButton);
        groupDialog.open();
    }

    public UserForm loadUserByUsername(String username){
        for(int x = 0; x<allusers.size();x++){
            if(allusers.get(x).getUsername().equals(username)){
                return allusers.get(x);
            }
        }
        return null;
    }


    }

