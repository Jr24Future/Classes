package DaFroggerPackage;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class Truck extends Obby{
	public Truck(int FSM, int levelSpeed) {
		super(224*FSM, 144*FSM, 32*FSM, 16*FSM, FSM, -1, true, levelSpeed, 1);
		BufferedImage image = craftImage(FSM);
		
		super.setIcon(new ImageIcon(image));
		super.repaint();

		
	}
	private BufferedImage craftImage(int FSM) {
		BufferedImage imageToDraw1 = null;
		BufferedImage imageToDraw2 = null;
		BufferedImage image = null;
		imageToDraw1 = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		imageToDraw2 = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		image = new BufferedImage(32*FSM, 16*FSM, BufferedImage.TYPE_INT_ARGB);
		
		String s1 = this.getClass().getResource("/DaFroggerAssets/truckA_16x16.png").toString();  //Make Sure to update this when importing assets
		s1 = s1.substring(6);
		//System.out.println(s1);
		File f1 = null;
		try {
			f1 = new File(s1);
			imageToDraw1 = ImageIO.read(f1);
			//System.out.println("Complete");
		} catch (Exception e) {
			System.out.println("Failure: "+e);
		}
		
		String s2 = this.getClass().getResource("/DaFroggerAssets/truckB_16x16.png").toString();  //Make Sure to update this when importing assets
		s2 = s2.substring(6);
		//System.out.println(s2);
		File f2 = null;
		try {
			f2 = new File(s2);
			imageToDraw2 = ImageIO.read(f2);
			//System.out.println("Complete");
		} catch (Exception e) {
			System.out.println("Failure: "+e);
		}
		
		Graphics2D g2 = image.createGraphics();
		g2.drawImage(imageToDraw1, 0, 0, 16*FSM, 16*FSM, null);
		g2.drawImage(imageToDraw2, 16*FSM, 0, 16*FSM, 16*FSM, null);
		return image;
	}
}
