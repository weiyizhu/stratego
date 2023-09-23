package com.killerf1.backend;

import lombok.Getter;

@Getter
public enum Side {
    RED(1), BLUE(-1), NEUTRAL(0);

    public final int value;

    private Side(int value) {
        this.value = value;
    }
}
