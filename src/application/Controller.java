package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Pair;

public class Controller {
    @FXML
    protected HBox cardBox;
    @FXML
    protected HBox enemyBox;
    @FXML
    protected HBox placedCardBox;
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

    protected List<Integer> turnOrder;

    protected boolean inPredictionPhase = false;
    protected boolean inRealPhase = false;
    protected boolean inGame = false;
    protected boolean inWaitingStage = false;

    protected Round currentRound = new Round();
    protected Stack<Round> roundRepo = new Stack<>();

    @FXML
    protected void cancelGameButtonClicked(MouseEvent me) {

    }

    protected void alert(String message) {
	Alert alert = new Alert(AlertType.INFORMATION);
	alert.setTitle("Game Information");
	alert.setHeaderText(null);
	alert.setContentText(message);
	alert.show();
    }

    protected void nextStep(int currentAnswer) {
	if (inPredictionPhase) {
	    currentRound.getPlayers().get(turnOrder.get(currentRound.getTurn())).setPrediction(currentAnswer);
	    currentRound.setTurn((currentRound.getTurn() + 1) % currentRound.getNumberOfPlayers());
	    currentRound.setTurnCounter((currentRound.getTurnCounter() + 1) % currentRound.getNumberOfPlayers());
	    if (currentRound.getTurnCounter() == 0) {
		inPredictionPhase = false;
		inRealPhase = true;
	    }
	} else if (inRealPhase) {
	    if (placedCardBox.getChildren().size() == 0) {
		currentRound.getMinorRound().setFirstCardColor(currentRound.getPlayers()
			.get(turnOrder.get(currentRound.getTurn())).getCards().get(currentAnswer).getColor());
	    }
	    placedCardBox.getChildren().add(
		    currentRound.getPlayers().get(turnOrder.get(currentRound.getTurn())).getCards().get(currentAnswer));
	    currentRound.getMinorRound().getPlayedCards()
		    .add(new Pair<Player, Card>(currentRound.getPlayers().get(turnOrder.get(currentRound.getTurn())),
			    currentRound.getPlayers().get(turnOrder.get(currentRound.getTurn())).getCards()
				    .get(currentAnswer)));
	    currentRound.getPlayers().get(turnOrder.get(currentRound.getTurn())).getCards().remove(currentAnswer);
	    currentRound.setTurn((currentRound.getTurn() + 1) % currentRound.getNumberOfPlayers());
	    currentRound.setTurnCounter((currentRound.getTurnCounter() + 1) % currentRound.getNumberOfPlayers());
	    if (currentRound.getTurnCounter() == 0) {
		currentRound.getPlayers().get(currentRound.getPlayers().indexOf(currentRound.getMinorRound().winner()))
			.setReality(currentRound.getPlayers()
				.get(currentRound.getPlayers().indexOf(currentRound.getMinorRound().winner()))
				.getReality() + 1);
		currentRound.setTurn(turnOrder
			.indexOf((Integer) currentRound.getPlayers().indexOf(currentRound.getMinorRound().winner())));
		placedCardBox.getChildren().clear();
		currentRound.getMinorRound().getPlayedCards().clear();
		currentRound.getMinorRound().setFirstCardColor(-1);
		currentRound.setMinorTurn((currentRound.getMinorTurn() + 1) % currentRound.getNumberOfCards());
	    }
	    if (currentRound.getMinorTurn() == 0) {
		inPredictionPhase = true;
		inRealPhase = false;
		currentRound.setScores();
		currentRound.nextRound();
		currentRound.setTurn((currentRound.getTurn() + 1) % currentRound.getNumberOfPlayers());
	    }
	}
    }

    @FXML
    protected void startGameButtonClicked(MouseEvent me) {
	turnOrder = new ArrayList<Integer>();
	for (int i = 0; i < currentRound.getNumberOfPlayers(); i++) {
	    turnOrder.add(i);
	}
	Collections.shuffle(turnOrder);
	currentRound.setTurn(0);
	inPredictionPhase = true;
	Player player = new Player();
	player.setName("House");
	player.setPlayerNumber(currentRound.getNumberOfPlayers() - 1);
	currentRound.getPlayers().add(player);
	currentRound.nextRound();
    }
}
