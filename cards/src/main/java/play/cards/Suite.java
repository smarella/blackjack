package play.cards;

public enum Suite {
  SPADE('\u2660'),
  CLUB('\u2663'),
  HEART('\u2665'),
  DIAMOND('\u2666');

  private char suite;

  Suite(char suite) {
    this.suite = suite;
  }

  @Override
  public String toString() {
    return String.valueOf(suite);
  }
}
