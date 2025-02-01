package org.chinesecheckers.server.config;

import org.chinesecheckers.server.serverBoard.BoardFactory;
import org.chinesecheckers.server.serverBoard.DefaultBoardFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for creating a BoardFactory bean.
 */
@Configuration
public class BoardFactoryConfig {

    /**
     * Creates and returns a BoardFactory bean.
     *
     * @return a BoardFactory instance
     */
    @Bean
    public BoardFactory boardFactory() {
        return new DefaultBoardFactory();
    }
}