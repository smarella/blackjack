package blackjack.controller;

import blackjack.event.NewGameRequestEvent;
import blackjack.model.Game;
import blackjack.model.GameFactory;
import blackjack.model.Hand;
import blackjack.model.Player;
import eventbus.event.EventDispatcher;
import eventbus.eventhandler.EventHandlerRegistry;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class NewGameRequestEventHandlerTest {

  @Test
  public void testNewGame() {
    GameFactory factory = Mockito.mock(GameFactory.class);
    EventDispatcher dispatcher = Mockito.mock(EventDispatcher.class);
    EventHandlerRegistry registry = Mockito.mock(EventHandlerRegistry.class);

    GameFactory myFactory = new GameFactory();
    myFactory.createGame(500);
    Game game = myFactory.getGame();
    Mockito.when(factory.getGame()).thenReturn(game);

    NewGameRequestEventHandler handler = new NewGameRequestEventHandler(dispatcher, registry, factory);

    handler.handle(new NewGameRequestEvent(500));

    Mockito.verify(factory).createGame(500);
    Assert.assertEquals(game.getState(), Game.State.PLAYERS_TURN);
    Assert.assertEquals(game.getDealer().getHand().getState(), Hand.State.CREATED);
    Assert.assertEquals(game.getPlayer().getActiveHand().getState(), Hand.State.CREATED);
    Assert.assertTrue(game.getPlayer().getAvailableActions().contains(Player.Action.BET));
    Assert.assertEquals(game.getPlayer().getAvailableActions().size(), 1);
  }

}