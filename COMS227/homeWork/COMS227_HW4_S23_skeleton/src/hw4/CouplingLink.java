package hw4;

import api.Point;
import api.PointPair;
import api.PositionVector;

public class CouplingLink extends AbstractLink{
	
	/**
	 * Constructor for CouplingLink
	 * @param highpoint top endpoint of path 1
	 * @param lowpoint bottom endpoint of path 2
	 */
	public CouplingLink(Point highpoint, Point lowpoint) {
		super(new PointPair[] {new PointPair(highpoint, lowpoint)});
		
	}
}
