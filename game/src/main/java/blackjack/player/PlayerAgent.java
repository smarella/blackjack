package blackjack.player;

import blackjack.event.*;
import blackjack.model.Player;
import eventbus.event.EventDispatcher;
import eventbus.eventhandler.EventHandler;
import eventbus.eventhandler.EventHandlerRegistry;

import javax.inject.Inject;

public abstract class PlayerAgent extends EventHandler<GameStateUpdateEvent> {
  private final int PLAYER_START_MONEY = 100;
  protected GameStateUpdateEvent lastUpdate;
  private int newBet;

  @Inject
  protected PlayerAgent(EventDispatcher dispatcher, EventHandlerRegistry registry) {
    super(dispatcher, registry);
  }

  public final void executePlayerAction(Player.Action action) {
    switch (action) {
      case NEW_GAME:
        int money = lastUpdate == null ? PLAYER_START_MONEY :
          lastUpdate.getGame().getPlayer().getAmountAvailable();
        getDispatcher().dispatch(new NewGameRequestEvent(money));
        break;

      case BET:
        getDispatcher().dispatch(new BetRequestEvent(newBet));
        break;
      case DEAL:
        getDispatcher().dispatch(new DealRequestEvent());
        break;
      case HIT:
        getDispatcher().dispatch(new HitRequestEvent());
        break;
      case STAND:
        getDispatcher().dispatch(new StandRequestEvent());
        break;
      case DOUBLE:
        break;
      case SPLIT:
        getDispatcher().dispatch(new SplitRequestEvent());
        break;
      case QUIT:
        break;
    }
  }

  protected void setNewBet(int bet) {
    this.newBet = bet;
  }

  @Override
  public final Class<GameStateUpdateEvent> getEventClass() {
    return GameStateUpdateEvent.class;
  }

  @Override
  public final void handle(GameStateUpdateEvent event) {
    this.lastUpdate = event;
    handleGameStateUpdate(event);
  }

  protected abstract void handleGameStateUpdate(GameStateUpdateEvent event);
}
