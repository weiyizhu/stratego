package com.killerf1.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.socket.WebSocketSession;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class GameSessionManagerTests {
    
    @Mock
    WebSocketSession session1;
    @Mock
    WebSocketSession session2;

    @Test
    void createSingleInstance () {
        GameSessionManager gameSessionManager = GameSessionManager.getInstance();
        GameSessionManager gameSessionManager2 = GameSessionManager.getInstance();
        gameSessionManager.createNewGame(session1, session2);
        assertEquals(gameSessionManager.getSessionToGame(), gameSessionManager2.getSessionToGame());
    }

    @Test
    void createNewGameTest() {
        GameSessionManager gameSessionManager = GameSessionManager.getInstance();
        gameSessionManager.createNewGame(session1, session2);
        assertEquals(gameSessionManager.getGame(session1), gameSessionManager.getGame(session2));
    }

    @Test
    void deleteGameTest() {
        GameSessionManager gameSessionManager = GameSessionManager.getInstance();
        gameSessionManager.createNewGame(session1, session2);
        gameSessionManager.deleteGame(session1, session2);
        assertNull(gameSessionManager.getGame(session1));
        assertNull(gameSessionManager.getGame(session2));
    }

}
