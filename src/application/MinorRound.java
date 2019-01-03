package application;

import java.util.ArrayList;

import javafx.util.Pair;

public class MinorRound {
    private ArrayList<Pair<Player, Card>> playedCards;
    private int firstCardColor;
    private int tromph;

    public int getFirstCardColor() {
	return firstCardColor;
    }

    public void setFirstCardColor(int firstCardColor) {
	this.firstCardColor = firstCardColor;
    }

    public ArrayList<Pair<Player, Card>> getPlayedCards() {
	return playedCards;
    }

    public void setPlayedCards(ArrayList<Pair<Player, Card>> playedCards) {
	this.playedCards = playedCards;
    }

    public int getTromph() {
	return tromph;
    }

    public void setTromph(int tromph) {
	this.tromph = tromph;
    }

    public Player winner() {
	Pair<Player, Card> winner = playedCards.get(0);
	for (Pair<Player, Card> p : playedCards) {
	    if (p.getValue().getColor() == tromph && winner.getValue().getColor() != tromph) {
		winner = p;
	    } else if (p.getValue().getColor() == tromph) {
		if (p.getValue().getNumber() > winner.getValue().getNumber()) {
		    winner = p;
		}
	    } else if (winner.getValue().getColor() != tromph && p.getValue().getColor() != tromph) {
		if (winner.getValue().getColor() != firstCardColor && p.getValue().getColor() == firstCardColor) {
		    winner = p;
		} else if (winner.getValue().getColor() == firstCardColor && p.getValue().getColor() == firstCardColor
			&& p.getValue().getNumber() > winner.getValue().getNumber()) {
		    winner = p;
		}
	    }
	}
	return winner.getKey();
    }
}
