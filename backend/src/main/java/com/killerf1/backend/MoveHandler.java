package com.killerf1.backend;

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
    private final GameManager gameManager;

    public MoveHandler(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public Game getGameFromSession(WebSocketSession session) {
        try {
            String gameId = session.getUri().getQuery();
            Game game = gameManager.getGame(gameId);
            if (game == null) {
                throw new Exception();
            }
            return game;
        } catch (Exception e) {
            SocketHelper.send(session, "Game not found");
            return null;
        }
    }

    /**
     * Handles incoming client messages and updates the game state accordingly
     * 
     * @param session The websocket session where clients and the server
     *                communicates over
     * @param message The text message clients send
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        logger.info("Message received: {}", message.toString());
        Game game = getGameFromSession(session);
        if (game != null) {
            game.getState().handleClientInput(session, message.toString());
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.info("Connection established with {}", session.toString());
        Game game = getGameFromSession(session);
        if (game != null) {
            game.assignSideASession(session.getId());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        logger.info("Connection closed with {}", session.getRemoteAddress());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        logger.error("Transport error: {}", exception.toString());
    }
}
