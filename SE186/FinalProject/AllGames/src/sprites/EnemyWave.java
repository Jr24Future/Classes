package sprites;

import game.Board;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import static game.Commons.*;

public class EnemyWave {

    private List<Enemy> enemies; //work on 
    private List<Enemy2> enemies2;
    private List<Enemy3> enemies3;
    private Integer numberOfEnemies;
    private Integer score = 0;
    private int enemySpeed;

    public List<Enemy> getEnemies() {
        return enemies;
    }
    public List<Enemy2> getEnemies2() {
        return enemies2;
    }
    public List<Enemy3> getEnemies3() {
        return enemies3;
    }

    public Integer getNumberOfEnemies() {
        return numberOfEnemies;
    }
    public Integer score(){
        return score;
    }

    public void decreaseNumberOfEnemies() {
        numberOfEnemies--;
        score += 100;
    }

    public EnemyWave() {
        enemies = new ArrayList<>();
        enemies2 = new ArrayList<>();
        enemies3 = new ArrayList<>();
        for(int i=0; i<1; i++) {
            for (int j = 0; j < 8; j++) {
                enemies.add(new Enemy(ENEMY_X + 32 * j, ENEMY_Y + 32 * i));
            }
        }
        for(int i=1; i<3; i++){
            for(int j = 0; j < 8; j++){
                enemies2.add(new Enemy2(ENEMY_X + 32 * j, ENEMY_Y + 32 * i));            
            }
        }
        for(int i=3; i<5; i++){
            for(int j = 0; j < 8; j++){
                enemies3.add(new Enemy3(ENEMY_X + 32 * j, ENEMY_Y + 32 * i));            
            }
        }
        numberOfEnemies=40;
        enemySpeed=1;
    }

    public void draw(Graphics g, Board board) {
        for (Enemy enemy : enemies) {
            if (enemy.visible)
                enemy.draw(g, board);
            if (enemy.bomb.visible)
                enemy.bomb.draw(g, board);
        }
        for (Enemy2 enemy2 : enemies2) {
            if (enemy2.visible)
                enemy2.draw(g, board);
        }
        for (Enemy3 enemy3 : enemies3) {
            if (enemy3.visible)
                enemy3.draw(g, board);
        }
    }

    public boolean reachedTheGround() {
        for(Enemy enemy: enemies) {
            if (enemy.visible && enemy.y + enemy.height > GUARD_POSY) {
                return true;
            }
        }
        for(Enemy2 enemy2: enemies2) {
            if (enemy2.visible && enemy2.y + enemy2.height > GUARD_POSY) {
                return true;
            }
        }
        for(Enemy3 enemy3: enemies3) {
            if (enemy3.visible && enemy3.y + enemy3.height > GUARD_POSY) {
                return true;
            }
        }
        return false;
    }

    public void fixStatus() {
        for(Enemy enemy : enemies) {
            if(enemy.dying) {
                enemy.setAlmostDied(true);
                enemy.setDying(false);
            }
            else if(enemy.almostDied) {
                enemy.die();
                enemy.setAlmostDied(false);
            }
            else if(enemy.visible)
                enemy.move();
        }
        for(Enemy2 enemy2 : enemies2) {
            if(enemy2.dying) {
                enemy2.setAlmostDied(true);
                enemy2.setDying(false);
            }
            else if(enemy2.almostDied) {
                enemy2.die();
                enemy2.setAlmostDied(false);
            }
            else if(enemy2.visible)
                enemy2.move();
        }
        for(Enemy3 enemy3 : enemies3) {
            if(enemy3.dying) {
                enemy3.setAlmostDied(true);
                enemy3.setDying(false);
            }
            else if(enemy3.almostDied) {
                enemy3.die();
                enemy3.setAlmostDied(false);
            }
            else if(enemy3.visible)
                enemy3.move();
        }
    }

    public void bombMove() {
        for(Enemy enemy: enemies) {
            if(enemy.bomb.visible) {
                enemy.bomb.move();
            }
        }
    }

    public void shooting() {
        for(Enemy enemy: enemies) {
            enemy.tryToShoot();
        }
    }

    public void accelerateIfNeeded() {
        boolean b=false;

        if(numberOfEnemies==20) {
            enemySpeed=2;
            b = true;
        }

        if(numberOfEnemies==12) {
            enemySpeed=3;
            b = true;
        }

        if(b) {
            for (Enemy enemy : enemies) {
                if (enemy.dx > 0) enemy.dx = enemySpeed;
                else enemy.dx = -enemySpeed;
            }
            for (Enemy2 enemy2 : enemies2) {
                if (enemy2.dx > 0) enemy2.dx = enemySpeed;
                else enemy2.dx = -enemySpeed;
            }
            for (Enemy3 enemy3 : enemies3) {
                if (enemy3.dx > 0) enemy3.dx = enemySpeed;
                else enemy3.dx = -enemySpeed;
            }
        }
    }

    public void turnAroundIfHitTheWall() {
        for(Enemy enemy : enemies) {
            for(Enemy2 enemy2 : enemies2){
                for(Enemy3 enemy3 : enemies3){
            
                    int boundary_x=0;
                    int boundary_y=0;
                    if (enemy.x>enemy2.x){
                        if (enemy.x>enemy3.x)
                        boundary_x = enemy.x;
                    }
                    if (enemy2.x>enemy.x){
                        if(enemy2.x>enemy3.x)
                        boundary_x = enemy2.x;
                    }
                    if (enemy3.x>enemy.x){
                        if (enemy3.x>enemy2.x)
                        boundary_x = enemy3.x;
                    }
                    if(boundary_x>BOARD_WIDTH-ENEMY_WIDTH) {
                        for(Enemy enemyReversed : enemies) {
                            enemyReversed.dx = -enemySpeed;
                            enemyReversed.y += 15;
                        }
                        
                        for(Enemy2 enemyReversed : enemies2) {
                            enemyReversed.dx = -enemySpeed;
                            enemyReversed.y += 15;
                        }
                        
                        for(Enemy3 enemyReversed : enemies3) {
                            enemyReversed.dx = -enemySpeed;
                            enemyReversed.y += 15;
                        }
                        return;
                    }

                    if (enemy.x<enemy2.x){
                        if (enemy.x<enemy3.x)
                        boundary_y = enemy.x;
                    }
                    if (enemy2.x<enemy.x){
                        if(enemy2.x<enemy3.x)
                        boundary_y = enemy2.x;
                    }
                    if (enemy3.x<enemy.x){
                        if (enemy3.x<enemy2.x)
                        boundary_y = enemy3.x;
                    }
                    if(boundary_y<0) {
                        for(Enemy enemyReversed : enemies) {
                            enemyReversed.dx = enemySpeed;
                            enemyReversed.y += 15;
                        }
                        
                        for(Enemy2 enemyReversed : enemies2) {
                            enemyReversed.dx = enemySpeed;
                            enemyReversed.y += 15;
                        }
                        
                        for(Enemy3 enemyReversed : enemies3) {
                            enemyReversed.dx = enemySpeed;
                            enemyReversed.y += 15;
                        }
                        return;
                    }
                } 
            }
        }
        
    }
}
