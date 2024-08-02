package simulation;

import static api.CardinalDirection.EAST;
import static api.CardinalDirection.WEST;

import api.Crossable;
import api.Path;
import hw4.DeadEndLink;

public class TestTrack1 extends Track {
	public TestTrack1() {
		Path path = addPathType(PathTypes.pathType5, 5, 5, WEST, EAST);
		Crossable link = new DeadEndLink();
		path.setHighEndpointLink(link);
	}
}
