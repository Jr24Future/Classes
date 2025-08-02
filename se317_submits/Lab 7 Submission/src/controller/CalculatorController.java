package controller;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import model.CalculatorModel;
import view.CalculatorView;

public class CalculatorController {
    private CalculatorModel model;
    private CalculatorView view;

    private double firstOperand = 0;
    private String currentOperation = null;
    private boolean waitingForSecondOperand = false;
    private JButton activeOperationButton = null;

    private StringBuilder currentInput = new StringBuilder();

    public CalculatorController(CalculatorModel model, CalculatorView view) {
        this.model = model;
        this.view = view;

        for (String label : view.buttons.keySet()) {
            JButton btn = view.buttons.get(label);
            btn.addActionListener(new ButtonListener(label));
        }
    }

    private class ButtonListener implements ActionListener {
        private final String label;

        public ButtonListener(String label) {
            this.label = label;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            resetAllButtonStyles();

            // Highlight the clicked button
            clickedButton.setOpaque(true);
            clickedButton.setBackground(Color.BLUE);
            clickedButton.setContentAreaFilled(true);

            switch (label) {
                case "0": case "1": case "2": case "3": case "4":
                case "5": case "6": case "7": case "8": case "9":
                case ".":
                    currentInput.append(label);
                    view.updateDisplay(currentInput.toString());
                    break;

                case "C":
                    currentInput.setLength(0);
                    currentOperation = null;
                    waitingForSecondOperand = false;
                    firstOperand = 0;
                    if (activeOperationButton != null) {
                        activeOperationButton.setBackground(null);
                        activeOperationButton.setOpaque(false);
                        activeOperationButton.setContentAreaFilled(false);
                        activeOperationButton = null;
                    }
                    view.updateDisplay("");
                    break;

                case "Del":
                    if (currentInput.length() > 0) {
                        currentInput.deleteCharAt(currentInput.length() - 1);
                        view.updateDisplay(currentInput.toString());
                    }
                    break;

                case "+": case "-": case "*": case "/":
                    if (!currentInput.toString().isEmpty()) {
                        try {
                            firstOperand = Double.parseDouble(currentInput.toString());
                            currentOperation = label;
                            waitingForSecondOperand = true;
                            currentInput.setLength(0);
                            activeOperationButton = view.buttons.get(label);
                        } catch (NumberFormatException ex) {
                            view.updateDisplay("Error");
                        }
                    } else {
                        // No operand entered before operation
                        view.updateDisplay("Error");
                    }
                    break;

                case "=":
                    if (currentOperation != null && !currentInput.toString().isEmpty()) {
                        try {
                            double secondOperand = Double.parseDouble(currentInput.toString());
                            double result = 0;
                            switch (currentOperation) {
                                case "+": result = model.add(firstOperand, secondOperand); break;
                                case "-": result = model.subtract(firstOperand, secondOperand); break;
                                case "*": result = model.multiply(firstOperand, secondOperand); break;
                                case "/": result = model.divide(firstOperand, secondOperand); break;
                            }
                            view.updateDisplay(Double.toString(result));
                            currentInput.setLength(0);
                            currentInput.append(result);
                            currentOperation = null;
                            waitingForSecondOperand = false;
                        } catch (Exception ex) {
                            view.updateDisplay("Error");
                            currentInput.setLength(0);
                            currentOperation = null;
                            waitingForSecondOperand = false;
                        }
                    } else {
                        view.updateDisplay("Error");
                    }
                    break;

                case "x²":
                    try {
                        double value = Double.parseDouble(currentInput.toString());
                        double result = model.square(value);
                        view.updateDisplay(Double.toString(result));
                        currentInput.setLength(0);
                        currentInput.append(result);
                    } catch (Exception ex) {
                        view.updateDisplay("Error");
                        currentInput.setLength(0);
                    }
                    break;

                case "√":
                    try {
                        double value = Double.parseDouble(currentInput.toString());
                        if (value < 0) throw new ArithmeticException("Negative square root");
                        double result = model.squareRoot(value);
                        view.updateDisplay(Double.toString(result));
                        currentInput.setLength(0);
                        currentInput.append(result);
                    } catch (Exception ex) {
                        view.updateDisplay("Error");
                        currentInput.setLength(0);
                    }
                    break;

                case "+/-":
                    if (!currentInput.toString().isEmpty()) {
                        if (currentInput.charAt(0) == '-') {
                            currentInput.deleteCharAt(0);
                        } else {
                            currentInput.insert(0, '-');
                        }
                        view.updateDisplay(currentInput.toString());
                    }
                    break;

                case "M+":
                    try {
                        if (!currentInput.toString().isEmpty()) {
                            double value = Double.parseDouble(currentInput.toString());
                            model.addToMemory(value);
                        } else {
                            view.updateDisplay("Error");
                        }
                    } catch (NumberFormatException e1) {
                        view.updateDisplay("Error");
                    }
                    break;

                case "M-":
                    try {
                        if (!currentInput.toString().isEmpty()) {
                            double value = Double.parseDouble(currentInput.toString());
                            model.subtractFromMemory(value);
                        } else {
                            view.updateDisplay("Error");
                        }
                    } catch (NumberFormatException e1) {
                        view.updateDisplay("Error");
                    }
                    break;

                case "MR":
                    double memoryValue = model.recallMemory();
                    view.updateDisplay(Double.toString(memoryValue));
                    currentInput.setLength(0);
                    currentInput.append(memoryValue);
                    break;

                case "MC":
                    model.clearMemory();
                    break;
            }
        }

        private void resetAllButtonStyles() {
            for (JButton button : view.buttons.values()) {
                button.setBackground(null);
                button.setOpaque(false);
                button.setContentAreaFilled(false);
            }
        }
    }
}
