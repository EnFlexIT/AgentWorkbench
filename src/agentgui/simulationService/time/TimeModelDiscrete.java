package agentgui.simulationService.time;

import jade.util.leap.Serializable;


public class TimeModelDiscrete extends TimeModel implements Serializable {

	private static final long serialVersionUID = 3931340225354221294L;
	
	private Long time = new Long(0);
	private Long step;
	
	public TimeModelDiscrete(Long stepInTime) {
		this.step = stepInTime;
	}
	public TimeModelDiscrete(Long stepInTime, Long startTime) {
		this.step = stepInTime;
		this.time = startTime;			
	}
	
	/**
	 * This will forward the simulation-counter incrementing one step
	 */
	public void step() {
		this.time = this.time + step;
	}
	/**
	 * @param counter the counter to set
	 */
	public void setTime(Long counter) {
		this.time = counter;
	}
	/**
	 * @return the counter
	 */
	public Long getTime() {
		return time;
	}
}