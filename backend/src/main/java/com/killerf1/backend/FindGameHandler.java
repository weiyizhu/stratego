package com.killerf1.backend;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class FindGameHandler extends TextWebSocketHandler {

    private static final Logger logger = LogManager.getLogger();

    /**
     * Handles connection requests for finding a new game to play. Creates a new
     * game if there
     * are other players available.
     * 
     * @param session The websocket session where clients and the server
     *                communicates over
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.info("/findGame endpoint hit with client address: {}", session.getRemoteAddress());

        Matchmaker matchMaker = Matchmaker.getInstance();
        WebSocketSession oppSession = matchMaker.findMatch(session);
        if (oppSession == null) {
            SocketHelper.send(session, "Waiting");
        } else {
            GameSessionManager gameSessionManager = GameSessionManager.getInstance();
            Game game = gameSessionManager.createNewGame(session, oppSession);
            SocketHelper.broadcast(game.getSessions(), "Game found");
        }
    }
}
