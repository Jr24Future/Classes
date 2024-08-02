package DaFroggerPackage;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class Frog extends JLabel{
	private int FSM;

	public Frog(int frameSizeMultiplyer) {
		super();
		FSM = frameSizeMultiplyer;
		super.setBounds(FSM*104, FSM*224, FSM*16, FSM*16);
		BufferedImage image = craftImage(FSM, 'n');
		super.setIcon(new ImageIcon(image));
		super.repaint();
		
		
	}
	public void move(char direction) {
		super.setIcon(new ImageIcon(craftImage(FSM, direction)));
		int oldx = super.getX();
		int oldy = super.getY();
		if (direction == 'n') {
			super.setBounds(oldx, oldy-(FSM*16), FSM*16, FSM*16);
		}
		else if (direction == 'e') {
			super.setBounds(oldx+(FSM*16), oldy, FSM*16, FSM*16);
		}
		if (direction == 's') {
			super.setBounds(oldx, oldy+(FSM*16), FSM*16, FSM*16);
		}
		if (direction == 'w') {
			super.setBounds(oldx-(FSM*16), oldy, FSM*16, FSM*16);
		}
		//System.out.println("Frog moved from "+oldx+", "+oldy+" to "+super.getX()+", "+super.getY());
		super.repaint();
	}
	private BufferedImage craftImage(int FSM, char direction) {
		BufferedImage imageToDraw = null;
		BufferedImage image = null;
		imageToDraw = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		image = new BufferedImage(16*FSM, 16*FSM, BufferedImage.TYPE_INT_ARGB);
		String s = "";
		switch (direction) {
		case 'n': {
			s = this.getClass().getResource("/DaFroggerAssets/frogUp1_16x16.png").toString();
			break;
		}
		case 'e': {
			s = this.getClass().getResource("/DaFroggerAssets/frogRight1_16x16.png").toString();
			break;
		}
		case 's': {
			s = this.getClass().getResource("/DaFroggerAssets/frogDown1_16x16.png").toString();
			break;
		}
		case 'w': {
			s = this.getClass().getResource("/DaFroggerAssets/frogLeft1_16x16.png").toString();
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + direction);
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
		return image;
	}
}
