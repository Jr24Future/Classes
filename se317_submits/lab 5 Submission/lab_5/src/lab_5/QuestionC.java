package lab_5;

import java.util.Scanner;

/**
 * EmbeddedSystemSimulatorRefactored uses QuestionC_Helper to handle both humidity and temperature calculations.
 * By reusing the QuestionC_Helper class, the common algorithms for calculating current, max, min, and trend values
 * are centralized, reducing duplication and making testing easier.
 */
public class QuestionC {

    private QuestionC_Helper humidityData;
    private QuestionC_Helper temperatureData;

    /**
     * Constructor initializes sensor data with default values.
     * For humidity, valid range: 0–100 (max=0, min=100).
     * For temperature, valid range: 0–150 (max=0, min=150).
     */
    public QuestionC() {
        humidityData = new QuestionC_Helper(0, 100);
        temperatureData = new QuestionC_Helper(0, 150);
    }

    /**
     * Resets the sensor data.
     */
    public void reset() {
        humidityData = new QuestionC_Helper(0, 100);
        temperatureData = new QuestionC_Helper(0, 150);
    }

    /**
     * Updates sensor values.
     * @param humidity the current humidity (0–100%).
     * @param temperature the current temperature (0–150°F).
     */
    public void updateSensors(int humidity, int temperature) {
        humidityData.update(humidity);
        temperatureData.update(temperature);
    }

    /**
     * Updates previous readings for both sensors.
     */
    public void updatePreviousReadings() {
        humidityData.updatePrevious();
        temperatureData.updatePrevious();
    }

    public String humidityTrend() {
        return humidityData.getTrend();
    }

    public String temperatureTrend() {
        return temperatureData.getTrend();
    }

    /**
     * Determines the humidity status based on the current humidity value.
     * @return "OK" if humidity is between 25% and 55%, "High" if above 55%, "Low" if below 25%.
     */
    public String humidityStatus() {
        int h = humidityData.getCurrent();
        if (h >= 25 && h <= 55) {
            return "OK";
        } else if (h > 55) {
            return "High";
        } else {
            return "Low";
        }
    }

    // Getter methods for testing and display.

    public int getCurrentHumidity() {
        return humidityData.getCurrent();
    }
    
    public int getMaxHumidity() {
        return humidityData.getMax();
    }
    
    public int getMinHumidity() {
        return humidityData.getMin();
    }
    
    public int getCurrentTemperature() {
        return temperatureData.getCurrent();
    }
    
    public int getMaxTemperature() {
        return temperatureData.getMax();
    }
    
    public int getMinTemperature() {
        return temperatureData.getMin();
    }

    public void displayData() {
        System.out.println("=======================================");
        System.out.println("Current Humidity: " + humidityData.getCurrent() + "%");
        System.out.println("Maximum Humidity: " + humidityData.getMax() + "%");
        System.out.println("Minimum Humidity: " + humidityData.getMin() + "%");
        System.out.println("Humidity Trend: " + humidityData.getTrend());
        System.out.println("Humidity Status: " + humidityStatus());
        System.out.println("---------------------------------------");
        System.out.println("Current Temperature: " + temperatureData.getCurrent() + "°F");
        System.out.println("Maximum Temperature: " + temperatureData.getMax() + "°F");
        System.out.println("Minimum Temperature: " + temperatureData.getMin() + "°F");
        System.out.println("Temperature Trend: " + temperatureData.getTrend());
        System.out.println("=======================================\n");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        QuestionC system = new QuestionC();

        System.out.println("Refactored Embedded System Simulator");
        System.out.println("Type 'r' to reset, 'q' to quit, or any other key to provide sensor input.");

        while (true) {
            System.out.print("Enter command: ");
            String command = sc.next();
            if (command.equalsIgnoreCase("q")) {
                System.out.println("Exiting simulation...");
                break;
            }
            if (command.equalsIgnoreCase("r")) {
                system.reset();
                System.out.println("System reset. Values reinitialized.\n");
                continue;
            }
            System.out.print("Enter current humidity (0-100%): ");
            int humidity = sc.nextInt();
            System.out.print("Enter current temperature (0-150°F): ");
            int temperature = sc.nextInt();

            system.updateSensors(humidity, temperature);
            system.displayData();
            system.updatePreviousReadings();
        }
        sc.close();
    }
}
