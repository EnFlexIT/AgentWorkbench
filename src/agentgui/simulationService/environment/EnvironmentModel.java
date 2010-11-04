package agentgui.simulationService.environment;

import java.io.Serializable;

import agentgui.simulationService.time.TimeModel;


public class EnvironmentModel implements Serializable {

	private static final long serialVersionUID = -2845036237763599630L;
	
	private TimeModel timeModel = null;
	private Object environmentInstance = null;
	
	
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
	public Object getEnvironmentInstance() {
		return environmentInstance;
	}
	/**
	 * @param environmentObject the environmentObject to set
	 */
	public void setEnvironmentInstance(Object currEnvironmentInstance) {
		this.environmentInstance = currEnvironmentInstance;
	}
	
}
