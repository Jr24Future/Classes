package model;

/**
 * This class handles all the mathematical logic for the calculator.
 * It represents the "Model" in the MVC architecture.
 */
public class CalculatorModel {
    private double memory = 0.0;

    // Basic arithmetic operations
    public double add(double a, double b) {
        return a + b;
    }

    public double subtract(double a, double b) {
        return a - b;
    }

    public double multiply(double a, double b) {
        return a * b;
    }

    public double divide(double a, double b) throws ArithmeticException {
        if (b == 0) {
			throw new ArithmeticException("Division by zero");
		}
        return a / b;
    }

    // Advanced operations
    public double square(double a) {
        return a * a;
    }

    public double squareRoot(double a) {
        return Math.sqrt(a);
    }

    // Memory functions
    public void addToMemory(double value) {
        memory += value;
    }

    public void subtractFromMemory(double value) {
        memory -= value;
    }

    public void clearMemory() {
        memory = 0.0;
    }

    public double recallMemory() {
        return memory;
    }
}
