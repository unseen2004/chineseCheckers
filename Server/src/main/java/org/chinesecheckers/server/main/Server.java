package org.chinesecheckers.server.main;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Server {
    private final ServerSocket m_serverSocket;
    private List<Socket> m_playerSockets;
    private GameSession m_gameSession;

    private int m_numberOfPlayers = 1;
    private String m_gameMode = "default"; // Add game mode
    private int m_numberOfBots = 0;

    Server(int port) throws Exception {
        System.out.println("Server is starting");
        try {
            m_serverSocket = new ServerSocket(port);
        } catch (Exception e) {
            throw new Exception("Port error " + port);
        }
    }

    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                readNumberOfPlayers();
                readNumberOfBots();
                m_numberOfPlayers -= m_numberOfBots;
                readGameMode();
                startMatch(m_numberOfPlayers, m_gameMode, m_numberOfBots);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

            closeAllSockets();
            m_playerSockets = null;
        }
    }

    private void startMatch(int numberOfPlayers, String gameMode, int numberOfBots) throws Exception {
        System.out.println("Game starts: " + numberOfPlayers + " players and " + numberOfBots + " bots");

        createMatch(numberOfPlayers, gameMode, numberOfBots);
        m_gameSession.start();


    }

    private void createMatch(int numberOfPlayers, String gameMode, int numberOfBots) throws Exception {
        connectPlayers(numberOfPlayers);
        m_gameSession = new GameSession(m_playerSockets, gameMode, numberOfBots);
    }

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

    private void readGameMode() {
        boolean inputCorrect;
        do {
            inputCorrect = true;
            System.out.println("Game Mode (default/diamond):");
            Scanner scanner = new Scanner(System.in);

            try {
                String mode = scanner.next();
                if ("default".equalsIgnoreCase(mode) || "diamond".equalsIgnoreCase(mode)) {
                    m_gameMode = mode;
                } else {
                    System.out.println("Wrong mode. Should be default or diamond");
                    inputCorrect = false;
                }
            } catch (Exception e) {
                System.out.println("Wrong mode.");
                inputCorrect = false;
            }
        } while (!inputCorrect);
    }

    private void closeAllSockets() {
        for (Socket socket : m_playerSockets) {
            try {
                socket.close();
            } catch (Exception ignored) {
            }
        }
    }
}