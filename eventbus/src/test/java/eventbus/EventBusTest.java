package eventbus;

import eventbus.event.Event;
import eventbus.event.EventDispatcher;
import eventbus.eventhandler.EventHandler;
import eventbus.eventhandler.EventHandlerRegistry;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class EventBusTest {

  @SuppressWarnings("unchecked")
  @Test
  public void testDispatch() {
    Event mockEvent = Mockito.mock(Event.class);
    EventHandler mockEventHandler1 = Mockito.mock(EventHandler.class);
    EventHandler mockEventHandler2 = Mockito.mock(EventHandler.class);

    List<EventHandler> handlers = new ArrayList<>();
    handlers.add(mockEventHandler1);
    handlers.add(mockEventHandler2);

    EventHandlerRegistry mockRegistry = Mockito.mock(EventHandlerRegistry.class);
    Mockito.when(mockRegistry.getHandlersForEvent(mockEvent)).thenReturn(handlers);

    EventDispatcher dispatcher = new EventBus(mockRegistry);


    // perform test
    dispatcher.dispatch(mockEvent);

    // verify
    Mockito.verify(mockRegistry).getHandlersForEvent(mockEvent);
    Mockito.verify(mockEventHandler1).handle(mockEvent);
    Mockito.verify(mockEventHandler2).handle(mockEvent);
  }
}
