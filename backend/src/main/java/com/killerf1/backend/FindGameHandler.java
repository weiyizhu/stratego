package com.killerf1.backend;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class FindGameHandler extends TextWebSocketHandler {

    private static final Logger logger = LogManager.getLogger();
    private final Matchmaker matchmaker;
    private final GameManager gameManager;

    public FindGameHandler(Matchmaker matchmaker, GameManager gameManager) {
        this.matchmaker = matchmaker;
        this.gameManager = gameManager;
    }

    /**
     * Handles connection requests for finding a new game to play. Creates a new
     * game if there are other players available.
     * 
     * @param session The websocket session where clients and the server
     *                communicates over
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.info("/findGame endpoint hit with client: {}", session.toString());

        WebSocketSession oppSession = matchmaker.findMatch(session);
        if (oppSession == null) {
            SocketHelper.send(session, "Waiting");
        } else {
            String gameId = gameManager.createNewGame();
            SocketHelper.broadcast(new WebSocketSession[] { session, oppSession }, gameId);
        }
    }
}
