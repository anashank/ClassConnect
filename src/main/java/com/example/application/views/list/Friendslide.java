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
    int flag = 0;
    public Friendslide(UserDetailsServiceImpl databaseService) {
        HorizontalLayout friendsContainer = new HorizontalLayout();
        friendsContainer.setId("friends-container");
        friendsContainer.setWidthFull();
        friendsContainer.getStyle().set("overflow", "hidden");

        this.databaseService = databaseService;

        List<UserForm> allusers = this.databaseService.findAllUsers();

        for (int i = 0; i < allusers.size(); i++) {
            if (allusers.get(i).getUsername().equals("user1")) {
                flag = 1;
                break;
            }

        }

        if (flag == 0) {
            this.databaseService.createTestUsers();
        }


        List<String> names = this.databaseService.getNames();

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