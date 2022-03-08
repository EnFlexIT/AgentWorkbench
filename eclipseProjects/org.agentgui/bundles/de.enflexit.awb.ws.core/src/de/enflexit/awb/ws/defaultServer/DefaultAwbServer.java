package de.enflexit.awb.ws.defaultServer;

import agentgui.core.application.Application;
import agentgui.core.config.GlobalInfo.ExecutionMode;
import de.enflexit.awb.ws.AwbWebServerService;
import de.enflexit.awb.ws.core.JettyConfiguration;
import de.enflexit.awb.ws.core.JettyConfiguration.StartOn;

/**
 * The Class DefaultAwbServer.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DefaultAwbServer implements AwbWebServerService {

	public static final String DEFAULT_AWB_SERVER_NAME = "AWB-WebServer";
	
	private JettyConfiguration jettyConfiguration;
	
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.ws.AwbWebServerService#getJettyConfiguration()
	 */
	@Override
	public JettyConfiguration getJettyConfiguration() {
		if (jettyConfiguration==null) {
			jettyConfiguration = new JettyConfiguration(DEFAULT_AWB_SERVER_NAME, this.getStartOn(), null, true);
		}
		return jettyConfiguration;
	}

	/**
	 * Depending on the {@link ExecutionMode} of AWB, this method returns the argument when the default server has to be started.
	 * @return the StartOn parameter for the DefaultAwbServer 
	 */
	private StartOn getStartOn() {
		
		switch (Application.getGlobalInfo().getExecutionMode()) {
		case APPLICATION:
			return StartOn.ManualStart;
			
		case SERVER:
		case SERVER_MASTER:
		case SERVER_SLAVE:
			return StartOn.AwbStart;

		case DEVICE_SYSTEM:
			return StartOn.AwbStart;
		}
		return StartOn.AwbStart;
	}
	
}
