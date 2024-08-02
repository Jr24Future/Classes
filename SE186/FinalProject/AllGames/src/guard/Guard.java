package guard;

import sprites.MovingObject;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import static game.Commons.SQUARE_SIZE;

public class Guard {

    private List<Square> squares;

    public Guard(int x, int y) {
        squares=new ArrayList<>();
        for(int i=0; i<3; i++) {
            for (int j = 0; j < 5; j++) {
                squares.add(new Square(x + SQUARE_SIZE * j, y + SQUARE_SIZE * i));
            }
        }
    }

    public void collisionWith(MovingObject obj) {
        for(Square square : squares) {
            if(square.visible && square.intersects(obj.getBoundary())) {
                square.setVisible(false);
                obj.die();
            }
        }
    }

    public void draw(Graphics g) {
        for(Square square : squares) {
            if(square.visible) square.draw(g);
        }
    }

}
