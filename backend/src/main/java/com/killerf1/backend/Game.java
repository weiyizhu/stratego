package com.killerf1.backend;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Game {
    private Board board;
    private State state;
    @Setter(AccessLevel.NONE)
    private String redSessionId = null;
    @Setter(AccessLevel.NONE)
    private String blueSessionId = null;
    private String id;

    public Game(String gameId) {
        this.board = new Board();
        this.state = new PrepState(this);
        this.id = gameId;
    }

    public void assignSideASession(String sessionId) {
        if (redSessionId == null) {
            redSessionId = sessionId;
        } else if (blueSessionId == null) {
            blueSessionId = sessionId;
        }
    }
}
