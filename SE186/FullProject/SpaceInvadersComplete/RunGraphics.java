
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.TextField;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.*;
import javax.swing.Timer;
import java.awt.event.*;

public class RunGraphics {

    private JFrame frame;
    int fW = 600;
    int fH = 600;

    // The method to set up
    public RunGraphics() {

        frame = new JFrame("Cool Game");
        frame.setSize(fW, fH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // the event that triggers the end of the program
        frame.setPreferredSize(frame.getSize());
        frame.add(new showGraphics(frame.getSize())); // Setting up the DrawBars public class function (getting bars and
        // putting it in this frame)
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);

    }

    // The main method
    public static void main(String... argv) {
        new RunGraphics();

    }

    public static class showGraphics extends JPanel implements Runnable, MouseListener, KeyListener {

        private Thread animator;

        int xAxis = 30;
        int yAxis = 30;
        Ship s;
        Alien[][] a = new Alien[3][10];
        Shot sh;

        public showGraphics(Dimension dimension) {
            s = new Ship(200,500,57,35,5,"player.png");
            sh = new Shot(200,500,5,20,15,"shot.png");
            int x = 10;
            int y = 10;
            for(int r = 0; r<a.length; r++){
                for (int c = 0; c<a[0].length; c++){
                    a[r][c] = new Alien(x,y,30,20,5,"alien.png");
                    x += 35;
                }
                x=10;
                y += 25;
            }

            setSize(dimension);
            setPreferredSize(dimension);
            addMouseListener(this);
            addKeyListener ( this ) ; 
            //addActionListener(this);
            setFocusable(true);
            if (animator == null) {
                animator = new Thread(this);
                animator.start();
            }

            setDoubleBuffered(true);

        }

        @Override

        public void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g;// g2 is the graphics object that we need to use

            // to draw things to the screen
            Dimension d = getSize();

            // create a background
            g2.setColor(Color.white);
            g2.fillRect(0, 0, d.width, d.height);

            moveAlien();
            s.move(0);
            sh.move(0);

            sh.draw(g2);
            s.draw(g2);
            hitDetect();

            for(int r = 0; r<a.length; r++){
                for (int c = 0; c<a[0].length; c++){
                    if(a[r][c].isVis)
                        a[r][c].draw(g2);
                }
            }

        } // end of paintcomponent

        public void hitDetect(){

            for(int r = 0; r<a.length; r++){
                for (int c = 0; c<a[0].length; c++){

                    // if ( a[r][c].isVisible() ) {//&& shot.isVisible()
                    if (a[r][c].isVis == true && sh.getX() + sh.getWidth() >= a[r][c].getX() && 
                    sh.getX() <= a[r][c].getX() + a[r][c].getWidth() && 
                    sh.getY() + sh.getHeight() >= (a[r][c].getY()) && 
                    sh.getY() <= a[r][c].getY() + a[r][c].getHeight()) {

                            a[r][c].isVis=false; 
                            sh.x = -30;
                    }
                    //}

                }}

        }

        public void moveAlien(){
            for(int r = 0; r<a.length; r++){
                for (int c = 0; c<a[0].length; c++){
                    if(a[r][c].moveLeft)
                        a[r][c].setX(a[r][c].getX()-a[r][c].getSpeed());

                    if(a[r][c].moveRight){
                        a[r][c].setX(a[r][c].getX()+a[r][c].getSpeed());
                    }
                }}
            //check if we need to switch directions
            for(int r = 0; r<a.length; r++){
                for (int c = 0; c<a[0].length; c++){

                    if(a[r][c].getX()>600){
                        moveLeftRight(1);
                        break;
                    }

                    if(a[r][c].getX()<0){
                        moveLeftRight(2);
                        break;
                    }
                }}

        }

        public void moveLeftRight(int d){
            for(int r = 0; r<a.length; r++){
                for (int c = 0; c<a[0].length; c++){
                    if(d==1){
                        a[r][c].moveLeft=true;
                        a[r][c].moveRight=false;
                    }else{
                        a[r][c].moveLeft=false;
                        a[r][c].moveRight=true;
                    }

                    a[r][c].setY(a[r][c].getY()+10);

                }}
        }

        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void keyTyped ( KeyEvent e ){  

        }  

        public void keyPressed ( KeyEvent e){  
            // System.out.println("Key: " + e.getKeyCode());
            int k = e.getKeyCode();
            s.setLeftRight(k);
            if(k==32)  {
                sh.goUp=true;
                sh.setX(s.getX() + (s.getWidth()/2));
                sh.setY(s.getY() );
            }
        }  

        public void keyReleased ( KeyEvent e ){  
            int k = e.getKeyCode();
            s.stop();

        }  
        public void run() {
            long beforeTime, timeDiff, sleep;
            beforeTime = System.currentTimeMillis();
            int animationDelay = 37;
            long time = System.currentTimeMillis();
            while (true) {// infinite loop
                // spriteManager.update();
                repaint();
                try {
                    time += animationDelay;
                    Thread.sleep(Math.max(0, time - System.currentTimeMillis()));
                } catch (InterruptedException e) {
                    System.out.println(e);
                } // end catch
            } // end while loop
        }// end of run

    }

}