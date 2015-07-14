package blackjack.event;

import blackjack.model.Player;

public class DealRequestEvent extends PlayerActionEvent {

  public DealRequestEvent() {
    super(Player.Action.DEAL);
  }
}
