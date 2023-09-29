package com.killerf1.backend;

import org.springframework.web.socket.WebSocketSession;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;

@Getter
@Setter
public class Game {
    private Board boardObject;
    private State state;
    @Setter(AccessLevel.NONE)
    private WebSocketSession redSession = null;
    @Setter(AccessLevel.NONE)
    private WebSocketSession blueSession = null;
    private String id;

    public Game(String gameId) {
        this.boardObject = new Board();
        this.state = new PrepState(this);
        this.id = gameId;
    }

    @Synchronized
    public void assignSideASession(WebSocketSession session) {
        if (redSession == null) {
            redSession = session;
        } else if (blueSession == null) {
            blueSession = session;
        }
    }
}
