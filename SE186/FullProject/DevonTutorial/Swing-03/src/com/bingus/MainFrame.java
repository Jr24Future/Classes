package com.bingus;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.*;

public class MainFrame {
	
	private JFrame frame;
	
	public MainFrame() {
		initialize();
	}
	
	public void initialize() {
		frame = new JFrame();
		frame.setTitle("JPanel Demo");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(500, 400);
		frame.setLocationRelativeTo(null);
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
		
		panel.setBackground(Color.GREEN);
		
		Button button = new Button("Bingus");
		panel.add(button);
		
		Button button2 = new Button("Bangus");
		panel.add(button2);
		
		Button button3 = new Button("Bongus");
		panel.add(button3);
		
		panel.setPreferredSize(new Dimension(250, 250));
		
		frame.add(panel, BorderLayout.CENTER);
		frame.setVisible(true);
		
	}
}
