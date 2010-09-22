package mas.service.load;

import jade.core.AID;
import jade.core.Location;

import java.util.Hashtable;
import java.util.Iterator;


import mas.service.distribution.ontology.BenchmarkResult;
import mas.service.distribution.ontology.ClientRemoteContainerReply;
import mas.service.distribution.ontology.OSInfo;
import mas.service.distribution.ontology.PlatformLoad;
import mas.service.distribution.ontology.PlatformPerformance;

/**
 * Manages all Load-Informations of the Containers, which
 * are connected to this Main-Container
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
 */
public class LoadInformation  {

	private static final long serialVersionUID = -8535049489754734307L;
	
	public Hashtable<String, NodeDescription> containerDescription = new Hashtable<String, NodeDescription>();
	public Hashtable<String, PlatformLoad> containerLoads = new Hashtable<String, PlatformLoad>(); 
	public Hashtable<String, Location> containerLocations = new Hashtable<String, Location>();
	public AgentMap agentLocations = new AgentMap();
	
	private Hashtable<String, Container2Wait4> newContainers2Wait4 = new Hashtable<String, Container2Wait4>();
	
	/**
	 * Constructor of this class
	 */
	public LoadInformation() {
		
	}
	
	/**
	 * Method to put the answer of the Server.Master directly 
	 * in the local public containerDescription - Variable
	 * @param crcReply
	 */
	public void putContainerDescription(ClientRemoteContainerReply crcReply) {
		NodeDescription node = new NodeDescription(crcReply);
		this.containerDescription.put(node.getContainerName(), node);
	}
	/**
	 * @param lastNewContainer the lastNewContainer to set
	 */
	public void setNewContainer2Wait4(String container2Wait4) {
		Container2Wait4 cont = new Container2Wait4(container2Wait4);
		this.newContainers2Wait4.put(container2Wait4, cont);
	}
	/**
	 * Returns the status of the New RemoteContainerRequest
	 * @param containerName
	 * @return
	 */
	public Container2Wait4 getNewContainer2Wait4Status(String containerName) {
		return newContainers2Wait4.get(containerName);
	}
	/**
	 * This method sets that the new RemoteContainerRequest
	 * was successfully
	 * @param containerName
	 */
	public void setNewContainerStarted(String containerName) {
		Container2Wait4 cont = newContainers2Wait4.get(containerName);
		if (cont!=null ) {
			cont.setStarted(true);
		}
	}
	/**
	 * This method sets that the new RemoteContainerRequest
	 * was NOT successfully
	 * @param containerName
	 */
	public void setNewContainerCanncelled(String containerName) {
		Container2Wait4 cont = newContainers2Wait4.get(containerName);
		if (cont!=null ) {
			cont.setCancelled(true);
		}
	}
	
	/**
	 * Resets the Object, which holds the Informations about 
	 * the agent in relation to it's locations etc.
	 */
	public void resetAIDs4Container() {
		agentLocations = new AgentMap();		
	}
	/**
	 * Method to put the Agent-AIDs which are coming from the 
	 * currently running containers 
	 * @param aid
	 */
	public void putAIDs4Container(String containerName, AID[] aid) {
		if (aid.length==0) {
			agentLocations.put(containerName, null);
		} else {
			for (int i = 0; i < aid.length; i++) {
				agentLocations.put(containerName, aid[i]);			
			}
		}
	}
	/**
	 * This method starts the countings for the agents at the platform 
	 * and the agents, which are located at one specific container  
	 */
	public void countAIDs4Container() {
		agentLocations.doCountings();
	}
	
	// --------------------------------------------------------------
	// --- Sub-Class AgentMap --- S T A R T -------------------------
	// --------------------------------------------------------------
	public class AgentMap {
	
		private Hashtable<String, AID_Container> agentsAtPlatform = new Hashtable<String, AID_Container>();
		private Hashtable<String, AID_Container_List> agentsAtContainer = new Hashtable<String, AID_Container_List>();
		
		public Integer noAgentsAtPlatform = null;
		public Hashtable<String, Integer> noAgentsAtContainer = null;
		
		/**
		 * Constructor(s) of this class
		 */
		public AgentMap() {
		}
		
		/**
		 * This method counts all of the agents at the platform and 
		 * the agents, which are located at one specific container  
		 */
		public void doCountings() {
			
			noAgentsAtPlatform = agentsAtPlatform.size();
			noAgentsAtContainer = new Hashtable<String, Integer>();
			
			Iterator<String> it = agentsAtContainer.keySet().iterator();
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
		 * Which Agents are available at the platform
		 * @param aid
		 */
		public void put(String containerName, AID aid) {
			if (aid==null) {
				this.put2AgentsAtContainer(containerName, null);
			} else {
				AID_Container cAID = new AID_Container(aid);
				cAID.setContainerName(containerName);
				// --- Put into the list of the Platform ------
				this.agentsAtPlatform.put(aid.getLocalName(), cAID);
				// --- Put also into the List for Containers --
				this.put2AgentsAtContainer(containerName, cAID);
			}
		}
		/**
		 * Which Agents are in which container
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
		
		// ----------------------------------------------------------
		// --- Sub-Class AID_Container_List --- S T A R T -----------
		// ----------------------------------------------------------
		private class AID_Container_List extends Hashtable<String, AID_Container> {
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
		public class AID_Container {
			
			private static final long serialVersionUID = 3077331688501516668L;
			private AID agentAID = null;
			private String containerName = null;
				
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
			
		}
		// ----------------------------------------------------------
		// --- Sub-Class AID_Container --- E N D E ------------------
		// ----------------------------------------------------------
		
	}
	// --------------------------------------------------------------
	// --- Sub-Class AgentMap --- E N D -----------------------------
	// --------------------------------------------------------------
	
	
	// --------------------------------------------------------------
	// --- Sub-Class NodeDescription --- S T A R T ------------------
	// --------------------------------------------------------------
	/**
	 * This class describes the container(computer), which are 
	 * connected to the Main-Container. There are informations about
	 * CPU, Memory and operating system available as well as the
	 * value from benchmark-test
	 */
	public class NodeDescription {
		
		private String containerName = null;
		private OSInfo osInfo = null;
		private PlatformPerformance plPerformace = null;
		private BenchmarkResult benchmarkValue = null;
		
		/**
		 * Constructor of this Sub-Class
		 */
		public NodeDescription() {
		
		}
		/**
		 * Constuructor of this Sub-Class, using the 
		 * ClientRemoteContainerReply from the Server.Master
		 * @param crcReply
		 */
		public NodeDescription(ClientRemoteContainerReply crcReply) {
			containerName = crcReply.getRemoteContainerName();
			osInfo = crcReply.getRemoteOS();
			plPerformace = crcReply.getRemotePerformance();
			benchmarkValue = crcReply.getRemoteBenchmarkResult();
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
			return containerName;
		}
		
		/**
		 * @return the osInfo
		 */
		public OSInfo getOsInfo() {
			return osInfo;
		}
		/**
		 * @param osInfo the osInfo to set
		 */
		public void setOsInfo(OSInfo osInfo) {
			this.osInfo = osInfo;
		}
		
		/**
		 * @return the plPerformace
		 */
		public PlatformPerformance getPlPerformace() {
			return plPerformace;
		}
		/**
		 * @param plPerformace the plPerformace to set
		 */
		public void setPlPerformace(PlatformPerformance plPerformace) {
			this.plPerformace = plPerformace;
		}
		
		/**
		 * @return the benchmarkValue
		 */
		public BenchmarkResult getBenchmarkValue() {
			return benchmarkValue;
		}
		/**
		 * @param benchmarkValue the benchmarkValue to set
		 */
		public void setBenchmarkValue(BenchmarkResult benchmarkValue) {
			this.benchmarkValue = benchmarkValue;
		}
		
	}
	// --------------------------------------------------------------
	// --- Sub-Class NodeDescription --- E N D ----------------------
	// --------------------------------------------------------------

	
	// --------------------------------------------------------------
	// --- Sub-Class NewContainer2Wait4 --- S T A R T ---------------
	// --------------------------------------------------------------
	public class Container2Wait4 {
		
		private String containerName = null;
		private long timeOut = (1000 * 10); // --- 10 Seconds ---
		private long time4TimeOut = 0;
		private boolean started = false;
		private boolean cancelled = false;
		
		/**
		 * Constructor of this Sub-Class
		 */
		public Container2Wait4(String newContainername) {
			setContainerName(newContainername);
			time4TimeOut = System.currentTimeMillis() + timeOut;
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
			return containerName;
		}
		
		/**
		 * @return if the RemoteContainerRequest is timed out or not
		 */
		public boolean isTimedOut(){
			if (System.currentTimeMillis() > time4TimeOut) {
				return true;
			} else {
				return false;	
			}
		}
		
		/**
		 * @param started the started to set
		 */
		public void setStarted(boolean started) {
			this.started = started;
		}
		/**
		 * @return the started
		 */
		public boolean isStarted() {
			return started;
		}

		/**
		 * @param chanceld the cancelled to set
		 */
		public void setCancelled(boolean chanceld) {
			this.cancelled = chanceld;
		}
		/**
		 * @return the cancelled-boolean
		 */
		public boolean isCancelled() {
			return cancelled;
		}

	}
	// --------------------------------------------------------------
	// --- Sub-Class NewContainer2Wait4 --- E N D -------------------
	// --------------------------------------------------------------
	
	
}
