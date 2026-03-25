package com.gamevault.config;

import com.gamevault.model.Game;
import com.gamevault.repository.GameRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * DataSeeder is a Spring component that initializes the database
 * with a predefined set of Game records when the application starts.
 *
 * It implements CommandLineRunner, allowing execution of logic
 * immediately after the Spring Boot application context is loaded.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final GameRepository gameRepository;

    /**
     * Constructor for injecting the GameRepository dependency.
     *
     * @param gameRepository the repository used to persist Game entities
     */
    public DataSeeder(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    /**
     * Runs automatically at application startup.
     * Seeds the database with sample game data if it is empty.
     *
     * @param args command-line arguments passed during application startup
     */
    @Override
    public void run(String... args) {
        // gameRepository.deleteAll();
        if (gameRepository.count() > 0) return;

        gameRepository.save(game("1", "Elden Ring", "Action RPG", "PC",
                "2022-02-25", 48, Game.PlayStatus.PLAYING, 90,
                "Incredible open world. Malenia is brutal.",
                "https://placehold.co/170x100/1e2a1e/5a9e5a?text=Elden+Ring"));

        gameRepository.save(game("2", "The Witcher 3: Wild Hunt", "RPG", "PC",
                "2015-05-19", 47, Game.PlayStatus.COMPLETED, 100,
                "Best RPG I've ever played. Hearts of Stone DLC is a masterpiece.",
                "https://placehold.co/170x100/2a1e1e/9e5a5a?text=Witcher+3"));

        gameRepository.save(game("3", "Hollow Knight", "Metroidvania", "PC",
                "2017-02-24", 45, Game.PlayStatus.BACKLOG, null,
                null,
                "https://placehold.co/170x100/1e1e2a/5a5abf?text=Hollow+Knight"));

        gameRepository.save(game("4", "Hades", "Roguelike", "PC",
                "2020-09-17", 46, Game.PlayStatus.COMPLETED, 90,
                "Perfect gameplay loop. Couldn't put it down.",
                "https://placehold.co/170x100/2a1f0a/c9a84c?text=Hades"));

        gameRepository.save(game("5", "Celeste", "Platformer", "PC",
                "2018-01-25", 45, Game.PlayStatus.COMPLETED, 80,
                "Brutally hard but incredibly rewarding.",
                "https://placehold.co/170x100/2a1a2a/bf5abf?text=Celeste"));

        gameRepository.save(game("6", "Cyberpunk 2077", "Action RPG", "PC",
                "2020-12-10", 42, Game.PlayStatus.DROPPED, 60,
                "Rough launch. Might revisit after more patches.",
                "https://placehold.co/170x100/1e2a2a/5abfbf?text=Cyberpunk"));

        gameRepository.save(game("7", "Stardew Valley", "Simulation", "PC",
                "2016-02-26", 45, Game.PlayStatus.WISHLIST, null,
                null,
                "https://placehold.co/170x100/1a2a1a/7abf5a?text=Stardew"));

        gameRepository.save(game("8", "God of War", "Action-Adventure", "PS4",
                "2018-04-20", 48, Game.PlayStatus.COMPLETED, 100,
                "Kratos and Atreus. Enough said.",
                "https://placehold.co/170x100/2a1a1a/bf5a5a?text=God+of+War"));
    }

    /**
     * Helper method to construct a Game object with the provided attributes.
     *
     * @param externalId       external identifier for the game
     * @param title            the title of the game
     * @param genre            the genre of the game
     * @param platform         the platform the game is played on (e.g., PC, PS4)
     * @param releaseDate      the release date of the game (ISO string format)
     * @param communityRating  average community rating (e.g., from external sources)
     * @param status           current play status of the game
     * @param personalRating   user's personal rating (nullable)
     * @param notes            optional notes or comments about the game
     * @param coverUrl         URL to the game's cover image
     * @return a populated Game object ready for persistence
     */
    private Game game(String externalId, String title, String genre, String platform,
                      String releaseDate, double communityRating, Game.PlayStatus status,
                      Integer personalRating, String notes, String coverUrl) {
        Game g = new Game();
        g.setExternalId(externalId);
        g.setTitle(title);
        g.setGenre(genre);
        g.setPlatform(platform);
        g.setReleaseDate(releaseDate);
        g.setCommunityRating(communityRating);
        g.setPlayStatus(status);
        g.setPersonalRating(personalRating);
        g.setNotes(notes);
        g.setCoverImageUrl(coverUrl);
        return g;
    }
}