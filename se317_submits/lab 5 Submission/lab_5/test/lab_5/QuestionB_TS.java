package lab_5;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import lab_5.Part1;

/**
 * Style 1: Single sequence test for temperature.
 * Processes a sequence of 7 temperature values (66, 68, 69, 67, 63, 59, 53)
 * with a constant humidity (50), then verifies final max, min, and trend.
 */
@RunWith(Parameterized.class)
public class QuestionB_TS {

    private int[] temperatures;
    private int constantHumidity;

    public QuestionB_TS(int[] temperatures, int constantHumidity) {
        this.temperatures = temperatures;
        this.constantHumidity = constantHumidity;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { new int[] {66, 68, 69, 67, 63, 59, 53}, 50 }
        });
    }

    @Test
    public void testTemperatureSequenceStyle1() {
        Part1 sim = new Part1();
        sim.updateSensors(constantHumidity, temperatures[0]);
        sim.updatePreviousReadings();
        
        for (int i = 1; i < temperatures.length; i++) {
            sim.updateSensors(constantHumidity, temperatures[i]);
            if (i != temperatures.length - 1) {
                sim.updatePreviousReadings();
            }
        }
        assertEquals(69, sim.getMaxTemperature());
        assertEquals(53, sim.getMinTemperature());
        assertEquals("Decreasing", sim.temperatureTrend());
    }
}
