package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import main.GamePanel;
import java.util.Random;

public class Enemy{
	
	GamePanel gp;
	Random rand = new Random();
	public int enemX, enemY, enemSpeed, topRight, botLeft, botRight;
	public boolean collide;
	public boolean dead;
	
	public Enemy(GamePanel gp) {
		this.gp = gp;
		enemX = rand.nextInt(gp.screenWidth);
		enemY = 0;
		enemSpeed = rand.nextInt(5) + 1;
	}
	
	public void update() {
		topRight = enemX + Player.playerSize;
		botLeft = enemY + Player.playerSize;
		botRight = (int) Math.sqrt(Math.pow(topRight, 2) + Math.pow(botLeft, 2));
		
		if(!dead) {
			if(enemX < Player.x)
				enemX += enemSpeed;
			else if(enemX > Player.x)
				enemX -= enemSpeed;
			if(enemY < Player.y)
				enemY += enemSpeed;
			else if(enemY > Player.y)
				enemY -= enemSpeed;
		}
		
		if((Player.x <= topRight) && (Player.topRight >= enemX) && 
		   (Player.y <= botLeft && Player.botLeft >= enemY))
				collide = true;
		else
			collide = false;
		
		if(collide && Player.immune)
			dead = true;

		if(collide && !dead && !Player.immune)
			killPlayer();
	
	}
	
	public void killPlayer() {
		Player.dead = true;
		Player.speed = 0;
		enemSpeed = 0;
	}
	
	public void draw(Graphics2D g2) {
		if(dead) {
			g2.setColor(Color.LIGHT_GRAY);
		}
		else
			g2.setColor(Color.yellow);
		
		g2.fillRect(enemX, enemY, Player.playerSize, Player.playerSize);
		
	}
}
