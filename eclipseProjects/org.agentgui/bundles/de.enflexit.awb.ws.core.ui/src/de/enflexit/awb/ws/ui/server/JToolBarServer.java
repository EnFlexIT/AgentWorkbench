package de.enflexit.awb.ws.ui.server;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JToolBar;

import de.enflexit.awb.ws.BundleHelper;
import de.enflexit.awb.ws.core.JettyConfiguration;
import de.enflexit.awb.ws.core.JettyServerManager;
import de.enflexit.awb.ws.core.SSLJettyConfiguration;
import de.enflexit.awb.ws.core.model.ServerTreeNodeServer;

/**
 * The Class JToolBarServer.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JToolBarServer extends JToolBar implements ActionListener {

	private static final long serialVersionUID = 935898280006480662L;

	private static final Dimension buttonSize = new Dimension(26, 26);
	
	private JPanelServerConfiguration jPanelServerConfiguration;
	private ServerTreeNodeServer serverTreeNodeServer;
	
	private JButton jButtonSave;
	
	private JButton jButtonResetToSavedSettings;
	private JButton jButtonResetToServiceSettings;
	
	private JButton jButtonSetDefaultSslKeyStore;
	
	private JButton jButtonStopServer;
	private JButton jButtonRestartServer;
	private JButton jButtonStartServer;

	
	public JToolBarServer(JPanelServerConfiguration jPanelServerConfiguration) {
		this.jPanelServerConfiguration = jPanelServerConfiguration;
		this.initialize();
	}
	/**
	 * Initialize.
	 */
	private void initialize() {
		
		this.setFloatable(false);
		this.setRollover(true);
		this.setBorder(BorderFactory.createEmptyBorder());
		
		this.add(this.getJButtonSave());
		this.addSeparator();
		
		this.add(this.getJButtonResetToSavedSettings());
		this.add(this.getJButtonResetToServiceSettings());
		this.addSeparator();
		
		this.add(this.getJButtonSetDefaultSslKeyStore());
		this.addSeparator();
		
		this.add(this.getJButtonStartServer());
		this.add(this.getJButtonRestartServer());
		this.add(this.getJButtonStopServer());
		this.addSeparator();
	}
	

	public JButton getJButtonSave() {
		if (jButtonSave==null) {
			jButtonSave = new JButton();
			jButtonSave.setPreferredSize(buttonSize);
			jButtonSave.setToolTipText("Save current settings");
			jButtonSave.setIcon(BundleHelper.getImageIcon("MBsave.png"));
			jButtonSave.addActionListener(this);
		}
		return jButtonSave;
	}
	
	public JButton getJButtonResetToSavedSettings() {
		if (jButtonResetToSavedSettings==null) {
			jButtonResetToSavedSettings = new JButton();
			jButtonResetToSavedSettings.setPreferredSize(buttonSize);
			jButtonResetToSavedSettings.setToolTipText("Reset to previous settings");
			jButtonResetToSavedSettings.setIcon(BundleHelper.getImageIcon("MBreset.png"));
			jButtonResetToSavedSettings.addActionListener(this);
		}
		return jButtonResetToSavedSettings;
	}
	public JButton getJButtonResetToServiceSettings() {
		if (jButtonResetToServiceSettings==null) {
			jButtonResetToServiceSettings = new JButton();
			jButtonResetToServiceSettings.setPreferredSize(buttonSize);
			jButtonResetToServiceSettings.setToolTipText("Reset to settings of service definition");
			jButtonResetToServiceSettings.setIcon(BundleHelper.getImageIcon("MBresetService.png"));
			jButtonResetToServiceSettings.addActionListener(this);
		}
		return jButtonResetToServiceSettings;
	}
	
	public JButton getJButtonSetDefaultSslKeyStore() {
		if (jButtonSetDefaultSslKeyStore==null) {
			jButtonSetDefaultSslKeyStore = new JButton();
			jButtonSetDefaultSslKeyStore.setToolTipText("Set default SSL-KeyStore for test purposes...");
			jButtonSetDefaultSslKeyStore.setPreferredSize(buttonSize);
			jButtonSetDefaultSslKeyStore.setIcon(BundleHelper.getImageIcon("MBstart.png"));
			jButtonSetDefaultSslKeyStore.addActionListener(this);
		}
		return jButtonSetDefaultSslKeyStore;
	}
	
	public JButton getJButtonStartServer() {
		if (jButtonStartServer==null) {
			jButtonStartServer = new JButton();
			jButtonStartServer.setToolTipText("Start Server");
			jButtonStartServer.setPreferredSize(buttonSize);
			jButtonStartServer.setIcon(BundleHelper.getImageIcon("MBstart.png"));
			jButtonStartServer.addActionListener(this);
		}
		return jButtonStartServer;
	}
	public JButton getJButtonRestartServer() {
		if (jButtonRestartServer==null) {
			jButtonRestartServer = new JButton();
			jButtonRestartServer.setToolTipText("Restart Server");
			jButtonRestartServer.setPreferredSize(buttonSize);
			jButtonRestartServer.setIcon(BundleHelper.getImageIcon("MBrestart.png"));
			jButtonRestartServer.addActionListener(this);
		}
		return jButtonRestartServer;
	}
	public JButton getJButtonStopServer() {
		if (jButtonStopServer==null) {
			jButtonStopServer = new JButton();
			jButtonStopServer.setToolTipText("Stop Server");
			jButtonStopServer.setPreferredSize(buttonSize);
			jButtonStopServer.setIcon(BundleHelper.getImageIcon("MBstop.png"));
			jButtonStopServer.addActionListener(this);
		}
		return jButtonStopServer;
	}
	
	/**
	 * Sets the current server tree node.
	 * @param serverTreeNode the new server tree node
	 */
	public void setServerTreeNode(ServerTreeNodeServer serverTreeNodeServer) {
		this.serverTreeNodeServer = serverTreeNodeServer;
		this.updateView();
	}
	private JettyConfiguration getJettyConfiguration() {
		return this.serverTreeNodeServer.getJettyConfiguration();
	}
	private void updateView() {
	
		boolean isRunningServer = this.serverTreeNodeServer.isRunningServer();
		this.getJButtonStartServer().setEnabled(!isRunningServer);
		this.getJButtonRestartServer().setEnabled(isRunningServer);
		this.getJButtonStopServer().setEnabled(isRunningServer);

	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		if (ae.getSource()==this.getJButtonSave()) {
			this.serverTreeNodeServer.save();
			
		} else if (ae.getSource()==this.getJButtonResetToSavedSettings()) {
			this.serverTreeNodeServer.revertJettyConfigurationToPropertiesFile();
			this.jPanelServerConfiguration.reloadView();
			
		} else if (ae.getSource()==this.getJButtonResetToServiceSettings()) {
			this.serverTreeNodeServer.revertJettyConfigurationToServiceDefinition();
			this.jPanelServerConfiguration.reloadView();
		
		} else if (ae.getSource()==this.getJButtonSetDefaultSslKeyStore()) {
			// --- Ask user for the password ------------------------
			char[] password = this.getPasswordFromUser();
			if (password!=null) {
				if (SSLJettyConfiguration.createDefaultSettingsForSSL(this.serverTreeNodeServer.getJettyConfiguration(), password)==true) {
					this.serverTreeNodeServer.save();
					this.jPanelServerConfiguration.reloadView();
				}
			}
			
		} else if (ae.getSource()==this.getJButtonStartServer()) {
			JettyServerManager.getInstance().startServer(this.getJettyConfiguration());
			this.jPanelServerConfiguration.reloadView();
			
		} else if (ae.getSource()==this.getJButtonRestartServer()) {
			JettyServerManager.getInstance().stopServer(this.getJettyConfiguration().getServerName());
			JettyServerManager.getInstance().startServer(this.getJettyConfiguration());
			this.jPanelServerConfiguration.reloadView();
			
		} else if (ae.getSource()==this.getJButtonStopServer()) {
			JettyServerManager.getInstance().stopServer(this.getJettyConfiguration().getServerName());
			this.jPanelServerConfiguration.reloadView();
		}
	}
	/**
	 * Returns the password entry from user.
	 * @return the password from user
	 */
	private char[] getPasswordFromUser() {
		
		char[] password = null;

		String title =   "Enter KeyStore Password";
		String message = "Please, enter a password for the default SSL-KeyStore: ";

		JPasswordField pass = new JPasswordField(20);

		JPanel panel = new JPanel();
		panel.add(new JLabel(message));
		panel.add(pass);
		
		int option = JOptionPane.showOptionDialog(null, panel, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
		if(option == JOptionPane.OK_OPTION) {
		    password = pass.getPassword();
		}
		return password;
	}
	
}
