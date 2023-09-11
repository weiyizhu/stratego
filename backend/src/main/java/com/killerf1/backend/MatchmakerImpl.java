package com.killerf1.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.web.socket.WebSocketSession;

interface Matchmaker {
    public WebSocketSession findMatch(WebSocketSession session);

    public void cancelFindMatch(WebSocketSession session);

    public List<WebSocketSession> getWaitingSessions();
}

/**
 * This singleton class manages the business logic of matchmaking
 */
public final class MatchmakerImpl implements Matchmaker {
    private static volatile MatchmakerImpl instance;
    private volatile List<WebSocketSession> waitingSessions;
    private static Random random = new Random();

    private MatchmakerImpl() {
        this.waitingSessions = new ArrayList<WebSocketSession>();
    }

    public static MatchmakerImpl getInstance() {
        MatchmakerImpl result = instance;
        if (result != null) {
            return result;
        }
        synchronized (MatchmakerImpl.class) {
            if (instance == null) {
                instance = new MatchmakerImpl();
            }
            return instance;
        }
    }

    /**
     * Attempts to find an opponent for the match request
     * 
     * @param session The websocket session where clients and the server
     *                communicates over
     * @return Opponent WebSocketSession if an opponent is found, else null
     */
    @Override
    public synchronized WebSocketSession findMatch(WebSocketSession session) {
        WebSocketSession oppSession = null;
        if (waitingSessions.size() == 0) {
            waitingSessions.add(session);
        } else {
            int index = random.nextInt(waitingSessions.size());
            oppSession = waitingSessions.get(index);
            waitingSessions.remove(oppSession);
        }
        return oppSession;
    }

    @Override
    public synchronized void cancelFindMatch(WebSocketSession session) {
        waitingSessions.remove(session);
    }

    @Override
    public List<WebSocketSession> getWaitingSessions() {
        return this.waitingSessions;
    }

}
