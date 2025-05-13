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

        if (targetX > startX) {
            dx = speed;
        } else if (targetX < startX) {
            dx = -speed;
        } else {
            dx = 0;
        }

        if (targetY > startY) {
            dy = speed;
        } else if (targetY < startY) {
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
