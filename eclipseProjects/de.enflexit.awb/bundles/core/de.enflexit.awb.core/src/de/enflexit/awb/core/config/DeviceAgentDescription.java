package de.enflexit.awb.core.config;

import java.io.Serializable;

/**
 * The Class DeviceAgentDescription is used as configuration value for agents 
 * that are to be executed in the embedded system execution mode.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 * 
 * @see GlobalInfo
 */
public class DeviceAgentDescription implements Serializable {

	private static final long serialVersionUID = -4381563610495572107L;

	private String agentName;
	private String agentClass;
	
	/**
	 * Instantiates a new device agent.
	 *
	 * @param agentName the agent name
	 * @param agentClass the agent class
	 */
	public DeviceAgentDescription(String agentName, String agentClass) {
		this.setAgentName(agentName);
		this.setAgentClass(agentClass);
	}
	/**
	 * Instantiates a new device agent description based on the .
	 * @param bundlePropValue the bundle property value
	 */
	public DeviceAgentDescription(String bundlePropValue) {
		this.proceedBundlePropertyValue(bundlePropValue);
	}
	
	/**
	 * Proceeds the bundle property value.
	 * @param bundlePropValue the bundle prop value
	 */
	private void proceedBundlePropertyValue(String bundlePropValue) {
		
		int posOpenBraket = bundlePropValue.indexOf("("); 
		String agentName  = bundlePropValue.substring(0, posOpenBraket);
		String agentClass = bundlePropValue.substring(posOpenBraket+1);
		agentClass = agentClass.replace(")", "");
		
		this.setAgentName(agentName.trim());
		this.setAgentClass(agentClass.trim());
	}
	
	/**
	 * Gets the agent name.
	 * @return the agent name
	 */
	public String getAgentName() {
		return agentName;
	}
	/**
	 * Sets the agent name.
	 * @param agentName the new agent name
	 */
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	
	/**
	 * Gets the agent class.
	 * @return the agent class
	 */
	public String getAgentClass() {
		return agentClass;
	}
	/**
	 * Sets the agent class.
	 * @param agentClass the new agent class
	 */
	public void setAgentClass(String agentClass) {
		this.agentClass = agentClass;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getAgentName() + "(" + this.getAgentClass() + ")";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObject) {
		
		if (compObject==null) return false;
		if (compObject instanceof DeviceAgentDescription) {
			DeviceAgentDescription dad = (DeviceAgentDescription) compObject; 
			return this.toString().equals(dad.toString());
		}
		return false;
	}
	
}
