package org.chinesecheckers.client.main;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Pair;
import org.chinesecheckers.client.board.Board;
import org.chinesecheckers.client.board.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller class for handling the Chinese Checkers game UI and interactions.
 */
public class Controller {
    private final List<Cell> m_cells = new ArrayList<>();
    @FXML
    private Pane m_boardPane;
    @FXML
    private Label m_infoBar;
    private ServerConnectionHandler m_serverConnectionHandler;
    private Board m_board;
    private Player m_player;
    private boolean m_guiBlocked = true;

    /**
     * Initializes the controller by loading all cells from the board and showing the connection alert.
     */
    @FXML
    private void initialize() {
        loadAllCellsFromBoard();
        showAlert("Connect to Server");
    }

    /**
     * Loads all cells from the board pane and sets their coordinates.
     */
    private void loadAllCellsFromBoard() {
        int x = 1;
        int y = 1;

        for (Node node : m_boardPane.getChildren()) {
            loadNodeAsCell(node, x, y);

            if (x == 13) {
                x = 1;
                y++;
            } else {
                x++;
            }
        }
    }

    /**
     * Loads a node as a cell with the specified coordinates.
     *
     * @param node the node to load as a cell
     * @param x    the x-coordinate of the cell
     * @param y    the y-coordinate of the cell
     */
    private void loadNodeAsCell(Node node, int x, int y) {
        if (node instanceof Circle) {
            m_cells.add(new Cell(x, y, (Circle) node));
            node.setOnMouseClicked(this::onFieldClick);
        } else {
            throw new RuntimeException("Error");
        }
    }

    /**
     * Handles the field click event.
     *
     * @param event the mouse event
     */
    private void onFieldClick(MouseEvent event) {
        boolean eventIsCircle = (event.getSource() instanceof Circle);

        if (!m_guiBlocked && eventIsCircle) {
            Circle clickedCircleReference = (Circle) event.getSource();
            Cell clickedCell = getFieldByCircleReference(clickedCircleReference);
            makeClickOnCell(clickedCell);
        }
    }

    /**
     * Gets the cell by the circle reference.
     *
     * @param circle the circle reference
     * @return the cell corresponding to the circle reference
     */
    private Cell getFieldByCircleReference(Circle circle) {
        for (Cell cell : m_cells) {
            if (cell.circleEquals(circle)) return cell;
        }
        return null;
    }

    /**
     * Handles the click on a cell.
     *
     * @param cell the cell that was clicked
     */
    private void makeClickOnCell(Cell cell) {
        if (cell != null) {
            m_player.handleClickOnCell(cell.getM_x(), cell.getM_y());
        }
    }

    /**
     * Shows an error message in the info bar.
     *
     * @param text the error message
     */
    private void showError(String text) {
        Platform.runLater(() -> {
            m_infoBar.setStyle("-fx-background-color: red; -fx-alignment: center");
            m_infoBar.setTextFill(Color.WHITE);
            m_infoBar.setText(text);
        });
    }

    /**
     * Shows an alert message in the info bar.
     *
     * @param text the alert message
     */
    private void showAlert(String text) {
        Platform.runLater(() -> {
            m_infoBar.setStyle("-fx-background-color: orange; -fx-alignment: center");
            m_infoBar.setTextFill(Color.WHITE);
            m_infoBar.setText(text);
        });
    }

    /**
     * Shows a success message in the info bar.
     *
     * @param text the success message
     */
    private void showSuccess(String text) {
        Platform.runLater(() -> {
            m_infoBar.setStyle("-fx-background-color: green; -fx-alignment: center");
            m_infoBar.setTextFill(Color.WHITE);
            m_infoBar.setText(text);
        });
    }

    /**
     * Connects to the server and starts the game.
     *
     * @param host the server host
     * @param port the server port
     */
    private void connectAndStartGame(String host, int port) {
        try {
            createConnection(host, port);
            startGame();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    /**
     * Starts the game by creating the board and player, and starting the match.
     *
     * @throws Exception if an error occurs while starting the game
     */
    private void startGame() throws Exception {
        createBoard();
        createPlayer();
        m_player.startMatch();
    }

    /**
     * Creates a connection to the server.
     *
     * @param host the server host
     * @param port the server port
     * @throws Exception if an error occurs while creating the connection
     */
    private void createConnection(String host, int port) throws Exception {
        m_serverConnectionHandler = new ServerConnectionHandler(host, port);
    }

    /**
     * Creates the game board.
     */
    private void createBoard() {
        m_board = new Board(m_cells);
        m_board.deselectCells();
    }

    /**
     * Creates the player.
     *
     * @throws Exception if an error occurs while creating the player
     */
    private void createPlayer() throws Exception {
        try {
            m_player = new Player(m_serverConnectionHandler, m_board, this::connectGUI, this::showSuccess, this::showAlert, this::showError);
        } catch (Exception e) {
            throw new Exception("Error");
        }
    }

    /**
     * Connects or disconnects the GUI.
     *
     * @param state true to connect the GUI, false to disconnect
     */
    private void connectGUI(boolean state) {
        this.m_guiBlocked = state;
    }

    /**
     * Handles the new connection action.
     */
    @FXML
    private void onNewConnection() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();

        ButtonType connectButtonType = new ButtonType("Connect", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(connectButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField ipAddressField = new TextField();
        ipAddressField.setPromptText("IP ");
        ipAddressField.setText("localhost");
        TextField portField = new TextField();
        portField.setPromptText("port");
        portField.setText("8080");

        grid.add(new Label("IP:"), 0, 0);
        grid.add(ipAddressField, 1, 0);
        grid.add(new Label("Port:"), 0, 1);
        grid.add(portField, 1, 1);

        Node connectButton = dialog.getDialogPane().lookupButton(connectButtonType);
        connectButton.setDisable(false);

        ipAddressField.textProperty().addListener((observable, oldValue, newValue) -> connectButton.setDisable(newValue.trim().isEmpty()));

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(ipAddressField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == connectButtonType) {
                return new Pair<>(ipAddressField.getText(), portField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        String host;
        int port;
        if (result.isPresent()) {
            Pair<String, String> r = result.get();
            host = r.getKey();
            try {
                port = Integer.parseInt(r.getValue());
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");

                alert.showAndWait();
                return;
            }

            connectAndStartGame(host, port);
        }
    }
}