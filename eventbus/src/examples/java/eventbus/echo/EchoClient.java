package eventbus.echo;

import com.google.inject.Guice;
import eventbus.event.EventDispatcher;
import eventbus.eventhandler.EventHandler;
import eventbus.eventhandler.EventHandlerRegistry;
import eventbus.module.EventBusModule;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class EchoClient extends EventHandler<EchoResponse> {

  @Inject
  protected EchoClient(EventDispatcher dispatcher, EventHandlerRegistry registry) {
    super(dispatcher, registry);
  }

  @Override
  public Class<EchoResponse> getEventClass() {
    return EchoResponse.class;
  }

  @Override
  public void handle(EchoResponse event) {
    System.out.println(event.getResponseMessage());
  }

  private void echo(String message) {
    getDispatcher().dispatch(new EchoRequest(message));
  }

  public static void main(String[] args) throws IOException {
    EchoClient service = Guice.createInjector(new EventBusModule(), new EchoModule()).getInstance(EchoClient.class);
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    String input = "";
    System.out.println("=== type 'exit' to stop the service ===");
    while (!input.equals("exit")) {
      input = in.readLine();
      service.echo(input);
    }
  }
}
