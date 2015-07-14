package blackjack.controller;

import blackjack.TestHelper;
import blackjack.event.BetRequestEvent;
import blackjack.model.Game;
import blackjack.model.Hand;
import org.junit.Test;


public class BetRequestEventHandlerTest {

  @Test
  public void testBet() {
    TestHelper helper = new TestHelper();
    helper.createTestFor(new BetRequestEvent(100))
      .setupNewGameWithAmount(100);
    helper.performPlayerAction();

    helper.assertPlayerActiveHandHasBet(100)
      .assertPlayerHasAmount(0)
      .assertDealerHandIs()
      .assertPlayerActiveHandIs();

    helper.performStateTransition();

    helper.assertDealerHandStateMovedTo(Hand.State.CREATED)
      .assertPlayerActiveHandStateMovedTo(Hand.State.BET_PLACED)
      .assertGameStateMovedTo(Game.State.PLAYERS_TURN);
  }
}