package com.killerf1.backend;

/**
 * This interface provides an abstraction for the three types of move strategies
 */
public interface MoveStrategy {
    /**
     * Determine whether the piece is allowed to move from its currPos to nextPos
     * 
     * @param board   The board to operate on
     * @param currPos The position the piece is on
     * @param nextPos The position the piece wants to move to
     * @return A boolean
     */
    public boolean isValidMove(Board boardObject, Coordinate currPos, Coordinate nextPos);
}

/**
 * This class implements strategies for pieces that can only move one space at a
 * time
 */
class OneSpaceMoveStrategy implements MoveStrategy {
    @Override
    public boolean isValidMove(Board boardObject, Coordinate currPos, Coordinate nextPos) {
        Piece currPiece = boardObject.getPiece(currPos);
        Piece nextPiece = boardObject.getPiece(nextPos);

        if (currPiece.getSide().equals(nextPiece.getSide())) {
            return false;
        }
        if (nextPiece.getUnit() == Unit.RIVER) {
            return false;
        }

        if ((currPos.row == nextPos.row && Math.abs(currPos.col - nextPos.col) == 1)
                || (currPos.col == nextPos.col && Math.abs(currPos.row - nextPos.row) == 1)) {
            return true;
        }
        return false;
    }
}

/**
 * This class implements strategies for pieces that can move any number of
 * open spaces in any directions (i.e Scout)
 */
class InfiniteSpaceMoveStrategy implements MoveStrategy {
    
    private boolean DFS(Board boardObject, Coordinate currPos, Coordinate destPos, int[] direction) {
        if (!Coordinate.isValidCoordinate(currPos)) {
            return false;
        }
        if (currPos.equals(destPos)) {
            return true;
        }
        if (boardObject.getPiece(currPos).getUnit() != Unit.EMPTY) {
            return false;
        }

        Coordinate nextPos = new Coordinate(currPos.row + direction[0], currPos.col + direction[1]);

        return DFS(boardObject, nextPos, destPos, direction);
    }

    @Override
    public boolean isValidMove(Board boardObject, Coordinate currPos, Coordinate destPos) {
        Piece currPiece = boardObject.getPiece(currPos);
        Piece destPiece = boardObject.getPiece(destPos);

        if (currPiece.getSide().equals(destPiece.getSide())) {
            return false;
        }
        if (destPiece.getUnit() == Unit.RIVER) {
            return false;
        }

        int[][] directions = new int[][] {
                new int[] { 0, 1 },
                new int[] { 0, -1 },
                new int[] { 1, 0 },
                new int[] { -1, 0 }
        };

        for (int[] direction : directions) {
            Coordinate nextPos = new Coordinate(currPos.row + direction[0], currPos.col + direction[1]);
            if (DFS(boardObject, nextPos, destPos, direction)) {
                return true;
            }
        }
        return false;
    }
}

/**
 * This class implements strategies for pieces that are immobile
 */
class ZeroSpaceMoveStrategy implements MoveStrategy {
    @Override
    public boolean isValidMove(Board boardObject, Coordinate currPos, Coordinate nextPos) {
        return false;
    }
}