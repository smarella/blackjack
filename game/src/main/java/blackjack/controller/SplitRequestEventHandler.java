package blackjack.controller;

import blackjack.controller.utils.BlackJackUtils;
import blackjack.event.SplitRequestEvent;
import blackjack.model.Game;
import blackjack.model.GameFactory;
import blackjack.model.Hand;
import blackjack.model.Player;
import eventbus.event.EventDispatcher;
import eventbus.eventhandler.EventHandlerRegistry;
import play.cards.Card;
import play.cards.Deck;

import javax.inject.Inject;
import java.util.List;

public class SplitRequestEventHandler extends PlayerActionEventHandler<SplitRequestEvent> {

  @Inject
  public SplitRequestEventHandler(EventDispatcher dispatcher,
                                  EventHandlerRegistry registry,
                                  GameFactory controller) {
    super(dispatcher, registry, controller);
  }

  @Override
  public Class<SplitRequestEvent> getEventClass() {
    return SplitRequestEvent.class;
  }

  @Override
  public void performPlayerAction(SplitRequestEvent event, Game game, Deck deck) {
    Player player = game.getPlayer();
    Hand activeHand = player.getActiveHand();
    List<Card> cards = activeHand.getCards();

    Hand newHand = new Hand();
    player.addHand(newHand);
    newHand.setBet(activeHand.getBet());
    BlackJackUtils.payHouseOnBet(player, activeHand.getBet());
    newHand.addCard(cards.get(1));
    activeHand.removeCard(cards.get(1));

    // deal an additional card into each hand
    activeHand.addCard(deck.get());
    newHand.addCard(deck.get());

    activeHand.setState(Hand.State.DEALT);
    newHand.setState(Hand.State.DEALT);

    if (BlackJackUtils.isBlackJack(activeHand)) {
      activeHand.setState(Hand.State.BLACKJACK);
      BlackJackUtils.payPlayerOnBlackJack(player, activeHand);
      player.setActiveHand(newHand); // will be newHand
    }

    if (BlackJackUtils.isBlackJack(newHand)) {
      newHand.setState(Hand.State.BLACKJACK);
      BlackJackUtils.payPlayerOnBlackJack(player, newHand);
    }

  }

  @Override
  public void transitionState(Game game) {
    Player player = game.getPlayer();
    Hand activeHand = player.getActiveHand();
    if (activeHand.getState() == Hand.State.BLACKJACK) {
      game.setState((Game.State.PLAYER_WON));
      BlackJackUtils.setNextAvailableActionsAtEndOfGame(player);
    } else {
      player.setAvailableActions(Player.Action.HIT, Player.Action.STAND);
    }
  }
}
