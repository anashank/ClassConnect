package com.example.application.views.list;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

import java.awt.*;
import java.util.*;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.component.UI;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Profile")
@Route(value = "test")
@RouteAlias(value = "test")
public class ListViewNew extends VerticalLayout {
    //might make a page dedicated to just global variables but will keep them here for now
String firstName, lastName, currentGrade, email, School;
Grid<Schedule> grid = new Grid<>(Schedule.class);
TextField filterText = new TextField();
List<Schedule> schedule = new ArrayList<>();

    public ListViewNew() {
        H2 title = new H2("User Profile");
        TextField Firstname = new TextField("First Name");
        TextField Lastname = new TextField("Last Name");
        EmailField Email = new EmailField("Email");
        TextField school = new TextField("School");

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


       schedule.add(new Schedule(0, "textfield1", "textfield2"));//example
        // grid.setItems(schedule);
       ScheduleForm example = new ScheduleForm(grid, schedule);
       //example.configureGrid();

        Button addSchedule = new Button("Add Schedule");
        Button edit = new Button("Edit Profile");
        Button done = new Button("Done");
        Button cancel = new Button("Cancel");


        edit.addClickListener(event -> {
            Firstname.setReadOnly(false);
            Lastname.setReadOnly(false);
            Email.setReadOnly(false);
            school.setReadOnly(false);
            grade.setReadOnly(false);
            add(cancel,done);
            remove(edit);

        });

        done.addClickListener(event -> {//adds value to variables when clicked
            firstName = Firstname.getValue();
            lastName = Lastname.getValue();
            email = Email.getValue();
            currentGrade = grade.getValue();
            School = school.getValue();
            add(edit);
            remove(cancel,done);

            Firstname.setReadOnly(true);
            Lastname.setReadOnly(true);
            Email.setReadOnly(true);
            school.setReadOnly(true);
            grade.setReadOnly(true);

           Notification.show("SAVED");//just using this to see if it saves right
        });

        //Make this happened the second time --> double-check the first time
        cancel.addClickListener(event ->{
            Firstname.clear();
            Lastname.clear();
            Email.clear();
            grade.clear();
            school.clear();
            add(edit);
            remove(cancel,done);

            Firstname.setReadOnly(true);
            Lastname.setReadOnly(true);
            Email.setReadOnly(true);
            school.setReadOnly(true);
            grade.setReadOnly(true);

            Notification.show("CLEARED");//Just using this to see if it clears everything
        });

        add(title,horizontalLayout,Lastname, Email, school, grade, example, edit);


//auto saving code
//        Firstname.addValueChangeListener(event -> {
//            // Get the value from the First name TextField
//            firstName = Firstname.getValue();
//        });
//        Lastname.addValueChangeListener(event -> {
//            // Get the value from the Last name TextField
//            lastName = Lastname.getValue();
//        });
//        grade.addValueChangeListener(event ->{
//            currentGrade = event.getValue();
//        });

    }



    //JUST PUTTING IN THE BASIC SETTER/GETTER METHODS FOR NOW, WILL MODIFY AS I GO


}
