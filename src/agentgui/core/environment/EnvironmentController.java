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
	protected Project project = null;
	
	/**
	 * The path to the folder where all environment related files are stored.
	 * (Contains the slash at the end).
	 */
	protected String envFolderPath = null;
	
	/**
	 * Do not use this constructor
	 */
	@Deprecated
	public EnvironmentController(){
		
	}
	/**
	 * Constructor to be invoked by all the subclasses
	 * @param project the current project
	 */
	public EnvironmentController(Project project){
		this.project = project;
		this.project.addObserver(this);
		envFolderPath = this.project.getProjectFolderFullPath()+this.project.getSubFolderEnvSetups() + File.separator;
	}
	
	/**
	 * Load environment model from files
	 */
	protected abstract void loadEnvironment();
	
	/**
	 * Save environment model to files
	 */
	protected abstract void saveEnvironment();
	
	/**
	 * Invoked by the project when the simulation setup change event occurs.
	 * @param sscn
	 */
	protected abstract void handleSimSetupChange(SimulationSetupsChangeNotification sscn);
	
	/**
	 * 
	 */
	protected void refreshFileName(){
	
	}
	

	/**
	 * Invoked when an observable( the {@link Project} in this case) notifies this class.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if(o.equals(project) && arg == Project.SAVED){
			saveEnvironment();
		}else if(o.equals(project) && arg instanceof SimulationSetupsChangeNotification){
			handleSimSetupChange((SimulationSetupsChangeNotification) arg);
			
		}
	}
	
	/**
	 * Set the environment object 
	 * @param environmentObject the environment model
	 */
	protected abstract void setEnvironment(Object environmentObject);
	
	/**
	 * The current project
	 * @return the current project
	 */
	public Project getProject(){
		return project;
	}
	
	/**
	 * Returns the current simulation setup
	 * @return the current simulation setup
	 */
	public SimulationSetup getCurrentSimSetup(){
		return project.simSetups.getCurrSimSetup();
	}
	
	/**
	 * The folder path where environment related files are stored (contains the slash at the end.)
	 * @return the folder path where environment related files are stored (contains the slash at the end.)
	 */
	public String getEnvFolderPath(){
		return envFolderPath;
	}
}
