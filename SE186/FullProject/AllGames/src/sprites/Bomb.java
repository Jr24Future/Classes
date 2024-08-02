package sprites;

import static game.Commons.*;

public class Bomb extends MovingObject {

    Bomb(int x, int y) {
        super(x, y);
        String s = this.getClass().getResource("/bomb.png").toString().substring(6);  //Make Sure to update this when importing assets
        loadImage(s);
        width=BOMB_WIDTH;
        height=BOMB_HEIGHT;
        dy=BOMB_SPEED;
    }

    @Override
    public void move() {
        if(y>GROUND-BOMB_HEIGHT)
            this.die();
        super.move();
    }
}
