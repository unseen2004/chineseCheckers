package org.chinesecheckers.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Configuration class for server settings.
 */
@Configuration
public class ServerConfig {

    /**
     * Creates and returns the server port from the environment properties.
     *
     * @param env the environment containing the properties
     * @return the server port
     */
    @Bean
    public int serverPort(Environment env) {
        return Integer.parseInt(env.getProperty("server.port", "8080"));
    }
}