package agentgui.core.sim.setup;

import java.io.Serializable;

import agentgui.simulationService.load.LoadThresholdLevels;

public class DistributionSetup implements Serializable {
	
	private static final long serialVersionUID = -3727386932566490036L;
	public final static String DEFAULT_StaticLoadBalancingClass = agentgui.simulationService.balancing.StaticLoadBalancing.class.getName();
	public final static String DEFAULT_DynamicLoadBalancingClass = agentgui.simulationService.balancing.DynamicLoadBalancing.class.getName();
	
	private boolean doStaticLoadBalalncing = false;
	private String staticLoadBalancingClass = DEFAULT_StaticLoadBalancingClass;
	private int numberOfAgents = 0;
	private int numberOfContainer = 0;
	
	private boolean doDynamicLoadBalalncing = false;
	private String dynamicLoadBalancingClass = DEFAULT_DynamicLoadBalancingClass;
	
	private boolean useUserThresholds = false;
	private LoadThresholdLevels UserThresholds = new LoadThresholdLevels();

	/**
	 * @return the doStaticLoadBalalncing
	 */
	public boolean isDoStaticLoadBalalncing() {
		return doStaticLoadBalalncing;
	}
	/**
	 * @param doStaticLoadBalalncing the doStaticLoadBalalncing to set
	 */
	public void setDoStaticLoadBalalncing(boolean doStaticLoadBalalncing) {
		this.doStaticLoadBalalncing = doStaticLoadBalalncing;
	}
	/**
	 * @return the numberOfAgents
	 */
	public int getNumberOfAgents() {
		return numberOfAgents;
	}
	/**
	 * @param numberOfAgents the numberOfAgents to set
	 */
	public void setNumberOfAgents(int numberOfAgents) {
		this.numberOfAgents = numberOfAgents;
	}
	/**
	 * @return the numberOfContainer
	 */
	public int getNumberOfContainer() {
		return numberOfContainer;
	}
	/**
	 * @param numberOfContainer the numberOfContainer to set
	 */
	public void setNumberOfContainer(int numberOfContainer) {
		this.numberOfContainer = numberOfContainer;
	}
	/**
	 * @param staticLoadBalancingClass the staticLoadBalancingClass to set
	 */
	public void setStaticLoadBalancingClass(String staticLoadBalancingClass) {
		this.staticLoadBalancingClass = staticLoadBalancingClass;
	}
	/**
	 * @return the staticLoadBalancingClass
	 */
	public String getStaticLoadBalancingClass() {
		return staticLoadBalancingClass;
	}
	/**
	 * @return the doDynamicLoadBalalncing
	 */
	public boolean isDoDynamicLoadBalalncing() {
		return doDynamicLoadBalalncing;
	}
	/**
	 * @param doDynamicLoadBalalncing the doDynamicLoadBalalncing to set
	 */
	public void setDoDynamicLoadBalalncing(boolean doDynamicLoadBalalncing) {
		this.doDynamicLoadBalalncing = doDynamicLoadBalalncing;
	}
	/**
	 * @return the dynamicLoadBalancingClass
	 */
	public String getDynamicLoadBalancingClass() {
		return dynamicLoadBalancingClass;
	}
	/**
	 * @param dynamicLoadBalancingClass the dynamicLoadBalancingClass to set
	 */
	public void setDynamicLoadBalancingClass(String dynamicLoadBalancingClass) {
		this.dynamicLoadBalancingClass = dynamicLoadBalancingClass;
	}
	/**
	 * @return the useUserThresholds
	 */
	public boolean isUseUserThresholds() {
		return useUserThresholds;
	}
	/**
	 * @param useUserThresholds the useUserThresholds to set
	 */
	public void setUseUserThresholds(boolean useUserThresholds) {
		this.useUserThresholds = useUserThresholds;
	}
	/**
	 * @return the userThresholds
	 */
	public LoadThresholdLevels getUserThresholds() {
		return UserThresholds;
	}
	/**
	 * @param userThresholds the userThresholds to set
	 */
	public void setUserThresholds(LoadThresholdLevels userThresholds) {
		UserThresholds = userThresholds;
	}
}
