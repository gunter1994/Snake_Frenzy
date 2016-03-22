package sample;

import javafx.scene.image.*;

/**
 * Created by gunter on 3/20/16.
 */
public class SnakePart {
    private int orientation;
    private Position pos;
    private Image pic;
    private SnakePart next = null;

    public SnakePart() {
        this.pos = new Position(0,0);
    }

    public SnakePart(Position p) {
        this.pos = p;
        this.orientation = 0;
    }

    public void setPic(Image i) {
        this.pic = i;
    }

    public Image getPic() {
        return this.pic;
    }

    public void setPos(Position p) {
        this.pos = p;
    }

    public Position getPos() {
        Position p = new Position(this.pos.getX(), this.pos.getY());
        return p;
    }

    public void setNext(SnakePart s) {
        this.next = s;
    }

    public SnakePart getNext() {
        return this.next;
    }

    public void setOrientation(int o) {
        this.orientation = o;
    }

    public int getOrientation() {
        return this.orientation;
    }
}
