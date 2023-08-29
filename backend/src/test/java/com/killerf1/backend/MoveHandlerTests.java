package com.killerf1.backend;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doThrow;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class MoveHandlerTests {

    @Mock
    WebSocketSession session;
    @Mock
    TextMessage msg;

    @Test
    void successfulMessageSent() {
        MoveHandler moveHandler = new MoveHandler();

        assertDoesNotThrow(() -> moveHandler.handleTextMessage(session, msg));
    }

    @Test
    void failedMessageSent() throws IOException {
        MoveHandler moveHandler = new MoveHandler();
        WebSocketSession session = mock(WebSocketSession.class);
        TextMessage msg = mock(TextMessage.class);
        doThrow(new IOException()).when(session).sendMessage(msg);

        assertDoesNotThrow(() -> moveHandler.handleTextMessage(session, msg));
    }

}
