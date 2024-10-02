package com.example.application.views.list;

import com.example.application.services.StudyGroupService;
import com.example.application.services.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@PermitAll
@Route("study-groups")
public class StudyGroupView extends VerticalLayout {

    private final StudyGroupService studyGroupService;
    private final UserService userService;
    private Grid<StudyGroup> grid = new Grid<>(StudyGroup.class);
    private TextField searchField = new TextField("Search by Subject");
    private UserForm currentUser;

    @Autowired
    public StudyGroupView(StudyGroupService studyGroupService, UserService userService) {
        this.studyGroupService = studyGroupService;
        this.userService = userService;

        grid.setColumns("name", "subject", "date", "spotsLeft");
        grid.addComponentColumn(this::createJoinButton).setHeader("Actions");

        searchField.addValueChangeListener(e -> updateList());

        add(searchField, grid);
        updateList();
    }

    private Button createJoinButton(StudyGroup studyGroup) {
        Button joinButton = new Button("Join");

//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//        currentUser = userService.getUserWithJoinedGroups(username); // Load current user's groups

        // Check if the user is a member of the study group
        boolean isMember = currentUser.getJoinedGroups().contains(studyGroup);
        if (isMember) {
            System.out.println("************************Joined************");
            joinButton.setText("Joined");
            joinButton.setEnabled(false);
        } else {
            joinButton.setEnabled(studyGroup.getSpotsLeft() > 0);
        }

        joinButton.addClickListener(click -> {
            // Disable the button immediately to prevent double clicks
            joinButton.setEnabled(false);

            try {
                // Join the group and decrease available spots
                studyGroupService.joinGroup(studyGroup, currentUser);
                Notification.show("Successfully joined the group: " + studyGroup.getName());

                // Change button text to "Joined"
                joinButton.setText("Joined");

                // Update the spots left immediately
                //studyGroup.setSpotsLeft(studyGroup.getSpotsLeft() - 1);
                grid.getDataProvider().refreshItem(studyGroup); // Refresh the grid item
            } catch (IllegalStateException e) {
                // Re-enable the button if there's an error
                joinButton.setEnabled(true);
                Notification.show(e.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        return joinButton;
    }


    private void updateList() {
        // Get the current user and their joined groups
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        currentUser = userService.getUserWithJoinedGroups(username); // Load current user's groups

        String filter = searchField.getValue();
        List<StudyGroup> currentStudyGroups = studyGroupService.findBySubject(filter);
        grid.setItems(currentStudyGroups); // Set the current items to the grid

        // Ensure the button states are set correctly based on the current user's joined groups
        for (StudyGroup studyGroup : currentStudyGroups) {
            boolean isMember = currentUser.getJoinedGroups().contains(studyGroup);
            if (isMember) {
                studyGroup.setSpotsLeft(studyGroup.getSpotsLeft() - 1); // Ensure spots left is correctly updated
            }
        }
    }
}
