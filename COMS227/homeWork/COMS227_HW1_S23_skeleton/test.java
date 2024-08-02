package hw1;

public class test {
	private double BatteryCharge = 0; //current battery capacity
	private double CameraCharge = 0;
	private double amount_of_drain;
	private double charging_amount;
	private double battery_external = 0;
	private double BatteryChargeSet;
	private double T_drain;
	private double removeB = 1;
	private double drainR = 0;
	private int button;
	boolean connected = false;
	
	public static final double NUM_CHARGER_SETTINGS = 4;
	public static final double CHARGE_RATE = 2.0;
	public static final double DEFAULT_CAMERA_POWER_CONSUMPTION = 1.0;
	
	
	//
	
	//public CameraBattery(double batteryStartingCharge, double batteryCapacity) {
	//	connected = false;
	//	BatteryCharge = Math.min(batteryStartingCharge, batteryCapacity);
	//}


	public double getBatteryCharge() {
		BatteryCharge = BatteryCharge - amount_of_drain;
		BatteryCharge = Math.max(charging_amount, BatteryCharge);
		BatteryCharge = BatteryCharge + battery_external;
		
		
		
		return BatteryCharge;
	}
	public double getCameraCharge() {
		
		CameraCharge = Math.min(CameraCharge, BatteryCharge);
		CameraCharge = charging_amount + CameraCharge;
		CameraCharge = CameraCharge * removeB;
		
		return CameraCharge;
	}
	
/*	
   public void setBatteryCharge(double batteryStartingCharge) {
		BatteryCharge = batteryStartingCharge;
	}

	public void setCameraCharge(double batteryCapacity) {
		CameraCharge = batteryCapacity;
	}
*/
	
	//
	public double getTotalDrain() {
		amount_of_drain = T_drain;
		amount_of_drain = Math.max(T_drain, drainR);
		
		
		return amount_of_drain;
	}
	public double drain(double minutes) {
		 connected = true;
		 amount_of_drain =  minutes * DEFAULT_CAMERA_POWER_CONSUMPTION;
		 T_drain = amount_of_drain;
		 amount_of_drain = Math.min(amount_of_drain, BatteryCharge);
		 amount_of_drain = amount_of_drain * removeB;
		 return amount_of_drain;
	}
	//
	
	public double cameraCharge(double minutes) {
		connected = true;
		charging_amount = minutes * CHARGE_RATE;
		charging_amount = Math.max(charging_amount, BatteryCharge);
		
	    return charging_amount;
	}
	
	public double externalCharge(double minutes) {
		connected = true;
		BatteryChargeSet = BatteryCharge - BatteryChargeSet;
		battery_external = minutes * button * CHARGE_RATE;
		battery_external = Math.max(battery_external, BatteryChargeSet);
		
		return battery_external;
	}
	
	
	//
	
	public double getCameraPowerConsumption() {
		
		return 1;
	}
	public double getBatteryCapacity() {
		
		return 0;
	}
	
	//
	public int getChargerSetting() {
		
		return button;
	}
	public void buttonPress() {
		button ++;
		button = (int) (button % NUM_CHARGER_SETTINGS);
	}	
	//
	
	
	public void moveBatteryCamera() {
		connected = true;
		removeB = 1;
		CameraCharge = BatteryCharge;
		battery_external = 0;
		charging_amount=0;
		
		
	}
	public void removeBattery() {
		connected = false;
		removeB = 0;
		drainR = 1000;
		
		
		
	}
	public void moveBatteryExternal() {
		connected = true;
		BatteryCharge = 0;
		
		
	}
	public void resetBatteryMonitor() {
		
	}
	public void setCameraPowerConsumption(double cameraPowerConsumption) {
		
	}
		
	
	

	
}