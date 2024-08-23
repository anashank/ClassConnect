package com.example.application.views.list;

import com.example.application.views.list.Schedule;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.dependency.CssImport;
import java.util.ArrayList;
import java.util.List;

public class ScheduleForm extends VerticalLayout {
    List<Schedule> schedule;

    Grid<Schedule> grid;
    public ScheduleForm(Grid<Schedule> sche, List<Schedule> wee) {
        grid = sche;
        schedule = wee;

        grid.setItems(schedule);
        grid.removeAllColumns();

        Grid.Column<Schedule> periodColumn = grid.addColumn(Schedule::getPeriod).setHeader("Period");
        Grid.Column<Schedule> classColumn = grid.addColumn(Schedule::getClassName).setHeader("Subject");
        Grid.Column<Schedule> teacherColumn = grid.addColumn(Schedule::getTeacherName).setHeader("Teacher's Full Name");

        Binder<Schedule> binder = new Binder<>(Schedule.class);
        Editor<Schedule> editor = grid.getEditor();
        editor.setBinder(binder);

        TextField classCol = new TextField();
        classCol.setPlaceholder("Placeholder");
        binder.forField(classCol)
                .asRequired("Subject is required")
                .withValidator(new StringLengthValidator("Subject's name must be between 1 and 100 characters", 1, 100))
                .bind(Schedule::getClassName, Schedule::setClassName);
        classColumn.setEditorComponent(classCol);

        TextField teacherCol = new TextField();
        binder.forField(teacherCol)
                .asRequired("Teacher's name is required")
                .withValidator(new StringLengthValidator("Teacher's name must be between 1 and 100 characters", 1, 100))
                .bind(Schedule::getTeacherName, Schedule::setTeacherName);
        teacherColumn.setEditorComponent(teacherCol);

        grid.addItemClickListener(event -> {
                    editor.editItem(event.getItem());
                    classCol.focus();
//            Schedule clickedItem = event.getItem();
//            int rowIndex = schedule.indexOf(clickedItem);
//            schedule.get(rowIndex).setClassName(String.valueOf(classCol.getPlaceholder()));
//            System.out.println(classCol.getPlaceholder());
//            System.out.println(schedule.get(rowIndex));
        });

        grid.addItemClickListener(event -> {
            editor.editItem(event.getItem());
            teacherCol.focus();
//            Schedule clickedItem = event.getItem();
//            int rowIndex = schedule.indexOf(clickedItem);
//            schedule.get(rowIndex).setTeacherName(String.valueOf(teacherCol));
        });

        editor.addSaveListener(event -> {
            try {
                binder.writeBean(event.getItem());
            } catch (ValidationException e) { //issue is not here
                Notification.show("issue is here");
                e.printStackTrace();
            }
        });

        editor.addCancelListener(event -> editor.cancel());

        // Add a button to save changes
        Button saveButton = new Button("Save", e -> editor.save());
        //*******************PUT IN CODE TO SAVE THE INFORMATION TO THE SCHEDULE ARRAYLIST***************************************************************
        saveButton.addClassName("save-button");

        add(grid,saveButton);
    }
    public void configureGrid() {
        grid.addClassName("Schedule");
        grid.setColumns("period", "className", "teacherName"); //<-- the error
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

    }

}
