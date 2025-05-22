//Basic Game Application
// Basic Object, Image, Movement
// Threaded

//*******************************************************************************
//Import Section
//Add Java libraries needed for the game
//import java.awt.Canvas;

//Graphics Libraries
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;

//*******************************************************************************


/***
 * Step 1: implement KeyListener
 * Can be done by click the red lightbulb
 */

public class BasicGameApp implements Runnable, KeyListener {

    //Variable Definition Section
    //Declare the variables used in the program
    //You can set their initial values too
    public Character jonesy;
    public Shield mini;

    Image backgroundPic;

    //Sets the width and height of the program window
    final int WIDTH = 1000;
    final int HEIGHT = 700;

    //Declare the variables needed for the graphics
    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;

    public BufferStrategy bufferStrategy;

    public int randomX() {
        return (int)(Math.random() * 950); //set a random X coord on screen
    }

    public int randomY() {
        return (int)(Math.random() * 650); //set a random Y coord on screen
    }

    public boolean gameOver = false; //end Screen
    public int jonesyHealth = 3; //set jonesy initial health
    public int score = 0; //set initial score
    boolean showStartScreen = true; //start Screen

    int maxShields = 3;
    // Main method definition
    // This is the code that runs first and automatically
    public static void main(String[] args) {
        BasicGameApp ex = new BasicGameApp();   //creates a new instance of the game
        new Thread(ex).start();                 //creates a threads & starts up the code in the run( ) method
    }


    ArrayList<Shield> pots = new ArrayList<>(); //arrayList of pots
    ArrayList<RPG> rpgs = new ArrayList<>(); //arrayList of rpgs


    // This section is the setup portion of the program
    // Initialize your variables and construct your program objects here.
    public BasicGameApp() { // BasicGameApp constructor

        setUpGraphics();

        /***
         * Step 2: addKeyListener(this)
         */

        canvas.addKeyListener(this);
        backgroundPic = Toolkit.getDefaultToolkit().getImage("wowza.jpg");

        //variable and objects
        //create (construct) the objects needed for the game
        jonesy = new Character(450,300,0,0,100,100);
        jonesy.name = "Jonesy"; //declare jonesy name
        jonesy.pic = Toolkit.getDefaultToolkit().getImage("jonesy.png"); //get jonesy image
        mini = new Shield(randomX(), randomY()); //set mini to a new shield class with Random X and Random Y
        mini.pic = Toolkit.getDefaultToolkit().getImage("mini.png"); // get mini image

    } // end BasicGameApp constructor


//*******************************************************************************
//User Method Section
//
// put your code to do things here.

    // main thread
    // this is the code that plays the game after you set things up
    public void run() {
        //for the moment we will loop things forever.
        long lastShieldSpawn = System.currentTimeMillis(); //set var to the time of lastshiield spawn
        long lastRPGSpawn = System.currentTimeMillis();
//set var to the time of lastRPg spawn
        while (true) { //runs code
            if (showStartScreen) { //runs it showStartScreen is true
                render();
            } else if (!gameOver) { //if gameOver is not true

                long currentTime = System.currentTimeMillis(); //keeps currenTime

                if (currentTime - lastShieldSpawn > 4000) { // spawns shield after every 4 seconds
                    pots.add(new Shield(randomX(), randomY())); //add to pots
                    lastShieldSpawn = currentTime; //set the last shield spawn to the current time
                }

                if (currentTime - lastRPGSpawn > 1500) { // spawns rocket after every 1.5 seconods
                    int Xco = 0;
                    int Yco = 0;
                    int side = (int) (Math.random() * 4); //random side of the screen

                    if (side == 0) { //spawns shield on random side
                        Xco = 0;
                        Yco = (int) (Math.random() * HEIGHT); //spawns on side on random part on the left
                    } else if (side == 1) {
                        Xco = WIDTH;
                        Yco = (int) (Math.random() * HEIGHT); //spawns on random part of the right side
                    } else if (side == 2) {
                        Xco = (int) (Math.random() * WIDTH); //spawns on random part of top
                        Yco = 0;
                    } else if (side == 3) {
                        Xco = (int) (Math.random() * WIDTH); //spawns on random part on the bottom
                        Yco = HEIGHT;
                    }

                    rpgs.add(new RPG(Xco, Yco, jonesy.xpos, jonesy.ypos)); //add new rpg to arraylist
                    lastRPGSpawn = currentTime; //set last rpg spawn to currenttime
                }

                checkKeys();
                jonesy.move();
                for (RPG r : rpgs) { //for every item in rpgs list move using method called in RPG.java
                    r.move();
                }

                collide(); //check collision
                moveThings();
            }
            render();
            pause(8); //sets the refresh rate of game
        }  //move all the game objects

    }

    public void checkKeys() {


        int speed = 2;
        jonesy.dx = 0;
        jonesy.dy = 0;
        if (jonesy.up) {jonesy.dy = -speed;} //if jonesy up is true move speed up
        if (jonesy.down) {jonesy.dy = speed;} //if jonesy down is true move char down
        if (jonesy.left) {jonesy.dx = -speed;} //if jonesy left is true move left
        if (jonesy.right) {jonesy.dx = speed;} //if jonesy right is true move right

        jonesy.move();


    }

    public void moveThings() {


        jonesy.wrap();

        if(jonesy.left == true) {
            jonesy.pic = Toolkit.getDefaultToolkit().getImage("jonesy1.png");
        } else if(jonesy.right == true) {
            jonesy.pic = Toolkit.getDefaultToolkit().getImage("jonesy.png");
        }
        //call the move() code for each object
    }

    public void collide() {

        for (Shield pot : pots) { //for every shield pot collected make pot dissapear and scor eincrease
            if (!pot.collected && new Rectangle(pot.xpos, pot.ypos, pot.width, pot.height)
                    .intersects(new Rectangle(jonesy.xpos, jonesy.ypos, jonesy.width, jonesy.height))) {
                pot.collected = true;
                score++;
            }
        }

        for (int i = 0; i < rpgs.size(); i++) { //for every rpg hit remove a health
            RPG rpg = rpgs.get(i);
            Rectangle rpgRect = new Rectangle(rpg.xpos, rpg.ypos, rpg.width, rpg.height);
            Rectangle jonesyRect = new Rectangle(jonesy.xpos, jonesy.ypos, jonesy.width, jonesy.height);

            if (rpgRect.intersects(jonesyRect)) {
                System.out.println("hit");
                rpgs.remove(i);
                i--; //remove the item from the arraylist and hide it on screen
                jonesyHealth--; //subtract jonesy health

                if(jonesyHealth == 0) { //if health is zero set gameover to true
                    gameOver = true;
                }
            }
        }

    }

    //Paints things on the screen using bufferStrategy
    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);

        g.drawImage(backgroundPic, 0, 0, WIDTH, HEIGHT, null);

        if (showStartScreen == true) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Press ENTER to Start", 350, 350);
            g.drawString("Dodge Rockets and Collect Shields", 280, 400);
            g.dispose();
            bufferStrategy.show();
            return;
        }

        if (gameOver == true) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("GAME OVER", 370, 300);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Score: " + score, 450, 350);
            g.drawString("Press ENTER to Restart", 370, 400);

            g.dispose();
            bufferStrategy.show();
            return;
        }



        g.drawImage(backgroundPic, 0, 0, WIDTH, HEIGHT, null);
        g.drawImage(jonesy.pic, jonesy.xpos, jonesy.ypos, jonesy.width, jonesy.height, null);

        for (Shield pot : pots) pot.draw(g);
        for (RPG rpg : rpgs) rpg.draw(g);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + score, 20, 60);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Health: " + jonesyHealth, 875, 60);

        g.dispose();
        bufferStrategy.show();

    }

    //Pauses or sleeps the computer for the amount specified in milliseconds
    public void pause(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    //Graphics setup method
    private void setUpGraphics() {
        frame = new JFrame("Application Template");   //Create the program window or frame.  Names it.

        panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
        panel.setLayout(null);   //set the layout

        // creates a canvas which is a blank rectangular area of the screen onto which the application can draw
        // and trap input events (Mouse and Keyboard events)
        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);  // adds the canvas to the panel.

        // frame operations
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
        frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
        frame.setResizable(false);   //makes it so the frame cannot be resized
        frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!

        // sets up things so the screen displays images nicely.
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
        System.out.println("DONE graphic setup");
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

            if (keyCode == 10) {
                if (showStartScreen || gameOver) {
                    showStartScreen = false;
                    gameOver = false;
                    jonesyHealth = 3;
                    score = 0;
                    rpgs.clear();
                    pots.clear();
                    jonesy.xpos = 450;
                    jonesy.ypos = 300;
                }
            }

        if (keyCode == 10){
        }

        if (keyCode == 87) {
            jonesy.up = true;
            jonesy.lup = true;
        }

        if (keyCode == 83) {
            jonesy.down = true;
            jonesy.ldown = true;

        }

        if (keyCode == 68) {
            jonesy.right = true;
            jonesy.lright = true;
        }

        if (keyCode == 65){
            jonesy.left = true;
            jonesy.lleft = true;
        }


//        if (keyCode == 68) {
//            santa.dx = 5;
//        }
//
//        if (keyCode == 65){
//            santa.dx=-5;
//        }
//
//        if (keyCode == 68) {
//            santa.dx=5;
//        }
//
//        if (keyCode == 87){
//            santa.dy= -5;
//        }
//
//        if (keyCode == 83){
//            santa.dy= 5;
//        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if(keyCode == 87) {
            jonesy.lup = false;
            jonesy.up = false;
        }

        if (keyCode == 83) {
            jonesy.ldown = false;
            jonesy.down = false;
        }

        if (keyCode == 68) {
            jonesy.lright = false;
            jonesy.right = false;
        }

        if (keyCode == 65) {
            jonesy.lleft = false;
            jonesy.left = false;
        }

//        if(keyCode == 68 || keyCode == 65) {
//            santa.dx = 0;
//        }
//        if(keyCode == 87 || keyCode == 83) {
//            santa.dy=;
//        }
    }
}
