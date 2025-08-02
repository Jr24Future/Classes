package view;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * This class sets up the graphical user interface of the calculator.
 * It represents the "View" in the MVC architecture.
 */
public class CalculatorView extends JFrame {
    public JTextField display = new JTextField();
    public HashMap<String, JButton> buttons = new HashMap<>();

    // All button labels for the calculator
    private final String[] buttonLabels = {
        "7", "8", "9", "/",
        "4", "5", "6", "*",
        "1", "2", "3", "-",
        "0", ".", "=", "+",
        "C", "Del", "x²", "√",
        "M+", "M-", "MR", "MC",
        "+/-"
    };

    // Constructor: builds the UI
    public CalculatorView() {
        this.setTitle("Scientific Calculator - Lab 7");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 600);
        this.setLayout(new BorderLayout());

        // Setup display
        display.setEditable(false);
        display.setFont(new Font("Arial", Font.PLAIN, 32));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setName("display"); // ✅ Set name for testing
        this.add(display, BorderLayout.NORTH);

        // Setup button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(7, 4, 5, 5));

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.BOLD, 20));
            button.setName(label); // ✅ Set name for AssertJ
            buttons.put(label, button);
            buttonPanel.add(button);
        }

        this.add(buttonPanel, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public void updateDisplay(String text) {
        display.setText(text);
    }

    public String getDisplayText() {
        return display.getText();
    }
}
