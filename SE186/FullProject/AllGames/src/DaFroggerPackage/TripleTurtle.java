package DaFroggerPackage;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class TripleTurtle extends Obby{
	private Boolean flipper;
	private int animationState;
	public TripleTurtle(int FSM, int levelSpeed, Boolean flipper) {
		super(224*FSM, 112*FSM, 48*FSM, 16*FSM, FSM, -1, false, levelSpeed, 2);
		this.flipper = flipper;
		animationState = (this.flipper) ? 1:0;
		BufferedImage image = craftImage(FSM);
		
		super.setIcon(new ImageIcon(image));
		super.repaint();

		
	}
	private BufferedImage craftImage(int FSM) {
		BufferedImage imageToDraw = null;
		BufferedImage image = null;
		imageToDraw = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		image = new BufferedImage(48*FSM, 16*FSM, BufferedImage.TYPE_INT_ARGB);
		String s = "";
		if (animationState == 1 || animationState == 0) {
			s = this.getClass().getResource("/DaFroggerAssets/turtle1_16x16.png").toString();  //Make Sure to update this when importing assets

		}
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
		g2.drawImage(imageToDraw, 0, 0, 16*FSM, 16*FSM, null);
		g2.drawImage(imageToDraw, 16*FSM, 0, 16*FSM, 16*FSM, null);
		g2.drawImage(imageToDraw, 32*FSM, 0, 16*FSM, 16*FSM, null);
		return image;
		
	}
}
