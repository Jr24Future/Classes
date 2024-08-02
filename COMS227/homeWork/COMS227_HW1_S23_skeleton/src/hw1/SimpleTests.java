 package hw1;

/**
 * Some simple tests that may help you to get started implementing the
 * CameraBattery class. Warning: these tests do not cover all of the
 * specifications. Perform your own testing in addition to using the
 * specchecker.
 */
public class SimpleTests {
	
	public static void main(String args[]) {
		CameraBattery cb = new CameraBattery(1000, 2000);
		System.out.println("Test 1:");
		System.out.println("Battery charge is " + cb.getBatteryCharge() + " expected 1000.0");
		System.out.println("Camera charge is " + cb.getCameraCharge() + " expected 0.0");

		// battery is connected to the camera for next tests
		cb.moveBatteryCamera();

		System.out.println();
		System.out.println("Test 2:");
		System.out.println("Battery charge is " + cb.getBatteryCharge() + " expected 1000.0");
		System.out.println("Camera charge is " + cb.getCameraCharge() + " expected 1000.0");

		// before calling drain
		System.out.println();
		System.out.println("Test 3:");
		System.out.println("Total drain is " + cb.getTotalDrain() + " expected 0.0");
		System.out.println("Camera power consumption is " + cb.getCameraPowerConsumption() + " expected " + 1.0);
	
		double drained = cb.drain(100.0);
		System.out.println();
		System.out.println("Test 4:");
		System.out.println("Battery drained by " + drained + " expected 100.0");
		System.out.println("Battery charge is " + cb.getBatteryCharge() + " expected 900.0");
		System.out.println("Camera charge is " + cb.getCameraCharge() + " expected 900.0");
		System.out.println("Total drain is " + cb.getTotalDrain() + " expected 100.0");

		drained = cb.drain(1000.0);
		System.out.println();
		System.out.println("Test 5:");
		System.out.println("Battery drained by " + drained + " expected 900.0");
		System.out.println("Battery charge is " + cb.getBatteryCharge() + " expected 0.0");
		System.out.println("Camera charge is " + cb.getCameraCharge() + " expected 0.0");
		System.out.println("Total drain is " + cb.getTotalDrain() + " expected 1000.0");

		double charged = cb.cameraCharge(50.0);
		System.out.println();
		System.out.println("Test 6:");
		System.out.println("Battery charged by " + charged + " expected 100.0");
		System.out.println("Battery charge is " + cb.getBatteryCharge() + " expected 100.0");
		System.out.println("Camera charge is " + cb.getCameraCharge() + " expected 100.0");

		// battery is removed for the next couple tests
		cb.removeBattery();

		drained = cb.drain(50.0);
		System.out.println();
		System.out.println("Test 7:");
		System.out.println("Battery drained by " + drained + " expected 0.0");
		System.out.println("Battery charge is " + cb.getBatteryCharge() + " expected 100.0");
		System.out.println("Camera charge is " + cb.getCameraCharge() + " expected 0.0");
		System.out.println("Total drain is " + cb.getTotalDrain() + " expected 1000.0");

		cb.cameraCharge(50.0);
		System.out.println();
		System.out.println("Test 8:");
		System.out.println("Battery charge is " + cb.getBatteryCharge() + " expected 100.0");
		System.out.println("Camera charge is " + cb.getCameraCharge() + " expected 0.0");
		System.out.println("Total drain is " + cb.getTotalDrain() + " expected 1000.0");

		// battery is moved to the external charger for the next few tests
		cb.moveBatteryExternal();

		cb.externalCharge(50.0);
		System.out.println();
		System.out.println("Test 9:");
		System.out.println("External charger setting is " + cb.getChargerSetting() + " exptected 0");
		System.out.println("Battery charge is " + cb.getBatteryCharge() + " expected 100.0");
		System.out.println("Camera charge is " + cb.getCameraCharge() + " expected 0.0");
		System.out.println("Total drain is " + cb.getTotalDrain() + " expected 1000.0");
		
		cb.buttonPress();
		cb.buttonPress();
		cb.externalCharge(50.0);
		System.out.println();
		System.out.println("Test 10:");
		System.out.println("External charger setting is " + cb.getChargerSetting() + " exptected 2");
		System.out.println("Battery charge is " + cb.getBatteryCharge() + " expected 300.0");
		System.out.println("Camera charge is " + cb.getCameraCharge() + " expected 0.0");
		System.out.println("Total drain is " + cb.getTotalDrain() + " expected 1000.0");

		cb.buttonPress();
		cb.buttonPress();
		cb.externalCharge(50.0);
		System.out.println();
		System.out.println("Test 11:");
		System.out.println("External charger setting is " + cb.getChargerSetting() + " exptected 0");
		System.out.println("Battery charge is " + cb.getBatteryCharge() + " expected 300.0");
		System.out.println("Camera charge is " + cb.getCameraCharge() + " expected 0.0");
		System.out.println("Total drain is " + cb.getTotalDrain() + " expected 1000.0");

		// battery is moved back to the camera
		cb.moveBatteryCamera();

		drained = cb.drain(1500.0);
		System.out.println();
		System.out.println("Test 12:");
		System.out.println("Battery drained by " + drained + " expected 300.0");
		System.out.println("Battery charge is " + cb.getBatteryCharge() + " expected 0.0");
		System.out.println("Camera charge is " + cb.getCameraCharge() + " expected 0.0");
		System.out.println("Total drain is " + cb.getTotalDrain() + " expected 1300.0");
	}
}