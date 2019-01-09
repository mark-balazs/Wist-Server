package application;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.Pair;

public class Controller {
    @FXML
    protected ListView<String> playersConnectedList;
    @FXML
    protected TextField numberOfPlayersField;
    @FXML
    protected Button startServerButton;
    @FXML
    protected Button startGameButton;
    @FXML
    protected Button cancelGameButton;
    @FXML
    protected TabPane tabPane;

    protected Stage stage;

    protected Round currentRound = new Round();
    // protected Stack<Round> roundRepo = new Stack<>();

    protected void alert(String message) {
	Platform.runLater(new Runnable() {

	    @Override
	    public void run() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Game Information");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.show();
	    }
	});

    }

    protected void nextStep(int currentAnswer) {
	Thread thread = new Thread(new Runnable() {

	    @Override
	    public void run() {
		if (currentRound.isPredictionPhase()) {
		    inPredictionPhase(currentAnswer);
		} else if (currentRound.isPickPhase()) {
		    inPickPhase(currentAnswer);
		}
	    }
	});
	thread.setDaemon(true);
	thread.start();
    }

    protected void inPredictionPhase(int currentAnswer) {
	System.out.println("In prediction phase, answer is: " + currentAnswer);
	currentRound.getPlayers().get(currentRound.getTurn()).setPrediction(currentAnswer);
	currentRound.setTurn((currentRound.getTurn() + 1) % currentRound.getNumberOfPlayers());
	currentRound.setTurnCounter((currentRound.getTurnCounter() + 1) % currentRound.getNumberOfPlayers());
	if (currentRound.getTurnCounter() == 0) {
	    System.out.println("Round ended");
	    currentRound.setPredictionPhase(false);
	    currentRound.setPickPhase(true);
	}
	System.out.println("Turn: " + currentRound.getTurn());
	System.out.println("TurnCounter: " + currentRound.getTurnCounter());
    }

    protected void inPickPhase(int currentAnswer) {
	if (currentRound.getMinorRound().getPlayedCards().isEmpty()) {
	    currentRound.getMinorRound().setFirstCardColor(
		    currentRound.getPlayers().get(currentRound.getTurn()).getCards().get(currentAnswer).getColor());
	}
	System.out.println("In pick phase, answer: " + currentAnswer);
	currentRound.getMinorRound().getPlayedCards()
		.add(new Pair<Player, Card>(currentRound.getPlayers().get(currentRound.getTurn()),
			currentRound.getPlayers().get(currentRound.getTurn()).getCards().get(currentAnswer)));
	System.out.println("Card added");
	currentRound.getPlayers().get(currentRound.getTurn()).getCards().remove(currentAnswer);
	System.out
		.println("Card removed from player " + currentRound.getPlayers().get(currentRound.getTurn()).getName());
	currentRound.setTurn((currentRound.getTurn() + 1) % currentRound.getNumberOfPlayers());
	currentRound.setTurnCounter((currentRound.getTurnCounter() + 1) % currentRound.getNumberOfPlayers());
	if (currentRound.getTurnCounter() == 0) {
	    minorRoundOver();
	}
	System.out.println("Turn: " + currentRound.getTurn());
	System.out.println("TurnCounter: " + currentRound.getTurnCounter());
    }

    protected void minorRoundOver() {
	System.out.println("Minor round ended");
	currentRound.setWaitingStage(true);
	for (Player p : currentRound.getPlayers()) {
	    p.setWaitingChecked(false);
	}
    }

    protected void roundOver() {
	System.out.println("Round ended");
	currentRound.setPredictionPhase(true);
	currentRound.setPickPhase(false);
	System.out.println("Setting scores");
	currentRound.setScores();
	System.out.println("Starting next round");
	if (currentRound.getRoundNumber() < 3 * currentRound.getNumberOfPlayers() + 11) {
	    currentRound.nextRound();
	    currentRound.setDealer((currentRound.getDealer() + 1) % currentRound.getNumberOfPlayers());
	    currentRound.setTurn((currentRound.getDealer() + 1) % currentRound.getNumberOfPlayers());
	} else {
	    currentRound.setRoundNumber(currentRound.getRoundNumber() + 1);
	    currentRound.setWaitingStage(true);
	}
    }
    public void setStage(Stage stage) {
	this.stage = stage;
    }

    public Stage getStage() {
	return stage;
    }

}
