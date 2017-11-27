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
package agentgui.simulationService.agents.example;

import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import agentgui.simulationService.LoadService;
import agentgui.simulationService.LoadServiceHelper;
import agentgui.simulationService.load.LoadInformation.Container2Wait4;

/**
 * This is a simple agent, which is able to start a new remote container, if the background system is running.
 * After doing this once, the agents will be suspended. To restart this agent, open the JADE rma-Agent, select 
 * the agent in its container and press the 'Resume' button in the menu.<br>
 * The full class name is: <b>agentgui.simulationService.agents.example.RemoteStarterAgent</b>
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class RemoteStarterAgent extends Agent {
	
	private static final long serialVersionUID = 3649851139158388559L;

	/* (non-Javadoc)
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected void setup() {
		this.addBehaviour(new StarterCycle());
	}
	
	/**
	 * The Class StarterCycle.
	 */
	private class StarterCycle extends CyclicBehaviour{

		private static final long serialVersionUID = -3389907697703023520L;

		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#action()
		 */
		@Override
		public void action() {
		
			//this.block();
			// --- Start a new remote container -----------------
			LoadServiceHelper loadHelper;
			try {
				loadHelper = (LoadServiceHelper) myAgent.getHelper(LoadService.NAME);
				String newContainerName = loadHelper.startNewRemoteContainer();
				while (true) {
					Container2Wait4 waitCont = loadHelper.startNewRemoteContainerStaus(newContainerName);	
					if (waitCont.isStarted()) {
						System.out.println("Remote Container '" + newContainerName + "' was started!");
						break;
					}
					if (waitCont.isCancelled()) {
						System.out.println("Remote Container '" + newContainerName + "' was NOT started!");
						break;
					}
					if (waitCont.isTimedOut()) {
						System.out.println("Remote Container '" + newContainerName + "' timed out!");
						break;	
					}
					this.block(100);
				} // end while
				
			} catch (ServiceException exeption) {
				exeption.printStackTrace();
			}
			
			myAgent.doSuspend();
			
		}

		
	}
	
	
	
}
