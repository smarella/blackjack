package eventbus.echo;

import com.google.inject.AbstractModule;

public class EchoModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(EchoServer.class).asEagerSingleton();
  }
}
