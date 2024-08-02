package DaFroggerPackage;

import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class Obby extends JLabel {
	private int FSM;
	private int x;
	private int y;
	private int width;
	private int height;
	private int ticksTillMove;
	private int DEFAULTTICKSTILLMOVE = 50;
	private int speed;
	private int speedMultiplyer;
	private Boolean isVehicle;
	private Boolean movedThisTick;
	
	private int direction; //always -1 or 1
	public Obby(int x, int y, int width, int height, int frameSizeMultiplyer, int direction, Boolean isVehicle, int levelSpeed, int naturalSpeed) {
		super(new ImageIcon());
		FSM = frameSizeMultiplyer;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		speed = levelSpeed;
		speedMultiplyer = naturalSpeed;
		ticksTillMove = DEFAULTTICKSTILLMOVE;
		this.isVehicle = isVehicle;
		this.direction = direction;
		super.setBounds(x, y, width, height);
		//super.setOpaque(true);
	}
	public Boolean isVehicle() {
		return isVehicle;
	}
	public void move() {
		ticksTillMove -= speed*speedMultiplyer;
		if (ticksTillMove <= 0) {
			x += direction*FSM;
			if (direction == 1 && x >= 224*FSM) {
				x = -1*width;
				this.setVisible(true);
				if (speed <= new Random().nextInt(10)-5 && isVehicle) {
					this.setVisible(false);
				}
			}
			else if (direction == -1 && (x+width) <= 0) {
				x = 224*FSM;
				this.setVisible(true);
				if (speed <= new Random().nextInt(15)-5 && isVehicle) {
					this.setVisible(false);
				}
			}
			this.setBounds(x, y, width, height);
			ticksTillMove = DEFAULTTICKSTILLMOVE;
			movedThisTick = true;
		}
		else {
			movedThisTick = false;
		}
	}
	public void updateImage(BufferedImage image) {
		this.setIcon(new ImageIcon(image));
	}
	public void manualOverideX(int newx) {
		this.x = newx;
	}
	public Boolean getMovedThisTick() {
		return movedThisTick;
	}
	public int getDirection() {
		return direction;
	}


}
