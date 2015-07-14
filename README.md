#blackjack [![Build Status](https://travis-ci.org/smarella/blackjack.svg?branch=version1)](https://travis-ci.org/smarella/blackjack)

###System Requirements
* JDK 1.7

###Try
```bash
export TERM=dumb # to suppress gradle build status messages
$PROJECT_HOME/gradlew game:run # PROJECT_HOME being the root of your git clone
```

###What is supported?
#####Player Actions:
- Place a bet
- Deal
- Hit
- Stand
- Split

#####House Rules
1. Blackjack pays 3 times the bet amount placed on a hand.
2. Dealer hits until his hand exceeds 17, with one Ace in dealer's hand counted as 11.
    - if dealer has two Aces, count is 12 (dealer continues to hit, since count < 17)
    - if dealer has a Ace and a Jack, count is 21 (black jack)
    - if dealer has a Ace and eight, count is 18 (dealer stops hitting at this point)
3. If dealer loses/busts player gets 2 times the bet amount associated with each hand put on 'stand'.
4. If there is a tie, player gets back the bet amount associated with the hand.
5. No re-splitting allowed.
6. Deck has 1 pack of cards (13 from each suite).
