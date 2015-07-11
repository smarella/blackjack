package eventbus.echo;

import eventbus.event.Event;

public class EchoRequest implements Event {
  private final String message;

  public EchoRequest(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
