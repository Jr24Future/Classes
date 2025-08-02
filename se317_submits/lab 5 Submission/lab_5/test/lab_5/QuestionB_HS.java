package lab_5;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import lab_5.Part1;

/**
 * Style 1: Single sequence test for humidity.
 * Processes a sequence of 7 humidity values (53, 51, 48, 49, 54, 56, 56)
 * with a constant temperature (70), then verifies final max, min, and trend.
 */
@RunWith(Parameterized.class)
public class QuestionB_HS {

    private int[] humidities;
    private int constantTemperature;

    public QuestionB_HS(int[] humidities, int constantTemperature) {
        this.humidities = humidities;
        this.constantTemperature = constantTemperature;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { new int[] {53, 51, 48, 49, 54, 56, 56}, 70 }
        });
    }

    @Test
    public void testHumiditySequenceStyle1() {
        Part1 sim = new Part1();
        sim.updateSensors(humidities[0], constantTemperature);
        sim.updatePreviousReadings();
        
        for (int i = 1; i < humidities.length; i++) {
            sim.updateSensors(humidities[i], constantTemperature);
            if (i != humidities.length - 1) {
                sim.updatePreviousReadings();
            }
        }
        assertEquals(56, sim.getMaxHumidity());
        assertEquals(48, sim.getMinHumidity());
        assertEquals("Stable", sim.humidityTrend());
    }
}
