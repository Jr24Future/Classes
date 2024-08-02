package DaFroggerPackage;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class ScoreLabel extends JLabel{
	private int FSM;
	private BufferedImage imageOne;
	private BufferedImage imageTwo;
	private BufferedImage imageThree;
	private BufferedImage imageFour;
	private BufferedImage imageFive;
	private BufferedImage imageSix;
	private BufferedImage imageSeven;
	private BufferedImage imageEight;
	private BufferedImage imageNine;
	private BufferedImage imageZero;
	public ScoreLabel(int FSM, int score) {
		super();
		this.FSM = FSM;
		setBounds(24*FSM, 8*FSM, 40*FSM, 8*FSM);
		makeImages();
		updateScore(score);
	}
	public void updateScore(int score) {
		int firstDigit;
		int secondDigit;
		int thirdDigit;
		int fourthDigit;
		int fifthDigit;
		
		score %= 100000;
		firstDigit = score/10000;
		score %= 10000;
		secondDigit = score/1000;
		score %= 1000;
		thirdDigit = score/100;
		score %= 100;
		fourthDigit = score/10;
		score %= 10;
		fifthDigit = score;
		
		
		
		BufferedImage image = null;
		image = new BufferedImage(40*FSM, 8*FSM, BufferedImage.TYPE_INT_ARGB);
		
		BufferedImage firstImageToDraw = null;
		BufferedImage secondImageToDraw = null;
		BufferedImage thirdImageToDraw = null;
		BufferedImage fourthImageToDraw = null;
		BufferedImage fifthImageToDraw = null;
		
		firstImageToDraw = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);
		secondImageToDraw = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);
		thirdImageToDraw = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);
		fourthImageToDraw = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);
		fifthImageToDraw = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);
		
		firstImageToDraw = pickImage(firstDigit);
		secondImageToDraw = pickImage(secondDigit);
		thirdImageToDraw = pickImage(thirdDigit);
		fourthImageToDraw = pickImage(fourthDigit);
		fifthImageToDraw = pickImage(fifthDigit);
		
		Graphics2D g2 = image.createGraphics();
		g2.drawImage(firstImageToDraw, 0, 0, 8*FSM, 8*FSM, null);
		g2.drawImage(secondImageToDraw, 8*FSM, 0, 8*FSM, 8*FSM, null);
		g2.drawImage(thirdImageToDraw, 16*FSM, 0, 8*FSM, 8*FSM, null);
		g2.drawImage(fourthImageToDraw, 24*FSM, 0, 8*FSM, 8*FSM, null);
		g2.drawImage(fifthImageToDraw, 32*FSM, 0, 8*FSM, 8*FSM, null);
		
		
		super.setIcon(new ImageIcon(image));
		super.repaint();
	}
	
	
	
	
	
	
	private BufferedImage pickImage(int num) {
		BufferedImage imageToReturn = null;
		//imageToReturn = new BufferedImage(8, 8, BufferedImage.TYPE_INT_ARGB);		
		switch (num) {
		case 1: { 
			imageToReturn = imageOne;
			break;
		}
		case 2: { 
			imageToReturn = imageTwo;
			break;
		}
		case 3: { 
			imageToReturn = imageThree;
			break;
		}
		case 4: { 
			imageToReturn = imageFour;
			break;
		}
		case 5: { 
			imageToReturn = imageFive;
			break;
		}
		case 6: { 
			imageToReturn = imageSix;
			break;
		}
		case 7: { 
			imageToReturn = imageSeven;
			break;
		}
		case 8: { 
			imageToReturn = imageEight;
			break;
		}
		case 9: { 
			imageToReturn = imageNine;
			break;
		}
		case 0: { 
			imageToReturn = imageZero;
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + num);
		}
		return imageToReturn;
	}
	
	
	private void makeImages() {
		String s = "";
		s = this.getClass().getResource("/DaFroggerAssets/one_8x8.png").toString();
		s = s.substring(6);
		//System.out.println(s);
		File f = null;
		try {
			f = new File(s);
			imageOne = ImageIO.read(f);
			//System.out.println("Complete");
		} catch (Exception e) {
			System.out.println("Failure: "+e);
		}
		
		
		s = this.getClass().getResource("/DaFroggerAssets/two_8x8.png").toString();
		s = s.substring(6);
		//System.out.println(s);
		//File f = null;
		try {
			f = new File(s);
			imageTwo = ImageIO.read(f);
			//System.out.println("Complete");
		} catch (Exception e) {
			System.out.println("Failure: "+e);
		}
		
		
		s = this.getClass().getResource("/DaFroggerAssets/three_8x8.png").toString();
		s = s.substring(6);
		//System.out.println(s);
		//File f = null;
		try {
			f = new File(s);
			imageThree = ImageIO.read(f);
			//System.out.println("Complete");
		} catch (Exception e) {
			System.out.println("Failure: "+e);
		}
		
		
		s = this.getClass().getResource("/DaFroggerAssets/four_8x8.png").toString();
		s = s.substring(6);
		//System.out.println(s);
		//File f = null;
		try {
			f = new File(s);
			imageFour = ImageIO.read(f);
			//System.out.println("Complete");
		} catch (Exception e) {
			System.out.println("Failure: "+e);
		}
		
		
		s = this.getClass().getResource("/DaFroggerAssets/five_8x8.png").toString();
		s = s.substring(6);
		//System.out.println(s);
		//File f = null;
		try {
			f = new File(s);
			imageFive = ImageIO.read(f);
			//System.out.println("Complete");
		} catch (Exception e) {
			System.out.println("Failure: "+e);
		}
		
		
		s = this.getClass().getResource("/DaFroggerAssets/six_8x8.png").toString();
		s = s.substring(6);
		//System.out.println(s);
		//File f = null;
		try {
			f = new File(s);
			imageSix = ImageIO.read(f);
			//System.out.println("Complete");
		} catch (Exception e) {
			System.out.println("Failure: "+e);
		}
		
		
		s = this.getClass().getResource("/DaFroggerAssets/seven_8x8.png").toString();
		s = s.substring(6);
		//System.out.println(s);
		//File f = null;
		try {
			f = new File(s);
			imageSeven = ImageIO.read(f);
			//System.out.println("Complete");
		} catch (Exception e) {
			System.out.println("Failure: "+e);
		}
		
		
		s = this.getClass().getResource("/DaFroggerAssets/eight_8x8.png").toString();
		s = s.substring(6);
		//System.out.println(s);
		//File f = null;
		try {
			f = new File(s);
			imageEight = ImageIO.read(f);
			//System.out.println("Complete");
		} catch (Exception e) {
			System.out.println("Failure: "+e);
		}
		
		
		s = this.getClass().getResource("/DaFroggerAssets/nine_8x8.png").toString();
		s = s.substring(6);
		//System.out.println(s);
		//File f = null;
		try {
			f = new File(s);
			imageNine = ImageIO.read(f);
			//System.out.println("Complete");
		} catch (Exception e) {
			System.out.println("Failure: "+e);
		}
		
		
		s = this.getClass().getResource("/DaFroggerAssets/zero_8x8.png").toString();
		s = s.substring(6);
		//System.out.println(s);
		//File f = null;
		try {
			f = new File(s);
			imageZero = ImageIO.read(f);
			//System.out.println("Complete");
		} catch (Exception e) {
			System.out.println("Failure: "+e);
		}
	}
}
