package app;

import controller.CalculatorController;
import model.CalculatorModel;
import view.CalculatorView;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            CalculatorModel model = new CalculatorModel();
            CalculatorView view = new CalculatorView();
            new CalculatorController(model, view);
        });
    }
}

