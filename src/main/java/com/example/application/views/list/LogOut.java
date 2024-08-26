package com.example.application.views.list;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;

@PermitAll
@Route("logout")
//@AnonymousAllowed
public class LogOut extends VerticalLayout {

    public LogOut() {
        Button logoutButton = new Button("Log Out", event -> {
            // Invalidate the session to log out the user
            VaadinSession.getCurrent().getSession().invalidate();
            // Redirect to login page
            getUI().ifPresent(ui -> ui.getPage().setLocation("/login"));
        });

        // Add the button to the layout
        add(logoutButton);
    }
}