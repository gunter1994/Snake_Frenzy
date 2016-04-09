package application;

import javafx.scene.image.*;

// class for the individual parts of the snake
class SnakePart {
    private SnakePart next = null;
    private ImageView image;
    private double cleanup;

    SnakePart(double x, double y) {
        this.cleanup = -1;
        this.image = new ImageView();
        this.image.setX(x);
        this.image.setY(y);
    }

    void setNext(SnakePart s) {
        this.next = s;
    }

    SnakePart getNext() {
        return this.next;
    }

    ImageView getImage() {
        return this.image;
    }

    void setCleanup(double c) {
        this.cleanup = c;
    }

    double getCleanup() {
        return this.cleanup;
    }
}