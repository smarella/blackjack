package blackjack.controller;

import blackjack.TestHelper;
import blackjack.event.SplitRequestEvent;
import blackjack.model.Game;
import blackjack.model.Hand;
import blackjack.model.Player;
import org.junit.Test;

import static play.cards.Card.create;
import static play.cards.Suite.*;
import static play.cards.Symbol.*;

public class SplitRequestEventHandlerTest {

  @Test
  public void split_FirstHand_BlackJack() {
    TestHelper helper = new TestHelper();
    helper
      .createTestFor(new SplitRequestEvent())
      .setupNewGameWithAmount(100)
      .placeBet(50)
      .withPlayerActiveHandState(Hand.State.DEALT)
      .withActiveHand(create(SPADE, TEN), create(DIAMOND, JACK))
      .withNextCardsFromDeckBeing(create(HEART, ACE), create(CLUB, TEN), create(SPADE, TWO))
      .performPlayerAction();
    helper
      .assertPlayerHasAmount(150) // paid back due to black jack
      .assertPlayerActiveHandHasBet(50)
      .assertAdditionalHandHasBet(0)
      .assertPlayerActiveHandIs(create(DIAMOND, JACK), create(CLUB, TEN))
      .assertAdditionalHandIs(create(SPADE, TEN), create(HEART, ACE))
      .assertPlayerActiveHandStateMovedTo(Hand.State.DEALT)
      .assertAdditionalHandStateMovedTo(Hand.State.BLACKJACK);
    helper.performStateTransition();
    helper.assertGameStateMovedTo(Game.State.PLAYERS_TURN)
      .assertPlayersAvailableActions(Player.Action.HIT, Player.Action.STAND);
  }

  @Test
  public void split_SecondHand_BlackJack() {
    TestHelper helper = new TestHelper();
    helper
      .createTestFor(new SplitRequestEvent())
      .setupNewGameWithAmount(100)
      .placeBet(50)
      .withPlayerActiveHandState(Hand.State.DEALT)
      .withActiveHand(create(SPADE, TEN), create(DIAMOND, JACK))
      .withNextCardsFromDeckBeing(create(CLUB, TEN), create(HEART, ACE), create(SPADE, TWO))
      .performPlayerAction();
    helper
      .assertPlayerHasAmount(150) // paid back due to black jack
      .assertPlayerActiveHandHasBet(50)
      .assertAdditionalHandHasBet(0)
      .assertPlayerActiveHandIs(create(SPADE, TEN), create(CLUB, TEN))
      .assertAdditionalHandIs(create(DIAMOND, JACK), create(HEART, ACE))
      .assertPlayerActiveHandStateMovedTo(Hand.State.DEALT)
      .assertAdditionalHandStateMovedTo(Hand.State.BLACKJACK);
    helper.performStateTransition();
    helper.assertGameStateMovedTo(Game.State.PLAYERS_TURN)
      .assertPlayersAvailableActions(Player.Action.HIT, Player.Action.STAND);
  }

  @Test
  public void split_BothHands_BlackJack() {
    TestHelper helper = new TestHelper();
    helper
      .createTestFor(new SplitRequestEvent())
      .setupNewGameWithAmount(100)
      .placeBet(50)
      .withPlayerActiveHandState(Hand.State.DEALT)
      .withActiveHand(create(SPADE, TEN), create(DIAMOND, JACK))
      .withNextCardsFromDeckBeing(create(CLUB, ACE), create(HEART, ACE), create(SPADE, TWO))
      .performPlayerAction();
    helper
      .assertPlayerHasAmount(300) // paid back due to black jacks
      .assertPlayerActiveHandHasBet(0)
      .assertAdditionalHandHasBet(0)
      .assertAdditionalHandIs(create(SPADE, TEN), create(CLUB, ACE))
      .assertPlayerActiveHandIs(create(DIAMOND, JACK), create(HEART, ACE))
      .assertPlayerActiveHandStateMovedTo(Hand.State.BLACKJACK)
      .assertAdditionalHandStateMovedTo(Hand.State.BLACKJACK);
    helper.performStateTransition();
    helper.assertGameStateMovedTo(Game.State.PLAYER_WON)
      .assertPlayersAvailableActions(Player.Action.NEW_GAME, Player.Action.QUIT);
  }

  @Test
  public void split_NoHand_BlackJack() {
    TestHelper helper = new TestHelper();
    helper
      .createTestFor(new SplitRequestEvent())
      .setupNewGameWithAmount(100)
      .placeBet(50)
      .withPlayerActiveHandState(Hand.State.DEALT)
      .withActiveHand(create(SPADE, TEN), create(DIAMOND, JACK))
      .withNextCardsFromDeckBeing(create(CLUB, TWO), create(HEART, FOUR), create(SPADE, TWO))
      .performPlayerAction();
    helper
      .assertPlayerHasAmount(0)
      .assertPlayerActiveHandHasBet(50)
      .assertAdditionalHandHasBet(50)
      .assertAdditionalHandIs(create(DIAMOND, JACK), create(HEART, FOUR))
      .assertPlayerActiveHandIs(create(SPADE, TEN), create(CLUB, TWO))
      .assertPlayerActiveHandStateMovedTo(Hand.State.DEALT)
      .assertAdditionalHandStateMovedTo(Hand.State.DEALT);
    helper.performStateTransition();
    helper.assertGameStateMovedTo(Game.State.PLAYERS_TURN)
      .assertPlayersAvailableActions(Player.Action.HIT, Player.Action.STAND);
  }

}