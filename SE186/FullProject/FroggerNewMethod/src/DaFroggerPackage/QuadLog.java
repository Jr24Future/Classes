package DaFroggerPackage;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class QuadLog extends Obby{
	public QuadLog(int FSM, int levelSpeed) {
		super(-64*FSM, 48*FSM, 64*FSM, 16*FSM, FSM, 1, false, levelSpeed, 1);
		BufferedImage image = craftImage(FSM);
		
		super.setIcon(new ImageIcon(image));
		super.repaint();

		
	}
	private BufferedImage craftImage(int FSM) {
		BufferedImage imageToDraw = null;
		BufferedImage image = null;
		imageToDraw = new BufferedImage(32, 16, BufferedImage.TYPE_INT_ARGB);
		image = new BufferedImage(64*FSM, 16*FSM, BufferedImage.TYPE_INT_ARGB);
		String s = this.getClass().getResource("/DaFroggerAssets/log_16x16.png").toString();  //Make Sure to update this when importing assets
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
		g2.drawImage(imageToDraw, 0, 0, 32*FSM, 16*FSM, null);
		g2.drawImage(imageToDraw, 16*FSM, 0, 32*FSM, 16*FSM, null);
		g2.drawImage(imageToDraw, 32*FSM, 0, 32*FSM, 16*FSM, null);
		return image;
	}
}
