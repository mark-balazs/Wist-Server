package application;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Enemy extends VBox{
    protected int streak;
    protected int score;
    protected String name;
    protected int won;
    protected int prediction;
    
    public Enemy(String name,int score,int streak,int prediction,int won) {
	setAlignment(Pos.CENTER);
	setPrefHeight(168);
	setPrefWidth(120);
	
	Label nameLabel = new Label();
	nameLabel.setText(name);
	getChildren().add(nameLabel);
	Label scoreLabel = new Label();
	scoreLabel.setText("" + score);
	getChildren().add(scoreLabel);
	Label streakLabel = new Label();
	streakLabel.setText("" + streak);
	getChildren().add(streakLabel);
	Label predLabel = new Label();
	predLabel.setText("" + prediction);
	getChildren().add(predLabel);
	Label wonLabel = new Label();
	wonLabel.setText("" + won);
	getChildren().add(wonLabel);
    }
}
