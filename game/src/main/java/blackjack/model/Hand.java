package blackjack.model;

import play.cards.Card;
import play.cards.Symbol;

import java.util.ArrayList;
import java.util.List;

public class Hand {
  private State state = State.CREATED; // state of the Hand
  private List<Card> cards = new ArrayList<>(2);
  private int highCount = 0; // treats first Ace as 11
  private int lowCount = 0;  // treats all Aces as 1
  private int bet = 0;
  // internal book keeping
  private boolean hasAce = false;

  public Hand() {
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public void addCard(Card card) {
    cards.add(card);
    updateCounts(card, false);
  }

  public void removeCard(Card card) {
    cards.remove(card);
    updateCounts(card, true);
  }

  public List<Card> getCards() {
    return cards;
  }

  public int getHighCount() {
    return highCount;
  }

  public int getLowCount() {
    return lowCount;
  }

  public int getBet() {
    return bet;
  }

  public void setBet(int bet) {
    this.bet = bet;
  }

  private void updateCounts(Card card, boolean subtract) {
    int value = getCardValue(card);
    value = subtract ? -value : value;

    lowCount += value;
    highCount += value;
    if (!hasAce && card.getSymbol().equals(Symbol.ACE)) {
      hasAce = true;
      highCount += subtract ? -10 : 10;
    }
    if (highCount > 21) {
      highCount = lowCount;
    }
  }

  private int getCardValue(Card card) {
    return Math.min(card.getSymbol().ordinal() + 1, 10);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(cards.toString());
    if (highCount == lowCount) {
      builder.append(" (count = ").append(lowCount).append(")");
    } else {
      builder.append(" (counts = ").append(lowCount).append(",").append(highCount).append(")");
    }
    switch (state) {
      case BLACKJACK:
      case BUSTED:
        builder.append(" <==== ").append(state);
        break;
    }
    return builder.toString();
  }

  public enum State {
    CREATED,
    BET_PLACED, // from CREATED
    DEALT, // from BET_PLACED (for player) or CREATED (for dealer)
    BLACKJACK, // from DEALT
    HIT, // from DEALT
    BUSTED, // from HIT
    STAND, // from HIT or DEALT
  }

}
