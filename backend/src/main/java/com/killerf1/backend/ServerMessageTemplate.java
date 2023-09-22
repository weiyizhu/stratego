package com.killerf1.backend;

import lombok.Getter;

@Getter
enum ClientGameState {
    ONCONNECTION("OnConnection"), PREP("Prep"), GAME("Game");

    public final String name;

    private ClientGameState(String name) {
        this.name = name;
    }
}

@Getter
enum MsgType {
    INFO("Info"), SWITCH("Switch"), ERROR("Error");

    public final String name;

    private MsgType(String name) {
        this.name = name;
    }
}

public class ServerMessageTemplate {
    public final String state;
    public final String type;
    public final String msg;

    public ServerMessageTemplate(ClientGameState gameState, MsgType msgType, String msg) {
        this.state = gameState.getName();
        this.type = msgType.getName();
        this.msg = msg;
    }
}
