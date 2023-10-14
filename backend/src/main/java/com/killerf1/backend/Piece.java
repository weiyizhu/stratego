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

    /**
     * This static method determines the winning piece by comparing the ranks and
     * other special interactions
     * 
     * @return The winning piece
     */
    public static Piece decideWinningPiece(Piece attackPiece, Piece defendPiece) {
        Unit attackUnit = attackPiece.getUnit();
        Unit defendUnit = defendPiece.getUnit();

        if ((attackUnit == Unit.SPY && defendUnit == Unit.MARSHAL)
                || (attackUnit == Unit.MINER && defendUnit == Unit.BOMB)) {
            return attackPiece;
        }
        if (defendUnit == Unit.EMPTY) {
            return attackPiece;
        }

        int attackUnitRank = attackUnit.getRank();
        int defendUnitRank = defendUnit.getRank();
        if (attackUnitRank > defendUnitRank) {
            return attackPiece;
        } else if (attackUnitRank < defendUnitRank) {
            return defendPiece;
        } else {
            return null;
        }
    }
}
