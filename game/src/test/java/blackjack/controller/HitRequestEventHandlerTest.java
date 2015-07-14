package blackjack.controller;

import blackjack.TestHelper;
import blackjack.event.HitRequestEvent;
import blackjack.model.Game;
import blackjack.model.Hand;
import blackjack.model.Player;
import org.junit.Test;

import static play.cards.Card.create;
import static play.cards.Suite.*;
import static play.cards.Symbol.*;

public class HitRequestEventHandlerTest {

  @Test
  public void testSingleHand_Bust() {
    testPlayerBust(0, 1000, Player.Action.QUIT);
    testPlayerBust(2000, 1000, Player.Action.QUIT, Player.Action.NEW_GAME);
  }

  @Test
  public void testSingleHand_ActiveHandHitNormal() {
    TestHelper helper = new TestHelper();
    helper.createTestFor(new HitRequestEvent())
      .setupNewGameWithAmount(100)
      .placeBet(10)
      .withDealerHandState(Hand.State.DEALT)
      .withPlayerActiveHandState(Hand.State.DEALT)
      .withActiveHand(create(HEART, TEN), create(CLUB, FOUR))
      .withNextCardsFromDeckBeing(create(DIAMOND, SEVEN), create(HEART, TWO))
      .performPlayerAction();

    // exactly one card should be added to hand
    helper.assertPlayerActiveHandIs(create(HEART, TEN), create(CLUB, FOUR), create(DIAMOND, SEVEN));

    helper.performStateTransition();
    helper.assertGameStateMovedTo(Game.State.PLAYERS_TURN)
      .assertPlayerActiveHandStateMovedTo(Hand.State.HIT)
      .assertDealerHandStateMovedTo(Hand.State.DEALT)
      .assertPlayersAvailableActions(Player.Action.HIT, Player.Action.STAND);//, Player.Action.DOUBLE);
  }

  @Test
  public void testMultiHand_FirstHandBustMoveToSecond() {
    TestHelper helper = new TestHelper();
    helper.createTestFor(new HitRequestEvent())
      .setupNewGameWithAmount(100)
      .placeBet(10)
      .withDealerHandState(Hand.State.DEALT)
      .withPlayerActiveHandState(Hand.State.DEALT)
      .withActiveHand(create(HEART, TEN), create(CLUB, FOUR))
      .withAdditionalPlayerHand(create(DIAMOND, ACE), create(CLUB, NINE))
      .withAdditionalPlayerHandState(Hand.State.DEALT)
      .withNextCardsFromDeckBeing(create(DIAMOND, EIGHT), create(HEART, TWO))
      .performPlayerAction();

    // exactly one card should be added to active hand
    helper.assertPlayerActiveHandIs(create(HEART, TEN), create(CLUB, FOUR), create(DIAMOND, EIGHT));

    helper.performStateTransition();
    helper.assertPlayerActiveHandIs(create(DIAMOND, ACE), create(CLUB, NINE)); // active hand is moved to next hand
    helper.assertGameStateMovedTo(Game.State.PLAYERS_TURN)
      .assertPlayerActiveHandStateMovedTo(Hand.State.DEALT)
      .assertDealerHandStateMovedTo(Hand.State.DEALT)
      .assertPlayersAvailableActions(Player.Action.HIT, Player.Action.STAND);//, Player.Action.DOUBLE);
  }

  private void testPlayerBust(int playerAmount, int bet, Player.Action... availableActions) {
    TestHelper helper = new TestHelper();
    helper.createTestFor(new HitRequestEvent())
      .setupNewGameWithAmount(playerAmount)
      .withDealerHandState(Hand.State.DEALT)
      .withPlayerActiveHandState(Hand.State.DEALT)
      .placeBet(bet)
      .withActiveHand(create(HEART, TEN), create(CLUB, FOUR))
      .withNextCardsFromDeckBeing(create(DIAMOND, EIGHT), create(HEART, TWO))
      .performPlayerAction();

    // exactly one card should be added to hand
    helper.assertPlayerActiveHandIs(create(HEART, TEN), create(CLUB, FOUR), create(DIAMOND, EIGHT));

    helper.performStateTransition();
    helper.assertGameStateMovedTo(Game.State.DEALER_WON)
      .assertPlayerActiveHandStateMovedTo(Hand.State.BUSTED)
      .assertDealerHandStateMovedTo(Hand.State.DEALT)
      .assertPlayersAvailableActions(availableActions);
  }

}