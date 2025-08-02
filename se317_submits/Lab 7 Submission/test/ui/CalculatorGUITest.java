package ui;

import controller.CalculatorController;
import model.CalculatorModel;
import view.CalculatorView;

import static org.junit.Assert.assertTrue;

import org.assertj.swing.core.BasicRobot;
import org.assertj.swing.core.Robot;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CalculatorGUITest {

    private FrameFixture window;
    private Robot robot;

    @Before
    public void setUp() {
        robot = BasicRobot.robotWithNewAwtHierarchy();
        robot.settings().delayBetweenEvents(100);

        CalculatorModel model = new CalculatorModel();
        CalculatorView view = new CalculatorView();
        new CalculatorController(model, view);

        window = new FrameFixture(robot, view);
        window.show();
    }

    @After
    public void tearDown() {
        window.cleanUp();
    }

    // TR1: Functional Operations
    @Test public void testAdditionSimple() {
        window.button("1").click(); window.button("2").click(); window.button("+").click();
        window.button("3").click(); window.button("4").click(); window.button("=").click();
        window.textBox("display").requireText("46.0");
    }

    @Test public void testSubtraction() {
        window.button("9").click(); window.button("0").click(); window.button("-").click();
        window.button("2").click(); window.button("5").click(); window.button("=").click();
        window.textBox("display").requireText("65.0");
    }

    @Test public void testMultiplication() {
        window.button("7").click(); window.button("*").click();
        window.button("6").click(); window.button("=").click();
        window.textBox("display").requireText("42.0");
    }

    @Test public void testDivision() {
        window.button("8").click(); window.button("4").click(); window.button("/").click();
        window.button("2").click(); window.button("1").click(); window.button("=").click();
        window.textBox("display").requireText("4.0");
    }

    @Test public void testNegativeNumberSquareRoot() {
        window.button("9").click(); window.button("+/-").click(); window.button("\u221a").click();
        window.textBox("display").requireText("Error");
    }

    @Test public void testSquareFunction() {
        window.button("5").click(); window.button("x²").click();
        window.textBox("display").requireText("25.0");
    }

    // TR2: Operand and Result Display Only
    @Test public void testOperandOnlyDisplayFirst() {
        window.button("1").click(); window.button("2").click(); window.button("3").click();
        window.button("+").click();
        window.textBox("display").requireText("123");
    }

    @Test public void testOperandOnlyDisplaySecond() {
        window.button("4").click(); window.button("5").click(); window.button("+").click();
        window.button("6").click(); window.button("7").click();
        window.textBox("display").requireText("67");
    }

    @Test public void testOnlyResultOnEquals() {
        window.button("8").click(); window.button("9").click(); window.button("+").click();
        window.button("1").click(); window.button("1").click(); window.button("=").click();
        window.textBox("display").requireText("100.0");
    }

    // TR3: Operation Button State Change
    @Test public void testOperationPersistsUntilEquals() {
        window.button("3").click(); window.button("4").click(); window.button("+").click();
        window.button("5").click(); window.button("=").click();
        window.textBox("display").requireText("39.0");
    }

    @Test public void testOperationChangeSign() {
        window.button("1").click(); window.button("0").click(); window.button("+").click();
        window.button("5").click(); window.button("+/-").click(); window.button("=").click();
        window.textBox("display").requireText("5.0");
    }
    
    @Test public void testOperationChangeMidway() {
        window.button("1").click(); window.button("0").click(); window.button("+").click();
        window.button("5").click(); window.button("+/-").click(); window.button("=").click();
        window.textBox("display").requireText("5.0");
    }

    @Test public void testClearRemovesOperationState() {
        window.button("9").click(); window.button("+").click(); window.button("C").click();
        window.button("2").click(); window.button("=").click();
        window.textBox("display").requireText("Error");
    }
    
    @Test
    public void testChainedCalculations() {
        // 12 + 8 =
        window.button("1").click();
        window.button("2").click();
        window.button("+").click();
        window.button("8").click();
        window.button("=").click();
        window.textBox("display").requireText("20.0");

        // * 3 =
        window.button("*").click();
        window.button("3").click();
        window.button("=").click();
        window.textBox("display").requireText("60.0");

        // - 10 =
        window.button("-").click();
        window.button("1").click();
        window.button("0").click();
        window.button("=").click();
        window.textBox("display").requireText("50.0");
    }
    
    @Test
    public void testFunctionChainedOperations() {
        // 5 x²
        window.button("5").click();
        window.button("x²").click();
        window.textBox("display").requireText("25.0");

        // + 20 =
        window.button("+").click();
        window.button("2").click();
        window.button("0").click();
        window.button("=").click();
        window.textBox("display").requireText("45.0");

        // √
        window.button("√").click();
        String result = window.textBox("display").text();
        double value = Double.parseDouble(result);
        assertTrue(Math.abs(value - 6.708) < 0.01); // Acceptable floating point tolerance
    }
    
    @Test
    public void testChainWithAllButtons() {
        // 12 + 8 =
        window.button("1").click();
        window.button("2").click();
        window.button("+").click();
        window.button("8").click();
        window.button("=").click();
        window.textBox("display").requireText("20.0");

        // M+ (store 20)
        window.button("M+").click();

        // × 3 =
        window.button("*").click();
        window.button("3").click();
        window.button("=").click();
        window.textBox("display").requireText("60.0");

        // M+ again (add 60 → memory = 80)
        window.button("M+").click();

        // - 10 =
        window.button("-").click();
        window.button("1").click();
        window.button("0").click();
        window.button("=").click();
        window.textBox("display").requireText("50.0");

        // M- (subtract 50 → memory = 30)
        window.button("M-").click();

        // MR (recall memory = 30)
        window.button("MR").click();
        window.textBox("display").requireText("30.0");

        // x²
        window.button("x²").click();
        window.textBox("display").requireText("900.0");

        // Clear everything
        window.button("C").click();
        window.textBox("display").requireText("");

        // Try a misclick (tap "+" before anything)
        window.button("+").click();
        window.button("2").click();
        window.button("=").click();
        window.textBox("display").requireText("Error");

        // Clear again
        window.button("C").click();

        // 7 √ =
        window.button("7").click();
        window.button("√").click();
        String sqrtResult = window.textBox("display").text();
        double value = Double.parseDouble(sqrtResult);
        assertTrue(Math.abs(value - Math.sqrt(7)) < 0.01);

        // Final: MR + 1 = (memory was 30, expect 31)
        window.button("MR").click();
        window.button("+").click();
        window.button("1").click();
        window.button("=").click();
        window.textBox("display").requireText("31.0");

        // Clear memory
        window.button("MC").click();
        window.button("MR").click();
        window.textBox("display").requireText("0.0");
    }
}

    

