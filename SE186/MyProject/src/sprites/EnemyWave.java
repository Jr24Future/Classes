package sprites;

import game.Board;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import static game.Commons.*;

public class EnemyWave {

    private List<Enemy> enemies;
    private Integer numberOfEnemies;
    private int enemySpeed;

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public Integer getNumberOfEnemies() {
        return numberOfEnemies;
    }

    public void decreaseNumberOfEnemies() {
        numberOfEnemies--;
    }

    public EnemyWave() {
        enemies = new ArrayList<>();
        for(int i=0; i<4; i++) {
            for (int j = 0; j < 8; j++) {
                enemies.add(new Enemy(ENEMY_X + 32 * j, ENEMY_Y + 32 * i));
            }
        }
        numberOfEnemies=32;
        enemySpeed=1;
    }

    public void draw(Graphics g, Board board) {
        for (Enemy enemy : enemies) {
            if (enemy.visible)
                enemy.draw(g, board);
            if (enemy.bomb.visible)
                enemy.bomb.draw(g, board);
        }
    }

    public boolean reachedTheGround() {
        for(Enemy enemy: enemies) {
            if (enemy.visible && enemy.y + enemy.height > GUARD_POSY) {
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

        if(numberOfEnemies==16) {
            enemySpeed = 2;
            b = true;
        }

        if(numberOfEnemies==8) {
            enemySpeed = 3;
            b = true;
        }

        if(b) {
            for (Enemy enemy : enemies) {
                if (enemy.dx > 0) enemy.dx = enemySpeed;
                else enemy.dx = -enemySpeed;
            }
        }
    }

    public void turnAroundIfHitTheWall() {
        for(Enemy enemy: enemies) {
            if(enemy.x>BOARD_WIDTH-ENEMY_WIDTH) {
                for(Enemy enemyReversed : enemies) {
                    enemyReversed.dx = -enemySpeed;
                    enemyReversed.y += 15;
                }
                return;
            }

            if(enemy.x<0) {
                for(Enemy enemyReversed : enemies) {
                    enemyReversed.dx = enemySpeed;
                    enemyReversed.y += 15;
                }
                return;
            }
        }
    }
}
