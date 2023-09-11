package com.killerf1.backend;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.WebSocketSession;

interface GameSessionManager {
    public Game getGame(WebSocketSession session);

    public Game createNewGame(WebSocketSession session1, WebSocketSession session2);

    public void deleteGame(WebSocketSession session1, WebSocketSession session2);

    public Map<WebSocketSession, Game> getSessionToGame();
}

/**
 * This singleton class manages the creation/deletion of games and mapping
 * between games and WebSocketSessions
 */
public final class GameSessionManagerImpl implements GameSessionManager {
    private static volatile GameSessionManagerImpl instance;
    private final Map<WebSocketSession, Game> sessionToGame;

    private GameSessionManagerImpl() {
        this.sessionToGame = new ConcurrentHashMap<>();
    }

    public static GameSessionManagerImpl getInstance() {
        GameSessionManagerImpl result = instance;
        if (result != null) {
            return result;
        }
        synchronized (GameSessionManagerImpl.class) {
            if (instance == null) {
                instance = new GameSessionManagerImpl();
            }
            return instance;
        }
    }

    @Override
    public Game getGame(WebSocketSession session) {
        return sessionToGame.get(session);
    }

    @Override
    public Game createNewGame(WebSocketSession session1, WebSocketSession session2) {
        Game game = new Game(session1, session2);
        sessionToGame.put(session1, game);
        sessionToGame.put(session2, game);
        return game;
    }

    @Override
    public void deleteGame(WebSocketSession session1, WebSocketSession session2) {
        sessionToGame.remove(session1);
        sessionToGame.remove(session2);
    }

    @Override
    public Map<WebSocketSession, Game> getSessionToGame() {
        return this.sessionToGame;
    }

}
