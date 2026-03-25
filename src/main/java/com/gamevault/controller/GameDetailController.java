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

/**
 * Controller for the game detail view.
 *
 * Responsible for displaying detailed information about a selected game
 * and allowing the user to:
 * - Update play status
 * - Adjust personal rating
 * - Add or edit notes
 * - Remove the game from the library
 */
@Component
public class GameDetailController implements Initializable {

    @FXML private Label detailTitle;
    @FXML private Label titleLabel;
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
    private Runnable onBackHandler;

    /**
     * Constructs the GameDetailController with the required GameService.
     *
     * @param gameService service used to manage and persist game data
     */
    public GameDetailController(GameService gameService) {
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
     * Populates the view with the selected game's data.
     *
     * Updates all UI elements including labels, slider, combo box,
     * notes field, and cover image.
     *
     * @param game the game whose details should be displayed
     */
    public void setGame(Game game) {
        this.game = game;

        detailTitle.setText(game.getTitle());
        titleLabel.setText(game.getTitle());
        genreLabel.setText("Genre: " + orBlank(game.getGenre()));
        platformLabel.setText("Platform: " + orBlank(game.getPlatform()));
        releasedLabel.setText("Released: " + orBlank(game.getReleaseDate()));
        communityRatingLabel.setText(game.getCommunityRating() != null
                ? "★ " + game.getCommunityRating() : "N/A");

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
     * Sets the handler to be executed when the back action is triggered.
     *
     * @param handler the action to run when navigating back
     */
    public void setOnBack(Runnable handler) {
        this.onBackHandler = handler;
    }

    /**
     * Handles the back button action.
     *
     * Invokes the registered back handler if one is set.
     */
    @FXML
    private void onBack() {
        if (onBackHandler != null) onBackHandler.run();
    }

    /**
     * Saves updates made to the game.
     *
     * Updates the game's play status, personal rating, and notes,
     * then persists the changes using GameService.
     *
     * Displays a confirmation alert upon successful save.
     */
    @FXML
    private void onSave() {
        game.setPlayStatus(statusCombo.getValue());
        game.setPersonalRating((int) Math.round(ratingSlider.getValue()));
        game.setNotes(notesArea.getText());
        gameService.updateGame(game);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Saved");
        alert.setHeaderText(null);
        alert.setContentText(game.getTitle() + " updated successfully.");
        alert.showAndWait();
    }

    /**
     * Handles removing the game from the user's library.
     *
     * Displays a confirmation dialog before deletion.
     * If confirmed, deletes the game and navigates back.
     */
    @FXML
    private void onRemove() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Remove game");
        confirm.setHeaderText(null);
        confirm.setContentText("Remove " + game.getTitle() + " from your library?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                gameService.deleteGame(game.getId());
                if (onBackHandler != null) onBackHandler.run();
            }
        });
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