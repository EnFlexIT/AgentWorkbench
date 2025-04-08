package de.enflexit.awb.ws.core.model;

import javax.swing.Icon;

import de.enflexit.awb.ws.BundleHelper;
import de.enflexit.awb.ws.core.JettyConfiguration;
import de.enflexit.awb.ws.core.JettySecuritySettings;
import de.enflexit.awb.ws.core.JettyServerInstances;

/**
 * The Class ServerTreeNodeWebAppSettings.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ServerTreeNodeWebAppSettings extends AbstractServerTreeNodeObject {

	private ServerTreeNodeServer serverTreeNodeServer;
	
	/**
	 * Instantiates a new server tree node object for server services.
	 *
	 * @param serverServiceWrapper the server service wrapper
	 * @param jettyServerInstances the jetty server instances
	 */
	public ServerTreeNodeWebAppSettings(ServerTreeNodeServer serverTreeNodeServer) {
		this.serverTreeNodeServer = serverTreeNodeServer;
	}

	/**
	 * Returns the corresponding {@link ServerTreeNodeServer}.
	 * @return the server tree node server
	 */
	public ServerTreeNodeServer getServerTreeNodeServer() {
		return serverTreeNodeServer;
	}
	/**
	 * Returns the {@link JettyServerInstances}.
	 * @return the jetty server instances (will be null, if the server was not started)
	 */
	public JettyServerInstances getJettyServerInstances() {
		return this.getServerTreeNodeServer().getJettyServerInstances();
	}
	
	
	/**
	 * Checks if the current server is running.
	 * @return true, if the server is running
	 */
	public boolean isRunningServer() {
		return this.getJettyServerInstances()!=null && this.getJettyServerInstances().getServer()!=null && this.getJettyServerInstances().getServer().isStarted()==true;
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
		return "<html><b>Web-Application Settings</b></html>";
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.ServerTreeNodeObject#getToolTipText()
	 */
	@Override
	public String getToolTipText() {
		return "<html><b>Web-Application Settings for server '" + this.getJettyConfiguration().getServerName() + "'</html>";
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.ServerTreeNodeObject#getNodeIcon()
	 */
	@Override
	public Icon getNodeIcon() {
		return BundleHelper.getImageIcon("WebApp.png");
	}

	/**
	 * Returns the {@link JettyConfiguration} that can be used to edit the JettySettings 
	 * before they are stored in the properties file within the AWB-properties directory.
	 * 
	 * @return the current working copy of the JettyConfiguration as stored in the corresponding properties file
	 */
	public JettyConfiguration getJettyConfiguration() {
		return this.getServerTreeNodeServer().getJettyConfiguration();
	}
	
	/**
	 * Returns the security settings for the whole server.
	 * @return the security settings
	 */
	public JettySecuritySettings getSecuritySettings() {
		return this.getJettyConfiguration().getSecuritySettings();
	}
	
	/**
	 * Saves the currently edited JettySettings.
	 * @return true, if successful
	 */
	public boolean save() {
		return this.getServerTreeNodeServer().save();
	}
	/**
	 * Checks for changed settings.
	 * @return true, if settings have changed
	 */
	public boolean hasChangedJettySettings() {
		return this.getServerTreeNodeServer().hasChangedJettySettings();
	}

}
