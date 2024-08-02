package sprites;

import static game.Commons.MISSILE_HEIGHT;
import static game.Commons.MISSILE_SPEED;
import static game.Commons.MISSILE_WIDTH;

public class Missile extends MovingObject {

    Missile(int x, int y) {
        super(x, y);
        String s = this.getClass().getResource("/missile.png").toString().substring(6);  //Make Sure to update this when importing assets
        loadImage(s);
        width=MISSILE_WIDTH;
        height=MISSILE_HEIGHT;
        dy=-MISSILE_SPEED;
    }

    @Override
    public void move() {
        if(y<=0)
            this.die();
        super.move();
    }

}
