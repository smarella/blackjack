package blackjack.controller;

import blackjack.controller.utils.BlackJackUtils;
import blackjack.event.BetRequestEvent;
import blackjack.model.Game;
import blackjack.model.GameFactory;
import blackjack.model.Hand;
import blackjack.model.Player;
import eventbus.event.EventDispatcher;
import eventbus.eventhandler.EventHandlerRegistry;
import play.cards.Deck;

import javax.inject.Inject;

public class BetRequestEventHandler extends PlayerActionEventHandler<BetRequestEvent> {


  @Inject
  public BetRequestEventHandler(EventDispatcher dispatcher,
                                EventHandlerRegistry registry,
                                GameFactory controller) {
    super(dispatcher, registry, controller);
  }

  @Override
  public Class<BetRequestEvent> getEventClass() {
    return BetRequestEvent.class;
  }

  @Override
  public void performPlayerAction(BetRequestEvent event, Game game, Deck deck) {
    Player player = game.getPlayer();
    player.getActiveHand().setBet(event.getBet());
    BlackJackUtils.payHouseOnBet(player, event.getBet());
  }

  @Override
  public void transitionState(Game game) {
    game.getPlayer().setAvailableActions(Player.Action.DEAL);
    game.getPlayer().getActiveHand().setState(Hand.State.BET_PLACED);
  }
}
