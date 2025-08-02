package lab_5;

/**
 * SensorData encapsulates the logic for tracking sensor readings.
 * It maintains the current value, maximum, and minimum values, and calculates
 * the trend based on the previous reading.
 *
 * This class is used for both humidity and temperature to eliminate duplicate logic.
 */
public class QuestionC_Helper {
    private int currentValue;
    private int maxValue;
    private int minValue;
    private int previousValue;

    /**
     * Constructor initializes the sensor data.
     * @param initialMax the initial max value
     * @param initialMin the initial min value 
     */
    public QuestionC_Helper(int initialMax, int initialMin) {
        this.maxValue = initialMax;
        this.minValue = initialMin;
        this.previousValue = -1; // -1 indicates no previous reading.
    }

    /**
     * Updates the sensor reading. Adjusts the max and min values accordingly.
     * @param value the new sensor reading.
     */
    public void update(int value) {
        if (previousValue == -1) {
            previousValue = value;
        }
        currentValue = value;
        if (value > maxValue) {
            maxValue = value;
        }
        if (value < minValue) {
            minValue = value;
        }
    }

    /**
     * Calculates the trend based on the difference between the current and previous values.
     * @return "Increasing", "Decreasing", "Stable", or "N/A" if no previous reading exists.
     */
    public String getTrend() {
        if (previousValue == -1) {
            return "N/A";
        }
        if (currentValue > previousValue) {
            return "Increasing";
        } else if (currentValue < previousValue) {
            return "Decreasing";
        } else {
            return "Stable";
        }
    }

    /**
     * Updates the previous value to be the current value.
     * Should be called after processing a reading.
     */
    public void updatePrevious() {
        previousValue = currentValue;
    }

    public int getCurrent() {
        return currentValue;
    }
    
    public int getMax() {
        return maxValue;
    }
    
    public int getMin() {
        return minValue;
    }
}
