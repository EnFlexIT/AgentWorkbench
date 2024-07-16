package de.enflexit.awb.remoteControl;

import java.io.Serializable;

/**
 * A set of configuration settings that can be configured using the remote control API.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class AwbSimulationSettings implements Serializable {
	
	private static final long serialVersionUID = 8230835452827168169L;
	
	public enum TimeModel{
		CONTINUOUS, DISCRETEM
	}
	
	private TimeModel timeModel;
	private long simulationStartTime;
	private long simulationEndTime;
	private long simulationStepSeconds;
	
	/**
	 * Gets the time model.
	 * @return the time model
	 */
	public TimeModel getTimeModel() {
		return timeModel;
	}
	/**
	 * Sets the time model.
	 * @param timeModel the new time model
	 */
	public void setTimeModel(TimeModel timeModel) {
		this.timeModel = timeModel;
	}
	
	/**
	 * Gets the simulation start time.
	 * @return the simulation start time
	 */
	public long getSimulationStartTime() {
		return simulationStartTime;
	}
	
	/**
	 * Sets the simulation start time.
	 * @param simulationStartTime the new simulation start time
	 */
	public void setSimulationStartTime(long simulationStartTime) {
		this.simulationStartTime = simulationStartTime;
	}
	
	/**
	 * Gets the simulation end time.
	 * @return the simulation end time
	 */
	public long getSimulationEndTime() {
		return simulationEndTime;
	}
	
	/**
	 * Sets the simulation end time.
	 * @param simulationEndTime the new simulation end time
	 */
	public void setSimulationEndTime(long simulationEndTime) {
		this.simulationEndTime = simulationEndTime;
	}
	
	/**
	 * Gets the simulation step seconds.
	 * @return the simulation step seconds
	 */
	public long getSimulationStepSeconds() {
		return simulationStepSeconds;
	}
	
	/**
	 * Sets the simulation step seconds.
	 * @param simulationStepSeconds the new simulation step seconds
	 */
	public void setSimulationStepSeconds(long simulationStepSeconds) {
		this.simulationStepSeconds = simulationStepSeconds;
	}
	
}
