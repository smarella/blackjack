package blackjack.controller;

import blackjack.TestHelper;
import blackjack.event.DealRequestEvent;
import blackjack.model.Game;
import blackjack.model.Hand;
import org.junit.Test;

import static blackjack.model.Player.Action.*;
import static play.cards.Card.create;
import static play.cards.Suite.*;
import static play.cards.Symbol.*;

public class DealRequestEventHandlerTest {

  @Test
  public void testDeal_WithPlayerBlackJack() {
    TestHelper helper = new TestHelper();
    helper.createTestFor(new DealRequestEvent())
      .setupNewGameWithAmount(100)
      .withNextCardsFromDeckBeing(create(CLUB, ACE), create(HEART, ACE), create(CLUB, TEN));

    // handle event
    helper.performPlayerAction();

    // verify hands
    helper.assertDealerHandIs(create(HEART, ACE));
    helper.assertPlayerActiveHandIs(create(CLUB, ACE), create(CLUB, TEN));

    // transition state
    helper.performStateTransition();

    // verify state transition
    helper.assertGameStateMovedTo(Game.State.PLAYER_WON)
      .assertDealerHandStateMovedTo(Hand.State.DEALT)
      .assertPlayerActiveHandStateMovedTo(Hand.State.BLACKJACK)
      .assertPlayersAvailableActions(NEW_GAME, QUIT);
  }

  @Test
  public void testDeal_WithSplitPossible_TwoCardsWithSameValue() {
    TestHelper helper = new TestHelper();
    helper.createTestFor(new DealRequestEvent())
      .setupNewGameWithAmount(100)
      .withNextCardsFromDeckBeing(create(CLUB, JACK), create(HEART, ACE), create(DIAMOND, TEN));

    // handle event
    helper.performPlayerAction();

    // verify hands
    helper.assertDealerHandIs(create(HEART, ACE));
    helper.assertPlayerActiveHandIs(create(CLUB, JACK), create(DIAMOND, TEN));

    // transition state
    helper.performStateTransition();

    // verify state transition
    helper.assertGameStateMovedTo(Game.State.PLAYERS_TURN)
      .assertDealerHandStateMovedTo(Hand.State.DEALT)
      .assertPlayerActiveHandStateMovedTo(Hand.State.DEALT)
      .assertPlayersAvailableActions(HIT, STAND, SPLIT);
  }

  @Test
  public void testDeal_WithSplitPossible_TwoCardsWithSameSymbol() {
    TestHelper helper = new TestHelper();
    helper.createTestFor(new DealRequestEvent())
      .setupNewGameWithAmount(100)
      .placeBet(50)
      .withNextCardsFromDeckBeing(create(CLUB, FOUR), create(HEART, ACE), create(DIAMOND, FOUR));

    // handle event
    helper.performPlayerAction();

    // verify hands
    helper.assertDealerHandIs(create(HEART, ACE));
    helper.assertPlayerActiveHandIs(create(CLUB, FOUR), create(DIAMOND, FOUR));

    // transition state
    helper.performStateTransition();

    // verify state transition
    helper.assertGameStateMovedTo(Game.State.PLAYERS_TURN)
      .assertDealerHandStateMovedTo(Hand.State.DEALT)
      .assertPlayerActiveHandStateMovedTo(Hand.State.DEALT)
      .assertPlayersAvailableActions(HIT, STAND, SPLIT);
  }

  @Test
  public void testDeal_NoSplitWhenNoCash() {
    TestHelper helper = new TestHelper();
    helper.createTestFor(new DealRequestEvent())
      .setupNewGameWithAmount(100)
      .placeBet(51)
      .withNextCardsFromDeckBeing(create(CLUB, TWO), create(HEART, FOUR), create(DIAMOND, TWO));

    // handle event
    helper.performPlayerAction();

    // verify hands
    helper.assertDealerHandIs(create(HEART, FOUR));
    helper.assertPlayerActiveHandIs(create(CLUB, TWO), create(DIAMOND, TWO));

    // verify amount left
    helper.assertPlayerHasAmount(49);

    // transition state
    helper.performStateTransition();

    // verify state transition
    helper.assertGameStateMovedTo(Game.State.PLAYERS_TURN)
      .assertDealerHandStateMovedTo(Hand.State.DEALT)
      .assertPlayerActiveHandStateMovedTo(Hand.State.DEALT)
      .assertPlayersAvailableActions(HIT, STAND);  // no split
  }

  @Test
  public void testDeal_WithNormalHand() {
    TestHelper helper = new TestHelper();
    helper.createTestFor(new DealRequestEvent())
      .setupNewGameWithAmount(100)
      .withNextCardsFromDeckBeing(create(CLUB, TWO), create(HEART, ACE), create(DIAMOND, ACE));

    // handle event
    helper.performPlayerAction();

    // verify hands
    helper.assertDealerHandIs(create(HEART, ACE));
    helper.assertPlayerActiveHandIs(create(CLUB, TWO), create(DIAMOND, ACE));

    // transition state
    helper.performStateTransition();

    // verify state transition
    helper.assertGameStateMovedTo(Game.State.PLAYERS_TURN)
      .assertDealerHandStateMovedTo(Hand.State.DEALT)
      .assertPlayerActiveHandStateMovedTo(Hand.State.DEALT)
      .assertPlayersAvailableActions(HIT, STAND);
  }

}