package blackjack.controller;

import blackjack.controller.utils.BlackJackUtils;
import blackjack.event.DealRequestEvent;
import blackjack.model.Game;
import blackjack.model.GameFactory;
import blackjack.model.Hand;
import blackjack.model.Player;
import eventbus.event.EventDispatcher;
import eventbus.eventhandler.EventHandlerRegistry;
import play.cards.Deck;

import javax.inject.Inject;

public class DealRequestEventHandler extends PlayerActionEventHandler<DealRequestEvent> {

  @Inject
  public DealRequestEventHandler(EventDispatcher dispatcher,
                                 EventHandlerRegistry registry,
                                 GameFactory factory) {
    super(dispatcher, registry, factory);
  }

  @Override
  public Class<DealRequestEvent> getEventClass() {
    return DealRequestEvent.class;
  }

  @Override
  public void performPlayerAction(DealRequestEvent event, Game game, Deck deck) {
    Hand playerHand = game.getPlayer().getActiveHand();
    Hand dealerHand = game.getDealer().getHand();

    playerHand.addCard(deck.get());
    dealerHand.addCard(deck.get()); // only one card to dealer
    playerHand.addCard(deck.get());
  }

  @Override
  public void transitionState(Game game) {
    Player player = game.getPlayer();
    Hand playerHand = player.getActiveHand();
    if (BlackJackUtils.isBlackJack(playerHand)) {
      playerHand.setState(Hand.State.BLACKJACK);
      game.setState(Game.State.PLAYER_WON);
      BlackJackUtils.payPlayerOnBlackJack(player, playerHand);
      player.setAvailableActions(Player.Action.NEW_GAME, Player.Action.QUIT);
    } else if (BlackJackUtils.isSplittable(playerHand)) {
      playerHand.setState(Hand.State.DEALT);
      player.setAvailableActions(
        Player.Action.HIT,
        Player.Action.STAND,
        Player.Action.SPLIT);
    } else {
      playerHand.setState(Hand.State.DEALT);
      player.setAvailableActions(Player.Action.HIT, Player.Action.STAND);
    }
    game.getDealer().getHand().setState(Hand.State.DEALT);
  }

}
