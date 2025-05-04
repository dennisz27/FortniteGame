import java.awt.*;

public class RPG {
    int xpos, ypos, width = 70, height = 70;
    boolean exploded = false;
    int explodedTime = 0;
    double dx, dy;
    Image pic;

    public RPG(int startX, int startY, int targetX, int targetY) {
        xpos = startX;
        ypos = startY;
        pic = Toolkit.getDefaultToolkit().getImage("rockets.png");

        int diffX = targetX - startX;
        int diffY = targetY - startY;
        double distance = Math.sqrt(diffX * diffX + diffY * diffY);

        dx = (diffX / distance) * 6;
        dy = (diffY / distance) * 6;
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
