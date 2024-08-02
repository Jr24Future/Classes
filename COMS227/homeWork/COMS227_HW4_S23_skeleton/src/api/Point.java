package api;

/**
 * Models a point which has an x and y coordinate and also knows its order in a
 * path.
 */
public class Point {
	private Path path;
	private int pointIndex;
	private double x;
	private double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Path getPath() {
		return path;
	}

	public int getPointIndex() {
		return pointIndex;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public void setPointIndex(int pointIndex) {
		this.pointIndex = pointIndex;
	}
}
