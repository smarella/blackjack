package play.cards;

import org.junit.Assert;
import org.junit.Test;

public class FiFoDeckTest {

  @Test
  public void testCreate() {
    Assert.assertNotNull(FiFoDeck.create());
    Assert.assertNotNull(FiFoDeck.create(2));
    CardsTestUtils.assertCardsAreFromSamePack(
      CardsTestUtils.getAllCardsInDeck(FiFoDeck.create()));
  }

  @Test
  public void testGet() {
    Deck d = FiFoDeck.create();
    for (int i = 0; i < 52; i++) {
      Assert.assertNotNull(d.get());
    }
    Assert.assertNull(d.get());
    Card card = Card.create(Suite.CLUB, Symbol.ACE);
    d.put(card);
    Card c = d.get();
    Assert.assertNotNull(c);
    Assert.assertEquals(c, card);
  }

  @Test
  public void testPut() {
    Deck d = FiFoDeck.create();
    Deck d2 = FiFoDeck.create();
    while (!d.isEmpty()) {
      d2.put(d.get());
    }
    Card[] allCards = new Card[2 * 52];
    int i = 0;
    while (!d2.isEmpty()) {
      allCards[i++] = d2.get();
    }

    for (i = 0; i < 52; i++) {
      Assert.assertEquals(allCards[i], allCards[i + 52]);
    }
  }

  @Test
  public void testIsEmpty() {
    Deck d = FiFoDeck.create();
    Assert.assertFalse(d.isEmpty());
    CardsTestUtils.drawCards(d, 52);
    Assert.assertTrue(d.isEmpty());

    d = FiFoDeck.create(40);
    Assert.assertFalse(d.isEmpty());
    d.shuffle();
    Assert.assertFalse(d.isEmpty());
    CardsTestUtils.drawCards(d, 40 * 52);
    Assert.assertTrue(d.isEmpty());
  }

  @Test
  public void testSize() {
    Deck d = FiFoDeck.create();
    Assert.assertEquals(52, d.size());
    CardsTestUtils.drawCards(d, 10);
    Assert.assertEquals(42, d.size());

    d = FiFoDeck.create(20);
    Assert.assertEquals(20 * 52, d.size());
    CardsTestUtils.drawCards(d, 200);
    Assert.assertEquals(20 * 52 - 200, d.size());

    d.shuffle();
    Assert.assertEquals(20 * 52 - 200, d.size());
    CardsTestUtils.drawCards(d, 200);
    Assert.assertEquals(20 * 52 - 400, d.size());
  }

  @Test
  public void testShuffle_singlePack() {
    Deck d = FiFoDeck.create();
    d.shuffle();
    CardsTestUtils.assertCardsAreFromSamePack(
      CardsTestUtils.getAllCardsInDeck(d));
  }

  @Test
  public void testShuffle_MultiPack() {
    Deck d = FiFoDeck.create(4);
    d.shuffle();
    CardsTestUtils.assertCardsAreFromPacks(4, CardsTestUtils.getAllCardsInDeck(d));
  }

}
