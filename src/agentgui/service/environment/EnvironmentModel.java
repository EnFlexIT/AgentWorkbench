package mas.service.environment;

import mas.service.time.TimeModel;

public class EnvironmentModel {

	private Long eventQueuePosition = null;
	private TimeModel timeModel = null;
	private Object environmentObject = null;
	
	
	
	
	
	
	/**
	 * @return the eventQueuePosition
	 */
	public Long getEventQueuePosition() {
		return eventQueuePosition;
	}
	/**
	 * @param eventQueuePosition the eventQueuePosition to set
	 */
	public void setEventQueuePosition(Long eventQueuePosition) {
		this.eventQueuePosition = eventQueuePosition;
	}
	/**
	 * @return the timeModel
	 */
	public TimeModel getTimeModel() {
		return timeModel;
	}
	/**
	 * @param timeModel the timeModel to set
	 */
	public void setTimeModel(TimeModel timeModel) {
		this.timeModel = timeModel;
	}
	/**
	 * @return the environmentObject
	 */
	public Object getEnvironmentObject() {
		return environmentObject;
	}
	/**
	 * @param environmentObject the environmentObject to set
	 */
	public void setEnvironmentObject(Object environmentObject) {
		this.environmentObject = environmentObject;
	}
	
}
