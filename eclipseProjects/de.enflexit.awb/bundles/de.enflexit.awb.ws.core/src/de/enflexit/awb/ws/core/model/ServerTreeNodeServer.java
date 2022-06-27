package de.enflexit.awb.ws.core.model;

import javax.swing.Icon;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import de.enflexit.awb.ws.AwbWebServerServiceWrapper;
import de.enflexit.awb.ws.BundleHelper;
import de.enflexit.awb.ws.core.JettyConfiguration;
import de.enflexit.awb.ws.core.JettyServerInstances;

/**
 * The Class ServerTreeNodeServer.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ServerTreeNodeServer extends AbstractServerTreeNodeObject {

	private AwbWebServerServiceWrapper serverServiceWrapper;
	private JettyServerInstances jettyServerInstances;
	
	
	/**
	 * Instantiates a new server tree node object for server services.
	 *
	 * @param serverServiceWrapper the server service wrapper
	 * @param jettyServerInstances the jetty server instances
	 */
	public ServerTreeNodeServer(AwbWebServerServiceWrapper serverServiceWrapper, JettyServerInstances jettyServerInstances) {
		this.serverServiceWrapper = serverServiceWrapper;
		this.jettyServerInstances = jettyServerInstances;
	}

	/**
	 * Returns the {@link AwbWebServerServiceWrapper}.
	 * @return the server service wrapper
	 */
	public AwbWebServerServiceWrapper getAwbWebServerServiceWrapper() {
		return serverServiceWrapper;
	}
	/**
	 * Returns the {@link JettyServerInstances}.
	 * @return the jetty server instances (will be null, if the server was not started)
	 */
	public JettyServerInstances getJettyServerInstances() {
		return jettyServerInstances;
	}
	
	/**
	 * Returns the source bundle of the service.
	 * @return the source bundle
	 */
	public Bundle getSourceBundle() {
		return FrameworkUtil.getBundle(this.serverServiceWrapper.getWebServerService().getClass());
	}
	/**
	 * Returns the service class name.
	 * @return the service class name
	 */
	public String getServiceClassName() {
		return this.serverServiceWrapper.getWebServerService().getClass().getName();
	}
	
	/**
	 * Checks if the current server is running.
	 * @return true, if the server is running
	 */
	public boolean isRunningServer() {
		return this.jettyServerInstances!=null && this.jettyServerInstances.getServer()!=null && this.jettyServerInstances.getServer().isStarted()==true;
	}
	/**
	 * Returns the running server description.
	 * @return the running server description
	 */
	public String getRunningServerDescription() {
		if (this.isRunningServer()==true) {
			return "Started";
		}
		return "Stopped";
	}
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.ServerTreeNodeObject#toString()
	 */
	@Override
	public String toString() {
		return "<html><b>Server '" + this.serverServiceWrapper.getJettyConfiguration().getServerName() + "'</b></html>";
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.ServerTreeNodeObject#getToolTipText()
	 */
	@Override
	public String getToolTipText() {
		return "<html><b>" + this.getRunningServerDescription() + ":</b> [" + this.getSourceBundle().getSymbolicName() + "] " + this.getServiceClassName() + "</html>";
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.ServerTreeNodeObject#getNodeIcon()
	 */
	@Override
	public Icon getNodeIcon() {
		if (this.isRunningServer()==true) {
			return BundleHelper.getImageIcon("awbWeb16Green.png");
		}
		return BundleHelper.getImageIcon("awbWeb16Red.png");
	}

	/**
	 * Returns the {@link JettyConfiguration} that can be used to edit the JettySettings 
	 * before they are stored in the properties file within the AWB-properties directory.
	 * 
	 * @return the current working copy of the JettyConfiguration as stored in the corresponding properties file
	 */
	public JettyConfiguration getJettyConfiguration() {
		return this.serverServiceWrapper.getJettyConfiguration();
	}
	/**
	 * Reverts the JettyConfiguration to the last stored file version.<br>
	 * Use {@link #getJettyConfiguration()} to get the revised version of the configuration
	 */
	public void revertJettyConfigurationToPropertiesFile() {
		this.serverServiceWrapper.revertJettyConfigurationToPropertiesFile();
	}
	/**
	 * Reverts the JettyConfiguration to the initial service defined configuration.
	 * Use {@link #getJettyConfiguration()} to get the revised version of the configuration
	 */
	public void revertJettyConfigurationToServiceDefinition() {
		this.serverServiceWrapper.revertJettyConfigurationToServiceDefinition();
	}
	
	/**
	 * Saves the currently edited JettySettings.
	 * @return true, if successful
	 */
	public boolean save() {
		return this.serverServiceWrapper.save();
	}
	/**
	 * Checks for changed settings.
	 * @return true, if settings have changed
	 */
	public boolean hasChangedJettySettings() {
		return this.serverServiceWrapper.hasChangedJettySettings();
	}

	

}
