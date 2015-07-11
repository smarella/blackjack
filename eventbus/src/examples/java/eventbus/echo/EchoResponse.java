package eventbus.echo;

import eventbus.event.Event;

public class EchoResponse implements Event {
  private final String responseMessage;


  public EchoResponse(String responseMessage) {
    this.responseMessage = responseMessage;
  }

  public String getResponseMessage() {
    return responseMessage;
  }
}
