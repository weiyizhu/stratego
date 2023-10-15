package com.killerf1.backend;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;

/**
 * This abstract class provides a template of the fields and methods that the
 * three states the game could be in share
 */
@AllArgsConstructor
public abstract class State {
  Game game;

  public abstract void handleClientInput(WebSocketSession session, String input);
}

class PrepState extends State {
  private static final Logger logger = LogManager.getLogger();
  private final Gson gson = new Gson();

  public PrepState(Game game) {
    super(game);
  }

  @Override
  public void handleClientInput(WebSocketSession session, String input) {
    logger.info("In Prep state, input: {}, session: {}", input, session.toString());
    Side side = session == this.game.getRedSession() ? Side.RED : Side.BLUE;
    int[][] boardSide = gson.fromJson(input, int[][].class);
    Board boardObject = this.game.getBoardObject();
    try {
      boardObject.populateBoardSide(boardSide, side);
      if (boardObject.getBoardSidePopulated() == 2) {
        this.game.setState(new GameState(game));
        WebSocketSession redSession = this.game.getRedSession();
        WebSocketSession blueSession = this.game.getBlueSession();
        String redBoard = boardObject.serializedRedBoard(null);
        String blueBoard = boardObject.serializedBlueBoard(null);
        SocketHelper.send(redSession,
            new ServerMessageTemplate(ClientGameState.PREP, MsgType.SWITCH, redBoard, Side.RED));
        SocketHelper.send(blueSession,
            new ServerMessageTemplate(ClientGameState.PREP, MsgType.SWITCH, blueBoard, Side.RED));
      }
    } catch (InvalidBoardException e) {
      SocketHelper.send(session,
          new ServerMessageTemplate(ClientGameState.PREP, MsgType.ERROR, "Invalid Board", Side.RED));
    }
  }
}

class GameState extends State {
  private static final Logger logger = LogManager.getLogger();
  private final Gson gson = new Gson();

  public GameState(Game game) {
    super(game);
  }

  private int[][] getPositionsFromInput(String input, Side side) {
    int[][] positions = gson.fromJson(input, int[][].class);
    if (side == Side.BLUE) {
      positions = new int[][] { new int[] { 9 - positions[0][0], 9 - positions[0][1] },
          new int[] { 9 - positions[1][0], 9 - positions[1][1] } };
    }
    return positions;
  }

  @Override
  public void handleClientInput(WebSocketSession session, String input) {
    logger.info("In Game state, input: {}, session: {}", input, session.toString());
    WebSocketSession redSession = this.game.getRedSession();
    WebSocketSession blueSession = this.game.getBlueSession();
    WebSocketSession[] gameSessions = new WebSocketSession[] { redSession, blueSession };
    Side side = session == redSession ? Side.RED : Side.BLUE;
    Side oppSide = session == redSession ? Side.BLUE : Side.RED;
    Side movingSide = this.game.getMovingSide();

    if (input.equals("Surrender")) {
      this.game.setState(new EndState(game));
      SocketHelper.broadcast(gameSessions,
          new ServerMessageTemplate(ClientGameState.GAME, MsgType.SWITCH, String.valueOf(oppSide.getValue()),
              movingSide));
      SocketHelper.closeGame(gameSessions);
    } else {
      if (side != movingSide) {
        SocketHelper.send(session,
            new ServerMessageTemplate(ClientGameState.GAME, MsgType.ERROR, "Not your turn", movingSide));
        return;
      }
      int[][] positions = getPositionsFromInput(input, side);

      Coordinate currPos = new Coordinate(positions[0][0], positions[0][1]);
      Coordinate nextPos = new Coordinate(positions[1][0], positions[1][1]);
      Board boardObject = this.game.getBoardObject();
      Piece currPiece = boardObject.getPiece(currPos);
      MoveStrategy moveStrategy = currPiece.getUnit().getMoveStrategy();

      if (moveStrategy.isValidMove(boardObject, currPos, nextPos)) {
        MoveResponse moveResponse = boardObject.move(currPos, nextPos);
        Coordinate visibleCoordinate = moveResponse.getVisibleCoordinate();
        Side winner = moveResponse.getWinner();
        this.game.setMovingSide(oppSide);
        movingSide = this.game.getMovingSide();

        String redBoard = boardObject.serializedRedBoard(visibleCoordinate);
        String blueBoard = boardObject.serializedBlueBoard(visibleCoordinate);
        SocketHelper.send(redSession,
            new ServerMessageTemplate(ClientGameState.GAME, MsgType.INFO, redBoard, movingSide));
        SocketHelper.send(blueSession,
            new ServerMessageTemplate(ClientGameState.GAME, MsgType.INFO, blueBoard, movingSide));

        if (winner != null) {
          this.game.setState(new EndState(game));
          SocketHelper.broadcast(gameSessions,
              new ServerMessageTemplate(ClientGameState.GAME, MsgType.SWITCH, String.valueOf(winner.getValue()),
                  movingSide));
          SocketHelper.closeGame(gameSessions);
        }
      } else {
        SocketHelper.send(session,
            new ServerMessageTemplate(ClientGameState.GAME, MsgType.ERROR, "Invalid move", movingSide));
      }
    }
  }
}

class EndState extends State {
  private static final Logger logger = LogManager.getLogger();

  public EndState(Game game) {
    super(game);
  }

  @Override
  public void handleClientInput(WebSocketSession session, String input) {
    logger.info("In End state, input: {}, session: {}", input, session.toString());
    SocketHelper.closeSession(session);
  }
}