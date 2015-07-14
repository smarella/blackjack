package blackjack.model;

import play.cards.Deck;
import play.cards.FiFoDeck;

/**
 * Factory that creates a game. It is configured as singleton in {@link blackjack.module.BlackJackModule}.
 * <p/>
 * All the event handlers that handle player actions access the game and deck via the factory.
 * <p/>
 * When the player hits "new game", the following are created:
 * - A new deck (single pack), shuffled.
 * - One hand each for dealer and player.
 * - A dealer object that holds the dealer's hand.
 * - A player object that holds the player's hand.
 */
public class GameFactory {
  private Game game;
  private Deck deck;

  // should ideally be broken into createGame and createPlayer. You can have a
  // multi player game, where the deck and dealer hands are created once and each player is created multiple times.
  // for a single player game, this should be fine.
  public void createGame(int playerMoney) {
    deck = FiFoDeck.create();
    deck.shuffle();
    game = new Game(new Dealer(), new Player(playerMoney));
    game.getDealer().setHand(new Hand());
    game.getPlayer().addHand(new Hand());
  }

  public Deck getDeck() {
    return deck;
  }

  public Game getGame() {
    return game;
  }
}
