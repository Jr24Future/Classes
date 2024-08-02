package DaFroggerPackage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainWindow {
	public JFrame frame;
	public JPanel panel;
	private int frameSizeMultiplyer;
	
	public MainWindow() {
		initialize();
	}
	
	private void initialize() {
		
		//Dimension minDimmy = new Dimension(224, 256);
		Dimension monitorDimmy = Toolkit.getDefaultToolkit().getScreenSize();
		double height = monitorDimmy.getHeight();
		double width = monitorDimmy.getWidth();
		Dimension frameDimmy; 
		frameSizeMultiplyer = (int) Math.min(Math.floor(height/256.0), Math.floor(width/224.0));
		
		
		
		frameDimmy = new Dimension(224 * frameSizeMultiplyer, 256 * frameSizeMultiplyer);
		
		frame = new JFrame();
		
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		frame.setMinimumSize(frameDimmy);
		frame.setMaximumSize(frameDimmy);
		frame.setBackground(Color.black);
		frame.setUndecorated(true);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setLayout(new FlowLayout());
		
		/*
		panel = new JPanel();
		panel.setMinimumSize(frameDimmy);
		panel.setMaximumSize(frameDimmy);
		panel.setLayout(null);

		frame.add(panel);
		*/
		
		
		
		
		//frame.pack();
		frame.addKeyListener(new KeyAdapter() {
			  public void keyPressed(KeyEvent e) {
			    int keyCode = e.getKeyCode();
			    if (keyCode == KeyEvent.VK_ESCAPE) {
			      frame.dispose();
			      System.exit(0);
			    }
			    
			  }
		});
		
	}
	
	
	
	public int getFrameSizeMultiplyer() {
		return frameSizeMultiplyer;
	}

}
