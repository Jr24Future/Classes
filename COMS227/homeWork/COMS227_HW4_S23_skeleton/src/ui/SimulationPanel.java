package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import api.Path;
import api.PositionVector;
import api.Train;
import simulation.Simulation;
import simulation.TestTrack1;
import simulation.TestTrack2;
import simulation.Track;

public class SimulationPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private TracksPanel tracksPanel;
	private Simulation simulation;
	private Timer updateTimer;
	private DateFormat dateandtime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	public SimulationPanel(Simulation simulation) {
		this.simulation = simulation;
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setLayout(new BorderLayout(0, 0));
		tracksPanel = new TracksPanel(simulation);
		add(tracksPanel, BorderLayout.CENTER);
		JPanel southPanel = new JPanel();
		southPanel.setOpaque(false);
		JButton track1Button = new JButton("Track 1");
		JButton track2Button = new JButton("Track 2");
		track1Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Track track = new TestTrack1();
				simulation.setTrack(track);
				makeTrain();
			}
		});
		track2Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Track track = new TestTrack2();
				simulation.setTrack(track);
				makeTrain();
			}
		});
		southPanel.add(track1Button);
		southPanel.add(track2Button);
		add(southPanel, BorderLayout.SOUTH);
		setBackground(new Color(0x444444));
		
		updateTimer = new Timer(200, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simulation.update();
				tracksPanel.repaint();
			}
		});
		updateTimer.start();
	}

	private void makeTrain() {
		Path path = simulation.getTrack().getPaths().get(0);
		PositionVector position = new PositionVector();
		position.setPointA(path.getPointByIndex(0));
		position.setPointB(path.getPointByIndex(1));
		Train train = new Train(position);
		simulation.setTrain(train);
	}
}
