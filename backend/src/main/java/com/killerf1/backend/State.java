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
        logger.info("red board: {}, blue board: {}", redBoard, blueBoard);
        SocketHelper.send(redSession, new ServerMessageTemplate(ClientGameState.PREP, MsgType.SWITCH, redBoard));
        SocketHelper.send(blueSession, new ServerMessageTemplate(ClientGameState.PREP, MsgType.SWITCH, blueBoard));
      }
    } catch (InvalidBoardException e) {
      SocketHelper.send(session, new ServerMessageTemplate(ClientGameState.PREP, MsgType.ERROR, "Invalid Board"));
    }
  }
}

class GameState extends State {
  private static final Logger logger = LogManager.getLogger();

  public GameState(Game game) {
    super(game);
  }

  @Override
  public void handleClientInput(WebSocketSession session, String input) {
    logger.info("In Game state, input: {}, session: {}", input, session.toString());
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
  }
}