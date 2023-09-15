package com.killerf1.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.socket.WebSocketSession;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameManagerImplTests {

    @Mock
    WebSocketSession session1;
    @Mock
    WebSocketSession session2;

    @Test
    @Order(1)
    void createSingleInstance() {
        GameManager gameManager = GameManagerImpl.getInstance();
        GameManager gameManager2 = GameManagerImpl.getInstance();
        String gameId = gameManager.createNewGame();
        assertEquals(gameManager.getGame(gameId), gameManager2.getGame(gameId));
    }

    @Test
    @Order(2)
    void createNewGameTest() {
        GameManager gameManager = GameManagerImpl.getInstance();
        String gameId = gameManager.createNewGame();
        assertEquals(gameManager.getGame(gameId), gameManager.getGame(gameId));
    }

    @Test
    @Order(4)
    void deleteGameTest() {
        GameManager gameManager = GameManagerImpl.getInstance();
        String gameId = gameManager.createNewGame();
        gameManager.deleteGame(gameId);
        assertNull(gameManager.getGame(gameId));
    }

}
