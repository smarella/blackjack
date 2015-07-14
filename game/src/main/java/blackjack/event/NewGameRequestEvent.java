package blackjack.event;

import blackjack.model.Player;

public class NewGameRequestEvent extends PlayerActionEvent {

  private final int playerMoney;

  public NewGameRequestEvent(int playerMoney) {
    super(Player.Action.NEW_GAME);
    this.playerMoney = playerMoney;
  }

  public int getPlayerMoney() {
    return playerMoney;
  }
}
