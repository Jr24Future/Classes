package game;

import javax.swing.*;
import static game.Commons.BOARD_HEIGHT;
import static game.Commons.BOARD_WIDTH;

public class SpaceInvaders extends JFrame{

    public SpaceInvaders() {
        initUI();
    }

    private void initUI() {
        this.add(new Board());

        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        setResizable(false);

        setTitle("SpaceInvaders");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new SpaceInvaders();
    }
}
