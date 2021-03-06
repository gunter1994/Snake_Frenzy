package application;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.net.URL;
import java.util.Random;


public class Food {
    ImageView foodPic;
    MediaPlayer foodSound;

    public Food(Snake s, Group root) {
        foodPic = new ImageView(new Image(new File("resources/snake_art/food.png").toURI().toString()));
        URL resource = getClass().getResource("/audio/eat.mp3");
        foodSound = new MediaPlayer(new Media(resource.toString()));
        newFood(s, root);
    }

    public void changeFood(Image image) {
        foodPic.setImage(image);
    }

    public void changeSound(String file) {
        URL resource = getClass().getResource("/audio/" + file);
        foodSound = new MediaPlayer(new Media(resource.toString()));
    }

    //check if the snake ate the food
    public boolean checkFood(Group root, Snake s) {
        if (foodPic.getX() == s.tail.getImage().getX() && foodPic.getY() == s.tail.getImage().getY()) {
            root.getChildren().remove(foodPic); //deletes the food if eaten
            newFood(s, root);
            foodSound.play();
            changeSound("eat.mp3");
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
