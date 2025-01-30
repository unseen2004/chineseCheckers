package org.chinesecheckers.server.main;

public class Main {


    public static void main(String[] args) {
        Server server;
        try {
            server = new Server(4444);
            server.run();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}