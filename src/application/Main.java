package application;

import java.awt.event.WindowEvent;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
    /**
     * Execution starts here
     * */
    @Override
    public void start(Stage primaryStage) {
	/**
	     * Loads the FXML file for the UI
	     * Opens the window
	     * */
	try {
	    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("WistServer.fxml"));
	    Parent root = fxmlLoader.load();
	    Scene scene = new Scene(root);
	    Server controller = (Server) fxmlLoader.getController();
	    controller.setStage(primaryStage);
	    primaryStage.setScene(scene);
	    primaryStage.setOnCloseRequest(e -> {
		try {
		    controller.closeServer();
		} catch (NullPointerException nullPointerException) {

		}
		// primaryStage.close();
	    });
	    primaryStage.show();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public static void main(String[] args) {
	launch(args);
    }
}
