package com.gamevault.controller;

import com.gamevault.model.Game;
import com.gamevault.service.GameService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * Controller for the game detail modal view.
 *
 * Provides a compact interface for viewing and editing game details,
 * including play status, personal rating, and notes.
 *
 * Also supports actions such as:
 * - Closing the modal
 * - Expanding to a full detail view
 * - Saving updates
 * - Removing the game
 */
@Component
public class GameDetailModalController implements Initializable {

    @FXML private Label modalTitle;
    @FXML private Label genreLabel;
    @FXML private Label platformLabel;
    @FXML private Label releasedLabel;
    @FXML private Label communityRatingLabel;
    @FXML private ImageView coverImage;
    @FXML private ComboBox<Game.PlayStatus> statusCombo;
    @FXML private Slider ratingSlider;
    @FXML private Label ratingLabel;
    @FXML private TextArea notesArea;

    private final GameService gameService;
    private Game game;
    private Runnable onCloseHandler;
    private Consumer<Game> onExpandHandler;
    private Runnable onSaveHandler;

    /**
     * Constructs the GameDetailModalController with the required GameService.
     *
     * @param gameService service used to manage and persist game data
     */
    public GameDetailModalController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Initializes the controller after the FXML has been loaded.
     *
     * Sets up UI components such as:
     * - Populating the status dropdown
     * - Adding a listener to update the rating label dynamically
     *
     * @param location the location used to resolve relative paths
     * @param resources the resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statusCombo.getItems().setAll(Game.PlayStatus.values());

        ratingSlider.valueProperty().addListener((obs, old, val) -> {
            int rounded = (int) Math.round(val.doubleValue());
            ratingLabel.setText(rounded + " / 100");
        });
    }

    /**
     * Populates the modal with the selected game's data.
     *
     * Updates all UI elements including labels, slider, combo box,
     * notes field, and cover image.
     *
     * @param game the game whose details should be displayed
     */
    public void setGame(Game game) {
        this.game = game;
        modalTitle.setText(game.getTitle());
        genreLabel.setText("Genre: " + orBlank(game.getGenre()));
        platformLabel.setText("Platform: " + orBlank(game.getPlatform()));
        releasedLabel.setText("Released: " + orBlank(game.getReleaseDate()));
        communityRatingLabel.setText("Community rating: " +
                (game.getCommunityRating() != null ? game.getCommunityRating() : "N/A"));

        statusCombo.setValue(game.getPlayStatus());

        int rating = game.getPersonalRating() != null ? game.getPersonalRating() : 50;
        ratingSlider.setValue(rating);
        ratingLabel.setText(rating + " / 100");

        notesArea.setText(game.getNotes() != null ? game.getNotes() : "");

        if (game.getCoverImageUrl() != null && !game.getCoverImageUrl().isBlank()) {
            try {
                coverImage.setImage(new Image(game.getCoverImageUrl(), true));
            } catch (Exception ignored) {}
        }
    }

    /**
     * Sets the handler to be executed when the modal is closed.
     *
     * @param handler the action to run on close
     */
    public void setOnClose(Runnable handler) {
        this.onCloseHandler = handler;
    }

    /**
     * Sets the handler to be executed when the expand action is triggered.
     *
     * @param handler the action to run when expanding the view
     */
    public void setOnExpand(Consumer<Game> handler) {
        this.onExpandHandler = handler;
    }

    /**
     * Sets the handler to be executed after a save or removal operation.
     *
     * @param handler the action to run after saving changes
     */
    public void setOnSave(Runnable handler) {
        this.onSaveHandler = handler;
    }

    /**
     * Handles the close action.
     *
     * Invokes the registered close handler if one is set.
     */
    @FXML
    private void onClose() {
        if (onCloseHandler != null) onCloseHandler.run();
    }

    /**
     * Handles the expand action.
     *
     * Invokes the registered expand handler with the current game.
     */
    @FXML
    private void onExpand() {
        if (onExpandHandler != null) onExpandHandler.accept(game);
    }

    /**
     * Saves updates made to the game.
     *
     * Updates the game's play status, personal rating, and notes,
     * then persists the changes using GameService.
     *
     * Invokes save and close handlers after completion.
     */
    @FXML
    private void onSave() {
        game.setPlayStatus(statusCombo.getValue());
        game.setPersonalRating((int) Math.round(ratingSlider.getValue()));
        game.setNotes(notesArea.getText());
        gameService.updateGame(game);
        if (onSaveHandler != null) onSaveHandler.run();
        if (onCloseHandler != null) onCloseHandler.run();
    }

    /**
     * Removes the game from the user's library.
     *
     * Deletes the game and invokes save and close handlers.
     */
    @FXML
    private void onRemove() {
        gameService.deleteGame(game.getId());
        if (onSaveHandler != null) onSaveHandler.run();
        if (onCloseHandler != null) onCloseHandler.run();
    }

    /**
     * Returns the provided value or a placeholder if it is null.
     *
     * @param val the string value to check
     * @return the original value if not null, otherwise a placeholder symbol
     */
    private String orBlank(String val) {
        return val != null ? val : "—";
    }
}