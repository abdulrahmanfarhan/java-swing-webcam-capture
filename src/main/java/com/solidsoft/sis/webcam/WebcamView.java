package com.solidsoft.sis.webcam;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.border.EmptyBorder;

public class WebcamView extends JFrame {

    private final List<WebcamPanel> panels = new ArrayList<>();
    private final JButton btSnapMe = new JButton("Snapshot");
    private final JButton btChooseDir = new JButton("Choose Save Directory");
    private final JFileChooser fileChooser;

    public WebcamView() {
        super("Webcam Snapshot Application");
        setLayout(new FlowLayout());
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setCurrentDirectory(new File("C:/"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Initializes webcam panels for the given webcams.
     *
     * @param webcams List of configured webcams.
     */
    public void initializePanels(List<Webcam> webcams) {
        for (Webcam webcam : webcams) {
            WebcamPanel panel = new WebcamPanel(webcam, webcam.getViewSize(), true);
            panel.setFPSDisplayed(true);
            panel.setFillArea(true);
            panels.add(panel);
            add(panel);
        }
    }

    /**
     * Adds buttons to the frame.
     *
     * @param snapAction Action for the Snapshot button.
     * @param chooseDirAction Action for the Choose Directory button.
     */
    public void addButtons(Action snapAction, Action chooseDirAction) {
        btSnapMe.setAction(snapAction);
        btChooseDir.setAction(chooseDirAction);
        add(btSnapMe);
        add(btChooseDir);
    }

    /**
     * Displays the frame.
     */
    public void display() {
        pack();
        setVisible(true);
    }

    /**
     * Shows a notification dialog.
     *
     * @param message The message to display.
     * @param title The dialog title.
     * @param messageType The type of message (e.g., INFORMATION_MESSAGE,
     * ERROR_MESSAGE).
     */
    public void showNotification(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    /**
     * Opens the file chooser dialog and returns the selected directory.
     *
     * @return The selected directory path, or null if canceled.
     */
    public String chooseDirectory() {
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

}
