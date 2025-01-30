package org.chinesecheckers.server.player;

public class PlayerLeftException extends Exception {
    PlayerLeftException(String msg) {
        super(msg);
    }
}