package eventbus.module;

import com.google.inject.AbstractModule;
import eventbus.EventBus;
import eventbus.event.EventDispatcher;
import eventbus.eventhandler.EventHandlerRegistry;


public class EventBusModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(EventHandlerRegistry.class).asEagerSingleton();
    bind(EventBus.class).asEagerSingleton();
    bind(EventDispatcher.class).to(EventBus.class);
  }
}
