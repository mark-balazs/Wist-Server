package application;

import java.util.ArrayList;

import javafx.util.Pair;

public class MinorRound {
    private ArrayList<Pair<Player, Card>> playedCards = new ArrayList<>();
    private int firstCardColor;
    private Card tromph = new Card(0,0);

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

    public Card getTromph() {
	return tromph;
    }

    public void setTromph(Card tromph) {
	this.tromph = tromph;
    }
    
    public ArrayList<Card> getCards() {
	ArrayList<Card> cards = new ArrayList<>();
	for(Pair<Player,Card> p: playedCards) {
	    cards.add(p.getValue());
	}
	return cards;
    }

    public int winner() {
	Pair<Player, Card> winner = playedCards.get(0);
	for (Pair<Player, Card> p : playedCards) {
	    if (p.getValue().getColor() == tromph.getColor() && winner.getValue().getColor() != tromph.getColor()) {
		winner = p;
	    } else if (p.getValue().getColor() == tromph.getColor()) {
		if (p.getValue().getNumber() > winner.getValue().getNumber()) {
		    winner = p;
		}
	    } else if (winner.getValue().getColor() != tromph.getColor() && p.getValue().getColor() != tromph.getColor()) {
		if (winner.getValue().getColor() != firstCardColor && p.getValue().getColor() == firstCardColor) {
		    winner = p;
		} else if (winner.getValue().getColor() == firstCardColor && p.getValue().getColor() == firstCardColor
			&& p.getValue().getNumber() > winner.getValue().getNumber()) {
		    winner = p;
		}
	    }
	}
	return winner.getKey().getPlayerNumber();
    }
}
