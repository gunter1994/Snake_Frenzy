package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;

import java.io.File;

/**
 * Created by gunter on 3/20/16.
 */
public class Snake {
    public SnakePart head;
    public SnakePart tail;
    public Snake() {
        Position p = new Position(19,20);
        SnakePart c = new SnakePart(p);
        head = c;
        for (int i = 0; i < 4; i++) {
            p.setX(p.getX()+1);
            c.setNext(new SnakePart(p));
            c = c.getNext();
        }
        tail = c;
    }

    public void drawSnake(GridPane grid)
    {
        SnakePart c = head;
        ImageView[] view = new ImageView[1600]; //largest possible snake
        for (int i = 0; i < 1600; i++)
        {
            view[i] = new ImageView();
        }
        int j = 0;
        while (c != null) {
            Position p = c.getPos();
            if (c == head)
            {
                c.setPic(new Image(new File("snake_art/tail.png").toURI().toString()));
            }
            else if (c == tail)
            {
                c.setPic(new Image(new File("snake_art/head.png").toURI().toString()));
            }
            else
            {
                c.setPic(new Image(new File("snake_art/body.png").toURI().toString()));
            }
            view[j].setImage(c.getPic());
            view[j].setRotate(90*c.getOrientation());
            grid.add(view[j], p.getX(), p.getY());
            c = c.getNext();
            j++;
        }
    }

    public void move(GridPane grid, int d)
    {
        SnakePart c = head;
        //tail needs different orientation than others
        c.setPos(c.getNext().getPos());
        c.setOrientation(c.getNext().getNext().getOrientation());
        c = c.getNext();
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
        grid.getChildren().clear();
        this.drawSnake(grid);
    }
}
