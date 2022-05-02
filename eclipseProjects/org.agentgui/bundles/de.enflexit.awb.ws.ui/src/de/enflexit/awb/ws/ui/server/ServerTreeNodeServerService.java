package de.enflexit.awb.ws.ui.server;

import javax.swing.Icon;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import de.enflexit.awb.ws.AwbWebServerServiceWrapper;
import de.enflexit.awb.ws.BundleHelper;
import de.enflexit.awb.ws.core.JettyServerInstances;

/**
 * The Class ServerTreeNodeServerService.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ServerTreeNodeServerService extends ServerTreeNodeObject {

	private AwbWebServerServiceWrapper serverServiceWrapper;
	private JettyServerInstances jettyServerInstances;
	
	/**
	 * Instantiates a new server tree node object for server services.
	 *
	 * @param serverServiceWrapper the server service wrapper
	 * @param jettyServerInstances the jetty server instances
	 */
	public ServerTreeNodeServerService(AwbWebServerServiceWrapper serverServiceWrapper, JettyServerInstances jettyServerInstances) {
		this.serverServiceWrapper = serverServiceWrapper;
		this.jettyServerInstances = jettyServerInstances;
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
	protected String getToolTipText() {
		return "<html><b>" + this.getRunningServerDescription() + ":</b> [" + this.getSourceBundle().getSymbolicName() + "] " + this.serverServiceWrapper.getWebServerService().getClass().getName() + "</html>";
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.ui.server.ServerTreeNodeObject#getNodeIcon()
	 */
	@Override
	protected Icon getNodeIcon() {
		if (this.isRunningServer()==true) {
			return BundleHelper.getImageIcon("awbWeb16Green.png");
		}
		return BundleHelper.getImageIcon("awbWeb16Red.png");
	}

	
	/**
	 * Returns the source bundle of the service.
	 * @return the source bundle
	 */
	private Bundle getSourceBundle() {
		return FrameworkUtil.getBundle(this.serverServiceWrapper.getWebServerService().getClass());
	}
	/**
	 * Checks if the current server is running.
	 * @return true, if the server is running
	 */
	private boolean isRunningServer() {
		return this.jettyServerInstances!=null && this.jettyServerInstances.getServer()!=null && this.jettyServerInstances.getServer().isStarted()==true;
	}
	/**
	 * Returns the running server description.
	 * @return the running server description
	 */
	private String getRunningServerDescription() {
		if (this.isRunningServer()==true) {
			return "Started";
		}
		return "Stopped";
	}
	
}
