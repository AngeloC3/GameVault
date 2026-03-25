package com.gamevault.repository;

import com.gamevault.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing and managing Game entities.
 * Provides CRUD operations and custom query methods using Spring Data JPA.
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    /**
     * Finds all games with a specific play status.
     *
     * @param playStatus the play status to filter by
     * @return a list of games matching the given play status
     */
    List<Game> findByPlayStatus(Game.PlayStatus playStatus);

    /**
     * Finds a game by its external ID.
     *
     * @param externalId the external unique identifier
     * @return an Optional containing the game if found, otherwise empty
     */
    Optional<Game> findByExternalId(String externalId);

    /**
     * Searches for games whose titles contain the given text, ignoring case.
     *
     * @param title the text to search for within game titles
     * @return a list of matching games
     */
    List<Game> findByTitleContainingIgnoreCase(String title);
}