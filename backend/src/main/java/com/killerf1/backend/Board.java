package com.killerf1.backend;

public class Board {
  private Piece[][] board;

  public Board() {
    this.board = new Piece[10][10];
  }

  public Piece getPiece(Coordinate pos) {
    return this.board[pos.row][pos.col];
  }

  /**
   * Populates one side of the board with pieces that a player submits during the
   * PREP state
   * 
   * @param boardSide A stringified side of the board with information about the
   *                  arrangement of pieces
   * @param side      The side the player that submits the arrangement is on
   */
  public void populateBoardSide(String boardSide, Side side) {
  }

  @Override
  public String toString() {
    return "";
  }
}
