package mas.service.distribution.agents;

import java.util.Vector;

import mas.service.distribution.ontology.PlatformLoad;

public class LoadMachine {

	private String jvmPID = null;
	private Vector<String> containerList = new Vector<String>();
	private Float benchmarkValue = null;	
	private PlatformLoad machineLoad = null;
	private Integer numberOfAgents = 0;
	
	/**
	 * Default constructor
	 */
	public LoadMachine(String pid) {
		this.jvmPID = pid;
	}

	/**
	 * Cumlate the informations of the Container, so that 
	 * the informations are available per JVM
	 * @param container
	 * @param benchValue
	 * @param pLoad
	 * @param nAgents
	 */
	public void merge(String containerName, Float benchValue, PlatformLoad pLoad, Integer nAgents) {
		
		containerList.addElement(containerName);
		if (this.benchmarkValue==null) {
			this.benchmarkValue = benchValue;
		}
		if (this.machineLoad==null) {
			this.machineLoad = pLoad;
		}
		numberOfAgents += nAgents;
	}

	/**
	 * @return the jvmPID
	 */
	public String getJvmPID() {
		return jvmPID;
	}
	/**
	 * @return the containerList
	 */
	public Vector<String> getContainerList() {
		return containerList;
	}
	/**
	 * @return the benchmarkValue
	 */
	public Float getBenchmarkValue() {
		return benchmarkValue;
	}
	/**
	 * @return the machineLoad
	 */
	public PlatformLoad getMachineLoad() {
		return machineLoad;
	}
	/**
	 * @return the numberOfAgents
	 */
	public Integer getNumberOfAgents() {
		return numberOfAgents;
	}
	
}
