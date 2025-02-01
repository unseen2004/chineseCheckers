package org.chinesecheckers.server.config;

import org.chinesecheckers.server.movement.DefaultMovementStrategy;
import org.chinesecheckers.server.movement.MovementStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for creating a MovementStrategy bean.
 */
@Configuration
public class MovementStrategyConfig {

    /**
     * Creates and returns a MovementStrategy bean.
     *
     * @return a MovementStrategy instance
     */
    @Bean
    public MovementStrategy movementStrategy() {
        return new DefaultMovementStrategy();
    }
}