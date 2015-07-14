package blackjack.controller;

import blackjack.event.NewGameRequestEvent;
import blackjack.model.Game;
import blackjack.model.GameFactory;
import blackjack.model.Hand;
import blackjack.model.Player;
import eventbus.event.EventDispatcher;
import eventbus.eventhandler.EventHandlerRegistry;
import play.cards.Deck;

import javax.inject.Inject;

public class NewGameRequestEventHandler extends PlayerActionEventHandler<NewGameRequestEvent> {

  @Inject
  public NewGameRequestEventHandler(EventDispatcher dispatcher,
                                    EventHandlerRegistry registry,
                                    GameFactory factory) {
    super(dispatcher, registry, factory);
  }

  @Override
  public Class<NewGameRequestEvent> getEventClass() {
    return NewGameRequestEvent.class;
  }

  @Override
  public void performPlayerAction(NewGameRequestEvent event, Game game, Deck deck) {
    factory.createGame(event.getPlayerMoney());
  }

  @Override
  public void transitionState(Game game) {
    game.setState(Game.State.PLAYERS_TURN);
    game.getPlayer().setAvailableActions(Player.Action.BET);
    game.getPlayer().getActiveHand().setState(Hand.State.CREATED);
  }
}
