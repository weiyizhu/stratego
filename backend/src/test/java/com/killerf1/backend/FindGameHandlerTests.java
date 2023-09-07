package com.killerf1.backend;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.socket.WebSocketSession;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class FindGameHandlerTests {

    @Mock
    WebSocketSession session;
    @Mock
    WebSocketSession oppSession;
    @Mock
    MatchmakerUtils mockMatchmaker;
    @Mock
    GameSessionManagerUtils mockGameSessionManager;
    @Mock
    Game mockGame;

    @Test
    void noOppFound() {
        try (MockedStatic<SocketHelper> mockedSocketHelper = mockStatic(SocketHelper.class)) {
            FindGameHandler findGameHandler = new FindGameHandler(mockMatchmaker, mockGameSessionManager);
            findGameHandler.afterConnectionEstablished(session);
            mockedSocketHelper.verify(() -> SocketHelper.send(session, "Waiting"));
        }
    }

    @Test
    void OppFound() {
        try (MockedStatic<SocketHelper> mockedSocketHelper = mockStatic(SocketHelper.class)) {
            FindGameHandler findGameHandler = new FindGameHandler(mockMatchmaker, mockGameSessionManager);
            WebSocketSession[] mockSessions = { session, oppSession };
            when(mockMatchmaker.findMatch(session)).thenReturn(oppSession);
            when(mockGame.getSessions()).thenReturn(mockSessions);
            when(mockGameSessionManager.createNewGame(session, oppSession)).thenReturn(mockGame);
            findGameHandler.afterConnectionEstablished(session);
            mockedSocketHelper.verify(() -> SocketHelper.broadcast(mockSessions, "Game found"));
        }
    }

}
