package de.enflexit.awb.core.ui;

import de.enflexit.awb.core.jade.Platform.JadeStatusColor;

/**
 * The Interface AwbMainWindow describes the methods to interact with 
 * the MainWindow of an {@link AgentWorkbenchUI} implementation.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface AwbMainWindow {

	
	public void refreshView();
	
	
	public void dispose();

	
	public void setTitelAddition(String add2BasicTitle);
	
	
	public void setStatusBarMessage(String statusText);
	
	
	public void setJadeStatusColor(JadeStatusColor jadeStatus);

	
	/**
	 * Configures the appearance of the application, depending on the current project configuration
	 */
	public void setProjectView(); 

	
	public void enableSetupSelector(boolean isSetEnabled);
	public boolean isEnableSetupSelector();
	
	
	public void setSimulationReady2Start();
	public boolean isSimulationReady2Start();

	
	public void setEnableSimStart(boolean isSetEnabled);
	public boolean isEnableSimStart();
	
	
	public void setEnableSimPause(boolean isSetEnabled);
	public boolean isEnableSimPause();
	
	
	public void setEnableSimStop(boolean isSetEnabled);
	public boolean isEnableSimStop();

	
}
