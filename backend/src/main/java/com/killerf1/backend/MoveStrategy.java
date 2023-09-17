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
    public boolean isValidMove(Board board, Coordinate currPos, Coordinate nextPos);

    /**
     * Makes the move from currPos to nextPos and updates the board accordingly
     * 
     * @param board   The board to operate on
     * @param currPos The position the piece is on
     * @param nextPos The position the piece wants to move to
     * @return The updated board after the move is made
     */
    public Board move(Board board, Coordinate currPos, Coordinate nextPos);
}

/**
 * This class implements strategies for pieces that can only move one space at a
 * time
 */
class OneSpaceMoveStrategy implements MoveStrategy {
    @Override
    public boolean isValidMove(Board board, Coordinate currPos, Coordinate nextPos) {
        return true;
    }

    @Override
    public Board move(Board board, Coordinate currPos, Coordinate nextPos) {
        return new Board();
    }
}

/**
 * This class implements strategies for pieces that can move any number of
 * open spaces in any directions (i.e Scout)
 */
class InfiniteSpaceMoveStrategy implements MoveStrategy {
    @Override
    public boolean isValidMove(Board board, Coordinate currPos, Coordinate nextPos) {
        return true;
    }

    @Override
    public Board move(Board board, Coordinate currPos, Coordinate nextPos) {
        return new Board();
    }
}

/**
 * This class implements strategies for pieces that are immobile
 */
class ZeroSpaceMoveStrategy implements MoveStrategy {
    @Override
    public boolean isValidMove(Board board, Coordinate currPos, Coordinate nextPos) {
        return true;
    }

    @Override
    public Board move(Board board, Coordinate currPos, Coordinate nextPos) {
        return new Board();
    }
}