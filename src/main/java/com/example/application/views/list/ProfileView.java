package com.example.application.views.list;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import com.vaadin.flow.component.notification.Notification;

@Route("profile/:username")
public class ProfileView extends VerticalLayout implements BeforeEnterObserver {
    private TextField usernameField = new TextField("Username");

    private UserDetailsManager userDetailsManager;

    @Autowired
    public ProfileView(UserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
        add(usernameField);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String username = event.getRouteParameters().get("username").orElse("");
        UserDetails userDetails = userDetailsManager.loadUserByUsername(username);
        if (userDetails != null) {
            usernameField.setValue(userDetails.getUsername());
        } else {
            Notification.show("User not found!");
        }
    }
}
