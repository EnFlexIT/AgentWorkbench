/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://sourceforge.net/projects/agentgui/
 * http://www.dawis.wiwi.uni-due.de/ 
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
package agentgui.core.agents;

import jade.core.Agent;

/**
 * Provides a container instance in order to deal with 
 * agent classes in displayable lists, like JList and other
 * 
 * @author Hanno-Felix Wagner
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg-Essen
 */
public class AgentClassElement {
	private Class<? extends Agent> agentClass = null;
	
	/**
	 * Constructor for this class. Needs a class which extends
	 * from the class 'jade.core.Agent' 
	 * @param agentClass
	 */
	public AgentClassElement(Class<? extends Agent> agentClass){
		this.agentClass=agentClass;
	}
	/**
	 * Returns the name of the class
	 * @return String
	 */
	@Override
	public String toString(){
		return agentClass.getName();
	}
	/**
	 * Returns the agent class 
	 * @return Class<? extends Agent>
	 */
	public Class<? extends Agent> getElementClass(){
		return agentClass;
	}
}
