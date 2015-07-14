package blackjack.event;

import blackjack.model.Player;
import eventbus.event.Event;

public class PlayerActionEvent implements Event {
  private final Player.Action action;

  public PlayerActionEvent(Player.Action action) {
    this.action = action;
  }

  public Player.Action getAction() {
    return action;
  }
}
