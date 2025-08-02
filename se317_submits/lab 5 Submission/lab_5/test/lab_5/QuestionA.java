package lab_5;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lab_5.Part1;

/**
 * Test class for EmbeddedSystemSimulator.
 *
 * Test Criteria & TR Set:
 * TR1: Current sensor reading must equal the last input.
 * TR2: Maximum sensor reading is the highest among inputs since the last reset.
 * TR3: Minimum sensor reading is the lowest among inputs since the last reset.
 * TR4: Trend is calculated correctly:
 *      - "Increasing" if the second reading is greater than the first,
 *      - "Decreasing" if lower, and "Stable" if equal.
 * TR5: Humidity status is "OK" if 25% <= humidity <= 55%, "High" if above 55%, "Low" if below 25%.
 * TR6: Reset returns the internal max/min values to defaults (humidity: max=0, min=100; temperature: max=0, min=150).
 *
 * Total Test Cases: Approximately 10.
 */
public class QuestionA {

    private Part1 system;

    @BeforeEach
    void setUp() {
        system = new Part1();
    }

    // ----- Test Current Sensor Values (TR1) -----
    @Test
    void testCurrentSensorValues_Case1() {
        system.updateSensors(30, 70);
        assertEquals(30, system.getCurrentHumidity(), "Current humidity should be 30");
        assertEquals(70, system.getCurrentTemperature(), "Current temperature should be 70");
    }

    @Test
    void testCurrentSensorValues_Case2() {
        system.updateSensors(55, 90);
        assertEquals(55, system.getCurrentHumidity(), "Current humidity should be 55");
        assertEquals(90, system.getCurrentTemperature(), "Current temperature should be 90");
    }

    // ----- Test Maximum and Minimum Values (TR2 & TR3) -----
    @Test
    void testMaxMinValues_Case1() {
        system.updateSensors(40, 70);
        system.updatePreviousReadings();
        system.updateSensors(55, 85);
        assertEquals(55, system.getMaxHumidity(), "Max humidity should be 55");
        assertEquals(40, system.getMinHumidity(), "Min humidity should be 40");
        assertEquals(85, system.getMaxTemperature(), "Max temperature should be 85");
        assertEquals(70, system.getMinTemperature(), "Min temperature should be 70");
    }

    @Test
    void testMaxMinValues_Case2() {
        system.updateSensors(50, 80);
        system.updatePreviousReadings();
        system.updateSensors(45, 75);
        assertEquals(50, system.getMaxHumidity(), "Max humidity should be 50");
        assertEquals(45, system.getMinHumidity(), "Min humidity should be 45");
        assertEquals(80, system.getMaxTemperature(), "Max temperature should be 80");
        assertEquals(75, system.getMinTemperature(), "Min temperature should be 75");
    }

    // ----- Test Trend Calculation (TR4) -----
    @Test
    void testHumidityTrend_Increasing() {
        system.updateSensors(30, 70);
        system.updatePreviousReadings();
        system.updateSensors(40, 70);
        assertEquals("Increasing", system.humidityTrend(), "Humidity trend should be Increasing");
    }

    @Test
    void testTemperatureTrend_Decreasing() {
        system.updateSensors(50, 90);
        system.updatePreviousReadings();
        system.updateSensors(50, 80);
        assertEquals("Decreasing", system.temperatureTrend(), "Temperature trend should be Decreasing");
    }

    // ----- Test Humidity Status (TR5) -----
    @Test
    void testHumidityStatus_OK() {
        system.updateSensors(30, 70);  
        assertEquals("OK", system.humidityStatus(), "Humidity status should be OK for 30%");
    }

    @Test
    void testHumidityStatus_High() {
        system.updateSensors(60, 70); 
        assertEquals("High", system.humidityStatus(), "Humidity status should be High for 60%");
    }

    @Test
    void testHumidityStatus_Low() {
        system.updateSensors(20, 70); 
        assertEquals("Low", system.humidityStatus(), "Humidity status should be Low for 20%");
    }

    // ----- Test Reset Functionality (TR6) -----
    @Test
    void testResetFunctionality() {
        system.updateSensors(40, 70);
        system.updatePreviousReadings();
        system.updateSensors(55, 85);
        assertEquals(55, system.getMaxHumidity(), "Max humidity should be 55 before reset");
        system.reset();
        assertEquals(0, system.getMaxHumidity(), "Max humidity should be 0 after reset");
        assertEquals(100, system.getMinHumidity(), "Min humidity should be 100 after reset");
        assertEquals(0, system.getMaxTemperature(), "Max temperature should be 0 after reset");
        assertEquals(150, system.getMinTemperature(), "Min temperature should be 150 after reset");
    }
}
