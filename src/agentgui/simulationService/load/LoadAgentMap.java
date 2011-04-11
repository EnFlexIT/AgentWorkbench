package agentgui.simulationService.load;

import jade.core.AID;
import jade.core.Location;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;


public class LoadAgentMap implements Serializable {
	
	private static final long serialVersionUID = 7651064205614961934L;

	private Hashtable<String, AID_Container> agentsAtPlatform = new Hashtable<String, AID_Container>();
	private Hashtable<String, AID_Container_List> agentsAtContainer = new Hashtable<String, AID_Container_List>();
	
	public Integer noAgentsAtPlatform = null;
	public Hashtable<String, Integer> noAgentsAtContainer = null;
	
	/**
	 * Constructor of this class
	 */
	public LoadAgentMap() {
	}
	
	/**
	 * This method counts all of the agents at the platform and 
	 * the agents, which are located at one specific container  
	 */
	public void doCountings() {
		
		noAgentsAtPlatform = agentsAtPlatform.size();
		noAgentsAtContainer = new Hashtable<String, Integer>();
		
		Vector<String> container = new Vector<String>(agentsAtContainer.keySet());
		Iterator<String> it = container.iterator();
		while (it.hasNext()) {
			String containerName = it.next();
			AID_Container_List aidList = agentsAtContainer.get(containerName);
			if (aidList==null) {
				noAgentsAtContainer.put(containerName,0);	
			} else {
				noAgentsAtContainer.put(containerName,aidList.size());
			}
		} // --- end while ---
	}
	
	/**
	 * Which agents are available at the platform
	 * @param aid
	 */
	public void put(String containerName, AID aid, boolean hasServiceSensor) {
		if (aid==null) {
			this.put2AgentsAtContainer(containerName, null);
		} else {
			AID_Container cAID = new AID_Container(aid);
			cAID.setContainerName(containerName);
			cAID.setServiceSensor(hasServiceSensor);
			// --- Put into the list of the Platform ------
			this.agentsAtPlatform.put(aid.getLocalName(), cAID);
			// --- Put also into the List for Containers --
			this.put2AgentsAtContainer(containerName, cAID);
		}
	}
	/**
	 * Which agents are in which container
	 * @param containeName
	 * @param aid
	 */
	private void put2AgentsAtContainer(String containerName, AID_Container cAID) {
		AID_Container_List agentList = agentsAtContainer.get(containerName);
		if (agentList==null) {
			agentList = new AID_Container_List(); 
		}
		if (cAID!=null) {
			agentList.put(cAID.getAID().getLocalName(), cAID);	
		}
		agentsAtContainer.put(containerName, agentList);
	}
	
	/**
	 * @return the agentsAtPlatform
	 */
	public Hashtable<String, AID_Container> getAgentsAtPlatform() {
		return agentsAtPlatform;
	}
	/**
	 * @return the agentsAtContainer
	 */
	public Hashtable<String, AID_Container_List> getAgentsAtContainer() {
		return agentsAtContainer;
	}

	/**
	 * Search's for the stored agent information. With this, the current 
	 * location of the agent can be found for example 
	 * @param agentAddress
	 * @return
	 */
	public AID_Container getcAID(AID agentAddress) {
				
		String[] aidArr = new String[this.agentsAtPlatform.keySet().size()];  
		aidArr = (String[]) this.agentsAtPlatform.keySet().toArray();
		
		for (int i = 0; i < aidArr.length; i++) {
			String key = aidArr[i];
			AID_Container cAID = this.agentsAtPlatform.get(key);
			if (cAID.getAID().equals(agentAddress) ) {
				return cAID;
			}
		}
		return null;
	}
	

	// ----------------------------------------------------------
	// --- Sub-Class AID_Container_List --- S T A R T -----------
	// ----------------------------------------------------------
	public class AID_Container_List extends Hashtable<String, AID_Container> implements Serializable {
		private static final long serialVersionUID = -575499631355769830L;
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
	public class AID_Container implements Serializable {
		
		private static final long serialVersionUID = 3077331688501516668L;
		private AID agentAID = null;
		private String containerName = null;
		private boolean serviceSensor = false;
		private Location newlocation = null;
		
		public AID_Container(AID aid) {
			setAID(aid);
		}
		/**
		 * @param agentAID the agentAID to set
		 */
		public void setAID(AID agentAID) {
			this.agentAID = agentAID;
		}
		/**
		 * @return the agentAID
		 */
		public AID getAID() {
			return this.agentAID;
		}
		/**
		 * @param containerName the containerName to set
		 */
		public void setContainerName(String containerName) {
			this.containerName = containerName;
		}
		/**
		 * @return the containerName
		 */
		public String getContainerName() {
			return this.containerName;
		}
		/**
		 * @param sensor the sensor to set
		 */
		public void setServiceSensor(boolean hasSensor) {
			this.serviceSensor = hasSensor;
		}
		/**
		 * @return the sensor
		 */
		public boolean hasServiceSensor() {
			return serviceSensor;
		}
		/**
		 * @param newlocation the newlocation to set
		 */
		public void setNewLocation(Location newLocation) {
			this.newlocation = newLocation;
		}
		/**
		 * @return the newlocation
		 */
		public Location getNewLocation() {
			return newlocation;
		}
		
	}
	
}
