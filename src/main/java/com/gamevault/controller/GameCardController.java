package com.gamevault.controller;

import com.gamevault.model.Game;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import java.util.function.Consumer;

/**
 * Controller for a game card displayed in the user's library.
 *
 * Responsible for presenting basic game information such as:
 * - Cover image
 * - Title
 * - Genre
 * - Play status badge
 *
 * Also supports click interaction for selecting or opening a game.
 */
public class GameCardController {

    @FXML private VBox cardRoot;
    @FXML private ImageView coverImage;
    @FXML private Label titleLabel;
    @FXML private Label genreLabel;
    @FXML private Label statusBadge;

    private Game game;

    /**
     * Populates the card with game data and updates all UI elements.
     *
     * @param game the game to display
     */
    public void setGame(Game game) {
        this.game = game;
        titleLabel.setText(game.getTitle());
        genreLabel.setText(game.getGenre() != null ? game.getGenre() : "");
        applyStatusBadge(game);
        loadCoverImage(game);
    }

    /**
     * Sets a click handler for the entire card.
     *
     * When the card is clicked, the provided handler is invoked
     * with the current game.
     *
     * @param handler the action to perform when the card is clicked
     */
    public void setOnClick(Consumer<Game> handler) {
        cardRoot.setOnMouseClicked(e -> handler.accept(game));
    }

    /**
     * Applies styling and text to the status badge based on the game's play status.
     *
     * If the game has no status, the badge is hidden.
     *
     * @param game the game whose status is used to update the badge
     */
    private void applyStatusBadge(Game game) {
        if (game.getPlayStatus() == null) {
            statusBadge.setVisible(false);
            statusBadge.setManaged(false);
            return;
        }
        String badgeClass = switch (game.getPlayStatus()) {
            case PLAYING   -> "badge-playing";
            case BACKLOG   -> "badge-backlog";
            case COMPLETED -> "badge-completed";
            case WISHLIST  -> "badge-wishlist";
            case DROPPED   -> "badge-dropped";
        };
        statusBadge.setText(game.getPlayStatus().name());
        statusBadge.getStyleClass().setAll("badge", badgeClass);
    }

    /**
     * Loads and displays the game's cover image if a valid URL is provided.
     *
     * If loading fails, the error is silently ignored.
     *
     * @param game the game whose cover image should be displayed
     */
    private void loadCoverImage(Game game) {
        if (game.getCoverImageUrl() != null && !game.getCoverImageUrl().isBlank()) {
            try {
                Image img = new Image(game.getCoverImageUrl(), true);
                coverImage.setImage(img);
            } catch (Exception ignored) {}
        }
    }
}