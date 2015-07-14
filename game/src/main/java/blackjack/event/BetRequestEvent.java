package blackjack.event;

import blackjack.model.Player;

public class BetRequestEvent extends PlayerActionEvent {
  private final int bet;

  public BetRequestEvent(int bet) {
    super(Player.Action.BET);
    this.bet = bet;
  }

  public int getBet() {
    return bet;
  }
}
