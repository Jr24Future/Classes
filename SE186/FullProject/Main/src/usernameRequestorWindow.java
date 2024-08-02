import java.awt.Color;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;

public class usernameRequestorWindow {
		private JFrame frame;
		private TextField tf;
		private File file;
		public usernameRequestorWindow() {
			initialize();
		}

		private void initialize() {
			
			
			
			frame = new JFrame();
			tf = new TextField();
			
			
			tf.setSize(500, 400);
			
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setMinimumSize(Toolkit.getDefaultToolkit().getScreenSize());
			frame.setMaximumSize(Toolkit.getDefaultToolkit().getScreenSize());
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setBackground(Color.black);
			frame.setUndecorated(true);
			frame.setLocationRelativeTo(null);
			frame.setResizable(false);
			
			frame.pack();
			frame.setVisible(true);
			tf.addKeyListener(new KeyAdapter() {
				  public void keyPressed(KeyEvent e) {
					    int keyCode = e.getKeyCode();
					    if (keyCode == KeyEvent.VK_ESCAPE) {
					      frame.dispose();
					    }
					    if (keyCode == KeyEvent.VK_ENTER) {
					    	System.out.println("Enter Pressed");
					    	enterPressed();
					    }
					    
					  }
				});
			frame.add(tf);
			frame.addKeyListener(new KeyAdapter() {
				  public void keyPressed(KeyEvent e) {
				    int keyCode = e.getKeyCode();
				    if (keyCode == KeyEvent.VK_ESCAPE) {
				      frame.dispose();
				    }
				    if (keyCode == KeyEvent.VK_ENTER) {
				    	System.out.println("Enter Pressed");
				    	enterPressed();
				    }
				    
				  }
			});
		}
		private void enterPressed() {
			try {
				file = new File("U:\\workspace\\storedScores\\" + tf.getText()+".txt");
				if (file.createNewFile()) {
					System.out.println("New file created: "+file.getName());
					FileWriter writer = new FileWriter(file.getAbsolutePath());
					//System.out.println( file.canWrite());

					writer.write("000000 000000 000000 000000");   // game scores will be imported here
					writer.close();
					System.out.println("Successfully wrote to the file.");
				}
				else {
					System.out.println("File already exists");
					
				}
			} catch (IOException e) {
				System.out.println("An error occurred.");
				e.printStackTrace();
			}
		}
}
