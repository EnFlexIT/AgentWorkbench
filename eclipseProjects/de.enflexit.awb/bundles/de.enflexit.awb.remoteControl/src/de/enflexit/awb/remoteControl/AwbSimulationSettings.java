package de.enflexit.awb.remoteControl;

import java.io.Serializable;

public class AwbSimulationSettings implements Serializable {
	
	private static final long serialVersionUID = 8230835452827168169L;
	
	public enum TimeModel{
		CONTINUOUS, DISCRETEM
	}
	
	private TimeModel timeModel;
	private long timeStepFrom;
	private long timeStepTo;
	private long stepLengthSeconds;
	
	public TimeModel getTimeModel() {
		return timeModel;
	}
	public void setTimeModel(TimeModel timeModel) {
		this.timeModel = timeModel;
	}
	public long getTimeStepFrom() {
		return timeStepFrom;
	}
	public void setTimeStepFrom(long timeStepFrom) {
		this.timeStepFrom = timeStepFrom;
	}
	public long getTimeStepTo() {
		return timeStepTo;
	}
	public void setTimeStepTo(long timeStepTo) {
		this.timeStepTo = timeStepTo;
	}
	public long getStepLengthSeconds() {
		return stepLengthSeconds;
	}
	public void setStepLengthSeconds(long stepLengthSeconds) {
		this.stepLengthSeconds = stepLengthSeconds;
	}
	
}
