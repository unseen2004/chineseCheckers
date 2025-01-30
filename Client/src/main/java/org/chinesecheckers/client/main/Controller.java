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

public class Controller {
    @FXML
    private Pane boardPane;
    @FXML
    private Label infoBar;

    private final List<Cell> cells = new ArrayList<>();
    private ServerConnectionHandler serverConnectionHandler;
    private Board board;
    private Player player;
    private boolean guiBlocked = true;


    @FXML
    private void initialize() {
        loadAllCellsFromBoard();
        showAlert("Connect to Server");
    }


    private void loadAllCellsFromBoard() {
        int x = 1;
        int y = 1;

        for (Node node : boardPane.getChildren()) {
            loadNodeAsCell(node, x, y);


            if (x == 13) {
                x = 1;
                y++;
            } else {
                x++;
            }
        }
    }

    private void loadNodeAsCell(Node node, int x, int y) {
        if (node instanceof Circle) {
            cells.add(new Cell(x, y, (Circle) node));
            node.setOnMouseClicked(this::onFieldClick);
        } else {
            throw new RuntimeException("Error");
        }
    }


    private void onFieldClick(MouseEvent event) {
        boolean eventIsCircle = (event.getSource() instanceof Circle);

        if (!guiBlocked && eventIsCircle) {
            Circle clickedCircleReference = (Circle) event.getSource();

            Cell clickedCell = getFieldByCircleReference(clickedCircleReference);
            makeClickOnCell(clickedCell);
        }
    }

    private Cell getFieldByCircleReference(Circle circle) {
        for (Cell cell : cells) {
            if (cell.circleEquals(circle)) return cell;
        }
        return null;
    }

    private void makeClickOnCell(Cell cell) {
        if (cell != null) {
            player.handleClickOnCell(cell.getX(), cell.getY());
        }
    }


    private void showError(String text) {
        Platform.runLater(() -> {
            infoBar.setStyle("-fx-background-color: red; -fx-alignment: center");
            infoBar.setTextFill(Color.WHITE);
            infoBar.setText(text);
        });
    }

    private void showAlert(String text) {
        Platform.runLater(() -> {
            infoBar.setStyle("-fx-background-color: orange; -fx-alignment: center");
            infoBar.setTextFill(Color.WHITE);
            infoBar.setText(text);
        });

    }

    private void showSuccess(String text) {
        Platform.runLater(() -> {
            infoBar.setStyle("-fx-background-color: green; -fx-alignment: center");
            infoBar.setTextFill(Color.WHITE);
            infoBar.setText(text);
        });
    }

    private void connectAndStartGame(String host, int port) {
        try {
            createConnection(host, port);
            startGame();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void startGame() throws Exception {
        createBoard();
        createPlayer();
        player.startMatch();
    }

    private void createConnection(String host, int port) throws Exception {
        serverConnectionHandler = new ServerConnectionHandler(host, port);
    }

    private void createBoard() {
        board = new Board(cells);
        board.deselectCells();
    }

    private void createPlayer() throws Exception {
        try {
            player = new Player(serverConnectionHandler, board, this::connectGUI, this::showSuccess, this::showAlert, this::showError);
        } catch (Exception e) {
            throw new Exception("Error");
        }
    }

    private void connectGUI(boolean state) {
        this.guiBlocked = state;
    }

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
        portField.setText("4444");

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