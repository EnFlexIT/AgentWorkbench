package de.enflexit.awb.remoteControl;

/**
 * This interface defines the functions that must be provided to control an AWB simulation from remote.  
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public interface AwbRemoteControl {
	public boolean loadProject(String projectName);
	public boolean selectSetup(String setupName);
	public boolean configureSimulation(AwbSimulationSettings simulationSettings);
	public boolean startMultiAgentSystem();
	public boolean stopMultiAgentSystem();
	public void discreteSimulationNextStep();
	public void sendStatusUpdate(AwbStatusUpdate statusUpdate);
}
