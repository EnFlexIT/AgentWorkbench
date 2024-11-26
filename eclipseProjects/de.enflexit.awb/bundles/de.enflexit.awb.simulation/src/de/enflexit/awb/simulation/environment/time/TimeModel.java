package de.enflexit.awb.simulation.environment.time;

import java.util.HashMap;


import jade.util.leap.Serializable;

/**
 * This is the abstract base class for any time model.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class TimeModel implements Serializable {

	private static final long serialVersionUID = 4597561080133786915L;
	
	private StopWatch stopWatch = null;
	
	/**
	 * Instantiates a new time model.
	 */
	public TimeModel() { }

	/**
	 * Steps the TimeModel.
	 */
	public abstract void step();
	/**
	 * Steps the TimeModel.
	 */
	public abstract void stepBack();
	
	/**
	 * Sets the stopwatch.
	 * @param stopWatch the new stopwatch
	 */
	public void setStopWatch(StopWatch stopWatch) {
		this.stopWatch = stopWatch;
	}
	/**
	 * Returns the current or a new stopwatch.
	 * @return the stop watch
	 */
	public StopWatch getStopWatch() {
		if (this.stopWatch==null) {
			this.stopWatch = new StopWatch();
		}
		return stopWatch;
	}
		
	/**
	 * Returns a copy of the current TimeModel.
	 * @return the copy of the current TimeModel 
	 */
	public abstract TimeModel getCopy();
	
	/**
	 * Sets the setup configuration as HashSet&lt;String, String&gt; (property, value) to the TimeModel.
	 * @param timeModelSettings the time model setup configuration as HashSet&lt;String, String&gt; (property, value)
	 */
	public abstract void setTimeModelSettings(HashMap<String, String> timeModelSettings);
	
	/**
	 * Returns the setup configuration of the TimeModel as HashMap consisting of (property, value).
	 * @return the setup configuration as HashMap consisting of (property, value)
	 */
	public abstract HashMap<String, String> getTimeModelSetting();
	
	/**
	 * Returns the DisplayJPanel4Configuration.java (an extended JPanel) for the configuration
	 * of a TimeModel (before the agency is executed).
	 *
	 * @param project the current Agent.Workbench Project
	 * @param timeModelController the current {@link TimeModelController}
	 * @return the DisplayJPanel4Configuration.java for the TimeModel configuration
	 */
	public abstract JPanel4TimeModelConfiguration getJPanel4Configuration(Project project, TimeModelController timeModelController);
	
	/**
	 * Returns a DisplayJToolBar4Execution (an extended JToolBar) that consists of the necessary 
	 * tools to handle a TimeModel during execution.
	 * @return the DisplayJToolBar4Execution with tools that can be used during the runtime of the agency
	 */
	public abstract TimeModelBaseExecutionElements getDisplayElements4Execution();
	
}
