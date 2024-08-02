package game;

import guard.*;
import sprites.Enemy;
import sprites.EnemyWave;
import sprites.Player;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import static game.Commons.*;

public class Board extends JPanel implements Runnable {

    private Player player;
    private EnemyWave enemyWave;
    private List<Guard> guards;

    private boolean inGame;
    private Integer lives;
    private String message;     //message for the end of a game

    Board() {

        inGame=true;
        lives=3;

        player=new Player(START_X, START_Y);
        enemyWave = new EnemyWave();

        guards = new ArrayList<>();
        for(int i=0; i<4 ; i++) {
            guards.add(new Guard(GUARD_POSX + i * 125, GUARD_POSY));
        }

        addKeyListener(new KAdapter());     //for Key events
        setFocusable(true);
        setBackground(Color.BLACK);
    }

    @Override
    public void addNotify() {
        super.addNotify();

        Thread animator = new Thread(this);
        animator.start();
    }

    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while(inGame) {
            repaint();
            animationCycle();       //mechanics of a game

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if(sleep<0) {
                sleep = 2;
            }
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            beforeTime=System.currentTimeMillis();
        }
        gameOver();
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        Font font = new Font("Helvetica", Font.PLAIN, 15);
        g.setColor(Color.WHITE);
        g.setFont(font);

        g.drawString("Lives: " + lives.toString(), BOARD_WIDTH - 90, 20);
        g.drawString("Enemies Left: " + enemyWave.getNumberOfEnemies().toString(), 28, 20);

        g.setColor(Color.GREEN);
        g.drawLine(0, GROUND, BOARD_WIDTH, GROUND);

        player.draw(g, this);
        if (player.getM().isVisible())
            player.getM().draw(g, this);

        enemyWave.draw(g, this);

        for (Guard guard : guards) {
            guard.draw(g);
        }
    }

    private void animationCycle() {
        if(enemyWave.getNumberOfEnemies()==0) {
            inGame=false;
            message = "You Won! Congrats!";
        }

        if(player.isDying()) {
            lives--;
            if(lives!=0) player.revive();
            else {
                inGame=false;
                message = "Game Over!";
            }
        }

        if(enemyWave.reachedTheGround()) {
            inGame=false;
            message="Game Over!";
        }

        player.move();
        player.missleMove();
        enemyWaveMove();
        collisionMissileEnemies();
        collisionBombPlayer();
        collisionWithGuards();
    }


    private void enemyWaveMove() {
        enemyWave.fixStatus();
        enemyWave.bombMove();
        enemyWave.shooting();
        enemyWave.accelerateIfNeeded();
        enemyWave.turnAroundIfHitTheWall();
    }

    private void collisionMissileEnemies() {
        if(player.getM().isVisible()) {
            for (Enemy enemy : enemyWave.getEnemies())
                if(enemy.isVisible() && player.getM().collisionWith(enemy)) {
                    enemy.explosion();
                    enemyWave.decreaseNumberOfEnemies();
                    player.getM().die();
                }
        }
    }

    private void collisionBombPlayer() {
        for(Enemy enemy : enemyWave.getEnemies()) {
            if (enemy.getBomb().isVisible() && enemy.getBomb().collisionWith(player)) {
                player.explosion();
                enemy.getBomb().die();
            }
        }
    }

    private void collisionWithGuards() {
        for(Guard guard : guards) {
            guard.collisionWith(player.getM());
            for (Enemy enemy : enemyWave.getEnemies()) {
                guard.collisionWith(enemy.getBomb());
            }
        }
    }

    private void gameOver() {
        Graphics g = this.getGraphics();
        super.paintComponent(g);

        Font font = new Font("Helvetica", Font.BOLD, 18);
        FontMetrics ft = this.getFontMetrics(font);

        g.setColor(Color.WHITE);
        g.setFont(font);
        g.drawString(message, (BOARD_WIDTH-ft.stringWidth(message))/2, BOARD_HEIGHT/2);
    }

    private class KAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

    }
}
