
package com.example.application.views.list;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.StringLengthValidator;

import java.util.List;

public class ScheduleForm extends VerticalLayout {
    List<Schedule> schedule;
    TextField classCol;
    TextField teacherCol;
    Editor<Schedule> editor;
    Binder<Schedule> binder;

    Grid<Schedule> grid;
    public ScheduleForm(Grid<Schedule> sche, List<Schedule> wee) {
        grid = sche;
        schedule = wee;

        grid.setItems(schedule);
        grid.removeAllColumns();

        Grid.Column<Schedule> periodColumn = grid.addColumn(Schedule::getPeriod).setHeader("Period");
        Grid.Column<Schedule> classColumn = grid.addColumn(Schedule::getClassName).setHeader("Subject");
        Grid.Column<Schedule> teacherColumn = grid.addColumn(Schedule::getTeacherName).setHeader("Teacher's Full Name");

        binder = new Binder<>(Schedule.class);
        editor = grid.getEditor();
        editor.setBinder(binder);

        classCol = new TextField();
        classCol.setPlaceholder("Placeholder");
        binder.forField(classCol)
                .asRequired("Subject is required")
                .withValidator(new StringLengthValidator("Subject's name must be between 1 and 100 characters", 1, 100))
                .bind(Schedule::getClassName, Schedule::setClassName);
        classColumn.setEditorComponent(classCol);

        teacherCol = new TextField();
        binder.forField(teacherCol)
                .asRequired("Teacher's name is required")
                .withValidator(new StringLengthValidator("Teacher's name must be between 1 and 100 characters", 1, 100))
                .bind(Schedule::getTeacherName, Schedule::setTeacherName);
        teacherColumn.setEditorComponent(teacherCol);

        add(grid);
    }

    public void addNewRow() {
        Schedule newSchedule = new Schedule();
        schedule.add(newSchedule);
        grid.getDataProvider().refreshAll(); // Refresh the grid to show the new row
        editor.editItem(newSchedule); // Start editing the new row
    }

    public void removeRow() {
        if (schedule.size() > 1) {
            Schedule removeSchedule = schedule.get(schedule.size() - 1);
            schedule.remove(removeSchedule);
            grid.getDataProvider().refreshAll();
        }
        else{
            Notification.show("Need atleast one subject!");
        }
    }

    public void editGrid(){
        grid.addItemClickListener(innerevent -> {
            editor.editItem(innerevent.getItem());
            classCol.focus();
            //            Schedule clickedItem = event.getItem();
        });

        grid.addItemClickListener(innerevent -> {
            editor.editItem(innerevent.getItem());
            teacherCol.focus();
            //            Schedule clickedItem = event.getItem();

        });

        editor.addSaveListener(event -> {
            try {
                binder.writeBean(event.getItem());
            } catch (ValidationException e) { //issue is not here
                Notification.show("issue is here");
                e.printStackTrace();
            }
        });
    }

    public void noEditGrid(){
        editor.cancel();
        classCol.setReadOnly(true);
        teacherCol.setReadOnly(true);
    }

    public void saveGrid(){
        for(int i = 0; i < schedule.size(); i++){
            schedule.get(i).setClassName(classCol.getValue());
            schedule.get(i).setTeacherName(teacherCol.getValue());
            schedule.get(i).setPeriod(i);
//            System.out.println(schedule.get(i).getClassName());
//            System.out.println(schedule.get(i).getTeacherName());
//            System.out.println(schedule.get(i).getPeriod());
        }
        editor.save();
    }

    public void cancelGrid() {
        editor.cancel();
        classCol.clear();
        teacherCol.clear();
    }



}