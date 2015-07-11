package eventbus.eventhandler;

import eventbus.event.Event;
import org.junit.Assert;
import org.junit.Test;

public class EventHandlerRegistryTest {

  @Test
  public void testRegistry() {
    Event event1 = new Event() {
    };
    Event event2 = new Event() {
    };

    EventHandlerRegistry registry = new EventHandlerRegistry();
    EventHandler eventHandler1 = getEventHandler(event1, registry);
    EventHandler eventHandler2 = getEventHandler(event2, registry);

    Assert.assertEquals(eventHandler1, registry.getHandlersForEvent(event1).get(0));
    Assert.assertEquals(eventHandler2, registry.getHandlersForEvent(event2).get(0));
  }

  @Test(expected = RuntimeException.class)
  public void testGetHandlersForEvent_throwsRunTimeException() {
    Event event = new Event() {
    };
    EventHandlerRegistry registry = new EventHandlerRegistry();
    registry.getHandlersForEvent(event);
  }

  private EventHandler getEventHandler(final Event event, final EventHandlerRegistry registry) {
    return new EventHandler(null, registry) {
      @Override
      public Class getEventClass() {
        return event.getClass();
      }

      @Override
      public void handle(Event event) {
      }
    };
  }


}