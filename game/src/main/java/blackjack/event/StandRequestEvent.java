package blackjack.event;

import blackjack.model.Player;

public class StandRequestEvent extends PlayerActionEvent {
  public StandRequestEvent() {
    super(Player.Action.STAND);
  }
}
