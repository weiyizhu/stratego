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
public class MatchmakerImplTests {
    
    @Mock
    WebSocketSession session;
    @Mock
    WebSocketSession oppSession;

    @Test
    @Order(4)
    void createSingleInstance () {
        Matchmaker matchmaker = MatchmakerImpl.getInstance();
        Matchmaker matchmaker2 = MatchmakerImpl.getInstance();
        matchmaker.findMatch(session);
        assertEquals(matchmaker.getWaitingSessions(), matchmaker2.getWaitingSessions());
    }

    @Test 
    @Order(3)
    void noOppFound() {
        Matchmaker matchmaker = MatchmakerImpl.getInstance();
        assertNull(matchmaker.findMatch(session));
        assertEquals(1, matchmaker.getWaitingSessions().size());
    }

    @Test
    @Order(1)
    void OppFound() {
        Matchmaker matchmaker = MatchmakerImpl.getInstance();
        matchmaker.findMatch(session);
        assertEquals(session, matchmaker.findMatch(oppSession));
        assertEquals(0, matchmaker.getWaitingSessions().size());
    }

    @Test
    @Order(2)
    void cancelRemovesFromWaitlist() {
        Matchmaker matchmaker = MatchmakerImpl.getInstance();
        matchmaker.findMatch(session);
        matchmaker.cancelFindMatch(session);
        assertEquals(0, matchmaker.getWaitingSessions().size());
    }
}
