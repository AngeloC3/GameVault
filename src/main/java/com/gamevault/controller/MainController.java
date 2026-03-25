package com.gamevault.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main controller for the application layout.
 *
 * Manages:
 * - Sidebar navigation
 * - View switching between dashboard, library, and discover
 * - Sidebar expand and collapse behavior
 *
 * Acts as the central coordinator for loading different views
 * into the main content area.
 */
@Component
public class MainController implements Initializable {

    @FXML private VBox sidebar;
    @FXML private Label logoText;
    @FXML private Label logoSub;
    @FXML private Button btnDashboard;
    @FXML private Button btnLibrary;
    @FXML private Button btnDiscover;
    @FXML private Button collapseBtn;
    @FXML private StackPane contentArea;

    private final ApplicationContext springContext;

    private boolean sidebarExpanded = true;
    private static final double EXPANDED_WIDTH = 200;
    private static final double COLLAPSED_WIDTH = 56;

    /**
     * Constructs the MainController with the Spring application context.
     *
     * @param springContext context used for dependency injection of controllers
     */
    public MainController(ApplicationContext springContext) {
        this.springContext = springContext;
    }

    /**
     * Initializes the controller after the FXML has been loaded.
     *
     * Sets the default active button and loads the initial view.
     *
     * @param location the location used to resolve relative paths
     * @param resources the resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setActiveButton(btnLibrary);
        loadView("LibraryView");
    }

    /**
     * Handles navigation to the dashboard view.
     */
    @FXML
    private void onDashboard() {
        setActiveButton(btnDashboard);
        loadView("DashboardView");
    }

    /**
     * Handles navigation to the library view.
     */
    @FXML
    private void onLibrary() {
        setActiveButton(btnLibrary);
        loadView("LibraryView");
    }

    /**
     * Handles navigation to the discover view.
     */
    @FXML
    private void onDiscover() {
        setActiveButton(btnDiscover);
        loadView("DiscoverView");
    }

    /**
     * Toggles the sidebar between expanded and collapsed states.
     *
     * Animates the width transition and updates visibility of text elements
     * and button labels accordingly.
     */
    @FXML
    private void onToggleSidebar() {
        sidebarExpanded = !sidebarExpanded;

        double targetWidth = sidebarExpanded ? EXPANDED_WIDTH : COLLAPSED_WIDTH;

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(200),
                        new KeyValue(sidebar.prefWidthProperty(), targetWidth)
                )
        );
        timeline.play();

        // Show/hide text elements
        logoText.setVisible(sidebarExpanded);
        logoText.setManaged(sidebarExpanded);
        logoSub.setVisible(sidebarExpanded);
        logoSub.setManaged(sidebarExpanded);

        // Swap button labels between icon-only and full text
        if (sidebarExpanded) {
            btnDashboard.setText("  Dashboard");
            btnLibrary.setText("  My Library");
            btnDiscover.setText("  Discover");
            collapseBtn.setText("◀  Collapse");
        } else {
            btnDashboard.setText("⊞");
            btnLibrary.setText("☰");
            btnDiscover.setText("⌕");
            collapseBtn.setText("▶");
        }
    }

    /**
     * Updates the active navigation button styling.
     *
     * Removes the active style from all buttons and applies it to the selected one.
     *
     * @param active the button to mark as active
     */
    private void setActiveButton(Button active) {
        for (Button btn : new Button[]{btnDashboard, btnLibrary, btnDiscover}) {
            btn.getStyleClass().remove("nav-button-active");
        }
        active.getStyleClass().add("nav-button-active");
    }

    /**
     * Loads a view into the main content area.
     *
     * Uses the provided view name to locate the corresponding FXML file,
     * initializes its controller via Spring, and replaces the current content.
     *
     * @param viewName the name of the view to load (without file extension)
     */
    private void loadView(String viewName) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/gamevault/view/" + viewName + ".fxml")
            );
            loader.setControllerFactory(springContext::getBean);
            Node view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}