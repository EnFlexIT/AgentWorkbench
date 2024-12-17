package de.enflexit.awb.core.ui;

import de.enflexit.awb.simulation.load.LoadInformation.NodeDescription;
import de.enflexit.awb.simulation.ontology.PlatformLoad;

/**
 * The Interface AwbMonitoringDialogSystemLoad.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface AwbMonitoringDialogSystemLoad {

	/**
	 * Will be used to set the load recording at system start.
	 *
	 * @param doLoadRecording the do load recording
	 * @param loadRecordingInterval the load recording interval
	 */
	public void setLoadRecordingAtSystemStart(boolean doLoadRecording, long loadRecordingInterval);
	
	/**
	 * Has to set the dialog or UI visible or not.
	 * @param setVisible the new visible
	 */
	public void setVisible(boolean setVisible);

	/**
	 * Sets the number of agents on the platform executed.
	 * @param noOfAgentsOnPlatform the new number of agents
	 */
	public void setNumberOfAgents(int noOfAgentsOnPlatform);
	
	/**
	 * Sets the number of container currently used.
	 * @param noOfContainer the new number of container
	 */
	public void setNumberOfContainer(int noOfContainer);
	
	/**
	 * Sets the cycle time.
	 * @param cycleTime the new cycle time
	 */
	public void setCycleTime(double cycleTime);
	
	/**
	 * Has to update the view for the container load for the specified container.
	 *
	 * @param containerName the container name
	 * @param nodeDescription the NodeDescription
	 * @param benchmarkValue the benchmark value
	 * @param containerLoad the container load
	 * @param noOfAgents the no of agents
	 */
	public void updateContainerLoad(final String containerName, final NodeDescription nodeDescription, final float benchmarkValue, final PlatformLoad containerLoad, final Integer noOfAgents);
	
	
	/**
	 * Refresh view after all container loads were update.
	 */
	public void refreshViewAfterContainerLoadUpdate();
	
	/**
	 * Can be used to alternate recording indicator (in an on/off manner).
	 */
	public void alternateRecordingIndicator();
	
	
}
