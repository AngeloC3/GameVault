package com.gamevault.controller;

import com.gamevault.model.Game;
import com.gamevault.service.GameService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the library view.
 *
 * Responsible for displaying the user's game collection in a grid layout.
 * Supports searching, filtering by play status, and interacting with
 * individual game cards.
 *
 * Also manages modal and full detail views for selected games.
 */
@Component
public class LibraryController implements Initializable {

    @FXML private TextField searchField;
    @FXML private FlowPane gameGrid;
    @FXML private StackPane modalOverlay;
    @FXML private Button filterAll;
    @FXML private Button filterPlaying;
    @FXML private Button filterBacklog;
    @FXML private Button filterCompleted;
    @FXML private Button filterWishlist;
    @FXML private Button filterDropped;

    private final GameService gameService;
    private final ApplicationContext springContext;

    private Game.PlayStatus activeFilter = null;

    /**
     * Constructs the LibraryController with required dependencies.
     *
     * @param gameService service used to retrieve and manage games
     * @param springContext Spring application context used for controller injection
     */
    public LibraryController(GameService gameService, ApplicationContext springContext) {
        this.gameService = gameService;
        this.springContext = springContext;
    }

    /**
     * Initializes the controller after the FXML has been loaded.
     *
     * Sets up a listener for the search field and populates the grid.
     *
     * @param location the location used to resolve relative paths
     * @param resources the resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        searchField.textProperty().addListener((obs, old, val) -> refreshGrid());
        refreshGrid();
    }

    /**
     * Handles filter button clicks.
     *
     * Updates the active filter based on the selected button,
     * applies visual styling, and refreshes the grid.
     *
     * @param e the action event triggered by clicking a filter button
     */
    @FXML
    private void onFilter(javafx.event.ActionEvent e) {
        Button clicked = (Button) e.getSource();

        // Clear all active styles
        for (Button b : new Button[]{filterAll, filterPlaying, filterBacklog,
                filterCompleted, filterWishlist, filterDropped}) {
            b.getStyleClass().remove("filter-pill-active");
        }
        clicked.getStyleClass().add("filter-pill-active");

        activeFilter = switch (clicked.getId()) {
            case "filterPlaying"   -> Game.PlayStatus.PLAYING;
            case "filterBacklog"   -> Game.PlayStatus.BACKLOG;
            case "filterCompleted" -> Game.PlayStatus.COMPLETED;
            case "filterWishlist"  -> Game.PlayStatus.WISHLIST;
            case "filterDropped"   -> Game.PlayStatus.DROPPED;
            default                -> null;
        };

        refreshGrid();
    }

    /**
     * Refreshes the game grid based on the current filter and search query.
     *
     * Retrieves games from the service, applies filtering and search,
     * and dynamically loads game card components into the grid.
     */
    private void refreshGrid() {
        List<Game> games = activeFilter != null
                ? gameService.getByStatus(activeFilter)
                : gameService.getLibrary();

        String query = searchField.getText();
        if (query != null && !query.isBlank()) {
            String lower = query.toLowerCase();
            games = games.stream()
                    .filter(g -> g.getTitle().toLowerCase().contains(lower))
                    .toList();
        }

        gameGrid.getChildren().clear();

        for (Game game : games) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/gamevault/view/GameCard.fxml")
                );
                Node card = loader.load();
                GameCardController ctrl = loader.getController();
                ctrl.setGame(game);
                ctrl.setOnClick(this::openModal);
                gameGrid.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Opens a modal view displaying detailed information about a game.
     *
     * Loads the modal FXML, configures its controller, and displays it
     * in the overlay container.
     *
     * @param game the game to display in the modal
     */
    private void openModal(Game game) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/gamevault/view/GameDetailModal.fxml")
            );
            loader.setControllerFactory(springContext::getBean);
            Node modal = loader.load();
            GameDetailModalController ctrl = loader.getController();
            ctrl.setGame(game);
            ctrl.setOnClose(this::closeModal);
            ctrl.setOnSave(this::refreshGrid);
            ctrl.setOnExpand(this::openFullDetail);

            modalOverlay.getChildren().setAll(modal);
            modalOverlay.setVisible(true);
            modalOverlay.setManaged(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the currently open modal and clears its content.
     */
    private void closeModal() {
        modalOverlay.setVisible(false);
        modalOverlay.setManaged(false);
        modalOverlay.getChildren().clear();
    }

    /**
     * Opens the full detail view for a game.
     *
     * Replaces the main content area with the detailed view and
     * configures navigation back to the library.
     *
     * @param game the game to display in the full detail view
     */
    private void openFullDetail(Game game) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/gamevault/view/GameDetailView.fxml")
            );
            loader.setControllerFactory(springContext::getBean);
            Node view = loader.load();
            GameDetailController ctrl = loader.getController();
            ctrl.setGame(game);
            ctrl.setOnBack(() -> {
                // Close modal and return to library
                closeModal();
                refreshGrid();
            });

            // Push detail view into the main content area
            StackPane contentArea = (StackPane) gameGrid.getScene()
                    .lookup("#contentArea");
            if (contentArea != null) {
                contentArea.getChildren().setAll(view);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}