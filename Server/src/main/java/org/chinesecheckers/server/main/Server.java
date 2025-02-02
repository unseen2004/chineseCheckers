package org.chinesecheckers.server.main;

import org.chinesecheckers.server.model.Game;
import org.chinesecheckers.server.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The Server class manages the server operations, including starting new games and replaying recorded games.
 */
@Component
class Server {
    private final ServerSocket m_serverSocket;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GameSession m_gameSession;
    private List<Socket> m_playerSockets;

    private int m_numberOfPlayers = 1;
    private String m_gameMode = "default"; // Add game mode
    private int m_numberOfBots = 0;
    private int m_sleepDuration = 400; // Default sleep duration

    /**
     * Constructs a Server with the specified port.
     *
     * @param port the port to bind the server socket to
     * @throws Exception if an error occurs while starting the server
     */
    Server(int port) throws Exception {
        System.out.println("Server is starting");
        try {
            m_serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            throw new Exception("Port error " + port);
        }
    }

    /**
     * Runs the server, allowing users to start new games or replay recorded games.
     */
    public void run() {
        while (true) {
            try {
                System.out.println("Choose an option: 1) Start new game 2) Replay recorded game");
                Scanner scanner = new Scanner(System.in);
                int choice = scanner.nextInt();

                if (choice == 1) {
                    readNumberOfPlayers();
                    readNumberOfBots();
                    if (m_numberOfBots > 0) {
                        readSleepDuration();
                    }
                    m_numberOfPlayers -= m_numberOfBots;
                    readGameMode();
                    startMatch(m_numberOfPlayers, m_gameMode, m_numberOfBots, m_sleepDuration);
                } else if (choice == 2) {
                    replayRecordedGame();
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

            closeAllSockets();
            m_playerSockets = null;
        }
    }

    /**
     * Starts a new match with the specified parameters.
     *
     * @param numberOfPlayers the number of players
     * @param gameMode        the game mode
     * @param numberOfBots    the number of bots
     * @param sleepDuration   the sleep duration for bots
     * @throws Exception if an error occurs while starting the match
     */
    private void startMatch(int numberOfPlayers, String gameMode, int numberOfBots, int sleepDuration) throws Exception {
        System.out.println("Game starts: " + numberOfPlayers + " players and " + numberOfBots + " bots");

        createMatch(numberOfPlayers, gameMode, numberOfBots, sleepDuration);
        m_gameSession.start();
    }

    /**
     * Creates a new match by connecting players and initializing the game session.
     *
     * @param numberOfPlayers the number of players
     * @param gameMode        the game mode
     * @param numberOfBots    the number of bots
     * @param sleepDuration   the sleep duration for bots
     * @throws Exception if an error occurs while creating the match
     */
    private void createMatch(int numberOfPlayers, String gameMode, int numberOfBots, int sleepDuration) throws Exception {
        connectPlayers(numberOfPlayers);
        m_gameSession.initialize(m_playerSockets, gameMode, numberOfBots, sleepDuration);
    }

    /**
     * Connects the specified number of players to the server.
     *
     * @param numberOfPlayersToConnect the number of players to connect
     * @throws Exception if an error occurs while connecting players
     */
    private void connectPlayers(int numberOfPlayersToConnect) throws Exception {
        m_playerSockets = new ArrayList<>();
        try {
            for (int i = 0; i < numberOfPlayersToConnect; i++) {
                System.out.println("Waiting for player " + (i + 1));
                m_playerSockets.add(m_serverSocket.accept());
            }
        } catch (Exception e) {
            throw new Exception("Error " + e.getMessage());
        }
    }

    /**
     * Replays a recorded game by selecting a game from the repository and replaying its moves.
     */
    private void replayRecordedGame() {
        List<Game> games = gameRepository.findAllGames();
        if (games.isEmpty()) {
            System.out.println("No recorded games found. Please create a new game.");
            try {
                readNumberOfPlayers();
                readNumberOfBots();
                if (m_numberOfBots > 0) {
                    readSleepDuration();
                }
                m_numberOfPlayers -= m_numberOfBots;
                readGameMode();
                startMatch(m_numberOfPlayers, m_gameMode, m_numberOfBots, m_sleepDuration);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            return;
        }

        System.out.println("Choose a game to replay:");
        for (int i = 0; i < games.size(); i++) {
            System.out.println((i + 1) + ") Game ID: " + games.get(i).getId() + ", Mode: " + games.get(i).getMode());
        }

        Scanner scanner = new Scanner(System.in);
        int gameChoice = scanner.nextInt();
        if (gameChoice < 1 || gameChoice > games.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        Game selectedGame = games.get(gameChoice - 1);
        System.out.println("Replaying game ID: " + selectedGame.getId() + ", Mode: " + selectedGame.getMode());

        try {
            m_playerSockets = new ArrayList<>(); // Initialize m_playerSockets as an empty list
            int numberOfPlayers = selectedGame.getNumberOfPlayers(); // Retrieve the number of players from the game
            int numberOfBots = selectedGame.getNumberOfBots(); // Retrieve the number of bots from the game
            m_gameSession.initialize(new ArrayList<>(), selectedGame.getMode(), numberOfBots, m_sleepDuration);
            m_gameSession.replayMoves(selectedGame.getId());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Reads the number of players from the user input.
     */
    private void readNumberOfPlayers() {
        boolean inputCorrect;
        do {
            inputCorrect = true;
            System.out.println("Number of Players(2,3,4,6):");
            Scanner scanner = new Scanner(System.in);

            try {
                int newNumberOfRealPlayers = scanner.nextInt();
                if (newNumberOfRealPlayers == 2 || newNumberOfRealPlayers == 3 || newNumberOfRealPlayers == 4 || newNumberOfRealPlayers == 6) {
                    m_numberOfPlayers = newNumberOfRealPlayers;
                } else {
                    System.out.println("Wrong number. Should be 2||3||4||6");
                    inputCorrect = false;
                }
            } catch (Exception e) {
                System.out.println("Wrong number.");
                inputCorrect = false;
            }
        } while (!inputCorrect);
    }

    /**
     * Reads the number of bots from the user input.
     */
    private void readNumberOfBots() {
        boolean inputCorrect;
        do {
            inputCorrect = true;
            System.out.println("Number of Bots (0-" + m_numberOfPlayers + "):");
            Scanner scanner = new Scanner(System.in);

            try {
                int newNumberOfBots = scanner.nextInt();
                if (newNumberOfBots >= 0 && newNumberOfBots <= m_numberOfPlayers) {
                    m_numberOfBots = newNumberOfBots;
                } else {
                    System.out.println("Wrong number. Should be between 0 and " + m_numberOfPlayers);
                    inputCorrect = false;
                }
            } catch (Exception e) {
                System.out.println("Wrong number.");
                inputCorrect = false;
            }
        } while (!inputCorrect);
    }

    /**
     * Reads the sleep duration for bots from the user input.
     */
    private void readSleepDuration() {
        boolean inputCorrect;
        do {
            inputCorrect = true;
            System.out.println("Enter sleep duration for bots (in milliseconds):");
            Scanner scanner = new Scanner(System.in);

            try {
                int newSleepDuration = scanner.nextInt();
                if (newSleepDuration > 0) {
                    m_sleepDuration = newSleepDuration;
                } else {
                    System.out.println("Wrong number. Should be greater than 0");
                    inputCorrect = false;
                }
            } catch (Exception e) {
                System.out.println("Wrong number.");
                inputCorrect = false;
            }
        } while (!inputCorrect);
    }

    /**
     * Reads the game mode from the user input.
     */
    private void readGameMode() {
        boolean inputCorrect;
        do {
            inputCorrect = true;
            System.out.println("Game Mode (default / diamond(ダイヤモンドゲーム)):");
            Scanner scanner = new Scanner(System.in);

            try {
                String mode = scanner.next();
                if ("default".equalsIgnoreCase(mode) || "diamond".equalsIgnoreCase(mode)) {
                    m_gameMode = mode;
                } else {
                    System.out.println("Wrong mode. Should be default or diamond(ダイヤモンドゲーム)");
                    inputCorrect = false;
                }
            } catch (Exception e) {
                System.out.println("Wrong mode.");
                inputCorrect = false;
            }
        } while (!inputCorrect);
    }

    /**
     * Closes all player sockets.
     */
    private void closeAllSockets() {
        for (Socket socket : m_playerSockets) {
            try {
                socket.close();
            } catch (Exception ignored) {
            }
        }
    }
}