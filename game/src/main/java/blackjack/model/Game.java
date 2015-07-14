package blackjack.model;

public class Game {
  private Dealer dealer;
  private Player player;
  private State state = State.PLAYERS_TURN;

  public Game(Dealer dealer, Player player) {
    this.dealer = dealer;
    this.player = player;
  }

  public Dealer getDealer() {
    return dealer;
  }

  public Player getPlayer() {
    return player;
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public enum State {
    PLAYERS_TURN("Your turn"),
    TIE("SCORES TIED"),
    PLAYER_WON("YOU WIN :)"),
    DEALER_WON("YOU LOSE :(");

    private String status;

    State(String s) {
      this.status = s;
    }

    @Override
    public String toString() {
      return status;
    }
  }
}
