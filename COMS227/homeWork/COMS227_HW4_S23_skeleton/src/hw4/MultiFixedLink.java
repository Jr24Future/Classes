package hw4;

import api.Point;
import api.PointPair;
import api.PositionVector;


public class MultiFixedLink extends AbstractLink{
	
	/**
	 * Constructor for MultiFixedLink
	 * @param connections given list of PointPairs
	 */
	public MultiFixedLink(PointPair[] connections) {
		super(connections);
	}
}
