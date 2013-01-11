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
package agentgui.core.project;

import java.io.Serializable;
import java.util.Vector;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

/**
 * The Class AgentConfiguration is used in the class {@link Project} in order
 * to save the configuration of the projects agents.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen 
 */
public class AgentStartConfiguration implements Serializable {

	private static final long serialVersionUID = 2162042661773211437L;

	@XmlElementWrapper(name = "agentVector")
	@XmlElement(name = "agent")
	private Vector<AgentStartArguments> agentVector = null;
	
	
	/**
	 * Sets the agent start arguments.
	 * @param agentStartArguments the new agent start arguments
	 */
	public void setAgentStartArguments(Vector<AgentStartArguments> agentStartArguments) {
		this.agentVector = agentStartArguments;
	}
	/**
	 * Returns the agent start arguments.
	 * @return the agent start arguments
	 */
	@XmlTransient
	public Vector<AgentStartArguments> getAgentStartArguments() {
		if (this.agentVector==null) {
			this.agentVector = new Vector<AgentStartArguments>();
		}
		return agentVector;
	}

	/**
	 * Gets the agent start arguments.
	 *
	 * @param agentReference the agent reference
	 * @return the agent start arguments
	 */
	public AgentStartArguments getAgentStartArguments(String agentReference) {
		if (agentReference!=null) {
			for (AgentStartArguments agentArguments:this.getAgentStartArguments()) {
	 			if (agentArguments.getAgentReference().equals(agentReference)) {
	 				return agentArguments;
	 			}
			}	
		}
		return null;
	}

	public boolean containsKey(String agentReference) {
		AgentStartArguments agentStartArguments = this.getAgentStartArguments(agentReference);
		if (agentStartArguments==null) {
			return false;
		} else {
			return true;
		}
	}
	/**
	 * Gets the start arguments for a single agent.
	 * 	
	 * @param agentReference the agent reference
	 * @return the vector of start arguments configured for this agent
	 */
	public Vector<AgentStartArgument> get(String agentReference) {
		AgentStartArguments agentArguments = this.getAgentStartArguments(agentReference);
		if (agentArguments!=null) {
			return agentArguments.getStartArguments();
		}
		return null;
	}
	
	
	/**
	 * Puts a new configuration for an agent into the AgentConfiguration.
	 *
	 * @param agentReference the agent reference
	 * @param argumentVector the argument vector
	 */
	private void put(String agentReference, Vector<AgentStartArgument> argumentVector) {
		
		// --- Create new Vector element --------
		AgentStartArguments newAgentStartArguments = new AgentStartArguments(agentReference);
		newAgentStartArguments.setStartArguments(argumentVector);
		
		// --- Remove old element, if there -----
		AgentStartArguments oldAgentStartArguments = this.getAgentStartArguments(agentReference);
		if (oldAgentStartArguments!=null) {
			this.getAgentStartArguments().remove(oldAgentStartArguments);
		}
		this.getAgentStartArguments().add(newAgentStartArguments);
	}
	
	/**
	 * Removes the start configuration of an agent.
	 * @param agentReference the agent reference
	 */
	public void remove(String agentReference) {
		// --- Remove old element, if there -----
		AgentStartArguments oldAgentStartArguments = this.getAgentStartArguments(agentReference);
		if (oldAgentStartArguments!=null) {
			this.getAgentStartArguments().remove(oldAgentStartArguments);
		}
	}
	
	// -------------
	// --- 	
	// -------------
	
	/**
	 * Adds a further ontoReference as start argument to the vector of start arguments.
	 *
	 * @param agentReference the agent reference
	 * @param ontoReference the onto reference
	 * @return the index position of the new element
	 */
	public int addReference(String agentReference, String ontoReference) {
		
		if (ontoReference==null) return -1;
		
		Vector<AgentStartArgument> argumentVector = this.get(agentReference);
		if (argumentVector==null) {
			argumentVector = new Vector<AgentStartArgument>();
			this.put(agentReference, argumentVector);
		}
		AgentStartArgument agentStartArgument = new AgentStartArgument(argumentVector.size()+1, ontoReference);
		argumentVector.addElement(agentStartArgument);
		return argumentVector.size()-1;
	}
	
	/**
	 * Removes the reference.
	 *
	 * @param agentReference the agent reference
	 * @param selectedIndex the index position
	 * @return the index of the new focus position
	 */
	public int removeReference(String agentReference, int selectedIndex) {
		
		if (selectedIndex==-1) return 0;
		
		Vector<AgentStartArgument> argumentVector = this.get(agentReference);
		argumentVector.remove(selectedIndex);
		
		// --- Renumber all elements ------------
		int counter=1;
		for (AgentStartArgument agentStartArgument : argumentVector) {
			agentStartArgument.setPosition(counter);
			counter++;
		}
		
		int newFocusPosition = selectedIndex;
		if (newFocusPosition > (argumentVector.size()-1)) {
			newFocusPosition--;
		}
		return newFocusPosition;
	}
	
	/**
	 * Removes the all references.
	 * @param agentReference the agent reference
	 */
	public void removeAllReferences(String agentReference) {
		this.remove(agentReference);
	}

	/**
	 * Move position.
	 *
	 * @param agentReference the agent reference
	 * @param selectedIndex the selected index
	 * @param direction the direction
	 * @return true, if successful
	 */
	public int movePosition(String agentReference, int selectedIndex, int direction) {

		int newSelectedIndex = -1;
		if (agentReference==null) return newSelectedIndex;
		if (selectedIndex==-1) return newSelectedIndex;
		
		Vector<AgentStartArgument> argumentVector = this.get(agentReference);
		if (selectedIndex>argumentVector.size()-1) return newSelectedIndex;
		
		// --- move element ---------------------
		AgentStartArgument agentStartArgument = argumentVector.remove(selectedIndex);
		if (direction>0) {
			newSelectedIndex = selectedIndex+1;
			if (newSelectedIndex>argumentVector.size()) {
				newSelectedIndex = argumentVector.size();
			}
			argumentVector.add(newSelectedIndex, agentStartArgument);
			
		} else if (direction<0) {
			newSelectedIndex = selectedIndex-1;
			if (newSelectedIndex<0) {
				newSelectedIndex=0;
			}
			argumentVector.add(newSelectedIndex, agentStartArgument);
			
		}
		
		// --- renumber list --------------------
		int counter=1;
		for (AgentStartArgument argument : argumentVector) {
			argument.setPosition(counter);
			counter++;
		}
		return newSelectedIndex;
	}
	
}
