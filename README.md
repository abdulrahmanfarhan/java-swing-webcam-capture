# Webcam Snapshot Application

## Introduction

The **Webcam Snapshot Application** is a Java-based desktop program that detects and utilizes connected webcams on a user's system. It allows users to preview live feeds from multiple webcams, capture snapshots, and save them to a specified directory. The application supports configuring webcams to the highest available resolution up to a desired size (default: 1920x1080) and provides a simple graphical user interface (GUI) for interaction.

This application follows the **Model-View-Controller (MVC)** architectural pattern for better maintainability, scalability, and testability. It leverages the Sarxos Webcam library (`com.github.sarxos.webcam`) for webcam access and image capture, alongside standard Java libraries like Swing and ImageIO.

**Primary features:**
- Detection and configuration of multiple webcams.
- Live preview of webcam feeds.
- Snapshot capture and saving as timestamped JPG files.
- User-selectable save directory via a file chooser.
- Error handling and notifications for capture failures.

---

## Architecture Overview

The application uses the **MVC pattern**:

- **Model (`WebcamModel`)**: Handles webcam detection, configuration, image capture, and file saving. Independent of GUI, making it reusable and testable.
- **View (`WebcamView`)**: Manages all user interface elements. Displays live previews, buttons, and dialogs. Delegates user actions to the Controller.
- **Controller (`WebcamController`)**: Bridges Model and View. Handles events, updates the View, and triggers Model operations.

**Design Benefits:**
- Loose coupling between components.
- Reusability of the Model in non-GUI contexts.
- Testability of each layer independently.
- Thread-safe GUI operations via the Event Dispatch Thread (`SwingUtilities.invokeLater()`).

Additional notes:
- All GUI operations are performed on the EDT.
- Error handling is centralized in the Model and propagated to the View through the Controller.
- No external databases or network dependencies are used; all operations are local.

---

## Class Descriptions

### 1. `WebcamModel.java` (Model Layer)
Encapsulates the core logic for webcam management and image operations.

- **Fields:**
  - `private final List<Webcam> webcams` – list of detected webcams.
  - `private String saveDirectory` – directory for saving images (default: `"C:/WebcamCaptures/"`).

- **Constructor:**
  - `public WebcamModel()` – initializes the list of webcams.

- **Key Methods:**
  - `selectHighestResolution(Webcam webcam, Dimension desiredSize)` – selects the highest supported resolution not exceeding the desired size.
  - `configureWebcams(Dimension desiredSize)` – sets all webcams to the highest suitable resolution; returns successfully configured webcams.
  - `captureAndSaveImages()` – captures images from all webcams, saves as timestamped JPG files, returns list of success/error messages.
  - `setSaveDirectory(String directory)` – updates the save directory.
  - `getSaveDirectory()` – returns the current save directory.
  - `getWebcams()` – returns the list of webcams.

- **Dependencies:** `com.github.sarxos.webcam.Webcam` for webcam access and `javax.imageio.ImageIO` for saving images.

---

### 2. `Main.java` (Entry Point)
- **Method:**
  - `public static void main(String[] args)` – launches the application by creating MVC components on the EDT.

- **Purpose:** Ensures thread-safe initialization of GUI components. Does not contain business logic.

---

### 3. `WebcamView.java` (View Layer)
- **Fields:**
  - `private final List<WebcamPanel> panels` – list of live webcam panels.
  - `private final JButton btSnapMe` – snapshot button.
  - `private final JButton btChooseDir` – choose directory button.
  - `private final JFileChooser fileChooser` – directory chooser dialog.

- **Constructor:**
  - `public WebcamView()` – sets up frame title, layout, file chooser defaults, and close operation.

- **Key Methods:**
  - `initializePanels(List<Webcam> webcams)` – creates and adds webcam panels.
  - `addButtons(Action snapAction, Action chooseDirAction)` – attaches button actions.
  - `display()` – packs and displays the frame.
  - `showNotification(String message, String title, int messageType)` – shows dialog notifications.
  - `chooseDirectory()` – opens file chooser and returns the selected directory path.

- **Dependencies:** Uses `WebcamPanel` for live feeds and standard Swing components for GUI.

---

### 4. `WebcamController.java` (Controller Layer)
- **Fields:**
  - `private final WebcamModel model` – reference to the Model.
  - `private final WebcamView view` – reference to the View.

- **Constructor:**
  - `public WebcamController(WebcamModel model, WebcamView view)` – stores references and calls `initialize()`.

- **Key Methods:**
  - `initialize()` – configures webcams, initializes panels, sets up button actions, displays the GUI.
  - `handleSnapshot()` – calls `captureAndSaveImages()`, shows notifications for success/errors.
  - `handleChooseDirectory()` – updates save directory and shows confirmation.

- **Dependencies:** Uses Swing `AbstractAction` for event handling, coordinates Model and View.

---

## Program Flow

1. **Startup (`Main.java`)**
   - `main()` invoked.
   - Swing components created on EDT.
   - MVC components instantiated: `WebcamModel`, `WebcamView`, `WebcamController`.

2. **Initialization (`WebcamController.initialize()`)**
   - Desired resolution defined (default: 1920x1080).
   - Model configures webcams.
   - View initializes webcam panels.
   - Button actions set for snapshots and directory selection.
   - GUI displayed.

3. **User Interaction**
   - Event-driven loop begins.
   - **Snapshot Button:** captures images, saves with timestamps, shows dialogs.
   - **Choose Directory Button:** opens file chooser, updates save directory, confirms.

4. **Termination**
   - GUI closed by user.
   - Webcams automatically released by Sarxos library.

**Error Handling:**
- Configuration failures logged to console.
- Capture failures generate error messages.
- Save failures (e.g., IOException) reported.
- All errors shown via dialogs.

---

## Dependencies and Requirements

- **Java 8+**
- **Sarxos Webcam Library** (`com.github.sarxos.webcam`)
- **Swing** and **ImageIO**
- At least one connected webcam with proper OS permissions.
- No additional network or database setup required.

---

## Guidelines for Extension or Modification

- **Adding Features:**
  - Webcam filtering by name in Model.
  - User-selectable resolutions via GUI.
  - Periodic batch capture using timers in Controller.
  - Image processing (filters, effects) before saving.

- **Modifying Existing Code:**
  - Change default save directory in `WebcamModel`.
  - Improve UI layout (e.g., `GridLayout` for multiple webcams).
  - Replace `System.err` logging with frameworks like Log4j.
  - Ensure thread-safety when adding asynchronous features.

- **Testing:**
  - Unit test Model using mocked webcams.
  - Integration tests with real hardware.
  - Test edge cases: no webcams, invalid directories, permission issues.

- **Best Practices:**
  - Maintain strict MVC separation.
  - Document new methods with Javadoc.
  - Use version control (e.g., Git) for all changes.

---

## Usage

1. Run `Main.java` to start the application.
2. Select a save directory via **Choose Save Directory**.
3. Click **Snapshot** to capture images.
4. Images saved as JPG files with timestamped filenames in the selected directory.

---
