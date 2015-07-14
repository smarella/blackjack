package blackjack.event;

import blackjack.model.Player;

public class SplitRequestEvent extends PlayerActionEvent {
  public SplitRequestEvent() {
    super(Player.Action.SPLIT);
  }
}
