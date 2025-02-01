package org.chinesecheckers.client.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Main class for launching the Chinese Checkers application.
 */
public class Main extends Application {

    /**
     * Main method to launch the JavaFX application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the JavaFX application by setting up the primary stage.
     *
     * @param primaryStage the primary stage for this application
     * @throws Exception if an error occurs during loading the FXML file
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/chinesecheckers/client/board.fxml")));
        primaryStage.setTitle("Chinese Checkers");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}