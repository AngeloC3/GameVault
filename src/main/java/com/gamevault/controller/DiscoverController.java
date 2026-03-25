package com.gamevault.controller;

import com.gamevault.model.Game;
import com.gamevault.service.GameSearchService;
import com.gamevault.service.GameService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Controller for the discover view.
 *
 * Responsible for handling game search functionality and displaying results
 * in a grid layout. Integrates with GameService to fetch results and
 * GameService to manage the user's library.
 */
@Component
public class DiscoverController implements Initializable {

    @FXML private TextField searchField;
    @FXML private FlowPane resultsGrid;
    @FXML private Label statusLabel;

    private final GameService gameService;

    /**
     * Constructs the DiscoverController with required services.
     *
     * @param gameService service used to manage the user's game library
     */
    public DiscoverController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Initializes the controller after the FXML has been loaded.
     *
     * @param location the location used to resolve relative paths
     * @param resources the resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    /**
     * Handles the search action triggered by the user.
     *
     * Retrieves the query from the input field, performs a search using
     * GameSearchService on a background thread, and updates the UI with results.
     *
     * Displays a message if no results are found and shows the number of results otherwise.
     */
    @FXML
    private void onSearch() {
        String query = searchField.getText();
        if (query == null || query.isBlank()) return;

        statusLabel.setText("Searching...");
        resultsGrid.getChildren().clear();

        // Run search off the FX thread when using real API later
        new Thread(() -> {
            List<Game> results = gameService.searchGames(query);
            Set<String> libraryIds = gameService.getLibrary().stream()
                    .map(Game::getExternalId)
                    .collect(Collectors.toSet());

            Platform.runLater(() -> {
                if (results.isEmpty()) {
                    statusLabel.setText("No results found for \"" + query + "\".");
                    return;
                }
                statusLabel.setText(results.size() + " results for \"" + query + "\"");

                for (Game game : results) {
                    try {
                        FXMLLoader loader = new FXMLLoader(
                                getClass().getResource("/com/gamevault/view/DiscoverCard.fxml")
                        );
                        Node card = loader.load();
                        DiscoverCardController ctrl = loader.getController();
                        boolean inLibrary = libraryIds.contains(game.getExternalId());
                        ctrl.setGame(game, inLibrary);
                        ctrl.setOnAdd(this::addToLibrary);
                        resultsGrid.getChildren().add(card);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }).start();
    }

    /**
     * Adds a game to the user's library.
     *
     * Sets the default play status to BACKLOG before saving the game.
     *
     * @param game the game to add to the library
     */
    private void addToLibrary(Game game) {
        game.setPlayStatus(Game.PlayStatus.BACKLOG);
        gameService.addToLibrary(game);
    }
}