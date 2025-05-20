import java.awt.*;

public class RPG {
    int xpos;
    int ypos;
    int width = 70;
    int height = 70;
    boolean exploded = false;
    int explodedTime = 0;
    double dx;
    double dy;
    Image pic;

    public RPG(int startX, int startY, int targetX, int targetY) {
        xpos = startX;
        ypos = startY;
        pic = Toolkit.getDefaultToolkit().getImage("rockets.png");

        int speed = 6;

        if (targetX > startX) { //if the target X is greater than rocket X, set the speed to postiive
            dx = speed;
        } else if (targetX < startX) { //inverse
            dx = -speed;
        } else {
            dx = 0;
        }

        if (targetY > startY) {//if the target Y is greater than rocket Y, set the speed to postiive
            dy = speed;
        } else if (targetY < startY) { //inverse
            dy = -speed;
        } else {
            dy = 0;
        }
    }


    public void move() {
        if (!exploded) {
            xpos += dx;
            ypos += dy;
        }
    }


    public void draw(Graphics2D g) {
        g.drawImage(pic, xpos, ypos, width, height, null);
    }
}
