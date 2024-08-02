package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import api.Path;
import api.Point;
import api.PositionVector;
import api.Train;
import simulation.Simulation;

public class TracksPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Simulation simulation;
	private Stroke solid = new BasicStroke(3);
	private int width = 600;
	private int height = 600;
	private BufferedImage trainEngineImage;

	public TracksPanel(Simulation simulation) {
		this.simulation = simulation;
		Dimension dim = new Dimension(width, height);
		setBackground(Color.WHITE);
		setPreferredSize(dim);
		setMaximumSize(dim);
		setMinimumSize(dim);
		String imagePath = "resources/train-engine.png";
		try {
			trainEngineImage = ImageIO.read(new File(imagePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		ArrayList<Path> paths = simulation.getTrack().getPaths();
		Graphics2D g2 = (Graphics2D) g;
		for (Path path : paths) {
			for (int i=0; i<path.getNumPoints() - 1; i+=2) {
				Point pointA = path.getPointByIndex(i);
				Point pointB = path.getPointByIndex(i+1);
				g2.setColor(Color.BLACK);
				g2.setStroke(solid);
				g2.drawLine((int)pointA.getX(), (int)pointA.getY(), (int)pointB.getX(), (int)pointB.getY());
			}
		}
		
		Train train = simulation.getTrain();
		PositionVector position = train.getPosition();
	    //AffineTransform backup = g2.getTransform();
	    //AffineTransform a = AffineTransform.getRotateInstance(0.8, trainx + 15, trainy + 11);
	    //g2.setTransform(a);
	    g2.drawImage(trainEngineImage, (int)position.getPointB().getX() - 15, (int)position.getPointB().getY() - 11, null);
	    //g2.setTransform(backup);
	}
}
