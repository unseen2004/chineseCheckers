// Main.java
package org.chinesecheckers.server.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "org.chinesecheckers.server")
@EnableJpaRepositories("org.chinesecheckers.server.repository")
@EntityScan("org.chinesecheckers.server.model")
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Main.class, args);
        try {
            Server server = context.getBean(Server.class);
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}// Main.java