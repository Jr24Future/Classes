package RunThisPackage;

import DaFroggerPackage.*;
import main.*;
import game.*;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class TheMainMain {

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setBackground(Color.black);
		frame.setUndecorated(false);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		//frame.setLayout(new FlowLayout());
		
		JLabel backgroundLabel = new JLabel();
		//backgroundLabel.setBounds(frame.getBounds());
		backgroundLabel.setLayout(new FlowLayout());
		backgroundLabel.setBackground(Color.DARK_GRAY);
		backgroundLabel.setOpaque(true);
		frame.add(backgroundLabel);
		
		JButton button1 = new JButton();
		JButton button2 = new JButton();
		JButton button3 = new JButton();
		
		
		button1.setBounds(0, frame.getHeight()/2, frame.getWidth()/3, frame.getHeight());
		button2.setBounds(frame.getWidth()/3, frame.getHeight()/2, frame.getWidth()/3, frame.getHeight());
		button3.setBounds(2*(frame.getWidth()/3), frame.getHeight()/2, frame.getWidth()/3, frame.getHeight());
		
		button1.setText("Frogger");
		button2.setText("Gauntlet");
		button3.setText("SpaceInvaders");
		
		button1.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        try {
		        	System.out.println("Attempting to run frogger");
		        	frame.dispose();
					DaFroggerPackage.Launcher.main(args);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    }
		});
		
		button2.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        try {
		        	System.out.println("Attempting to run gauntlet");
		        	frame.dispose();
					main.Main.main(args);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    }
		});
		
		button3.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        try {
		        	System.out.println("Attempting to run Space Invaders");
		        	frame.dispose();
					game.SpaceInvaders.main(args);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    }
		});
		
		backgroundLabel.add(button1);
		backgroundLabel.add(button2);
		backgroundLabel.add(button3);
		
		
		frame.setVisible(true);
		
	}

}
