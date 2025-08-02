package lab_5;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import lab_5.Part1;

/**
 * Style 2: Individual tests for temperature.
 * Each test case provides a single temperature value (with constant humidity = 50).
 * Since the simulator is reset for each test, the current, max, and min should equal the input,
 * and the trend should be "N/A" (no previous reading).
 */
@RunWith(Parameterized.class)
public class QuestionB_TI {

    private int temperature;
    private int humidity;

    public QuestionB_TI(int temperature, int humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            {66, 50},
            {68, 50},
            {69, 50},
            {67, 50},
            {63, 50},
            {59, 50},
            {53, 50}
        });
    }

    @Test
    public void testTemperatureIndividual() {
        Part1 sim = new Part1();
        sim.updateSensors(humidity, temperature);
        assertEquals(temperature, sim.getCurrentTemperature());
        assertEquals(temperature, sim.getMaxTemperature());
        assertEquals(temperature, sim.getMinTemperature());
        assertEquals("N/A", sim.temperatureTrend());
    }
}
