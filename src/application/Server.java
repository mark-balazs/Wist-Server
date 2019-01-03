package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.ResourceBundle;

public class Server extends Controller implements Initializable {
    protected ServerSocket server;

    @FXML
    private void startServerButtonClicked(MouseEvent me) {
	if (numberOfPlayersField.getText().matches("[2-6]")) {
	    setupServer();
	    waitForConnections();
	    startServerButton.setDisable(true);
	    numberOfPlayersField.setDisable(true);
	} else {
	    alert("The number of players must be a whole number between 2 and 6!");
	    numberOfPlayersField.clear();
	}
    }

    private void setupServer() {
	try {
	    server = new ServerSocket(6968, 4);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	currentRound.setNumberOfPlayers(Integer.parseInt(numberOfPlayersField.getText()));

    }

    private void waitForConnections() {
	Thread thread = new Thread(new Runnable() {

	    @Override
	    public void run() {
		for (int i = 0; i < currentRound.getNumberOfPlayers() - 1; i++) {
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
	thread.start();
    }

    private void setupStreams(final Socket connection, final int index) {
	Thread thread = new Thread(new Runnable() {
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
		    player.setName(connection.getInetAddress().getHostName());
		    player.setClientInfo(info);
		    player.setPlayerNumber(index);
		    currentRound.getPlayers().add(player);
		    createClientInputThread(currentRound.getPlayers().get(currentRound.getPlayers().size() - 1));
		    createClientOutputThread(currentRound.getPlayers().get(currentRound.getPlayers().size() - 1));
		    playersConnectedList.getItems().add(player.getName());
		} catch (IOException ioException) {
		    ioException.printStackTrace();
		}
	    }
	});
	thread.start();
    }

    private void createClientOutputThread(Player player) {
	// TODO: 1-card rounds work differently
	Thread thread = new Thread(new Runnable() {

	    @Override
	    public void run() {
		while (!inPredictionPhase && !inRealPhase) {

		}
		try {
		    player.getClientInfo().getOutput().writeObject(currentRound.getPlayers().indexOf(player));
		    player.getClientInfo().getOutput().flush();
		} catch (IOException ioException) {
		    ioException.printStackTrace();
		}
		while (true) {
		    try {

			Thread.sleep(50);
			player.getClientInfo().getOutput().writeObject(currentRound);
			player.getClientInfo().getOutput().flush();
		    } catch (InterruptedException interruptedException) {
			interruptedException.printStackTrace();
		    } catch (IOException ioException) {
			ioException.printStackTrace();
		    }
		}
	    }
	});
	thread.start();
    }

    private void createClientInputThread(Player player) {
	Thread thread = new Thread(new Runnable() {

	    @Override
	    public void run() {
		try {
		    String name = (String) player.getClientInfo().getInput().readObject();
		    player.setName(name);

		    while (!player.getClientInfo().getConnection().isClosed()) {
			int answer = (Integer) player.getClientInfo().getInput().readObject();
			if (turnOrder.get(currentRound.getTurn()) == player.getPlayerNumber()) {
			    if (inRealPhase) {
				if (currentRound.getTurnCounter() != 0) {
				    if (player.hasColor(currentRound.getMinorRound().getFirstCardColor())
					    && player.getCards().get(answer).getColor() != currentRound.getMinorRound()
						    .getFirstCardColor()) {

				    } else if (!player.hasColor(currentRound.getMinorRound().getFirstCardColor())
					    && player.hasColor(currentRound.getTromph())
					    && player.getCards().get(answer).getColor() != currentRound.getTromph()) {

				    } else {
					nextStep(answer);
				    }
				}
			    } else if (inPredictionPhase) {
				if (currentRound.getTurnCounter() == currentRound.getNumberOfPlayers() - 1) {
				    int sum = 0;
				    for (Player p : currentRound.getPlayers()) {
					sum += p.getPrediction();
				    }
				    if (answer == currentRound.getNumberOfCards() - sum) {

				    } else {
					nextStep(answer);
				    }
				}
			    }

			}
		    }
		} catch (ClassNotFoundException classNotFoundException) {

		} catch (IOException ioException) {
		    ioException.printStackTrace();
		} finally {
		    closeServer();
		}
	    }
	});
	thread.start();
    }

    private void closeServer() {
	try {
	    for (Player p : currentRound.getPlayers()) {
		p.getClientInfo().close();
	    }
	    server.close();
	} catch (IOException ioException) {
	    ioException.printStackTrace();
	}
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

    }

}
