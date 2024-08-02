package simulation;

import java.util.ArrayList;
import java.util.Arrays;

import api.CardinalDirection;
import api.Crossable;
import api.Path;
import api.Point;
import hw4.CouplingLink;

public class Track {
	private static final double SCALE = 40;
	private ArrayList<Path> paths;

	public Track() {
		paths = new ArrayList<Path>();
	}
	
	public Path addPathType(double[][] pointsXY, double x, double y, CardinalDirection endADirection, CardinalDirection endBDirection) {
		Point[] points = new Point[pointsXY.length];
		for (int i=0; i<points.length; i++) {
			points[i] = new Point((pointsXY[i][0] + x) * SCALE, (pointsXY[i][1] + y) * SCALE);
		}
		Path path = new Path(points, endADirection, endBDirection);
		for (int i=0; i<points.length; i++) {
			points[i].setPath(path);
			points[i].setPointIndex(i);
		}
		paths.add(path);
		return path;
	}
	
	public void addCouplingLink(Path path1, Path path2) {
		Crossable link = new CouplingLink(path1.getHighpoint(), path2.getLowpoint());
		path1.setHighEndpointLink(link);
		path2.setLowEndpointLink(link);
	}

	public ArrayList<Path> getPaths() {
		return paths;
	}
}
