package application;

import java.util.ArrayList;
import java.util.Random;

public class Round {
    private ArrayList<Player> players;
    private MinorRound minorRound;

    private int roundNumber = -1;
    private int numberOfCards;
    private int tromph;
    private int numberOfPlayers;
    private int turn = 0;
    private int turnCounter = 0;
    private int minorTurn = 0;

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
	for (Player p : players) {
	    if (p.getPrediction() == p.getReality()) {
		p.setScore(p.getScore() + 5 + p.getReality());
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
		p.setScore(p.getScore() - Math.abs(p.getPrediction() - p.getReality()));
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
	ArrayList<Card> used = new ArrayList<>();
	Random random = new Random();
	for (Player p : players) {
	    p.getCards().clear();
	    ;
	    p.setReality(0);
	    p.setPrediction(0);
	    for (int i = 0; i < numberOfCards; i++) {
		Card card = new Card();
		do {
		    card.setColor(random.nextInt(4));
		    card.setNumber(random.nextInt(2 * players.size()) + 13 - 2 * players.size());
		} while (isUsed(used, card));
		p.getCards().add(card);
	    }
	}
	if (numberOfCards != 8) {
	    Card card = new Card();
	    do {
		card.setColor(random.nextInt(4));
		card.setNumber(random.nextInt(2 * players.size()) + 13 - 2 * players.size());
	    } while (isUsed(used, card));
	    tromph = card.getColor();
	    minorRound.setTromph(tromph);
	} else {
	    tromph = -1;
	    minorRound.setTromph(tromph);
	}
    }

    private boolean isUsed(ArrayList<Card> usedCards, Card card) {
	for (Card c : usedCards) {
	    if (c.getColor() == card.getColor() && c.getNumber() == card.getNumber()) {
		return true;
	    }
	}
	return false;
    }

    public int getTromph() {
	return tromph;
    }

    public void setTromph(int tromph) {
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
}
