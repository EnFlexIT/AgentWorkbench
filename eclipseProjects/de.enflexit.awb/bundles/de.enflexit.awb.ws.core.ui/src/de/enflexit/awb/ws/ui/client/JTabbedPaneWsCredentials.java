package de.enflexit.awb.ws.ui.client;

import javax.swing.JTabbedPane;

import de.enflexit.awb.ws.ui.WsConfigurationInterface;
import de.enflexit.common.swing.AwbBasicTabbedPaneUI;

public class JTabbedPaneWsCredentials extends JTabbedPane implements WsConfigurationInterface {

	private static final long serialVersionUID = -1781728055097048615L;
	private JTabbedPane JTabbedPaneServer;
	private JPanelClientConfiguration panelClientConfiguration;
	
	public JTabbedPaneWsCredentials() {
		this.setUI(new AwbBasicTabbedPaneUI());
		addTab("Assign Credentials", null, getJPanelClientConfiguration(), null);
		addTab("Server", null, getJTabbedPaneServer(), null);
	}

	private JTabbedPane getJTabbedPaneServer() {
		if (JTabbedPaneServer == null) {
			JTabbedPaneServer = new JTabbedPane(JTabbedPane.TOP);
		}
		return JTabbedPaneServer;
	}
	private JPanelClientConfiguration getJPanelClientConfiguration() {
		if (panelClientConfiguration == null) {
			panelClientConfiguration = new JPanelClientConfiguration();
		}
		return panelClientConfiguration;
	}

	@Override
	public boolean hasUnsavedChanges() {
		return getJPanelClientConfiguration().hasUnsavedChanges();
	}

	@Override
	public boolean userConfirmedToChangeView() {
		return hasUnsavedChanges();
	}
}
