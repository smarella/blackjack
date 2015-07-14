package blackjack.controller;

import blackjack.TestHelper;
import blackjack.event.StandRequestEvent;
import blackjack.model.Game;
import blackjack.model.Hand;
import blackjack.model.Player;
import org.junit.Test;

import static play.cards.Card.create;
import static play.cards.Suite.*;
import static play.cards.Symbol.*;

public class StandRequestEventHandlerTest {


  @Test
  public void testStand_ActiveHand_dealerBlackJack() {
    TestHelper helper = new TestHelper();
    helper.createTestFor(new StandRequestEvent())
      .setupNewGameWithAmount(100)
      .placeBet(50)
      .withDealerHand(create(CLUB, ACE))
      .withDealerHandState(Hand.State.DEALT)
      .withPlayerActiveHandState(Hand.State.HIT)
      .withActiveHand(create(DIAMOND, TEN), create(HEART, FOUR), create(SPADE, TWO))
      .withNextCardsFromDeckBeing(create(SPADE, JACK), create(DIAMOND, FOUR))
      .performPlayerAction();


    helper.assertPlayerActiveHandStateMovedTo(Hand.State.STAND);
    helper.assertDealerHandIs(create(CLUB, ACE), create(SPADE, JACK));
    helper.assertPlayerActiveHandIs(create(DIAMOND, TEN), create(HEART, FOUR), create(SPADE, TWO));

    helper.performStateTransition();
    helper.assertDealerHandStateMovedTo(Hand.State.BLACKJACK);
    helper.assertPlayerActiveHandStateMovedTo(Hand.State.STAND);
    helper.assertGameStateMovedTo(Game.State.DEALER_WON);
    helper.assertPlayersAvailableActions(Player.Action.NEW_GAME, Player.Action.QUIT);
  }

  @Test
  public void testStand_ActiveHand_dealerBusts() {
    TestHelper helper = new TestHelper();
    helper.createTestFor(new StandRequestEvent())
      .setupNewGameWithAmount(100)
      .placeBet(50)
      .withDealerHand(create(CLUB, TEN))
      .withDealerHandState(Hand.State.DEALT)
      .withPlayerActiveHandState(Hand.State.HIT)
      .withActiveHand(create(DIAMOND, TEN), create(HEART, FOUR), create(SPADE, TWO))
      .withNextCardsFromDeckBeing(create(DIAMOND, FOUR), create(SPADE, JACK), create(SPADE, SEVEN), create(SPADE, SIX))
      .performPlayerAction();


    helper.assertPlayerActiveHandStateMovedTo(Hand.State.STAND);
    helper.assertDealerHandIs(create(CLUB, TEN), create(DIAMOND, FOUR), create(SPADE, JACK));
    helper.assertPlayerActiveHandIs(create(DIAMOND, TEN), create(HEART, FOUR), create(SPADE, TWO));
    helper.assertPlayerHasAmount(50);

    helper.performStateTransition();
    helper.assertDealerHandStateMovedTo(Hand.State.BUSTED);
    helper.assertPlayerActiveHandStateMovedTo(Hand.State.STAND);
    helper.assertGameStateMovedTo(Game.State.PLAYER_WON);
    helper.assertPlayerHasAmount(150); // + (2 * bet)
    helper.assertPlayersAvailableActions(Player.Action.NEW_GAME, Player.Action.QUIT);
  }

  @Test
  public void testStand_ActiveHand_playerWins() {
    TestHelper helper = new TestHelper();
    helper.createTestFor(new StandRequestEvent())
      .setupNewGameWithAmount(100)
      .placeBet(50)
      .withDealerHand(create(CLUB, TEN))
      .withDealerHandState(Hand.State.DEALT)
      .withPlayerActiveHandState(Hand.State.HIT)
      .withActiveHand(create(DIAMOND, TEN), create(HEART, FOUR), create(SPADE, SIX))
      .withNextCardsFromDeckBeing(create(DIAMOND, FOUR), create(SPADE, FOUR), create(SPADE, SEVEN), create(SPADE, SIX))
      .performPlayerAction();


    helper.assertPlayerActiveHandStateMovedTo(Hand.State.STAND);
    helper.assertDealerHandIs(create(CLUB, TEN), create(DIAMOND, FOUR), create(SPADE, FOUR)); // stops at > 17
    helper.assertPlayerActiveHandIs(create(DIAMOND, TEN), create(HEART, FOUR), create(SPADE, SIX));
    helper.assertPlayerHasAmount(50);

    helper.performStateTransition();
    helper.assertDealerHandStateMovedTo(Hand.State.HIT);
    helper.assertPlayerActiveHandStateMovedTo(Hand.State.STAND);
    helper.assertGameStateMovedTo(Game.State.PLAYER_WON);
    helper.assertPlayerHasAmount(150); // + (2 * bet)
    helper.assertPlayersAvailableActions(Player.Action.NEW_GAME, Player.Action.QUIT);
  }

  @Test
  public void testStand_ActiveHand_playerLoses() {
    TestHelper helper = new TestHelper();
    helper.createTestFor(new StandRequestEvent())
      .setupNewGameWithAmount(100)
      .placeBet(50)
      .withDealerHand(create(CLUB, TEN))
      .withDealerHandState(Hand.State.DEALT)
      .withPlayerActiveHandState(Hand.State.HIT)
      .withActiveHand(create(DIAMOND, TEN), create(HEART, FOUR), create(SPADE, FIVE))
      .withNextCardsFromDeckBeing(create(DIAMOND, FOUR), create(SPADE, SIX), create(SPADE, SEVEN), create(SPADE, EIGHT))
      .performPlayerAction();


    helper.assertPlayerActiveHandStateMovedTo(Hand.State.STAND);
    helper.assertDealerHandIs(create(CLUB, TEN), create(DIAMOND, FOUR), create(SPADE, SIX)); // stops at > 17
    helper.assertPlayerActiveHandIs(create(DIAMOND, TEN), create(HEART, FOUR), create(SPADE, FIVE));
    helper.assertPlayerHasAmount(50);

    helper.performStateTransition();
    helper.assertDealerHandStateMovedTo(Hand.State.HIT);
    helper.assertPlayerActiveHandStateMovedTo(Hand.State.STAND);
    helper.assertGameStateMovedTo(Game.State.DEALER_WON);
    helper.assertPlayerHasAmount(50);
    helper.assertPlayersAvailableActions(Player.Action.NEW_GAME, Player.Action.QUIT);
  }

  @Test
  public void testStand_ActiveHand_Tie() {
    TestHelper helper = new TestHelper();
    helper.createTestFor(new StandRequestEvent())
      .setupNewGameWithAmount(100)
      .placeBet(50)
      .withDealerHand(create(CLUB, TEN))
      .withDealerHandState(Hand.State.DEALT)
      .withPlayerActiveHandState(Hand.State.HIT)
      .withActiveHand(create(DIAMOND, TEN), create(HEART, FOUR), create(SPADE, FIVE))
      .withNextCardsFromDeckBeing(create(DIAMOND, THREE), create(SPADE, SIX), create(SPADE, SEVEN), create(SPADE, EIGHT))
      .performPlayerAction();


    helper.assertPlayerActiveHandStateMovedTo(Hand.State.STAND);
    helper.assertDealerHandIs(create(CLUB, TEN), create(DIAMOND, THREE), create(SPADE, SIX)); // stops at > 17
    helper.assertPlayerActiveHandIs(create(DIAMOND, TEN), create(HEART, FOUR), create(SPADE, FIVE));
    helper.assertPlayerHasAmount(50);

    helper.performStateTransition();
    helper.assertDealerHandStateMovedTo(Hand.State.HIT);
    helper.assertPlayerActiveHandStateMovedTo(Hand.State.STAND);
    helper.assertGameStateMovedTo(Game.State.TIE);
    helper.assertPlayerHasAmount(100);
    helper.assertPlayersAvailableActions(Player.Action.NEW_GAME, Player.Action.QUIT);
  }

  @Test
  public void testStand_MultiHand_moveToNextHand() {
    TestHelper helper = new TestHelper();
    helper.createTestFor(new StandRequestEvent())
      .setupNewGameWithAmount(100)
      .placeBet(50)
      .withDealerHand(create(CLUB, TEN))
      .withDealerHandState(Hand.State.DEALT)
      .withPlayerActiveHandState(Hand.State.HIT)
      .withActiveHand(create(DIAMOND, TEN), create(HEART, FOUR), create(SPADE, FIVE))
      .withAdditionalPlayerHand(create(DIAMOND, FOUR), create(CLUB, FOUR))
      .withAdditionalPlayerHandState(Hand.State.DEALT)
      .withNextCardsFromDeckBeing(create(DIAMOND, THREE), create(SPADE, SIX), create(SPADE, SEVEN), create(SPADE, EIGHT))
      .performPlayerAction();


    helper.assertPlayerActiveHandStateMovedTo(Hand.State.DEALT);
    helper.assertDealerHandIs(create(CLUB, TEN));
    helper.assertPlayerActiveHandIs(create(DIAMOND, FOUR), create(CLUB, FOUR));
    helper.assertPlayerHasAmount(50);

    helper.performStateTransition();
    helper.assertDealerHandStateMovedTo(Hand.State.DEALT);
    helper.assertPlayerActiveHandStateMovedTo(Hand.State.DEALT);
    helper.assertGameStateMovedTo(Game.State.PLAYERS_TURN);
    helper.assertPlayerHasAmount(50);
    helper.assertPlayersAvailableActions(Player.Action.HIT, Player.Action.STAND);//, Player.Action.DOUBLE);
  }

  @Test
  public void testStand_MultiHand_DealerBusts() {
    TestHelper helper = new TestHelper();
    helper.createTestFor(new StandRequestEvent())
      .setupNewGameWithAmount(100)
      .placeBet(50)
      .withDealerHand(create(CLUB, TEN))
      .withDealerHandState(Hand.State.DEALT)

      .withPlayerActiveHandState(Hand.State.HIT)
      .withActiveHand(create(DIAMOND, TEN), create(HEART, FOUR), create(SPADE, FIVE))

      .withAdditionalPlayerHand(create(DIAMOND, FOUR), create(CLUB, FOUR))
      .withAdditionalPlayerHandState(Hand.State.STAND)
      .placeBetOnAdditionalHand(50)

      .withNextCardsFromDeckBeing(create(DIAMOND, THREE), create(SPADE, KING), create(SPADE, SEVEN), create(SPADE, EIGHT))
      .performPlayerAction();


    helper.assertPlayerActiveHandStateMovedTo(Hand.State.STAND);
    helper.assertDealerHandIs(create(CLUB, TEN), create(DIAMOND, THREE), create(SPADE, KING));
    helper.assertPlayerActiveHandIs(create(DIAMOND, TEN), create(HEART, FOUR), create(SPADE, FIVE));
    helper.assertPlayerHasAmount(0);

    helper.performStateTransition();
    helper.assertDealerHandStateMovedTo(Hand.State.BUSTED);
    helper.assertPlayerActiveHandStateMovedTo(Hand.State.STAND);
    helper.assertGameStateMovedTo(Game.State.PLAYER_WON);
    helper.assertPlayerHasAmount(200);
    helper.assertPlayersAvailableActions(Player.Action.NEW_GAME, Player.Action.QUIT);//, Player.Action.DOUBLE);
  }

}