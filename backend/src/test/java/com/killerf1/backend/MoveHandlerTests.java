package com.killerf1.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class MoveHandlerTests {

    @Mock
    WebSocketSession mockSession;
    @Mock
    Game mockGame;
    @Mock
    State mockState;
    @Mock
    TextMessage mockMessage;
    @Mock
    GameManager mockGameManager;
    @Mock
    URI mockUri;

    @Test
    void noUriInSession() {
        try (MockedStatic<SocketHelper> mockedSocketHelper = mockStatic(SocketHelper.class)) {
            MoveHandler moveHandler = new MoveHandler(mockGameManager);
            when(mockSession.getUri()).thenReturn(null);
            assertNull(moveHandler.getGameFromSession(mockSession));
            mockedSocketHelper.verify(() -> SocketHelper.send(mockSession, "Game not found"));
        }
    }

    @Test
    void noQueryInUri() {
        try (MockedStatic<SocketHelper> mockedSocketHelper = mockStatic(SocketHelper.class)) {
            MoveHandler moveHandler = new MoveHandler(mockGameManager);
            when(mockSession.getUri()).thenReturn(mockUri);
            when(mockUri.getQuery()).thenReturn(null);
            assertNull(moveHandler.getGameFromSession(mockSession));
            mockedSocketHelper.verify(() -> SocketHelper.send(mockSession, "Game not found"));
        }
    }

    @Test
    void gameNotFound() {
        try (MockedStatic<SocketHelper> mockedSocketHelper = mockStatic(SocketHelper.class)) {
            MoveHandler moveHandler = new MoveHandler(mockGameManager);
            String mockGameId = "12345";
            when(mockSession.getUri()).thenReturn(mockUri);
            when(mockUri.getQuery()).thenReturn(mockGameId);
            when(mockGameManager.getGame(mockGameId)).thenReturn(null);
            assertNull(moveHandler.getGameFromSession(mockSession));
            mockedSocketHelper.verify(() -> SocketHelper.send(mockSession, "Game not found"));
        }
    }

    @Test
    void gameFound() {
        MoveHandler moveHandler = new MoveHandler(mockGameManager);
        String mockGameId = "12345";
        when(mockSession.getUri()).thenReturn(mockUri);
        when(mockUri.getQuery()).thenReturn(mockGameId);
        when(mockGameManager.getGame(mockGameId)).thenReturn(mockGame);
        assertEquals(mockGame, moveHandler.getGameFromSession(mockSession));
    }

    @Test
    void afterConnectionEstablishedWithValidGameId() {
        MoveHandler moveHandler = new MoveHandler(mockGameManager);
        MoveHandler spyMoveHandler = spy(moveHandler);
        when(spyMoveHandler.getGameFromSession(mockSession)).thenReturn(mockGame);
        spyMoveHandler.afterConnectionEstablished(mockSession);
        verify(mockGame).assignSideASession(mockSession);
    }

    @Test
    void afterConnectionEstablishedWithInvalidGameId() {
        MoveHandler moveHandler = new MoveHandler(mockGameManager);
        MoveHandler spyMoveHandler = spy(moveHandler);
        when(spyMoveHandler.getGameFromSession(mockSession)).thenReturn(null);
        spyMoveHandler.afterConnectionEstablished(mockSession);
        verify(mockGame, never()).assignSideASession(mockSession);
    }

    @Test
    void handleTextMessageWithValidGameId() {
        MoveHandler moveHandler = new MoveHandler(mockGameManager);
        MoveHandler spyMoveHandler = spy(moveHandler);
        when(spyMoveHandler.getGameFromSession(mockSession)).thenReturn(mockGame);
        when(mockGame.getState()).thenReturn(mockState);
        spyMoveHandler.handleTextMessage(mockSession, mockMessage);
        verify(mockGame).getState();
        verify(mockState).handleClientInput(mockSession, mockMessage.toString());
    }

    @Test
    void handleTextMessageWithInvalidGameId() {
        MoveHandler moveHandler = new MoveHandler(mockGameManager);
        MoveHandler spyMoveHandler = spy(moveHandler);
        when(spyMoveHandler.getGameFromSession(mockSession)).thenReturn(null);
        spyMoveHandler.handleTextMessage(mockSession, mockMessage);
        verify(mockGame, never()).getState();
    }

}
