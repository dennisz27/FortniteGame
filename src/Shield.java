import java.awt.*;

public class Shield {
    int xpos = 30;
    int ypos = 30;
    int width = 30;
    int height = 30;
    boolean collected = false;
    Image pic;

    public Shield(int x, int y) {
        xpos = x;
        ypos = y;
        pic = Toolkit.getDefaultToolkit().getImage("mini.png");
    }

    public void draw(Graphics2D g) {
        if (!collected) { //if not collected draw pot
            g.drawImage(pic, xpos, ypos, width, height, null);
        }
    }
}
