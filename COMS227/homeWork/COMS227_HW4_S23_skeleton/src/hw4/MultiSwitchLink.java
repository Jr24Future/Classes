package hw4;


import api.Point;
import api.PointPair;
import api.PositionVector;

public class MultiSwitchLink extends AbstractLink{
	 
	/**
	 * This refers to the recorded value indicating which PointPair will be utilized by the train when it passes through this link.
	 */
	private PointPair pairs = null;
	
	/**
	 * Constructor for MultiSwitchLink
	 * @param connections given list of PointPairs
	 */
	public MultiSwitchLink(PointPair[] connections){
		super(connections);
		pairs = connections[0];
	}
	
	/**
	 * When applied to a MultiSwitchLink, this will alter the selection of PointPair to be used by the train as it
	 * traverses the link. However, this action will not affect any other links.
	 * 
	 * @param pointPair This refers to the updated PointPair that will be utilized by the link.
	 * 
	 * @param index gets the point at the given path
	 */
	public void switchConnection(PointPair pointPair, int index) {
		pairs = pointPair;
	}

	

	@Override
	public Point getConnectedPoint(Point point) {
		if (point == pairs.getPointA()) {
			return pairs.getPointB();
		}
		else {
			return pairs.getPointA();
		}
	}
}
