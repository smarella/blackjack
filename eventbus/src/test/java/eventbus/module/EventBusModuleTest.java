package eventbus.module;

import com.google.inject.Guice;
import com.google.inject.Injector;
import eventbus.EventBus;
import eventbus.event.EventDispatcher;
import eventbus.eventhandler.EventHandlerRegistry;
import org.junit.Assert;
import org.junit.Test;

public class EventBusModuleTest {

  @Test
  public void testEventBusModule() {
    assertClassIsSingleton(EventHandlerRegistry.class);
    assertClassIsSingleton(EventBus.class);
    assertClassIsSingleton(EventDispatcher.class);

    assertImplementationAndSingleton(EventDispatcher.class, EventBus.class);
  }

  private void assertClassIsSingleton(Class clazz) {
    Injector injector = Guice.createInjector(new EventBusModule());
    Assert.assertNotNull(injector.getInstance(clazz));
    Assert.assertEquals(injector.getInstance(clazz), injector.getInstance(clazz));
  }

  protected void assertImplementationAndSingleton(Class parent, Class child) {
    Injector injector = Guice.createInjector(new EventBusModule());
    Assert.assertTrue(injector.getInstance(parent).getClass().equals(injector.getInstance(child).getClass()));
    Assert.assertTrue(injector.getInstance(parent).equals(injector.getInstance(child)));
  }
}
