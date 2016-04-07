package application;

import javafx.scene.image.*;

/**
 * Created by gunter on 3/20/16.
 */
public class SnakePart {
    private SnakePart next = null;
    private ImageView image;
    private double cleanup;

    public SnakePart() {
        this.image = new ImageView();
        this.image.setX(0);
        this.image.setY(0);
    }

    public SnakePart(double x, double y) {
        this.cleanup = -1;
        this.image = new ImageView();
        this.image.setX(x);
        this.image.setY(y);
    }

    public void setNext(SnakePart s) {
        this.next = s;
    }

    public SnakePart getNext() {
        return this.next;
    }

    public void setImage(ImageView i) {
        this.image = i;
    }

    public ImageView getImage() {
        return this.image;
    }

    public void setCleanup(double c) {
        this.cleanup = c;
    }

    public double getCleanup() {
        return this.cleanup;
    }
}
