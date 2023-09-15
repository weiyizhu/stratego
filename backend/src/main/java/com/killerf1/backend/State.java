package com.killerf1.backend;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.socket.WebSocketSession;

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

  public PrepState(Game game) {
    super(game);
  }

  @Override
  public void handleClientInput(WebSocketSession session, String input) {
    logger.info("In Prep state, input: {}, session: {}", input, session.toString());
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