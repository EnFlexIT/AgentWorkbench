package de.enflexit.awb.core.utillity;

import de.enflexit.awb.core.jade.Platform.RemoteStartAgentWaiter;
import jade.core.Agent;

/**
 * The UtilityAgent is used to affect a running Multi-Agent system from the application.<br> 
 * Since the application does not have direct access to any agents, the UtilityAgent will do some tasks here.
 * <br>
 * Depending on the start-arguments for this Agent the tasks are as follows:<br>
 * <ul>
 * 		<li>Platform.UTILITY_AGENT_JOB#OpernDF: will send a message in order to show the DF</li>
 * 		<li>Platform.UTILITY_AGENT_JOB#StartAgent: will try to start an agent<br> 
 * 		For this case the start argument needs to be extend:<br>
 * 		index[0] = <i>UtilityAgentJob.StartAgent</i><br> 
 *  	index[1] = <br>
 *  	index[2] = the agent name<br>
 *  	index[3] = the agent class name<br>
 *  	index[4] = an Object[] of arguments<br>   
 *  	index[5] = the container name, where the agent is to be hosted<br>  
 * 		</li>
 * 		<li>Platform.UTILITY_AGENT_JOB#ShutdownPlatform: will send a message to the AMS in order to shutdown the whole platform</li>
 * 		<li>Platform.UTILITY_AGENT_JOB#OpenLoadMonitor: will send a message to show the LoadMonitor</li>
 * 		<li>Platform.UTILITY_AGENT_JOB#OpenThreadMonitor: will send a message to show the ThreadMonitor</li>
 * </ul>
 * The setup-method of the agent will evaluate the start argument and will add the corresponding behaviour.
 * 
 * 
 * @see agentgui.core.jade.Platform
 * 
 * @see agentgui.core.utillity.UtilityAgent.UtilityAgentJob#OpernDF
 * @see agentgui.core.utillity.ShowDFBehaviour
 * 
 * @see agentgui.core.utillity.UtilityAgent.UtilityAgentJob#ShutdownPlatform
 * @see agentgui.core.utillity.PlatformShutdownBehaviour
 * 
 * @see agentgui.core.utillity.UtilityAgent.UtilityAgentJob#OpenLoadMonitor
 * @see agentgui.core.utillity.ShowLoadMonitorBehaviour
 * @see de.enflexit.awb.simulation.agents.simulationService.agents.LoadMeasureAgent
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class UtilityAgent extends Agent {

	private static final long serialVersionUID = 4018534357973603L;
	
	/**
	 * This enumeration describes the possible jobs of this {@link UtilityAgent}.
	 */
	public static enum UtilityAgentJob {
		OpernDF,
		StartAgent,
		ShutdownPlatform,
		OpenLoadMonitor,
		OpenThreadMonitor
	}

	
	/**
	 * The setup will evaluate the start argument for the agent and 
	 * will add the corresponding behaviour to it.
	 */
	@Override
	protected void setup() {
		super.setup();
		
		Object[] args = getArguments();
		if (args==null || args.length==0) {
			this.doDelete();
			return;
		}
		
		UtilityAgentJob job = (UtilityAgentJob) args[0];
		switch (job) {
		case OpernDF:
			this.addBehaviour(new ShowDFBehaviour());
			break;

		case StartAgent:
			RemoteStartAgentWaiter waitingInstance = (RemoteStartAgentWaiter) args[1];
			String agentName = (String) args[2];
			String agentClassName = (String) args[3];
			Object[] agentArguments = (Object[]) args[4];
			String containerName = (String) args[5];
			this.addBehaviour(new StartAgentBehaviour(waitingInstance, agentName, agentClassName, agentArguments, containerName));
			break;
			
		case ShutdownPlatform:
			this.addBehaviour(new PlatformShutdownBehaviour());
			break;
			
		case OpenLoadMonitor:
			this.addBehaviour(new ShowLoadMonitorBehaviour());
			break;

		case OpenThreadMonitor:
			this.addBehaviour(new ShowThreadMonitorBehaviour());
			break;
			
		default:
			this.doDelete();
		}
		
	}

}
