package agentgui.simulationService.load;

import jade.core.AID;
import jade.core.Location;

import java.util.Hashtable;
import java.util.Vector;

import agentgui.simulationService.ontology.BenchmarkResult;
import agentgui.simulationService.ontology.ClientRemoteContainerReply;
import agentgui.simulationService.ontology.OSInfo;
import agentgui.simulationService.ontology.PlatformAddress;
import agentgui.simulationService.ontology.PlatformLoad;
import agentgui.simulationService.ontology.PlatformPerformance;


/**
 * Manages all Load-Informations of the Containers, which
 * are connected to this Main-Container
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
 */
public class LoadInformation  {

	private static final long serialVersionUID = -8535049489754734307L;
	
	public Vector<String> containerQueue = new Vector<String>();
	public Hashtable<String, NodeDescription> containerDescription = new Hashtable<String, NodeDescription>();
	public Hashtable<String, PlatformLoad> containerLoads = new Hashtable<String, PlatformLoad>(); 
	public Hashtable<String, Location> containerLocations = new Hashtable<String, Location>();
	public LoadAgentMap agentLocations = new LoadAgentMap();
	public Vector<AID> sensorAgents = null;
	
	private Hashtable<String, Container2Wait4> newContainers2Wait4 = new Hashtable<String, Container2Wait4>();
	
	private long cycleTimeStart = 0; 
	private Vector<Long> cycleTimeVector = new Vector<Long>();
	private long cycleTimeVectorMaxSize = 500;

	/**
	 * Constructor of this class
	 */
	public LoadInformation() {
		
	}
	
	/**
	 * Method to calculate the cycle-frequency
	 */
	public void setCycleStartTimeStamp() {
		
		long cycleTimeNow = System.currentTimeMillis();
		if (this.cycleTimeStart != 0) {
			long cycleTime = cycleTimeNow - this.cycleTimeStart;
			this.cycleTimeVector.addElement(cycleTime);
			if (this.cycleTimeVector.size() > this.cycleTimeVectorMaxSize) {
				this.cycleTimeVector.removeElementAt(0);	
			}
		}
		this.cycleTimeStart = cycleTimeNow;
	}
	/**
	 * Method to get the current cycle-frequency
	 */
	public double getAvgCycleTime() {
		
		if (this.cycleTimeVector.size()==0) {
			return 0;
		}
		
		long cycleTimeSum = 0;
		Long[] currentCycleTimeArray = new Long[cycleTimeVector.size()];
		currentCycleTimeArray = cycleTimeVector.toArray(currentCycleTimeArray);
		for (int i = 0; i < currentCycleTimeArray.length; i++) {
			cycleTimeSum += currentCycleTimeArray[i];
		}		
		return (double)cycleTimeSum/(double)currentCycleTimeArray.length;
	}
	
	
	/**
	 * Method to put the answer of the Server.Master directly 
	 * in the local public containerDescription - Variable
	 * @param crcReply
	 */
	public void putContainerDescription(ClientRemoteContainerReply crcReply) {
		NodeDescription node = new NodeDescription(crcReply);
		this.containerDescription.put(node.getContainerName(), node);
		if (containerQueue.contains(node.getContainerName())==false) {
			containerQueue.addElement(node.getContainerName());	
		}
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
	public void setNewContainerCancelled(String containerName) {
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
		agentLocations = new LoadAgentMap();		
	}
	/**
	 * Method to put the Agent-AIDs which are coming from the 
	 * currently running containers 
	 * @param aid
	 */
	public void putAIDs4Container(String containerName, AID[] aid) {
		
		boolean hasSensor = false;
		
		if (aid.length==0) {
			agentLocations.put(containerName, null, false);
		} else {
			for (int i = 0; i < aid.length; i++) {
				// --- ServiceSensor available? -----
				if (sensorAgents.contains(aid[i])==true) {
					hasSensor = true;
				} else {
					hasSensor = false;
				}
				agentLocations.put(containerName, aid[i], hasSensor);			
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
		private String jvmPID = null;
		private PlatformAddress plAddress = null;
		private OSInfo osInfo = null;
		private PlatformPerformance plPerformace = null;
		private BenchmarkResult benchmarkValue = null;
		
		/**
		 * Constructor of this Sub-Class
		 */
		public NodeDescription() {
		
		}
		/**
		 * Constructor of this Sub-Class, using the 
		 * ClientRemoteContainerReply from the Server.Master
		 * @param crcReply
		 */
		public NodeDescription(ClientRemoteContainerReply crcReply) {
			containerName = crcReply.getRemoteContainerName();
			jvmPID = crcReply.getRemotePID();
			setPlAddress(crcReply.getRemoteAddress());
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
		 * @param jvmPID the jvmPID to set
		 */
		public void setJvmPID(String jvmPID) {
			this.jvmPID = jvmPID;
		}
		/**
		 * @return the jvmPID
		 */
		public String getJvmPID() {
			return jvmPID;
		}
		
		/**
		 * @param plAddress the plAddress to set
		 */
		public void setPlAddress(PlatformAddress plAddress) {
			this.plAddress = plAddress;
		}
		/**
		 * @return the plAddress
		 */
		public PlatformAddress getPlAddress() {
			return plAddress;
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
		private long timeOut = (1000 * 20); // --- 10 Seconds ---
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
