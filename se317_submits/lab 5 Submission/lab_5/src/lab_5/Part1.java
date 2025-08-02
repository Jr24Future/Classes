package lab_5;

import java.util.Scanner;

public class Part1 {

    private int currentHumidity;
    private int currentTemperature;
    private int maxHumidity;
    private int minHumidity;
    private int maxTemperature;
    private int minTemperature;
    private int previousHumidity;
    private int previousTemperature;
    
    
    public Part1() {
        reset();
    }
    
    /**
     * Resets max/min and previous sensor values.
     * For humidity: max = 0, min = 100.
     * For temperature: max = 0, min = 150.
     */
    public void reset() {
        maxHumidity = 0;
        minHumidity = 100;
        maxTemperature = 0;
        minTemperature = 150;
        previousHumidity = -1;
        previousTemperature = -1;
    }
    
    /**
     * Updates current sensor values and adjusts max/min values.
     * @param humidity the current humidity (0–100%)
     * @param temperature the current temperature (0–150°F)
     */
    public void updateSensors(int humidity, int temperature) {
        currentHumidity = humidity;
        currentTemperature = temperature;
        
        if (humidity > maxHumidity) {
            maxHumidity = humidity;
        }
        if (humidity < minHumidity) {
            minHumidity = humidity;
        }
        if (temperature > maxTemperature) {
            maxTemperature = temperature;
        }
        if (temperature < minTemperature) {
            minTemperature = temperature;
        }
    }
    
    /**
     * Updates the previous sensor readings.
     */
    public void updatePreviousReadings() {
        previousHumidity = currentHumidity;
        previousTemperature = currentTemperature;
    }
    
    /**
     * Calculates the humidity trend by comparing the current reading with the previous.
     * @return "Increasing", "Decreasing", "Stable", or "N/A" if no previous reading exists.
     */
    public String humidityTrend() {
        if (previousHumidity == -1) return "N/A";
        if (currentHumidity > previousHumidity) return "Increasing";
        if (currentHumidity < previousHumidity) return "Decreasing";
        return "Stable";
    }
    
    /**
     * Calculates the temperature trend by comparing the current reading with the previous.
     * @return "Increasing", "Decreasing", "Stable", or "N/A" if no previous reading exists.
     */
    public String temperatureTrend() {
        if (previousTemperature == -1) return "N/A";
        if (currentTemperature > previousTemperature) return "Increasing";
        if (currentTemperature < previousTemperature) return "Decreasing";
        return "Stable";
    }
    
    /**
     * Determines the humidity status based on the current humidity value.
     * @return "OK" if between 25% and 55%, "High" if above 55%, "Low" if below 25%.
     */
    public String humidityStatus() {
        if (currentHumidity >= 25 && currentHumidity <= 55) return "OK";
        if (currentHumidity > 55) return "High";
        return "Low";
    }
    
    public void displayData() {
        System.out.println("=======================================");
        System.out.println("Current Humidity: " + currentHumidity + "%");
        System.out.println("Maximum Humidity: " + maxHumidity + "%");
        System.out.println("Minimum Humidity: " + minHumidity + "%");
        System.out.println("Humidity Trend: " + humidityTrend());
        System.out.println("Humidity Status: " + humidityStatus());
        System.out.println("---------------------------------------");
        System.out.println("Current Temperature: " + currentTemperature + "°F");
        System.out.println("Maximum Temperature: " + maxTemperature + "°F");
        System.out.println("Minimum Temperature: " + minTemperature + "°F");
        System.out.println("Temperature Trend: " + temperatureTrend());
        System.out.println("=======================================\n");
    }
    
    /**
     * @return the current humidity reading.
     */
    public int getCurrentHumidity() {
        return currentHumidity;
    }
    
    /**
     * @return the current temperature reading.
     */
    public int getCurrentTemperature() {
        return currentTemperature;
    }
    
    /**
     * @return the maximum humidity recorded since last reset.
     */
    public int getMaxHumidity() {
        return maxHumidity;
    }
    
    /**
     * @return the minimum humidity recorded since last reset.
     */
    public int getMinHumidity() {
        return minHumidity;
    }
    
    /**
     * @return the maximum temperature recorded since last reset.
     */
    public int getMaxTemperature() {
        return maxTemperature;
    }
    
    /**
     * @return the minimum temperature recorded since last reset.
     */
    public int getMinTemperature() {
        return minTemperature;
    }
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Part1 system = new Part1();

        System.out.println("Embedded System Simulator");
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
                System.out.println("System reset. Values have been reinitialized.\n");
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
