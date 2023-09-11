package com.killerf1.backend;

import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.TextMessage;

/**
 * This class contains static methods that deal with communications over the
 * socket
 */
public class SocketHelper {

    private static final Logger logger = LogManager.getLogger();

    public static void broadcast(WebSocketSession[] sessions, String msg) {
        try {
            for (WebSocketSession session : sessions) {
                session.sendMessage(new TextMessage(msg));
            }
        } catch (IOException e) {
            logger.catching(e);
        }
    }

    public static void send(WebSocketSession session, String msg) {
        try {
            session.sendMessage(new TextMessage(msg));
        } catch (IOException e) {
            logger.catching(e);
        }
    }
}
