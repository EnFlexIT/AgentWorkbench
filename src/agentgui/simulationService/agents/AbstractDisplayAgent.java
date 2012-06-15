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
package agentgui.simulationService.agents;

import jade.core.ServiceException;
import agentgui.simulationService.SimulationService;
import agentgui.simulationService.SimulationServiceHelper;

/**
 * The Class VisualisationAgent can be used in order to build agents
 * that are passively observing environment changes that have to be
 * displayed by an extended VisualisationAgent.<br>
 * If the SimulationService is running, the VisualisationAgent will 
 * register there as a displaying agent. Furthermore the service holds<br>
 * In an extended SimulationMangerAgent
 * 
 * @see SimulationAgent
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class AbstractDisplayAgent extends SimulationAgent {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6499021588257662334L;

	
	/**
	 * Instantiates a new visualisation agent.
	 */
	public AbstractDisplayAgent() {
		// --- Initialise this agent as a passive ---------
		// --- SimulationAgent (or observing agent) ------- 
		super(true);
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#setup()
	 */
	@Override
	protected void setup() {
		super.setup();
		this.registerAsDisplayAgent();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#beforeMove()
	 */
	@Override
	protected void beforeMove() {
		this.unregisterAsDisplayAgent();
		super.beforeMove();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#afterMove()
	 */
	@Override
	protected void afterMove() {
		super.afterMove();
		this.registerAsDisplayAgent();
	}
	
	/* (non-Javadoc)
	 * @see agentgui.simulationService.agents.SimulationAgent#afterClone()
	 */
	@Override
	protected void afterClone() {
		super.afterClone();
		this.registerAsDisplayAgent();
	}
	
	@Override
	protected void takeDown() {
		this.unregisterAsDisplayAgent();
		super.takeDown();
	}
	
	
	/**
	 * Register this agent as DisplayAgent at the SimulationServie.
	 */
	protected void registerAsDisplayAgent() {
		try {
			SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			simHelper.displayAgentRegister(this.getAID());	
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Unregister this agent as DisplayAgent at the SimulationServie.
	 */
	protected void unregisterAsDisplayAgent() {
		try {
			SimulationServiceHelper simHelper = (SimulationServiceHelper) getHelper(SimulationService.NAME);
			simHelper.displayAgentUnregister(this.getAID());	
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
}
