package hw4;
/**
 * @author Erroll Barker
 */
import java.util.Arrays;

import api.Crossable;
import api.Path;
import api.Point;
import api.PointPair;
import api.PositionVector;

public abstract class AbstractLink extends java.lang.Object implements Crossable{
	private PointPair[] pairs = null;
	private Boolean A = true;
	private Boolean B = true;
	
	
	/**
	 * This constructor is intended solely for use by classes that inherit from 
	 * this abstract link class. It should not be invoked directly.
	 * 
	 * @param connections   Abstract link requires a list of PointPairs to function 
	 * properly. If this list is not provided in the constructor, the abstract link 
	 * class will create its own list of every possible PointPair. Note that this is 
	 * unnecessary for links that only have a single path.
	 */
	protected AbstractLink(PointPair[] connections) {
		pairs = connections;
		
	}

	@Override
	public void shiftPoints(PositionVector positionVector) {
		Point connectedPoint = this.getConnectedPoint(positionVector.getPointB());
		positionVector.setPointA(connectedPoint.getPath().getLowpoint());
		positionVector.setPointB(connectedPoint.getPath().getPointByIndex(1));
		
	}

	@Override
	public Point getConnectedPoint(Point point) {
		for (PointPair pair : pairs) {
			if (pair.getPointA() == point) {
				return pair.getPointB();
			} else if (pair.getPointB() == point) {
				return pair.getPointA();
			}
		}
		return null;
	}

	@Override
	public void trainEnteredCrossing() {
		// TODO Auto-generated method stub
		//Don't know what it does aka no purpose i guess
	}

	@Override
	public void trainExitedCrossing() {
		// TODO Auto-generated method stub
		//Don't know what it does aka no purpose i guess
	}
	
	/**
	 * This function retrieves the list of stored PointPair values.
	 * 
	 * @return stored PointPair values
	 */
	protected PointPair[] getpairs() {
		return pairs;
	}
	
	@Override
	public int getNumPaths() {
		Path[] pairsL = new Path[] {};
		for (PointPair pair : pairs) {
			if (pairsL.length > 0) {
				for (Path pairs : pairsL) {
					if (pair.getPointA().getPath() == pairs) {
						A = false;
					}
					if (pair.getPointB().getPath() == pairs) {
						B = false;
					}
				}
			}
			if (A) {
				pairsL = Arrays.copyOf(pairsL, pairsL.length + 1);
				pairsL[pairsL.length - 1] = pair.getPointA().getPath();
			}
			if (B) {
				pairsL = Arrays.copyOf(pairsL, pairsL.length + 1);
				pairsL[pairsL.length - 1] = pair.getPointB().getPath();
			}
		}
		return pairsL.length;
	}
}
