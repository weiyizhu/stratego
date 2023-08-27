package com.killerf1.backend;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * This class implements business logic than handles various socket events
 */
public class MoveHandler extends TextWebSocketHandler {

    private static final Logger logger = LogManager.getLogger();

    /**
     * Handles incoming client messages and updates the game state accordingly
     * 
     * @param session The websocket session where clients and the server
     *                communicates over
     * @param message The text message clients send
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        logger.info("Message received: " + message.toString());
        try {
            session.sendMessage(new TextMessage("message received"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.info("Connection established with " + session.getRemoteAddress());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        logger.info("Connection closed with " + session.getRemoteAddress());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        logger.error("Transport error: " + exception.toString());
    }
}
