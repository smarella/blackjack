package play.cards;

public class Card {
  private final Suite suite;
  private final Symbol symbol;

  private Card(Suite suite, Symbol symbol) {
    this.suite = suite;
    this.symbol = symbol;
  }

  public Suite getSuite() {
    return suite;
  }

  public Symbol getSymbol() {
    return symbol;
  }

  @Override
  public int hashCode() {
    int result = suite != null ? suite.hashCode() : 0;
    result = 31 * result + (symbol != null ? symbol.hashCode() : 0);
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Card card = (Card) o;

    return suite == card.suite && symbol == card.symbol;
  }

  @Override
  public String toString() {
    return suite.toString() + symbol.toString();
  }

  static Card create(Suite suite, Symbol symbol) {
    return new Card(suite, symbol);
  }
}
