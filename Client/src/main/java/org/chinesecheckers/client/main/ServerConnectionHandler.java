package org.chinesecheckers.client.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Handles the connection to the server for the Chinese Checkers game.
 */
class ServerConnectionHandler {
    private final Socket m_socket;
    private final BufferedReader m_in;
    private final PrintWriter m_out;

    /**
     * Constructs a ServerConnectionHandler with the specified host and port.
     *
     * @param host the server host
     * @param port the server port
     * @throws Exception if an error occurs while connecting to the server
     */
    ServerConnectionHandler(String host, int port) throws Exception {
        try {
            m_socket = new Socket(host, port);
            m_out = new PrintWriter(m_socket.getOutputStream(), true);
            m_in = new BufferedReader(new InputStreamReader(m_socket.getInputStream()));
        } catch (Exception e) {
            throw new Exception("Server error");
        }
    }

    /**
     * Reads a line of text from the server.
     *
     * @return the line of text read from the server
     * @throws Exception if an error occurs while reading from the server
     */
    synchronized String readLine() throws Exception {
        try {
            return m_in.readLine();
        } catch (Exception e) {
            throw new Exception("Connection Lost");
        }
    }

    /**
     * Writes a line of text to the server.
     *
     * @param line the line of text to write to the server
     */
    void writeLine(String line) {
        m_out.println(line);
    }
}