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
package gasmas.clustering;

import gasmas.clustering.coalitions.CoalitionBehaviour;
import jade.core.behaviours.SimpleBehaviour;

/**
 * The Class ClusteringBehaviour.
 */
abstract public class ClusteringBehaviour extends SimpleBehaviour {

	public static final String CLUSTER_NETWORK_MODL_NAME = "ClusterdNM";

	/** The coalition behaviour. */
	protected CoalitionBehaviour coalitionBehaviour;

	/**
	 * Sets the coalition behaviours.
	 *
	 * @param coalitionBehaviour the new coalition behaviours
	 */
	public void setCoalitionBehaviours(CoalitionBehaviour coalitionBehaviour) {
		this.coalitionBehaviour = coalitionBehaviour;
	}

	/**
	 * Gets the coalition behaviour.
	 *
	 * @return the coalition behaviour
	 */
	public CoalitionBehaviour getCoalitionBehaviour() {
		return coalitionBehaviour;
	}

}
