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
package agentgui.simulationService.transaction;

import java.io.Serializable;

import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.networkModel.NetworkModel;
import agentgui.envModel.graph.visualisation.DisplayAgent;
import agentgui.simulationService.agents.SimulationManagerAgent;
import agentgui.simulationService.environment.EnvironmentModel;

/**
 * The abstract Class DisplayAgentNotification is the base class for notifications that can 
 * be directed to display agents that are working on an environment model, located in
 * {@link EnvironmentModel#getDisplayEnvironment()}. 
 * For the possible notifications that can be send to specialised display agents, have 
 * a look at the corresponding packages of the special environment model. 
 * As example see the links of 'See also:'
 *  
 * @see SimulationManagerAgent
 * @see GraphEnvironmentController#getDisplayEnvironmentModel()
 * @see NetworkModel
 * @see DisplayAgent
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class DisplayAgentNotification implements Serializable {

	private static final long serialVersionUID = -2755204178753148481L;
	
}
