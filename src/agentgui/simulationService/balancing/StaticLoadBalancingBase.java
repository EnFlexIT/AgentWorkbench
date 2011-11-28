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
package agentgui.simulationService.balancing;

import jade.core.Agent;
import jade.core.ServiceException;

import java.util.ArrayList;
import java.util.Iterator;

import agentgui.core.agents.AgentClassElement4SimStart;
import agentgui.core.application.Language;
import agentgui.core.environment.EnvironmentPanel;
import agentgui.core.environment.EnvironmentType;
import agentgui.core.project.RemoteContainerConfiguration;
import agentgui.simulationService.agents.LoadExecutionAgent;
import agentgui.simulationService.ontology.RemoteContainerConfig;

/**
 * This is the base class for every tailored static load balancing class.
 *
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class StaticLoadBalancingBase extends BaseLoadBalancing {

	private static final long serialVersionUID = 8876791160586073658L;

	/** The current agent list. */
	protected ArrayList<AgentClassElement4SimStart> currAgentList = null;
	/** The current number of agents. */
	protected int currNumberOfAgents = 0;
	/** The current number of container. */
	protected int currNumberOfContainer = 0;


	/**
	 * Instantiates a new static load balancing base.
	 */
	public StaticLoadBalancingBase(LoadExecutionAgent agent) {
		super(agent);
		// --- Which agents are to start ----------------------------
		currAgentList = currSimSetup.getAgentList();	

		// --- Get number of expected agents and the number --------- 
		// --- of container, which are wanted for this setup --------
		currNumberOfAgents = currDisSetup.getNumberOfAgents();
		currNumberOfContainer = currDisSetup.getNumberOfContainer();
	}
	
	/**
	 * This Method will be called right before the begin of the action() method and 
	 * will start the visualization agent in the prepared tag of <b>Agent.GUI</b>.
	 */
	@Override
	public void onStart() {
		this.setDefaults4RemoteContainerConfig();
		this.startVisualizationAgent();
	}
	
	/**
	 * This Method will be called right after the end of the action() method and 
	 * will remove shut down the current agent.
	 *
	 * @return an integer code representing the termination value of the behaviour.
	 */
	@Override
	public int onEnd() {
		myAgent.doDelete();
		return super.onEnd();
	}

	/**
	 * This method will start all agents defined in the agent list
	 * of 'Agent-Start' from the 'Simulation-Setup'.
	 */
	protected void startAgentsFromCurrAgentList() {
		
		for (Iterator<AgentClassElement4SimStart> iterator = currAgentList.iterator(); iterator.hasNext();) {
			AgentClassElement4SimStart agent2Start = iterator.next();
			// --- Check for start arguments -------------------------
			Object[] startArgs = this.getStartArguments(agent2Start);
			// --- Start the agent -----------------------------------
			this.startAgent(agent2Start.getStartAsName(), agent2Start.getAgentClassReference(), startArgs);
		} 
	}

	/**
	 * Sets the defaults4 remote container config.
	 */
	private void setDefaults4RemoteContainerConfig() {

		if (this.currRemConConfig==null) {
			this.currRemConConfig = new RemoteContainerConfiguration();
		}
		
		RemoteContainerConfig remConConf = new RemoteContainerConfig();
		remConConf.setPreventUsageOfUsedComputer(currRemConConfig.isPreventUsageOfAlreadyUsedComputers());
		remConConf.setJadeShowGUI(currRemConConfig.isShowJADErmaGUI());
		remConConf.setJvmMemAllocInitial(currRemConConfig.getJvmMemAllocInitial());
		remConConf.setJvmMemAllocMaximum(currRemConConfig.getJvmMemAllocMaximum());
		
		try {
			loadHelper.setDefaults4RemoteContainerConfig(remConConf);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * This method will start the agents, which will show the
	 * visualisation of the current environment model.
	 */
	protected void startVisualizationAgent() {
		
		EnvironmentPanel envPanel = currProject.getEnvironmentPanel();
		if (envPanel!=null) {
			
			EnvironmentType envType = currProject.getEnvironmentModelType();
			String envTypeInternalKey = envType.getInternalKey();
			
			// ----------------------------------------------------------------
			// --- If an visualised environment is used -----------------------
			// ----------------------------------------------------------------
			if (envTypeInternalKey.equalsIgnoreCase("none")) {
				// --- Do nothing here ------------------------------
			
			} else {
				// --------------------------------------------------
				// --- Start the visualization of the model ---------
				// --------------------------------------------------

				// --- Get the Agent which has to be started for ---
				Class<? extends Agent> displayAgentClass = envType.getDisplayAgentClass();
				
				Object[] startArg = new Object[3];
				startArg[0] = currProject.projectVisualizationPanel;
				startArg[1] = envPanel;
				this.startAgent("DisplayAgent", displayAgentClass, startArg);
				
				// --- Set the focus on Visualisation-Tab ---------------------
				currProject.projectWindow.setFocus2Tab(Language.translate("Simulations-Visualisierung"));
			}
			// ----------------------------------------------------------------
		}
	}
	
}
