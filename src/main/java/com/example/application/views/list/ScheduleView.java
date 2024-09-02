package com.example.application.views.list;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.vaadin.stefan.fullcalendar.Entry;
import org.vaadin.stefan.fullcalendar.FullCalendar;
import org.vaadin.stefan.fullcalendar.FullCalendarBuilder;
import org.vaadin.stefan.fullcalendar.dataprovider.InMemoryEntryProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@PermitAll
@Route("schedule")
public class ScheduleView extends VerticalLayout {

    public ScheduleView() {

        // Creating the input fields
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


        add(calendarContainer, datePicker, timeField, eventNameField, submitButton);
    }
}