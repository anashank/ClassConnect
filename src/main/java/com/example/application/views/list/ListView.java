package com.example.application.views.list;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@PermitAll
@PageTitle("list")
@Route(value = "profile")

public class ListView extends VerticalLayout {
    private Button sayHello;
    public ListView() {

        sayHello = new Button("Say hello to new user");
        add(sayHello);
        addLoggedInUser();

    }
    private void addLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        TextField loggedInUser = new TextField("Logged in as:");
        loggedInUser.setValue(currentUserName);
        loggedInUser.setReadOnly(true);
        loggedInUser.setValue(currentUserName);
        loggedInUser.setReadOnly(true);

        add(loggedInUser);
    }
}
