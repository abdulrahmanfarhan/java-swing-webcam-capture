package com.solidsoft.sis.webcam;

import com.github.sarxos.webcam.Webcam;
import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Model class for handling webcam operations and image saving.
 */
public class WebcamModel {
    private final List<Webcam> webcams;
    private String saveDirectory = "C:/WebcamCaptures/"; // Default directory

    public WebcamModel() {
        this.webcams = Webcam.getWebcams();
    }

    /**
     * Selects the highest supported resolution up to the desired size.
     * @param webcam The webcam to configure.
     * @param desiredSize The desired resolution.
     * @return The selected resolution.
     */
    public Dimension selectHighestResolution(Webcam webcam, Dimension desiredSize) {
        Dimension[] availableSizes = webcam.getViewSizes();
        Dimension selectedSize = availableSizes[0]; // Default to first available size
        int maxArea = 0;

        for (Dimension size : availableSizes) {
            int area = size.width * size.height;
            if (area > maxArea && size.width <= desiredSize.width && size.height <= desiredSize.height) {
                maxArea = area;
                selectedSize = size;
            }
        }
        return selectedSize;
    }

    /**
     * Configures webcams with the highest supported resolution.
     * @param desiredSize The desired resolution.
     * @return List of configured webcams.
     */
    public List<Webcam> configureWebcams(Dimension desiredSize) {
        List<Webcam> configuredWebcams = new ArrayList<>();
        for (Webcam webcam : webcams) {
            try {
                Dimension selectedSize = selectHighestResolution(webcam, desiredSize);
                webcam.setViewSize(selectedSize);
                configuredWebcams.add(webcam);
                System.out.printf("Configured %s with resolution %dx%d%n",
                        webcam.getName(), selectedSize.width, selectedSize.height);
            } catch (Exception e) {
                System.err.printf("Failed to configure webcam %s: %s%n", webcam.getName(), e.getMessage());
            }
        }
        return configuredWebcams;
    }

    /**
     * Captures and saves images from all webcams.
     * @return List of save results (success messages or error messages).
     */
    public List<String> captureAndSaveImages() {
        List<String> results = new ArrayList<>();
        File directory = new File(saveDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        for (int i = 0; i < webcams.size(); i++) {
            Webcam webcam = webcams.get(i);
            BufferedImage image = webcam.getImage();
            if (image == null) {
                String error = String.format("Failed to capture image from %s: Image is null", webcam.getName());
                results.add(error);
                continue;
            }
            try {
                File file = new File(saveDirectory + File.separator + String.format("capture_%s_%d.jpg", timestamp, i));
                ImageIO.write(image, "JPG", file);
                String success = String.format("Image for %s saved to %s", webcam.getName(), file.getAbsolutePath());
                results.add(success);
            } catch (IOException ex) {
                String error = String.format("Error saving image for %s: %s", webcam.getName(), ex.getMessage());
                results.add(error);
            }
        }
        return results;
    }

    /**
     * Sets the save directory for images.
     * @param directory The new save directory.
     */
    public void setSaveDirectory(String directory) {
        this.saveDirectory = directory;
    }

    /**
     * Gets the current save directory.
     * @return The save directory.
     */
    public String getSaveDirectory() {
        return saveDirectory;
    }

    public List<Webcam> getWebcams() {
        return webcams;
    }
}