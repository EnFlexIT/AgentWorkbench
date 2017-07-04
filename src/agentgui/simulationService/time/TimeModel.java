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

import agentgui.core.project.Project;


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
	public TimeModel(){
	}

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
	 * Sets the setup configuration as HashSet<String, String> (property, value) to the TimeModel.
	 * @param timeModelSettings the time model setup configuration as HashSet<String, String> (property, value)
	 */
	public abstract void setTimeModelSettings(HashMap<String, String> timeModelSettings);
	
	/**
	 * Returns the setup configuration of the TimeModel as HashSet<String, String> consisting of (property, value).
	 * @return the setup configuration as HashSet<String, String> consisting of (property, value)
	 */
	public abstract HashMap<String, String> getTimeModelSetting();
	
	/**
	 * Returns the DisplayJPanel4Configuration.java (an extended JPanel) for the configuration
	 * of a TimeModel (before the agency is executed).
	 * @param project the current Agent.GUI Project
	 * @return the DisplayJPanel4Configuration.java for the TimeModel configuration
	 */
	public abstract JPanel4TimeModelConfiguration getJPanel4Configuration(Project project);
	
	/**
	 * Returns a DisplayJToolBar4Execution (an extended JToolBar) that consists of the necessary 
	 * tools to handle a TimeModel during execution.
	 * @return the DisplayJToolBar4Execution with tools that can be used during the runtime of the agency
	 */
	public abstract TimeModelBaseExecutionElements getDisplayElements4Execution();
	
}
