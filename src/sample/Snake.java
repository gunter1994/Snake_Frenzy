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
        Position p = new Position(9,10);
        head = new SnakePart(p);
        Position p2 = new Position(10,10);
        head.setNext(new SnakePart(p2));
        Position p3 = new Position(11,10);
        head.getNext().setNext(new SnakePart(p3));
        tail = head.getNext().getNext();
    }

    public void drawSnake(GridPane grid)
    {
        SnakePart c = head;
        ImageView[] view = new ImageView[400];
        for (int i = 0; i < 400; i++)
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
            System.out.println(p.getX() + " " + p.getY());
            grid.add(view[j], p.getX(), p.getY());
            c = c.getNext();
            j++;
        }
    }

    public void move(GridPane grid, int d)
    {
        SnakePart c = head;
        while (c != tail) {
            c.setPos(c.getNext().getPos());
            c = c.getNext();
        }
        Position p = c.getPos();
        if (d == 0) {
            p.setY((p.getY()-1);
            c.setPos(p);
        }
        else if (d == 1) {
            p.setX(p.getX()+1);
            c.setPos(p);
        }
        else if (d == 2) {
            p.setY(p.getY()+1);
            c.setPos(p);
        }
        else if (d == 3) {
            p.setX(p.getX()-1);
            c.setPos(p);
        }
        grid.getChildren().clear();
        this.drawSnake(grid);
    }
}
