package DaFroggerPackage;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class LifeLabel extends JLabel{
	public LifeLabel(int FSM) {
		super();
		BufferedImage imageToDraw = null;
		BufferedImage image = null;
		imageToDraw = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);
		image = new BufferedImage(8*FSM, 8*FSM, BufferedImage.TYPE_INT_ARGB);
		String s = this.getClass().getResource("/DaFroggerAssets/life_8x8.png").toString();  //Make Sure to update this when importing assets
		s = s.substring(6);
		//System.out.println(s);
		File f = null;
		try {
			f = new File(s);
			imageToDraw = ImageIO.read(f);
			//System.out.println("Complete");
		} catch (Exception e) {
			System.out.println("Failure: "+e);
		}
		Graphics2D g2 = image.createGraphics();
		g2.drawImage(imageToDraw, 0, 0, 8*FSM, 8*FSM, null);
		super.setIcon(new ImageIcon(image));
	}
}
