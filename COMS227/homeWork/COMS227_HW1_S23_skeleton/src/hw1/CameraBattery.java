package hw1;
import java.lang.Math;

public class CameraBattery {
	int button;                                           // the current charger setting 
    double BatteryCharge; //current battery capacity			
	double totalDrain;
	double camPowerConsume;
	double charge;         
    int inCamera;
	int isCharging;
    public static final int NUM_CHARGER_SETTINGS = 4;
    public static final double CHARGE_RATE = 2.0;
    public static final double DEFAULT_CAMERA_POWER_CONSUMPTION = 1.0;
    
    public CameraBattery(double batteryStartingCharge, double batteryCapacity) {
        charge = batteryStartingCharge;
        BatteryCharge = batteryCapacity;
        totalDrain = 0;
        camPowerConsume = DEFAULT_CAMERA_POWER_CONSUMPTION;
        button = 0;
        inCamera = isCharging = 0;                                   // initially not connected 
    }
    public void buttonPress() {
		button++;
        button = button % NUM_CHARGER_SETTINGS;     					// toggle button 
        
    }
    public double cameraCharge(double minutes) {
        double charging_amount = 0; 
		charging_amount = inCamera * Math.min(minutes * CHARGE_RATE, BatteryCharge - charge);   
        charge += charging_amount;        
        return charging_amount;
    }
    public double drain(double minutes) {
        double amount_of_drain = 0;
		amount_of_drain = inCamera * Math.min (minutes * DEFAULT_CAMERA_POWER_CONSUMPTION, charge);
        charge -= amount_of_drain;
        totalDrain += amount_of_drain;
        return amount_of_drain;
    }
    public double externalCharge(double minutes) {
        double battery_external = 0; 
		battery_external = isCharging * Math.min (minutes * button * CHARGE_RATE, BatteryCharge - charge);
        charge += battery_external;        
        return battery_external;
    }
    public void resetBatteryMonitor() {
        totalDrain = 0;
    }
    public double getBatteryCapacity() {
        return BatteryCharge;
    }
    public double getBatteryCharge() {
        return charge;
    }
    public double getCameraCharge() {
		return inCamera * charge;
    }
    public double getCameraPowerConsumption() {
        return camPowerConsume;
    }
    public int getChargerSetting() {
        return button;
    }
    public double getTotalDrain() {
        return totalDrain;
    }
    public void moveBatteryExternal() {
        isCharging = 1;
        inCamera = 0 ;
    }
    public void moveBatteryCamera() {
        inCamera = 1;
        isCharging = 0;
    }
    public void removeBattery() {
        isCharging = Math.max(0,inCamera);
    	inCamera = 0;
    }
    public void setCameraPowerConsumption(double cameraPowerConsumption) {
        camPowerConsume = cameraPowerConsumption;
    }
}
