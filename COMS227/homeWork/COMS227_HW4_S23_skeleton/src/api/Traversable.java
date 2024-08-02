package api;

public interface Traversable {
	/**
	 * Shift the location of the given positionVector to be between the next pair of
	 * points.
	 * <p>
	 * For example, suppose the vector is currently at the end of path 1 on points A
	 * and B. Assume endpoint B on path 1 is linked to endpoint C on path 2. Then
	 * the positionVector will be updated to be between points C and D, where D is
	 * the next point on path 2 after C.
	 * <p>
	 * The method does not change the relative distance between the points, it only
	 * modifies the points.
	 */
	public void shiftPoints(PositionVector positionVector);
}
