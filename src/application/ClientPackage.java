package application;

import java.io.Serializable;
import java.util.ArrayList;

import javafx.util.Pair;

@SuppressWarnings("serial")
public class ClientPackage implements Serializable {
    private int minorTurn;
    private int turn;
    private ArrayList<String> playerNames;
    private ArrayList<Integer> playerScores;
    private ArrayList<Integer> playerStreaks;
    private ArrayList<Pair<Integer,Integer>> ownCards;
    private ArrayList<Pair<Integer,Integer>> enemyCards;
    private ArrayList<Pair<Integer,Integer>> playedCards;
    private Pair<Integer,Integer> tromph;
    private ArrayList<Integer> predictions;
    private ArrayList<Integer> won;
    private ArrayList<String> winners;
    private int numberOfPlayers;
    private int numberOfCards;
    private int dealerNumber;
    
    private boolean isPredictionPhase;
    private boolean isWaitingPhase;

    public int getMinorTurn() {
	return minorTurn;
    }

    public void setMinorTurn(int minorTurn) {
	this.minorTurn = minorTurn;
    }

    public int getTurn() {
	return turn;
    }

    public void setTurn(int turn) {
	this.turn = turn;
    }

    public ArrayList<String> getPlayerNames() {
	return playerNames;
    }

    public void setPlayerNames(ArrayList<String> playerNames) {
	this.playerNames = playerNames;
    }

    public ArrayList<Integer> getPlayerScores() {
	return playerScores;
    }

    public void setPlayerScores(ArrayList<Integer> playerScores) {
	this.playerScores = playerScores;
    }

    public ArrayList<Integer> getPlayerStreaks() {
	return playerStreaks;
    }

    public void setPlayerStreaks(ArrayList<Integer> playerStreaks) {
	this.playerStreaks = playerStreaks;
    }

    public int getNumberOfPlayers() {
	return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
	this.numberOfPlayers = numberOfPlayers;
    }

    public ArrayList<Integer> getPredictions() {
	return predictions;
    }

    public void setPredictions(ArrayList<Integer> predictions) {
	this.predictions = predictions;
    }

    public ArrayList<Integer> getWon() {
	return won;
    }

    public void setWon(ArrayList<Integer> won) {
	this.won = won;
    }

    public ArrayList<Pair<Integer,Integer>> getOwnCards() {
	return ownCards;
    }

    public void setOwnCards(ArrayList<Pair<Integer,Integer>> ownCards) {
	this.ownCards = ownCards;
    }

    public ArrayList<Pair<Integer,Integer>> getPlayedCards() {
	return playedCards;
    }

    public void setPlayedCards(ArrayList<Pair<Integer,Integer>> playedCards) {
	this.playedCards = playedCards;
    }

    public boolean isPredictionPhase() {
	return isPredictionPhase;
    }

    public void setPredictionPhase(boolean inPredictionPhase) {
	this.isPredictionPhase = inPredictionPhase;
    }

    public Pair<Integer,Integer> getTromph() {
	return tromph;
    }

    public void setTromph(Pair<Integer,Integer> tromph) {
	this.tromph = tromph;
    }

    public int getNumberOfCards() {
	return numberOfCards;
    }

    public void setNumberOfCards(int numberOfCards) {
	this.numberOfCards = numberOfCards;
    }

    public boolean isWaitingPhase() {
	return isWaitingPhase;
    }

    public void setWaitingPhase(boolean isWaitingStage) {
	this.isWaitingPhase = isWaitingStage;
    }

    public ArrayList<Pair<Integer,Integer>> getEnemyCards() {
	return enemyCards;
    }

    public void setEnemyCards(ArrayList<Pair<Integer,Integer>> enemyCards) {
	this.enemyCards = enemyCards;
    }

    public int getDealerNumber() {
	return dealerNumber;
    }

    public void setDealerNumber(int dealerNumber) {
	this.dealerNumber = dealerNumber;
    }

    public ArrayList<String> getWinners() {
	return winners;
    }

    public void setWinners(ArrayList<String> winners) {
	this.winners = winners;
    }
}
