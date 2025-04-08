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
    public Character krampus;
    public Character[] manyKrampus;

    Image backgroundPic;

    //Sets the width and height of the program window
    final int WIDTH = 1000;
    final int HEIGHT = 700;

    //Declare the variables needed for the graphics
    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;

    public BufferStrategy bufferStrategy;

    // Main method definition
    // This is the code that runs first and automatically
    public static void main(String[] args) {
        BasicGameApp ex = new BasicGameApp();   //creates a new instance of the game
        new Thread(ex).start();                 //creates a threads & starts up the code in the run( ) method
    }


    // This section is the setup portion of the program
    // Initialize your variables and construct your program objects here.
    public BasicGameApp() { // BasicGameApp constructor

        setUpGraphics();

        /***
         * Step 2: addKeyListener(this)
         */

        canvas.addKeyListener(this);
        backgroundPic = Toolkit.getDefaultToolkit().getImage("krampusbg.jpg");

        //variable and objects
        //create (construct) the objects needed for the game
        jonesy = new Character(100,100,0,0,100,100);
        jonesy.name = "Jonesy";
        jonesy.pic = Toolkit.getDefaultToolkit().getImage("jonesy.png");


        manyKrampus = new Character[5];
        for (int i = 0; i< manyKrampus.length; i++) {
            manyKrampus[i] = new Character((int)(990* Math.random() + 1), (int)(690 * Math.random() + 1), 5, 5, 5, 5);
        }

        krampus = new Character(200, 400,0,0,100,100);
        krampus.name = "krampus";
        krampus.pic = Toolkit.getDefaultToolkit().getImage("krampus.png");

        for (int i=0; i< manyKrampus.length; i++) {
            manyKrampus[i].pic = Toolkit.getDefaultToolkit().getImage("krampus.png");
        }

    } // end BasicGameApp constructor


//*******************************************************************************
//User Method Section
//
// put your code to do things here.

    // main thread
    // this is the code that plays the game after you set things up
    public void run() {
        //for the moment we will loop things forever.
        while (true) {
            checkKeys();
            moveThings();  //move all the game objects
            collide();
            render();  // paint the graphics
            pause(10); // sleep for 10 ms
        }
    }

    public void checkKeys() {
        if(jonesy.up == true) {
            jonesy.dy = -5;
        }
        else if(jonesy.down == true) {
            jonesy.dy = 5;
        }
        else {
            jonesy.dy = 0;
        }

        if(jonesy.right == true) {
            jonesy.dx = 5;
        }
        else if(jonesy.left == true) {
            jonesy.dx = -5;
        }
        else {
            jonesy.dx = 0;
        }

    }

    public void moveThings() {
        krampus.wrap();
        krampus.printInfo();
        for (int i=0; i< manyKrampus.length; i++) {
            manyKrampus[i].move();
        }

        jonesy.wrap();
        jonesy.printInfo();

        if(jonesy.left == true) {
            jonesy.pic = Toolkit.getDefaultToolkit().getImage("jonesy1.png");
        } else {
            jonesy.pic = Toolkit.getDefaultToolkit().getImage("jonesy.png");
        }
        //call the move() code for each object
    }

    public void collide() {
        if (jonesy.hitbox.intersects(krampus.hitbox)) {
            jonesy.dx = -jonesy.dx;
            jonesy.dy = -jonesy.dy;
            krampus.dx = -krampus.dx;
            krampus.dy = -krampus.dy;
        }
    }

    //Paints things on the screen using bufferStrategy
    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);

        //draw the images
        Color myColor = new Color(3, 120, 180);
        g.setColor(myColor);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.drawImage(backgroundPic, 0, 0, WIDTH, HEIGHT, null);
        g.drawImage(krampus.pic, krampus.xpos, krampus.ypos, krampus.width, krampus.height, null);
        g.drawImage(jonesy.pic, jonesy.xpos, jonesy.ypos, jonesy.width, jonesy.height, null);
        g.setColor(Color.MAGENTA);
        g.setColor(Color.MAGENTA);
        g.drawRect(krampus.hitbox.x, krampus.hitbox.y, krampus.hitbox.width, krampus.hitbox.height);


        g.dispose();
        bufferStrategy.show();
    }

    //Pauses or sleeps the computer for the amount specified in milliseconds
    public void pause(int time ) {
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

        if (keyCode == 87) {
            jonesy.up = true;
        }

        if (keyCode == 83) {
            jonesy.down = true;

        }

        if (keyCode == 68) {
            jonesy.right = true;
        }

        if (keyCode == 65){
            jonesy.left = true;
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
            jonesy.up = false;
        }

        if (keyCode == 83) {
            jonesy.down = false;
        }

        if (keyCode == 68) {
            jonesy.right = false;
        }

        if (keyCode == 65) {
            jonesy.left = false;
        }

//        if(keyCode == 68 || keyCode == 65) {
//            santa.dx = 0;
//        }
//        if(keyCode == 87 || keyCode == 83) {
//            santa.dy=0;
//        }
    }
}
