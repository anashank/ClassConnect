package com.example.application.views.list;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
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

@PermitAll
@Route("schedule")
public class ScheduleView extends VerticalLayout {

    public ScheduleView() {

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


        add(calendarContainer,datePicker,timeField,submitButton);
    }
}
