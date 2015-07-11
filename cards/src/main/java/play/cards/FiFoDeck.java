package play.cards;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * A Queue based implementation of {@link Deck}.
 */
public class FiFoDeck implements Deck {
  private final Queue<Card> cards = new LinkedList<>();

  private FiFoDeck(Pack[] packs) {
    for (Pack pack : packs) {
      Collections.addAll(cards, pack.getCards());
    }
  }

  @Override
  public boolean isEmpty() {
    return cards.peek() == null;
  }

  /**
   * Gets the first card from the queue or null if the queue is empty.
   *
   * @return first card from the queue.
   */
  @Override
  public Card get() {
    return cards.poll();
  }

  /**
   * Adds the card to the end of the queue
   *
   * @param card card to be added to the deck
   * @return always returns true as the implementation uses a linked list
   */
  @Override
  public boolean put(Card card) {
    return cards.add(card);
  }

  @Override
  public int size() {
    return cards.size();
  }

  @Override
  public void shuffle() {
    Collections.shuffle((List<?>) cards);
  }

  @Override
  public String toString() {
    return cards.toString();
  }

  public static Deck create() {
    return create(1);
  }

  public static Deck create(int numPacks) {
    Pack[] packs = new Pack[numPacks];
    for (int i = 0; i < numPacks; i++) {
      packs[i] = Pack.create();
    }
    return new FiFoDeck(packs);
  }

  public static void main(String[] args) {
    Deck deck = FiFoDeck.create();
    System.out.println(deck.toString());
    deck.shuffle();
    System.out.println(deck.toString());
    deck.shuffle();
    System.out.println(deck.toString());
  }
}
