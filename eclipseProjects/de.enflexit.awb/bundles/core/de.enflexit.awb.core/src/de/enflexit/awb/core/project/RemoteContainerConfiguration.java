package de.enflexit.awb.core.project;

import java.io.Serializable;

import de.enflexit.awb.simulation.distribution.JadeRemoteStart;

/**
 * This class manages the configuration for remote containers, which is 
 * stored in a project. 
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class RemoteContainerConfiguration implements Serializable {

	private static final long serialVersionUID = 7745495134485079177L;
	
	private boolean preventUsageOfAlreadyUsedComputers = true;
	private boolean showJADErmaGUI = false;
	
	private String jvmMemAllocInitial = JadeRemoteStart.jvmMemo128MB;
	private String jvmMemAllocMaximum = JadeRemoteStart.jvmMemo1GB;
	
	/**
	 * Instantiates a new remote container configuration.
	 */
	public RemoteContainerConfiguration() {
		
	}
	
	/**
	 * Checks if is prevent usage of already used computers.
	 * @return the preventUsageOfAlreadyUsedComputers
	 */
	public boolean isPreventUsageOfAlreadyUsedComputers() {
		return preventUsageOfAlreadyUsedComputers;
	}
	/**
	 * Sets the prevent usage of already used computers.
	 * @param preventUsageOfAlreadyUsedComputers the preventUsageOfAlreadyUsedComputers to set
	 */
	public void setPreventUsageOfAlreadyUsedComputers(boolean preventUsageOfAlreadyUsedComputers) {
		this.preventUsageOfAlreadyUsedComputers = preventUsageOfAlreadyUsedComputers;
	}
	
	/**
	 * Checks if a remote RMA should be shown.
	 * @return the showJADErmaGUI
	 */
	public boolean isShowJADErmaGUI() {
		return showJADErmaGUI;
	}
	/**
	 * Sets if a remote RMA should be shown.
	 * @param showRMA true, if the Jade rma agent shall appear
	 */
	public void setShowJADErmaGUI(boolean showRMA) {
		this.showJADErmaGUI = showRMA;
	}

	/**
	 * Gets the the JVM initial memory allocation.
	 * @return the jvmMemAllocInitial
	 */
	public String getJvmMemAllocInitial() {
		return jvmMemAllocInitial;
	}
	/**
	 * Sets the JVM initial memory allocation.
	 * @param jvmMemAllocInitial the jvmMemAllocInitial to set
	 */
	public void setJvmMemAllocInitial(String jvmMemAllocInitial) {
		this.jvmMemAllocInitial = jvmMemAllocInitial;
	}

	/**
	 * Gets the JVM maximum memory allocation.
	 * @return the jvmMemAllocMaximum
	 */
	public String getJvmMemAllocMaximum() {
		return jvmMemAllocMaximum;
	}
	/**
	 * Sets the JVM maximum memory allocation.
	 * @param jvmMemAllocMaximum the jvmMemAllocMaximum to set
	 */
	public void setJvmMemAllocMaximum(String jvmMemAllocMaximum) {
		this.jvmMemAllocMaximum = jvmMemAllocMaximum;
	}

}
