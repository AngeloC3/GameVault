package com.gamevault.service;

import com.gamevault.model.Game;
import java.util.List;

/**
 * Service interface for searching games from an external source or API.
 */
public interface GameSearchService {

    /**
     * Searches for games based on a query string.
     *
     * @param query the search term entered by the user
     * @return a list of games matching the query
     */
    List<Game> searchGames(String query);
}