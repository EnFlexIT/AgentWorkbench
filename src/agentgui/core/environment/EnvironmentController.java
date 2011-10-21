/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://sourceforge.net/projects/agentgui/
 * http://www.dawis.wiwi.uni-due.de/ 
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
package agentgui.core.environment;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListModel;

import agentgui.core.application.Project;
import agentgui.core.sim.setup.SimulationSetup;
import agentgui.core.sim.setup.SimulationSetupsChangeNotification;

/**
 * This class has to be extended if you are writing your own environment model and visulization.
 * This class manages the environment model and notifies the observers about the changes to the environment model.
 * 
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati 
 */
public abstract class EnvironmentController extends Observable implements Observer {

	/**
	 * The current project
	 */
	protected Project currProject = null;
	/**
	 * The path to the folder where all environment related files are stored.
	 * (Contains the slash at the end).
	 */
	protected String envFolderPath = null;
	/**
	 * The list model for the agents, which has to be started with the current environment model 
	 */
	protected DefaultListModel agents2Start = new DefaultListModel();


	/**
	 * Constructor to be invoked by all the subclasses
	 * @param project the current project
	 */
	public EnvironmentController(Project project){
		this.currProject = project;
		this.currProject.addObserver(this);
		this.envFolderPath = this.currProject.getProjectFolderFullPath()+this.currProject.getSubFolderEnvSetups() + File.separator;
	}
	
	/**
	 * Returns the current simulation setup
	 * @return the current simulation setup
	 */
	protected SimulationSetup getCurrentSimSetup(){
		return currProject.simulationSetups.getCurrSimSetup();
	}
	
	/**
	 * @return the agents2Start
	 */
	public DefaultListModel getAgents2Start() {
		return agents2Start;
	}
	/**
	 * @param agents2Start the agents2Start to set
	 */
	public void setAgents2Start(DefaultListModel agents2Start) {
		this.agents2Start = agents2Start;
	}

	/**
	 * With this method the list of Agents {@link EnvironmentController#agents2Start}, which has to 
	 * be started with the environment can be registered at the {@link SimulationSetup}
	 *  
	 * @param listName Consider the use of the constant value {@link SimulationSetup#AGENT_LIST_EnvironmentConfiguration} 
	 * or just use an individual name   
	 */
	protected void registerDefaultListModel4SimulationStart(String listName) {
		this.agents2Start = this.getCurrentSimSetup().getAgentDefaultListModel(this.agents2Start, listName);
	}
	
	/**
	 * Invoked when an observable( the {@link Project} in this case) notifies this class.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if(o.equals(currProject) && arg == Project.SAVED){
			this.saveEnvironment();
		}else if(o.equals(currProject) && arg instanceof SimulationSetupsChangeNotification){
			this.handleSimSetupChange((SimulationSetupsChangeNotification) arg);
		}
	}
	/**
	 * Invoked by the project when the simulation setup change event occurs.
	 * @param sscn
	 */
	protected abstract void handleSimSetupChange(SimulationSetupsChangeNotification sscn);
	
	
	/**
	 * Load environment model from files
	 */
	protected abstract void loadEnvironment();
	/**
	 * Save environment model to files
	 */
	protected abstract void saveEnvironment();
	
	
	/**
	 * Set the environment object 
	 * @param environmentObject the environment model
	 */
	public abstract void setEnvironmentModel(Object environmentObject);
	/**
	 * Get the current environment object
	 * @return the current instance of the environment model
	 */
	public abstract Object getEnvironmentModel();
	 /**
	  * Get the current environment object as a copy
	  * @return a copy of the environment model
	  */
	public abstract Object getEnvironmentModelCopy();
	
}
