package com.example.application.views.list;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@PermitAll
@Route("location")
public class LocationView extends VerticalLayout {
//comment
    private Span latitudeLabel = new Span();
    private Span longitudeLabel = new Span();
    private Span locationLabel = new Span();
    private TextField manualLocationField = new TextField("Enter Location Manually");
    private Button submitManualLocationButton = new Button("Submit Manual Location");

    private final ObjectMapper objectMapper = new ObjectMapper();

    public LocationView() {
        // Set up the UI
        Button getLocationButton = new Button("Find Location Automatically", event -> getCurrentLocation());

        submitManualLocationButton.addClickListener(event -> handleManualLocation());

//        latitudeLabel.setText("Latitude: Not Available");
//        longitudeLabel.setText("Longitude: Not Available");
        locationLabel.setText("Location: Not Available");

        // Create layout for manual location
        HorizontalLayout manualLocationLayout = new HorizontalLayout();
        manualLocationLayout.setAlignItems(Alignment.BASELINE); // Align items to baseline
        manualLocationLayout.add(manualLocationField, submitManualLocationButton);
        manualLocationLayout.setSpacing(false); // Remove spacing between items

        // Add components to the main layout
        Div locationContainer = new Div(latitudeLabel, longitudeLabel, locationLabel);
        //locationContainer.setPadding(false); // Optional: adjust padding as needed

        add(getLocationButton, manualLocationLayout, locationContainer);
        setSpacing(true); // Add spacing between components if needed
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

//                // Update the UI with the location
//                latitudeLabel.setText("Latitude: " + latitude);
//                longitudeLabel.setText("Longitude: " + longitude);

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
            // Process the manual location (this would be where you'd do additional processing)
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
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body());

            // Extract city and state
            JsonNode addressNode = jsonNode.path("address");
            String city = addressNode.path("city").asText();
            String state = addressNode.path("state").asText();

            // Format result
            String result = String.format("%s, %s", city.isEmpty() ? "Not Available" : city, state.isEmpty() ? "Not Available" : state);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to retrieve address";
        }
    }
}
