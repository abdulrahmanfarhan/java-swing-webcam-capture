package com.solidsoft.sis.webcam;

import com.github.sarxos.webcam.Webcam;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Controller class to handle interactions between WebcamModel and WebcamView.
 */
public class WebcamController {
    private final WebcamModel model;
    private final WebcamView view;

    public WebcamController(WebcamModel model, WebcamView view) {
        this.model = model;
        this.view = view;
        initialize();
    }

    /**
     * Initializes the controller by setting up the view and actions.
     */
    private void initialize() {
        // Configure webcams and initialize panels
        Dimension desiredSize = new Dimension(1920, 1080);
        List<Webcam> configuredWebcams = model.configureWebcams(desiredSize);
        view.initializePanels(configuredWebcams);

        // Set up button actions
        view.addButtons(
                new AbstractAction("Snapshot") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleSnapshot();
                    }
                },
                new AbstractAction("Choose Save Directory") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleChooseDirectory();
                    }
                }
        );

        // Display the view
        view.display();
    }

    /**
     * Handles the snapshot action.
     */
    private void handleSnapshot() {
        List<String> results = model.captureAndSaveImages();
        for (String result : results) {
            if (result.contains("Failed") || result.contains("Error")) {
                view.showNotification(result, "Capture Error", JOptionPane.ERROR_MESSAGE);
            } else {
                view.showNotification(result, "Snapshot Captured", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
     * Handles the directory selection action.
     */
    private void handleChooseDirectory() {
        String selectedDir = view.chooseDirectory();
        if (selectedDir != null) {
            model.setSaveDirectory(selectedDir);
            view.showNotification("Save directory set to: " + selectedDir, "Directory Selected", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Selected save directory: " + selectedDir);
        }
    }
}