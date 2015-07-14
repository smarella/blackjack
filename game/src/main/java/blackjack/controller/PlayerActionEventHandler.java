package blackjack.controller;

import blackjack.event.GameStateUpdateEvent;
import blackjack.event.PlayerActionEvent;
import blackjack.model.Game;
import blackjack.model.GameFactory;
import eventbus.event.EventDispatcher;
import eventbus.eventhandler.EventHandler;
import eventbus.eventhandler.EventHandlerRegistry;
import play.cards.Deck;

import javax.inject.Inject;

public abstract class PlayerActionEventHandler<T extends PlayerActionEvent> extends EventHandler<T> {
  protected final GameFactory factory;

  @Inject
  protected PlayerActionEventHandler(EventDispatcher dispatcher,
                                     EventHandlerRegistry registry,
                                     GameFactory factory) {
    super(dispatcher, registry);
    this.factory = factory;
  }

  public abstract Class<T> getEventClass();

  @Override
  public final void handle(T event) {
    performPlayerAction(event, factory.getGame(), factory.getDeck());
    transitionState(factory.getGame());
    getDispatcher().dispatch(new GameStateUpdateEvent(factory.getGame()));
  }

  // perform action
  public abstract void performPlayerAction(T event, Game game, Deck deck);

  // transition to new states
  public abstract void transitionState(Game game);

}
