package entity;

import java.awt.*;



import main.GamePanel;
import main.KeyHandler;

public class Player{
	
	public static int x, y;
	public static int speed;
	GamePanel gp;
	public String direction;
	public String name;
	KeyHandler keyH;
	final int DASHSPEED = 100;
	public static int playerSize;
	boolean dashReady;
	public static boolean immune;
	int dashInterval;
	int dashMax = 100;
	public static int topRight;
	public static int botLeft;
	public static int botRight;
	public static boolean dead = false;
	
	public Player(GamePanel gp, KeyHandler keyH) {
		
		this.gp = gp;
		this.keyH = keyH;
		
		setDefaultValues();
	}
	
	public void setDefaultValues() {
		x = gp.screenWidth / 2;
		y = gp.screenHeight / 2;
		speed = 4;
		direction = "right";
		playerSize = gp.tileSize / 2;
		dashReady = true;
		dashInterval = dashMax;
	}
	
	
	public void dash(String s) {
		dashReady = false;
		if(!dead) {
			immune = true;
			if(s == "right") {
				for(int i = 0; i < DASHSPEED; i++) {
					x++;
				}
			}
			else if(s == "left") {
				for(int i = 0; i < DASHSPEED; i++) {
					x--;
				}
			}
			else if(s == "up") {
				for(int i = 0; i < DASHSPEED; i++) {
					y--;
				}
			}
			else if(s == "down") {
				for(int i = 0; i < DASHSPEED; i++) {
					y++;
				}
			}
			dashInterval = 0;
		}
	}
	
	public void update() {
		immune = false;
		topRight = x + playerSize;
		botLeft = y + playerSize;
		botRight = (int) Math.sqrt(Math.pow(topRight, 2) + Math.pow(botLeft, 2));
		if(keyH.upPressed) {
			y -= speed;
			direction = "up";
		}
		if(keyH.downPressed) {
			y += speed;
			direction = "down";
		}
		if(keyH.rightPressed) {
			x += speed;
			direction = "right";
		}
		if(keyH.leftPressed) {
			x -= speed;
			direction = "left";
		}
		if(keyH.spacePressed && dashReady) {
			dash(direction);
		}
		if(y > gp.screenHeight - playerSize) {
			y = gp.screenHeight- playerSize;
		}
		if(y < 0) {
			y = 0;
		}
		if(x > gp.screenWidth - playerSize) {
			x = gp.screenWidth - playerSize;
		}
		if(x < 0) {
			x = 0;
		}
		
		dashInterval++;
		if(dashInterval >= dashMax) {
			dashReady = true;
			dashInterval = 0;
		}
		
		//System.out.println(topRight + ", " + botLeft + ", " + botRight);
	
	}
	
	public void draw(Graphics2D g2) {
		
		if(!dead) {
			if(dashReady)
				g2.setColor(Color.blue);
			else
				g2.setColor(Color.white);
		}
		else
			g2.setColor(Color.red);
		
		g2.fillRect(x, y, playerSize, playerSize);
	}

}
