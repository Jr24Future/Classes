package hw4;

import api.Point;
import api.PointPair;
import api.PositionVector;

public class TurnLink extends AbstractLink{
    
	/**
	 * Constructor for TurnLink
	 * @param highpoint top endpoint of path 1
	 * @param lowpoint bottom endpoint of path 2
	 * @param lowpoint2 bottom endpoint of path 3
	 */
	public TurnLink(Point highpoint, Point lowpoint, Point lowpoint2) {
		super(new PointPair[] {new PointPair(highpoint, lowpoint), new PointPair(highpoint, lowpoint2)});
		
	}
	
	@Override
	public Point getConnectedPoint(Point point) {
		return (point != getpairs()[1].getPointA()) ? getpairs()[1].getPointA():getpairs()[1].getPointB();
	}	
}
