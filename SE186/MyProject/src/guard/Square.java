package guard;

import java.awt.*;
import static game.Commons.SQUARE_SIZE;

class Square extends Rectangle  {

    boolean visible;

    Square(int x, int y) {
        super(x, y, SQUARE_SIZE, SQUARE_SIZE);
        setVisible(true);
    }

    void setVisible(boolean visible) {
        this.visible = visible;
    }

    void draw(Graphics g) {
        g.setColor(new Color(241, 59, 53));
        g.fillRect(x, y, width, height);
    }
}
