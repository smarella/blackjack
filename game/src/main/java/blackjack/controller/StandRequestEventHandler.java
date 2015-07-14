package blackjack.controller;

import blackjack.controller.utils.BlackJackUtils;
import blackjack.event.StandRequestEvent;
import blackjack.model.Game;
import blackjack.model.GameFactory;
import blackjack.model.Hand;
import blackjack.model.Player;
import eventbus.event.EventDispatcher;
import eventbus.eventhandler.EventHandlerRegistry;
import play.cards.Deck;

import javax.inject.Inject;

public class StandRequestEventHandler extends PlayerActionEventHandler<StandRequestEvent> {

  @Inject
  public StandRequestEventHandler(EventDispatcher dispatcher,
                                  EventHandlerRegistry registry,
                                  GameFactory controller) {
    super(dispatcher, registry, controller);
  }

  @Override
  public Class<StandRequestEvent> getEventClass() {
    return StandRequestEvent.class;
  }

  @Override
  public void performPlayerAction(StandRequestEvent event, Game game, Deck deck) {
    Player player = game.getPlayer();
    player.getActiveHand().setState(Hand.State.STAND); // mark active hand as STAND
    if (player.hasNextHand() && player.getNextHand().getState() != Hand.State.STAND) {
      player.setActiveHand(player.getNextHand());
    } else {
      Hand dealerHand = game.getDealer().getHand();
      while (dealerHand.getHighCount() < 17) { // hits until soft 17
        dealerHand.addCard(deck.get());
      }
      dealerHand.setState(Hand.State.HIT);
    }
  }

  @Override
  public void transitionState(Game game) {
    Player player = game.getPlayer();
    Hand activeHand = player.getActiveHand();
    Hand dealerHand = game.getDealer().getHand();

    if (activeHand.getState() != Hand.State.STAND) {
      game.getPlayer().setAvailableActions(Player.Action.HIT, Player.Action.STAND); //, Player.Action.DOUBLE);
    } else {
      if (BlackJackUtils.isBlackJack(dealerHand)) {
        dealerHand.setState(Hand.State.BLACKJACK);
        game.setState(Game.State.DEALER_WON);
      } else if (BlackJackUtils.isBusted(dealerHand)) {
        dealerHand.setState(Hand.State.BUSTED);
        game.setState(Game.State.PLAYER_WON);
        payPlayer(player, 2);
      } else if (dealerHand.getHighCount() == activeHand.getHighCount()) {
        game.setState(Game.State.TIE);
        payPlayer(player, 1);
      } else if (dealerHand.getHighCount() < activeHand.getHighCount()) {
        game.setState(Game.State.PLAYER_WON);
        payPlayer(player, 2);
      } else if (dealerHand.getHighCount() > activeHand.getHighCount()) {
        game.setState(Game.State.DEALER_WON);
      }
      BlackJackUtils.setNextAvailableActionsAtEndOfGame(player);
    }
  }

  // wins 2:1 the bet's amount
  private void payPlayer(Player player, int times) {
    for (Hand hand : player.getHands()) {
      if (hand.getState() == Hand.State.STAND) {
        player.setAmountAvailable(player.getAmountAvailable() + times * hand.getBet());
        hand.setBet(0);
      }
    }
  }
}
