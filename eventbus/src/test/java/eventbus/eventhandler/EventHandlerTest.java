package eventbus.eventhandler;

import eventbus.event.Event;
import eventbus.event.EventDispatcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class EventHandlerTest {

  private Event event;
  private Event newMockEvent;
  private EventHandlerRegistry mockRegistry;
  private EventDispatcher mockDispatcher;
  private EventHandler handler;

  @Before
  public void setUp() throws Exception {
    event = new Event() {
    };
    newMockEvent = Mockito.mock(Event.class);
    mockRegistry = Mockito.mock(EventHandlerRegistry.class);
    mockDispatcher = Mockito.mock(EventDispatcher.class);

    handler = new EventHandler(mockDispatcher, mockRegistry) {
      @Override
      public Class getEventClass() {
        return event.getClass();
      }

      @Override
      public void handle(Event event) {
        mockDispatcher.dispatch(newMockEvent);
      }
    };
  }

  @Test
  public void testConstructor() {
    Mockito.verify(mockRegistry).register(handler);
  }

  @Test
  public void testGetEventClass() {
    Assert.assertEquals(event.getClass(), handler.getEventClass());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testHandle() {
    handler.handle(event);
    Mockito.verify(mockDispatcher).dispatch(newMockEvent);
  }

  @Test
  public void testGetDispatcher() {
    Assert.assertEquals(mockDispatcher, handler.getDispatcher());
  }
}
