package DaPackage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame {
	private JFrame frame;
	private JPanel masterPanel;
	private List<ArrayList<JPanel>> listOfPanels = new ArrayList<ArrayList<JPanel>>();

	public MainFrame() {
		initialize();
	}

	private void initialize() {
		
		
		
		frame = new JFrame();
		
		
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setMinimumSize(Toolkit.getDefaultToolkit().getScreenSize());
		frame.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setBackground(Color.black);
		frame.setUndecorated(true);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		
		int mpSide = (frame.getHeight()/448)*448;

		masterPanel = new JPanel(new GridLayout(14, 14, 0, 0));
		masterPanel.setMinimumSize(new Dimension(mpSide, mpSide));

		Color nextColor = Color.pink;
		
		for (int row = 0; row <= 13; row++) {
			listOfPanels.add(new ArrayList<JPanel>());
			nextColor = (nextColor == Color.pink) ? Color.black : Color.pink;
			for (int collumn = 0; collumn <= 13; collumn++) {
				JPanel tempPanel = new JPanel();
				tempPanel.setMinimumSize(new Dimension(32, 32));
				tempPanel.setMaximumSize(new Dimension(32, 32));
				tempPanel.setBackground(nextColor);
				nextColor = (nextColor == Color.pink) ? Color.black : Color.pink;
				listOfPanels.get(row).add(tempPanel);
				masterPanel.add(tempPanel);
				}
		}
		frame.pack();
		frame.setVisible(true);
		frame.add(masterPanel);
		frame.addKeyListener(new KeyAdapter() {
			  public void keyPressed(KeyEvent e) {
			    int keyCode = e.getKeyCode();
			    if (keyCode == KeyEvent.VK_ESCAPE) {
			      frame.dispose();
			    }
			    
			  }
			});
	}
}
