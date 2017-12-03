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
package agentgui.simulationService.distribution;

import agentgui.simulationService.ontology.RemoteContainerConfig;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

/**
 * The Class JadeRemoteStartAgent.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JadeRemoteStartAgent extends Agent {

	private static final long serialVersionUID = -880859677817836126L;

	private RemoteContainerConfig remoteContainerConfig;
	private JadeRemoteStartConfiguration remoteStartConfiguration;
	
	
	/* (non-Javadoc)
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected void setup() {

		Object[] startArg = this.getArguments();
		if (startArg.length>0) {
			if (startArg[0] instanceof RemoteContainerConfig) {
				// --- Start the remote container ---------
				this.remoteContainerConfig = (RemoteContainerConfig) startArg[0];
				this.addBehaviour(new JadeRemoteStartBehaviour(this));

			} else if (startArg[0] instanceof JadeRemoteStartConfiguration) {
				// --- Retry to start remote container ----
				this.remoteStartConfiguration = (JadeRemoteStartConfiguration) startArg[0];
				this.addBehaviour(new JadeRemoteReStartBehaviour(this));
			}
		}
	}
	
	/**
	 * The Class JadeRemoteStartBehaviour is used in case that a remote container
	 * start was requested. It will organize the download of the required resources
	 * and starts the container if everything  was done successfully.
	 */
	private class JadeRemoteStartBehaviour extends OneShotBehaviour {

		private static final long serialVersionUID = 5986902939912984745L;

		/**
		 * Instantiates a new jade remote start.
		 * @param myAgent the my agent
		 */
		public JadeRemoteStartBehaviour(Agent myAgent) {
			this.setAgent(myAgent);
		}
		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#action()
		 */
		@Override
		public void action() {
			System.out.println("Prepare for remote container start ... ");
			// --- Initiate the starter -----------------------------
			JadeRemoteStart jrs = new JadeRemoteStart(this.getAgent(), remoteContainerConfig);
			if (jrs.isReadyToStartRemoteContainer()==true) {
				// --- Start the remote container -------------------
				System.out.println("Starting remote container ... ");
				jrs.startJade();
			}
			// +++ Returns here after container shutdown ++++++++++++
			this.getAgent().doDelete();
		}
	}

	/**
	 * The Class JadeRemoteStartBehaviour is used in case that a remote container
	 * start was requested. It will organize the download of the required resources
	 * and starts the container if everything  was done successfully.
	 */
	private class JadeRemoteReStartBehaviour extends OneShotBehaviour {

		private static final long serialVersionUID = 5986902939912984745L;

		/**
		 * Instantiates a new jade remote restart.
		 * @param myAgent the my agent
		 */
		public JadeRemoteReStartBehaviour(Agent myAgent) {
			this.setAgent(myAgent);
		}
		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#action()
		 */
		@Override
		public void action() {
			System.out.println("Re-Prepare for remote container start ... ");
			// --- Initiate the starter -----------------------------
			JadeRemoteStart jrs = new JadeRemoteStart(this.getAgent(), remoteStartConfiguration);
			if (jrs.isReadyToStartRemoteContainer()==true) {
				// --- Start the remote container -------------------
				System.out.println("Starting remote container ... ");
				jrs.startJade();
			}
			// +++ Returns here after container shutdown ++++++++++++
			this.getAgent().doDelete();
		}
	}

	
}
