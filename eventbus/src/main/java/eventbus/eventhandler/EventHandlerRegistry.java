package eventbus.eventhandler;

import eventbus.event.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventHandlerRegistry {
  private Map<Class<? extends Event>, List<EventHandler>> eventHandlers = new HashMap<>();

  @SuppressWarnings("unchecked")
  public void register(EventHandler handler) {
    Class<? extends Event> eventClass = handler.getEventClass();
    List<EventHandler> handlers = eventHandlers.get(eventClass);
    if (handlers == null) {
      handlers = new ArrayList<>();
      eventHandlers.put(eventClass, handlers);
    }
    handlers.add(handler);
  }

  public List<EventHandler> getHandlersForEvent(Event event) {
    List<EventHandler> registeredHandlers = eventHandlers.get(event.getClass());
    if (registeredHandlers == null || registeredHandlers.isEmpty()) {
      throw new RuntimeException("No event handler registered to handle " + event.getClass().getName());
    }
    return registeredHandlers;
  }

}
