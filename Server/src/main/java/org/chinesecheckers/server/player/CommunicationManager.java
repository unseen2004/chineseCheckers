package org.chinesecheckers.server.player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Manages communication with a player through a socket connection.
 */
class CommunicationManager {
    private final Socket m_socket;
    private final BufferedReader m_in;
    private final PrintWriter m_out;

    /**
     * Constructs a CommunicationManager with the specified socket.
     *
     * @param s the socket for communication
     * @throws Exception if an error occurs while setting up the input or output streams
     */
    CommunicationManager(Socket s) throws Exception {
        m_socket = s;
        try {
            m_in = new BufferedReader(new InputStreamReader(m_socket.getInputStream()));
            m_out = new PrintWriter(m_socket.getOutputStream(), true);
        } catch (Exception e) {
            throw new Exception("Player connection error");
        }
    }

    /**
     * Reads a line of text from the input stream.
     *
     * @return the line of text read
     * @throws Exception if an error occurs while reading the line
     */
    String readLine() throws Exception {
        return m_in.readLine();
    }

    /**
     * Writes a line of text to the output stream.
     *
     * @param line the line of text to write
     */
    void writeLine(String line) {
        m_out.println(line);
    }
}