package DaFroggerPackage;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class EndZone extends JLabel{
	private int FSM;
	private Boolean filled;
	public EndZone(int FSM, Boolean filled) {
		super();
		this.FSM = FSM;
		this.filled = filled;
		
		if (filled) {
			super.setIcon(new ImageIcon(craftImage()));
			this.repaint();
		}
		else {
			/*
			BufferedImage image = new BufferedImage(16*FSM, 16*FSM, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = image.createGraphics();
			g2.setColor(Color.pink);
			g2.fillRect(0, 0, 16*FSM, 16*FSM);
			
			
			this.setIcon(new ImageIcon(image));
			*/
		}
		
	}
	public Boolean getFilled() {
		return filled;
	}
	public void fill() {
		filled = true;
	}
	public void unFill() {
		filled = false;
	}
	public BufferedImage craftImage() {
		BufferedImage imageToDraw = null;
		BufferedImage image = null;
		imageToDraw = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		image = new BufferedImage(16*FSM, 16*FSM, BufferedImage.TYPE_INT_ARGB);
		String s = "";
		s = this.getClass().getResource("/DaFroggerAssets/endFrog1_16x16.png").toString();  //Make Sure to update this when importing assets
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
