package lab_5;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import lab_5.Part1;

/**
 * Style 2: Individual tests for humidity.
 * Each test case provides a single humidity value (temperature = 70).
 * Since the simulator is reset for each test, current, max, and min should equal the input,
 * and the trend should be "N/A" (no previous reading).
 */
@RunWith(Parameterized.class)
public class QuestionB_HI {

    private int humidity;
    private int temperature;

    public QuestionB_HI(int humidity, int temperature) {
        this.humidity = humidity;
        this.temperature = temperature;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {53, 70},
            {51, 70},
            {48, 70},
            {49, 70},
            {54, 70},
            {56, 70},
            {56, 70}
        });
    }

    @Test
    public void testHumidityIndividual() {
        Part1 sim = new Part1();
        sim.updateSensors(humidity, temperature);
        assertEquals(humidity, sim.getCurrentHumidity());
        assertEquals(humidity, sim.getMaxHumidity());
        assertEquals(humidity, sim.getMinHumidity());
        assertEquals("N/A", sim.humidityTrend());
    }
}
