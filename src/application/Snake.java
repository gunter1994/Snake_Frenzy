package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import java.io.File;

/**
 * Created by gunter on 3/20/16.
 */
public class Snake {
    public SnakePart head;
    public SnakePart tail;
    public int grow;
    private String pic;
    public void setPic(String i) {
        this.pic = i;
    }

    public String getPic() {
        return this.pic;
    }

    public Snake(String pic, Position p) {
        this.setPic(pic);
        SnakePart c = new SnakePart(p);
        head = c;
        for (int i = 1; i < 5; i++) {
            Position p2 = new Position(p.getX()+i, p.getY());
            c.setNext(new SnakePart(p2));
            c = c.getNext();
        }
        tail = c;
    }

    public void drawSnake(GridPane grid)
    {
        SnakePart c = head;
        ImageView[] view = new ImageView[1296]; //largest possible snake
        for (int i = 0; i < 1296; i++)
        {
            view[i] = new ImageView();
        }
        int j = 0;
        while (c != null) {
            Position p = c.getPos();
            Image im;
            int cornerOri = 0;
            if (c == head)
            {
                im =(new Image(new File("snake_art/" + this.getPic() + "/tail.png").toURI().toString()));
            }
            else if (c == tail)
            {
                im = (new Image(new File("snake_art/" + this.getPic() + "/head.png").toURI().toString()));
            }
            else
            {
                if (c.getNext().getOrientation() == c.getOrientation()) {
                    im = (new Image(new File("snake_art/" + this.getPic() + "/body.png").toURI().toString()));
                }
                else
                {
                    //0 = right up and down left 1 = down right 2 and left up 2 = left down and up right 3 = up left and right down
                    if ((c.getOrientation() == 0 && c.getNext().getOrientation() == 3) || (c.getOrientation() == 1 && c.getNext().getOrientation() == 2)) {
                        cornerOri = 0;
                    }
                    else if ((c.getOrientation() == 1 && c.getNext().getOrientation() == 0) || (c.getOrientation() == 2 && c.getNext().getOrientation() == 3)) {
                        cornerOri = 1;
                    }
                    else if ((c.getOrientation() == 2 && c.getNext().getOrientation() == 1) || (c.getOrientation() == 3 && c.getNext().getOrientation() == 0)) {
                        cornerOri = 2;
                    }
                    else if ((c.getOrientation() == 3 && c.getNext().getOrientation() == 2) || (c.getOrientation() == 0 && c.getNext().getOrientation() == 1)) {
                        cornerOri = 3;
                    }
                    im = (new Image(new File("snake_art/" + this.getPic() + "/corner.png").toURI().toString()));
                }
            }
            view[j].setImage(im);
            //special case for corner tiles
            if (!c.equals(tail) && !c.equals(head) && (c.getNext().getOrientation() != c.getOrientation())){
                view[j].setRotate(90 * cornerOri);
            }
            //normal case
            else {
                view[j].setRotate(90 * c.getOrientation());
            }
            grid.add(view[j], p.getX(), p.getY());
            c = c.getNext();
            j++;
        }
    }

    public void move(GridPane grid, int d)
    {
        SnakePart c = head;
        //tail needs different orientation than others
        if (grow == 0) //grows snake if needed
        {
            c.setPos(c.getNext().getPos());
            c.setOrientation(c.getNext().getNext().getOrientation());
            c = c.getNext();
        }
        else //adds a new joint where the tail would move too, and doesn't move tail
        {
            grow--;
            SnakePart temp = new SnakePart(c.getNext().getPos());
            temp.setNext(c.getNext());
            c.setNext(temp);
            c = c.getNext();
            c = c.getNext();
        }
        while (c != tail) {
            c.setPos(c.getNext().getPos());
            c.setOrientation(c.getNext().getOrientation());
            c = c.getNext();
        }
        Position p = c.getPos();
        if (d == 0) {
            p.setX(p.getX()+1);
            c.setPos(p);
            c.setOrientation(0);
        }
        else if (d == 1) {
            p.setY(p.getY()+1);
            c.setPos(p);
            c.setOrientation(1);
        }
        else if (d == 2) {
            p.setX(p.getX()-1);
            c.setPos(p);
            c.setOrientation(2);
        }
        else if (d == 3) {
            p.setY((p.getY()-1));
            c.setPos(p);
            c.setOrientation(3);
        }
        if (!this.checkCollision()) {
        grid.getChildren().clear();
            this.drawSnake(grid);
        }
    }

    public boolean checkCollision() {
        SnakePart c = tail; //actually the head of the snake
        if (c.getPos().getX() > 35 || c.getPos().getX() < 0 || c.getPos().getY() > 35 || c.getPos().getY() < 0)
        {
            return true;
        }
        SnakePart temp = head;
        while (temp.getNext().getNext() != tail)
        {
            if (temp.getPos().equals(c.getPos()))
            {
                return true;
            }
            temp = temp.getNext();
        }
        return false;
    }
}
