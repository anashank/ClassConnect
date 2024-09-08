package com.example.application.views.list;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.dom.Element;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;


public class ProfilePictureView extends VerticalLayout {

    private Image profilePicture;
    Upload upload;

    public ProfilePictureView() {
        // Create a memory buffer to hold the uploaded file data
        MemoryBuffer buffer = new MemoryBuffer();

        // Create the Upload component and set the memory buffer as the receiver
        upload = new Upload(buffer);
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");

        // Set up a default image (local or from a URL)
        String defaultImageUrl = "https://placekitten.com/200/300"; // Replace with your default image URL
        profilePicture = new Image(defaultImageUrl, "Default Profile Picture");

        // Set the size of the profile picture
        profilePicture.setWidth("150px");
        profilePicture.setHeight("150px");
        profilePicture.setAlt("Profile Picture");
        profilePicture.addClassName("circle-image");


        // Add an upload succeeded listener to handle the image after upload
        upload.addSucceededListener(event -> {
            // Get the uploaded file as an InputStream
            InputStream inputStream = buffer.getInputStream();

            // Convert the InputStream to a byte array
            byte[] bytes = null;
            try {
                bytes = inputStream.readAllBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Convert the byte array to a Base64 string
            String base64Image = Base64.getEncoder().encodeToString(bytes);

            // Set the Image component's source to the Base64 string
            profilePicture.setSrc("data:image/png;base64," + base64Image);
        });
        Element profilePictureElement = profilePicture.getElement();
        profilePictureElement.getStyle().set("border-radius", "50%");
        profilePictureElement.getStyle().set("object-fit", "cover");

        // Add the components to the layout
        add(profilePicture);
    }

    public void addUpload(){
        add(upload);
    }
    public void removeUpload(){
        remove(upload);
    }
}