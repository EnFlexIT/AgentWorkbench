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
package agentgui.core.sim.setup;

import java.io.Serializable;

import agentgui.core.gui.projectwindow.simsetup.Distribution;
import agentgui.simulationService.load.LoadThresholdLevels;

/**
 * This class represents the model data for the distribution of an agency, which
 * can be configured in the tab {@link Distribution} of the simulation setup.
 *  
 * @see Distribution
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen 
 */
public class DistributionSetup implements Serializable {
	
	private static final long serialVersionUID = -3727386932566490036L;
	
	public final static String DEFAULT_StaticLoadBalancingClass = agentgui.simulationService.balancing.StaticLoadBalancing.class.getName();
	public final static String DEFAULT_DynamicLoadBalancingClass = agentgui.simulationService.balancing.DynamicLoadBalancing.class.getName();
	
	private boolean doStaticLoadBalancing = false;
	private String staticLoadBalancingClass = DEFAULT_StaticLoadBalancingClass;
	
	private int numberOfAgents = 0;
	private int numberOfContainer = 0;
	
	private boolean doDynamicLoadBalancing = false;
	private String dynamicLoadBalancingClass = DEFAULT_DynamicLoadBalancingClass;
	
	private boolean useUserThresholds = false;
	private LoadThresholdLevels UserThresholds = new LoadThresholdLevels();

	/**
	 * Checks if is do static load balancing.
	 *
	 * @return the doStaticLoadBalalncing
	 */
	public boolean isDoStaticLoadBalancing() {
		return doStaticLoadBalancing;
	}
	/**
	 * Sets the do static load balancing.
	 *
	 * @param doStaticLoadBalalncing the doStaticLoadBalalncing to set
	 */
	public void setDoStaticLoadBalancing(boolean doStaticLoadBalalncing) {
		this.doStaticLoadBalancing = doStaticLoadBalalncing;
	}
	
	/**
	 * Gets the number of agents.
	 *
	 * @return the numberOfAgents
	 */
	public int getNumberOfAgents() {
		return numberOfAgents;
	}
	/**
	 * Sets the number of agents.
	 *
	 * @param numberOfAgents the numberOfAgents to set
	 */
	public void setNumberOfAgents(int numberOfAgents) {
		this.numberOfAgents = numberOfAgents;
	}
	
	/**
	 * Gets the number of container.
	 *
	 * @return the numberOfContainer
	 */
	public int getNumberOfContainer() {
		return numberOfContainer;
	}
	/**
	 * Sets the number of container.
	 *
	 * @param numberOfContainer the numberOfContainer to set
	 */
	public void setNumberOfContainer(int numberOfContainer) {
		this.numberOfContainer = numberOfContainer;
	}
	
	/**
	 * Sets the static load balancing class.
	 *
	 * @param staticLoadBalancingClass the staticLoadBalancingClass to set
	 */
	public void setStaticLoadBalancingClass(String staticLoadBalancingClass) {
		this.staticLoadBalancingClass = staticLoadBalancingClass;
	}
	/**
	 * Gets the static load balancing class.
	 *
	 * @return the staticLoadBalancingClass
	 */
	public String getStaticLoadBalancingClass() {
		return staticLoadBalancingClass;
	}
	
	/**
	 * Checks if is do dynamic load balalncing.
	 *
	 * @return the doDynamicLoadBalalncing
	 */
	public boolean isDoDynamicLoadBalancing() {
		return doDynamicLoadBalancing;
	}
	/**
	 * Sets the do dynamic load balalncing.
	 *
	 * @param doDynamicLoadBalalncing the doDynamicLoadBalalncing to set
	 */
	public void setDoDynamicLoadBalancing(boolean doDynamicLoadBalalncing) {
		this.doDynamicLoadBalancing = doDynamicLoadBalalncing;
	}
	
	/**
	 * Gets the dynamic load balancing class.
	 *
	 * @return the dynamicLoadBalancingClass
	 */
	public String getDynamicLoadBalancingClass() {
		return dynamicLoadBalancingClass;
	}
	/**
	 * Sets the dynamic load balancing class.
	 *
	 * @param dynamicLoadBalancingClass the dynamicLoadBalancingClass to set
	 */
	public void setDynamicLoadBalancingClass(String dynamicLoadBalancingClass) {
		this.dynamicLoadBalancingClass = dynamicLoadBalancingClass;
	}
	
	/**
	 * Checks if is use user thresholds.
	 *
	 * @return the useUserThresholds
	 */
	public boolean isUseUserThresholds() {
		return useUserThresholds;
	}
	/**
	 * Sets the use user thresholds.
	 *
	 * @param useUserThresholds the useUserThresholds to set
	 */
	public void setUseUserThresholds(boolean useUserThresholds) {
		this.useUserThresholds = useUserThresholds;
	}
	
	/**
	 * Gets the user thresholds.
	 *
	 * @return the userThresholds
	 */
	public LoadThresholdLevels getUserThresholds() {
		return UserThresholds;
	}
	/**
	 * Sets the user thresholds.
	 *
	 * @param userThresholds the userThresholds to set
	 */
	public void setUserThresholds(LoadThresholdLevels userThresholds) {
		UserThresholds = userThresholds;
	}
}
