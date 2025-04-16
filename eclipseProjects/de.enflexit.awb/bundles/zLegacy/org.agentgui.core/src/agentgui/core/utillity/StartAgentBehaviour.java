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
package agentgui.core.utillity;

import agentgui.core.jade.Platform.RemoteStartAgentWaiter;
import agentgui.simulationService.LoadService;
import agentgui.simulationService.LoadServiceHelper;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;

/**
 * This behaviour class will be used by the UtilityAgent, if a new agent has to be started locally or on a remote container.<br> 
 * Especially if a remote container is to be used, the {@link LoadService} will be used to start the actual agent. 
 * 
 * @see UtilityAgent
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class StartAgentBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1921236046994970137L;

	private RemoteStartAgentWaiter startWaiter;
	
	private String agentName;
	private String agentClassName;
	private Object[] args;
	private String containerName;
	
	/**
	 * Instantiates a new start agent behaviour.
	 *
	 * @param startWaiter the waiting instance 
	 * @param agentName the agent name
	 * @param agentClassName the agent class name
	 * @param args the start arguments for the agent to start 
	 * @param containerName the container name
	 */
	public StartAgentBehaviour(RemoteStartAgentWaiter startWaiter, String agentName, String agentClassName, Object[] args, String containerName) {
		this.startWaiter = startWaiter;
		this.agentName = agentName;
		this.agentClassName = agentClassName;
		this.args = args;
		this.containerName = containerName;
	}
	
	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action() {

		boolean startedSuccessfully = false;

		LoadServiceHelper loadHelper = this.getLoadServiceHelper();
		if (loadHelper!=null) {
			try {
				startedSuccessfully = loadHelper.startAgent(this.agentName, this.agentClassName, this.args, this.containerName);
				
			} catch (ServiceException sEx) {
				sEx.printStackTrace();
			}
		}
		
		// --- Awake the waiting thread ---------
		if (this.startWaiter!=null) {
			this.startWaiter.setAgentStarted(startedSuccessfully);
			synchronized (this.startWaiter) {
				this.startWaiter.notifyAll();	
			}
		}
		
		this.myAgent.doDelete();
		
	}

	/**
	 * Checks if the {@link LoadService} is running or not .
	 * @return true, if the service is running
	 */
	private LoadServiceHelper getLoadServiceHelper() {
		try {
			LoadServiceHelper loadHelper = (LoadServiceHelper) myAgent.getHelper(LoadService.NAME);
			return loadHelper;
		} catch (ServiceException e) {
			//e.printStackTrace();
		}
		return null;
	}
	
}
