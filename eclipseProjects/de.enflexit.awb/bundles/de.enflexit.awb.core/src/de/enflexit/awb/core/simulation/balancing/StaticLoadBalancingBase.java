package de.enflexit.awb.core.simulation.balancing;

import jade.core.Agent;
import jade.core.ServiceException;

import java.util.ArrayList;
import java.util.Iterator;

import de.enflexit.awb.core.Application;
import de.enflexit.awb.core.environment.EnvironmentController;
import de.enflexit.awb.core.environment.EnvironmentType;
import de.enflexit.awb.core.project.RemoteContainerConfiguration;
import de.enflexit.awb.core.project.setup.AgentClassElement4SimStart;
import de.enflexit.awb.core.simulation.agents.LoadExecutionAgent;
import de.enflexit.awb.simulation.ontology.RemoteContainerConfig;
import de.enflexit.language.Language;

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
	/** The remote only boolean, do not use local machine for agents. */
	protected boolean isRemoteOnly = true;
	/** The local container name. */
	protected String localContainerName;


	/**
	 * Instantiates a new static load balancing base.
	 * @param agent the agent that organizes the current load balancing action
	 */
	public StaticLoadBalancingBase(LoadExecutionAgent agent) {
		super(agent);
		// --- Which agents are to start ----------------------------
		currAgentList = currSimSetup.getAgentList();	

		// --- Get number of expected agents and the number --------- 
		// --- of container, which are wanted for this setup --------
		currNumberOfAgents = currDisSetup.getNumberOfAgents();
		currNumberOfContainer = currDisSetup.getNumberOfContainer();
		localContainerName = currProject.getProjectFolder();
		isRemoteOnly = currDisSetup.isRemoteOnly();
	}
	
	/**
	 * This Method will be called right before the begin of the action() method and 
	 * will start the visualization agent in the prepared tag of <b>Agent.GUI</b>.
	 */
	@Override
	public void onStart() {
		this.openMonitorAgents();
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
		this.startAgentsFromCurrAgentList(false);
	}
	/**
	 * This method will start all agents defined in the agent list
	 * of 'Agent-Start' from the 'Simulation-Setup'.
	 * @param printAgentStart2Console if true, every agent start will be printed in the console
	 */
	protected void startAgentsFromCurrAgentList(boolean printAgentStart2Console) {
		
		boolean debug = printAgentStart2Console;
		int counter=1;
		
		for (Iterator<AgentClassElement4SimStart> iterator = currAgentList.iterator(); iterator.hasNext();) {
			AgentClassElement4SimStart agent2Start = iterator.next();
			// --- Check for start arguments -------------------------
			Object[] startArgs = this.getStartArguments(agent2Start);
			// --- Start the agent -----------------------------------
			if (debug==true) {
				System.out.println("Start Agent " + counter + ": " + agent2Start.getStartAsName() + " Class: " + agent2Start.getAgentClassReference());
			}
			this.startAgent(agent2Start.getStartAsName(), agent2Start.getAgentClassReference(), startArgs);
			counter++;
		} 
		if (debug==true) {
			System.out.println(counter + " Agents were started.");	
		}
	}

	/**
	 * Sets the defaults4 remote container configuration.
	 */
	private void setDefaults4RemoteContainerConfig() {

		RemoteContainerConfiguration projectRemContConfig = this.currProject.getRemoteContainerConfiguration();
		if (projectRemContConfig==null) {
			projectRemContConfig = new RemoteContainerConfiguration();
		}
		
		RemoteContainerConfig remConConf = new RemoteContainerConfig();
		remConConf.setPreventUsageOfUsedComputer(projectRemContConfig.isPreventUsageOfAlreadyUsedComputers());
		remConConf.setJadeShowGUI(projectRemContConfig.isShowJADErmaGUI());
		remConConf.setJvmMemAllocInitial(projectRemContConfig.getJvmMemAllocInitial());
		remConConf.setJvmMemAllocMaximum(projectRemContConfig.getJvmMemAllocMaximum());
		
		try {
			if (loadHelper!=null) {
				loadHelper.setDefaults4RemoteContainerConfig(remConConf);	
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * This method will start the agent that will show the visualisation of the current environment model.
	 * In case of an headless operation of Agent.GUI, this method will do nothing.
	 * 
	 * @see Application#isOperatingHeadless()
	 */
	protected void startVisualizationAgent() {
		
		// --- Visualisation is only needed in case of a none headless operation
		if (Application.isOperatingHeadless()==true) return;
		
		EnvironmentController envController = currProject.getEnvironmentController();
		if (envController!=null) {
			
			EnvironmentType envType = currProject.getEnvironmentModelType();
			String envTypeInternalKey = envType.getInternalKey();
			
			// ----------------------------------------------------------------
			// --- Start visualisation, if an visualised environment is used --
			// ----------------------------------------------------------------
			if (envTypeInternalKey.equalsIgnoreCase("none")==false) {

				// --- Get the Agent which has to be started for ---
				Class<? extends Agent> displayAgentClass = envType.getDisplayAgentClass();
				
				Object[] startArg = new Object[3];
				startArg[0] = currProject.getVisualizationTab4SetupExecution();
				startArg[1] = envController;
				this.startAgent("DisplayAgent", displayAgentClass, startArg);
				
				// --- Set the focus on Visualisation-Tab ---------------------
				currProject.getProjectEditorWindow().setFocus2Tab(Language.translate(ProjectWindowTab.TAB_4_RUNTIME_VISUALIZATION));
			}
			// ----------------------------------------------------------------
		}
	}
	
}
