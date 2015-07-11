package eventbus.eventhandler;

import eventbus.event.Event;
import eventbus.event.EventDispatcher;

import javax.inject.Inject;

public abstract class EventHandler<T extends Event> {

  private final EventDispatcher dispatcher;

  @Inject
  protected EventHandler(EventDispatcher dispatcher, EventHandlerRegistry registry) {
    this.dispatcher = dispatcher;
    registry.register(this);
  }

  /**
   * Gets the class type of the events handled by this event handler.
   *
   * @return Class type of the event
   */
  public abstract Class<T> getEventClass();

  /**
   * Handles the event.
   *
   * @param event input event to handle
   */
  public abstract void handle(T event);

  /**
   * Allow the dispatcher to be readily accessible by child classes
   *
   * @return event dispatcher
   */
  protected EventDispatcher getDispatcher() {
    return dispatcher;
  }


}
