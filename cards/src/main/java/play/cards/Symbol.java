package play.cards;

public enum Symbol {
  ACE('A'),
  TWO('2'),
  THREE('3'),
  FOUR('4'),
  FIVE('5'),
  SIX('6'),
  SEVEN('7'),
  EIGHT('8'),
  NINE('9'),
  TEN('T'),
  JACK('J'),
  QUEEN('Q'),
  KING('K');

  private char symbol;

  Symbol(char symbol) {
    this.symbol = symbol;
  }

  @Override
  public String toString() {
    return String.valueOf(symbol);
  }
}
