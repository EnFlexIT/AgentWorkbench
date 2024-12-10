package de.enflexit.awb.simulation.load;

import java.util.Vector;

import de.enflexit.awb.simulation.ontology.PlatformLoad;

/**
 * This class is to be used in order to merge the load information of one JVM or 
 * of one machine/computer, even such information come several times.<br> 
 * If, for example, the number of agents are counted for the JVM, where the
 * end-user application of Agent.GUI is running (usually the Main-Container and
 * a project specific container will be there), this class can be used in order 
 * to get the total number of agents. A similar method can be used, if several 
 * JVM are executed on one computer.    
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class LoadMerger {

	private String identifier = null;
	private Vector<String> containerList = new Vector<String>();
	private Vector<String> jvmList = new Vector<String>();
	private Float benchmarkValue = null;	
	private PlatformLoad machineLoad = null;
	private Integer numberOfAgents = 0;
	
	/**
	 * Default constructor.
	 *
	 * @param identifier An identifier, which is for example the PID for a JVM or an URL for a computer
	 */
	public LoadMerger(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Cumlate the information coming from a container, so that the informations are available 
	 * per JVM or machine/computer.
	 *
	 * @param containerName the container name
	 * @param jvmPID The PID of the JAVA virtual machine
	 * @param benchValue the benchmark value
	 * @param pLoad the PlatformLoad
	 * @param nAgents the number of agents
	 */
	public void merge(String containerName, String jvmPID, Float benchValue, PlatformLoad pLoad, Integer nAgents) {
		
		if (containerList.contains(containerName)==false) {
			containerList.addElement(containerName);	
		}
		if (jvmList.contains(jvmPID)==false) {
			jvmList.addElement(jvmPID);	
		}		
		if (this.benchmarkValue==null) {
			this.benchmarkValue = benchValue;
		}
		if (this.machineLoad==null) {
			this.machineLoad = pLoad;
		}
		if (nAgents!=null) {
			numberOfAgents += nAgents;	
		}
		
	}

	/**
	 * Returns the current identifier.
	 * @return the identifier of this object
	 */
	public String getIdentifier() {
		return identifier;
	}
	
	/**
	 * Gets the list of JVM.
	 * @return the containerList
	 */
	public Vector<String> getJvmList() {
		return jvmList;
	}
	
	/**
	 * Gets the container list.
	 * @return the containerList
	 */
	public Vector<String> getContainerList() {
		return containerList;
	}
	
	/**
	 * Gets the benchmark value.
	 * @return the benchmarkValue
	 */
	public Float getBenchmarkValue() {
		return benchmarkValue;
	}
	
	/**
	 * Returns the machine load.
	 * @return the machineLoad
	 */
	public PlatformLoad getMachineLoad() {
		return machineLoad;
	}
	
	/**
	 * Returns the number of agents.
	 * @return the numberOfAgents
	 */
	public Integer getNumberOfAgents() {
		return numberOfAgents;
	}
	
}
