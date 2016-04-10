package application;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.Random;

/**
 * Created by gunter on 4/10/16.
 */
public class Food {
    ImageView foodPic;

    public Food(Snake s, Group root) {
        foodPic = new ImageView(new Image(new File("snake_art/food.png").toURI().toString()));
        newFood(s, root);
    }

    //check if the snake ate the food
    public boolean checkFood(Group root, Snake s) {
        if (foodPic.getX() == s.tail.getImage().getX() && foodPic.getY() == s.tail.getImage().getY()) {
            root.getChildren().remove(foodPic); //deletes the food if eaten
            newFood(s, root);
            return true;
        }
        return false;
    }

    //creates a new randomly located food not on top of the snake
    public void newFood(Snake s, Group root) {
        Random rand = new Random();
        int x = 0;
        int y = 0;
        boolean available = false;
        while (!available) { //makes sure the snake isn't in the way of the new food
            available = true;
            x = rand.nextInt(40)*20;
            y = rand.nextInt(20)*20;
            SnakePart c = s.head;

            while (c != null) {
                if (x == c.getImage().getX() && y == c.getImage().getY()) {
                    available = false;
                    break;
                }
                c = c.getNext();
            }
        }
        //sets food location then displays it
        foodPic.setX(x);
        foodPic.setY(y);
        root.getChildren().add(foodPic);
    }
}
