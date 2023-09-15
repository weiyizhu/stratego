package com.killerf1.backend;

public abstract class State {
  public State(Game game) {
  }
}

class PrepState extends State {
  public PrepState(Game game) {
    super(game);
  }
}
