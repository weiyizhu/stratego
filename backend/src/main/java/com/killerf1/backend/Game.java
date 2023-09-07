package com.killerf1.backend;

import org.springframework.web.socket.WebSocketSession;

public class Game {
    private WebSocketSession[] sessions;

    public Game(WebSocketSession session1, WebSocketSession session2) {
        this.sessions = new WebSocketSession[2];
        this.sessions[0] = session1;
        this.sessions[1] = session2;
    }

    public String serialize() {
        return "game started";
    }

    public WebSocketSession[] getSessions() {
        return this.sessions;
    }

}
