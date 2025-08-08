import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Flappybird extends JPanel implements ActionListener, KeyListener{
    int boardWidth = 360;
    int boardHeight = 640;

    //Images
    Image backgroundImg;
    Image birdImg;
    Image toppieImg;
    Image bottompieImg;

    //Bird
    int birdX = boardWidth/8;
    int bridY = boardHeight/2;
    int birdwidth = 34;
    int birdHeight = 24;

    class Bird{
        int x = birdX;
        int y = bridY;
        int width = birdwidth;
        int height = birdHeight;
        Image img;

        Bird(Image img){
            this.img = img;
        }
    }

    //pipe
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;


    class Pipe{
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;


        Pipe(Image img){
            this.img = img;
        }
    }

//game logic
    Bird bird;
    int velocityX = -4; // move pipes to the left speed
    int velocityY = 0; // move bird up / down speed
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();



    Timer gameloop;
    Timer placePipesTimer;
    boolean gameOver = false;
    double score = 0;

    Flappybird(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
       // setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);

        //load img
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        toppieImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottompieImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

         bird = new Bird(birdImg);
         pipes = new ArrayList<Pipe>();
         placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                placePipe();
            }
         });
         placePipesTimer.start();

         //game timer
         gameloop = new Timer(1000/60, this);
         gameloop.start();
    }


    public void placePipe(){
        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = boardHeight/4;

        Pipe topPipes =new Pipe(toppieImg);
        topPipes.y=randomPipeY;
        pipes.add(topPipes);

        Pipe bottomPipes =new Pipe(bottompieImg);
        bottomPipes.y = topPipes.y + pipeHeight +openingSpace;
        pipes.add(bottomPipes);

    }

    public void paintComponent ( Graphics g){
        super.paintComponent(g);
        draw(g);

    }


    public void draw(Graphics g){
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);
        g.drawImage(birdImg, bird.x, bird.y, bird.width, bird.height, null);

        for (int i=0 ; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img,pipe.x, pipe.y, pipe.width, pipe.height, null );

        }

        //score
        g.setColor(Color.white);
        g.setFont(new Font("Arial" , Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf((int) score), 10 ,35);
        }else{
            g.drawString(String.valueOf((int) score), 10, 35);
        }

    }


    public void move(){
        //bird
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0);

        //pipes
          for (int i=0 ; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                pipe.passed = true;
                score += 0.5; //0.5 beacuse there is two pipes (top one and bottom one) each 1 is 0.5 so the each set will be 1 point.
            }

            if (collision(bird, pipe)) {
                gameOver = true;
            }

          }  

          

          if (bird.y > boardHeight) {
                gameOver = true;
          }
    }

     public boolean collision (Bird a, Pipe b){
        return a.x <b.x + b.width && a.x + a.width > b.x && a.y< b.y + b.height && a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        move();
        repaint();
        if (gameOver) {
            placePipesTimer.stop();
            gameloop.stop();
        }
    }
   


    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -9;

            if (gameOver) {
                //restart the game 
                bird.y = bridY;
                velocityY =0;
                pipes.clear();
                score = 0;
                gameOver = false;
                gameloop.start();
                placePipesTimer.start();
            }
        }
       
    }

     @Override
    public void keyTyped(KeyEvent e) {}


    @Override
    public void keyReleased(KeyEvent e) {}
}
