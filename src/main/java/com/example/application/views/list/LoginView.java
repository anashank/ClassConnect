package com.example.application.views.list;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.server.VaadinServletRequest;

@Route("")
@RouteAlias("login")
@AnonymousAllowed
public class LoginView extends VerticalLayout {

    public LoginView(){
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        var login = new LoginForm();
        login.setAction("login");

        Button regButton = new Button("New User? Register here");

        regButton.addClickListener(event -> UI.getCurrent().navigate("user-form"));

        if (VaadinServletRequest.getCurrent().getWrappedSession().getAttribute("loginError") != null) {
            // Display the error message
            login.setError(true);
            // Remove the error message after displaying it
            VaadinServletRequest.getCurrent().getWrappedSession().removeAttribute("loginError");
        }
        add(
            new H1("Study buddy"),
                login,
                regButton
        );
        //hello
    }
}
