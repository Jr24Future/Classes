package bugnus;

import javax.swing.JFrame;

public class Bilgus {
	
	public Bilgus() {
		JFrame frame = new JFrame();
		
		BilgusPanel bilpan = new BilgusPanel();
		
		frame.add(bilpan);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Glaggle");
		frame.setLocationRelativeTo(null);
		
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		
		new Bilgus();

	}

}
