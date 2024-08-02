import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Alien1 extends GameObj {
	 public static final String img_file = "alien1.png";
	 public static final int SIZE = 21;
	 public static final int INIT_VEL_Y = 0;
	 
	 private static BufferedImage img;
	 
	 public Alien1(int vel_x, int pos_x, int pos_y, int courtWidth, int courtHeight) {
		super(vel_x, INIT_VEL_Y, pos_x, pos_y, 
				SIZE, SIZE, courtWidth, courtHeight);
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
