package blackjack.event;

import blackjack.model.Game;
import eventbus.event.Event;

public class GameStateUpdateEvent implements Event {

  private final Game game;

  public GameStateUpdateEvent(Game game) {
    this.game = game;
  }

  public Game getGame() {
    return game;
  }
}
