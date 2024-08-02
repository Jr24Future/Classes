package api;

/**
 * Class that indicates a location between two points on a path.
 * The direction of travel is from pointA to pointB.
 */
public class PositionVector {
	/**
	 * The source point.
	 */
	private Point pointA;
	/**
	 * The destination point.
	 */
	private Point pointB;
	/**
	 * Distance between point A and point B and a value between 0 and 1.
	 */
	private double relativeDistance;

	public Point getPointA() {
		return pointA;
	}

	public void setPointA(Point pointA) {
		this.pointA = pointA;
	}

	public Point getPointB() {
		return pointB;
	}

	public void setPointB(Point pointB) {
		this.pointB = pointB;
	}

	public double getRelativeDistance() {
		return relativeDistance;
	}

	public void setRelativeDistance(double relativeDistance) {
		this.relativeDistance = relativeDistance;
	}
}
