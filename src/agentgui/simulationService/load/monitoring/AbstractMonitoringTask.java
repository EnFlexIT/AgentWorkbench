/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.simulationService.load.monitoring;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.simulationService.load.LoadMeasureThread;

/**
 * The Class AbstractMonitoringTask can be used to extend specific tasks 
 * that re used to control process within the JVM. In case of malfunction,
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
		REDO_AGENTGUI_START_SEQUENCE,
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
	private void setMonitoringState(MonitoringState monitoringState) {
		this.monitoringState = monitoringState;
	}
	/**
	 * Calls the actual monitoring task and returns the current {@link MonitoringState}.
	 * In case of a malfunction, it tries to execute the defined measure.
	 * @return the current monitoring state
	 */
	public MonitoringState doMonitoringTask() {
		
		if (this.isFaultlessProcess()==true) {
			// --- No errors found ------------------------
			this.setMonitoringState(MonitoringState.OK);
		} else {
			// --- Errors found - change current state ----
			this.setMonitoringState(MonitoringState.FAULTY_PROCESS);
			// --- Try to get fault measure ---------------
			switch (this.getMonitoringMeasureType()) {
			case SHUTDOWN_JVM:
				this.doShutDownJVM();
				break;
			
			case REDO_AGENTGUI_START_SEQUENCE:
				this.doRedoAgentGuiStartSequence();
				break;
				
			case RESTART_AGENT:
				this.doRestartAgent();
				break;
			
			case CUSTOM_MEASURE:
				this.doCustomMeasure();
				break;
			}			
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
		Application.quit();

		// ----------------------------------------------------------
		// --- In case that the executions arrives here, wait 5s. --- 
		// --- Finally, terminate the JVM from this point -----------
		// ----------------------------------------------------------
		try {
			Thread.sleep(1000 * 5);
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
		System.exit(1);
	}
	
	/**
	 * Re-does the AgentGui start sequence, while before it tries 
	 * to reset the system in a neutral state (e.g. shutdown JADE etc.).
	 */
	private void doRedoAgentGuiStartSequence() {
		// --- Stop Agent.GUI -------------------
		Application.stopAgentGUI();
		System.out.println(Language.translate("Reinitialisiere ... ") );
		// --- Start Agent.GUI ------------------
		Application.startAgentGUI();
	}
	
	/**
	 * Restarts the specified agent.
	 */
	private void doRestartAgent() {
		
		
		
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
				repairThread.setName(this.getTaskDescription());
				repairThread.start();
				this.setMonitoringState(MonitoringState.FAULTY_PROCESS_EXCUTED_MEASURE);
				
			} catch (Exception ex) {
				ex.printStackTrace();
				this.setMonitoringState(MonitoringState.FAULTY_PROCESS_FAULTY_MEASURE);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getTaskDescription();
	}
	/**
	 * Registers at the local {@link LoadMeasureThread}. Thus, the monitoring is active.
	 */
	public void register() {
		LoadMeasureThread.registerMonitoringTask(this);
	}
	
}
