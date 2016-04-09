package application;

import javafx.scene.Group;
import javafx.scene.image.Image;
import java.io.File;

class Snake {
    SnakePart head; // head of the link list, tail of the snake
    SnakePart tail; // tail of the link list, head of the snake
    int grow;
    private String pic;
    void setPic(String i) {
        this.pic = i;
    }

    Snake(String pic, int x, int y) {
        this.setPic(pic);
        SnakePart c = new SnakePart(x*20, y*20);
        head = c;
        for (int i = 1; i < 5; i++) {
            c.setNext(new SnakePart(x*20 + i*20, y*20));
            c = c.getNext();
        }
        tail = c;
    }

    // clears the snake picture from the window
    void clearSnake(Group root) {
        SnakePart c = head;
        while (c != null) {
            root.getChildren().remove(c.getImage()); //removes the image from root
            c = c.getNext();
        }
    }

    // draws the snake from tail to head
    void drawSnake(Group root)
    {
        //draws the snake to the screen
        SnakePart c = head;
        while (c != null) {
            //checks what image to use
            if (c == head)
            {
                c.getImage().setImage((new Image(new File("snake_art/" + pic + "/tail.png").toURI().toString())));
            }
            else if (c == tail)
            {
                c.getImage().setImage((new Image(new File("snake_art/" + pic + "/head.png").toURI().toString())));
            }
            else
            {
                c.getImage().setImage(new Image(new File("snake_art/" + pic + "/body.png").toURI().toString()));
            }
            root.getChildren().add(c.getImage()); //adds the imageview to the screen
            c = c.getNext();
        }
    }

    void move(Group root, int d)
    {
        //only moves if not colliding
        if (!this.checkCollision()) {
            //first clean up all the corners
            SnakePart deCorn = head;
            deCorn = deCorn.getNext();
            while (deCorn !=tail) {
                if (deCorn.getCleanup() != -1) {
                    deCorn.getImage().setRotate(deCorn.getCleanup());
                    deCorn.getImage().setImage(new Image(new File("snake_art/" + pic + "/body.png").toURI().toString()));
                    deCorn.setCleanup(-1);
                }
                deCorn = deCorn.getNext();
            }
            SnakePart c = head;
            //tail can't be a corner, so needs to be seperate
            if (grow == 0) //checks to see if it needs to grow
            {
                c.getImage().setX(c.getNext().getImage().getX());
                c.getImage().setY(c.getNext().getImage().getY());
                if (c.getNext().getNext().getCleanup() == -1) {
                    c.getImage().setRotate(c.getNext().getImage().getRotate());
                }
                else {
                    c.getImage().setRotate(c.getNext().getCleanup());
                    c.getNext().setCleanup(-1);
                }
                c = c.getNext();
            } else //adds a new joint where the tail would move too, and doesn't move tail
            {
                grow--;
                SnakePart temp = new SnakePart(c.getNext().getImage().getX(), c.getNext().getImage().getY());
                temp.getImage().setImage(c.getNext().getImage().getImage());
                temp.getImage().setRotate(head.getImage().getRotate());
                temp.setCleanup(c.getNext().getCleanup());
                temp.setNext(c.getNext());
                c.setNext(temp);
                c = c.getNext();
                root.getChildren().add(c.getImage());
            }
            //make sure new direction is updated before cornering
            if (d == 0) {
                tail.getImage().setRotate(0.0);
            } else if (d == 1) {
                tail.getImage().setRotate(90.0);
            } else if (d == 2) {
                tail.getImage().setRotate(180.0);
            } else if (d == 3) {
                tail.getImage().setRotate(270.0);
            }

            // moves the body of the snake and sets the rotation
            while (c != tail) {
                c.getImage().setX(c.getNext().getImage().getX());
                c.getImage().setY(c.getNext().getImage().getY());
                if (c.getImage().getRotate() != c.getNext().getImage().getRotate()) {
                    int cornerOri; //checks all possible corners
                    if        ((c.getImage().getRotate() == 0.0 && c.getNext().getImage().getRotate() == 270.0)
                            || (c.getImage().getRotate() == 90.0 && c.getNext().getImage().getRotate() == 180.0)) {
                        cornerOri = 0;
                    }
                    else if   ((c.getImage().getRotate() == 90.0 && c.getNext().getImage().getRotate() == 0.0)
                            || (c.getImage().getRotate() == 180.0 && c.getNext().getImage().getRotate() == 270.0)) {
                        cornerOri = 1;
                    }
                    else if   ((c.getImage().getRotate() == 180.0 && c.getNext().getImage().getRotate() == 90.0)
                            || (c.getImage().getRotate() == 270.0 && c.getNext().getImage().getRotate() == 0.0)) {
                        cornerOri = 2;
                    }
                    else {
                        cornerOri = 3;
                    }

                    // sets the picture to a corner picture
                    c.getImage().setImage(new Image(new File("snake_art/" + pic + "/corner.png").toURI().toString()));
                    c.getImage().setRotate(90.0 * cornerOri);
                    c.setCleanup(c.getNext().getImage().getRotate());
                }
                else {
                    c.getImage().setRotate(c.getNext().getImage().getRotate());
                }
                c = c.getNext();
            } //move head of the snake
            if (d == 0) {
                tail.getImage().setX(tail.getImage().getX() + 20);
            } else if (d == 1) {
                tail.getImage().setY(tail.getImage().getY() + 20);
            } else if (d == 2) {
                tail.getImage().setX(tail.getImage().getX() - 20);
            } else if (d == 3) {
                tail.getImage().setY(tail.getImage().getY() - 20);
            }
        }
    }


    boolean checkCollision() {
        SnakePart c = tail; //actually the head of the snake
        //checks if snake is out of bounds
        if (c.getImage().getX() > 40*20 || c.getImage().getX() < 0 || c.getImage().getY() > 20*20 || c.getImage().getY() < 0)
        {
            return true;
        }

        //checks if snake is colliding with itself
        SnakePart temp = head;
        while (temp.getNext().getNext() != tail)
        {
            if (temp.getImage().getX() == c.getImage().getX() && temp.getImage().getY() == (c.getImage().getY()))
            {
                return true;
            }
            temp = temp.getNext();
        }
        return false;
    }
}