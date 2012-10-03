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
package gasmas.adapter;

import jade.core.Agent;

import java.util.HashSet;
import java.util.Vector;

import agentgui.envModel.graph.networkModel.NetworkComponent;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.simulationService.behaviour.SimulationServiceBehaviour;

/**
 * The Class AA_TestAgen is an example agent that shows how an agent can be extended 
 * with an individual SimulationServiceBehaviour in order to use the SimulationService.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class AA_TestAgent extends Agent {

	private static final long serialVersionUID = -1852015424113418368L;

	/* (non-Javadoc)
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected void setup() {
		super.setup();
		this.addBehaviour(new SimServiceBehaviour(this));
	}
	
	
	/**
	 * The Class SimServiceBehaviour.
	 */
	private class SimServiceBehaviour extends SimulationServiceBehaviour {

		private static final long serialVersionUID = 1L;

		/**
		 * Instantiates a new sim service behaviour.
		 * @param agent the agent
		 */
		public SimServiceBehaviour(Agent agent) {
			super(agent);
			
			// --- In case that the start of the agent was to slowly, check --- 
			// --- environment model in the SimulationService 				---
			this.checkAndActOnEnvironmentChanges();
			
		}
		
		/* (non-Javadoc)
		 * @see agentgui.simulationService.behaviour.SimulationServiceBehaviour#onEnvironmentStimulus()
		 */
		@Override
		protected void onEnvironmentStimulus() {

			System.out.println("= = = > Got environment stimulus < = = = ");
			NetworkModel networkModel = (NetworkModel) myEnvironmentModel.getDisplayEnvironment();
			
			HashSet<String> netComps = new HashSet<String>(networkModel.getNetworkComponents().keySet()); 
			for (String netCompName : netComps) {
				// --- Get name and concrete NetworkComponentn ------
				NetworkComponent netComp = networkModel.getNetworkComponents().get(netCompName);
				// --- Get neighbour components ---------------------
				Vector<NetworkComponent> neighbourComps = networkModel.getNeighbourNetworkComponents(netComp);

				System.out.println("=> NetworkComponent: " + netCompName + " has " + neighbourComps.size() + " neighbour components !");
				
			}
		}
		
		
	}// end sub class / behaviour
	
}
