package api;

/**
 * Class that represents a train at a particular PositionVector.
 */
public class Train {
	private PositionVector position;

	public Train(PositionVector position) {
		this.position = position;
	}

	public void move(double distance) {
		double currentDistance = position.getRelativeDistance();
		if (currentDistance + distance > 1.0) {
			Path path = position.getPointB().getPath();
			path.shiftPoints(position);
		} else {
			position.setRelativeDistance(currentDistance + distance);
		}
	}

	public PositionVector getPosition() {
		return position;
	}
}
