package eventbus;

import eventbus.event.Event;
import eventbus.event.EventDispatcher;
import eventbus.eventhandler.EventHandler;
import eventbus.eventhandler.EventHandlerRegistry;

import javax.inject.Inject;

/**
 * A event dispatcher that delivers events to the handlers registered
 * with {@link EventHandlerRegistry} to receive events.
 * <p/>
 * NOTE: When multiple event handlers are registered for the same
 * event, the event is synchronously delivered to each of the handlers.
 */
public class EventBus implements EventDispatcher {

  private final EventHandlerRegistry registry;

  @Inject
  public EventBus(EventHandlerRegistry registry) {
    this.registry = registry;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void dispatch(Event event) {
    for (EventHandler handler : registry.getHandlersForEvent(event)) {
      handler.handle(event);
    }
  }
}
