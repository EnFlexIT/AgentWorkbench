package agentgui.simulationService.load;

import java.util.Vector;

import agentgui.simulationService.ontology.PlatformLoad;


public class LoadMerger {

	private String identifier = null;
	private Vector<String> containerList = new Vector<String>();
	private Vector<String> jvmList = new Vector<String>();
	private Float benchmarkValue = null;	
	private PlatformLoad machineLoad = null;
	private Integer numberOfAgents = 0;
	
	/**
	 * Default constructor
	 */
	public LoadMerger(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Cumlate the informations of the Container, so that 
	 * the informations are available per JVM
	 * @param container
	 * @param benchValue
	 * @param pLoad
	 * @param nAgents
	 */
	public void merge(String containerName, String jvm ,Float benchValue, PlatformLoad pLoad, Integer nAgents) {
		
		if (containerList.contains(containerName)==false) {
			containerList.addElement(containerName);	
		}
		if (jvmList.contains(jvm)==false) {
			jvmList.addElement(jvm);	
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
	 * @return the identifier of this object
	 */
	public String getIdentifier() {
		return identifier;
	}
	/**
	 * @return the containerList
	 */
	public Vector<String> getJvmList() {
		return jvmList;
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
