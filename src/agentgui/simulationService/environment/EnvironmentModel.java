package agentgui.simulationService.environment;

import java.io.Serializable;

import agentgui.simulationService.time.TimeModel;


public class EnvironmentModel implements Serializable {

	private static final long serialVersionUID = -2845036237763599630L;
	
	private TimeModel timeModel = null;
	private Object abstractEnvironment = null;
	private Object displayEnvironment	= null;	
	
	/**
	 * Returns true if nothing is set yet (e.g. timeModel, abstractEnvironment or displayEnvironment)
	 * @return
	 */
	public boolean isEmpty() {
		if (timeModel==null && abstractEnvironment==null && displayEnvironment==null) {
			return true;
		} else {
			return false;	
		}	
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
	public Object getAbstractEnvironment() {
		return abstractEnvironment;
	}
	/**
	 * @param environmentObject the environmentObject to set
	 */
	public void setAbstractEnvironment(Object newAbstractEnvironment) {
		this.abstractEnvironment = newAbstractEnvironment;
	}
	/**
	 * 
	 * @return
	 */
	public Object getDisplayEnvironment() {
		return displayEnvironment;
	}
	/**
	 * 
	 * @param displayEnvironment
	 */
	public void setDisplayEnvironment(Object displayEnvironment) {
		this.displayEnvironment = displayEnvironment;
	}

}
