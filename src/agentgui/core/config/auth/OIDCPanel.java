/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.core.config.auth;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import agentgui.core.application.Language;

/**
 * The Class OIDCPanel.
 */
public class OIDCPanel extends JPanel implements ActionListener {

	/** The Constant DEBUG_ISSUER_URI. */
	public static final String DEBUG_ISSUER_URI = "https://se238124.zim.uni-due.de:8443/auth/realms/EOMID/";
	
	/** The Constant DEBUG_RESOURCE_URI. */
	public static final String DEBUG_RESOURCE_URI = "https://se238124.zim.uni-due.de:18443/vanilla/profile.jsp";
	
	/** The Constant DEBUG_CLIENT_ID. */
	public static final String DEBUG_CLIENT_ID = "testclient";
	
	/** The Constant DEBUG_CLIENT_SECRET. */
	public static final String DEBUG_CLIENT_SECRET = "b3b651a0-66a7-435e-8f1c-b1460bbfe9e0";
	
	/** The Constant COMMAND_CONNECT. */
	private static final String COMMAND_CONNECT = "connectOIDC";

	/** The lbl OIDC values. */
	private JLabel lblOIDCValues;
	
	/** The b connect. */
	private JButton bConnect;

	/** The tf username. */
	private JTextField tfUsername;
	
	/** The lbl username. */
	private JLabel lblUsername;
	
	/** The lbl password. */
	private JLabel lblPassword;
	
	/** The tf password. */
	private JTextField tfPassword;
	
	/** The parent GUI. */
	private ActionListener parentGUI;
	
	/** The owner. */
	private OIDCAuthorization owner;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -169367444435859302L;
	
	/** The btn result. */
	private JButton btnResult;

	/**
	 * Instantiates a new OIDC panel.
	 *
	 * @param owner the owner
	 */
	public OIDCPanel(OIDCAuthorization owner) {
		this();
		this.owner = owner;
	}

	/**
	 * Sets the parent.
	 *
	 * @param parent the parent
	 * @return the OIDC panel
	 */
	public OIDCPanel setParent(ActionListener parent) {
		this.parentGUI = parent;
		return this; // for chaining
	}

	/**
	 * Instantiates a new OIDC panel.
	 */
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
	 * This method initializes jButtonApply.
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

	/**
	 * Gets the tf username.
	 *
	 * @return the tf username
	 */
	public JTextField getTfUsername() {
		if (tfUsername == null) {
			tfUsername = new JTextField();
			tfUsername.setColumns(10);
		}
		return tfUsername;
	}

	/**
	 * Gets the lbl username.
	 *
	 * @return the lbl username
	 */
	private JLabel getLblUsername() {
		if (lblUsername == null) {
			lblUsername = new JLabel(Language.translate("Benutzername"));
		}
		return lblUsername;
	}

	/**
	 * Gets the lbl password.
	 *
	 * @return the lbl password
	 */
	private JLabel getLblPassword() {
		if (lblPassword == null) {
			lblPassword = new JLabel(Language.translate("Passwort"));
		}
		return lblPassword;
	}


	/**
	 * Gets the tf password.
	 *
	 * @return the tf password
	 */
	public JTextField getTfPassword() {
		if (tfPassword == null) {
			tfPassword = new JTextField();
//			tfPassword.setText("test");
			tfPassword.setColumns(10);
		}
		return tfPassword;
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

	/**
	 * Gets the btn result.
	 *
	 * @return the btn result
	 */
	private JButton getBtnResult() {
		if (btnResult == null) {
			btnResult = new JButton("result");
//			btnResult.setEnabled(false);
			getBtnResult().setVisible(false);
		}
		return btnResult;
	}

	/**
	 * Display result.
	 *
	 * @param successful the successful
	 */
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
