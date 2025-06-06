package de.enflexit.awb.simulation.load;

import jade.core.AID;
import jade.core.Location;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import de.enflexit.awb.core.jade.Platform;
import de.enflexit.awb.simulation.LoadServiceHelper;
import de.enflexit.awb.simulation.ontology.BenchmarkResult;
import de.enflexit.awb.simulation.ontology.ClientRemoteContainerReply;
import de.enflexit.awb.simulation.ontology.OSInfo;
import de.enflexit.awb.simulation.ontology.PlatformAddress;
import de.enflexit.awb.simulation.ontology.PlatformLoad;
import de.enflexit.awb.simulation.ontology.PlatformPerformance;

/**
 * This class manages all Load-Informations of all container at the platform.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
 */
public class LoadInformation  {

	private Vector<String> containerNames;
	private Hashtable<String, NodeDescription> containerDescriptionHash;
	private Hashtable<String, PlatformLoad> containerLoadHash; 
	private Hashtable<String, Location> containerLocationHash;
	private LoadAgentMap agentLocations;
	private Vector<AID> sensorAgents;
	
	private Hashtable<String, Container2Wait4> newContainers2Wait4;
	
	private long cycleTimeStart = 0; 
	private Vector<Long> cycleTimeVector = new Vector<Long>();
	private long cycleTimeVectorMaxSize = 500;

	
	/**
	 * Returns the names of all known container.
	 * @return the container names
	 */
	public Vector<String> getContainerNames() {
		if (containerNames==null) {
			containerNames = new Vector<>();
		}
		return containerNames;
	}
	/**
	 * Returns the container description hash.
	 * @return the container description hash
	 */
	public Hashtable<String, NodeDescription> getContainerDescriptionHash() {
		if (containerDescriptionHash==null) {
			containerDescriptionHash = new Hashtable<>();
		}
		return containerDescriptionHash;
	}
	/**
	 * Returns the container load hash that holds load information by container name.
	 * @return the container loads
	 */
	public Hashtable<String, PlatformLoad> getContainerLoadHash() {
		if (containerLoadHash==null) {
			containerLoadHash = new Hashtable<>(); 
		}
		return containerLoadHash;
	}
	/**
	 * Returns the container location hash that describes the relation between container name and JADE Location.
	 * @return the container location hash
	 */
	public Hashtable<String, Location> getContainerLocationHash() {
		if (containerLocationHash==null) {
			containerLocationHash = new Hashtable<>();
		}
		return containerLocationHash;
	}
	
	/**
	 * Returns the {@link LoadAgentMap} that allows to answer: where can I find a specified agent).
	 * @return the load agent map
	 */
	public LoadAgentMap getLoadAgentMap() {
		if (agentLocations==null) {
			agentLocations = new LoadAgentMap(); 
		}
		return agentLocations;
	}
	/**
	 * Resets the Object, which holds the Informations about 
	 * the agent in relation to it's locations etc.
	 * @see #agentLocations
	 */
	public void resetLoadAgentMap() {
		agentLocations = null;		
	}
	/**
	 * Method to put the Agent-AIDs which are coming from the
	 * currently running containers.
	 *
	 * @param containerName the container name
	 * @param aid the AID array of the agents
	 * @see #agentLocations
	 */
	public void putAIDsToLoadAgentMap(String containerName, AID[] aid) {
		
		boolean hasSensor = false;
		
		if (aid.length==0) {
			this.getLoadAgentMap().put(containerName, null, false);
		} else {
			for (int i = 0; i < aid.length; i++) {
				// --- ServiceSensor available? -----
				if (this.getSensorAgents().contains(aid[i])==true) {
					hasSensor = true;
				} else {
					hasSensor = false;
				}
				this.getLoadAgentMap().put(containerName, aid[i], hasSensor);			
			}
		}
	}
	/**
	 * This method starts the counting for the agents at the platform 
	 * and the agents that are located at one specific container  
	 */
	public void doCountingsInLoadAgentMap() {
		this.getLoadAgentMap().doCountings();
	}
	
	/**
	 * Returns all known sensor agents (agents that are connected to an environment model 
	 * by the actuator-sensor relationship of the SimulationService).
	 * @return the sensor agents
	 */
	public Vector<AID> getSensorAgents() {
		if (sensorAgents==null) {
			sensorAgents = new Vector<>();
		}
		return sensorAgents;
	}
	/**
	 * Returns the Hashtable of new remote containers on which the current platform is waiting for.
	 * @return the new containers 2 wait 4 hash
	 */
	private Hashtable<String, Container2Wait4> getNewContainers2Wait4Hash() {
		if (newContainers2Wait4==null) {
			newContainers2Wait4 = new Hashtable<>(); 
		}
		return newContainers2Wait4;
	}
	
	/**
	 * Returns the list of pending {@link Container2Wait4}.
	 * @return the remote container pending
	 */
	public List<Container2Wait4> getRemoteContainerPending() {
		List<Container2Wait4> containerList = new ArrayList<>();
		for (Container2Wait4 c2w4 : this.getNewContainers2Wait4Hash().values()) {
			if (c2w4.isPending()==true) {
				containerList.add(c2w4);
			}
		}
		return containerList;
	}
	
	
	/**
	 * Method to calculate the cycle-frequency of the simulation.
	 */
	public void setSimulationCycleStartTimeStamp() {
		
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
	 * Method to get the current cycle-frequency in average. The average will be calculated 
	 * out of 500 single values in maximum. If a further cycle time occurs, the first measurement
	 * will be removed and so on.
	 *
	 * @return the average cycle time
	 */
	public double getAvgCycleTime() {
		
		if (this.cycleTimeVector.size()==0) return 0;
		
		long cycleTimeSum = 0;
		Long[] currentCycleTimeArray = new Long[cycleTimeVector.size()];
		currentCycleTimeArray = cycleTimeVector.toArray(currentCycleTimeArray);
		for (int i = 0; i < currentCycleTimeArray.length; i++) {
			if (currentCycleTimeArray[i]!=null) {
				cycleTimeSum += currentCycleTimeArray[i];
			}
		}		
		return (double)cycleTimeSum/(double)currentCycleTimeArray.length;
	}

	
	/**
	 * Method to put the answer of the Server.Master directly
	 * in the local public containerDescription - Variable
	 *
	 * @param crcReply the ClientRemoteContainerReply, which describes a single container and its machine.
	 */
	public void putContainerDescription(ClientRemoteContainerReply crcReply) {
		NodeDescription node = new NodeDescription(crcReply);
		this.getContainerDescriptionHash().put(node.getContainerName(), node);
		if (this.getContainerNames().contains(node.getContainerName())==false) {
			this.getContainerNames().addElement(node.getContainerName());	
		}
	}
	
	/**
	 * Sets the container name on which the running system is waiting for, because
	 * a new remote container was requested.
	 *
	 * @param container2Wait4 the new new container2 wait4
	 * @param maxWaitingTime the maximum waiting time in milliseconds
	 * @see LoadServiceHelper#startNewRemoteContainer()
	 * @see LoadServiceHelper#startNewRemoteContainer(de.enflexit.awb.simulation.ontology.RemoteContainerConfig)
	 */
	public void setNewContainer2Wait4(String container2Wait4, Long maxWaitingTime) {
		Container2Wait4 cont = new Container2Wait4(container2Wait4, maxWaitingTime);
		this.getNewContainers2Wait4Hash().put(container2Wait4, cont);
	}
	/**
	 * Returns the status of the container on which the system is waiting for
	 * in order to join the current platform.
	 *
	 * @param containerName the container name
	 * @return the status of the new container described by the class Container2Wait4
	 * 
	 * @see LoadServiceHelper#startNewRemoteContainerStaus(String)
	 */
	public Container2Wait4 getNewContainer2Wait4Status(String containerName) {
		return this.getNewContainers2Wait4Hash().get(containerName);
	}
	/**
	 * This method sets that the new RemoteContainerRequest was successfully.
	 * @param containerName the new container, which started
	 */
	public void setNewContainerStarted(String containerName) {
		Container2Wait4 cont = this.getNewContainers2Wait4Hash().get(containerName);
		if (cont!=null ) {
			cont.setStarted(true);
		}
	}
	/**
	 * This method sets that the new RemoteContainerRequest was NOT successfully.
	 * @param containerName the container, which was NOT started
	 */
	public void setNewContainerCanceled(String containerName) {
		Container2Wait4 cont = this.getNewContainers2Wait4Hash().get(containerName);
		if (cont!=null ) {
			cont.setCancelled(true);
		}
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
		 * Default constructor of this Sub-Class
		 */
		public NodeDescription() {
		
		}
		/**
		 * Constructor of this Sub-Class, using the ClientRemoteContainerReply from the {@link ServerMasterAgent}.
		 *
		 * @param crcReply the ClientRemoteContainerReply 
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
			return containerName;
		}
		
		/**
		 * Sets the process ID (PID) of the JVM
		 * @param jvmPID the PID of the JVM
		 */
		public void setJvmPID(String jvmPID) {
			this.jvmPID = jvmPID;
		}
		/**
		 * Gets the PID of the JVM.
		 * @return the jvmPID
		 */
		public String getJvmPID() {
			return jvmPID;
		}
		
		/**
		 * Sets the PlatformAddress.
		 * @param plAddress the PlatformAddress to set
		 */
		public void setPlAddress(PlatformAddress plAddress) {
			this.plAddress = plAddress;
		}
		/**
		 * Gets the PlatformAddress address.
		 * @return the PlatformAddress
		 */
		public PlatformAddress getPlAddress() {
			return plAddress;
		}
		
		/**
		 * Gets the information of operating system.
		 * @return the osInfo
		 */
		public OSInfo getOsInfo() {
			return osInfo;
		}
		/**
		 * Sets the information of operating system.
		 * @param osInfo the OSInfo to set
		 */
		public void setOsInfo(OSInfo osInfo) {
			this.osInfo = osInfo;
		}
		
		/**
		 * Gets the PlatformPerformance.
		 * @return the plPerformace
		 */
		public PlatformPerformance getPlPerformace() {
			return plPerformace;
		}
		/**
		 * Sets the PlatformPerformance.
		 * @param plPerformace the PlatformPerformance to set
		 */
		public void setPlPerformace(PlatformPerformance plPerformace) {
			this.plPerformace = plPerformace;
		}
		
		/**
		 * Gets the benchmark value.
		 * @return the benchmarkValue
		 */
		public BenchmarkResult getBenchmarkValue() {
			return benchmarkValue;
		}
		/**
		 * Sets the benchmark value.
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
	/**
	 * Describes the container on which it can be waited, in order to join the 
	 * currently running platform.
	 */
	public class Container2Wait4 {
		
		private String containerName = null;
		private long timeOut = 0;
		private boolean started = false;
		private boolean cancelled = false;
		
		/**
		 * Constructor of this Sub-Class.
		 *
		 * @param newContainerName the new container name
		 * @param maxWaitingDuration [optional] the time out duration to be used for waiting on the new container
		 */
		public Container2Wait4(String newContainerName, Long maxWaitingDuration) {
			this.setContainerName(newContainerName);
			if (maxWaitingDuration!=null && maxWaitingDuration > Platform.DEFAULT_REMOTE_CONTAINER_WAITING_DURATION) {
				this.timeOut = System.currentTimeMillis() + maxWaitingDuration;
			} else {
				this.timeOut = System.currentTimeMillis() + Platform.DEFAULT_REMOTE_CONTAINER_WAITING_DURATION;
			}
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
			return containerName;
		}
		
		/**
		 * Checks if is timed out.
		 * @return if the RemoteContainerRequest is timed out or not
		 */
		public boolean isTimedOut(){
			if (System.currentTimeMillis() > timeOut) {
				return true;
			} else {
				return false;	
			}
		}
		
		/**
		 * Sets the started.
		 * @param started the started to set
		 */
		public void setStarted(boolean started) {
			this.started = started;
		}
		/**
		 * Checks if is started.
		 * @return the started
		 */
		public boolean isStarted() {
			return started;
		}

		/**
		 * Sets the cancelled.
		 * @param cancelled the cancelled to set
		 */
		public void setCancelled(boolean chanceld) {
			this.cancelled = chanceld;
		}
		/**
		 * Checks if is cancelled.
		 * @return the cancelled-boolean
		 */
		public boolean isCancelled() {
			return cancelled;
		}
		
		/**
		 * Checks if the container to wait for is pending.
		 * @return true, if is pending
		 */
		public boolean isPending() {
			return this.isStarted()==false && this.isCancelled()==false && this.isTimedOut()==false;
		}

	}
	// --------------------------------------------------------------
	// --- Sub-Class NewContainer2Wait4 --- E N D -------------------
	// --------------------------------------------------------------


}
