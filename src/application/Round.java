package application;

import java.util.ArrayList;
import java.util.Random;

import javafx.util.Pair;

public class Round {
    /**
     * Stores information about the match's state
     */
    private ArrayList<Player> players = new ArrayList<>();
    private MinorRound minorRound = new MinorRound();

    private int roundNumber = -1;
    private int numberOfCards;
    private int numberOfPlayers;
    private int turn = 0;
    private int turnCounter = 0;
    private int minorTurn = 0;
    private int dealer = 0;

    private boolean isPickPhase = false;
    private boolean isPredictionPhase = false;
    private boolean isWaitingStage = false;

    private Card tromph = new Card(0, 0);

    public ArrayList<Player> getPlayers() {
	return players;
    }

    public void setPlayers(ArrayList<Player> players) {
	this.players = players;
    }

    public int getRoundNumber() {
	return roundNumber;
    }

    public void setRoundNumber(int number) {
	this.roundNumber = number;
    }

    public int getNumberOfCards() {
	return numberOfCards;
    }

    public void setNumberOfCards(int numberOfCards) {
	this.numberOfCards = numberOfCards;
    }

    public void setScores() {
	/**
	 * Sets the players' scores
	 */
	for (Player p : players) {
	    if (p.getPrediction() == p.getWon()) {
		p.setScore(p.getScore() + 5 + p.getWon());
		if (numberOfCards != 1) {
		    if (p.getStreak() >= 0) {
			p.setStreak(p.getStreak() + 1);
			if (p.getStreak() == 5) {
			    p.setScore(p.getScore() + 10);
			    p.setStreak(0);
			}
		    } else {
			p.setStreak(1);
		    }
		}
	    } else {
		p.setScore(p.getScore() - Math.abs(p.getPrediction() - p.getWon()));
		if (numberOfCards != 1) {
		    if (p.getStreak() <= 0) {
			p.setStreak(p.getStreak() - 1);
			if (p.getStreak() == -5) {
			    p.setScore(p.getScore() - 10);
			    p.setStreak(0);
			}
		    } else {
			p.setStreak(-1);
		    }
		}
	    }
	}
    }

    public void nextRound() {
	/**
	 * Calculates necessary information about the next round and sets the
	 * appropriate variables
	 */
	roundNumber++;
	if (roundNumber < players.size() || roundNumber >= 12 + 2 * players.size()) {
	    numberOfCards = 1;
	} else if (roundNumber >= players.size() && roundNumber < players.size() + 6) {
	    numberOfCards = roundNumber - players.size() + 2;
	} else if (roundNumber >= 6 + 2 * players.size() && roundNumber < 12 + 2 * players.size()) {
	    numberOfCards = 12 + 2 * players.size() - roundNumber + 1;
	} else if (roundNumber >= 6 + players.size() && roundNumber < 6 + 2 * players.size()) {
	    numberOfCards = 8;
	}
	dealCards();
    }

    private void dealCards() {
	/**
	 * Deals cards for each player and sets the tromph if necessary
	 */
	ArrayList<Card> used = new ArrayList<>();
	Random random = new Random();
	random.setSeed(System.currentTimeMillis());
	for (Player p : players) {
	    p.getCards().clear();
	    p.setWon(0);
	    p.setPrediction(0);
	    for (int i = 0; i < numberOfCards; i++) {
		Card card = new Card(0, 0);
		do {
		    card.setColor(random.nextInt(4));
		    card.setNumber(random.nextInt(2 * players.size()) + 13 - 2 * players.size() + 1);
		} while (isUsed(used, card));
		p.getCards().add(card);
		used.add(card);
	    }
	}
	if (numberOfCards != 8) {
	    Card card = new Card(0, 0);
	    do {
		card.setColor(random.nextInt(4));
		card.setNumber(random.nextInt(2 * players.size()) + 13 - 2 * players.size() + 1);
	    } while (isUsed(used, card));
	    tromph = card;
	    minorRound.setTromph(tromph);
	} else {
	    tromph = new Card(-1, -1);
	    minorRound.setTromph(tromph);
	}
    }

    private boolean isUsed(ArrayList<Card> usedCards, Card card) {
	/**
	 * Checks if the card was already dealt
	 */
	for (Card c : usedCards) {
	    if (c.getColor() == card.getColor() && c.getNumber() == card.getNumber()) {
		return true;
	    }
	}
	return false;
    }

    public ClientPackage createClientPackage(int playerIndex) {
	/**
	 * Creates a package that is to be sent to the given player
	 */
	ClientPackage clientPackage = new ClientPackage();

	ArrayList<String> playerNames = new ArrayList<>();
	ArrayList<Integer> playerScores = new ArrayList<>();
	ArrayList<Integer> playerStreaks = new ArrayList<>();
	ArrayList<Integer> predictions = new ArrayList<>();
	ArrayList<Integer> won = new ArrayList<>();
	ArrayList<Pair<Integer, Integer>> cards = new ArrayList<>();
	ArrayList<Pair<Integer, Integer>> enemyCards = new ArrayList<>();
	ArrayList<Pair<Integer, Integer>> placedCards = new ArrayList<>();
	for (Card c : players.get(playerIndex).getCards()) {
	    cards.add(new Pair<Integer, Integer>(c.getNumber(), c.getColor()));
	}
	for (Pair<Player, Card> p : minorRound.getPlayedCards()) {
	    placedCards.add(new Pair<Integer, Integer>(p.getValue().getNumber(), p.getValue().getColor()));
	}
	for (Player p : players) {
	    playerNames.add(p.getName());
	    playerScores.add(p.getScore());
	    playerStreaks.add(p.getStreak());
	    predictions.add(p.getPrediction());
	    won.add(p.getWon());
	    for (Card c : p.getCards()) {
		enemyCards.add(new Pair<Integer, Integer>(c.getNumber(), c.getColor()));
	    }
	}
	clientPackage.setTromph(
		new Pair<Integer, Integer>(minorRound.getTromph().getColor(), minorRound.getTromph().getNumber()));
	clientPackage.setMinorTurn(minorTurn);
	clientPackage.setPlayerNames(playerNames);
	clientPackage.setPlayerScores(playerScores);
	clientPackage.setPlayerStreaks(playerStreaks);
	clientPackage.setPredictions(predictions);
	clientPackage.setWon(won);
	clientPackage.setTurn(turn);
	clientPackage.setNumberOfPlayers(numberOfPlayers);
	clientPackage.setPlayedCards(placedCards);
	clientPackage.setOwnCards(cards);
	clientPackage.setPredictionPhase(isPredictionPhase());
	clientPackage.setNumberOfCards(numberOfCards);
	clientPackage.setWaitingPhase(isWaitingStage);
	clientPackage.setEnemyCards(enemyCards);
	clientPackage.setDealerNumber(dealer);
	if (isWaitingStage) {
	    clientPackage.setMinorRoundWinner(minorRound.winner());
	} else {
	    clientPackage.setMinorRoundWinner(-1);
	}
	if (roundNumber < 3 * numberOfPlayers + 12) {
	    clientPackage.setWinners(null);
	} else {
	    clientPackage.setWinners(getWinners());
	}

	return clientPackage;
    }

    public boolean isAllWaitingsChecked() {
	/**
	 * Checks if there are players in Waiting Stage
	 */
	for (Player p : players) {
	    if (!p.isWaitingChecked()) {
		return false;
	    }
	}
	return true;
    }

    public ArrayList<String> getWinners() {
	/**
	 * Calculates the winner(s) of the match
	 */
	ArrayList<String> winners = new ArrayList<>();
	int max = -Integer.MIN_VALUE;
	for (Player p : players) {
	    if (p.getScore() == 0 && max != 0) {
		max = 0;
		winners.add(p.getName());
	    } else if (p.getScore() == max) {
		winners.add(p.getName());
	    } else if (p.getScore() > max && max != 0) {
		winners.add(p.getName());
		max = p.getScore();
	    }
	}
	return winners;
    }

    public Card getTromph() {
	return tromph;
    }

    public void setTromph(Card tromph) {
	this.tromph = tromph;
    }

    public MinorRound getMinorRound() {
	return minorRound;
    }

    public void setMinorRound(MinorRound minorRound) {
	this.minorRound = minorRound;
    }

    public int getNumberOfPlayers() {
	return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
	this.numberOfPlayers = numberOfPlayers;
    }

    public int getTurn() {
	return turn;
    }

    public void setTurn(int turn) {
	this.turn = turn;
    }

    public int getTurnCounter() {
	return turnCounter;
    }

    public void setTurnCounter(int turnCounter) {
	this.turnCounter = turnCounter;
    }

    public int getMinorTurn() {
	return minorTurn;
    }

    public void setMinorTurn(int minorTurn) {
	this.minorTurn = minorTurn;
    }

    public int getDealer() {
	return dealer;
    }

    public void setDealer(int dealer) {
	this.dealer = dealer;
    }

    public boolean isPickPhase() {
	return isPickPhase;
    }

    public void setPickPhase(boolean inRealPhase) {
	this.isPickPhase = inRealPhase;
    }

    public boolean isPredictionPhase() {
	return isPredictionPhase;
    }

    public void setPredictionPhase(boolean inPredictionPhase) {
	this.isPredictionPhase = inPredictionPhase;
    }

    public boolean isWaitingStage() {
	return isWaitingStage;
    }

    public void setWaitingStage(boolean isWaitingStage) {
	this.isWaitingStage = isWaitingStage;
    }
}
