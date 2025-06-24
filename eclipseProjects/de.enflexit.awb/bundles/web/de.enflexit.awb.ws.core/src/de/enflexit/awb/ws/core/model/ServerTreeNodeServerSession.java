package de.enflexit.awb.ws.core.model;

import javax.swing.Icon;

import de.enflexit.awb.ws.BundleHelper;
import de.enflexit.awb.ws.core.JettyConfiguration;
import de.enflexit.awb.ws.core.JettyServerInstances;
import de.enflexit.awb.ws.core.JettySessionSettings;

/**
 * The Class ServerTreeNodeServerSession.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ServerTreeNodeServerSession extends AbstractServerTreeNodeObject {

	private ServerTreeNodeServer serverTreeNodeServer;
	
	/**
	 * Instantiates a new server tree node object for server services.
	 *
	 * @param serverServiceWrapper the server service wrapper
	 * @param jettyServerInstances the jetty server instances
	 */
	public ServerTreeNodeServerSession(ServerTreeNodeServer serverTreeNodeServer) {
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
		return "<html><b>Session Settings</b></html>";
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.ServerTreeNodeObject#getToolTipText()
	 */
	@Override
	public String getToolTipText() {
		return "<html><b>Session Settings for server '" + this.getJettyConfiguration().getServerName() + "'</html>";
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.ServerTreeNodeObject#getNodeIcon()
	 */
	@Override
	public Icon getNodeIcon() {
		boolean isUseIndividualSettings = this.getSessionSettings().isUseIndividualSettings();
		if (isUseIndividualSettings==false) {
			return BundleHelper.getImageIcon("SessionGrey.png");
		}
		if (this.isRunningServer()==true) {
			return BundleHelper.getImageIcon("SessionGreen.png");
		}
		return BundleHelper.getImageIcon("SessionRed.png");
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
	 * Returns the session settings for the whole server.
	 * @return the session settings
	 */
	public JettySessionSettings getSessionSettings() {
		return this.getJettyConfiguration().getSessionSettings();
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
