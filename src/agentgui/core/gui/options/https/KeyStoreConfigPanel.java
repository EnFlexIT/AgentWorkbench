/**
 * ***************************************************************
 * Agent.GUI is a thiswork to develop Multi-agent based simulation 
 * applications based on the JADE - thiswork in compliance with the 
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
package agentgui.core.gui.options.https;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import agentgui.core.application.Application;
import agentgui.core.application.Language;

/**
 * This JPanel allows the user to : 
 * 1- create new KeyStore and protect its integrity with a password 
 * 2- update his KeyStore informations 
 * 3- generate certificate from the KeyStore
 * @see HttpsConfigWindow
 * 
 * @author Mohamed Amine JEDIDI <mohamedamine_jedidi@outlook.com>
 * @version 1.0
 * @since 05-04-2016
 */

public class KeyStoreConfigPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = -4676410555112580221L;

	private HttpsConfigWindow httpsConfigWindow;
	private KeyStoreController keyStoreController;

	private final Dimension fieldSize = new Dimension(120, 26);

	private JLabel jLabelKeyStoreName;
	private JLabel jLabelPassword;
	private JLabel jLabelConfirmPassword;
	private JLabel jLabelAlias;
	private JLabel jLabelFullName;
	private JLabel jLabelOrganizationalUnit;
	private JLabel jLabelOrganization;
	private JLabel jLabelCity;
	private JLabel jLabelState;
	private JLabel jLabelCountryCode;
	private JLabel jLabelKeyStoreInformations;

	private JTextField jTextFieldKeyStoreName;
	private JTextField jTextFieldAlias;
	private JTextField jTextFieldFullName;
	private JTextField jTextFieldOrganizationalUnit;
	private JTextField jTextFieldOrganization;
	private JTextField jTextFieldCity;
	private JTextField jTextFieldState;
	private JTextField jTextFieldCountryCode;

	private JPasswordField jPasswordFieldPassword;
	private JPasswordField jPasswordFieldConfirmPassword;

	private JButton jButtonCertificate;
	private JButton jButtonApplyKeyStore;

	private JFileChooser jFileChooserOpen;
	private JFileChooser jFileChooserSave;
	
	private final String pathImage = Application.getGlobalInfo().getPathImageIntern();
	private final ImageIcon iconSave = new ImageIcon( this.getClass().getResource( pathImage + "MBsave.png") );

	/**
	 * Create the application.
	 */
	public KeyStoreConfigPanel(HttpsConfigWindow httpsConfigWindow) {
		this.httpsConfigWindow = httpsConfigWindow;
		this.initialize();
	}
	/**
	 * Initialize the contents of the Panel.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0 };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelKeyStoreInformations = new GridBagConstraints();
		gbc_jLabelKeyStoreInformations.anchor = GridBagConstraints.SOUTHWEST;
		gbc_jLabelKeyStoreInformations.insets = new Insets(9, 10, 15, 5);
		gbc_jLabelKeyStoreInformations.gridx = 0;
		gbc_jLabelKeyStoreInformations.gridy = 0;
		add(getJLabelKeyStoreInformations(), gbc_jLabelKeyStoreInformations);
		GridBagConstraints gbc_jButtonCertificate = new GridBagConstraints();
		gbc_jButtonCertificate.insets = new Insets(9, 0, 10, 5);
		gbc_jButtonCertificate.gridx = 1;
		gbc_jButtonCertificate.gridy = 0;
		add(getJButtonCertificate(), gbc_jButtonCertificate);
		GridBagConstraints gbc_jButtonApplyKeyStore = new GridBagConstraints();
		gbc_jButtonApplyKeyStore.insets = new Insets(9, 0, 10, 10);
		gbc_jButtonApplyKeyStore.gridx = 2;
		gbc_jButtonApplyKeyStore.gridy = 0;
		add(getJButtonApplyKeyStore(), gbc_jButtonApplyKeyStore);
		GridBagConstraints gbc_jLabelKeyStoreName = new GridBagConstraints();
		gbc_jLabelKeyStoreName.anchor = GridBagConstraints.WEST;
		gbc_jLabelKeyStoreName.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelKeyStoreName.gridx = 0;
		gbc_jLabelKeyStoreName.gridy = 1;
		add(getJLabelKeyStoreName(), gbc_jLabelKeyStoreName);
		GridBagConstraints gbc_jTextFieldKeyStoreName = new GridBagConstraints();
		gbc_jTextFieldKeyStoreName.gridwidth = 2;
		gbc_jTextFieldKeyStoreName.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldKeyStoreName.insets = new Insets(0, 0, 5, 10);
		gbc_jTextFieldKeyStoreName.gridx = 1;
		gbc_jTextFieldKeyStoreName.gridy = 1;
		add(getJTextFieldKeyStoreName(), gbc_jTextFieldKeyStoreName);
		GridBagConstraints gbc_jLabelPassword = new GridBagConstraints();
		gbc_jLabelPassword.anchor = GridBagConstraints.WEST;
		gbc_jLabelPassword.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelPassword.gridx = 0;
		gbc_jLabelPassword.gridy = 2;
		add(getJLabelPassword(), gbc_jLabelPassword);
		GridBagConstraints gbc_jPasswordField = new GridBagConstraints();
		gbc_jPasswordField.gridwidth = 2;
		gbc_jPasswordField.fill = GridBagConstraints.HORIZONTAL;
		gbc_jPasswordField.insets = new Insets(0, 0, 5, 10);
		gbc_jPasswordField.gridx = 1;
		gbc_jPasswordField.gridy = 2;
		add(getJPasswordField(), gbc_jPasswordField);
		GridBagConstraints gbc_jLabelConfirmPassword = new GridBagConstraints();
		gbc_jLabelConfirmPassword.anchor = GridBagConstraints.WEST;
		gbc_jLabelConfirmPassword.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelConfirmPassword.gridx = 0;
		gbc_jLabelConfirmPassword.gridy = 3;
		add(getJLabelConfirmPassword(), gbc_jLabelConfirmPassword);
		GridBagConstraints gbc_jPasswordConfirmPassword = new GridBagConstraints();
		gbc_jPasswordConfirmPassword.gridwidth = 2;
		gbc_jPasswordConfirmPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_jPasswordConfirmPassword.insets = new Insets(0, 0, 5, 10);
		gbc_jPasswordConfirmPassword.gridx = 1;
		gbc_jPasswordConfirmPassword.gridy = 3;
		add(getJPasswordConfirmPassword(), gbc_jPasswordConfirmPassword);
		GridBagConstraints gbc_jLabelAlias = new GridBagConstraints();
		gbc_jLabelAlias.anchor = GridBagConstraints.WEST;
		gbc_jLabelAlias.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelAlias.gridx = 0;
		gbc_jLabelAlias.gridy = 4;
		add(getJLabelAlias(), gbc_jLabelAlias);
		GridBagConstraints gbc_jTextFieldAlias = new GridBagConstraints();
		gbc_jTextFieldAlias.gridwidth = 2;
		gbc_jTextFieldAlias.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldAlias.insets = new Insets(0, 0, 5, 10);
		gbc_jTextFieldAlias.gridx = 1;
		gbc_jTextFieldAlias.gridy = 4;
		add(getJTextFieldAlias(), gbc_jTextFieldAlias);
		GridBagConstraints gbc_jLabelFullName = new GridBagConstraints();
		gbc_jLabelFullName.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabelFullName.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelFullName.gridx = 0;
		gbc_jLabelFullName.gridy = 5;
		add(getJLabelFullName(), gbc_jLabelFullName);
		GridBagConstraints gbc_jTextFieldFullName = new GridBagConstraints();
		gbc_jTextFieldFullName.gridwidth = 2;
		gbc_jTextFieldFullName.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldFullName.insets = new Insets(0, 0, 5, 10);
		gbc_jTextFieldFullName.gridx = 1;
		gbc_jTextFieldFullName.gridy = 5;
		add(getJTextFieldFullName(), gbc_jTextFieldFullName);
		GridBagConstraints gbc_jLabelOrganization = new GridBagConstraints();
		gbc_jLabelOrganization.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabelOrganization.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelOrganization.gridx = 0;
		gbc_jLabelOrganization.gridy = 6;
		add(getJLabelOrganization(), gbc_jLabelOrganization);
		GridBagConstraints gbc_jTextFieldOrganization = new GridBagConstraints();
		gbc_jTextFieldOrganization.gridwidth = 2;
		gbc_jTextFieldOrganization.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldOrganization.insets = new Insets(0, 0, 5, 10);
		gbc_jTextFieldOrganization.gridx = 1;
		gbc_jTextFieldOrganization.gridy = 6;
		add(getJTextFieldOrganization(), gbc_jTextFieldOrganization);
		GridBagConstraints gbc_jLabelOrganizationalUnit = new GridBagConstraints();
		gbc_jLabelOrganizationalUnit.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabelOrganizationalUnit.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelOrganizationalUnit.gridx = 0;
		gbc_jLabelOrganizationalUnit.gridy = 7;
		add(getJLabelOrganizationalUnit(), gbc_jLabelOrganizationalUnit);
		GridBagConstraints gbc_jTextFieldOrganizationalUnit = new GridBagConstraints();
		gbc_jTextFieldOrganizationalUnit.gridwidth = 2;
		gbc_jTextFieldOrganizationalUnit.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldOrganizationalUnit.insets = new Insets(0, 0, 5, 10);
		gbc_jTextFieldOrganizationalUnit.gridx = 1;
		gbc_jTextFieldOrganizationalUnit.gridy = 7;
		add(getJTextFieldOrganizationalUnit(), gbc_jTextFieldOrganizationalUnit);
		GridBagConstraints gbc_jLabelCity = new GridBagConstraints();
		gbc_jLabelCity.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabelCity.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelCity.gridx = 0;
		gbc_jLabelCity.gridy = 8;
		add(getJLabelCity(), gbc_jLabelCity);
		GridBagConstraints gbc_jTextFieldCity = new GridBagConstraints();
		gbc_jTextFieldCity.gridwidth = 2;
		gbc_jTextFieldCity.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldCity.insets = new Insets(0, 0, 5, 10);
		gbc_jTextFieldCity.gridx = 1;
		gbc_jTextFieldCity.gridy = 8;
		add(getJTextFieldCity(), gbc_jTextFieldCity);
		GridBagConstraints gbc_jLabelState = new GridBagConstraints();
		gbc_jLabelState.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabelState.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelState.gridx = 0;
		gbc_jLabelState.gridy = 9;
		add(getJLabelState(), gbc_jLabelState);
		GridBagConstraints gbc_jTextFieldState = new GridBagConstraints();
		gbc_jTextFieldState.gridwidth = 2;
		gbc_jTextFieldState.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldState.insets = new Insets(0, 0, 5, 10);
		gbc_jTextFieldState.gridx = 1;
		gbc_jTextFieldState.gridy = 9;
		add(getJTextFieldState(), gbc_jTextFieldState);
		GridBagConstraints gbc_jLabelCountryCode = new GridBagConstraints();
		gbc_jLabelCountryCode.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabelCountryCode.insets = new Insets(0, 10, 0, 5);
		gbc_jLabelCountryCode.gridx = 0;
		gbc_jLabelCountryCode.gridy = 10;
		add(getJLabelCountryCode(), gbc_jLabelCountryCode);
		GridBagConstraints gbc_jTextFieldCountryCode = new GridBagConstraints();
		gbc_jTextFieldCountryCode.gridwidth = 2;
		gbc_jTextFieldCountryCode.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldCountryCode.insets = new Insets(0, 0, 0, 10);
		gbc_jTextFieldCountryCode.gridx = 1;
		gbc_jTextFieldCountryCode.gridy = 10;
		add(getJTextFieldCountryCode(), gbc_jTextFieldCountryCode);
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
	}
	/**
	 * This method initializes KeyStoreController.
	 */
	protected KeyStoreController getKeyStoreController() {
		if (keyStoreController == null) {
			keyStoreController = new KeyStoreController();
		}
		return keyStoreController;
	}
	/**
	 * This method initializes jLabelKeyStoreInformations.
	 */
	private JLabel getJLabelKeyStoreInformations() {
		if (jLabelKeyStoreInformations == null) {
			jLabelKeyStoreInformations = new JLabel(Language.translate("Keystore Information",Language.EN));
			jLabelKeyStoreInformations.setFont(new Font("Dialog", Font.BOLD, 11));
		}
		return jLabelKeyStoreInformations;
	}
	/**
	 * This method initializes jLabelKeyStoreName.
	 */
	private JLabel getJLabelKeyStoreName() {
		if (jLabelKeyStoreName == null) {
			jLabelKeyStoreName = new JLabel(Language.translate("KeyStore Name", Language.EN));
			jLabelKeyStoreName.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return jLabelKeyStoreName;
	}
	/**
	 * This method initializes jLabelPassword.
	 */
	private JLabel getJLabelPassword() {
		if (jLabelPassword == null) {
			jLabelPassword = new JLabel(Language.translate("Password",Language.EN));
			jLabelPassword.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return jLabelPassword;
	}
	/**
	 * This method initializes jLabelConfirmPassword.
	 */
	private JLabel getJLabelConfirmPassword() {
		if (jLabelConfirmPassword == null) {
			jLabelConfirmPassword = new JLabel(Language.translate("Confirm Password",Language.EN));
			jLabelConfirmPassword.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return jLabelConfirmPassword;
	}
	/**
	 * This method initializes jLabelAlias.
	 */
	private JLabel getJLabelAlias() {
		if (jLabelAlias == null) {
			jLabelAlias = new JLabel(Language.translate("Alias",Language.EN));
			jLabelAlias.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return jLabelAlias;
	}
	/**
	 * This method initializes jLabelFullName.
	 */
	private JLabel getJLabelFullName() {
		if (jLabelFullName == null) {
			jLabelFullName = new JLabel(Language.translate("Full Name",Language.EN));
			jLabelFullName.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return jLabelFullName;
	}
	/**
	 * This method initializes jLabelOrganizationalUnit.
	 */
	private JLabel getJLabelOrganizationalUnit() {
		if (jLabelOrganizationalUnit == null) {
			jLabelOrganizationalUnit = new JLabel(Language.translate("Organizational Unit",Language.EN));
			jLabelOrganizationalUnit.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return jLabelOrganizationalUnit;
	}
	/**
	 * This method initializes jLabelOrganization.
	 */
	private JLabel getJLabelOrganization() {
		if (jLabelOrganization == null) {
			jLabelOrganization = new JLabel(Language.translate("Organization",Language.EN));
			jLabelOrganization.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return jLabelOrganization;
	}
	/**
	 * This method initializes jLabelCity.
	 */
	private JLabel getJLabelCity() {
		if (jLabelCity == null) {
			jLabelCity = new JLabel(Language.translate("City or Locality",Language.EN));
			jLabelCity.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return jLabelCity;
	}
	/**
	 * This method initializes jLabelState.
	 */
	private JLabel getJLabelState() {
		if (jLabelState == null) {
			jLabelState = new JLabel(Language.translate("State or Province",Language.EN));
			jLabelState.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return jLabelState;
	}
	/**
	 * This method initializes jLabelCountryCode.
	 */
	private JLabel getJLabelCountryCode() {
		if (jLabelCountryCode == null) {
			jLabelCountryCode = new JLabel(Language.translate("Country Code",Language.EN));
			jLabelCountryCode.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return jLabelCountryCode;
	}
	/**
	 * This method initializes jTextFieldKeyStoreName.
	 */
	protected JTextField getJTextFieldKeyStoreName() {
		if (jTextFieldKeyStoreName == null) {
			jTextFieldKeyStoreName = new JTextField();
			jTextFieldKeyStoreName.setEnabled(false);
			jTextFieldKeyStoreName.setPreferredSize(this.fieldSize);
		}
		return jTextFieldKeyStoreName;
	}
	/**
	 * This method initializes jTextFieldAlias.
	 */
	protected JTextField getJTextFieldAlias() {
		if (jTextFieldAlias == null) {
			jTextFieldAlias = new JTextField();
			jTextFieldAlias.setEnabled(false);
			jTextFieldAlias.setPreferredSize(this.fieldSize);
		}
		return jTextFieldAlias;
	}
	/**
	 * This method initializes jTextFieldFullName.
	 */
	protected JTextField getJTextFieldFullName() {
		if (jTextFieldFullName == null) {
			jTextFieldFullName = new JTextField();
			jTextFieldFullName.setEnabled(false);
			jTextFieldFullName.setPreferredSize(this.fieldSize);
		}
		return jTextFieldFullName;
	}
	/**
	 * This method initializes jTextFieldOrganizationalUnit.
	 */
	protected JTextField getJTextFieldOrganizationalUnit() {
		if (jTextFieldOrganizationalUnit == null) {
			jTextFieldOrganizationalUnit = new JTextField();
			jTextFieldOrganizationalUnit.setEnabled(false);
			jTextFieldOrganizationalUnit.setPreferredSize(this.fieldSize);
		}
		return jTextFieldOrganizationalUnit;
	}
	/**
	 * This method initializes jTextFieldOrganization.
	 */
	protected JTextField getJTextFieldOrganization() {
		if (jTextFieldOrganization == null) {
			jTextFieldOrganization = new JTextField();
			jTextFieldOrganization.setEnabled(false);
			jTextFieldOrganization.setPreferredSize(this.fieldSize);
		}
		return jTextFieldOrganization;
	}
	/**
	 * This method initializes jTextFieldCity.
	 */
	protected JTextField getJTextFieldCity() {
		if (jTextFieldCity == null) {
			jTextFieldCity = new JTextField();
			jTextFieldCity.setEnabled(false);
			jTextFieldCity.setPreferredSize(this.fieldSize);
		}
		return jTextFieldCity;
	}
	/**
	 * This method initializes jTextFieldState.
	 */
	protected JTextField getJTextFieldState() {
		if (jTextFieldState == null) {
			jTextFieldState = new JTextField();
			jTextFieldState.setEnabled(false);
			jTextFieldState.setPreferredSize(this.fieldSize);
		}
		return jTextFieldState;
	}
	/**
	 * This method initializes jTextFieldCountryCode.
	 */
	protected JTextField getJTextFieldCountryCode() {
		if (jTextFieldCountryCode == null) {
			jTextFieldCountryCode = new JTextField();
			jTextFieldCountryCode.setEnabled(false);
			jTextFieldCountryCode.setPreferredSize(this.fieldSize);
		}
		return jTextFieldCountryCode;
	}
	/**
	 * This method initializes jPasswordField.
	 */
	protected JPasswordField getJPasswordField() {
		if (jPasswordFieldPassword == null) {
			jPasswordFieldPassword = new JPasswordField();
			jPasswordFieldPassword.setEnabled(false);
			jPasswordFieldPassword.setPreferredSize(this.fieldSize);
		}
		return jPasswordFieldPassword;
	}
	/**
	 * This method initializes jPasswordConfirmPassword.
	 */
	protected JPasswordField getJPasswordConfirmPassword() {
		if (jPasswordFieldConfirmPassword == null) {
			jPasswordFieldConfirmPassword = new JPasswordField();
			jPasswordFieldConfirmPassword.setEnabled(false);
			jPasswordFieldConfirmPassword.setPreferredSize(this.fieldSize);
		}
		return jPasswordFieldConfirmPassword;
	}
	/**
	 * This method initializes jButtonApplyKeyStore.
	 */
	private JButton getJButtonApplyKeyStore() {
		if (jButtonApplyKeyStore == null) {
			jButtonApplyKeyStore = new JButton("");
			jButtonApplyKeyStore.setToolTipText(Language.translate("Save",Language.EN));
			jButtonApplyKeyStore.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonApplyKeyStore.setForeground(new Color(0, 0, 255));
			jButtonApplyKeyStore.setVerticalAlignment(SwingConstants.TOP);
			jButtonApplyKeyStore.setPreferredSize(new Dimension(26, 26));
			jButtonApplyKeyStore.setIcon(iconSave);
			jButtonApplyKeyStore.addActionListener(this);
		}
		return jButtonApplyKeyStore;
	}
	/**
	 * This method initializes jButtonCertificate.
	 */
	private JButton getJButtonCertificate() {
		if (jButtonCertificate == null) {
			jButtonCertificate = new JButton(Language.translate("Export Certificate",Language.EN));
			jButtonCertificate.setFont(new Font("Dialog", Font.BOLD, 11));
			jButtonCertificate.setPreferredSize(new Dimension(127, 26));
			jButtonCertificate.setToolTipText(Language.translate("Generate your own certificate from the KeyStore",Language.EN));
			jButtonCertificate.addActionListener(this);
		}
		return jButtonCertificate;
	}
	/**
	 * This method initializes jFileChooserOpen.
	 */
	protected JFileChooser getJFileChooserOpen() {
		if (jFileChooserOpen == null) {
			jFileChooserOpen = new JFileChooser();
			jFileChooserOpen.setDialogType(JFileChooser.SAVE_DIALOG);
			jFileChooserOpen.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
		return jFileChooserOpen;
	}
	/**
	 * This method initializes jFileChooserSave.
	 */
	protected JFileChooser getJFileChooserSave() {
		if (jFileChooserSave == null) {
			jFileChooserSave = new JFileChooser();
			jFileChooserSave.setDialogType(JFileChooser.SAVE_DIALOG);
		}
		return jFileChooserSave;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		// ---- Save changes in case of creating of editing KeyStore ------
		if (ae.getSource() == this.getJButtonApplyKeyStore()) {
			// ----- Create KeyStore --------------------------------------
			if (this.httpsConfigWindow.getKeyStoreButtonPressed() == "CreateKeyStore") {
				// ---- Verify if all the fields are filled ---------------
				if (jTextFieldKeyStoreName.getText().equals("") || jTextFieldAlias.getText().equals("")
						|| jTextFieldCity.getText().equals("") || jTextFieldCountryCode.getText().equals("")
						|| jTextFieldFullName.getText().equals("") || jTextFieldOrganization.getText().equals("")
						|| jTextFieldOrganizationalUnit.getText().equals("") || jTextFieldState.getText().equals("")
						|| jPasswordFieldPassword.getPassword().equals("")
						|| jPasswordFieldConfirmPassword.getPassword().equals("")) {
					String msg = Language.translate("You must fill out all required fields!",Language.EN);
					String title = Language.translate("Required fields",Language.EN);
					JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
				}
				/*
				 * ----- Verify if The password and its confirm -----------
				 * ---------------- are the same --------------------------
				 */
				else if (!Arrays.equals(jPasswordFieldPassword.getPassword(),jPasswordFieldConfirmPassword.getPassword())) {
					String msg = Language.translate("The password and its confirm are not the same!",Language.EN);
					String title = Language.translate("Warning",Language.EN);
					JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
				}
				/*
				 * ----- Verify if the password is at least ---------------
				 * ------------- 6 characters in length -------------------
				 */
				else if (jPasswordFieldPassword.getPassword().length < 6) {
					String msg = Language.translate("The password should be at least 6 characters in length!",Language.EN);
					String title = Language.translate("Password length",Language.EN);
					JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
				} else {
					// ---- Get Provider Informations ---------------------
					String informations = "CN=" + jTextFieldFullName.getText() + ",OU="
							+ jTextFieldOrganizationalUnit.getText() + ",O=" + jTextFieldOrganization.getText() + ",L="
							+ jTextFieldCity.getText() + ",S=" + jTextFieldState.getText() + ",C="
							+ jTextFieldCountryCode.getText();
					String alias = jTextFieldAlias.getText();
					String keystore_name = jTextFieldKeyStoreName.getText();
					String keystore_password = new String(jPasswordFieldPassword.getPassword());

					// ----- Open jFileChooser to select the directory ---- 
					getJFileChooserOpen();
					int jfile = jFileChooserOpen.showSaveDialog(null);
					if (jfile == JFileChooser.APPROVE_OPTION) {
						// ---- Get The selected directory path ------------
						String KeyStorePath = jFileChooserOpen.getSelectedFile().getAbsoluteFile().getAbsolutePath() + "\\";
						/* ---- Create the KeyStore with informations ------
						 * ---------------- entered by the User ------------
						 */
						getKeyStoreController().CreateKeyStore(informations, alias, keystore_name, keystore_password,
								KeyStorePath);
						String msg = Language.translate("Your keystore has been created successfully!",Language.EN);
						String title = Language.translate("KeyStore created",Language.EN);
						JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);
						
						this.httpsConfigWindow.setKeyStorefilepath(KeyStorePath + keystore_name + "KeyStore.jks");
						this.httpsConfigWindow.setKeyStorePassword(keystore_password);
						this.httpsConfigWindow.setKeyStoreAlias(alias);
						// ---- Get the Content of the keyStore ---------------
						String result = getKeyStoreController().ListKeyStoreContent(this.httpsConfigWindow.getKeyStorefilepath(),this.httpsConfigWindow.getKeyStorePassword());
						// ---- Substring informations from the result --------
						String FullName = result.substring(result.indexOf("CN=") + 3, result.indexOf(",OU"));
						String OrganizationalUnit = result.substring(result.indexOf("OU=") + 3, result.indexOf(",O="));
						String Organization = result.substring(result.indexOf("O=") + 2, result.indexOf(",L"));
						String Locality = result.substring(result.indexOf("L=") + 2, result.indexOf(",ST"));
						String State = result.substring(result.indexOf("ST=") + 3, result.indexOf(",C"));
						String CountryCode = result.substring(result.indexOf("C=") + 2, result.indexOf("C=") + 4);
						// ----- Fill in fields with KeyStore informations -----
						this.httpsConfigWindow.setFieldsEnabledFalse(jTextFieldCity);
						this.httpsConfigWindow.setFieldsEnabledFalse(jTextFieldCity);
						this.httpsConfigWindow.setFieldsEnabledFalse(jTextFieldCountryCode);
						this.httpsConfigWindow.setFieldsEnabledFalse(jTextFieldFullName);
						this.httpsConfigWindow.setFieldsEnabledFalse(jTextFieldOrganization);
						this.httpsConfigWindow.setFieldsEnabledFalse(jTextFieldOrganizationalUnit);
						this.httpsConfigWindow.setFieldsEnabledFalse(jTextFieldState);
						this.httpsConfigWindow.setFieldsEnabledFalse(jTextFieldKeyStoreName);
						jTextFieldCity.setText(Locality);
						jTextFieldCountryCode.setText(CountryCode);
						jTextFieldFullName.setText(FullName);
						jTextFieldOrganization.setText(Organization);
						jTextFieldOrganizationalUnit.setText(OrganizationalUnit);
						jTextFieldState.setText(State);
						jTextFieldKeyStoreName.setText(this.httpsConfigWindow.getKeyStorefilepath().substring(this.httpsConfigWindow.getKeyStorefilepath().lastIndexOf("\\") + 1));
						jTextFieldAlias.setText(this.httpsConfigWindow.getKeyStoreAlias());
						jPasswordFieldPassword.setText(keystore_password);
						jPasswordFieldConfirmPassword.setText(null);
						this.httpsConfigWindow.getJLabelKeyStoreLocationPath().setText(KeyStorePath + "\\" + keystore_name + "KeyStore.jks");
						this.httpsConfigWindow.setKeyStoreButtonPressed("UpdateKeyStore");
					}
				}
			} else if (this.httpsConfigWindow.getKeyStoreButtonPressed() == "UpdateKeyStore") {
				// ---- Update KeyStore ---------------------------
				// ---- Verify if all the fields are filled -------
				if (jTextFieldAlias.getText().equals("") || jPasswordFieldPassword.getPassword().equals("")
						|| jPasswordFieldConfirmPassword.getPassword().equals("")) {
					String msg = Language.translate("You must fill out all required fields!",Language.EN);
					String title = Language.translate("Required fields",Language.EN);
					JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
				}
				/*
				 * ----- Verify if The password and its confirm ----
				 * ---------------- are the same -------------------
				 */
				else if (!Arrays.equals(jPasswordFieldPassword.getPassword(),jPasswordFieldConfirmPassword.getPassword())) {
					String msg = Language.translate("The password and its confirm are not the same!",Language.EN);
					String title = Language.translate("Warning",Language.EN);
					JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
				}
				/*
				 * ----- Verify if the password is at least -------- 
				 * ----------- 6 characters in length --------------
				 */
				else if (jPasswordFieldPassword.getPassword().length < 6) {
					String msg = Language.translate("The password should be at least 6 characters in length!",Language.EN);
					String title = Language.translate("Password length",Language.EN);
					JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
				} else {
					
					String newAlias = jTextFieldAlias.getText();
					String newKeyStorePassword = new String(jPasswordFieldPassword.getPassword());
					String keyStoreName = this.httpsConfigWindow.getKeyStorefilepath().substring(this.httpsConfigWindow.getKeyStorefilepath().lastIndexOf("\\") + 1);
					// --- Create JOptionPane to enter the old KeyStore password ---
					JPanel jPanelPassword = new JPanel();
					String msg1 = Language.translate("Please, enter the old password for  ",Language.EN);
					JLabel jLabelEnterPassword = new JLabel( msg1 + keyStoreName + "  :");
					jLabelEnterPassword.setFont(new Font("Dialog", Font.BOLD, 11));
					JPasswordField jPasswordField = new JPasswordField(10);
					jPanelPassword.add(jLabelEnterPassword);
					jPanelPassword.add(jPasswordField);
					String[] options = new String[] { "OK", "Cancel" };
					String title = Language.translate("Password",Language.EN);
					int option = JOptionPane.showOptionDialog(null, jPanelPassword, title, JOptionPane.NO_OPTION,JOptionPane.PLAIN_MESSAGE, null, options, jPasswordField);
					String oldPassword = new String(jPasswordField.getPassword());
					if (option == 0) {
						// ------------ Press OK -------------------------------
						if (this.httpsConfigWindow.getKeyStorePassword().equals(oldPassword)){
							// ---- Edit the KeyStore Alias and Password ---
							getKeyStoreController().EditKeyStore(this.httpsConfigWindow.getKeyStorefilepath(),this.httpsConfigWindow.getKeyStoreAlias(), newAlias, newKeyStorePassword,
									this.httpsConfigWindow.getKeyStorePassword());
							String msg = Language.translate("Your keystore has been updated successfully!",Language.EN);
							String title1 = Language.translate("KeyStore updated",Language.EN);
							JOptionPane.showMessageDialog(this, msg, title1, JOptionPane.INFORMATION_MESSAGE); 
							this.httpsConfigWindow.setKeyStorePassword(newKeyStorePassword);
							this.httpsConfigWindow.setKeyStoreAlias(newAlias);
							jPasswordFieldPassword.setText(newKeyStorePassword);
							jPasswordFieldConfirmPassword.setText(null);
							Dialog ownerDialog = Application.getGlobalInfo().getOwnerDialogForComponent(this);
							this.httpsConfigWindow.setKeyStoreAlias(getKeyStoreController().GetKeyStoreAlias(this.httpsConfigWindow.getKeyStorefilepath(), newKeyStorePassword, ownerDialog));
						}else{
							String msg = Language.translate("The password you entered is incorrect. Please try again!",Language.EN);
							String title2 = Language.translate("Password incorrect",Language.EN);
							JOptionPane.showMessageDialog(this, msg, title2, JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			}
			
		}else if (ae.getSource() == this.getJButtonCertificate()) {
			// ---- Export the certificate ---------------------------
			// ----- Verify if there is a KeyStore opened ------------
			if (this.httpsConfigWindow.getKeyStorefilepath() == null) {
				String msg = Language.translate("Please open a Keystore first!",Language.EN);
				String title = Language.translate("Warning message",Language.EN);
				JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
			} else {
				/*
				 * ----- Generate a JFileChooser to enter -------
				 * -------------- Certificate name ---------------
				 */
				getJFileChooserSave();
				jFileChooserSave = new JFileChooser();
				int jfile = jFileChooserSave.showSaveDialog(null);
				if (jfile == JFileChooser.APPROVE_OPTION) {
					// ------- Get Certificate Path --------------------
					String certificatePath = jFileChooserSave.getSelectedFile().getAbsoluteFile().getAbsolutePath();
					// ------- Get KeySTore information ----------------
					Dialog ownerDialog = Application.getGlobalInfo().getOwnerDialogForComponent(this);
					String Alias = getKeyStoreController().GetKeyStoreAlias(this.httpsConfigWindow.getKeyStorefilepath(),this.httpsConfigWindow.getKeyStorePassword(), ownerDialog);
					// ------- Generate the Certificate -----------------
					getKeyStoreController().ExportCertificate(this.httpsConfigWindow.getKeyStorefilepath(),Alias,this.httpsConfigWindow.getKeyStorePassword(), certificatePath);
					String msg = Language.translate("Your certificate has been created successfully!",Language.EN);
					String title = Language.translate("Certificate generated",Language.EN);
					JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);
			}
		}
		}
	}
}
