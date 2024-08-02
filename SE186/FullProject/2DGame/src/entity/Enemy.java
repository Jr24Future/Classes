package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import main.GamePanel;
import java.util.Random;

public class Enemy {
	
	GamePanel gp;
	Random rand = new Random();
	public int enemX, enemY, enemSpeed, topRight, botLeft, botRight;
	public boolean collide;
	
	public Enemy(GamePanel gp) {
		this.gp = gp;
		enemX = rand.nextInt(gp.screenWidth);
		enemY = rand.nextInt(gp.screenHeight);
		enemSpeed = 1;
	}
	
	public void update() {
		topRight = enemX + Player.playerSize;
		botLeft = enemY + Player.playerSize;
		botRight = (int) Math.sqrt(Math.pow(topRight, 2) + Math.pow(botLeft, 2));
//		if(enemX < Player.x)
//			enemX += enemSpeed;
//		else if(enemX > Player.x)
//			enemX -= enemSpeed;
//		if(enemY < Player.y)
//			enemY += enemSpeed;
//		else if(enemY > Player.y)
//			enemY -= enemSpeed;
		
		if((enemX >= Player.x) && (enemX <= Player.topRight))
				collide = true;
		else
			collide = false;
		
		
	}
	
	
	public void draw(Graphics2D g2) {
		
		g2.setColor(Color.red);
		
		g2.fillRect(enemX, enemY, Player.playerSize, Player.playerSize);
		
	}
}
