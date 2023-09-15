package com.killerf1.backend;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.RandomStringUtils;

interface GameManager {
    public Game getGame(String gameId);

    public String createNewGame();

    public void deleteGame(String gameId);
}

/**
 * This singleton class manages the creation/deletion of games and mapping
 * between games and WebSocketSessions
 */
public final class GameManagerImpl implements GameManager {
    private static volatile GameManagerImpl instance;
    private final Map<String, Game> gameIdToGame;

    private GameManagerImpl() {
        this.gameIdToGame = new ConcurrentHashMap<>();
    }

    public static GameManagerImpl getInstance() {
        GameManagerImpl result = instance;
        if (result != null) {
            return result;
        }
        synchronized (GameManagerImpl.class) {
            if (instance == null) {
                instance = new GameManagerImpl();
            }
            return instance;
        }
    }

    @Override
    public Game getGame(String gameId) {
        return gameIdToGame.get(gameId);
    }

    @Override
    public String createNewGame() {
        String gameId = RandomStringUtils.randomAlphanumeric(5).toLowerCase();
        while (gameIdToGame.containsKey(gameId)) {
            gameId = RandomStringUtils.randomAlphanumeric(5).toLowerCase();
        }
        Game game = new Game(gameId);
        gameIdToGame.put(gameId, game);
        return gameId;
    }

    @Override
    public void deleteGame(String gameId) {
        gameIdToGame.remove(gameId);
    }
}
