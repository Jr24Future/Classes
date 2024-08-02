package com.bingus;

import javax.swing.SwingUtilities;

public class Launcher {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				MainWindow main = new MainWindow();
				main.show();
			}
			
		});

	}

}

//followed this tutorial https://www.youtube.com/watch?v=1vVJPzVzaK8&ab_channel=JavaCodeJunkie