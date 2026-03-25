package com.gamevault.controller;

import com.gamevault.model.Game;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * Controller for an individual game card in the discover view.
 *
 * Responsible for displaying game details such as:
 * - Cover image
 * - Title
 * - Genre
 * - Community rating
 *
 * Also handles user interaction for adding a game to the library.
 */
@Component
public class DiscoverCardController {

    @FXML private ImageView coverImage;
    @FXML private Label titleLabel;
    @FXML private Label genreLabel;
    @FXML private Label ratingLabel;
    @FXML private Button addButton;

    private Game game;

    /**
     * Populates the card with game data and updates UI state.
     *
     * If the game is already in the user's library, the add button
     * is disabled and updated to reflect that state.
     *
     * @param game the game to display on the card
     * @param alreadyInLibrary whether the game already exists in the user's library
     */
    public void setGame(Game game, boolean alreadyInLibrary) {
        this.game = game;
        titleLabel.setText(game.getTitle());
        genreLabel.setText(game.getGenre() != null ? game.getGenre() : "");
        ratingLabel.setText(game.getCommunityRating() != null
                ? "★ " + game.getCommunityRating() : "");

        if (alreadyInLibrary) {
            addButton.setText("✓ In library");
            addButton.setDisable(true);
            addButton.getStyleClass().add("filter-pill");
        }

        if (game.getCoverImageUrl() != null && !game.getCoverImageUrl().isBlank()) {
            try {
                coverImage.setImage(new Image(game.getCoverImageUrl(), true));
            } catch (Exception ignored) {}
        }
    }

    /**
     * Sets the handler to be invoked when the add button is clicked.
     *
     * When triggered, the handler is called with the current game,
     * and the button is updated to reflect that the game has been added.
     *
     * @param handler a consumer that defines the action to take when adding a game
     */
    public void setOnAdd(Consumer<Game> handler) {
        addButton.setOnAction(e -> {
            handler.accept(game);
            addButton.setText("✓ In library");
            addButton.setDisable(true);
        });
    }
}