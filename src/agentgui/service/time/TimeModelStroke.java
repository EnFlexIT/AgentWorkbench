package mas.service.time;

public class TimeModelStroke extends TimeModel {

	private Integer counter = 0;
	
	public TimeModelStroke() {
		this.typeOfTimeModel = TimeModel.STROKE;
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
	
	
	
	
	
	
}
