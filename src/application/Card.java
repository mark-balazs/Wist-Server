package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Card extends ImageView {

    private int number;
    private int color;

    public Card() {
	Image image = new Image("images/sample.jpg");
	// Image image = new Image("images/" + number + color + ".png");

	setImage(image);
	setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.8), 10, 0, 0, 0);");
    }

    public int getNumber() {
	return number;
    }

    public void setNumber(int number) {
	this.number = number;
    }

    public int getColor() {
	return color;
    }

    public void setColor(int color) {
	this.color = color;
    }
}
