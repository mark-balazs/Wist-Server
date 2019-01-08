package application;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Server extends Controller {
    protected ServerSocket server;
    protected boolean inGame = false;

    @FXML
    private void startServerButtonClicked(MouseEvent me) {
	if (numberOfPlayersField.getText().matches("[2-6]")) {
	    setupServer();
	    currentRound.setDealer(0);
	    currentRound.setTurn((currentRound.getDealer() + 1) % currentRound.getNumberOfPlayers());
	    currentRound.setPredictionPhase(true);
	    waitForConnections();
	    startServerButton.setDisable(true);
	    numberOfPlayersField.setDisable(true);
	} else {
	    alert("The number of players must be a whole number between 2 and 6!");
	    numberOfPlayersField.clear();
	}
    }

    @FXML
    protected void startGameButtonClicked(MouseEvent me) {
	inGame = true;
	currentRound.nextRound();
	SelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
	tabPane.getTabs().get(0).setDisable(true);
	selectionModel.select(1);
    }

    private void setupServer() {
	try {
	    server = new ServerSocket(4444, 5);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	currentRound.setNumberOfPlayers(Integer.parseInt(numberOfPlayersField.getText()));

    }

    private void waitForConnections() {
	Thread thread = new Thread(new Runnable() {

	    @Override
	    public void run() {
		for (int i = 0; i < currentRound.getNumberOfPlayers(); i++) {
		    try {
			Socket connection = server.accept();
			setupStreams(connection, i);
		    } catch (SocketException socketException) {

		    } catch (IOException e) {
			e.printStackTrace();
		    }
		}
		startGameButton.setDisable(false);
	    }
	});
	thread.setDaemon(true);
	thread.start();
    }

    private void setupStreams(final Socket connection, final int index) {
	Platform.runLater(new Runnable() {
	    @Override
	    public void run() {
		try {
		    ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
		    out.flush();
		    ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
		    ClientInfo info = new ClientInfo();
		    info.setConnection(connection);
		    info.setInput(in);
		    info.setOutput(out);
		    Player player = new Player();
		    player.setClientInfo(info);
		    player.setPlayerNumber(index);
		    currentRound.getPlayers().add(player);
		    createClientInputThread(currentRound.getPlayers().get(currentRound.getPlayers().size() - 1),
			    currentRound.getPlayers().size() - 1);
		    createClientOutputThread(currentRound.getPlayers().get(currentRound.getPlayers().size() - 1),
			    currentRound.getPlayers().size() - 1);
		    playersConnectedList.getItems().add(connection.getInetAddress().getHostName());
		} catch (IOException ioException) {
		    ioException.printStackTrace();
		}
	    }
	});
    }

    private void createClientOutputThread(Player player, int playerIndex) {
	Thread thread = new Thread(new Runnable() {

	    @Override
	    public void run() {
		try {
		    player.getClientInfo().getOutput().writeObject(playerIndex);
		    player.getClientInfo().getOutput().flush();

		} catch (Exception e) {
		    e.printStackTrace();
		}

		while (!player.getClientInfo().getConnection().isClosed()) {
		    try {
			Thread.sleep(500);
			player.getClientInfo().getOutput().writeObject(currentRound.createClientPackage(playerIndex));
			player.getClientInfo().getOutput().flush();
		    } catch (InterruptedException interruptedException) {
			interruptedException.printStackTrace();
		    } catch (IOException ioException) {
			ioException.printStackTrace();
		    }
		}
	    }
	});
	thread.setDaemon(true);
	thread.start();

    }

    private void createClientInputThread(Player player, int playerIndex) {
	Thread thread = new Thread(new Runnable() {

	    @Override
	    public void run() {
		try {
		    String name = player.getClientInfo().getInput().readObject().toString();
		    player.setName(name);

		    while (!player.getClientInfo().getConnection().isClosed()) {
			System.out.println("Waiting for " + currentRound.getTurn());
			Integer answer = (Integer) player.getClientInfo().getInput().readObject();
			System.out.println(player.getPlayerNumber() + " " + playerIndex + " sent " + answer);
			if (currentRound.getTurn() == player.getPlayerNumber() && !currentRound.isWaitingStage()) {
			    if (currentRound.isPickPhase()) {
				processPickPhaseInput(player, answer);
			    } else if (currentRound.isPredictionPhase()) {
				processPredictionPhaseInput(player, answer);
			    }

			} else if (currentRound.isWaitingStage() && !player.isWaitingChecked()) {
			    inWaitingStage(playerIndex);
			}
		    }
		} catch (ClassNotFoundException classNotFoundException) {
		    classNotFoundException.printStackTrace();
		} catch (IOException ioException) {
		    ioException.printStackTrace();
		} finally {
		    closeServer();
		}
	    }
	});
	thread.setDaemon(true);
	thread.start();
    }

    protected void processPickPhaseInput(Player player, int answer) {
	if (currentRound.getTurnCounter() != 0) {
	    if (player.hasColor(currentRound.getMinorRound().getFirstCardColor())
		    && player.getCards().get(answer).getColor() != currentRound.getMinorRound().getFirstCardColor()) {

	    } else if (!player.hasColor(currentRound.getMinorRound().getFirstCardColor())
		    && player.hasColor(currentRound.getTromph().getColor())
		    && player.getCards().get(answer).getColor() != currentRound.getTromph().getColor()) {

	    } else {
		System.out.println("Next step...");
		nextStep(answer);
	    }
	} else {
	    System.out.println("Next step...");
	    nextStep(answer);
	}
    }

    protected void processPredictionPhaseInput(Player player, int answer) {
	System.out.println("Next step...");
	nextStep(answer);
    }

    public void closeServer() {
	try {
	    for (Player p : currentRound.getPlayers()) {
		p.getClientInfo().close();
	    }
	    server.close();
	} catch (IOException ioException) {
	    ioException.printStackTrace();
	}
    }

    protected void waitingStageOver() {
	if (currentRound.getRoundNumber() < 3 * currentRound.getNumberOfPlayers() + 12) {
	    currentRound.getPlayers().get(currentRound.getMinorRound().winner())
		    .setWon(currentRound.getPlayers().get(currentRound.getMinorRound().winner()).getWon() + 1);
	    currentRound.setTurn(currentRound.getMinorRound().winner());
	    currentRound.getMinorRound().getPlayedCards().clear();
	    currentRound.getMinorRound().setFirstCardColor(-1);
	    currentRound.setMinorTurn((currentRound.getMinorTurn() + 1) % currentRound.getNumberOfCards());
	    if (currentRound.getMinorTurn() == 0 && currentRound.getTurnCounter() == 0) {
		roundOver();
	    }
	} else {
	    closeServer();
	    stage.close();
	}
    }

    protected void inWaitingStage(int playerIndex) {
	currentRound.getPlayers().get(playerIndex).setWaitingChecked(true);
	System.out.println("In waiting stage");
	System.out.println("Player done: " + playerIndex);
	if (currentRound.isAllWaitingsChecked()) {
	    currentRound.setWaitingStage(false);
	    System.out.println("All players done, exiting waiting phase");
	    waitingStageOver();
	}
    }

    @FXML
    protected void cancelGameButtonClicked(MouseEvent me) {
	closeServer();
	stage.close();
    }

}
