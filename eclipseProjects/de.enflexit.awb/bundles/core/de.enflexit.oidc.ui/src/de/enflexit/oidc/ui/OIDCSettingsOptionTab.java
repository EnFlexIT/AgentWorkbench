package de.enflexit.oidc.ui;

import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Window;

import javax.swing.JTextField;

import de.enflexit.awb.baseUI.options.AbstractOptionTab;
import de.enflexit.common.swing.AwbThemeColor;
import de.enflexit.language.Language;
import de.enflexit.oidc.OIDCSettings;

import javax.swing.JButton;

/**
 * This options tab provides the configuration options for OpenID Connect authentication. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class OIDCSettingsOptionTab extends AbstractOptionTab {
	
	private static final long serialVersionUID = -4020259086366150207L;
	
	public static final String OPTIONS_TAB_TITLE = "OpenID Connect Settings";
	private static final String OPTIONS_TAB_TOOLTIP_TEXT = "Configuration options for OpenID Connect authenticaiton";
	
	private JLabel jLabelIssuerURI;
	private JTextField jTextFieldIssuerURI;
	private JLabel jLabelRealmID;
	private JTextField jTextFieldRealmID;
	private JLabel jLabelClientID;
	private JTextField jTextFieldClientID;
	private JLabel jLabelClientSecret;
	private JTextField jTextFieldClientSecret;
	private JLabel jLabelOIDCSettings;
	private JButton jButtonApply;
	
	private OIDCSettings oidcSettings;
	private Window parent;
	
	/**
	 * Instantiates a new OIDC settings panel.
	 */
	public OIDCSettingsOptionTab() {
		this.initialize();
	}
	
	/**
	 * Instantiates a new OIDC settings panel.
	 * @param oidcSettings the oidc settings
	 * @param parent the parent
	 */
	public OIDCSettingsOptionTab(OIDCSettings oidcSettings, Window parent) {
		this.parent = parent;
		this.oidcSettings = oidcSettings;
		this.initialize();
		this.setModelToForm();
	}

	/**
	 * Initializes the UI components.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelOIDCSettings = new GridBagConstraints();
		gbc_jLabelOIDCSettings.anchor = GridBagConstraints.SOUTHWEST;
		gbc_jLabelOIDCSettings.gridwidth = 2;
		gbc_jLabelOIDCSettings.insets = new Insets(20, 20, 5, 5);
		gbc_jLabelOIDCSettings.gridx = 0;
		gbc_jLabelOIDCSettings.gridy = 0;
		add(getJLabelOIDCSettings(), gbc_jLabelOIDCSettings);
		GridBagConstraints gbc_jButtonApply = new GridBagConstraints();
		gbc_jButtonApply.insets = new Insets(20, 20, 5, 20);
		gbc_jButtonApply.anchor = GridBagConstraints.NORTHWEST;
		gbc_jButtonApply.gridx = 2;
		gbc_jButtonApply.gridy = 0;
		add(getJButtonApply(), gbc_jButtonApply);
		GridBagConstraints gbc_jLabelIssuerURI = new GridBagConstraints();
		gbc_jLabelIssuerURI.anchor = GridBagConstraints.WEST;
		gbc_jLabelIssuerURI.insets = new Insets(10, 20, 5, 5);
		gbc_jLabelIssuerURI.gridx = 0;
		gbc_jLabelIssuerURI.gridy = 1;
		add(getJLabelIssuerURI(), gbc_jLabelIssuerURI);
		GridBagConstraints gbc_jTextFieldIssuerURI = new GridBagConstraints();
		gbc_jTextFieldIssuerURI.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldIssuerURI.insets = new Insets(5, 5, 5, 5);
		gbc_jTextFieldIssuerURI.gridx = 1;
		gbc_jTextFieldIssuerURI.gridy = 1;
		add(getJTextFieldIssuerURI(), gbc_jTextFieldIssuerURI);
		GridBagConstraints gbc_jLabelRealmID = new GridBagConstraints();
		gbc_jLabelRealmID.anchor = GridBagConstraints.WEST;
		gbc_jLabelRealmID.insets = new Insets(10, 20, 5, 5);
		gbc_jLabelRealmID.gridx = 0;
		gbc_jLabelRealmID.gridy = 2;
		add(getJLabelRealmID(), gbc_jLabelRealmID);
		GridBagConstraints gbc_jTextFieldRealmID = new GridBagConstraints();
		gbc_jTextFieldRealmID.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldRealmID.insets = new Insets(5, 5, 5, 5);
		gbc_jTextFieldRealmID.gridx = 1;
		gbc_jTextFieldRealmID.gridy = 2;
		add(getJTextFieldRealmID(), gbc_jTextFieldRealmID);
		GridBagConstraints gbc_jLabelClientID = new GridBagConstraints();
		gbc_jLabelClientID.anchor = GridBagConstraints.WEST;
		gbc_jLabelClientID.insets = new Insets(10, 20, 5, 5);
		gbc_jLabelClientID.gridx = 0;
		gbc_jLabelClientID.gridy = 3;
		add(getJLabelClientID(), gbc_jLabelClientID);
		GridBagConstraints gbc_jTextFieldClientID = new GridBagConstraints();
		gbc_jTextFieldClientID.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldClientID.insets = new Insets(5, 5, 5, 5);
		gbc_jTextFieldClientID.gridx = 1;
		gbc_jTextFieldClientID.gridy = 3;
		add(getJTextFieldClientID(), gbc_jTextFieldClientID);
		GridBagConstraints gbc_jLabelClientSecret = new GridBagConstraints();
		gbc_jLabelClientSecret.anchor = GridBagConstraints.WEST;
		gbc_jLabelClientSecret.insets = new Insets(10, 20, 0, 5);
		gbc_jLabelClientSecret.gridx = 0;
		gbc_jLabelClientSecret.gridy = 4;
		add(getJLabelClientSecret(), gbc_jLabelClientSecret);
		GridBagConstraints gbc_jTextFieldClientSecret = new GridBagConstraints();
		gbc_jTextFieldClientSecret.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldClientSecret.insets = new Insets(5, 5, 0, 5);
		gbc_jTextFieldClientSecret.gridx = 1;
		gbc_jTextFieldClientSecret.gridy = 4;
		add(getJTextFieldClientSecret(), gbc_jTextFieldClientSecret);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.baseUI.options.AbstractOptionTab#getTitle()
	 */
	@Override
	public String getTitle() {
		return Language.translate(OPTIONS_TAB_TITLE, Language.EN);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.baseUI.options.AbstractOptionTab#getTabToolTipText()
	 */
	@Override
	public String getTabToolTipText() {
		return Language.translate(OPTIONS_TAB_TOOLTIP_TEXT, Language.EN);
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
	private JLabel getJLabelOIDCSettings() {
		if (jLabelOIDCSettings == null) {
			jLabelOIDCSettings = new JLabel("OpenID Connect Settings:");
			jLabelOIDCSettings.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelOIDCSettings;
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
	}
	
	private void setFormToModel() {
		this.getOidcSettings().setIssuerURL(this.getJTextFieldIssuerURI().getText());
		this.getOidcSettings().setRealmID(this.getJTextFieldRealmID().getText());
		this.getOidcSettings().setClientID(this.getJTextFieldClientID().getText());
		this.getOidcSettings().setClientSecret(this.getJTextFieldClientSecret().getText());
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
		}
	}
	
}
