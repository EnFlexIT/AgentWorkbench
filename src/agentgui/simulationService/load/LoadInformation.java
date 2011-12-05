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

import java.util.Hashtable;
import java.util.Vector;

import agentgui.simulationService.LoadServiceHelper;
import agentgui.simulationService.agents.ServerMasterAgent;
import agentgui.simulationService.ontology.BenchmarkResult;
import agentgui.simulationService.ontology.ClientRemoteContainerReply;
import agentgui.simulationService.ontology.OSInfo;
import agentgui.simulationService.ontology.PlatformAddress;
import agentgui.simulationService.ontology.PlatformLoad;
import agentgui.simulationService.ontology.PlatformPerformance;

/**
 * This class manages all Load-Informations of all container at the platform.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg / Essen
 */
public class LoadInformation  {

	private static final long serialVersionUID = -8535049489754734307L;
	
	/** The Vector of container, which are or were connected. */
	public Vector<String> containerQueue = new Vector<String>();
	/** The Hashtable containerDescription, which holds the information identifiable by the container name. */
	public Hashtable<String, NodeDescription> containerDescription = new Hashtable<String, NodeDescription>();
	/** The Hashtable containerLoads, which holds the load information identifiable by the container name. */
	public Hashtable<String, PlatformLoad> containerLoads = new Hashtable<String, PlatformLoad>(); 
	/** The Hashtable containerLocations, which holds the location identifiable by the container name. */
	public Hashtable<String, Location> containerLocations = new Hashtable<String, Location>();
	/** The agents locations (answers to: where can I find a specified agent). */
	public LoadAgentMap agentLocations = new LoadAgentMap();
	/** The Vector of agents, which are connected by the actuator/sensor relationship of the SimulationService. */
	public Vector<AID> sensorAgents = null;
	
	/** The Hashtable, which holds the list of remote container, that should join the current platform. */
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
	 * out of 500 single values in maximum. If a further cycle time occurs, the first measurment
	 * will be removed and so on. 
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
	 *
	 * @param crcReply the ClientRemoteContainerReply, which describes a single container and its machine.
	 */
	public void putContainerDescription(ClientRemoteContainerReply crcReply) {
		NodeDescription node = new NodeDescription(crcReply);
		this.containerDescription.put(node.getContainerName(), node);
		if (containerQueue.contains(node.getContainerName())==false) {
			containerQueue.addElement(node.getContainerName());	
		}
	}
	
	/**
	 * Sets the container name on which the running system is waiting for, because
	 * a new remote container was requested.
	 * @param container2Wait4 the new new container2 wait4
	 * 
	 * @see LoadServiceHelper#startNewRemoteContainer()
	 * @see LoadServiceHelper#startNewRemoteContainer(agentgui.simulationService.ontology.RemoteContainerConfig)
	 */
	public void setNewContainer2Wait4(String container2Wait4) {
		Container2Wait4 cont = new Container2Wait4(container2Wait4);
		this.newContainers2Wait4.put(container2Wait4, cont);
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
		return newContainers2Wait4.get(containerName);
	}
	/**
	 * This method sets that the new RemoteContainerRequest was successfully.
	 * @param containerName the new container, which started
	 */
	public void setNewContainerStarted(String containerName) {
		Container2Wait4 cont = newContainers2Wait4.get(containerName);
		if (cont!=null ) {
			cont.setStarted(true);
		}
	}
	/**
	 * This method sets that the new RemoteContainerRequest was NOT successfully.
	 * @param containerName the container, which was NOT started
	 */
	public void setNewContainerCanceled(String containerName) {
		Container2Wait4 cont = newContainers2Wait4.get(containerName);
		if (cont!=null ) {
			cont.setCancelled(true);
		}
	}
	
	/**
	 * Resets the Object, which holds the Informations about 
	 * the agent in relation to it's locations etc.
	 * @see #agentLocations
	 */
	public void resetAIDs4Container() {
		agentLocations = new LoadAgentMap();		
	}
	/**
	 * Method to put the Agent-AIDs which are coming from the
	 * currently running containers.
	 *
	 * @param containerName the container name
	 * @param aid the AID array of the agents
	 * @see #agentLocations
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
	 * This method starts the counting for the agents at the platform 
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
		private long timeOut = (1000 * 20); // --- 10 Seconds ---
		private long time4TimeOut = 0;
		private boolean started = false;
		private boolean cancelled = false;
		
		/**
		 * Constructor of this Sub-Class.
		 * @param newContainerName the new container name
		 */
		public Container2Wait4(String newContainerName) {
			setContainerName(newContainerName);
			time4TimeOut = System.currentTimeMillis() + timeOut;
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
			if (System.currentTimeMillis() > time4TimeOut) {
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
		 * @param chanceld the cancelled to set
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

	}
	// --------------------------------------------------------------
	// --- Sub-Class NewContainer2Wait4 --- E N D -------------------
	// --------------------------------------------------------------


}
