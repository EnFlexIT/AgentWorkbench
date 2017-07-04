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
package agentgui.envModel.graph.visualisation.notifications;

import agentgui.simulationService.environment.EnvironmentModel;

/**
 * The Class EnvironmentModelUpdateNotification can be used in order to explicitly
 * distribute a new {@link EnvironmentModel} to the DisplayAgent.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class EnvironmentModelUpdateNotification extends DisplayAgentNotificationGraph {

	private static final long serialVersionUID = -224881716018746102L;

	private EnvironmentModel envModel;

	/**
	 * Instantiates a new environment model update notification.
	 * @param envMode the EnvironmentModel to use
	 */
	public EnvironmentModelUpdateNotification(EnvironmentModel envMode) {
		this.setEnvironmentModel(envMode);
	}

	/**
	 * Sets the environment model.
	 * @param newEnvironmentModel the new environment model
	 */
	public void setEnvironmentModel(EnvironmentModel newEnvironmentModel) {
		this.envModel = newEnvironmentModel;
	}
	/**
	 * Gets the environment model.
	 * @return the environment model
	 */
	public EnvironmentModel getEnvironmentModel() {
		return envModel;
	}
	
}
