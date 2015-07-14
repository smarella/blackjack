package blackjack.player.ui;

import blackjack.event.GameStateUpdateEvent;
import blackjack.model.Dealer;
import blackjack.model.Game;
import blackjack.model.Hand;
import blackjack.model.Player;
import blackjack.player.PlayerAgent;
import com.google.inject.Guice;
import eventbus.event.EventDispatcher;
import eventbus.eventhandler.EventHandlerRegistry;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BlackJackConsole extends PlayerAgent {

  private static final String SEPARATOR = "====================================\n";

  @Inject
  protected BlackJackConsole(EventDispatcher dispatcher, EventHandlerRegistry registry) {
    super(dispatcher, registry);
  }

  public void start() throws IOException {
    System.out.println("\n======WELCOME TO BLACKJACK ! PLAY HERE & WIN IN VEGAS !!=======\n");
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    Player.Action action = Player.Action.NEW_GAME;
    while (action != Player.Action.QUIT) {
      executePlayerAction(action);

      do {
        String input = in.readLine();
        action = Player.Action.getActionFromString(input);
        Player player = lastUpdate.getGame().getPlayer();
        if (action == null || !player.getAvailableActions().contains(action)) {
          handleInvalidOption(input, player);
          action = null;
        } else {
          System.out.print(SEPARATOR);
          if (action == Player.Action.BET) {
            Integer bet = Integer.valueOf(input);
            if (bet <= player.getAmountAvailable() && bet > 0) {
              setNewBet(bet);
            } else {
              handleInvalidBet(player);
              action = null;
            }
          }
        }
      } while (action == null);
    }
  }

  private void handleInvalidOption(String input, Player player) {
    StringBuilder builder = new StringBuilder();
    builder.append(">>>Invalid input: ").append(input).append("\n");
    addOptions(player, builder);
    System.out.print(builder.toString());
  }

  private void handleInvalidBet(Player player) {
    StringBuilder builder = new StringBuilder();
    builder.append(">>>Bet amount should be between 1 and ").append(player.getAmountAvailable()).append("\n");
    addOptions(player, builder);
    System.out.print(builder.toString());
  }

  @Override
  protected void handleGameStateUpdate(GameStateUpdateEvent event) {
    System.out.print(render(event.getGame()));
  }

  public String render(Game game) {
    Player player = game.getPlayer();
    Dealer dealer = game.getDealer();
    Game.State state = game.getState();

    StringBuilder builder = new StringBuilder();

    builder.append("Cash Available: $").append(player.getAmountAvailable()).append("\n");

    Hand activeHand = player.getActiveHand();
    if (activeHand == null || activeHand.getState().ordinal() < Hand.State.BET_PLACED.ordinal()) {
      addOptions(player, builder);
      return builder.toString();
    }

    if (state == Game.State.PLAYERS_TURN) {
      if (player.getHands().size() == 1) {
        builder.append("Bet: $").append(activeHand.getBet()).append("\n");
      } else {
        builder.append("Bets: [$")
          .append(player.getHands().get(0).getBet())
          .append(", $")
          .append(player.getHands().get(1).getBet())
          .append("]")
          .append("\n");
      }
    }

    if (!dealer.getHand().getCards().isEmpty() && !player.getActiveHand().getCards().isEmpty()) {
      builder.append("Dealer's hand: ").append(dealer.getHand().toString()).append("\n");
      if (player.getHands().size() == 1) {
        builder.append("Player's hand: ").append(activeHand.toString()).append("\n");
      } else {
        builder.append("Player's hands: ").append("\n");
        for (Hand hand : player.getHands()) {
          builder.append("\t").append(hand.toString());
          if (player.isActiveHand(hand)) {
            builder.append(" * ").append("\n");
          } else {
            builder.append("\n");
          }
        }
      }
    }

    if (state != Game.State.PLAYERS_TURN) {
      builder.append("Result: *** ").append(state.toString()).append(" ***\n");
    }

    addOptions(player, builder);

    return builder.toString();
  }

  private void addOptions(Player player, StringBuilder builder) {
    builder.append(SEPARATOR);
    builder.append("Options: ").append(player.getAvailableActions().toString()).append(": ");
  }

  public static void main(String[] args) throws IOException {
    BlackJackConsole console = Guice.createInjector(new UIModule()).getInstance(BlackJackConsole.class);
    console.start();
  }

}
