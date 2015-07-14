package blackjack.event;

import blackjack.model.Player;

public class HitRequestEvent extends PlayerActionEvent {
  public HitRequestEvent() {
    super(Player.Action.HIT);
  }
}
