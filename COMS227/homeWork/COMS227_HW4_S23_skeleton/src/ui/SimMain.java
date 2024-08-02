package ui;

import java.awt.EventQueue;
import java.util.Random;

import javax.swing.JFrame;

import simulation.Simulation;

public class SimMain extends JFrame {
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SimMain frame = new SimMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public SimMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 700);
		Simulation simulation = new Simulation();
		SimulationPanel simulationPanel = new SimulationPanel(simulation);
		setContentPane(simulationPanel);
	}
}
