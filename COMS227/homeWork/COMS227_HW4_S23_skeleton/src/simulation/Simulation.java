package simulation;

import api.Path;
import api.PositionVector;
import api.Train;

public class Simulation {
	private Track track;
	private Train train;
	
	public Simulation() {
		track = new TestTrack1();
		Path path = track.getPaths().get(0);
		PositionVector position = new PositionVector();
		position.setPointA(path.getPointByIndex(0));
		position.setPointB(path.getPointByIndex(1));
		train = new Train(position);
	}
	
	public void update() {
		train.move(0.2);
	}

	public Track getTrack() {
		return track;
	}

	public void setTrack(Track track) {
		this.track = track;
	}

	public Train getTrain() {
		return train;
	}

	public void setTrain(Train train) {
		this.train = train;
	}
}
