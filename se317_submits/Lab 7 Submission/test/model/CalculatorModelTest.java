package model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit tests for CalculatorModel logic.
 */
public class CalculatorModelTest {
    private CalculatorModel model;

    @Before
    public void setUp() {
        model = new CalculatorModel();
    }

    // Basic arithmetic tests
    @Test
    public void testAddition() {
        assertEquals(5.0, model.add(2, 3), 0.001);
    }

    @Test
    public void testSubtraction() {
        assertEquals(4.0, model.subtract(10, 6), 0.001);
    }

    @Test
    public void testMultiplication() {
        assertEquals(12.0, model.multiply(3, 4), 0.001);
    }

    @Test(expected = ArithmeticException.class)
    public void testDivisionByZero() {
        model.divide(10, 0); // Should throw
    }

    @Test
    public void testDivision() {
        assertEquals(2.5, model.divide(5, 2), 0.001);
    }

    // Advanced functions
    @Test
    public void testSquare() {
        assertEquals(16.0, model.square(4), 0.001);
    }

    @Test
    public void testSquareRoot() {
        assertEquals(5.0, model.squareRoot(25), 0.001);
    }

    // Memory tests
    @Test
    public void testMemoryAddAndRecall() {
        model.addToMemory(10.0);
        model.addToMemory(5.0);
        assertEquals(15.0, model.recallMemory(), 0.001);
    }

    @Test
    public void testMemorySubtract() {
        model.addToMemory(20.0);
        model.subtractFromMemory(5.0);
        assertEquals(15.0, model.recallMemory(), 0.001);
    }

    @Test
    public void testMemoryClear() {
        model.addToMemory(10.0);
        model.clearMemory();
        assertEquals(0.0, model.recallMemory(), 0.001);
    }
}
