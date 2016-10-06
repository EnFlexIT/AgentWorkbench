package agentgui.core.config.auth;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import agentgui.core.application.Language;

public class OIDCPanel extends JPanel implements ActionListener {

	public static final String DEBUG_ISSUER_URI = "https://se238124.zim.uni-due.de:8443/auth/realms/EOMID/";
	public static final String DEBUG_RESOURCE_URI = "https://se238124.zim.uni-due.de:18443/vanilla/profile.jsp";
	public static final String DEBUG_CLIENT_ID = "testclient";
	public static final String DEBUG_CLIENT_SECRET = "b3b651a0-66a7-435e-8f1c-b1460bbfe9e0";
	private static final String COMMAND_CONNECT = "connectOIDC";

	private JLabel lblOIDCValues;
	private JButton bConnect;

	private JTextField tfUsername;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JLabel lblLicenseServer;
	private JLabel lblIdProvider;
	private JTextField tfPassword;
	private JFormattedTextField tfLicenseServer;
	private JFormattedTextField tfIdProvider;
	private JTextField tfClientId;
	private JLabel lblClientId;
	private JTextField tfClientSecret;
	private JLabel lblClientSecret;

	private ActionListener parentGUI;
	private OIDCAuthorization owner;

	/**
	 * 
	 */
	private static final long serialVersionUID = -169367444435859302L;
	private JButton btnResult;

	public OIDCPanel(OIDCAuthorization owner) {
		this();
		this.owner = owner;
	}

	public OIDCPanel setParent(ActionListener parent) {
		this.parentGUI = parent;
		return this; // for chaining
	}

	private OIDCPanel() {
		super();

		GridBagLayout gridBagLayout = new GridBagLayout();

		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 24, 22, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };

		this.setLayout(gridBagLayout);

/*
		GridBagLayout gbl_jPanelTop = new GridBagLayout();
		gbl_jPanelTop.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		this.setLayout(gbl_jPanelTop);
	*/	
		GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
		gridBagConstraints18.anchor = GridBagConstraints.WEST;
		gridBagConstraints18.gridx = 1;
		gridBagConstraints18.gridy = 0;
		gridBagConstraints18.insets = new Insets(10, 20, 0, 10);
		
		GridBagConstraints gbc_lblOIDCValues = new GridBagConstraints();
		gbc_lblOIDCValues.gridwidth = 2;
		gbc_lblOIDCValues.insets = new Insets(10, 10, 5, 5);
		gbc_lblOIDCValues.gridy = 0;
		gbc_lblOIDCValues.ipadx = 0;
		gbc_lblOIDCValues.anchor = GridBagConstraints.WEST;
		gbc_lblOIDCValues.weightx = 0.0;
		gbc_lblOIDCValues.gridx = 0;

		lblOIDCValues = new JLabel();
		lblOIDCValues.setFont(new Font("Dialog", Font.BOLD, 12));
		this.add(lblOIDCValues, gbc_lblOIDCValues);
		// this.add(getTfClientSecret(), gbc_tfClientSecret);

		lblOIDCValues.setText(Language.translate("OpenID Connect-Authorisierung"));
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.insets = new Insets(0, 10, 5, 5);
		gbc_lblUsername.anchor = GridBagConstraints.WEST;
		gbc_lblUsername.gridx = 0;
		gbc_lblUsername.gridy = 1;
		this.add(getLblUsername(), gbc_lblUsername);
		GridBagConstraints gbc_tfUsername = new GridBagConstraints();
		gbc_tfUsername.insets = new Insets(0, 0, 5, 5);
		gbc_tfUsername.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfUsername.gridx = 1;
		gbc_tfUsername.gridy = 1;
		this.add(getTfUsername(), gbc_tfUsername);
		GridBagConstraints gbc_bConnect = new GridBagConstraints();
		gbc_bConnect.anchor = GridBagConstraints.WEST;
		gbc_bConnect.gridx = 2;
		gbc_bConnect.gridy = 1;
		gbc_bConnect.weightx = 0.0;
		gbc_bConnect.insets = new Insets(0, 0, 5, 10);
		this.add(getBConnect(), gbc_bConnect);
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.EAST;
		gbc_lblPassword.insets = new Insets(0, 10, 5, 5);
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 2;
		this.add(getLblPassword(), gbc_lblPassword);
		GridBagConstraints gbc_tfPassword = new GridBagConstraints();
		gbc_tfPassword.insets = new Insets(0, 0, 5, 5);
		gbc_tfPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfPassword.gridx = 1;
		gbc_tfPassword.gridy = 2;
		this.add(getTfPassword(), gbc_tfPassword);
		GridBagConstraints gbc_lblLicenseServer = new GridBagConstraints();
		gbc_lblLicenseServer.anchor = GridBagConstraints.EAST;
		gbc_lblLicenseServer.insets = new Insets(0, 0, 5, 5);
		gbc_lblLicenseServer.gridx = 1;
		gbc_lblLicenseServer.gridy = 2;
//		this.add(getLblLicenseServer(), gbc_lblLicenseServer);

		GridBagConstraints gbc_tfLicenseServer = new GridBagConstraints();
		gbc_tfLicenseServer.insets = new Insets(0, 0, 5, 5);
		gbc_tfLicenseServer.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfLicenseServer.gridx = 2;
		gbc_tfLicenseServer.gridy = 2;
		GridBagConstraints gbc_lblIdProvider = new GridBagConstraints();
		gbc_lblIdProvider.anchor = GridBagConstraints.EAST;
		gbc_lblIdProvider.insets = new Insets(0, 0, 5, 5);
		gbc_lblIdProvider.gridx = 1;
		gbc_lblIdProvider.gridy = 3;
//		this.add(getLblIdProvider(), gbc_lblIdProvider);
		GridBagConstraints gbc_tfIdProvider = new GridBagConstraints();
		gbc_tfIdProvider.insets = new Insets(0, 0, 5, 5);
		gbc_tfIdProvider.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfIdProvider.gridx = 2;
		gbc_tfIdProvider.gridy = 3;
//		this.add(getTfIdProvider(), gbc_tfIdProvider);
		GridBagConstraints gbc_lblClientId = new GridBagConstraints();
		gbc_lblClientId.insets = new Insets(0, 0, 5, 5);
		gbc_lblClientId.anchor = GridBagConstraints.EAST;
		gbc_lblClientId.gridx = 1;
		gbc_lblClientId.gridy = 4;
//		this.add(getLblClientId(), gbc_lblClientId);
		GridBagConstraints gbc_tfClientId = new GridBagConstraints();
		gbc_tfClientId.insets = new Insets(0, 0, 5, 5);
		gbc_tfClientId.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfClientId.gridx = 2;
		gbc_tfClientId.gridy = 4;
//		this.add(getTfClientId(), gbc_tfClientId);
		GridBagConstraints gbc_lblClientSecret = new GridBagConstraints();
		gbc_lblClientSecret.insets = new Insets(0, 0, 0, 5);
		gbc_lblClientSecret.anchor = GridBagConstraints.EAST;
		gbc_lblClientSecret.gridx = 1;
		gbc_lblClientSecret.gridy = 5;
//		this.add(getLblClientSecret(), gbc_lblClientSecret);
		GridBagConstraints gbc_tfClientSecret = new GridBagConstraints();
		gbc_tfClientSecret.insets = new Insets(0, 0, 0, 5);
		gbc_tfClientSecret.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfClientSecret.gridx = 2;
		gbc_tfClientSecret.gridy = 5;
		// this.add(getTfLicenseServer(), gbc_tfLicenseServer);
		
		bConnect.setText(Language.translate("Verbinden"));
		GridBagConstraints gbc_btnResult = new GridBagConstraints();
		gbc_btnResult.insets = new Insets(0, 0, 0, 5);
		gbc_btnResult.gridx = 1;
		gbc_btnResult.gridy = 3;
		add(getBtnResult(), gbc_btnResult);

	}

	/**
	 * This method initializes jButtonApply
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBConnect() {
		if (bConnect == null) {
			bConnect = new JButton();
			bConnect.setFont(new Font("Dialog", Font.BOLD, 12));
			bConnect.setActionCommand(COMMAND_CONNECT);
			bConnect.addActionListener(this);
		}
		return bConnect;
	}

	public JTextField getTfUsername() {
		if (tfUsername == null) {
			tfUsername = new JTextField();
			tfUsername.setColumns(10);
		}
		return tfUsername;
	}

	private JLabel getLblUsername() {
		if (lblUsername == null) {
			lblUsername = new JLabel(Language.translate("Benutzername"));
		}
		return lblUsername;
	}

	private JLabel getLblPassword() {
		if (lblPassword == null) {
			lblPassword = new JLabel(Language.translate("Passwort"));
		}
		return lblPassword;
	}

	private JLabel getLblLicenseServer() {
		if (lblLicenseServer == null) {
			lblLicenseServer = new JLabel(Language.translate("Lizenz-Server"));
		}
		return lblLicenseServer;
	}

	private JLabel getLblIdProvider() {
		if (lblIdProvider == null) {
			lblIdProvider = new JLabel(Language.translate("ID-Provider"));
		}
		return lblIdProvider;
	}

	public JTextField getTfPassword() {
		if (tfPassword == null) {
			tfPassword = new JTextField();
//			tfPassword.setText("test");
			tfPassword.setColumns(10);
		}
		return tfPassword;
	}

	private JFormattedTextField getTfLicenseServer() {
		if (tfLicenseServer == null) {
			tfLicenseServer = new JFormattedTextField();
			tfLicenseServer.setText(DEBUG_RESOURCE_URI);
		}
		return tfLicenseServer;
	}

	private JFormattedTextField getTfIdProvider() {
		if (tfIdProvider == null) {
			tfIdProvider = new JFormattedTextField();
			tfIdProvider.setText(DEBUG_ISSUER_URI);
		}
		return tfIdProvider;
	}

	private JTextField getTfClientId() {
		if (tfClientId == null) {
			tfClientId = new JTextField();
			tfClientId.setText(DEBUG_CLIENT_ID);
			tfClientId.setColumns(10);
		}
		return tfClientId;
	}

	private JLabel getLblClientId() {
		if (lblClientId == null) {
			lblClientId = new JLabel(Language.translate("Client-ID"));
		}
		return lblClientId;
	}

	private JTextField getTfClientSecret() {
		if (tfClientSecret == null) {
			tfClientSecret = new JTextField();
			tfClientSecret.setText(DEBUG_CLIENT_SECRET);
			tfClientSecret.setColumns(10);
		}
		return tfClientSecret;
	}

	private JLabel getLblClientSecret() {
		if (lblClientSecret == null) {
			lblClientSecret = new JLabel(Language.translate("Client-Secret"));
		}
		return lblClientSecret;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		String actCMD = ae.getActionCommand();

		if (actCMD.equalsIgnoreCase(COMMAND_CONNECT)) {
			displayResult(owner.connect(getTfUsername().getText(), getTfPassword().getText()));
		} else {
			if (parentGUI != null) {
				parentGUI.actionPerformed(ae);
			}
		}

	}

	private JButton getBtnResult() {
		if (btnResult == null) {
			btnResult = new JButton("result");
//			btnResult.setEnabled(false);
			getBtnResult().setVisible(false);
		}
		return btnResult;
	}

	public void displayResult(boolean successful) {
		getBtnResult().setVisible(true);
		if (successful) {
			getBtnResult().setBackground(new Color(0, 255, 0));
			getBtnResult().setText(Language.translate("Erfolgreich"));
		} else {
			getBtnResult().setBackground(new Color(255, 0, 0));
			getBtnResult().setText(Language.translate("Fehlgeschlagen"));
		}
	}
}
