package sprites;

import static game.Commons.ENEMY_HEIGHT;
import static game.Commons.ENEMY_WIDTH;

public class Enemy2 extends MovingObject{
    boolean almostDied;     //for proper projection of explosion

    Enemy2(int x, int y) {
        super(x, y);
        loadImage("alien2.png");
        width=ENEMY_WIDTH;
        height=ENEMY_HEIGHT;
        dx=1;
        almostDied=false;
        
    }


    void setAlmostDied(boolean almostDied) {
        this.almostDied = almostDied;
    }

    @Override
    public void move() {
        super.move();
    }
}
