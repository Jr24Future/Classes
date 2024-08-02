package Game;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.*;
import java.awt.FlowLayout;

public class test {
	public static void main (String[] args) {
		JFrame f = new JFrame("Minesweeper"); // creates new window with the label "Minesweeper"
		f.setVisible(true); // makes the window visible
		f.setSize(800,400); // sets size of window
		f.setLayout(new FlowLayout());
		JLabel l1 = new JLabel("Username: "); // adds new text
		f.add(l1);
		JTextField t1 = new JTextField(20); // creates an input box with the argument being how many characters may be entered into it
		f.add(t1); // adds the object into the frame
	}
}
