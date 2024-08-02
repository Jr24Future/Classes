package hw4;


import api.Point;
import api.PointPair;
import api.PositionVector;

public class SwitchLink extends AbstractLink{
	
	/**
	 * By default, the recorded state of the train's turning as it traverses this link is false.
	 * 
	 */
    private boolean turn;
    
    /**
     * Constructor for SwitchLink
     * @param highpoint top endpoint of path 1
     * @param lowpoint bottom endpoint of path 2
     * @param lowpoint2 bottom endpoint of path 3
     */
    
    
	public SwitchLink(Point highpoint, Point lowpoint, Point lowpoint2) {
		super(new PointPair[] {new PointPair(highpoint, lowpoint), new PointPair(highpoint, lowpoint2)});
        turn = false;
	}
	
	/**
	 * This will determine whether the train will utilize the primary PointPair or the secondary PointPair.
	 * @param turn If the value is true, the train will be instructed to turn, and if the value is false, the train will be instructed not to turn.
	 */
	public void setTurn(boolean turn) {
		this.turn = turn;
	}

	
	@Override
	public Point getConnectedPoint(Point point) {
		if (turn) {
			return (point != getpairs()[1].getPointA()) ? getpairs()[1].getPointA():getpairs()[1].getPointB();
		}
		else {
			return (point != getpairs()[0].getPointA()) ? getpairs()[0].getPointA():getpairs()[0].getPointB();
		}
	}
}


