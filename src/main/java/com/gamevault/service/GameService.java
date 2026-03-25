package com.gamevault.service;

import com.gamevault.model.Game;
import com.gamevault.repository.GameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for handling business logic related to games.
 * Acts as a bridge between controllers, repositories, and external search services.
 */
@Service
public class GameService {
    private final GameRepository gameRepository;
    private final GameSearchService gameSearchService;

    /**
     * Constructs a GameService with required dependencies.
     *
     * @param gameRepository the repository used for database operations
     * @param activeGameSearchService the service used to search external game data
     */
    public GameService(GameRepository gameRepository, GameSearchService activeGameSearchService) {
        this.gameRepository = gameRepository;
        this.gameSearchService = activeGameSearchService;
    }

    /**
     * Searches for games using an external search service.
     *
     * @param query the search term entered by the user
     * @return a list of games matching the query
     */
    public List<Game> searchGames(String query) {
        return gameSearchService.searchGames(query);
    }

    /**
     * Adds a game to the user's library if it does not already exist.
     *
     * @param game the game to add
     * @return the existing or newly saved game
     */
    public Game addToLibrary(Game game) {
        return gameRepository.findByExternalId(game.getExternalId())
                .orElseGet(() -> gameRepository.save(game));
    }

    /**
     * Retrieves all games in the user's library.
     *
     * @return a list of all stored games
     */
    public List<Game> getLibrary() {
        return gameRepository.findAll();
    }

    /**
     * Retrieves games filtered by play status.
     *
     * @param status the play status to filter by
     * @return a list of games with the specified status
     */
    public List<Game> getByStatus(Game.PlayStatus status) {
        return gameRepository.findByPlayStatus(status);
    }

    /**
     * Updates an existing game in the database.
     *
     * @param game the game to update
     * @return the updated game
     */
    public Game updateGame(Game game) {
        return gameRepository.save(game);
    }

    /**
     * Deletes a game from the database by its ID.
     *
     * @param id the ID of the game to delete
     */
    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }
}