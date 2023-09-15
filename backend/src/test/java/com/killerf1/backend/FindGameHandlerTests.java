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
    WebSocketSession mockSession;
    @Mock
    WebSocketSession mockOppSession;
    @Mock
    Matchmaker mockMatchmaker;
    @Mock
    GameManager mockGameManager;

    @Test
    void noOppFound() {
        try (MockedStatic<SocketHelper> mockedSocketHelper = mockStatic(SocketHelper.class)) {
            FindGameHandler findGameHandler = new FindGameHandler(mockMatchmaker, mockGameManager);
            findGameHandler.afterConnectionEstablished(mockSession);
            mockedSocketHelper.verify(() -> SocketHelper.send(mockSession, "Waiting"));
        }
    }

    @Test
    void OppFound() {
        try (MockedStatic<SocketHelper> mockedSocketHelper = mockStatic(SocketHelper.class)) {
            FindGameHandler findGameHandler = new FindGameHandler(mockMatchmaker, mockGameManager);
            WebSocketSession[] mockSessions = { mockSession, mockOppSession };
            String mockGameId = "12345";
            when(mockMatchmaker.findMatch(mockSession)).thenReturn(mockOppSession);
            when(mockGameManager.createNewGame()).thenReturn(mockGameId);
            findGameHandler.afterConnectionEstablished(mockSession);
            mockedSocketHelper.verify(() -> SocketHelper.broadcast(mockSessions, mockGameId));
        }
    }

}
