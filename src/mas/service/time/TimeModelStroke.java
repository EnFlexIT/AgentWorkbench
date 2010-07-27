package mas.service.time;

import jade.util.leap.Serializable;



public class TimeModelStroke extends TimeModel implements Serializable {

	private static final long serialVersionUID = -63223704339241994L;

	private Integer counter = 0;
	
	public TimeModelStroke() {
	}

	/**
	 * This will forward the simulation-counter incrementing one step
	 */
	public void step() {
		counter++;
	}
	/**
	 * @param counter the counter to set
	 */
	public void setCounter(Integer counter) {
		this.counter = counter;
	}
	/**
	 * @return the counter
	 */
	public Integer getCounter() {
		return counter;
	}
} // --- End of Sub-Class -----