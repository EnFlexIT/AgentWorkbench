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
package de.enflexit.common.ontology;

import java.io.Serializable;
import java.util.Vector;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;


/**
 * The Class AgentStartArgument is used in AWB Projects as attribute
 * for the start configuration and a single (of many) start arguments for
 * a single agent.  
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class AgentStartArguments implements Serializable {

	private static final long serialVersionUID = -905464238108612354L;

	/** The agent reference. */
	@XmlAttribute
	private String agentReference = null;

	/** The start arguments. */
	@XmlElementWrapper(name="startArgumentVector")
	@XmlElement(name="startArgument")
	private Vector<AgentStartArgument> startArguments = null;
	
	
	/**
	 * Default constructor
	 */
	public AgentStartArguments() {
	}
	
	/**
	 * Instantiates a new agent start arguments.
	 * @param agentReference the agent reference
	 */
	public AgentStartArguments(String agentReference) {
		this.agentReference=agentReference;
	}
	
	/**
	 * Sets the agent reference.
	 * @param agentReference the new agent reference
	 */
	public void setAgentReference(String agentReference) {
		this.agentReference = agentReference;
	}
	/**
	 * Gets the agent reference.
	 * @return the agent reference
	 */
	@XmlTransient
	public String getAgentReference() {
		return agentReference;
	}
	
	/**
	 * Sets the start arguments.
	 * @param startArguments the new start arguments
	 */
	public void setStartArguments(Vector<AgentStartArgument> startArguments) {
		this.startArguments = startArguments;
	}
	/**
	 * Gets the start arguments.
	 * @return the start arguments
	 */
	@XmlTransient
	public Vector<AgentStartArgument> getStartArguments() {
		if (this.startArguments==null) {
			this.startArguments=new Vector<AgentStartArgument>();
		}
		return startArguments;
	}

}
