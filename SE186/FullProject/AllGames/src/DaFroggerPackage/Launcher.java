package DaFroggerPackage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Launcher {
	static int FSM;
	static MainWindow MF;
	static JLabel timerLabel = new JLabel();
	static JLabel backgroundLabel;
	static LifeLabel livesLabel1;
	static LifeLabel livesLabel2;
	static LifeLabel livesLabel3;
	static LifeLabel livesLabel4;
	static LifeLabel livesLabel5;
	static LifeLabel livesLabel6;
	static LifeLabel livesLabel7;
	static EndZone endZone1;
	static EndZone endZone2;
	static EndZone endZone3;
	static EndZone endZone4;
	static EndZone endZone5;
	static ScoreLabel scoreLabel;
	static HiScoreLabel HiScoreLabel;
	static double timeRemaining = 30.0;
	static int livesRemaining = 7;
	static int levelSpeed = 1;
	static int score = 0;
	static Boolean stopped = false;
	static int lastLaneReached = 0;
	static int numLivesGained = 0;
	public static void main(String[] args) {
		Boolean[] frogCanMoveDirections = {true, true, true, true};
		MF = new MainWindow();
		MF.frame.pack();
		MF.frame.setVisible(true);
		FSM = MF.getFrameSizeMultiplyer();
		
		
		makeBackground();
		
		
		//makeTimer();
		updateTimer();
		
		makeLiveslabels();
		 
		makeEndZones(new Boolean[] {false, false, false, false, false});
		
		makeScoreLabels();
		
		List<Obby> list = new ArrayList<>();
		
		list = initObbys(list);

		Frog frog = new Frog(FSM);
		backgroundLabel.add(frog);
		backgroundLabel.setComponentZOrder(frog, 0);
		frog.repaint();
		
		
		
		
		
		
		
		
		
		
		play(list, frog, frogCanMoveDirections);
		
		
	}
	private static void play(List<Obby> list, Frog frog, Boolean[] frogCanMoveDirections){
		updateTimer();
		updateLives();
		for (int i = 0; i <= randomMovesCount(); i++) {//Moves a random number of times to populate that screen and let random disappear take effect
			move(list);
		}
		stopped = false;
		while (!stopped) {//begins game loop
			try {
				Thread.sleep(1);//updates every millisecond
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			move(list);
			frogCanMoveDirections = moveFrog(list, frog, frogCanMoveDirections);
			hitReg(list, frog, frogCanMoveDirections);
			updateLives();
			timeRemaining -= .001;
			if (timeRemaining <= 0){
				death(list, frog, frogCanMoveDirections);
			}
			updateTimer();
			//System.out.println("Score: "+score);
		}
	}
	private static int randomMovesCount() {//generates a random number which will later be used to determine how many moves will be made before game begins
		Random rand = new Random();
		int returnValue = (int) (rand.nextDouble()+1.0)*100000;
		return returnValue;
	}
	private static void move(List<Obby> list) {
		for (Obby i : list) {
			i.move();
			i.repaint();
		}
	}
	private static Boolean[] moveFrog(List<Obby>list, Frog frog, Boolean[] frogCanMoveDirections) {
		Boolean up = false;
		Boolean down = false;
		Boolean right = false;
		Boolean left = false;
		
		if (Keyboard.isKeyPressed(KeyEvent.VK_W) || Keyboard.isKeyPressed(KeyEvent.VK_UP)){
			up = true;
		}
		else if (Keyboard.isKeyPressed(KeyEvent.VK_S) || Keyboard.isKeyPressed(KeyEvent.VK_DOWN)){
			down = true;
		}
		if (Keyboard.isKeyPressed(KeyEvent.VK_D) || Keyboard.isKeyPressed(KeyEvent.VK_RIGHT)){
			right = true;
		}
		else if (Keyboard.isKeyPressed(KeyEvent.VK_A) || Keyboard.isKeyPressed(KeyEvent.VK_LEFT)){
			left = true;
		}
		
		
		
		if (up) {
			Boolean canJump = false;
			if (frog.getY() >= 48*FSM && frog.getY() <= 128*FSM) {
				JLabel newLabel = new JLabel();
				newLabel.setBounds(frog.getX()+FSM, frog.getY()-(15*FSM), frog.getWidth()-(2*FSM), frog.getHeight()-(2*FSM));
				for (Obby i : list) {
					Area areaA = new Area(i.getBounds());
				    Area areaB = new Area(newLabel.getBounds());
				    

				    if (areaA.contains(areaB.getBounds2D())) {
				    	
				    	canJump = true;
					}
				}
			    Area areaB = new Area(newLabel.getBounds());
			    Area area1 = new Area(endZone1.getBounds());
			    Area area2 = new Area(endZone2.getBounds());
			    Area area3 = new Area(endZone3.getBounds());
			    Area area4 = new Area(endZone4.getBounds());
			    Area area5 = new Area(endZone5.getBounds());

			    if ((area1.contains(areaB.getBounds2D()) && !endZone1.getFilled()) || (area2.contains(areaB.getBounds2D()) && !endZone2.getFilled()) || (area3.contains(areaB.getBounds2D()) && !endZone3.getFilled()) || 
			    		(area4.contains(areaB.getBounds2D()) && !endZone4.getFilled()) || (area5.contains(areaB.getBounds2D()) && !endZone5.getFilled())) {
			    	
			    	canJump = true;
				}
			}
			else {
				canJump = true;
			}
			if (frogCanMoveDirections[0] && canJump) {
				frog.move('n');
				if (lastLaneReached <= 0 && frog.getX() <= 208*FSM) {
					updateScores(10);
					lastLaneReached++;
				}
				else if (lastLaneReached <= 0 && frog.getX() <= 192*FSM) {
					updateScores(10);
					lastLaneReached++;
				}
				else if (lastLaneReached <= 1 && frog.getX() <= 176*FSM) {
					updateScores(10);
					lastLaneReached++;
				}
				else if (lastLaneReached <= 2 && frog.getY() <= 160*FSM) {
					updateScores(10);
					lastLaneReached++;
				}
				else if (lastLaneReached <= 3 && frog.getY() <= 144*FSM) {
					updateScores(10);
					lastLaneReached++;
				}
				else if (lastLaneReached <= 4 && frog.getY() <= 128*FSM) {
					updateScores(10);
					lastLaneReached++;
				}
				else if (lastLaneReached <= 5 && frog.getY() <= 112*FSM) {
					updateScores(10);
					lastLaneReached++;
				}
				else if (lastLaneReached <= 6 && frog.getY() <= 96*FSM) {
					updateScores(10);
					lastLaneReached++;
				}
				else if (lastLaneReached <= 7 && frog.getY() <= 80*FSM) {
					updateScores(10);
					lastLaneReached++;
				}
				else if (lastLaneReached <= 8 && frog.getY() <= 64*FSM) {
					updateScores(10);
					lastLaneReached++;
				}
				else if (lastLaneReached <= 9 && frog.getY() <= 48*FSM) {
					updateScores(10);
					lastLaneReached++;
				}
				else if (lastLaneReached <= 10 && frog.getY() <= 32*FSM) {
					updateScores(10);
					lastLaneReached++;
				}
			}
			frogCanMoveDirections[0] = false;
			
		}
		else {
			frogCanMoveDirections[0] = true;
		}
		if (down) {
			Boolean canJump = false;
			if (frog.getY() >= 48*FSM && frog.getY() <= 98*FSM) {
				JLabel newLabel = new JLabel();
				newLabel.setBounds(frog.getX()+FSM, frog.getY()+(17*FSM), frog.getWidth()-(2*FSM), frog.getHeight()-(2*FSM));
				for (Obby i : list) {
					Area areaA = new Area(i.getBounds());
				    Area areaB = new Area(newLabel.getBounds());

				    if (areaA.contains(areaB.getBounds2D())) {
				    	canJump = true;
					}
				}
			}
			else if (frog.getY()<224*FSM){
				canJump = true;
			}
			if (frogCanMoveDirections[1] && canJump) {
				frog.move('s');
			}
			frogCanMoveDirections[1] = false;
		}
		else {
			frogCanMoveDirections[1] = true;
		}
		if (right) {
			Boolean canJump = false;
			if (frog.getY() >= 48*FSM && frog.getY() <= 112*FSM && frog.getX() < 208*FSM) {
				JLabel newLabel = new JLabel();
				newLabel.setBounds(frog.getX()+(15*FSM), frog.getY()+FSM, frog.getWidth()-(2*FSM), frog.getHeight()-(2*FSM));
				for (Obby i : list) {
					Area areaA = new Area(i.getBounds());
				    Area areaB = new Area(newLabel.getBounds());

				    if (areaA.contains(areaB.getBounds2D())) {
				    	canJump = true;
					}
				}
			}
			else if (frog.getX() < 208*FSM){
				canJump = true;
			}
			if (frogCanMoveDirections[2] && canJump) {
				frog.move('e');
			}
			frogCanMoveDirections[2] = false;
		}
		else {
			frogCanMoveDirections[2] = true;
		}
		if (left) {
			Boolean canJump = false;
			if (frog.getY() >= 48*FSM && frog.getY() <= 112*FSM && frog.getX() > 7*FSM) {
				JLabel newLabel = new JLabel();
				newLabel.setBounds(frog.getX()-(15*FSM), frog.getY()+FSM, frog.getWidth()-(2*FSM), frog.getHeight()-(2*FSM));
				for (Obby i : list) {
					Area areaA = new Area(i.getBounds());
				    Area areaB = new Area(newLabel.getBounds());

				    if (areaA.contains(areaB.getBounds2D())) {
				    	canJump = true;
					}
				}
			}
			else if (frog.getX() > 7*FSM){
				canJump = true;
			}
			if (frogCanMoveDirections[3] && canJump) {
				frog.move('w');
			}
			frogCanMoveDirections[3] = false;
		}
		else {
			frogCanMoveDirections[3] = true;
		}

		
		
		return frogCanMoveDirections;
		
		
	}
	private static void hitReg(List<Obby>list, Frog frog, Boolean[] frogCanMoveDirections){
		Boolean shouldDie = false;
		for (Obby i : list) {
			if (i.isVehicle() && i.isVisible()) {
				Area areaA = new Area(frog.getBounds());
			    Area areaB = new Area(i.getBounds());

			    if (areaA.intersects(areaB.getBounds2D())) {
			    	shouldDie = true;
				}
			}

			
		}
		if (frog.getY() >= 48*FSM && frog.getY() <= 112*FSM){
			shouldDie = true;
			for (Obby i : list) {

				if (!i.isVehicle()) {
					Area areaA = new Area(i.getBounds());
				    Area areaB = new Area(frog.getBounds());
				    
				    if (!areaA.intersects(areaB.getBounds2D())) {
						shouldDie = false;
					}
				    else {
				    	if (i.getMovedThisTick()) {
				    		frog.setBounds(frog.getX()+(i.getDirection()*FSM), frog.getY(), frog.getWidth(), frog.getHeight());
						}
				    }
				}
				
			}
		}
		if (frog.getX() > 216*FSM || frog.getX() < -8*FSM) {
			shouldDie = true;
		}
		if (shouldDie) {
			death(list, frog, frogCanMoveDirections);
		}
		Area areaB = new Area(frog.getBounds());
	    Area area1 = new Area(endZone1.getBounds());
	    Area area2 = new Area(endZone2.getBounds());
	    Area area3 = new Area(endZone3.getBounds());
	    Area area4 = new Area(endZone4.getBounds());
	    Area area5 = new Area(endZone5.getBounds());
	    
	    Boolean shouldReset = false;
	    
		if (area1.intersects(areaB.getBounds2D()) && !endZone1.getFilled()) {
			updateScores(50);
			endZone1.fill();
			shouldReset = true;
		}
		if (area2.intersects(areaB.getBounds2D()) && !endZone2.getFilled()) {
			updateScores(50);
			endZone2.fill();
			shouldReset = true;
			}
		if (area3.intersects(areaB.getBounds2D()) && !endZone3.getFilled()) {
			updateScores(50);
			endZone3.fill();
			shouldReset = true;
			}
		if (area4.intersects(areaB.getBounds2D()) && !endZone4.getFilled()) {
			updateScores(50);
			endZone4.fill();
			shouldReset = true;
			}
		if (area5.intersects(areaB.getBounds2D()) && !endZone5.getFilled()) {
			updateScores(50);
			endZone5.fill();
			shouldReset = true;
			}
		if (endZone1.getFilled() && endZone2.getFilled() && endZone3.getFilled() && endZone4.getFilled() && endZone5.getFilled()) {
			endZone1.unFill();
			endZone2.unFill();
			endZone3.unFill();
			endZone4.unFill();
			endZone5.unFill();
			updateScores(1000);
			levelSpeed += 1;
			shouldReset = true;
		}
		if (shouldReset) {
			updateScores((int) (timeRemaining*10));
			resetLevel(list, frog, frogCanMoveDirections);
			}
	}
	private static void death(List<Obby> list,  Frog frog, Boolean[] frogCanMoveDirections) {
		livesRemaining-=1;
		if (livesRemaining < 1) {
			MF.frame.dispose();
			System.exit(0);
		}
		resetLevel(list, frog, frogCanMoveDirections);
	}
	private static void resetLevel(List<Obby> list,  Frog frog, Boolean[] frogCanMoveDirections){
		stopped = true;
		timeRemaining = 30.0;
		lastLaneReached = 0;
		backgroundLabel.removeAll();
		frog = new Frog(FSM);
		backgroundLabel.add(frog);
		makeEndZones(new Boolean[] {endZone1.getFilled(), endZone2.getFilled(), endZone3.getFilled(), endZone4.getFilled(), endZone5.getFilled()});
		makeLiveslabels();
		makeScoreLabels();
		//makeTimer();
		updateTimer();
		updateScores(0);
		list = initObbys(list);
		play(list, frog, frogCanMoveDirections);
	}
	private static void makeBackground() {
		BufferedImage imageToDraw = null;
		BufferedImage image = null;
		imageToDraw = new BufferedImage(224, 256, BufferedImage.TYPE_INT_ARGB);
		image = new BufferedImage(224*FSM, 256*FSM, BufferedImage.TYPE_INT_ARGB);
		String s = MF.getClass().getResource("/DaFroggerAssets/mainGameBackground.png").toString();  //Make Sure to update this when importing assets
		s = s.substring(6);
		System.out.println(s);
		File f = null;
		try {
			f = new File(s);
			imageToDraw = ImageIO.read(f);
			System.out.println("Complete");
		} catch (Exception e) {
			System.out.println("Failure: "+e);
		}
		
		Graphics2D g2 = image.createGraphics();
		g2.drawImage(imageToDraw, 0, 0, 224*FSM, 256*FSM, null);
		backgroundLabel = new JLabel(new ImageIcon(image));
		backgroundLabel.setBounds(0, 0, 224*FSM, 256*FSM);
		MF.frame.add(backgroundLabel);
		backgroundLabel.repaint();
	}
	/*
	private static void makeTimer() {
		timerLabel = new JLabel();
		timerLabel.setBounds(70*FSM, 248*FSM, 120*FSM, 8*FSM);
		timerLabel.setOpaque(true);
		backgroundLabel.add(timerLabel);
		timerLabel.repaint();
		updateTimer();
	}
	*/
	private static void updateTimer() {
		BufferedImage image = new BufferedImage(120*FSM, 8*FSM, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = image.createGraphics();
		g2.setColor(Color.green);
		g2.fillRect(0, 0, 120*FSM, 8*FSM);
		//g2.setColor(Color.black);
		//g2.fillRect(0, 0, ((int) (30.0-timeRemaining)*4)*FSM, 8*FSM);
		
		backgroundLabel.remove(timerLabel);
		
		timerLabel = new JLabel(new ImageIcon(image));
		timerLabel.setBounds((70*FSM)+((int) ((30.0-timeRemaining)*4))*FSM, 248*FSM, (120*FSM) - ((int) ((30.0-timeRemaining)*4))*FSM, 8*FSM);
		//timerLabel.setOpaque(true);
		backgroundLabel.add(timerLabel);
		timerLabel.repaint();
	}
	private static void makeLiveslabels() {		
		livesLabel1 = new LifeLabel(FSM);
		livesLabel2 = new LifeLabel(FSM);
		livesLabel3 = new LifeLabel(FSM);
		livesLabel4 = new LifeLabel(FSM);
		livesLabel5 = new LifeLabel(FSM);
		livesLabel6 = new LifeLabel(FSM);
		livesLabel7 = new LifeLabel(FSM);
		
		livesLabel1.setBounds(1*FSM, 240*FSM, 8*FSM, 8*FSM);
		livesLabel2.setBounds(9*FSM, 240*FSM, 8*FSM, 8*FSM);
		livesLabel3.setBounds(17*FSM, 240*FSM, 8*FSM, 8*FSM);
		livesLabel4.setBounds(25*FSM, 240*FSM, 8*FSM, 8*FSM);
		livesLabel5.setBounds(33*FSM, 240*FSM, 8*FSM, 8*FSM);
		livesLabel6.setBounds(41*FSM, 240*FSM, 8*FSM, 8*FSM);
		livesLabel7.setBounds(49*FSM, 240*FSM, 8*FSM, 8*FSM);
		
		
		backgroundLabel.add(livesLabel1);
		backgroundLabel.add(livesLabel2);
		backgroundLabel.add(livesLabel3);
		backgroundLabel.add(livesLabel4);
		backgroundLabel.add(livesLabel5);
		backgroundLabel.add(livesLabel6);
		backgroundLabel.add(livesLabel7);
		
		updateLives();
	}
	private static void updateLives() {
		int lives = Math.min(livesRemaining, 8);
		
		if (lives > 1) {
			livesLabel1.setVisible(true);
		}
		else {
			livesLabel1.setVisible(false);
		}
		if (lives > 2) {
			livesLabel2.setVisible(true);
		}
		else {
			livesLabel2.setVisible(false);
		}
		if (lives > 3) {
			livesLabel3.setVisible(true);
		}
		else {
			livesLabel3.setVisible(false);
		}
		if (lives > 4) {
			livesLabel4.setVisible(true);
		}
		else {
			livesLabel4.setVisible(false);
		}
		if (lives > 5) {
			livesLabel5.setVisible(true);
		}
		else {
			livesLabel5.setVisible(false);
		}
		if (lives > 6) {
			livesLabel6.setVisible(true);
		}
		else {
			livesLabel6.setVisible(false);
		}
		if (lives > 7) {
			livesLabel7.setVisible(true);
		}
		else {
			livesLabel7.setVisible(false);
		}


		livesLabel1.repaint();
		livesLabel2.repaint();
		livesLabel3.repaint();
		livesLabel4.repaint();
		livesLabel5.repaint();
		livesLabel6.repaint();
		livesLabel7.repaint();
		
	}
	private static void makeScoreLabels(){
		scoreLabel = new ScoreLabel(FSM, score);
		HiScoreLabel = new HiScoreLabel(FSM, score);
		backgroundLabel.add(scoreLabel);
		backgroundLabel.add(HiScoreLabel);
		scoreLabel.repaint();
		HiScoreLabel.repaint();
	}
	private static void updateScores(int scoreToAdd) {
		score += scoreToAdd;
		if (score/20000 > numLivesGained) {
			numLivesGained++;
			livesRemaining++;
		}
		scoreLabel.updateScore(score);
		if (score > HiScoreLabel.getHiScore()) {
			HiScoreLabel.updateScore(score);
		}
	}
	private static void makeEndZones(Boolean[] filledList) {
		endZone1 = new EndZone(FSM, filledList[0]);
		endZone2 = new EndZone(FSM, filledList[1]);
		endZone3 = new EndZone(FSM, filledList[2]);
		endZone4 = new EndZone(FSM, filledList[3]);
		endZone5 = new EndZone(FSM, filledList[4]);
		
		endZone1.setBounds(8*FSM,   32*FSM, 16*FSM, 16*FSM);
		endZone2.setBounds(56*FSM,  32*FSM, 16*FSM, 16*FSM);
		endZone3.setBounds(104*FSM, 32*FSM, 16*FSM, 16*FSM);
		endZone4.setBounds(152*FSM, 32*FSM, 16*FSM, 16*FSM);
		endZone5.setBounds(200*FSM, 32*FSM, 16*FSM, 16*FSM);
		
		backgroundLabel.add(endZone1);
		backgroundLabel.add(endZone2);
		backgroundLabel.add(endZone3);
		backgroundLabel.add(endZone4);
		backgroundLabel.add(endZone5);
		
		endZone1.repaint();
		endZone2.repaint();
		endZone3.repaint();
		endZone4.repaint();
		endZone5.repaint();
	}
	private static List<Obby> initObbys(List<Obby> list) {
		
		list= new ArrayList<>();
		
		YellowRacecar yellowRacecar1 = new YellowRacecar(FSM, levelSpeed);
		yellowRacecar1.manualOverideX(224*FSM);
		list.add(yellowRacecar1);
		backgroundLabel.add(yellowRacecar1);
		yellowRacecar1.repaint();
		
		YellowRacecar yellowRacecar2 = new YellowRacecar(FSM, levelSpeed);
		yellowRacecar1.manualOverideX((279*FSM)+yellowRacecar2.getWidth());
		list.add(yellowRacecar2);
		backgroundLabel.add(yellowRacecar2);
		yellowRacecar2.repaint();
		
		YellowRacecar yellowRacecar3 = new YellowRacecar(FSM, levelSpeed);
		yellowRacecar3.manualOverideX((334*FSM)+(yellowRacecar3.getWidth()*2));
		list.add(yellowRacecar3);
		backgroundLabel.add(yellowRacecar3);
		yellowRacecar3.repaint();
		
		YellowRacecar yellowRacecar4 = new YellowRacecar(FSM, levelSpeed);
		yellowRacecar4.manualOverideX((389*FSM)+(yellowRacecar4.getWidth()*2));
		list.add(yellowRacecar4);
		backgroundLabel.add(yellowRacecar4);
		yellowRacecar4.repaint();
		
		Tractor tractor1 = new Tractor(FSM, levelSpeed);
		tractor1.manualOverideX(tractor1.getWidth()*-1);
		list.add(tractor1);
		backgroundLabel.add(tractor1);
		tractor1.repaint();
		
		Tractor tractor2 = new Tractor(FSM, levelSpeed);
		tractor2.manualOverideX((tractor2.getWidth()*-2)-(45*FSM));
		list.add(tractor2);
		backgroundLabel.add(tractor2);
		tractor2.repaint();
		
		Tractor tractor3 = new Tractor(FSM, levelSpeed);
		tractor3.manualOverideX((tractor3.getWidth()*-3)-(90*FSM));
		list.add(tractor3);
		backgroundLabel.add(tractor3);
		tractor3.repaint();
		
		Tractor tractor4 = new Tractor(FSM, levelSpeed);
		tractor4.manualOverideX((tractor4.getWidth()*-4)-(135*FSM));
		list.add(tractor4);
		backgroundLabel.add(tractor4);
		tractor4.repaint();
		
		Car car1 = new Car(FSM, levelSpeed);
		car1.manualOverideX(224*FSM);
		list.add(car1);
		backgroundLabel.add(car1);
		car1.repaint();
		
		Car car2 = new Car(FSM, levelSpeed);
		car2.manualOverideX((279*FSM)+car2.getWidth());
		list.add(car2);
		backgroundLabel.add(car2);
		car2.repaint();
		
		Car car3 = new Car(FSM, levelSpeed);
		car3.manualOverideX((334*FSM)+(car3.getWidth()*2));
		list.add(car3);
		backgroundLabel.add(car3);
		car3.repaint();
		
		Car car4 = new Car(FSM, levelSpeed);
		car4.manualOverideX((389*FSM)+(car4.getWidth()*2));
		list.add(car4);
		backgroundLabel.add(car4);
		car4.repaint();
		
		RedRacecar redRacecar1 = new RedRacecar(FSM, levelSpeed);
		redRacecar1.manualOverideX(redRacecar1.getWidth()*-1);
		list.add(redRacecar1);
		backgroundLabel.add(redRacecar1);
		redRacecar1.repaint();
		
		RedRacecar redRacecar2 = new RedRacecar(FSM, levelSpeed);
		redRacecar2.manualOverideX((redRacecar2.getWidth()*-1)-(35*FSM));
		list.add(redRacecar2);
		backgroundLabel.add(redRacecar2);
		redRacecar2.repaint();
		
		Truck truck1 = new Truck(FSM, levelSpeed);
		truck1.manualOverideX(224*FSM);
		list.add(truck1);
		backgroundLabel.add(truck1);
		truck1.repaint();
		
		Truck truck2 = new Truck(FSM, levelSpeed);
		truck2.manualOverideX((284*FSM)+truck2.getWidth());
		list.add(truck2);
		backgroundLabel.add(truck2);
		truck2.repaint();
		
		Truck truck3 = new Truck(FSM, levelSpeed);
		truck3.manualOverideX((344*FSM)+(truck3.getWidth()*2));
		list.add(truck3);
		backgroundLabel.add(truck3);
		truck3.repaint();
		
		TripleTurtle tripleTurtle1 = new TripleTurtle(FSM, levelSpeed, true);
		tripleTurtle1.manualOverideX(224*FSM);
		list.add(tripleTurtle1);
		backgroundLabel.add(tripleTurtle1);
		tripleTurtle1.repaint();
		
		TripleTurtle tripleTurtle2 = new TripleTurtle(FSM, levelSpeed, false);
		tripleTurtle2.manualOverideX((244*FSM)+(tripleTurtle2.getWidth()));
		list.add(tripleTurtle2);
		backgroundLabel.add(tripleTurtle2);
		tripleTurtle2.repaint();
		
		TripleTurtle tripleTurtle3 = new TripleTurtle(FSM, levelSpeed, false);
		tripleTurtle3.manualOverideX((264*FSM)+(tripleTurtle3.getWidth()*2));
		list.add(tripleTurtle3);
		backgroundLabel.add(tripleTurtle3);
		tripleTurtle3.repaint();
		
		TripleTurtle tripleTurtle4 = new TripleTurtle(FSM, levelSpeed, false);
		tripleTurtle4.manualOverideX((284*FSM)+(tripleTurtle4.getWidth()*3));
		list.add(tripleTurtle4);
		backgroundLabel.add(tripleTurtle4);
		tripleTurtle4.repaint();
		
		TripleLog tripleLog1 = new TripleLog(FSM, levelSpeed);
		tripleLog1.manualOverideX((tripleLog1.getWidth()*-1));
		list.add(tripleLog1);
		backgroundLabel.add(tripleLog1);
		tripleLog1.repaint();
		
		TripleLog tripleLog2 = new TripleLog(FSM, levelSpeed);
		tripleLog2.manualOverideX((-40*FSM)+(tripleLog2.getWidth()*-2));
		list.add(tripleLog2);
		backgroundLabel.add(tripleLog2);
		tripleLog2.repaint();
		
		TripleLog tripleLog3 = new TripleLog(FSM, levelSpeed);
		tripleLog3.manualOverideX((-80*FSM)+(tripleLog3.getWidth()*-3));
		list.add(tripleLog3);
		backgroundLabel.add(tripleLog3);
		tripleLog3.repaint();
		
		PentaLog pentaLog1 = new PentaLog(FSM, levelSpeed);
		pentaLog1.manualOverideX(pentaLog1.getWidth()*-1);
		list.add(pentaLog1);
		backgroundLabel.add(pentaLog1);
		pentaLog1.repaint();
		
		PentaLog pentaLog2 = new PentaLog(FSM, levelSpeed);
		pentaLog2.manualOverideX((-20*FSM)+(pentaLog2.getWidth()*-2));
		list.add(pentaLog2);
		backgroundLabel.add(pentaLog2);
		pentaLog2.repaint();
		
		PentaLog pentaLog3 = new PentaLog(FSM, levelSpeed);
		pentaLog3.manualOverideX((-40*FSM)+(pentaLog3.getWidth()*-3));
		list.add(pentaLog3);
		backgroundLabel.add(pentaLog3);
		pentaLog3.repaint();
		
		DoubleTurtle doubleTurtle1 = new DoubleTurtle(FSM, levelSpeed, true);
		doubleTurtle1.manualOverideX(224*FSM);
		list.add(doubleTurtle1);
		backgroundLabel.add(doubleTurtle1);
		doubleTurtle1.repaint();
		
		DoubleTurtle doubleTurtle2 = new DoubleTurtle(FSM, levelSpeed, false);
		doubleTurtle2.manualOverideX((254*FSM)+(doubleTurtle2.getWidth()));
		list.add(doubleTurtle2);
		backgroundLabel.add(doubleTurtle2);
		doubleTurtle2.repaint();
		
		DoubleTurtle doubleTurtle3 = new DoubleTurtle(FSM, levelSpeed, false);
		doubleTurtle3.manualOverideX((284*FSM)+(doubleTurtle3.getWidth()*2));
		list.add(doubleTurtle3);
		backgroundLabel.add(doubleTurtle3);
		doubleTurtle3.repaint();
		
		DoubleTurtle doubleTurtle4 = new DoubleTurtle(FSM, levelSpeed, false);
		doubleTurtle4.manualOverideX((314*FSM)+(doubleTurtle4.getWidth()*3));
		list.add(doubleTurtle4);
		backgroundLabel.add(doubleTurtle4);
		doubleTurtle4.repaint();
		
		QuadLog quadLog1 = new QuadLog(FSM, levelSpeed);
		quadLog1.manualOverideX((quadLog1.getWidth()*-1));
		list.add(quadLog1);
		backgroundLabel.add(quadLog1);
		quadLog1.repaint();
		
		QuadLog quadLog2 = new QuadLog(FSM, levelSpeed);
		quadLog2.manualOverideX((-30*FSM)+(quadLog2.getWidth()*-2));
		list.add(quadLog2);
		backgroundLabel.add(quadLog2);
		quadLog2.repaint();
		
		QuadLog quadLog3 = new QuadLog(FSM, levelSpeed);
		quadLog3.manualOverideX((-60*FSM)+(quadLog3.getWidth()*-3));
		list.add(quadLog3);
		backgroundLabel.add(quadLog3);
		quadLog3.repaint();
		
		
		
		
		return list;
	}
	
}


