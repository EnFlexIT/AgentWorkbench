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
package agentgui.core.project;

import java.io.Serializable;

import agentgui.simulationService.distribution.JadeRemoteStart;

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
