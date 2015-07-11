package play.cards;

/**
 * Interface for a deck of cards.
 */
public interface Deck {

  /**
   * Checks if the Deck is empty or not.
   *
   * @return true if empty, false if not.
   */
  public boolean isEmpty();

  /**
   * Gets a card from the Deck or null if the Deck is empty.
   *
   * @return a card from the deck or null
   */
  public Card get();

  /**
   * Puts the given card into the deck.
   *
   * @param card card to be added to the deck
   * @return return true if the card is successfully added to the deck. false otherwise.
   */
  public boolean put(Card card);

  /**
   * Gets the number of cards in the deck.
   *
   * @return number of cards in the deck
   */
  public int size();

  /**
   * Shuffles the cards present in the deck.
   */
  public void shuffle();

}
