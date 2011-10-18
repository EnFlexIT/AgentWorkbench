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
package agentgui.envModel.p2Dsvg.provider;

import java.util.HashSet;
import java.util.Iterator;

import org.w3c.dom.Document;

import agentgui.envModel.p2Dsvg.ontology.ActiveObject;
import agentgui.envModel.p2Dsvg.ontology.PassiveObject;
import agentgui.envModel.p2Dsvg.ontology.Physical2DEnvironment;

import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.TickerBehaviour;

/**
 * Agent managing a Physical2dEnvironment instance.
 *
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 */
public class EnvironmentProviderAgent extends Agent {
	
	/** serialVersionUID. */
	private static final long serialVersionUID = -2239610711152280115L;
	
	/** Number of milliseconds between two position updates. */
	private final int PERIOD = 100;
	
	/** The helper. */
	private EnvironmentProviderHelper helper = null;
	
	
	/**
	 * Setup method
	 * This method initializes environment and envWrap properties and registers this agent at the local EnvironmentProviderService.
	 */
	public void setup(){
		Object[] args = getArguments();
		if(args != null && args[0] != null && args[0] instanceof Physical2DEnvironment){
			try {
				this.helper = (EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
				this.helper.setEnvironment((Physical2DEnvironment) args[0]);
				if(args[1] != null && args[1] instanceof Document){
					helper.setSVGDoc((Document) args[1]);
				}
				this.addBehaviour(new UpdatePositionsBehaviour(this, PERIOD));
			} catch (ServiceException e) {
				System.err.println(getLocalName()+" - Error: Environment provider service not found, shutting down!");
				doDelete();
			}
		}else{
			System.err.println(getLocalName()+" - Error: No Physical2DEnvironment given, shutting down!");
			doDelete();
		}
		
	}
	
	/**
	 * This behaviour updates the positions of all moving environment objects.
	 *
	 * @author Nils
	 */
	private class UpdatePositionsBehaviour extends TickerBehaviour{
		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 3819944524810503066L;
		
		/** The helper. */
		private EnvironmentProviderHelper helper;

		/**
		 * Instantiates a new update positions behaviour.
		 *
		 * @param a the a
		 * @param period the period
		 * @throws ServiceException the service exception
		 */
		public UpdatePositionsBehaviour(Agent a, long period) throws ServiceException {
			super(a, period);
			helper = (EnvironmentProviderHelper) getHelper(EnvironmentProviderService.SERVICE_NAME);
		}

		/* (non-Javadoc)
		 * @see jade.core.behaviours.TickerBehaviour#onTick()
		 */
		@SuppressWarnings("unchecked")
		@Override
		protected synchronized void onTick() {
			// --- CD: Bug wg. concurrent Exception behoben ----
			HashSet<ActiveObject> moving = new HashSet<ActiveObject>( helper.getCurrentlyMovingAgents() );
			if(moving.size() > 0){	// Any moving agents?
				
				Iterator<ActiveObject> movingAgents = moving.iterator();
				while(movingAgents.hasNext()){
					ActiveObject movingAgent = movingAgents.next();
					
					// Calculate position change per period
					float xPosChange = movingAgent.getMovement().getXPosChange()/1000*PERIOD;
					float yPosChange = movingAgent.getMovement().getYPosChange()/1000*PERIOD;
					
					// Update the agent's position
					movingAgent.getPosition().setXPos(movingAgent.getPosition().getXPos() + xPosChange);
					movingAgent.getPosition().setYPos(movingAgent.getPosition().getYPos() + yPosChange);
					
					// Update the positions of all PassiveObjects controlled by this agent, if any
					Iterator<PassiveObject> controlledObjects = movingAgent.getAllPayload();
					while(controlledObjects.hasNext()){
						PassiveObject controlledObject = controlledObjects.next();
						controlledObject.getPosition().setXPos(controlledObject.getPosition().getXPos() + xPosChange);
						controlledObject.getPosition().setYPos(controlledObject.getPosition().getYPos() + yPosChange);
					}
				}
			}
		}
	}
}
