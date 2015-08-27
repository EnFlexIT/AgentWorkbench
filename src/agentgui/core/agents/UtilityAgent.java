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
package agentgui.core.agents;

import agentgui.core.agents.behaviour.ExitDeviceExecutionBehaviour;
import agentgui.core.agents.behaviour.PlatformShutdownBehaviour;
import agentgui.core.agents.behaviour.ShowDFBehaviour;
import agentgui.core.agents.behaviour.ShowLoadMonitorBehaviour;
import agentgui.core.agents.behaviour.ShowThreadMonitorBehaviour;
import agentgui.core.jade.Platform.UTILITY_AGENT_JOB;
import jade.core.Agent;

/**
 * The UtilityAgent is used to affect a running Multi-Agent system from the application.<br> 
 * Since the application does not have direct access to any agents, the UtilityAgent will do some tasks here.
 * <br>
 * Depending on the start-arguments for this Agent the tasks are as follows:<br>
 * <ul>
 * 		<li>Platform.UTILITY_AGENT_JOB#OpernDF: will send a message in order to show the DF</li>
 * 		<li>Platform.UTILITY_AGENT_JOB#ShutdownPlatform: will send a message to the AMS in order to shutdown the whole platform</li>
 * 		<li>Platform.UTILITY_AGENT_JOB#OpenLoadMonitor: will send a message to show the LoadMonitor</li>
 * </ul>
 * The setup-method of the agent will evaluate the start argument and will add the corresponding behaviour.
 * 
 * 
 * @see agentgui.core.jade.Platform
 * 
 * @see agentgui.core.jade.Platform.UTILITY_AGENT_JOB#OpernDF
 * @see agentgui.core.agents.behaviour.ShowDFBehaviour
 * 
 * @see agentgui.core.jade.Platform.UTILITY_AGENT_JOB#ShutdownPlatform
 * @see agentgui.core.agents.behaviour.PlatformShutdownBehaviour
 * 
 * @see agentgui.core.jade.Platform.UTILITY_AGENT_JOB#OpenLoadMonitor
 * @see agentgui.core.agents.behaviour.ShowLoadMonitorBehaviour
 * @see agentgui.simulationService.agents.LoadMeasureAgent
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class UtilityAgent extends Agent {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4018534357973603L;

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
		
		UTILITY_AGENT_JOB job = (UTILITY_AGENT_JOB) args[0];
		switch (job) {
		case OpernDF:
			this.addBehaviour(new ShowDFBehaviour());
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
			
		case ExitDeviceExecutionModus:
			this.addBehaviour(new ExitDeviceExecutionBehaviour(this, 1000));
			break;
			
		default:
			this.doDelete();
		}
		
	}

}
