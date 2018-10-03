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
package agentgui.core.environment;

import java.io.File;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.DefaultListModel;

import agentgui.core.project.Project;
import agentgui.core.project.setup.AgentClassElement4SimStart;
import agentgui.core.project.setup.SimulationSetup;
import agentgui.core.project.setup.SimulationSetupNotification;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.agents.AbstractDisplayAgent;
import agentgui.simulationService.environment.AbstractEnvironmentModel;
import agentgui.simulationService.environment.DisplaytEnvironmentModel;
import agentgui.simulationService.environment.EnvironmentModel;
import agentgui.simulationService.time.TimeModel;
import jade.lang.acl.ACLMessage;

/**
 * This class has to be extended if you are writing your own environment model and visualisation.
 * This class manages the environment model and notifies the observers about the changes to the environment model.
 * 
 * @author Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen 
 */
public abstract class EnvironmentController extends Observable implements Observer {

	private Project currProject;

	/** The {@link AbstractDisplayAgent} that is currently using this EnvironmentController. */
	private AbstractDisplayAgent myDisplayAgent;

	/** The current environment panel. */
	private EnvironmentPanel myEnvironmentPanel;
	/** The current TimeModel. */
	private TimeModel myTimeModel;
	
	/**
	 * The path to the folder where all environment related files are stored.
	 * (Contains the slash at the end).
	 */
	private String envFolderPath;
	/** The list model for the agents, which has to be started with the current environment model */
	private DefaultListModel<AgentClassElement4SimStart> agents2Start = new DefaultListModel<AgentClassElement4SimStart>();

	
	
	/**
	 * Constructor for a new environment controller
	 * for displaying the current environment model
	 * during a running simulation.
	 */
	public EnvironmentController() {
	}
	/**
	 * Constructor for a controller within the Agent.GUI application.
	 * @param project the current project
	 */
	public EnvironmentController(Project project){
		this.currProject = project;
		if (currProject!=null) {
			this.currProject.addObserver(this);
			this.setEnvFolderPath(this.currProject.getProjectFolderFullPath()+this.currProject.getSubFolderEnvSetups() + File.separator);	
		}
	}
	
	/**
	 * Sets the environment panel for displaying the data model.
	 * @param environmentPanel the EnvironmentPanel to set
	 */
	public void setEnvironmentPanel(EnvironmentPanel environmentPanel) {
		this.myEnvironmentPanel = environmentPanel;
	}
	/**
	 * Gets the environment panel for displaying the data model.
	 * @return the myEnvironmentPanel
	 */
	public EnvironmentPanel getEnvironmentPanel() {
		return this.myEnvironmentPanel;
	}
	/**
	 * Gets the environment panel for displaying the data model or creates it new.
	 * @return the myEnvironmentPanel
	 */
	public EnvironmentPanel getOrCreateEnvironmentPanel() {
		if (this.myEnvironmentPanel==null) {
			this.myEnvironmentPanel = this.createEnvironmentPanel();
		}
		return this.myEnvironmentPanel;
	}
	/**
	 * Creates here the EnvironmentPanel, which allows to display the current data model 
	 * of the environment model.
	 * @return the environment panel
	 */
	protected abstract EnvironmentPanel createEnvironmentPanel();
	
	/**
	 * Returns the current simulation setup
	 * @return the current simulation setup
	 */
	protected SimulationSetup getCurrentSimulationSetup(){
		if (this.getProject()!=null) {
			return this.getProject().getSimulationSetups().getCurrSimSetup();
		}
		return null;
	}
	
	/**
	 * Gets the current project.
	 * @return the project
	 */
	public Project getProject() {
		return currProject;
	}
	/**
	 * Sets the project.
	 * @param currProject the new project
	 */
	public void setProject(Project currProject) {
		this.currProject = currProject;
	}

	/**
	 * Sets the environment folder path.
	 * @param envFolderPath the new environment folder path
	 */
	public void setEnvFolderPath(String envFolderPath) {
		this.envFolderPath = envFolderPath;
	}
	/**
	 * Gets the environment folder path.
	 * @return the environment folder path
	 */
	public String getEnvFolderPath() {
		return envFolderPath;
	}

	/**
	 * Gets the list of agents to start.
	 * @return the agents2 start
	 */
	public DefaultListModel<AgentClassElement4SimStart> getAgents2Start() {
		return agents2Start;
	}
	/**
	 * Sets the list of agents to start.
	 * @param agents2Start the new agents2 start
	 */
	public void setAgents2Start(DefaultListModel<AgentClassElement4SimStart> agents2Start) {
		this.agents2Start = agents2Start;
	}
	
	/**
	 * Returns an empty position for the Agents2Start.
	 * @return the empty position4 agents2 start
	 */
	public int getEmptyPosition4Agents2Start() {
	
		int emptyPos = 0;
		
		Vector<Integer> positions = new Vector<Integer>(); 
		for (int i = 0; i < this.agents2Start.size(); i++) {
			AgentClassElement4SimStart ace4s = (AgentClassElement4SimStart) this.agents2Start.get(i);
			positions.add(ace4s.getPostionNo());
		}
		
		Collections.sort(positions);
		for (int i = 0; i < positions.size(); i++) {
			if (positions.get(i)!=i+1) {
				emptyPos = i+1;
				break;
			}
		}
		
		if (emptyPos==0) {
			emptyPos = this.agents2Start.size() + 1;
		}
		return emptyPos;
	}
	
	/**
	 * Gets the description instances of the agents to start, identified by the agent name
	 *
	 * @param agentName the agent name
	 * @return the array of AgentClassElement4SimStart depending on the agent name
	 */
	public AgentClassElement4SimStart[] getAgents2StartFromAgentName(String agentName) {
		
		AgentClassElement4SimStart[] ace4sArr = null;
		Vector<AgentClassElement4SimStart> ace4sVector = new Vector<AgentClassElement4SimStart>();
		
		for (int i = 0; i < this.agents2Start.size(); i++) {
			AgentClassElement4SimStart ace4s = (AgentClassElement4SimStart) this.agents2Start.get(i);
			if (ace4s.getStartAsName().equals(agentName)) {
				ace4sVector.add(ace4s);
			}
		}

		if (ace4sVector.size()!=0) {
			ace4sArr = ace4sVector.toArray(new AgentClassElement4SimStart[ace4sVector.size()]);	
		}
		return ace4sArr;
	}
	
	/**
	 * Renumber the list of agents to start.
	 */
	public void reNumberAgents2Start() {
		
		Integer counter = 1;
		AgentClassElement4SimStart ac4s = null;
		
		for (int i=0; i<this.agents2Start.size(); i++) {
			 ac4s = (AgentClassElement4SimStart) this.agents2Start.getElementAt(i); 
			 ac4s.setPostionNo(counter);
			 counter++;
		}
	}
	
	/**
	 * With this method the list of Agents {@link EnvironmentController#agents2Start}, which has to 
	 * be started with the environment can be registered at the {@link SimulationSetup}
	 *  
	 * @param listName Consider the use of the constant value {@link SimulationSetup#AGENT_LIST_EnvironmentConfiguration} 
	 * or just use an individual name   
	 */
	protected void registerDefaultListModel4SimulationStart(String listName) {
		this.agents2Start = this.getCurrentSimulationSetup().getAgentDefaultListModel(this.agents2Start, listName);
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object updateObject) {
		if (this.currProject!=null && observable==this.currProject && updateObject==Project.SAVED) {
			this.saveEnvironment();
		} else if (observable.equals(this.currProject) && updateObject instanceof SimulationSetupNotification) {
			this.handleSimSetupChange((SimulationSetupNotification) updateObject);
		}
	}
	/**
	 * Invoked by the project when the simulation setup change event occurs.
	 * @param sscn the SimulationSetupsChangeNotification that can be differentiated by its globals
	 */
	protected abstract void handleSimSetupChange(SimulationSetupNotification sscn);
	
	
	/** Load environment model from files */
	protected abstract void loadEnvironment();
	/** Save environment model to files */
	protected abstract void saveEnvironment();
	
	
	/**
	 * Sets the environment model.
	 * @param environmentModel the new environment model
	 */
	public void setEnvironmentModel(EnvironmentModel environmentModel) {
		this.setTimeModel(environmentModel.getTimeModel());
		this.setDisplayEnvironmentModel(environmentModel.getDisplayEnvironment());
		this.setAbstractEnvironmentModel(environmentModel.getAbstractEnvironment());
	}
	/**
	 * Returns the current instances of the EnvironmentModel.
	 * @return the environment model
	 */
	public EnvironmentModel getEnvironmentModel() {
		EnvironmentModel envModel = new EnvironmentModel();
		envModel.setTimeModel(this.getTimeModel());
		envModel.setDisplayEnvironment(this.getDisplayEnvironmentModel());
		envModel.setAbstractEnvironment(this.getAbstractEnvironmentModel());
		return envModel;
	}
	
	
	/**
	 * Sets the TimeModel.
	 * @param mewTimeModel the new TimeModel
	 */
	public void setTimeModel(TimeModel mewTimeModel) {
		this.myTimeModel = mewTimeModel;
	}
	/**
	 * Returns the TimeModel.
	 * @return the TimeModel
	 */
	public TimeModel getTimeModel() {
		if (this.getProject()!=null) {
			myTimeModel = this.getProject().getTimeModelController().getTimeModel();
		}
		return myTimeModel;
	}
	/**
	 * Returns the TimeModel copy.
	 * @return the TimeModel copy
	 */
	public TimeModel getTimeModelCopy() {
		TimeModel timeModel = this.getTimeModel(); 
		if (timeModel==null) {
			return null;
		} 
		return timeModel.getCopy();
	}
	
	/**
	 * Set the displayable environment object of the EnvironmentModel 
	 * @param displayEnvironmentModel the environment model
	 */
	public abstract void setDisplayEnvironmentModel(DisplaytEnvironmentModel displayEnvironmentModel);
	/**
	 * Returns the displayable environment object of the EnvironmentModel
	 * @return the current instance of the environment model
	 */
	public abstract DisplaytEnvironmentModel getDisplayEnvironmentModel();
	 /**
	  * Returns a copy of the displayable environment object of the EnvironmentModel
	  * @return a copy of the environment model
	  */
	public abstract DisplaytEnvironmentModel getDisplayEnvironmentModelCopy();
	
	/**
	 * Set the abstract (and individual) environment object of the EnvironmentModel 
	 * @param abstractEnvironmentModel the environment model
	 */
	public abstract void setAbstractEnvironmentModel(AbstractEnvironmentModel abstractEnvironmentModel);
	/**
	 * Returns the abstract (and individual) environment object of the EnvironmentModel
	 * @return the current instance of the environment model
	 */
	public abstract AbstractEnvironmentModel getAbstractEnvironmentModel();
	 /**
	  * Returns a copy of the abstract (and individual) environment object of the EnvironmentModel
	  * @return a copy of the environment model
	  */
	public abstract AbstractEnvironmentModel getAbstractEnvironmentModelCopy();
	
	
	/**
	 * Sets the {@link AbstractDisplayAgent} that is using this {@link EnvironmentController} 
	 * In case that the agency is executed. Using this instance you're able to translate 
	 * user interactions into {@link ACLMessage} and {@link SimulationService}-notifications
	 * that can change settings and parameters during the runtime of the agency.
	 * 
	 * @param newDisplayAgent the new, extended {@link AbstractDisplayAgent}
	 */
	public void setDisplayAgent(AbstractDisplayAgent newDisplayAgent) {
		this.myDisplayAgent = newDisplayAgent;
	}
	/**
	 * Returns the {@link AbstractDisplayAgent} that is using this EnvironmentController 
	 * in case that the agency is executed. Using this instance you're able to translate 
	 * user interactions into {@link ACLMessage} and {@link SimulationService}-notifications 
	 * that can change settings and parameters during the runtime of the agency.
	 * 
	 * @return the environment controller agent
	 */
	public AbstractDisplayAgent getDisplayAgent() {
		return myDisplayAgent;
	}
	
}
