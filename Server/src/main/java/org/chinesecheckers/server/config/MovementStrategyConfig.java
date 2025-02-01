// MovementStrategyConfig.java
package org.chinesecheckers.server.config;

import org.chinesecheckers.server.movement.MovementStrategy;
import org.chinesecheckers.server.movement.DefaultMovementStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MovementStrategyConfig {

    @Bean
    public MovementStrategy movementStrategy() {
        return new DefaultMovementStrategy();
    }
}