package com.example.application.views.list;

import com.example.application.services.UserDetailsServiceImpl;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.html.Div;

import java.util.List;


//@CssImport("./styles/styles.css") // Ensure the path is correct
public class Friendslide extends VerticalLayout {
    private UserDetailsServiceImpl databaseService;
    public Friendslide(UserDetailsServiceImpl databaseService) {
        HorizontalLayout friendsContainer = new HorizontalLayout();
        friendsContainer.setId("friends-container");
        friendsContainer.setWidthFull();
        friendsContainer.getStyle().set("overflow", "hidden");

        this.databaseService = databaseService;

        List<String> names = databaseService.getNames();

        System.out.println(names);

        for (int i = 0; i < names.size(); i++) {
            Div friendDiv = new Div();
            friendDiv.setText(names.get(i));
            friendDiv.addClassName("friend-item"); // Add the CSS class
            friendsContainer.add(friendDiv);
        }

        add(friendsContainer);

    }
}