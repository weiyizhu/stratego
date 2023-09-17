package com.killerf1.backend;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This class provides information about a concrete piece on the board,
 * including its unit and the side it belongs to
 */
@AllArgsConstructor
@Getter
public class Piece {
    private Unit unit;
    private Side side;
}
