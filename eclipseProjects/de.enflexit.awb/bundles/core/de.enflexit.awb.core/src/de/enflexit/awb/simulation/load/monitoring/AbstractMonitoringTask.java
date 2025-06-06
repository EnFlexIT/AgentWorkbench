package de.enflexit.awb.simulation.load.monitoring;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.simulation.load.LoadMeasureThread;
import de.enflexit.language.Language;

/**
 * The Class AbstractMonitoringTask can be used to extend specific tasks 
 * that are used to control processes within the JVM. In case of malfunction,
 * a task can define a countermeasure to solve specific problems.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class AbstractMonitoringTask {

	/**
	 * The enumeration allows to differentiate monitoring tasks.
	 */
	public enum MonitoringType {
		THREAD_MONITORING,
		CUSTOMIZED_MONITORING
	}
	/**
	 * The enumeration MonitoringMeasureType describes different types of 
	 * measures for cases where processes run into malfunction.
	 */
	public enum MonitoringMeasureType {
		SHUTDOWN_JVM,
		REDO_AGENT_WORKBENCH_START_SEQUENCE,
		RESTART_AGENT,
		CUSTOM_MEASURE
	}
	/**
	 * The Enumeration MonitoringState.
	 */
	public enum MonitoringState {
		OK,
		FAULTY_PROCESS,
		FAULTY_PROCESS_EXCUTED_MEASURE,
		FAULTY_PROCESS_FAULTY_MEASURE
	}
	
	
	private MonitoringState monitoringState;
	
	private Integer checkIntervalInSeconds;
	private long nextCheckTime;
	
	
	/**
	 * Returns the current {@link MonitoringType}.
	 * @return the monitoring type
	 */
	public abstract MonitoringType getMonitoringType();
	/**
	 * Returns the current {@link MonitoringMeasureType}.
	 * @return the monitoring measure
	 */
	public abstract MonitoringMeasureType getMonitoringMeasureType();
	/**
	 * Has to return a suitable task description.
	 * @return the task description
	 */
	public abstract String getTaskDescription();
	/**
	 * Do monitoring task.
	 * @return true, if is faultless process
	 */
	public abstract boolean isFaultlessProcess();
	/**
	 * Should return a runnable that contains the suitable measure in case of a failure.
	 * @return the fault measure
	 */
	public abstract Runnable getFaultMeasure();
	/**
	 * Has to return true in case that the monitoring task can be removed after the failure measure was executed.
	 * @return true, if the monitoring task can be removed
	 */
	public abstract boolean removeTaskAfterMeasure();
		
		
	/**
	 * Returns the check interval in seconds.
	 * @return the check interval in seconds
	 */
	public int getCheckIntervalInSeconds() {
		if (checkIntervalInSeconds==null || checkIntervalInSeconds==0) {
			checkIntervalInSeconds = 5;
		}
		return checkIntervalInSeconds;
	}
	/**
	 * Sets the check interval in seconds. If set to <code>null</code> or 0, the 
	 * default value of 5s will be taken.
	 * @param newcheckIntervalInSeconds the new check interval in seconds
	 */
	public void setCheckIntervalInSeconds(Integer newcheckIntervalInSeconds) {
		this.checkIntervalInSeconds = newcheckIntervalInSeconds;
	}
	
	/**
	 * Returns the current monitoring state.
	 * @return the monitoring state
	 */
	public MonitoringState getMonitoringState() {
		if (monitoringState==null) {
			monitoringState = MonitoringState.OK;
		}
		return monitoringState;
	}
	/**
	 * Sets the current {@link MonitoringState}.
	 * @param monitoringState the new monitoring state
	 */
	protected void setMonitoringState(MonitoringState monitoringState) {
		this.monitoringState = monitoringState;
	}
	/**
	 * Calls the actual monitoring task and returns the current {@link MonitoringState}.
	 * In case of a malfunction, it tries to execute the defined measure.
	 * @return the current monitoring state
	 */
	public MonitoringState doMonitoringTask() {
		
		if (System.currentTimeMillis()>this.nextCheckTime) {
		
			if (this.isFaultlessProcess()==true) {
				// --- No errors found ------------------------------
				this.setMonitoringState(MonitoringState.OK);
			} else {
				// --- Errors found - change current state ----------
				this.setMonitoringState(MonitoringState.FAULTY_PROCESS);
				// --- Try to get fault measure ---------------------
				switch (this.getMonitoringMeasureType()) {
				case SHUTDOWN_JVM:
					System.err.println("[MonitoringEvent] Shutdown JVM for '" + this.getTaskDescription() + "'");
					this.doShutDownJVM();
					break;
				
				case REDO_AGENT_WORKBENCH_START_SEQUENCE:
					System.err.println("[MonitoringEvent] Redo Agent.GUI start sequence for '" + this.getTaskDescription() + "'");
					this.doRedoAgentGuiStartSequence();
					break;
					
				case RESTART_AGENT:
					System.err.println("[MonitoringEvent] Restart agent for '" + this.getTaskDescription() + "'");
					this.doCustomMeasure();
					break;
					
				case CUSTOM_MEASURE:
					System.err.println("[MonitoringEvent] Do custom measure for '" + this.getTaskDescription() + "'");
					this.doCustomMeasure();
					break;
				}	
				// --- Remove task after measure execution? ---------
				if (this.removeTaskAfterMeasure()==true) {
					this.removeTaskAfterMeasure();
				}
			}
			// --- Set the next check time --------------------------
			this.nextCheckTime = System.currentTimeMillis() + (this.getCheckIntervalInSeconds()*1000);
		}
		return this.getMonitoringState();
	}

	/**
	 * Does the shutdown of the JVM.
	 */
	private void doShutDownJVM() {
		
		// ----------------------------------------------------------
		// --- Do normal application quit ---------------------------
		// ----------------------------------------------------------
		this.setMonitoringState(MonitoringState.FAULTY_PROCESS_EXCUTED_MEASURE);
		Application.stop();

		// ----------------------------------------------------------
		// --- In case that executions still arrives here, wait 5s. - 
		// --- Finally, terminate the JVM from here. ----------------
		// ----------------------------------------------------------
		try {
			Thread.sleep(1000 * 5);
		} catch (InterruptedException ie) {
			//ie.printStackTrace();
		}
		System.exit(1);
	}
	
	/**
	 * Re-does the AgentGui start sequence, while before it tries 
	 * to reset the system in a neutral state (e.g. shutdown JADE etc.).
	 */
	private void doRedoAgentGuiStartSequence() {
		
		try {
			// --- Stop Agent.GUI -------------------
			this.setMonitoringState(MonitoringState.FAULTY_PROCESS_EXCUTED_MEASURE);
			Application.stopAgentWorkbench();
			System.err.println("=> " + Language.translate("Reinitialisiere") + " " + Application.getGlobalInfo().getApplicationTitle()  + " ... ");
			// --- Start Agent.GUI ------------------
			Application.startAgentWorkbench();
			
		} catch (Exception ex) {
			this.setMonitoringState(MonitoringState.FAULTY_PROCESS_FAULTY_MEASURE);
			ex.printStackTrace();
		}
	}
	/**
	 * Does the specific custom measure.
	 */
	private void doCustomMeasure() {
	
		Runnable repairRun = this.getFaultMeasure();
		if (repairRun!=null) {
			try {
				// --- Start repair thread ------------ 
				Thread repairThread = new Thread(this.getFaultMeasure());
				repairThread.setName(this.getTaskDescription() + "_Measure");
				repairThread.start();
				this.setMonitoringState(MonitoringState.FAULTY_PROCESS_EXCUTED_MEASURE);
				
			} catch (Exception ex) {
				ex.printStackTrace();
				this.setMonitoringState(MonitoringState.FAULTY_PROCESS_FAULTY_MEASURE);
			}
		}
	}
	
	/**
	 * Registers at the local {@link LoadMeasureThread}. Thus, the monitoring is active.
	 */
	public void registerTask() {
		LoadMeasureThread.registerMonitoringTask(this);
	}
	/**
	 * Removes the current monitoring task.
	 */
	public void unregisterTask() {
		LoadMeasureThread.removeMonitoringTask(this);
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getTaskDescription();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compareObject) {
		
		if (compareObject==null) return false;
		if (compareObject==this) return true;
		if (compareObject instanceof AbstractMonitoringTask) {
			return compareObject.toString().equals(this.toString());	
		}
		return false;
	}

}
