package com.example.application.views.list;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

public class ProfilePictureView extends VerticalLayout {

    private Image profileImage;

    public ProfilePictureView() {
        // Profile Image
        profileImage = new Image();
        profileImage.setWidth("150px");
        profileImage.setHeight("150px");
        profileImage.getStyle().set("border-radius", "50%");

        // Upload component
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");

        upload.addSucceededListener(event -> {
            // Display the uploaded image
            String mimeType = event.getMIMEType();
            String fileName = event.getFileName();
            //profileImage.setSrc(String.valueOf(buffer.getInputStream()));
        });

        // Layout
        add(profileImage, upload);
    }
}