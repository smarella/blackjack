package blackjack.model;

import java.util.*;

public class Player {
  private int amountAvailable;
  private List<Hand> hands = new ArrayList<>(1);
  private Hand active;
  private Set<Action> availableActions = new HashSet<>(1);

  public Player(int amountAvailable) {
    this.amountAvailable = amountAvailable;
    availableActions.add(Action.BET);
  }

  public int getAmountAvailable() {
    return amountAvailable;
  }

  public void setAmountAvailable(int amountAvailable) {
    this.amountAvailable = amountAvailable;
  }

  public List<Hand> getHands() {
    return hands;
  }

  public void addHand(Hand hand) {
    hands.add(hand);
    if (active == null) {
      setActiveHand(hand);
    }
  }

  public Hand getActiveHand() {
    return active;
  }

  public void setActiveHand(Hand active) {
    this.active = active;
  }

  public boolean isActiveHand(Hand hand) {
    return hand.equals(this.active);
  }

  public boolean hasNextHand() {
    return getNextHand() != null;
  }

  public Hand getNextHand() {
    int activeHandIndex = hands.indexOf(active);
    if (activeHandIndex == hands.size() - 1) {
      return null;
    }
    return hands.get(activeHandIndex + 1);
  }

  public Set<Action> getAvailableActions() {
    return availableActions;
  }

  public void setAvailableActions(Action... actions) {
    this.availableActions.clear();
    Collections.addAll(this.availableActions, actions);
  }

  public enum Action {
    NEW_GAME("Start a new game", "N"),
    BET("Place a bet", "$"),
    DEAL("Deal", "D"),
    HIT("Hit", "H"),
    STAND("Stand", "ST"),
    DOUBLE("Double down", "D"),
    SPLIT("Split", "SP"),
    QUIT("Quit", "Q");

    //TODO(Santosh): UI should have a separate way to convert the Action into user friendly help text and key bindings.
    private String help;
    private String keyBinding;

    Action(String help, String keyBinding) {
      this.help = help;
      this.keyBinding = keyBinding;
    }

    @Override
    public String toString() {
      return help + "(" + keyBinding + ")";
    }

    public static Action getActionFromString(String input) {
      for (Action action : values()) {
        if (action.keyBinding.equals(input)) {
          return action;
        }
      }
      try {
        Integer.valueOf(input);
        return BET;
      } catch (NumberFormatException nfe) {
      }
      return null;
    }
  }

}
