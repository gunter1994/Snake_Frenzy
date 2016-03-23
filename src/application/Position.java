package application;

/**
 * Created by gunter on 3/20/16.
 */
public class Position {
    private int x;
    private int y;
    public Position() {
        x = 0;
        y = 0;
    }
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean equals(Position p) {
        if (this.x == p.getX() && this.y == p.getY())
        {
            return true;
        }
        return false;
    }
}
