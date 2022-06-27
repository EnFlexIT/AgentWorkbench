package de.enflexit.awb.ws.ui;

import java.awt.MenuItem;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.agentgui.gui.swing.MainWindowExtension;

import agentgui.core.application.Application;
import de.enflexit.awb.ws.AwbWebServerServiceWrapper;
import de.enflexit.awb.ws.BundleHelper;
import de.enflexit.awb.ws.core.JettyServerManager;
import de.enflexit.common.swing.JMenuItemDynamicEnabled;
import de.enflexit.common.swing.JMenuItemDynamicEnabled.EnablementCheck;

/**
 * The AwbWsUiIntegration class for the WS-Feature.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class AwbWsUiIntegration extends MainWindowExtension implements ActionListener {

	public enum WsUiIntegrationType {
		Application,
		TrayIcon,
		None
	}
	
	private JMenu jMenuWS;
	private JMenuItem jMenuItemConfiguration;
	private JButton jButtonWsConfiguration;

	private MenuItem trayIconMenuItemConfiguration;
	
	private static JDialogWsConfiguration configDialog;
	
	
	/* (non-Javadoc)
	 * @see org.agentgui.gui.swing.MainWindowExtension#initialize()
	 */
	@Override
	public void initialize() {

		switch (this.getWsUiIntegrationType()) {
		case Application:
			this.addJMenu(this.getJMenuWS(), 5);
			this.addToolbarComponent(this.getToolBarButtonWsConfiguration(), 7, SeparatorPosition.SeparatorAfter);
			this.addTrayIconMenuItem(this.getTrayIconMenuItemWsConfiguration(), 4, SeparatorPosition.SeparatorInFrontOf);
			break;
			
		case TrayIcon:
			this.addTrayIconMenuItem(this.getTrayIconMenuItemWsConfiguration(), 4, SeparatorPosition.SeparatorInFrontOf);
			break;
			
		case None:
			// --- Nothing to do here ! ---------
			break;
		}
	}
	
	/**
	 * Returns the indicator that explains how to integrate UI into AWB.
	 * @return the WsUiIntegrationType
	 */
	public WsUiIntegrationType getWsUiIntegrationType() {
		
		// --- Start with a default value -----------------------
		WsUiIntegrationType wsUiIntegrationType = WsUiIntegrationType.None;
		switch (Application.getGlobalInfo().getExecutionMode()) {
		case APPLICATION:
			wsUiIntegrationType = WsUiIntegrationType.Application;
			break;
			
		case DEVICE_SYSTEM:
			switch (Application.getGlobalInfo().getDeviceServiceExecutionMode()) {
			case AGENT:
				wsUiIntegrationType = WsUiIntegrationType.TrayIcon;
				break;
			case SETUP:
				wsUiIntegrationType = WsUiIntegrationType.Application;	
				break;
			}
			break;

		case SERVER:
		case SERVER_MASTER:
		case SERVER_SLAVE:
			wsUiIntegrationType = WsUiIntegrationType.TrayIcon;
			break;
		}
		
		if (Application.isOperatingHeadless()==true) {
			wsUiIntegrationType = WsUiIntegrationType.None;
		}
		return wsUiIntegrationType;
	}

	
	/**
	 * Returns the WS-JMenu.
	 * @return the j menu WS
	 */
	private JMenu getJMenuWS() {
		if (jMenuWS==null) {
			jMenuWS = new JMenu("Web-Server");
			this.addServerMenuItems();
			jMenuWS.addSeparator();
			jMenuWS.add(this.getJMenuItemWsConfiguration());
		}
		return jMenuWS;
	}
	/**
	 * Adds the server menu items.
	 */
	private void addServerMenuItems() {
		
		List<AwbWebServerServiceWrapper> serverList = JettyServerManager.getInstance().getAwbWebRegistry().getRegisteredWebServerSorted();
		for (AwbWebServerServiceWrapper serverWrapper : serverList) {

			// --- Define check if a menu item is enabled or not ----  
			final String serverName = serverWrapper.getJettyConfiguration().getServerName();
			EnablementCheck checkStartServer = new EnablementCheck() {
				@Override
				public boolean isEnabled() { return JettyServerManager.getInstance().getServerInstances(serverName)==null; }
			};
			EnablementCheck checkStopRestartServer = new EnablementCheck() {
				@Override
				public boolean isEnabled() { return JettyServerManager.getInstance().getServerInstances(serverName)!=null; }
			};
			
			// --- Define the menu items ----------------------------
			JMenuItemDynamicEnabled miStart = new JMenuItemDynamicEnabled("Start Server");
			miStart.setIcon(BundleHelper.getImageIcon("MBstart.png"));
			miStart.setActionCommand(serverName + "@start");
			miStart.setEnablementCheck(checkStartServer);
			miStart.addActionListener(this);
			
			JMenuItemDynamicEnabled miRestart = new JMenuItemDynamicEnabled("Restart Server");
			miRestart.setIcon(BundleHelper.getImageIcon("MBrestart.png"));
			miRestart.setActionCommand(serverName + "@restart");
			miRestart.setEnablementCheck(checkStopRestartServer);
			miRestart.addActionListener(this);
			
			JMenuItemDynamicEnabled miStop = new JMenuItemDynamicEnabled("Stop Server");
			miStop.setIcon(BundleHelper.getImageIcon("MBstop.png"));
			miStop.setActionCommand(serverName + "@stop");
			miStop.setEnablementCheck(checkStopRestartServer);
			miStop.addActionListener(this);
			
			// --- Define the server menu ---------------------------
			JMenu jMenu = new JMenu(serverName);
			jMenu.add(miStart);
			jMenu.add(miRestart);
			jMenu.add(miStop);
			this.getJMenuWS().add(jMenu);
		}
	}
	
	/**
	 * Returns the JMenuItem for the WS configuration.
	 * @return the JMenuItem for the WS configuration
	 */
	private JMenuItem getJMenuItemWsConfiguration() {
		if (jMenuItemConfiguration==null) {
			jMenuItemConfiguration = new JMenuItem("WS-Configuration");
			jMenuItemConfiguration.setIcon(BundleHelper.getImageIcon("awbWeb16.png"));
			jMenuItemConfiguration.addActionListener(this);
		}
		return jMenuItemConfiguration;
	}
	
	/**
	 * Returns the tool bar button for the WS configuration.
	 * @return the tool bar button WS configuration
	 */
	private JButton getToolBarButtonWsConfiguration() {
		if (jButtonWsConfiguration==null) {
			jButtonWsConfiguration = new JButton();
			jButtonWsConfiguration.setIcon(BundleHelper.getImageIcon("awbWeb16.png"));
			jButtonWsConfiguration.addActionListener(this);
		}
		return jButtonWsConfiguration;
	}
	
	/**
	 * Returns the {@link TrayIcon} MenuItem for the WS configuration.
	 * @return the MenuItem for the WS configuration
	 */
	private MenuItem getTrayIconMenuItemWsConfiguration() {
		if (trayIconMenuItemConfiguration==null) {
			trayIconMenuItemConfiguration = new MenuItem("WS-Configuration");
			trayIconMenuItemConfiguration.addActionListener(this);
		}
		return trayIconMenuItemConfiguration;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource()==this.getToolBarButtonWsConfiguration() || 
			ae.getSource()==this.getJMenuItemWsConfiguration() || 
			ae.getSource()==this.getTrayIconMenuItemWsConfiguration()) {
			// --- Open the modal JDialogWsConfiguration ------------ 
			if (configDialog==null) {
				configDialog = new JDialogWsConfiguration(Application.getMainWindow());
				configDialog.setVisible(true);
				// - - - - - - - -
				configDialog.dispose();
				configDialog = null;
				
			} else {
				configDialog.requestFocus();
			}
			
		} else if (ae.getSource() instanceof JMenuItem && ae.getActionCommand()!=null) {
			// --- Server control action ----------------------------
			String[] serverControl = ae.getActionCommand().split("@");
			String serverName = serverControl[0];
			String action = serverControl[1];
			
			switch (action) {
			case "start":
				JettyServerManager.getInstance().startServer(serverName);
				break;
			case "restart":
				JettyServerManager.getInstance().stopServer(serverName);
				JettyServerManager.getInstance().startServer(serverName);
				break;
			case "stop":
				JettyServerManager.getInstance().stopServer(serverName);
				break;
			}
		}
	}
	
}
