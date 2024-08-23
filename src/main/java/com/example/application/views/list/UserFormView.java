package com.example.application.views.list;

import com.example.application.services.UserDetailsServiceImpl;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;
import org.springframework.transaction.annotation.Transactional;

@PermitAll
@Route("user-form")
@AnonymousAllowed
public class UserFormView extends VerticalLayout {

    private final UserDetailsServiceImpl service;
    private TextField username;
    private PasswordField password;
    private TextField email; // Optional: If you want to collect email
    private Button submitButton;
    private Binder<UserForm> binder;



    public UserFormView(UserDetailsServiceImpl service) {
        this.service = service;

        username = new TextField("Username");
        password = new PasswordField("Password");
        email = new TextField("Email"); // Optional

        submitButton = new Button("Submit");

        // Initialize binder
        binder = new Binder<>(UserForm.class);

        // Bind form fields to UserForm class properties
        binder.bindInstanceFields(this);

        // Handle form submission
        submitButton.addClickListener(event -> handleSubmit());

        // Layout
        FormLayout formLayout = new FormLayout();

        formLayout.addFormItem(username, "").getElement().getStyle().set("width", "100%");
        formLayout.addFormItem(password, "").getElement().getStyle().set("width", "100%");
        formLayout.addFormItem(email, "").getElement().getStyle().set("width", "100%");
        formLayout.addFormItem(submitButton, "").getElement().getStyle().set("width", "100%");

        formLayout.setWidth("400px");

        submitButton.getElement().getStyle().set("position", "relative");
        submitButton.getElement().getStyle().set("left", "60px");

        VerticalLayout container = new VerticalLayout();
        container.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        container.setAlignItems(Alignment.CENTER);
        container.setJustifyContentMode(JustifyContentMode.CENTER);
        container.setSizeFull();

        container.getStyle().set("padding-left", "500px");

        H1 title = new H1("Register a new User");
        title.getElement().getStyle().set("position", "relative");
        title.getElement().getStyle().set("left", "20px");
        title.getElement().getStyle().set("top", "120px");

        container.add(formLayout);

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(title,container);
    }

    @Transactional
    private void handleSubmit() {
        if (binder.validate().isOk()) {
            UserForm user = new UserForm();
            binder.writeBeanIfValid(user);

            // Save user details using UserDetailsServiceImpl
            saveUserDetails(user);
            Notification.show("User details saved successfully!");
        } else {
            Notification.show("Please fill out the form correctly.");
        }

        UI.getCurrent().navigate("login");
    }

    private void saveUserDetails(UserForm user) {
        service.saveUserDetails(user);
    }

}
