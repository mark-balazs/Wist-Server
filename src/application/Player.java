package application;

import java.util.ArrayList;

public class Player {
    private ArrayList<Card> cards = new ArrayList<>();
    private String name;
    private int playerNumber;
    private int score;
    private int prediction;
    private int reality;
    private int streak;
    private ClientInfo clientInfo = new ClientInfo();

    public Player() {
	score = 0;
	prediction = 0;
	reality = 0;
	streak = 0;
    }

    public ArrayList<Card> getCards() {
	return cards;
    }

    public void setCards(ArrayList<Card> cards) {
	this.cards = cards;
    }

    public int getScore() {
	return score;
    }

    public void setScore(int score) {
	this.score = score;
    }

    public int getPrediction() {
	return prediction;
    }

    public void setPrediction(int expectation) {
	this.prediction = expectation;
    }

    public int getReality() {
	return reality;
    }

    public void setReality(int reality) {
	this.reality = reality;
    }

    public int getStreak() {
	return streak;
    }

    public void setStreak(int streak) {
	this.streak = streak;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public ClientInfo getClientInfo() {
	return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
	this.clientInfo = clientInfo;
    }

    public int getPlayerNumber() {
	return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
	this.playerNumber = playerNumber;
    }

    public boolean hasColor(int color) {
	for (Card c : cards) {
	    if (c.getColor() == color) {
		return true;
	    }
	}
	return false;
    }

}
