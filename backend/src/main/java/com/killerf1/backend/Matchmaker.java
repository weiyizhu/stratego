package com.killerf1.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.web.socket.WebSocketSession;

/**
 * This singleton class manages the business logic of matchmaking
 */
public final class Matchmaker {
    private static volatile Matchmaker instance;
    private List<WebSocketSession> waitingSessions;

    private Matchmaker() {
        this.waitingSessions = new ArrayList<WebSocketSession>();
    }

    public static Matchmaker getInstance() {
        Matchmaker result = instance;
        if (result != null) {
            return result;
        }
        synchronized (Matchmaker.class) {
            if (instance == null) {
                instance = new Matchmaker();
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
    public synchronized WebSocketSession findMatch(WebSocketSession session) {
        WebSocketSession oppSession = null;
        if (waitingSessions.size() == 0) {
            waitingSessions.add(session);
        } else {
            Random random = new Random();
            int index = random.nextInt(waitingSessions.size());
            oppSession = waitingSessions.get(index);
            waitingSessions.remove(oppSession);
        }
        return oppSession;
    }

    public synchronized void cancelFindMatch(WebSocketSession session) {
        waitingSessions.remove(session);
    }

    public List<WebSocketSession> getWaitingSessions() {
        return this.waitingSessions;
    }

}
