package blackjack.controller;

import blackjack.controller.utils.BlackJackUtils;
import blackjack.event.HitRequestEvent;
import blackjack.model.Game;
import blackjack.model.GameFactory;
import blackjack.model.Hand;
import blackjack.model.Player;
import eventbus.event.EventDispatcher;
import eventbus.eventhandler.EventHandlerRegistry;
import play.cards.Deck;

import javax.inject.Inject;

public class HitRequestEventHandler extends PlayerActionEventHandler<HitRequestEvent> {

  @Inject
  public HitRequestEventHandler(EventDispatcher dispatcher,
                                EventHandlerRegistry registry,
                                GameFactory controller) {
    super(dispatcher, registry, controller);
  }

  @Override
  public Class<HitRequestEvent> getEventClass() {
    return HitRequestEvent.class;
  }

  @Override
  public void performPlayerAction(HitRequestEvent event, Game game, Deck deck) {
    game.getPlayer().getActiveHand().addCard(deck.get());
  }

  @Override
  public void transitionState(Game game) {
    Player player = game.getPlayer();
    if (BlackJackUtils.isBusted(player.getActiveHand())) {
      player.getActiveHand().setState(Hand.State.BUSTED);
      if (player.hasNextHand()) { // if there was a split
        // split should ensure that if one of the hands was BJ, then that one is not active.
        player.setActiveHand(player.getNextHand()); // move to next hand.
        player.setAvailableActions(Player.Action.HIT, Player.Action.STAND); //, Player.Action.DOUBLE);
      } else { // only one hand
        game.setState(Game.State.DEALER_WON);
        BlackJackUtils.setNextAvailableActionsAtEndOfGame(player);
      }
    } else {
      game.getPlayer().getActiveHand().setState(Hand.State.HIT);
      game.getPlayer().setAvailableActions(Player.Action.HIT, Player.Action.STAND);//, Player.Action.DOUBLE);
    }
  }
}
