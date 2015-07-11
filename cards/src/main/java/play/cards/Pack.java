package play.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pack {
  private final Card[] cards;

  private Pack(Card[] cards) {
    this.cards = cards;
  }

  public Card[] getCards() {
    return cards;
  }

  @Override
  public String toString() {
    List<Card> cards = new ArrayList<>(this.cards.length);
    Collections.addAll(cards, this.cards);
    return cards.toString();
  }

  static Pack create() {
    Card[] cards = new Card[Suite.values().length * Symbol.values().length];
    int i = 0;
    for (Suite suite : Suite.values()) {
      for (Symbol symbol : Symbol.values()) {
        cards[i++] = Card.create(suite, symbol);
      }
    }
    return new Pack(cards);
  }

  public static void main(String[] args) {
    System.out.println(Pack.create().toString());
  }
}
