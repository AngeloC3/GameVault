package com.gamevault.config;

import com.gamevault.service.GameSearchService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration class responsible for determining which GameSearchService
 * implementation should be used at runtime.
 *
 * This class defines a primary GameSearchService bean that dynamically
 * selects between multiple implementations based on the application
 * configuration property "game.search.service".
 *
 * Supported values:
 * - "rawg": Uses the RAWG-based implementation
 * - Any other value (default: "mock"): Uses the mock implementation
 */
@Configuration
public class GameSearchConfig {

    /**
     * Creates and exposes the active GameSearchService bean.
     *
     * This method selects between the provided mock and rawg
     * implementations based on the "game.search.service" property.
     *
     * If the property value is "rawg", the RAWG service is returned;
     * otherwise, the mock service is used as the default.
     *
     * @param mock the mock implementation of GameSearchService,
     *             injected using the qualifier "mockGameSearchService"
     * @param rawg the RAWG API-based implementation of GameSearchService,
     *             injected using the qualifier "rawgGameSearchService"
     * @param activeService the configuration property that determines which service to use;
     *                      defaults to "mock" if not specified
     * @return the selected GameSearchService implementation to be used as the primary bean
     */
    @Bean
    @Primary
    public GameSearchService activeGameSearchService(
            @Qualifier("mockGameSearchService") GameSearchService mock,
            @Qualifier("rawgGameSearchService") GameSearchService rawg,
            @Value("${game.search.service:mock}") String activeService) {

        return activeService.equals("rawg") ? rawg : mock;
    }
}