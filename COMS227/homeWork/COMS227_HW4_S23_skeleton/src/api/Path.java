package api;

/**
 * Models a small section of a train track called a path. A path is made up of n
 * ordered points indexed from 0 to n-1. A path has exactly two ends at point
 * index 0 and point index n-1. The cardinal direction (north, south, west, and
 * east) in which the endpoints exit is labeled as the low endpoint exit
 * direction for point 0 and the high endpoint exit direction for point n-1.
 */
public class Path implements Traversable {
	private Point[] points;
	private CardinalDirection lowEndpointExitDirection;
	private CardinalDirection highEndpointExitDirection;
	private Crossable lowEndpointLink;
	private Crossable highEndpointLink;

	public Path(Point[] points, CardinalDirection lowEndpointExitDirection,
			CardinalDirection highEndpointExitDirection) {
		this.points = points;
		this.lowEndpointExitDirection = lowEndpointExitDirection;
		this.highEndpointExitDirection = highEndpointExitDirection;
	}

	@Override
	public void shiftPoints(PositionVector positionVector) {
		Point pointA = positionVector.getPointA();
		Point pointB = positionVector.getPointB();

		// find the direction of travel
		int travel = 0;
		if (pointA.getPointIndex() < pointB.getPointIndex()) {
			travel = 1;
		} else {
			travel = -1;
		}

		int updatedPointBIndex = pointB.getPointIndex() + travel;
		if (updatedPointBIndex < 0) {
			// we have reached the low end of the path, use the link to shift the points to
			// the next path
			if (lowEndpointLink != null) {
				lowEndpointLink.shiftPoints(positionVector);
			}
		} else if (updatedPointBIndex >= points.length) {
			// we have reached the high end of the path, use the link to shift the points to
			// the next path
			if (highEndpointLink != null) {
				highEndpointLink.shiftPoints(positionVector);
			}
		} else {
			// we have not reached and endpoint, update the points from this path
			positionVector.setPointA(pointB);
			positionVector.setPointB(points[updatedPointBIndex]);
		}
	}

	public Point getPointByIndex(int index) {
		return points[index];
	}

	public CardinalDirection getLowEndpointExitDirection() {
		return lowEndpointExitDirection;
	}

	public CardinalDirection getHighEndpointExitDirection() {
		return highEndpointExitDirection;
	}

	public int getNumPoints() {
		return points.length;
	}

	public Crossable getLowEndpointLink() {
		return lowEndpointLink;
	}
	
	public Point getLowpoint() {
		return points[0];
	}
	
	public Point getHighpoint() {
		return points[points.length-1];
	}

	public void setLowEndpointLink(Crossable lowEndpointLink) {
		this.lowEndpointLink = lowEndpointLink;
	}

	public Crossable getHighEndpointLink() {
		return highEndpointLink;
	}

	public void setHighEndpointLink(Crossable highEndpointLink) {
		this.highEndpointLink = highEndpointLink;
	}
}
