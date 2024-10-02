//package com.example.application.views.list;
//
//import com.example.application.views.list.Schedule;
//import com.example.application.views.list.UserForm;
//import com.example.application.repositories.ScheduleRepository;
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.formlayout.FormLayout;
//import com.vaadin.flow.component.grid.Grid;
//import com.vaadin.flow.component.notification.Notification;
//import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
//import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.data.binder.Binder;
//
//import java.util.List;
//
//public class ScheduleForm extends VerticalLayout {
//    private ScheduleRepository scheduleRepository;
//    private UserForm currentUser;
//    private Grid<Schedule> grid = new Grid<>(Schedule.class);
//    private Binder<Schedule> binder = new Binder<>(Schedule.class);
//
//    private TextField classNameField = new TextField("Class Name");
//    private TextField teacherNameField = new TextField("Teacher Name");
//    private TextField periodField = new TextField("Period");
//
//    public ScheduleForm(ScheduleRepository scheduleRepository, UserForm currentUser) {
//        this.scheduleRepository = scheduleRepository;
//        this.currentUser = currentUser;
//
//        // Configure grid
//        grid.setColumns("className", "teacherName", "period");
//        grid.addComponentColumn(schedule -> {
//            Button deleteButton = new Button("Delete", clickEvent -> deleteSchedule(schedule));
//            return deleteButton;
//        });
//
//        // Create form layout for adding new schedule entries
//        FormLayout scheduleFormLayout = new FormLayout();
//        Button addScheduleButton = new Button("Add Schedule", clickEvent -> addSchedule());
//
//        scheduleFormLayout.add(classNameField, teacherNameField, periodField, addScheduleButton);
//
//        // Add grid and form to the layout
//        add(scheduleFormLayout, grid);
//
//        loadSchedulesForUser(currentUser);
//    }
//
//    public void loadSchedulesForUser(UserForm user) {
//        List<Schedule> schedules = scheduleRepository.findByUser(user);
//        grid.setItems(schedules);
//    }
//
//    public void saveSchedules() {
//        List<Schedule> scheduleList = grid.getDataProvider().fetch(new com.vaadin.flow.data.provider.Query<>()).toList();
//        for (Schedule schedule : scheduleList) {
//            schedule.setUser(currentUser);  // Associate the current user
//            scheduleRepository.save(schedule);
//        }
//        Notification.show("Schedules saved successfully!");
//    }
//
//    private void addSchedule() {
//        // Create a new Schedule entity and set its values
//        Schedule schedule = new Schedule();
//        schedule.setClassName(classNameField.getValue());
//        schedule.setTeacherName(teacherNameField.getValue());
//        schedule.setPeriod(Integer.parseInt(periodField.getValue())); // Assuming period is an integer
//        schedule.setUser(currentUser);  // Link to the current user
//
//        // Save the schedule to the database
//        scheduleRepository.save(schedule);
//
//        // Clear input fields after saving
//        classNameField.clear();
//        teacherNameField.clear();
//        periodField.clear();
//
//        // Reload the grid to reflect the new schedule
//        loadSchedulesForUser(currentUser);
//
//        Notification.show("New schedule added!");
//    }
//
//    private void deleteSchedule(Schedule schedule) {
//        scheduleRepository.delete(schedule);
//        loadSchedulesForUser(currentUser);  // Reload after deletion
//        Notification.show("Schedule deleted");
//    }
//}
