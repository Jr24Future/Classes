package part_b;

public class Transmission {

	
	 private Gear gear;
	   private Moveable moveable;

	   public Transmission(Moveable moveable) {
	      this.moveable = moveable;
	   }

	   public void shift(Gear gear) {
	      // begs for a state-machine implementation
	      if (moveable.currentSpeedInMph() > 0 && gear == Gear.PARK) return; 
	      this.gear = gear;
	   }

	   public Gear getGear() {
	      return gear;
	   }
}
