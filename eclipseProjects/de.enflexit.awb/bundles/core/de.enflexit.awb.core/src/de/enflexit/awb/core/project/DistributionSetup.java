package de.enflexit.awb.core.project;

import java.io.Serializable;

import de.enflexit.awb.simulation.load.LoadThresholdLevels;

/**
 * This class represents the model data for the distribution of an agency that 
 * can be configured in the tab 'Distribution' of the simulation setup.
 *  
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen 
 */
public class DistributionSetup implements Serializable {
	
	private static final long serialVersionUID = -3727386932566490036L;
	
	public final static String DEFAULT_StaticLoadBalancingClass = de.enflexit.awb.simulation.balancing.PredictiveStaticLoadBalancing.class.getName();
	public final static String DEFAULT_DynamicLoadBalancingClass = de.enflexit.awb.simulation.balancing.DynamicLoadBalancing.class.getName();
	
	private boolean doStaticLoadBalancing = true;
	private String staticLoadBalancingClass = DEFAULT_StaticLoadBalancingClass;
	
	private int numberOfAgents = 0;
	private int numberOfContainer = 0;
	private int additionalRemoteContainerTimeOutInSeconds;
	private boolean isDistributeAllProjectResources;
	
	private boolean doDynamicLoadBalancing = false;
	private String dynamicLoadBalancingClass = DEFAULT_DynamicLoadBalancingClass;
	
	private boolean useUserThresholds = true;
	private LoadThresholdLevels UserThresholds = new LoadThresholdLevels();

	private boolean showLoadMonitorAtPlatformStart = true;
	private boolean showThreadMonitorAtPlatformStart = true;
	private boolean autoSaveRealMetricsOnSimStop = false;
	private boolean immediatelyStartLoadRecording = false;
	private long loadRecordingInterval=500;
	
	/** if true, do not use local machine for agents. */
	private boolean isRemoteOnly = true;
	
	/** if true, distribute agents evenly on available machines. */
	private boolean isEvenDistribution = false;
	
	
	/**
	 * Checks if is do static load balancing.
	 * @return the doStaticLoadBalalncing
	 */
	public boolean isDoStaticLoadBalancing() {
		return doStaticLoadBalancing;
	}
	/**
	 * Sets the do static load balancing.
	 * @param doStaticLoadBalalncing the doStaticLoadBalalncing to set
	 */
	public void setDoStaticLoadBalancing(boolean doStaticLoadBalalncing) {
		this.doStaticLoadBalancing = doStaticLoadBalalncing;
	}
	
	/**
	 * Gets the number of agents.
	 * @return the numberOfAgents
	 */
	public int getNumberOfAgents() {
		return numberOfAgents;
	}
	/**
	 * Sets the number of agents.
	 * @param numberOfAgents the numberOfAgents to set
	 */
	public void setNumberOfAgents(int numberOfAgents) {
		this.numberOfAgents = numberOfAgents;
	}
	
	/**
	 * Gets the number of container.
	 * @return the numberOfContainer
	 */
	public int getNumberOfContainer() {
		return numberOfContainer;
	}
	/**
	 * Sets the number of container.
	 * @param numberOfContainer the numberOfContainer to set
	 */
	public void setNumberOfContainer(int numberOfContainer) {
		this.numberOfContainer = numberOfContainer;
	}
	
	/**
	 * Returns the additional remote container time out in seconds.
	 * @return the additional remote container time out in seconds
	 */
	public int getAdditionalRemoteContainerTimeOutInSeconds() {
		return additionalRemoteContainerTimeOutInSeconds;
	}
	/**
	 * Sets the additional remote container time out in seconds.
	 * @param additionalRemoteContainerTimeOutInSeconds the new additional remote container time out in seconds
	 */
	public void setAdditionalRemoteContainerTimeOutInSeconds(int additionalRemoteContainerTimeOutInSeconds) {
		this.additionalRemoteContainerTimeOutInSeconds = additionalRemoteContainerTimeOutInSeconds;
	}
	
	/**
	 * Checks if is distribute all project resources.
	 * @return true, if is distribute all project resources
	 */
	public boolean isDistributeAllProjectResources() {
		return this.isDistributeAllProjectResources;
	}
	/**
	 * Sets to distribute all project resources or not.
	 * @param isDistributeAllProjectResources the new distribute all project resources
	 */
	public void setDistributeAllProjectResources(boolean isDistributeAllProjectResources) {
		this.isDistributeAllProjectResources = isDistributeAllProjectResources;
	}
	
	/**
	 * Sets the static load balancing class.
	 * @param staticLoadBalancingClass the staticLoadBalancingClass to set
	 */
	public void setStaticLoadBalancingClass(String staticLoadBalancingClass) {
		this.staticLoadBalancingClass = staticLoadBalancingClass;
	}
	/**
	 * Gets the static load balancing class.
	 * @return the staticLoadBalancingClass
	 */
	public String getStaticLoadBalancingClass() {
		return staticLoadBalancingClass;
	}
	
	/**
	 * Checks if is do dynamic load balancing.
	 * @return the doDynamicLoadBalalncing
	 */
	public boolean isDoDynamicLoadBalancing() {
		return doDynamicLoadBalancing;
	}
	/**
	 * Sets the do dynamic load balancing.
	 * @param doDynamicLoadBalancing the doDynamicLoadBalalncing to set
	 */
	public void setDoDynamicLoadBalancing(boolean doDynamicLoadBalancing) {
		this.doDynamicLoadBalancing = doDynamicLoadBalancing;
	}
	
	/**
	 * Gets the dynamic load balancing class.
	 * @return the dynamicLoadBalancingClass
	 */
	public String getDynamicLoadBalancingClass() {
		return dynamicLoadBalancingClass;
	}
	/**
	 * Sets the dynamic load balancing class.
	 * @param dynamicLoadBalancingClass the dynamicLoadBalancingClass to set
	 */
	public void setDynamicLoadBalancingClass(String dynamicLoadBalancingClass) {
		this.dynamicLoadBalancingClass = dynamicLoadBalancingClass;
	}
	
	/**
	 * Checks if is use user thresholds.
	 * @return the useUserThresholds
	 */
	public boolean isUseUserThresholds() {
		return useUserThresholds;
	}
	/**
	 * Sets the use user thresholds.
	 * @param useUserThresholds the useUserThresholds to set
	 */
	public void setUseUserThresholds(boolean useUserThresholds) {
		this.useUserThresholds = useUserThresholds;
	}
	
	/**
	 * Gets the user thresholds.
	 * @return the userThresholds
	 */
	public LoadThresholdLevels getUserThresholds() {
		return UserThresholds;
	}
	/**
	 * Sets the user thresholds.
	 * @param userThresholds the userThresholds to set
	 */
	public void setUserThresholds(LoadThresholdLevels userThresholds) {
		UserThresholds = userThresholds;
	}
	
	/**
	 * Checks if is show load monitor at platform start.
	 * @return true, if is show load monitor
	 */
	public boolean isShowLoadMonitorAtPlatformStart() {
		return showLoadMonitorAtPlatformStart;
	}
	
	/**
	 * Checks if is show thread monitor at platform start.
	 * @return true, if is show thread monitor at platform start
	 */
	public boolean isShowThreadMonitorAtPlatformStart() {
		return showThreadMonitorAtPlatformStart;
	}
	
	/**
	 * Checks if is auto save on sim stop.
	 * @return true, if is auto save on sim stop
	 */
	public boolean isAutoSaveRealMetricsOnSimStop() {
		return autoSaveRealMetricsOnSimStop;
	}
	
	/**
	 * Sets the show load monitor at platform start.
	 * @param showLoadMonitorAtPlatformStart the new show load monitor at platform start
	 */
	public void setShowLoadMonitorAtPlatformStart(boolean showLoadMonitorAtPlatformStart) {
		this.showLoadMonitorAtPlatformStart = showLoadMonitorAtPlatformStart;
	}
	
	/**
	 * Sets the show thread monitor at platform start
	 * @param showThreadMonitorAtPlatformStart the new show thread monitor at platform start
	 */
	public void setShowThreadMonitorAtPlatformStart(boolean showThreadMonitorAtPlatformStart) {
		this.showThreadMonitorAtPlatformStart = showThreadMonitorAtPlatformStart;
	}
	
	/**
	 * Sets the auto save real metrics on sim stop.
	 * @param autoSaveRealMetricsOnSimStop the new auto save real metrics on sim stop
	 */
	public void setAutoSaveRealMetricsOnSimStop(boolean autoSaveRealMetricsOnSimStop) {
		this.autoSaveRealMetricsOnSimStop = autoSaveRealMetricsOnSimStop;
	}
	
	/**
	 * Checks if is immediately start load recording.
	 * @return true, if is immediately start load recording
	 */
	public boolean isImmediatelyStartLoadRecording() {
		return immediatelyStartLoadRecording;
	}
	/**
	 * Sets the immediately start load recording.
	 * @param immediatelyStartLoadRecording the new immediately start load recording
	 */
	public void setImmediatelyStartLoadRecording(boolean immediatelyStartLoadRecording) {
		this.immediatelyStartLoadRecording = immediatelyStartLoadRecording;
	}

	/**
	 * Returns the load recording interval.
	 * @return the load recording interval
	 */
	public long getLoadRecordingInterval() {
		return loadRecordingInterval;
	}
	/**
	 * Sets the load recording interval.
	 * @param loadRecordingInterval the new load recording interval
	 */
	public void setLoadRecordingInterval(long loadRecordingInterval) {
		this.loadRecordingInterval = loadRecordingInterval;
	}
	
	/**
	 * Checks if is remote only.
	 *
	 * @return the isRemoteOnly
	 */
	public boolean isRemoteOnly() {
		return isRemoteOnly;
	}
	
	/**
	 * Sets the remote only.
	 *
	 * @param isRemoteOnly the isRemoteOnly to set
	 */
	public void setRemoteOnly(boolean isRemoteOnly) {
		this.isRemoteOnly = isRemoteOnly;
	}
	
	/**
	 * Checks if is even distribution.
	 *
	 * @return the isRemoteOnly
	 */
	public boolean isEvenDistribution() {
		return isEvenDistribution;
	}
	
	/**
	 * Sets the even distribution.
	 *
	 * @param isEvenDistribution the new even distribution
	 */
	public void setEvenDistribution(boolean isEvenDistribution) {
		this.isEvenDistribution = isEvenDistribution;
	}
	
}
