package de.enflexit.oidc.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.JTextField;

import de.enflexit.common.swing.AwbThemeColor;
import de.enflexit.oidc.OIDCSettings;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class OIDCSettingsPanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = -4020259086366150207L;
	
	private static final String ICON_PATH_RESTORE_DEFAULTS = "/icons/Reset.png";
	
	private JLabel jLabelIssuerURI;
	private JTextField jTextFieldIssuerURI;
	private JLabel jLabelRealmID;
	private JTextField jTextFieldRealmID;
	private JLabel jLabelClientID;
	private JTextField jTextFieldClientID;
	private JLabel jLabelClientSecret;
	private JTextField jTextFieldClientSecret;
	private JLabel jLabelLocalCallbackServer;
	private JLabel jLabelLocalHTTPPort;
	private JTextField jTextFieldLocalHTTPPort;
	private JLabel jLabelOIDCSettings;
	private JLabel jLabelAuthenticationEndpoint;
	private JTextField jTextFieldAuthenticationEndpoint;
	private JLabel jLabelLogOutEndpoint;
	private JTextField jTextFieldLogOutEndpoint;
	private JPanel jPanelButtons;
	private JButton jButtonApply;
	private JButton jButtonCancel;
	private JButton jButtonRestoreDefaults;
	
	private OIDCSettings oidcSettings;
	private Window parent;
	
	public OIDCSettingsPanel() {
		this.initialize();
	}
	
	
	public OIDCSettingsPanel(OIDCSettings oidcSettings, Window parent) {
		this.parent = parent;
		this.oidcSettings = oidcSettings;
		this.initialize();
		this.setModelToForm();
	}

	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelOIDCSettings = new GridBagConstraints();
		gbc_jLabelOIDCSettings.anchor = GridBagConstraints.SOUTHWEST;
		gbc_jLabelOIDCSettings.gridwidth = 3;
		gbc_jLabelOIDCSettings.insets = new Insets(10, 10, 5, 5);
		gbc_jLabelOIDCSettings.gridx = 0;
		gbc_jLabelOIDCSettings.gridy = 0;
		add(getJLabelOIDCSettings(), gbc_jLabelOIDCSettings);
		GridBagConstraints gbc_jButtonRestoreDefaults = new GridBagConstraints();
		gbc_jButtonRestoreDefaults.insets = new Insets(10, 5, 5, 10);
		gbc_jButtonRestoreDefaults.gridx = 3;
		gbc_jButtonRestoreDefaults.gridy = 0;
		add(getJButtonRestoreDefaults(), gbc_jButtonRestoreDefaults);
		GridBagConstraints gbc_jLabelIssuerURI = new GridBagConstraints();
		gbc_jLabelIssuerURI.anchor = GridBagConstraints.EAST;
		gbc_jLabelIssuerURI.insets = new Insets(5, 10, 5, 5);
		gbc_jLabelIssuerURI.gridx = 0;
		gbc_jLabelIssuerURI.gridy = 1;
		add(getJLabelIssuerURI(), gbc_jLabelIssuerURI);
		GridBagConstraints gbc_jTextFieldIssuerURI = new GridBagConstraints();
		gbc_jTextFieldIssuerURI.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldIssuerURI.gridwidth = 3;
		gbc_jTextFieldIssuerURI.insets = new Insets(5, 5, 5, 5);
		gbc_jTextFieldIssuerURI.gridx = 1;
		gbc_jTextFieldIssuerURI.gridy = 1;
		add(getJTextFieldIssuerURI(), gbc_jTextFieldIssuerURI);
		GridBagConstraints gbc_jLabelRealmID = new GridBagConstraints();
		gbc_jLabelRealmID.anchor = GridBagConstraints.EAST;
		gbc_jLabelRealmID.insets = new Insets(5, 10, 5, 5);
		gbc_jLabelRealmID.gridx = 0;
		gbc_jLabelRealmID.gridy = 2;
		add(getJLabelRealmID(), gbc_jLabelRealmID);
		GridBagConstraints gbc_jTextFieldRealmID = new GridBagConstraints();
		gbc_jTextFieldRealmID.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldRealmID.gridwidth = 3;
		gbc_jTextFieldRealmID.insets = new Insets(5, 5, 5, 5);
		gbc_jTextFieldRealmID.gridx = 1;
		gbc_jTextFieldRealmID.gridy = 2;
		add(getJTextFieldRealmID(), gbc_jTextFieldRealmID);
		GridBagConstraints gbc_jLabelClientID = new GridBagConstraints();
		gbc_jLabelClientID.anchor = GridBagConstraints.EAST;
		gbc_jLabelClientID.insets = new Insets(5, 10, 5, 5);
		gbc_jLabelClientID.gridx = 0;
		gbc_jLabelClientID.gridy = 3;
		add(getJLabelClientID(), gbc_jLabelClientID);
		GridBagConstraints gbc_jTextFieldClientID = new GridBagConstraints();
		gbc_jTextFieldClientID.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldClientID.gridwidth = 3;
		gbc_jTextFieldClientID.insets = new Insets(5, 5, 5, 5);
		gbc_jTextFieldClientID.gridx = 1;
		gbc_jTextFieldClientID.gridy = 3;
		add(getJTextFieldClientID(), gbc_jTextFieldClientID);
		GridBagConstraints gbc_jLabelClientSecret = new GridBagConstraints();
		gbc_jLabelClientSecret.anchor = GridBagConstraints.EAST;
		gbc_jLabelClientSecret.insets = new Insets(5, 10, 5, 5);
		gbc_jLabelClientSecret.gridx = 0;
		gbc_jLabelClientSecret.gridy = 4;
		add(getJLabelClientSecret(), gbc_jLabelClientSecret);
		GridBagConstraints gbc_jTextFieldClientSecret = new GridBagConstraints();
		gbc_jTextFieldClientSecret.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldClientSecret.gridwidth = 3;
		gbc_jTextFieldClientSecret.insets = new Insets(5, 5, 5, 5);
		gbc_jTextFieldClientSecret.gridx = 1;
		gbc_jTextFieldClientSecret.gridy = 4;
		add(getJTextFieldClientSecret(), gbc_jTextFieldClientSecret);
		GridBagConstraints gbc_jLabelLocalCallbackServer = new GridBagConstraints();
		gbc_jLabelLocalCallbackServer.anchor = GridBagConstraints.WEST;
		gbc_jLabelLocalCallbackServer.gridwidth = 3;
		gbc_jLabelLocalCallbackServer.insets = new Insets(15, 12, 5, 5);
		gbc_jLabelLocalCallbackServer.gridx = 0;
		gbc_jLabelLocalCallbackServer.gridy = 5;
		add(getJLabelLocalCallbackServer(), gbc_jLabelLocalCallbackServer);
		GridBagConstraints gbc_jLabelLocalPort = new GridBagConstraints();
		gbc_jLabelLocalPort.gridwidth = 2;
		gbc_jLabelLocalPort.anchor = GridBagConstraints.EAST;
		gbc_jLabelLocalPort.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelLocalPort.gridx = 0;
		gbc_jLabelLocalPort.gridy = 6;
		add(getJLabelLocalHTTPPort(), gbc_jLabelLocalPort);
		GridBagConstraints gbc_jTextFieldLocalPort = new GridBagConstraints();
		gbc_jTextFieldLocalPort.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldLocalPort.gridwidth = 2;
		gbc_jTextFieldLocalPort.insets = new Insets(5, 5, 5, 5);
		gbc_jTextFieldLocalPort.gridx = 2;
		gbc_jTextFieldLocalPort.gridy = 6;
		add(getJTextFieldLocalHTTPPort(), gbc_jTextFieldLocalPort);
		GridBagConstraints gbc_jLabelAuthenticationEndpoint = new GridBagConstraints();
		gbc_jLabelAuthenticationEndpoint.anchor = GridBagConstraints.EAST;
		gbc_jLabelAuthenticationEndpoint.gridwidth = 2;
		gbc_jLabelAuthenticationEndpoint.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelAuthenticationEndpoint.gridx = 0;
		gbc_jLabelAuthenticationEndpoint.gridy = 7;
		add(getJLabelAuthenticationEndpoint(), gbc_jLabelAuthenticationEndpoint);
		GridBagConstraints gbc_jTextFieldAuthenticationEndpoint = new GridBagConstraints();
		gbc_jTextFieldAuthenticationEndpoint.gridwidth = 2;
		gbc_jTextFieldAuthenticationEndpoint.insets = new Insets(5, 5, 5, 5);
		gbc_jTextFieldAuthenticationEndpoint.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldAuthenticationEndpoint.gridx = 2;
		gbc_jTextFieldAuthenticationEndpoint.gridy = 7;
		add(getJTextFieldAuthenticationEndpoint(), gbc_jTextFieldAuthenticationEndpoint);
		GridBagConstraints gbc_jLabelLogOutEndpoint = new GridBagConstraints();
		gbc_jLabelLogOutEndpoint.anchor = GridBagConstraints.EAST;
		gbc_jLabelLogOutEndpoint.gridwidth = 2;
		gbc_jLabelLogOutEndpoint.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelLogOutEndpoint.gridx = 0;
		gbc_jLabelLogOutEndpoint.gridy = 8;
		add(getJLabelLogOutEndpoint(), gbc_jLabelLogOutEndpoint);
		GridBagConstraints gbc_jTextFieldLogOutEndpoint = new GridBagConstraints();
		gbc_jTextFieldLogOutEndpoint.gridwidth = 2;
		gbc_jTextFieldLogOutEndpoint.insets = new Insets(5, 5, 5, 5);
		gbc_jTextFieldLogOutEndpoint.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldLogOutEndpoint.gridx = 2;
		gbc_jTextFieldLogOutEndpoint.gridy = 8;
		add(getJTextFieldLogOutEndpoint(), gbc_jTextFieldLogOutEndpoint);
		GridBagConstraints gbc_jPanelButtons = new GridBagConstraints();
		gbc_jPanelButtons.gridwidth = 4;
		gbc_jPanelButtons.insets = new Insets(0, 0, 0, 5);
		gbc_jPanelButtons.fill = GridBagConstraints.BOTH;
		gbc_jPanelButtons.gridx = 0;
		gbc_jPanelButtons.gridy = 9;
		add(getJPanelButtons(), gbc_jPanelButtons);
	}

	private JLabel getJLabelIssuerURI() {
		if (jLabelIssuerURI == null) {
			jLabelIssuerURI = new JLabel("Issuer URI:");
			jLabelIssuerURI.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelIssuerURI;
	}
	private JTextField getJTextFieldIssuerURI() {
		if (jTextFieldIssuerURI == null) {
			jTextFieldIssuerURI = new JTextField();
			jTextFieldIssuerURI.setColumns(10);
		}
		return jTextFieldIssuerURI;
	}
	private JLabel getJLabelRealmID() {
		if (jLabelRealmID == null) {
			jLabelRealmID = new JLabel("Realm ID:");
			jLabelRealmID.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelRealmID;
	}
	private JTextField getJTextFieldRealmID() {
		if (jTextFieldRealmID == null) {
			jTextFieldRealmID = new JTextField();
			jTextFieldRealmID.setColumns(10);
		}
		return jTextFieldRealmID;
	}
	private JLabel getJLabelClientID() {
		if (jLabelClientID == null) {
			jLabelClientID = new JLabel("Client ID:");
			jLabelClientID.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelClientID;
	}
	private JTextField getJTextFieldClientID() {
		if (jTextFieldClientID == null) {
			jTextFieldClientID = new JTextField();
			jTextFieldClientID.setColumns(10);
		}
		return jTextFieldClientID;
	}
	private JLabel getJLabelClientSecret() {
		if (jLabelClientSecret == null) {
			jLabelClientSecret = new JLabel("Client Secret:");
			jLabelClientSecret.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelClientSecret;
	}
	private JTextField getJTextFieldClientSecret() {
		if (jTextFieldClientSecret == null) {
			jTextFieldClientSecret = new JTextField();
			jTextFieldClientSecret.setColumns(10);
		}
		return jTextFieldClientSecret;
	}
	private JLabel getJLabelLocalCallbackServer() {
		if (jLabelLocalCallbackServer == null) {
			jLabelLocalCallbackServer = new JLabel("Callback Server Settings");
			jLabelLocalCallbackServer.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelLocalCallbackServer;
	}
	private JLabel getJLabelLocalHTTPPort() {
		if (jLabelLocalHTTPPort == null) {
			jLabelLocalHTTPPort = new JLabel("Local HTTP Port:");
			jLabelLocalHTTPPort.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelLocalHTTPPort;
	}
	private JTextField getJTextFieldLocalHTTPPort() {
		if (jTextFieldLocalHTTPPort == null) {
			jTextFieldLocalHTTPPort = new JTextField();
			jTextFieldLocalHTTPPort.setColumns(10);
		}
		return jTextFieldLocalHTTPPort;
	}
	private JLabel getJLabelOIDCSettings() {
		if (jLabelOIDCSettings == null) {
			jLabelOIDCSettings = new JLabel("KeyCloak / OpenID Connect Settings:");
			jLabelOIDCSettings.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelOIDCSettings;
	}
	private JLabel getJLabelAuthenticationEndpoint() {
		if (jLabelAuthenticationEndpoint == null) {
			jLabelAuthenticationEndpoint = new JLabel("Authentication Endpoint:");
			jLabelAuthenticationEndpoint.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelAuthenticationEndpoint;
	}
	private JTextField getJTextFieldAuthenticationEndpoint() {
		if (jTextFieldAuthenticationEndpoint == null) {
			jTextFieldAuthenticationEndpoint = new JTextField();
			jTextFieldAuthenticationEndpoint.setColumns(10);
		}
		return jTextFieldAuthenticationEndpoint;
	}
	private JLabel getJLabelLogOutEndpoint() {
		if (jLabelLogOutEndpoint == null) {
			jLabelLogOutEndpoint = new JLabel("Log Out Endpoint:");
			jLabelLogOutEndpoint.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelLogOutEndpoint;
	}
	private JTextField getJTextFieldLogOutEndpoint() {
		if (jTextFieldLogOutEndpoint == null) {
			jTextFieldLogOutEndpoint = new JTextField();
			jTextFieldLogOutEndpoint.setColumns(10);
		}
		return jTextFieldLogOutEndpoint;
	}
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			GridBagLayout gbl_jPanelButtons = new GridBagLayout();
			gbl_jPanelButtons.columnWidths = new int[]{0, 0, 0};
			gbl_jPanelButtons.rowHeights = new int[]{0, 0};
			gbl_jPanelButtons.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
			gbl_jPanelButtons.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelButtons.setLayout(gbl_jPanelButtons);
			GridBagConstraints gbc_jButtonApply = new GridBagConstraints();
			gbc_jButtonApply.anchor = GridBagConstraints.EAST;
			gbc_jButtonApply.insets = new Insets(10, 0, 10, 15);
			gbc_jButtonApply.gridx = 0;
			gbc_jButtonApply.gridy = 0;
			jPanelButtons.add(getJButtonApply(), gbc_jButtonApply);
			GridBagConstraints gbc_jButtonCancel = new GridBagConstraints();
			gbc_jButtonCancel.insets = new Insets(10, 15, 12, 0);
			gbc_jButtonCancel.anchor = GridBagConstraints.WEST;
			gbc_jButtonCancel.gridx = 1;
			gbc_jButtonCancel.gridy = 0;
			jPanelButtons.add(getJButtonCancel(), gbc_jButtonCancel);
		}
		return jPanelButtons;
	}
	private JButton getJButtonApply() {
		if (jButtonApply == null) {
			jButtonApply = new JButton("Apply");
			jButtonApply.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonApply.setPreferredSize(new Dimension(100, 26));
			jButtonApply.setForeground(AwbThemeColor.ButtonTextGreen.getColor());
			jButtonApply.addActionListener(this);
		}
		return jButtonApply;
	}
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton("Cancel");
			jButtonCancel.setPreferredSize(new Dimension(100, 26));
			jButtonCancel.setForeground(AwbThemeColor.ButtonTextRed.getColor());
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}
	private JButton getJButtonRestoreDefaults() {
		if (jButtonRestoreDefaults == null) {
			jButtonRestoreDefaults = new JButton(new ImageIcon(this.getClass().getResource(ICON_PATH_RESTORE_DEFAULTS)));
			jButtonRestoreDefaults.addActionListener(this);
		}
		return jButtonRestoreDefaults;
	}

	/**
	 * Gets the oidc settings.
	 * @return the oidc settings
	 */
	public OIDCSettings getOidcSettings() {
		return oidcSettings;
	}

	/**
	 * Sets the oidc settings.
	 * @param oidcSettings the new oidc settings
	 */
	public void setOidcSettings(OIDCSettings oidcSettings) {
		this.oidcSettings = oidcSettings;
	}
	
	private void setModelToForm() {
		this.getJTextFieldIssuerURI().setText(this.getOidcSettings().getIssuerURL());
		this.getJTextFieldRealmID().setText(this.getOidcSettings().getRealmID());
		this.getJTextFieldClientID().setText(this.getOidcSettings().getClientID());
		this.getJTextFieldClientSecret().setText(this.getOidcSettings().getClientSecret());
		this.getJTextFieldLocalHTTPPort().setText(String.valueOf(this.getOidcSettings().getLocalHTTPPort()));
		this.getJTextFieldAuthenticationEndpoint().setText(this.getOidcSettings().getAuthenticationEndpoint());
		this.getJTextFieldLogOutEndpoint().setText(this.getOidcSettings().getLogOutEndpoint());
	}
	
	private void setFormToModel() {
		this.getOidcSettings().setIssuerURL(this.getJTextFieldIssuerURI().getText());
		this.getOidcSettings().setRealmID(this.getJTextFieldRealmID().getText());
		this.getOidcSettings().setClientID(this.getJTextFieldClientID().getText());
		this.getOidcSettings().setClientSecret(this.getJTextFieldClientSecret().getText());
		this.getOidcSettings().setLocalHTTPPort(Integer.parseInt(this.getJTextFieldLocalHTTPPort().getText()));
		this.getOidcSettings().setAuthenticationEndpoint(this.getJTextFieldAuthenticationEndpoint().getText());
		this.getOidcSettings().setLogOutEndpoint(this.getJTextFieldLogOutEndpoint().getText());
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJButtonApply()) {
			this.setFormToModel();
			parent.setVisible(false);
			this.getOidcSettings().storeToPreferences();
		} else if (ae.getSource()==this.getJButtonCancel()) {
			parent.setVisible(false);
		} else if (ae.getSource()==this.getJButtonRestoreDefaults()) {
			this.setOidcSettings(OIDCSettings.getDefaultSettings());
			this.setModelToForm();
		}
	}
	
}
