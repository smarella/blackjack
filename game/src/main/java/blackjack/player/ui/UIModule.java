package blackjack.player.ui;

import blackjack.module.BlackJackModule;
import com.google.inject.AbstractModule;

public class UIModule extends AbstractModule {

  @Override
  protected void configure() {
    binder().install(new BlackJackModule());
  }
}
