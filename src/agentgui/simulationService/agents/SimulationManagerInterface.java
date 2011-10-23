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

import agentgui.simulationService.environment.EnvironmentModel;

/**
 * The SimulationManagerInterface used in the {@link SimulationManagerAgent}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public interface SimulationManagerInterface {

	/**
	 * This method is used for initialising the simulation during the .setup()-method of the agent.
	 * Here the environment model (see class agentgui.simulationService.environment.EnvironmentModel)
	 * should be set.
	 *
	 * @return the initial environment model
	 */
	public EnvironmentModel getInitialEnvironmentModel();
	
	/**
	 * 	The logic of the simulation is implemented here. It's highly recommended to use 
	 *  the provided methods for implementing the logic.
	 */
	public abstract void doSingleSimulationSequennce();
	
	
}
