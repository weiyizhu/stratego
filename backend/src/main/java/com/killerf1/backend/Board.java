package com.killerf1.backend;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.Getter;

class InvalidBoardException extends Exception {

}

@Getter
public class Board {
  private static final Logger logger = LogManager.getLogger();
  private Piece[][] board;
  private volatile int boardSidePopulated = 0;

  public Board() {
    this.board = new Piece[10][10];
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        if ((i == 4 && j == 2) ||
            (i == 4 && j == 3) ||
            (i == 5 && j == 2) ||
            (i == 5 && j == 3) ||
            (i == 4 && j == 6) ||
            (i == 4 && j == 7) ||
            (i == 5 && j == 6) ||
            (i == 5 && j == 7)) {
          this.board[i][j] = new Piece(Unit.RIVER, Side.NEUTRAL);
        } else {
          this.board[i][j] = new Piece(Unit.EMPTY, Side.NEUTRAL);
        }
      }
    }
  }

  public Piece getPiece(Coordinate pos) {
    return this.board[pos.row][pos.col];
  }

  private boolean isValidBoardSide(int[][] boardSide) {
    int m = boardSide.length;
    int n = boardSide[0].length;
    Map<Integer, Integer> boardSideRankToQuantity = new HashMap<>();

    for (int i = 0; i < m; i++) {
      for (int j = 0; j < n; j++) {
        int rank = boardSide[i][j];
        int quantity = boardSideRankToQuantity.getOrDefault(rank, 0) + 1;
        boardSideRankToQuantity.put(rank, quantity);
      }
    }
    logger.info("boardSide: {}, expected: {}, equal: {}", boardSideRankToQuantity, Unit.rankToQuantity,
        boardSideRankToQuantity.equals(Unit.rankToQuantity));
    return boardSideRankToQuantity.equals(Unit.rankToQuantity);
  }

  /**
   * Populates one side of the board with pieces that a player submits during the
   * PREP state
   * 
   * @param boardSide A stringified side of the board with information about the
   *                  arrangement of pieces
   * @param side      The side the player that submits the arrangement is on
   */
  public void populateBoardSide(int[][] boardSide, Side side) throws InvalidBoardException {
    if (isValidBoardSide(boardSide)) {
      if (side == Side.RED) {
        for (int i = 0; i < 4; i++) {
          for (int j = 0; j < 10; j++) {
            Unit unit = Unit.rankToUnit.get(boardSide[i][j]);
            board[i + 6][j] = new Piece(unit, side);
          }
        }
      } else {
        for (int i = 0; i < 4; i++) {
          for (int j = 0; j < 10; j++) {
            Unit unit = Unit.rankToUnit.get(boardSide[i][j]);
            board[3 - i][9 - j] = new Piece(unit, side);
          }
        }
      }
      boardSidePopulated += 1;
    } else {
      throw new InvalidBoardException();
    }

  }

  public String serializedRedBoard(Coordinate visibleCoordinate) {
    Piece[][] board = this.getBoard();
    int[][][] serializedBoard = new int[10][10][2];
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {

        Piece piece = board[i][j];
        int side = piece.getSide().getValue();
        int rank = piece.getUnit().getRank();

        if (visibleCoordinate != null && visibleCoordinate.row == i && visibleCoordinate.col == j) {
          serializedBoard[i][j] = new int[] { side, rank };
          continue;
        }

        if (side == Side.BLUE.getValue()) {
          serializedBoard[i][j] = new int[] { side, Unit.UNKNOWN_ENEMY.getRank() };
        } else {
          serializedBoard[i][j] = new int[] { side, rank };
        }
      }
    }
    return Arrays.deepToString(serializedBoard);
  }

  public String serializedBlueBoard(Coordinate visibleCoordinate) {
    Piece[][] board = this.getBoard();
    int[][][] serializedBoard = new int[10][10][2];
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {

        Piece piece = board[i][j];
        int side = piece.getSide().getValue();
        int rank = piece.getUnit().getRank();

        if (visibleCoordinate != null && visibleCoordinate.row == i && visibleCoordinate.col == j) {
          serializedBoard[9-i][9-j] = new int[] { side, rank };
          continue;
        }

        if (side == Side.RED.getValue()) {
          serializedBoard[9-i][9-j] = new int[] { side, Unit.UNKNOWN_ENEMY.getRank() };
        } else {
          serializedBoard[9-i][9-j] = new int[] { side, rank };
        }
      }
    }
    return Arrays.deepToString(serializedBoard);
  }
}
