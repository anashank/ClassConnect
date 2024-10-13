package com.example.application.views.list;

import com.example.application.views.list.Profile;
import com.example.application.repositories.ProfileRepository;
import com.example.application.repositories.UserRepository;
import com.example.application.views.list.UserForm;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@PermitAll
@Route("location")
public class LocationView extends AppLayout {

    private final Span latitudeLabel = new Span();
    private final Span longitudeLabel = new Span();
    private final Span locationLabel = new Span();
    private final TextField manualLocationField = new TextField("Enter Location Manually");
    private final Button submitManualLocationButton = new Button("Submit Manual Location");

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    public LocationView() {
        // Set up the navigation bar
        createNavBar();

        // Set up the UI components
        Button getLocationButton = new Button("Find Location Automatically", event -> getCurrentLocation());

        submitManualLocationButton.addClickListener(event -> handleManualLocation());
        locationLabel.setText("Location: Not Available");

        // Create layout for manual location
        HorizontalLayout manualLocationLayout = new HorizontalLayout();
        manualLocationLayout.setAlignItems(FlexComponent.Alignment.BASELINE);
        manualLocationLayout.add(manualLocationField, submitManualLocationButton);
        manualLocationLayout.setSpacing(false);

        // Create a container for displaying the location information
       // Div locationContainer = new Div(latitudeLabel, longitudeLabel, locationLabel);
        Div locationContainer = new Div(locationLabel);

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
                new SideNavItem("Recommendations", "/rec", VaadinIcon.RECORDS.create()),
                new SideNavItem("Groups", "/creategroup", VaadinIcon.CALENDAR.create()),
                new SideNavItem("Location", "/location", VaadinIcon.LOCATION_ARROW.create()),
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
                JsonNode jsonNode = objectMapper.readTree(result.asString());
                double latitude = jsonNode.get("latitude").asDouble();
                double longitude = jsonNode.get("longitude").asDouble();

                latitudeLabel.setText("Latitude: " + latitude);
                longitudeLabel.setText("Longitude: " + longitude);

                String location = getCityAndStateFromCoordinates(latitude, longitude);
                locationLabel.setText("Location: " + location);

                String[] parts = location.split(", ");
                if (parts.length == 2) {
                    String city = parts[0];
                    String state = parts[1];
                    saveLocationToProfile(city, state); // Save location to profile
                }

                Notification.show("Location retrieved successfully");
            } catch (Exception e) {
                Notification.show("Failed to parse location data");
            }
        }));
    }

    private void handleManualLocation() {
        String manualLocation = manualLocationField.getValue();

        if (manualLocation.isEmpty()) {
            Notification.show("Please enter a valid location.");
        } else {
            locationLabel.setText("Location: " + manualLocation);

            String[] parts = manualLocation.split(", ");
            if (parts.length == 2) {
                String city = parts[0].trim();
                String state = parts[1].trim();
                saveLocationToProfile(city, state); // Save location to profile
            } else {
                Notification.show("Please enter a valid format: City, State.");
            }
        }
    }

    private void saveLocationToProfile(String city, String state) {
        UserForm currentUser = getCurrentUser();

        if (currentUser != null) {
            Optional<Profile> profileOpt = profileRepository.findByUser(currentUser);
            if (profileOpt.isPresent()) {
                Profile profile = profileOpt.get();
                profile.setCity(city);
                profile.setState(state);
                profileRepository.save(profile); // Save updated profile
                Notification.show("Location saved to profile.");
            } else {
                Notification.show("Profile not found for the user.");
            }
        } else {
            Notification.show("User not logged in.");
        }
    }

    private UserForm getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            System.out.println("Authentication is not null");
            System.out.println("Principal: " + authentication.getPrincipal());
            if (authentication.getPrincipal() instanceof User) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String username = userDetails.getUsername();
                UserForm userformobj = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
                return(userformobj);
            }
        }
        System.out.println("Authentication is null or principal is not UserForm");
        return null;
    }


    private String getCityAndStateFromCoordinates(double latitude, double longitude) {
        try {
            String urlString = String.format("https://nominatim.openstreetmap.org/reverse?lat=%f&lon=%f&format=json&addressdetails=1", latitude, longitude);
            URI uri = new URI(urlString);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode jsonNode = objectMapper.readTree(response.body());

            JsonNode addressNode = jsonNode.path("address");
            String city = addressNode.path("city").asText();
            String state = addressNode.path("state").asText();

            return String.format("%s, %s", city.isEmpty() ? "Not Available" : city, state.isEmpty() ? "Not Available" : state);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to retrieve address";
        }
    }
}
