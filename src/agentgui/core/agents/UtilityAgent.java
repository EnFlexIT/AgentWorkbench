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
package agentgui.core.agents;

import agentgui.core.agents.behaviour.PlatformShutdownBehaviour;
import agentgui.core.agents.behaviour.ShowDFBehaviour;
import agentgui.core.agents.behaviour.ShowLoadMonitorBehaviour;
import agentgui.core.jade.Platform;
import jade.core.Agent;

/**
 * The UtilityAgent is used to affect a running Multi-Agent system from the application.<br> 
 * Since the application does not have direct access to any agents, the UtilityAgent will do some tasks here.
 * <br>
 * Depending on the start-arguments for this Agent the tasks are as follows:<br>
 * <ul>
 * 		<li><b>int</b> Platform.UTIL_CMD_OpenDF: will send a message in order to show the DF</li>
 * 		<li><b>int</b> Platform.UTIL_CMD_ShutdownPlatform: will send a message to the AMS in order to shutdown the whole platform</li>
 * 		<li><b>int</b> Platform.UTIL_CMD_OpenLoadMonitor: will send a message to show the LoadMonitor</li>
 * </ul>
 * The setup-method of the agent will evaluate the start argument and will add the corresponding behaviour.
 * 
 * 
 * @see agentgui.core.jade.Platform
 * 
 * @see agentgui.core.jade.Platform#UTIL_CMD_OpenDF
 * @see agentgui.core.agents.behaviour.ShowDFBehaviour
 * 
 * @see agentgui.core.jade.Platform#UTIL_CMD_ShutdownPlatform
 * @see agentgui.core.agents.behaviour.PlatformShutdownBehaviour
 * 
 * @see agentgui.core.jade.Platform#UTIL_CMD_OpenLoadMonitor
 * @see agentgui.core.agents.behaviour.ShowLoadMonitorBehaviour
 * @see agentgui.simulationService.agents.LoadAgent
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class UtilityAgent extends Agent {

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
		
		Integer start4 = (Integer) args[0];
		switch (start4) {
		case Platform.UTIL_CMD_OpenDF:
			this.addBehaviour(new ShowDFBehaviour());
			break;

		case Platform.UTIL_CMD_ShutdownPlatform:
			this.addBehaviour(new PlatformShutdownBehaviour());
			break;
			
		case Platform.UTIL_CMD_OpenLoadMonitor:
			this.addBehaviour(new ShowLoadMonitorBehaviour());
			break;
			
		default:
			this.doDelete();
		}
		
	}

}
