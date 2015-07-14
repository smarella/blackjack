package blackjack.module;

import blackjack.controller.*;
import blackjack.model.GameFactory;
import eventbus.module.EventBusModule;

public class BlackJackModule extends EventBusModule {

  @Override
  protected void configure() {
    binder().install(new EventBusModule());
    bind(GameFactory.class).asEagerSingleton();
    bind(NewGameRequestEventHandler.class).asEagerSingleton();
    bind(DealRequestEventHandler.class).asEagerSingleton();
    bind(BetRequestEventHandler.class).asEagerSingleton();
    bind(HitRequestEventHandler.class).asEagerSingleton();
    bind(StandRequestEventHandler.class).asEagerSingleton();
    bind(SplitRequestEventHandler.class).asEagerSingleton();
  }
}
