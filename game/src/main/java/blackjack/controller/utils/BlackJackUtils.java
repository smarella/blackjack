package blackjack.controller.utils;

import blackjack.model.Hand;
import blackjack.model.Player;
import play.cards.Card;
import play.cards.Symbol;

public class BlackJackUtils {

  public static boolean isBlackJack(Hand hand) {
    if (hand.getCards().size() == 2) {
      Card one = hand.getCards().get(0);
      Card two = hand.getCards().get(1);

      if (one.getSymbol() == Symbol.ACE &&
        two.getSymbol().ordinal() >= Symbol.TEN.ordinal()) {
        return true;
      } else if (two.getSymbol() == Symbol.ACE &&
        one.getSymbol().ordinal() >= Symbol.TEN.ordinal()) {
        return true;
      }
    }
    return false;
  }

  public static boolean isSplittable(Hand hand) {
    if (hand.getCards().size() == 2) {
      Card one = hand.getCards().get(0);
      Card two = hand.getCards().get(1);
      return one.getSymbol().equals(two.getSymbol()) ||
        (one.getSymbol().ordinal() >= Symbol.TEN.ordinal() &&
          two.getSymbol().ordinal() >= Symbol.TEN.ordinal());
    }
    return false;
  }

  public static boolean isBusted(Hand hand) {
    return hand.getLowCount() > 21;
  }

  public static void setNextAvailableActionsAtEndOfGame(Player player) {
    if (player.getAmountAvailable() <= 0) {
      player.setAvailableActions(Player.Action.QUIT);
    } else {
      player.setAvailableActions(Player.Action.NEW_GAME, Player.Action.QUIT);
    }
  }

  // wins 3:1 the bet's amount
  public static void payPlayerOnBlackJack(Player player, Hand blackJackHand) {
    player.setAmountAvailable(player.getAmountAvailable() + 3 * blackJackHand.getBet()); // black jack pays 3:1
    blackJackHand.setBet(0);
  }

  public static void payHouseOnBet(Player player, int bet) {
    player.setAmountAvailable((player.getAmountAvailable() - bet));
  }

}
