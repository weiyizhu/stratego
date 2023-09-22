package com.killerf1.backend;

import org.springframework.web.socket.WebSocketSession;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Game {
    private Board board;
    private State state;
    @Setter(AccessLevel.NONE)
    private WebSocketSession redSession = null;
    @Setter(AccessLevel.NONE)
    private WebSocketSession blueSession = null;
    private String id;

    public Game(String gameId) {
        this.board = new Board();
        this.state = new PrepState(this);
        this.id = gameId;
    }

    public void assignSideASession(WebSocketSession session) {
        if (redSession == null) {
            redSession = session;
        } else if (blueSession == null) {
            blueSession = session;
        }
    }
}
