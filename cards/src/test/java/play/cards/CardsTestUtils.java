package play.cards;

import org.junit.Assert;

public class CardsTestUtils {

  public static void drawCards(Deck d, int num) {
    while (num-- != 0) {
      d.get();
    }
  }

  public static Card[] getAllCardsInDeck(Deck d) {
    Card[] cards = new Card[d.size()];
    int i = 0;
    while (!d.isEmpty()) {
      cards[i++] = d.get();
    }
    return cards;
  }

  public static void assertCardsAreFromSamePack(Card... cards) {
    int[] suiteBitVectors = getSuiteBitVectorsForSinglePackDeck();
    for (Card c : cards) {
      suiteBitVectors[c.getSuite().ordinal()] ^= (1 << (c.getSymbol().ordinal() + 1));
    }
    for (Suite suite : Suite.values()) {
      Assert.assertEquals(0, suiteBitVectors[suite.ordinal()]);
    }
  }

  public static void assertCardsAreFromPacks(int numPacks, Card... cards) {
    Assert.assertEquals(0, cards.length % 52);
    Assert.assertEquals(numPacks, cards.length / 52);
    long[] symbolBitVectors = getSymbolBitVectorsForMultiPackDeck(numPacks);
    for (Card c : cards) {
      int pos = c.getSuite().ordinal() + 1;
      Symbol symbol = c.getSymbol();
      while ((symbolBitVectors[symbol.ordinal()] & (1 << pos)) == 0) {
        pos = pos + Suite.values().length;
      }
      symbolBitVectors[symbol.ordinal()] ^= 1 << pos;
    }
    for (Symbol symbol : Symbol.values()) {
      Assert.assertEquals(0, symbolBitVectors[symbol.ordinal()]);
    }
  }

  private static int[] getSuiteBitVectorsForSinglePackDeck() {
    int bitVector = 0;
    for (int i = 1; i <= 13; i++) {
      bitVector |= 1 << i;
    }
    int[] suiteBitVectors = new int[4];
    for (int i = 0; i < 4; i++) {
      suiteBitVectors[i] = bitVector;
    }
    return suiteBitVectors;
  }

  private static long[] getSymbolBitVectorsForMultiPackDeck(int numPacks) {
    Assert.assertTrue(numPacks <= 4);
    long bitVector = 0;
    for (int i = 1; i <= 4 * numPacks; i++) {
      bitVector |= 1 << i;
    }
    long[] symbolBitVectors = new long[13];
    for (int i = 0; i < 13; i++) {
      symbolBitVectors[i] = bitVector;
    }
    return symbolBitVectors;
  }


}
