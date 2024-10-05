package com.example.application.views.list;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@PermitAll
@Route("location")
public class LocationView extends AppLayout {

    private Span latitudeLabel = new Span();
    private Span longitudeLabel = new Span();
    private Span locationLabel = new Span();
    private TextField manualLocationField = new TextField("Enter Location Manually");
    private Button submitManualLocationButton = new Button("Submit Manual Location");

    private final ObjectMapper objectMapper = new ObjectMapper();

    public LocationView() {
        // Set up the navigation bar
        createNavBar();

        // Set up the UI components
        Button getLocationButton = new Button("Find Location Automatically", event -> getCurrentLocation());

        submitManualLocationButton.addClickListener(event -> handleManualLocation());
        locationLabel.setText("Location: Not Available");

        // Create layout for manual location
        HorizontalLayout manualLocationLayout = new HorizontalLayout();
        manualLocationLayout.setAlignItems(FlexComponent.Alignment.BASELINE); // Align items to baseline
        manualLocationLayout.add(manualLocationField, submitManualLocationButton);
        manualLocationLayout.setSpacing(false); // Remove spacing between items

        // Create a container for displaying the location information
        Div locationContainer = new Div(latitudeLabel, longitudeLabel, locationLabel);

        // Set main content layout
        VerticalLayout mainLayout = new VerticalLayout(getLocationButton, manualLocationLayout, locationContainer);
        setContent(mainLayout);
    }

    private void createNavBar() {
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("Location");

        title.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");

        SideNav nav = getSideNav();

        Scroller scroller = new Scroller(nav);
        addToDrawer(scroller);
        addToNavbar(toggle, title);
        setPrimarySection(Section.DRAWER);
    }

    private SideNav getSideNav() {
        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Dashboard", "/dashboard", VaadinIcon.DASHBOARD.create()),
                new SideNavItem("Profile", "/profile", VaadinIcon.USER.create()),
                new SideNavItem("Assignments", "/assignments", VaadinIcon.LIST.create()),
                new SideNavItem("Subjects", "/subjects", VaadinIcon.RECORDS.create()),
                new SideNavItem("Groups", "/creategroup", VaadinIcon.CALENDAR.create()),
                new SideNavItem("Location", "/location", VaadinIcon.LIST.create()),
                new SideNavItem("Friends", "/friends", VaadinIcon.USER_HEART.create()),
                new SideNavItem("Messages", "/messages", VaadinIcon.MAILBOX.create()));
        return nav;
    }

    private void getCurrentLocation() {
        getUI().ifPresent(ui -> ui.getPage().executeJs(
                "return new Promise((resolve, reject) => {" +
                        "    if (navigator.geolocation) {" +
                        "        navigator.geolocation.getCurrentPosition(position => {" +
                        "            resolve(JSON.stringify({ latitude: position.coords.latitude, longitude: position.coords.longitude }));" +
                        "        }, error => {" +
                        "            reject(error.message);" +
                        "        });" +
                        "    } else {" +
                        "        reject('Geolocation is not supported by this browser.');" +
                        "    }" +
                        "});"
        ).then(result -> {
            try {
                // Parse result to JsonNode using Jackson
                JsonNode jsonNode = objectMapper.readTree(result.asString());
                double latitude = jsonNode.get("latitude").asDouble();
                double longitude = jsonNode.get("longitude").asDouble();

                // Get the city and state from the coordinates
                String location = getCityAndStateFromCoordinates(latitude, longitude);
                locationLabel.setText("Location: " + location);

                Notification.show("Location retrieved successfully");
            } catch (Exception e) {
                Notification.show("Failed to parse location data");
            }
        }));
    }

    private void handleManualLocation() {
        // Get the user input for manual location
        String manualLocation = manualLocationField.getValue();

        if (manualLocation.isEmpty()) {
            Notification.show("Please enter a valid location.");
        } else {
            Notification.show("Manual location entered: " + manualLocation);
            locationLabel.setText("Location: " + manualLocation);
        }
    }

    private String getCityAndStateFromCoordinates(double latitude, double longitude) {
        try {
            // Construct the URL for Nominatim API
            String urlString = String.format("https://nominatim.openstreetmap.org/reverse?lat=%f&lon=%f&format=json&addressdetails=1", latitude, longitude);
            URI uri = new URI(urlString);

            // Create HttpClient and HttpRequest
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            // Send request and get response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse JSON response
            JsonNode jsonNode = objectMapper.readTree(response.body());

            // Extract city and state
            JsonNode addressNode = jsonNode.path("address");
            String city = addressNode.path("city").asText();
            String state = addressNode.path("state").asText();

            // Format result
            return String.format("%s, %s", city.isEmpty() ? "Not Available" : city, state.isEmpty() ? "Not Available" : state);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to retrieve address";
        }
    }
}
