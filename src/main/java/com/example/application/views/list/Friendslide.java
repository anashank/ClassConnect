package com.example.application.views.list;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.html.Div;


//@CssImport("./styles/styles.css") // Ensure the path is correct
public class Friendslide extends VerticalLayout {

    public Friendslide() {
        // Main container
        HorizontalLayout friendsContainer = new HorizontalLayout();
        friendsContainer.setId("friends-container");
        friendsContainer.setWidthFull();
        friendsContainer.getStyle().set("overflow", "hidden");

        // Example friend components
        for (int i = 1; i <= 10; i++) {
            Div friendDiv = new Div();
            friendDiv.setText("Friend " + i);
            friendDiv.addClassName("friend-item"); // Add the CSS class
            friendsContainer.add(friendDiv);
        }

        add(friendsContainer);

        // Load custom JS for sliding functionality
        getElement().executeJs("loadSlidingFunctionality()");
    }
}