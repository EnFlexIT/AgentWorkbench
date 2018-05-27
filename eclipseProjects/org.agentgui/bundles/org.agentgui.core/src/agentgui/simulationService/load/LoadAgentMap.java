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
package agentgui.simulationService.load;

import jade.core.AID;
import jade.core.Location;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import agentgui.simulationService.LoadService;

/**
 * This class provides a map that describes the locations of agents on the platform
 * and the number of them at the platform or at a specified container.
 * The class is used through the {@link LoadInformation}, which is part of the 
 * {@link LoadService}.
 *
 * @see LoadInformation
 * @see LoadService
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class LoadAgentMap implements Serializable {
	
	private static final long serialVersionUID = 7651064205614961934L;

	/** The agents at platform. */
	private Hashtable<String, AID_Container> agentsAtPlatform;
	
	/** The agents at container. */
	private Hashtable<String, AID_Container_List> agentsAtContainer;
	
	/** The no agents at platform. */
	public Integer noAgentsAtPlatform = null;
	
	/** The no agents at container. */
	private Hashtable<String, Integer> noAgentsAtContainer;
	
	/**
	 * Constructor of this class.
	 */
	public LoadAgentMap() {
	}
	
	/**
	 * This method counts all of the agents at the platform and
	 * the agents, which are located at one specific container.
	 */
	public void doCountings() {
		
		noAgentsAtPlatform = this.getAgentsAtPlatform().size();
		this.setNoAgentsAtContainerHash(null);
		
		Vector<String> container = new Vector<String>(this.getAgentsAtContainer().keySet());
		Iterator<String> it = container.iterator();
		while (it.hasNext()) {
			String containerName = it.next();
			AID_Container_List aidList = this.getAgentsAtContainer().get(containerName);
			if (aidList==null) {
				this.getNoAgentsAtContainerHash().put(containerName, 0);	
			} else {
				this.getNoAgentsAtContainerHash().put(containerName, aidList.size());
			}
		} // --- end while ---
	}
	
	/**
	 * Which agents are available at the platform.
	 *
	 * @param containerName the container name
	 * @param aid the AID of an agent
	 * @param hasServiceSensor the has service sensor
	 */
	public void put(String containerName, AID aid, boolean hasServiceSensor) {
		if (aid==null) {
			this.put2AgentsAtContainer(containerName, null);
		} else {
			AID_Container cAID = new AID_Container(aid);
			cAID.setContainerName(containerName);
			cAID.setServiceSensor(hasServiceSensor);
			// --- Put into the list of the Platform ------
			this.getAgentsAtPlatform().put(aid.getLocalName(), cAID);
			// --- Put also into the List for Containers --
			this.put2AgentsAtContainer(containerName, cAID);
		}
	}
	
	/**
	 * This method stores the information which agents are in which container.
	 *
	 * @param containerName the container name
	 * @param cAID the AID_Container
	 */
	private void put2AgentsAtContainer(String containerName, AID_Container cAID) {
		AID_Container_List agentList = this.getAgentsAtContainer().get(containerName);
		if (agentList==null) {
			agentList = new AID_Container_List(); 
		}
		if (cAID!=null) {
			agentList.put(cAID.getAID().getLocalName(), cAID);	
		}
		this.getAgentsAtContainer().put(containerName, agentList);
	}
	
	/**
	 * Provides the agents at the whole platform.
	 * @return the agentsAtPlatform
	 */
	public Hashtable<String, AID_Container> getAgentsAtPlatform() {
		if (agentsAtPlatform==null) {
			agentsAtPlatform = new Hashtable<>();
		}
		return agentsAtPlatform;
	}
	/**
	 * Gets the agents at container.
	 * @return the agentsAtContainer
	 */
	public Hashtable<String, AID_Container_List> getAgentsAtContainer() {
		if (agentsAtContainer==null) {
			agentsAtContainer = new Hashtable<>();
		}
		return agentsAtContainer;
	}

	/**
	 * Gets the no agents at container hash.
	 * @return the no agents at container hash
	 */
	public Hashtable<String, Integer> getNoAgentsAtContainerHash() {
		if (noAgentsAtContainer==null) {
			noAgentsAtContainer = new Hashtable<String, Integer>();
		}
		return noAgentsAtContainer;
	}
	/**
	 * Sets the no agents at container hash.
	 * @param newHashTable the new hash table
	 */
	private void setNoAgentsAtContainerHash(Hashtable<String, Integer> newHashTable) {
		noAgentsAtContainer = newHashTable;
	}
	
	/**
	 * Search's for the stored agent information. With this, the current
	 * location of the agent can be found for example
	 *
	 * @param agentsAID the agents AID
	 * @return the AID_Container of the agent
	 */
	public AID_Container getAID_Container(AID agentsAID) {
				
		String[] aidArr = new String[this.getAgentsAtPlatform().keySet().size()];  
		aidArr = (String[]) this.getAgentsAtPlatform().keySet().toArray();
		
		for (int i = 0; i < aidArr.length; i++) {
			String key = aidArr[i];
			AID_Container cAID = this.getAgentsAtPlatform().get(key);
			if (cAID.getAID().equals(agentsAID) ) {
				return cAID;
			}
		}
		return null;
	}
	

	// ----------------------------------------------------------
	// --- Sub-Class AID_Container_List --- S T A R T -----------
	// ----------------------------------------------------------
	/**
	 * The Class AID_Container_List is an extended <code>Hashtable<String, AID_Container></code>.
	 */
	public class AID_Container_List extends Hashtable<String, AID_Container> implements Serializable {
		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -575499631355769830L;
		
		/**
		 * Instantiates a new AID_Container_List.
		 */
		public AID_Container_List() {
			super();
		}
	}
	// ----------------------------------------------------------
	// --- Sub-Class AID_Container_List --- E N D ---------------
	// ----------------------------------------------------------
	
	// ----------------------------------------------------------
	// --- Sub-Class AID_Container --- S T A R T ----------------
	// ----------------------------------------------------------
	/**
	 * The Class AID_Container is used to extend the information of an agent, behind
	 * the point of just knowing its AID.
	 */
	public class AID_Container implements Serializable {
		
		private static final long serialVersionUID = 3077331688501516668L;
		
		private AID agentAID = null;
		private String containerName = null;
		private boolean serviceSensor = false;
		private Location newlocation = null;
		
		/**
		 * Instantiates a new AID_Container.
		 * @param aid the AID of an agent
		 */
		public AID_Container(AID aid) {
			setAID(aid);
		}
		
		/**
		 * Sets the AID.
		 * @param agentAID the AID of the agent.
		 */
		public void setAID(AID agentAID) {
			this.agentAID = agentAID;
		}
		/**
		 * Provides the AID of an agent.
		 * @return the AID of the agent
		 */
		public AID getAID() {
			return this.agentAID;
		}
		
		/**
		 * Sets the container name.
		 * @param containerName the containerName to set
		 */
		public void setContainerName(String containerName) {
			this.containerName = containerName;
		}
		/**
		 * Gets the container name.
		 * @return the containerName
		 */
		public String getContainerName() {
			return this.containerName;
		}
		
		/**
		 * Sets the availability of a service sensor.
		 * @param hasSensor the new service sensor
		 */
		public void setServiceSensor(boolean hasSensor) {
			this.serviceSensor = hasSensor;
		}
		/**
		 * Checks for service sensor.
		 * @return true, if an sensor is available 
		 */
		public boolean hasServiceSensor() {
			return serviceSensor;
		}
		
		/**
		 * Sets the new location for an agent.
		 * @param newLocation the new location
		 */
		public void setNewLocation(Location newLocation) {
			this.newlocation = newLocation;
		}
		/**
		 * Gets the new location for an agent.
		 * @return the new location
		 */
		public Location getNewLocation() {
			return newlocation;
		}
		
	}
	
}
