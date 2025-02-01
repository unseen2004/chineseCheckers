// BoardFactoryConfig.java
package org.chinesecheckers.server.config;

import org.chinesecheckers.server.serverBoard.BoardFactory;
import org.chinesecheckers.server.serverBoard.DefaultBoardFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BoardFactoryConfig {

    @Bean
    public BoardFactory boardFactory() {
        return new DefaultBoardFactory();
    }
}