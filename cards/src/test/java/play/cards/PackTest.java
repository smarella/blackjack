package play.cards;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PackTest {

  @Test
  public void testCreate_counts() {
    Pack pack = Pack.create();
    Assert.assertEquals(52, pack.getCards().length);
    Map<Suite, List<Card>> suiteToCards = new HashMap<>();
    for (Card card : pack.getCards()) {
      List<Card> cards;
      if (suiteToCards.containsKey(card.getSuite())) {
        cards = suiteToCards.get(card.getSuite());
      } else {
        cards = new ArrayList<>(Symbol.values().length);
        suiteToCards.put(card.getSuite(), cards);
      }
      cards.add(card);
    }
    for (Suite suite : Suite.values()) {
      Assert.assertEquals("Expected 13 cards for suite: " + suite, 13, suiteToCards.get(suite).size());
    }
  }
}
