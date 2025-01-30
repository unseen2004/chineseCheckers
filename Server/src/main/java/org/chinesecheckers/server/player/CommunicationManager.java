package org.chinesecheckers.server.player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class CommunicationManager {
    private final Socket m_socket;
    private final BufferedReader m_in;
    private final PrintWriter m_out;

    CommunicationManager(Socket s) throws Exception {
        m_socket = s;
        try {
            m_in = new BufferedReader(new InputStreamReader(m_socket.getInputStream()));
            m_out = new PrintWriter(m_socket.getOutputStream(), true);
        } catch (Exception e) {
            throw new Exception("Player connection error");
        }
    }


    String readLine() throws Exception {
        return m_in.readLine();
    }


    void writeLine(String line) {
        m_out.println(line);
    }
}