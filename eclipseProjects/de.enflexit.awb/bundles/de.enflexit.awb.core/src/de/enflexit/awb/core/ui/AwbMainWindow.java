package de.enflexit.awb.core.ui;

/**
 * The Interface AwbMainWindow describes the methods to interact with 
 * the MainWindow of an {@link AgentWorkbenchUI} implementation.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface AwbMainWindow {

	
	public void dispose();

	
	public void setSimulationReady2Start();


	public void setEnableSimStart(boolean isSetEnabled);

	
}
