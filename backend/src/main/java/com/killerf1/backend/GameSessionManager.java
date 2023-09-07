package com.killerf1.backend;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.WebSocketSession;

/**
 * This singleton class manages the creation/deletion of games and mapping
 * between games and WebSocketSessions
 */
public final class GameSessionManager {
    private static volatile GameSessionManager instance;
    private Map<WebSocketSession, Game> sessionToGame;

    private GameSessionManager() {
        this.sessionToGame = new ConcurrentHashMap<>();
    }

    public static GameSessionManager getInstance() {
        GameSessionManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized (GameSessionManager.class) {
            if (instance == null) {
                instance = new GameSessionManager();
            }
            return instance;
        }
    }

    public Game getGame(WebSocketSession session) {
        return sessionToGame.get(session);
    }

    public Game createNewGame(WebSocketSession session1, WebSocketSession session2) {
        Game game = new Game(session1, session2);
        sessionToGame.put(session1, game);
        sessionToGame.put(session2, game);
        return game;
    }

    public void deleteGame(WebSocketSession session1, WebSocketSession session2) {
        sessionToGame.remove(session1);
        sessionToGame.remove(session2);
    }

    public Map<WebSocketSession, Game> getSessionToGame() {
        return this.sessionToGame;
    }

}
