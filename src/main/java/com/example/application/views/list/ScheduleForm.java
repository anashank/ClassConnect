package com.example.application.views.list;

import com.example.application.repositories.ScheduleRepository;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.StringLengthValidator;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

public class ScheduleForm extends VerticalLayout {

    private final ScheduleRepository scheduleRepository;
    private List<Schedule> scheduleList;
    private TextField classCol;
    private TextField teacherCol;
    private Editor<Schedule> editor;
    private Binder<Schedule> binder;
    private Grid<Schedule> grid;

    @Autowired
    public ScheduleForm(Grid<Schedule> grid, List<Schedule> scheduleList, ScheduleRepository scheduleRepository) {
        this.grid = grid;
        this.scheduleList = scheduleList;
        this.scheduleRepository = scheduleRepository;

        grid.setItems(scheduleList);
        grid.removeAllColumns();

        // Define columns
        Grid.Column<Schedule> periodColumn = grid.addColumn(Schedule::getPeriod).setHeader("Period");
        Grid.Column<Schedule> classColumn = grid.addColumn(Schedule::getClassName).setHeader("Subject");
        Grid.Column<Schedule> teacherColumn = grid.addColumn(Schedule::getTeacherName).setHeader("Teacher's Full Name");

        // Setup Binder and Editor
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

        // Add components to layout
        add(grid);

        // Initialize editing logic
        initializeEditing();
    }

    private void initializeEditing() {
        grid.addItemClickListener(event -> {
            editor.editItem(event.getItem());
            classCol.focus();
        });

        editor.addSaveListener(event -> {
            try {
                binder.writeBean(event.getItem());
                scheduleRepository.save(event.getItem()); // Persist changes to the database
                grid.getDataProvider().refreshItem(event.getItem());
                Notification.show("Schedule saved");
            } catch (ValidationException e) {
                Notification.show("Failed to save schedule");
                e.printStackTrace();
            }
        });

        editor.addCancelListener(event -> {
            Notification.show("Editing cancelled");
        });
    }

    public void addNewRow() {
        Schedule newSchedule = new Schedule();
        scheduleList.add(newSchedule);
        grid.getDataProvider().refreshAll(); // Refresh the grid to show the new row
        editor.editItem(newSchedule); // Start editing the new row
    }

    public void removeRow() {
        if (!scheduleList.isEmpty()) {
            Schedule scheduleToRemove = scheduleList.get(scheduleList.size() - 1);
            scheduleList.remove(scheduleToRemove);
            scheduleRepository.delete(scheduleToRemove); // Delete from the database
            grid.getDataProvider().refreshAll();
            Notification.show("Schedule removed");
        } else {
            Notification.show("At least one subject is required!");
        }
    }

    public void saveGrid() {
        for (int i = 0; i < scheduleList.size(); i++) {
            Schedule schedule = scheduleList.get(i);
            schedule.setPeriod(i); // Update the period or other fields if needed
            scheduleRepository.save(schedule); // Save changes to the database
        }
        grid.getDataProvider().refreshAll();
        Notification.show("All schedules updated");
    }

    public void cancelGrid() {
        editor.cancel();
        classCol.clear();
        teacherCol.clear();
    }
}
