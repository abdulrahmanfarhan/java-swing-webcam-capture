/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.solidsoft.sis.webcam;

public class Main {
    public static void main(String[] args) {
        // Ensure GUI is created on the Event Dispatch Thread
        javax.swing.SwingUtilities.invokeLater(() -> {
            WebcamModel model = new WebcamModel();
            WebcamView view = new WebcamView();
            WebcamController webcamController = new WebcamController(model, view);
        });
    }
}