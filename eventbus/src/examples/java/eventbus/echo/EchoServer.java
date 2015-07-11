package eventbus.echo;

import eventbus.event.EventDispatcher;
import eventbus.eventhandler.EventHandler;
import eventbus.eventhandler.EventHandlerRegistry;

import javax.inject.Inject;

public class EchoServer extends EventHandler<EchoRequest> {

  @Inject
  protected EchoServer(EventDispatcher dispatcher, EventHandlerRegistry registry) {
    super(dispatcher, registry);
  }

  @Override
  public Class<EchoRequest> getEventClass() {
    return EchoRequest.class;
  }

  @Override
  public void handle(EchoRequest event) {
    getDispatcher().dispatch(new EchoResponse("echo: " + event.getMessage()));
  }
}
