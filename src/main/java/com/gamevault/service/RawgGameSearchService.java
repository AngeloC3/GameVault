package com.gamevault.service;

import com.gamevault.model.Game;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the GameSearchService that retrieves game data
 * from the RAWG external API.
 */
@Service("rawgGameSearchService")
public class RawgGameSearchService implements GameSearchService {

    @Value("${rawg.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "https://api.rawg.io/api";

    /**
     * Searches for games using the RAWG API based on a query string.
     * Maps the API response into a list of Game objects.
     *
     * @param query the search term entered by the user
     * @return a list of games matching the query
     */
    public List<Game> searchGames(String query) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/games")
                .queryParam("key", apiKey)
                .queryParam("search", query)
                .queryParam("page_size", 10)
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        List<Game> results = new ArrayList<>();

        if (response == null || !response.containsKey("results")) return results;

        List<Map<String, Object>> rawResults = (List<Map<String, Object>>) response.get("results");

        for (Map<String, Object> raw : rawResults) {
            Game game = new Game();
            game.setExternalId(String.valueOf(raw.get("id")));
            game.setTitle((String) raw.get("name"));
            game.setReleaseDate((String) raw.get("released"));
            game.setCoverImageUrl((String) raw.get("background_image"));

            Object rating = raw.get("rating");
            if (rating instanceof Number) {
                game.setCommunityRating(((Number) rating).doubleValue());
            }

            List<Map<String, Object>> genres = (List<Map<String, Object>>) raw.get("genres");
            if (genres != null && !genres.isEmpty()) {
                game.setGenre((String) genres.get(0).get("name"));
            }

            List<Map<String, Object>> platforms = (List<Map<String, Object>>) raw.get("platforms");
            if (platforms != null && !platforms.isEmpty()) {
                Map<String, Object> platformObj = (Map<String, Object>) platforms.get(0).get("platform");
                if (platformObj != null) {
                    game.setPlatform((String) platformObj.get("name"));
                }
            }

            results.add(game);
        }

        return results;
    }
}