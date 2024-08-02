import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Alien2 extends GameObj {
	 public static final String img_file = "alien2.png";
	 public static final int WIDTH = 29;
	 public static final int HEIGHT = 22;
	 public static final int INIT_VEL_Y = 0;
	 
	 private static BufferedImage img;
	 
	 public Alien2(int vel_x, int pos_x, int pos_y, int courtWidth, int courtHeight) {
			super(vel_x, INIT_VEL_Y, pos_x, pos_y, 
				WIDTH, HEIGHT, courtWidth, courtHeight);
		try {
			if (img == null) {
				img = ImageIO.read(new File(img_file));
			}
		} catch (IOException e) {
			System.out.println("Internal Error:" + e.getMessage());
		}
	}

	@Override
    public void move() {
        super.move();
    }

}
