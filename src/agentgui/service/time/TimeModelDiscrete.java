package mas.service.time;

public class TimeModelDiscrete extends TimeModel {

	private Long time = new Long(0);
	private Long step;
	
	public TimeModelDiscrete(Long stepInTime) {
		this.typeOfTimeModel = TimeModel.DISCRETE_TIME;
		this.step = stepInTime;
	}
	public TimeModelDiscrete(Long startTime, Long stepInTime) {
		this.typeOfTimeModel = TimeModel.DISCRETE_TIME;
		this.time = startTime;
		this.step = stepInTime;
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
