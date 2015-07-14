package blackjack;

import blackjack.controller.*;
import blackjack.event.*;
import blackjack.model.*;
import eventbus.event.EventDispatcher;
import eventbus.eventhandler.EventHandlerRegistry;
import org.junit.Assert;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import play.cards.Card;
import play.cards.Deck;

import java.util.Set;

public class TestHelper {
  private Game game;
  private Deck deck;
  private PlayerActionEvent event;
  private PlayerActionEventHandler handler;

  public TestHelper createTestFor(PlayerActionEvent event) {
    this.event = event;
    EventDispatcher dispatcher = Mockito.mock(EventDispatcher.class);
    EventHandlerRegistry registry = Mockito.mock(EventHandlerRegistry.class);
    GameFactory factory = Mockito.mock(GameFactory.class);
    if (event instanceof NewGameRequestEvent) {
      handler = new NewGameRequestEventHandler(dispatcher, registry, factory);
    } else if (event instanceof BetRequestEvent) {
      handler = new BetRequestEventHandler(dispatcher, registry, factory);
    } else if (event instanceof DealRequestEvent) {
      handler = new DealRequestEventHandler(dispatcher, registry, factory);
    } else if (event instanceof HitRequestEvent) {
      handler = new HitRequestEventHandler(dispatcher, registry, factory);
    } else if (event instanceof StandRequestEvent) {
      handler = new StandRequestEventHandler(dispatcher, registry, factory);
    } else if (event instanceof SplitRequestEvent) {
      handler = new SplitRequestEventHandler(dispatcher, registry, factory);
    }
    return this;
  }


  public TestHelper setupNewGameWithAmount(int amount) {
    game = new Game(new Dealer(), new Player(amount));
    game.getDealer().setHand(new Hand());
    game.getPlayer().addHand(new Hand());

    deck = Mockito.mock(Deck.class);
    return this;
  }

  public TestHelper placeBet(int bet) {
    Player player = game.getPlayer();
    player.setAmountAvailable(player.getAmountAvailable() - bet);
    player.getActiveHand().setBet(bet);
    return this;
  }

  public TestHelper placeBetOnAdditionalHand(int bet) {
    Player player = game.getPlayer();
    player.setAmountAvailable(player.getAmountAvailable() - bet);
    for (Hand hand : player.getHands()) {
      if (!hand.equals(player.getActiveHand())) {
        hand.setBet(bet);
      }
    }
    return this;
  }

  public TestHelper withDealerHand(Card... cards) {
    for (Card card : cards) {
      game.getDealer().getHand().addCard(card);
    }
    return this;
  }

  public TestHelper withDealerHandState(Hand.State state) {
    game.getDealer().getHand().setState(state);
    return this;
  }

  public TestHelper withActiveHand(Card... cards) {
    for (Card card : cards) {
      game.getPlayer().getActiveHand().addCard(card);
    }
    return this;
  }

  public TestHelper withAdditionalPlayerHand(Card... cards) {
    Hand hand = new Hand();
    game.getPlayer().addHand(hand);
    for (Card card : cards) {
      hand.addCard(card);
    }
    return this;
  }

  public TestHelper withAdditionalPlayerHandState(Hand.State state) {
    game.getPlayer().getHands().get(1).setState(state);
    return this;
  }

  public TestHelper withPlayerActiveHandState(Hand.State state) {
    game.getPlayer().getActiveHand().setState(state);
    return this;
  }


  public TestHelper withNextCardsFromDeckBeing(Card... cards) {
    OngoingStubbing<Card> when = Mockito.when(deck.get());
    for (Card card : cards) {
      when = when.thenReturn(card);
    }
    return this;
  }

  @SuppressWarnings("unchecked")
  public void performPlayerAction() {
    handler.performPlayerAction(event, game, deck);
  }

  public void performStateTransition() {
    handler.transitionState(game);
  }

  public TestHelper assertDealerHandIs(Card... cards) {
    Hand hand = game.getDealer().getHand();
    assertHandIs(hand, cards);
    return this;
  }

  public TestHelper assertPlayerActiveHandIs(Card... cards) {
    Hand hand = game.getPlayer().getActiveHand();
    assertHandIs(hand, cards);
    return this;
  }

  private void assertHandIs(Hand hand, Card[] cards) {
    for (Card card : cards) {
      Assert.assertTrue(hand.getCards().contains(card));
    }
    Assert.assertEquals(hand.getCards().size(), cards.length);
  }

  public TestHelper assertGameStateMovedTo(Game.State state) {
    Assert.assertEquals(state, game.getState());
    return this;
  }

  public TestHelper assertDealerHandStateMovedTo(Hand.State state) {
    Assert.assertEquals(state, game.getDealer().getHand().getState());
    return this;
  }

  public TestHelper assertPlayerActiveHandStateMovedTo(Hand.State state) {
    Assert.assertEquals(state, game.getPlayer().getActiveHand().getState());
    return this;
  }

  public TestHelper assertPlayersAvailableActions(Player.Action... actions) {
    Set<Player.Action> availableActions = game.getPlayer().getAvailableActions();
    for (Player.Action action : actions) {
      Assert.assertTrue(action.toString() + " not found", availableActions.contains(action));
    }
    Assert.assertEquals(availableActions.size(), actions.length);
    return this;
  }

  public TestHelper assertPlayerActiveHandHasBet(int bet) {
    Assert.assertEquals(bet, game.getPlayer().getActiveHand().getBet());
    return this;
  }

  public TestHelper assertPlayerHasAmount(int amount) {
    Assert.assertEquals(amount, game.getPlayer().getAmountAvailable());
    return this;
  }

  public TestHelper assertAdditionalHandHasBet(int bet) {
    Assert.assertEquals(bet, getNonActiveHand().getBet());
    return this;
  }

  public TestHelper assertAdditionalHandIs(Card... cards) {
    Hand hand = getNonActiveHand();
    for (Card card : cards) {
      Assert.assertTrue(hand.getCards().contains(card));
    }
    Assert.assertEquals(hand.getCards().size(), cards.length);
    return this;
  }

  public TestHelper assertAdditionalHandStateMovedTo(Hand.State state) {
    Assert.assertEquals(state, getNonActiveHand().getState());
    return this;
  }

  private Hand getNonActiveHand() {
    for (Hand hand : game.getPlayer().getHands()) {
      if (!hand.equals(game.getPlayer().getActiveHand())) {
        return hand;
      }
    }
    return null;
  }
}
