package org.chinesecheckers.client.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ServerConnectionHandler {
    private final Socket m_socket;
    private final BufferedReader m_in;
    private final PrintWriter m_out;

    ServerConnectionHandler(String host, int port) throws Exception {
        try {
            m_socket = new Socket(host, port);
            m_out = new PrintWriter(m_socket.getOutputStream(), true);
            m_in = new BufferedReader(new InputStreamReader(m_socket.getInputStream()));
        } catch (Exception e) {
            throw new Exception("Server error");
        }
    }


    synchronized String readLine() throws Exception {
        try {
            return m_in.readLine();
        } catch (Exception e) {
            throw new Exception("Connection Lost");
        }
    }

    void writeLine(String line) {
        m_out.println(line);
    }
}