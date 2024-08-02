package api;

/**
 * Class to keep track of a pair of points.
 */
public class PointPair {
	private Point pointA;
	private Point pointB;
	
	public PointPair(Point pointA, Point pointB) {
		this.pointA = pointA;
		this.pointB = pointB;
	}

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
}
