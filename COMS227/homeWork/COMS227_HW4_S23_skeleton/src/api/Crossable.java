package api;

/**
 * An interface that must be implemented by all types of links.
 */
public interface Crossable extends Traversable {
	/**
	 * Gets the point that is connected to the given point by the link. Returns null
	 * if no point is connected to the given point.
	 * 
	 * @param point the given point
	 * @return the connected point or null
	 */
	public Point getConnectedPoint(Point point);

	/**
	 * This method is called by the simulation to indicate a train has entered the
	 * crossing.
	 */
	public void trainEnteredCrossing();

	/**
	 * This method is called by the simulation to indicate a train has exited the
	 * crossing.
	 */
	public void trainExitedCrossing();

	/**
	 * Gets the total number of paths connected by the link.
	 * 
	 * @return the total number of paths
	 */
	public int getNumPaths();
}
