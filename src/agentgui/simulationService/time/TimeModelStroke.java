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
package agentgui.simulationService.time;

import java.util.HashMap;

import agentgui.core.gui.projectwindow.simsetup.TimeModelController;
import agentgui.core.project.Project;

/**
 * This is a stroke time model, which inherits just a simple counter and can
 * be used in an {@link EnvironmentModel}
 * 
 * @see TimeModelController
 * @see TimeModelStrokeConfiguration
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeModelStroke extends TimeModel {

	private static final long serialVersionUID = -63223704339241994L;

	private int counterStart = 1;
	private int counterStop = 9999;
	private int counter = 1;
	
	
	/**
	 * Instantiates a new time model stroke.
	 */
	public TimeModelStroke() {
	}
	/**
	 * Instantiates a new time model stroke.
	 * @param currentCounterValue the position number the counter is currently set
	 */
	public TimeModelStroke(Integer currentCounterValue) {
		this.counter = currentCounterValue;
	}
	/**
	 * Instantiates a new time model stroke.
	 * @param counterValueStart the counter start value
	 * @param counterValueStop the counter stop value
	 */
	public TimeModelStroke(Integer counterValueStart, Integer counterValueStop) {
		this.counter = counterValueStart;
		this.counterStart = counterValueStart;
		this.counterStop = counterValueStop;
	}
		
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.TimeModel#step()
	 */
	@Override
	public void step() {
		counter++;
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.TimeModel#stepBack()
	 */
	@Override
	public void stepBack() {
		counter--;
	}

	/**
	 * Sets the value at which the TimeModel starts.
	 * @param counterStart the new counter start
	 */
	public void setCounterStart(int counterStart) {
		this.counterStart = counterStart;
	}
	/**
	 * Returns the value at which the TimeModel starts.
	 * @return the counter start
	 */
	public int getCounterStart() {
		return counterStart;
	}
	
	/**
	 * Sets the value to stop the TimeModel.
	 * @param counterStop the new counter stop
	 */
	public void setCounterStop(int counterStop) {
		this.counterStop = counterStop;
	}
	/**
	 * Returns the value to stop the TimeModel.
	 * @return the counter stop
	 */
	public int getCounterStop() {
		return counterStop;
	}
	
	/**
	 * Sets the current counter.
	 * @param counter the counter to set
	 */
	public void setCounter(Integer counter) {
		this.counter = counter;
	}
	/**
	 * Returns the current counter.
	 * @return the counter
	 */
	public Integer getCounter() {
		return counter;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.TimeModel#getJPanel4Configuration()
	 */
	@Override
	public DisplayJPanel4Configuration getJPanel4Configuration(Project project) {
		return new TimeModelStrokeConfiguration(project);
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.TimeModel#getJToolBar4Execution()
	 */
	@Override
	public DisplayJToolBar4Execution getJToolBar4Execution() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.TimeModel#setSetupConfiguration(java.util.HashMap)
	 */
	@Override
	public void setTimeModelSettings(HashMap<String, String> timeModelSetupConfiguration) {
		
	}
	/* (non-Javadoc)
	 * @see agentgui.simulationService.time.TimeModel#getSetupConfiguration()
	 */
	@Override
	public HashMap<String, String> getTimeModelSetting() {
		return null;
	}
	
} // --- End of Sub-Class -----