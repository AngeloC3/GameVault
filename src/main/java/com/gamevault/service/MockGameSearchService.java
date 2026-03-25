package com.gamevault.service;

import com.gamevault.model.Game;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mock implementation of the GameSearchService.
 * Provides a predefined list of games for testing or development purposes
 * without relying on an external API.
 */
@Service("mockGameSearchService")
public class MockGameSearchService implements GameSearchService {

    private static final List<Game> MOCK_GAMES = List.of(
            mockGame("1", "The Witcher 3: Wild Hunt", "RPG", "PC", "2015-05-19",
                    4.7, "https://via.placeholder.com/300x200?text=Witcher+3"),
            mockGame("2", "Red Dead Redemption 2", "Action-Adventure", "PS4", "2018-10-26",
                    4.6, "https://via.placeholder.com/300x200?text=RDR2"),
            mockGame("3", "Hollow Knight", "Metroidvania", "PC", "2017-02-24",
                    4.5, "https://via.placeholder.com/300x200?text=Hollow+Knight"),
            mockGame("4", "Elden Ring", "Action RPG", "PC", "2022-02-25",
                    4.8, "https://via.placeholder.com/300x200?text=Elden+Ring"),
            mockGame("5", "Hades", "Roguelike", "PC", "2020-09-17",
                    4.6, "https://via.placeholder.com/300x200?text=Hades"),
            mockGame("6", "Celeste", "Platformer", "PC", "2018-01-25",
                    4.5, "https://via.placeholder.com/300x200?text=Celeste"),
            mockGame("7", "God of War", "Action-Adventure", "PS4", "2018-04-20",
                    4.8, "https://via.placeholder.com/300x200?text=God+of+War"),
            mockGame("8", "Stardew Valley", "Simulation", "PC", "2016-02-26",
                    4.5, "https://via.placeholder.com/300x200?text=Stardew+Valley"),
            mockGame("9", "Cyberpunk 2077", "Action RPG", "PC", "2020-12-10",
                    4.2, "https://via.placeholder.com/300x200?text=Cyberpunk+2077"),
            mockGame("10", "Disco Elysium", "RPG", "PC", "2019-10-15",
                    4.7, "https://via.placeholder.com/300x200?text=Disco+Elysium")
    );

    /**
     * Searches the mock game list based on a query string.
     * Matches against title, genre, or platform fields.
     *
     * @param query the search term entered by the user
     * @return a list of games matching the query
     */
    @Override
    public List<Game> searchGames(String query) {
        String lower = query.toLowerCase();
        return MOCK_GAMES.stream()
                .filter(g -> g.getTitle().toLowerCase().contains(lower)
                        || g.getGenre().toLowerCase().contains(lower)
                        || g.getPlatform().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    /**
     * Creates a mock Game instance with the provided data.
     *
     * @param externalId the external identifier for the game
     * @param title the title of the game
     * @param genre the genre of the game
     * @param platform the platform the game is available on
     * @param releaseDate the release date of the game
     * @param rating the community rating of the game
     * @param coverUrl the URL of the game's cover image
     * @return a populated Game object
     */
    private static Game mockGame(String externalId, String title, String genre,
                                 String platform, String releaseDate,
                                 double rating, String coverUrl) {
        Game g = new Game();
        g.setExternalId(externalId);
        g.setTitle(title);
        g.setGenre(genre);
        g.setPlatform(platform);
        g.setReleaseDate(releaseDate);
        g.setCommunityRating(rating);
        g.setCoverImageUrl(coverUrl);
        return g;
    }
}