package com.gamevault.controller;

import com.gamevault.model.Game;
import com.gamevault.service.GameService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller for the dashboard view.
 *
 * Responsible for displaying summary statistics and lists such as:
 * - Total number of games
 * - Games by play status
 * - Average rating
 * - Recently added games
 * - Top rated games
 *
 * This controller integrates JavaFX UI elements with backend data
 * provided by the GameService.
 */
@Component
public class DashboardController implements Initializable {

    @FXML private Label totalGamesNum;
    @FXML private Label playingNum;
    @FXML private Label completedNum;
    @FXML private Label avgRatingNum;
    @FXML private Label backlogNum;
    @FXML private VBox statusBreakdown;
    @FXML private VBox recentList;
    @FXML private VBox topRatedList;

    private final GameService gameService;

    /**
     * Constructs the DashboardController with the required GameService.
     *
     * @param gameService service used to retrieve and manage game data
     */
    public DashboardController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Initializes the controller after the FXML has been loaded.
     *
     * Automatically triggers a refresh of all dashboard data.
     *
     * @param location the location used to resolve relative paths
     * @param resources the resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refresh();
    }

    /**
     * Refreshes all dashboard data and updates the UI.
     *
     * This includes:
     * - Updating statistic labels
     * - Rendering status breakdown bars
     * - Populating recently added games
     * - Populating top rated games
     */
    private void refresh() {
        List<Game> all = gameService.getLibrary();

        // Stat cards
        totalGamesNum.setText(String.valueOf(all.size()));

        long playing = all.stream()
                .filter(g -> g.getPlayStatus() == Game.PlayStatus.PLAYING).count();
        playingNum.setText(String.valueOf(playing));

        long completed = all.stream()
                .filter(g -> g.getPlayStatus() == Game.PlayStatus.COMPLETED).count();
        completedNum.setText(String.valueOf(completed));

        long backlog = all.stream()
                .filter(g -> g.getPlayStatus() == Game.PlayStatus.BACKLOG).count();
        backlogNum.setText(String.valueOf(backlog));

        double avg = all.stream()
                .filter(g -> g.getPersonalRating() != null)
                .mapToInt(Game::getPersonalRating)
                .average()
                .orElse(-1);
        avgRatingNum.setText(avg >= 0 ? String.format("%.1f", avg) : "—");

        // Status breakdown bars
        statusBreakdown.getChildren().clear();
        Map<Game.PlayStatus, Long> counts = all.stream()
                .filter(g -> g.getPlayStatus() != null)
                .collect(Collectors.groupingBy(Game::getPlayStatus, Collectors.counting()));

        Map<Game.PlayStatus, String> barColors = Map.of(
                Game.PlayStatus.PLAYING,   "#5a9e5a",
                Game.PlayStatus.COMPLETED, "#c9a84c",
                Game.PlayStatus.BACKLOG,   "#5a5abf",
                Game.PlayStatus.WISHLIST,  "#bf5abf",
                Game.PlayStatus.DROPPED,   "#bf5a5a"
        );

        for (Game.PlayStatus status : Game.PlayStatus.values()) {
            long count = counts.getOrDefault(status, 0L);
            if (count == 0) continue;

            double pct = all.isEmpty() ? 0 : (count * 100.0 / all.size());

            HBox row = new HBox(8);
            row.setAlignment(Pos.CENTER_LEFT);

            Region bar = new Region();
            bar.setPrefHeight(6);
            bar.setPrefWidth(pct * 1.4);
            bar.setStyle("-fx-background-color: " + barColors.getOrDefault(status, "#888")
                    + "; -fx-background-radius: 3;");

            Label lbl = new Label(status.name() + "  " + count);
            lbl.setStyle("-fx-text-fill: #666666; -fx-font-size: 11px;");

            row.getChildren().addAll(bar, lbl);
            statusBreakdown.getChildren().add(row);
        }

        // Recently added (last 5 by DB id)
        recentList.getChildren().clear();
        all.stream()
                .sorted(Comparator.comparingLong(Game::getId).reversed())
                .limit(5)
                .forEach(g -> recentList.getChildren().add(recentRow(g)));

        // Top rated
        topRatedList.getChildren().clear();
        all.stream()
                .filter(g -> g.getPersonalRating() != null)
                .sorted(Comparator.comparingInt(Game::getPersonalRating).reversed())
                .limit(5)
                .forEach(g -> topRatedList.getChildren().add(topRatedRow(g)));
    }

    /**
     * Creates a UI row representing a recently added game.
     *
     * Displays the game title and its genre.
     *
     * @param game the game to display
     * @return an HBox containing formatted game information
     */
    private HBox recentRow(Game game) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label(game.getTitle());
        title.setStyle("-fx-text-fill: #bbbbbb; -fx-font-size: 12px;");
        HBox.setHgrow(title, Priority.ALWAYS);

        Label genre = new Label(game.getGenre() != null ? game.getGenre() : "");
        genre.setStyle("-fx-text-fill: #555555; -fx-font-size: 11px;");

        row.getChildren().addAll(title, genre);
        return row;
    }

    /**
     * Creates a UI row representing a top-rated game.
     *
     * Displays the game title and its personal rating.
     *
     * @param game the game to display
     * @return an HBox containing formatted game information
     */
    private HBox topRatedRow(Game game) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label(game.getTitle());
        title.setStyle("-fx-text-fill: #bbbbbb; -fx-font-size: 12px;");
        HBox.setHgrow(title, Priority.ALWAYS);

        Label rating = new Label(game.getPersonalRating() + " / 100");
        rating.setStyle("-fx-text-fill: #e0c97a; -fx-font-size: 11px;");

        row.getChildren().addAll(title, rating);
        return row;
    }
}