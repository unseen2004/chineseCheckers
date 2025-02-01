package org.chinesecheckers.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class ServerConfig {

    @Bean
    public int serverPort(Environment env) {
        return Integer.parseInt(env.getProperty("server.port", "8080"));
    }
}