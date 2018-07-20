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
package agentgui.core.gui.options.https;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import de.enflexit.common.crypto.CertificateProperties;
import de.enflexit.common.crypto.KeyStoreController;

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

	private JPanel jPanelCertificate;
	
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
	private JLabel jLabelCertificatePath;
	private JLabel jLabelCertificateInformation;
	private JLabel jLabelValidity;
	private JLabel jLabelDays;
	
	private JTextField jTextFieldKeyStoreName;
	private JTextField jTextFieldAlias;
	private JTextField jTextFieldFullName;
	private JTextField jTextFieldOrganizationalUnit;
	private JTextField jTextFieldOrganization;
	private JTextField jTextFieldCity;
	private JTextField jTextFieldState;
	private JTextField jTextFieldCountryCode;
	private JTextField jTextFieldCertificateValidity;
	private JTextField jTextFieldCertificateName;
	private JTextField jTextFieldCertificatePath;
	private JTextField jTextFieldValidity;

	private JPasswordField jPasswordFieldPassword;
	private JPasswordField jPasswordFieldConfirmPassword;

	private JButton jButtonCertificate;
	private JButton jButtonApplyKeyStore;
	private JButton jButtonCertificatePath;

	private JFileChooser jFileChooserOpen;
	private JFileChooser jFileChooserSave;
	
	private final Dimension fieldSize = new Dimension(120, 26);
	private Set<String> ISO_COUNTRIES = new HashSet<String>(Arrays.asList(Locale.getISOCountries()));

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
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0 };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,Double.MIN_VALUE };
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelKeyStoreInformations = new GridBagConstraints();
		gbc_jLabelKeyStoreInformations.anchor = GridBagConstraints.SOUTHWEST;
		gbc_jLabelKeyStoreInformations.insets = new Insets(9, 10, 15, 5);
		gbc_jLabelKeyStoreInformations.gridx = 0;
		gbc_jLabelKeyStoreInformations.gridy = 0;
		add(getJLabelKeyStoreInformations(), gbc_jLabelKeyStoreInformations);
		GridBagConstraints gbc_jButtonCertificate = new GridBagConstraints();
		gbc_jButtonCertificate.anchor = GridBagConstraints.EAST;
		gbc_jButtonCertificate.insets = new Insets(9, 0, 10, 5);
		gbc_jButtonCertificate.gridx = 1;
		gbc_jButtonCertificate.gridy = 0;
		add(getJButtonCertificate(), gbc_jButtonCertificate);
		GridBagConstraints gbc_jButtonApplyKeyStore = new GridBagConstraints();
		gbc_jButtonApplyKeyStore.fill = GridBagConstraints.VERTICAL;
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
		gbc_jLabelCountryCode.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelCountryCode.gridx = 0;
		gbc_jLabelCountryCode.gridy = 10;
		add(getJLabelCountryCode(), gbc_jLabelCountryCode);
		GridBagConstraints gbc_jTextFieldCountryCode = new GridBagConstraints();
		gbc_jTextFieldCountryCode.gridwidth = 2;
		gbc_jTextFieldCountryCode.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldCountryCode.insets = new Insets(0, 0, 5, 10);
		gbc_jTextFieldCountryCode.gridx = 1;
		gbc_jTextFieldCountryCode.gridy = 10;
		add(getJTextFieldCountryCode(), gbc_jTextFieldCountryCode);
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GridBagConstraints gbc_jLabelValidity = new GridBagConstraints();
		gbc_jLabelValidity.fill = GridBagConstraints.HORIZONTAL;
		gbc_jLabelValidity.insets = new Insets(0, 10, 0, 5);
		gbc_jLabelValidity.gridx = 0;
		gbc_jLabelValidity.gridy = 11;
		add(getJLabelValidity(), gbc_jLabelValidity);
		GridBagConstraints gbc_jTextFieldValidity = new GridBagConstraints();
		gbc_jTextFieldValidity.insets = new Insets(0, 0, 0, 5);
		gbc_jTextFieldValidity.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldValidity.gridx = 1;
		gbc_jTextFieldValidity.gridy = 11;
		add(getJTextFieldValidity(), gbc_jTextFieldValidity);
		GridBagConstraints gbc_jLabelDays = new GridBagConstraints();
		gbc_jLabelDays.insets = new Insets(0, 0, 0, 10);
		gbc_jLabelDays.gridx = 2;
		gbc_jLabelDays.gridy = 11;
		add(getJLabelDays(), gbc_jLabelDays);
		
		getKeyStoreController(); // init the controller
	}
	/**
	 * Create the JPanel.
	 * @return the jPanelCertificate
	 */
	public JPanel getJPanelCertificate() {
		if (jPanelCertificate == null) {
			jPanelCertificate = new JPanel();
			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
			gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
			gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
			gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
			jPanelCertificate.setLayout(gridBagLayout);
			GridBagConstraints gbc_jLabelCertificateInformation = new GridBagConstraints();
			gbc_jLabelCertificateInformation.anchor = GridBagConstraints.WEST;
			gbc_jLabelCertificateInformation.gridwidth = 2;
			gbc_jLabelCertificateInformation.insets = new Insets(0, 5, 10, 5);
			gbc_jLabelCertificateInformation.gridx = 0;
			gbc_jLabelCertificateInformation.gridy = 0;
			jPanelCertificate.add(getJLabelCertificateInformation(), gbc_jLabelCertificateInformation);
			GridBagConstraints gbc_jLabelCertificateName = new GridBagConstraints();
			gbc_jLabelCertificateName.insets = new Insets(0, 5, 5, 5);
			gbc_jLabelCertificateName.anchor = GridBagConstraints.WEST;
			gbc_jLabelCertificateName.gridx = 0;
			gbc_jLabelCertificateName.gridy = 1;
//			jPanelCertificate.add(getJLabelCertificateName(), gbc_jLabelCertificateName);
			GridBagConstraints gbc_jLabelCertificateValidity = new GridBagConstraints();
			gbc_jLabelCertificateValidity.anchor = GridBagConstraints.WEST;
			gbc_jLabelCertificateValidity.insets = new Insets(0, 5, 5, 5);
			gbc_jLabelCertificateValidity.gridx = 0;
			gbc_jLabelCertificateValidity.gridy = 2;
//			jPanelCertificate.add(getJLabelCertificateValidity(), gbc_jLabelCertificateValidity);
			GridBagConstraints gbc_jLabelCertificateValidityDays = new GridBagConstraints();
			gbc_jLabelCertificateValidityDays.anchor = GridBagConstraints.WEST;
			gbc_jLabelCertificateValidityDays.insets = new Insets(0, 5, 5, 5);
			gbc_jLabelCertificateValidityDays.gridx = 2;
			gbc_jLabelCertificateValidityDays.gridy = 2;
//			jPanelCertificate.add(getJLabelCertificateValidityDays(), gbc_jLabelCertificateValidityDays);
			GridBagConstraints gbc_jLabelCertificatePath = new GridBagConstraints();
			gbc_jLabelCertificatePath.anchor = GridBagConstraints.WEST;
			gbc_jLabelCertificatePath.insets = new Insets(0, 5, 0, 0);
			gbc_jLabelCertificatePath.gridx = 0;
			gbc_jLabelCertificatePath.gridy = 3;
			jPanelCertificate.add(getJLabelCertificatePath(), gbc_jLabelCertificatePath);
			GridBagConstraints gbc_jTextFieldCertificateName = new GridBagConstraints();
			gbc_jTextFieldCertificateName.gridwidth = 2;
			gbc_jTextFieldCertificateName.insets = new Insets(0, 0, 5, 0);
			gbc_jTextFieldCertificateName.fill = GridBagConstraints.HORIZONTAL;
			gbc_jTextFieldCertificateName.gridx = 1;
			gbc_jTextFieldCertificateName.gridy = 1;
//			jPanelCertificate.add(getJTextFieldCertificateName(), gbc_jTextFieldCertificateName);
			GridBagConstraints gbc_jTextFieldCertificateValidity = new GridBagConstraints();
			gbc_jTextFieldCertificateValidity.gridwidth = 1;
			gbc_jTextFieldCertificateValidity.insets = new Insets(0, 0, 5, 0);
			gbc_jTextFieldCertificateValidity.fill = GridBagConstraints.HORIZONTAL;
			gbc_jTextFieldCertificateValidity.gridx = 1;
			gbc_jTextFieldCertificateValidity.gridy = 2;
//			jPanelCertificate.add(getJTextFieldCertificateValidity(), gbc_jTextFieldCertificateValidity);
			GridBagConstraints gbc_jTextFieldCertificatePath = new GridBagConstraints();
			gbc_jTextFieldCertificatePath.insets = new Insets(0, 0, 0, 0);
			gbc_jTextFieldCertificatePath.fill = GridBagConstraints.HORIZONTAL;
			gbc_jTextFieldCertificatePath.gridx = 1;
			gbc_jTextFieldCertificatePath.gridy = 3;
			jPanelCertificate.add(getJTextFieldCertificatePath(), gbc_jTextFieldCertificatePath);
			GridBagConstraints gbc_jButtonCertificatePath = new GridBagConstraints();
			gbc_jButtonCertificatePath.gridx = 2;
			gbc_jButtonCertificatePath.gridy = 3;
			jPanelCertificate.add(getButtonCertificatePath(), gbc_jButtonCertificatePath);
		}
		return jPanelCertificate;
	}
	/**
	 * This method initializes KeyStoreController.
	 */
	protected KeyStoreController getKeyStoreController() {
		if (keyStoreController == null) {
			keyStoreController = new KeyStoreController(this.httpsConfigWindow);
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
	 * This method initializes jLabelValidity.
	 */
	private JLabel getJLabelValidity() {
		if (jLabelValidity == null) {
			jLabelValidity = new JLabel(Language.translate("Validity",Language.EN));
			jLabelValidity.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return jLabelValidity;
	}
	/**
	 * This method initializes jTextFieldValidity.
	 */
	protected JTextField getJTextFieldValidity() {
		if (jTextFieldValidity == null) {
			jTextFieldValidity = new JTextField();
			jTextFieldValidity.setEnabled(false);
			jTextFieldValidity.setPreferredSize(this.fieldSize);
		}
		return jTextFieldValidity;
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
			jButtonApplyKeyStore.setPreferredSize(new Dimension(26, 26));
			jButtonApplyKeyStore.setIcon(GlobalInfo.getInternalImageIcon("MBsave.png"));
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
	/**
	 * This method initializes jTextFieldCertificateName.
	 */
	private JTextField getJTextFieldCertificateName() {
		if (jTextFieldCertificateName == null) {
			jTextFieldCertificateName = new JTextField();
			jTextFieldCertificateName.setPreferredSize(new Dimension(300, 26));
		}
		return jTextFieldCertificateName;
	}
	/**
	 * This method initializes jTextFieldCertificateValidity.
	 */
	private JTextField getJTextFieldCertificateValidity() {
		if (jTextFieldCertificateValidity == null) {
			jTextFieldCertificateValidity = new JTextField();
			jTextFieldCertificateValidity.setPreferredSize(new Dimension(300, 26));
		}
		return jTextFieldCertificateValidity;
	}
	/**
	 * This method initializes jTextFieldCertificatePath.
	 */
	public JTextField getJTextFieldCertificatePath() {
		if (jTextFieldCertificatePath == null) {
			jTextFieldCertificatePath = new JTextField();
			jTextFieldCertificatePath.setPreferredSize(new Dimension(300, 26));
		}
		return jTextFieldCertificatePath;
	}
	/**
	 * This method initializes jLabelCertificatePath.
	 */
	private JLabel getJLabelCertificatePath() {
		if (jLabelCertificatePath == null) {
			jLabelCertificatePath = new JLabel("Certificate path:");
			jLabelCertificatePath.setFont(new Font("Dialog", Font.PLAIN, 11));
			
		}
		return jLabelCertificatePath;
	}
	/**
	 * This method initializes jButtonCertificatePath.
	 */
	private JButton getButtonCertificatePath() {
		if (jButtonCertificatePath == null) {
			jButtonCertificatePath = new JButton("");
			jButtonCertificatePath.setPreferredSize(new Dimension(26, 26));
			jButtonCertificatePath.setIcon(GlobalInfo.getInternalImageIcon("MBopen.png"));
			jButtonCertificatePath.addActionListener(this);
		}
		return jButtonCertificatePath;
	}
	/**
	 * This method initializes jLabelCertificateInformation.
	 */
	private JLabel getJLabelCertificateInformation() {
		if (jLabelCertificateInformation == null) {
			jLabelCertificateInformation = new JLabel(Language.translate("Certifcate Information",Language.EN));
			jLabelCertificateInformation.setFont(new Font("Dialog", Font.BOLD, 11));
		}
		return jLabelCertificateInformation;
	}
	/**
	 * This method initializes jLabelDays.
	 */
	private JLabel getJLabelDays() {
		if (jLabelDays == null) {
			jLabelDays = new JLabel(Language.translate("day(s)",Language.EN));
			jLabelDays.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return jLabelDays;
	}
	/**
	 * Checks if is numeric.
	 * @param input the input
	 * @return true, if is numeric
	 */
	public boolean isNumeric(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
    /**
     * Checks if is valid ISO country code.
     *
     * @param s the s
     * @return true, if is valid ISO country code
     */
    public boolean isValidISOCountry(String s) {
        return ISO_COUNTRIES.contains(s);
    }
    
    public void fillFields(CertificateProperties keyStoreSettings){
		// ----- Fill in fields with KeyStore informations -------------------------
		this.getJTextFieldCity().setEnabled(false);
		this.getJTextFieldCountryCode().setEnabled(false);
		this.getJTextFieldFullName().setEnabled(false);
		this.getJTextFieldOrganization().setEnabled(false);
		this.getJTextFieldOrganizationalUnit().setEnabled(false);
		this.getJTextFieldState().setEnabled(false);
		this.getJTextFieldKeyStoreName().setEnabled(false);
		this.getJTextFieldValidity().setEnabled(false);
		this.getJTextFieldCity().setText(keyStoreSettings.getCityOrLocality());
		this.getJTextFieldCountryCode().setText(keyStoreSettings.getCountryCode());
		this.getJTextFieldFullName().setText(keyStoreSettings.getCommonName());
		this.getJTextFieldOrganization().setText(keyStoreSettings.getOrganization());
		this.getJTextFieldOrganizationalUnit().setText(keyStoreSettings.getOrganizationalUnit());
		this.getJTextFieldState().setText(keyStoreSettings.getStateOrProvince());
		this.getJTextFieldValidity().setText(keyStoreSettings.getValidity());
		this.getJTextFieldKeyStoreName().setText(this.httpsConfigWindow.getKeyStoreFile().getName());
		this.getJTextFieldAlias().setText(this.httpsConfigWindow.getKeyAlias());
		this.getJPasswordField().setText(getKeyStoreController().getTrustStorePassword());
		this.getJPasswordConfirmPassword().setText(null);
		
		this.getJTextFieldAlias().setEnabled(true);
		this.getJPasswordField().setEnabled(true);
		this.getJPasswordConfirmPassword().setEnabled(true);
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
				if (this.getJTextFieldKeyStoreName().getText().isEmpty() || this.getJTextFieldAlias().getText().isEmpty() || this.getJTextFieldCity().getText().isEmpty() || this.getJTextFieldCountryCode().getText().isEmpty()
						|| this.getJTextFieldFullName().getText().isEmpty() || this.getJTextFieldOrganization().getText().isEmpty() || this.getJTextFieldOrganizationalUnit().getText().isEmpty() || this.getJTextFieldState().getText().isEmpty()
						|| this.getJTextFieldValidity().getText().isEmpty() || this.getJPasswordField().getPassword().length==0 || this.getJPasswordConfirmPassword().getPassword().length==0) {
					String msg = Language.translate("You must fill out all required fields!",Language.EN);
					String title = Language.translate("Required fields",Language.EN);
					JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
				}
				/* --------------------------------------------------------
				 * ----- Verify if The password and its confirm -----------
				 * ---------------- are the same --------------------------
				 */
				else if (!Arrays.equals(this.getJPasswordField().getPassword(),this.getJPasswordConfirmPassword().getPassword())) {
					String msg = Language.translate("The password and its confirm are not the same!",Language.EN);
					String title = Language.translate("Warning",Language.EN);
					JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
				}
				/* --------------------------------------------------------
				 * ----- Verify if the password is at least ---------------
				 * ------------- 6 characters in length -------------------
				 */
				else if (this.getJPasswordField().getPassword().length < 6) {
					String msg = Language.translate("The password should be at least 6 characters in length!",Language.EN);
					String title = Language.translate("Password length",Language.EN);
					JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
				} 
				/* -------------------------------------------------------
				 * ----- Verify if the Country code is valid -------------
				 * ------------------ ISO code ---------------------------
				 */
				else if (isValidISOCountry(this.getJTextFieldCountryCode().getText())==false) {
					String msg = Language.translate("You must use a valid country code!",Language.EN);
					String title = Language.translate("Country code",Language.EN);
					JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
				}
				/* -------------------------------------------------------
				 * ----- Verify if the Validity value is number ----------
				 * -------------------------------------------------------
				 */
				else if ( isNumeric(getJTextFieldValidity().getText()) == false){
						String msg = Language.translate("Please, enter number of days in validity field!",Language.EN);
						String title = Language.translate("Numeric value",Language.EN);
						JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
				}else {
					// ---- Get Provider Informations ---------------------
//					String informations = "CN=" + this.getJTextFieldFullName().getText() + ",OU=" + this.getJTextFieldOrganizationalUnit().getText() + ",O=" + this.getJTextFieldOrganization().getText() + ",L=" + this.getJTextFieldCity().getText() + ",S=" + this.getJTextFieldState().getText() + ",C=" + this.getJTextFieldCountryCode().getText();
					CertificateProperties certificateProperties = new CertificateProperties();
					certificateProperties.setCommonName(this.getJTextFieldFullName().getText());
					certificateProperties.setOrganizationalUnit(this.getJTextFieldOrganizationalUnit().getText());
					certificateProperties.setOrganization(this.getJTextFieldOrganization().getText());
					certificateProperties.setCityOrLocality(this.getJTextFieldCity().getText());
					certificateProperties.setStateOrProvince(this.getJTextFieldState().getText());
					certificateProperties.setCountryCode(this.getJTextFieldCountryCode().getText());
					certificateProperties.setAlias(this.getJTextFieldAlias().getText());

//					String alias = this.getJTextFieldAlias().getText();
					String keystoreName = this.getJTextFieldKeyStoreName().getText();
					String keystorePassword = new String(this.getJPasswordField().getPassword());
					String keyStoreValidity = this.getJTextFieldValidity().getText();
					// ----- Open jFileChooser to select the directory ---- 
					getJFileChooserOpen();
					int jfile = jFileChooserOpen.showSaveDialog(null);
					if (jfile == JFileChooser.APPROVE_OPTION) {
						// ---- Get The selected directory path ------------------------------------
						File keyStoreFile = new File(jFileChooserOpen.getSelectedFile().getAbsoluteFile().getAbsolutePath() + File.separator +keystoreName + HttpsConfigWindow.KEYSTORE_FILENAME);

						// ---- Create the KeyStore with informations  entered by the User ---------
						getKeyStoreController().createKeyStore(certificateProperties, keyStoreFile, keystorePassword, keyStoreValidity);
						String msg = Language.translate("Your keystore has been created successfully!",Language.EN);
						String title = Language.translate("KeyStore created",Language.EN);
						JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);
						
						this.httpsConfigWindow.setKeyStoreFile(keyStoreFile);
						this.httpsConfigWindow.setKeyStorePassword(keystorePassword);
						this.httpsConfigWindow.setKeyAlias(this.getJTextFieldAlias().getText());
						// ---- Get the Content of the keyStore ------------------------------------
						getKeyStoreController().openTrustStore(this.httpsConfigWindow.getKeyStoreFile(), keystorePassword);
						CertificateProperties keyStoreSettings = getKeyStoreController().getFirstCertificateProperties();
						
						fillFields(keyStoreSettings);

						this.httpsConfigWindow.getJTextFieldKeyStoreLocationPath().setText(keyStoreFile.getAbsolutePath());
						this.httpsConfigWindow.setKeyStoreButtonPressed("UpdateKeyStore");
					}
				}
			} else if (this.httpsConfigWindow.getKeyStoreButtonPressed() == "UpdateKeyStore") {
				// ---- Update KeyStore ---------------------------
				// ---- Verify if all the fields are filled -------
				if (this.getJTextFieldAlias().getText().equals("") || this.getJPasswordField().getPassword().length==0 || this.getJPasswordConfirmPassword().getPassword().length==0) {
					String msg = Language.translate("You must fill out all required fields!",Language.EN);
					String title = Language.translate("Required fields",Language.EN);
					JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
				}
				/* -------------------------------------------------
				 * ----- Verify if The password and its confirm ----
				 * ---------------- are the same -------------------
				 */
				else if (!Arrays.equals(this.getJPasswordField().getPassword(),this.getJPasswordConfirmPassword().getPassword())) {
					String msg = Language.translate("The password and its confirm are not the same!",Language.EN);
					String title = Language.translate("Warning",Language.EN);
					JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
				}
				/* -------------------------------------------------
				 * ----- Verify if the password is at least -------- 
				 * ----------- 6 characters in length --------------
				 */
				else if (this.getJPasswordField().getPassword().length < 6) {
					String msg = Language.translate("The password should be at least 6 characters in length!",Language.EN);
					String title = Language.translate("Password length",Language.EN);
					JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
				} else {
					String newKeyAlias = this.getJTextFieldAlias().getText();
					String newKeyStorePassword = new String(this.getJPasswordField().getPassword());
					String keyStoreName = this.httpsConfigWindow.getKeyStoreFile().getName();
					
					// --- Create JOptionPane to enter the old KeyStore password ---
					JPanel jPanelPassword = new JPanel();
					String msg = Language.translate("Please, enter the old password for  ",Language.EN);
					JLabel jLabelEnterPassword = new JLabel( msg + keyStoreName + "  :");
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
							getKeyStoreController().editLonelyKeyEntry(this.httpsConfigWindow.getKeyAlias(), newKeyAlias, newKeyStorePassword, this.httpsConfigWindow.getKeyStorePassword());
							String msg1 = Language.translate("Your keystore has been updated successfully!",Language.EN);
							String title1 = Language.translate("KeyStore updated",Language.EN);
							JOptionPane.showMessageDialog(this, msg1, title1, JOptionPane.INFORMATION_MESSAGE); 
							this.httpsConfigWindow.setKeyStorePassword(newKeyStorePassword);
							this.httpsConfigWindow.setKeyAlias(newKeyAlias);
							this.getJPasswordField().setText(newKeyStorePassword);
							this.getJPasswordConfirmPassword().setText(null);
							this.httpsConfigWindow.setKeyAlias(getKeyStoreController().getFirstCertificateProperties().getAlias());
						}else{
							String msg2 = Language.translate("The password you entered is incorrect. Please try again!",Language.EN);
							String title2 = Language.translate("Password incorrect",Language.EN);
							JOptionPane.showMessageDialog(this, msg2, title2, JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			}
			
		}else if (ae.getSource() == this.getJButtonCertificate()) {
			// ---- Export the certificate ---------------------------
			// ----- Verify if there is a KeyStore opened ------------
			if (this.httpsConfigWindow.getKeyStoreFile() == null) {
				String msg = Language.translate("Please open a Keystore first!",Language.EN);
				String title = Language.translate("Warning message",Language.EN);
				JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
			} else {
				String[] options = new String[] { "OK", "Cancel" };
				String title = Language.translate("Export Certificate",Language.EN);
				int option = JOptionPane.showOptionDialog(null, getJPanelCertificate(), title, JOptionPane.NO_OPTION,JOptionPane.PLAIN_MESSAGE, null, options, jTextFieldCertificateName);
				if (option == 0) {
					if (this.httpsConfigWindow.getKeyStoreFile() == null) {
						String msg = Language.translate("Please open a Keystore first!",Language.EN);
						String title1 = Language.translate("Warning message",Language.EN);
						JOptionPane.showMessageDialog(this, msg, title1, JOptionPane.WARNING_MESSAGE);
//					} else if ( isNumeric(getJTextFieldCertificateValidity().getText()) == false){
//						String msg = Language.translate("Please, enter number of days in validity field!",Language.EN);
//						String title2 = Language.translate("Numeric value",Language.EN);
//						JOptionPane.showMessageDialog(this, msg, title2, JOptionPane.WARNING_MESSAGE);
//						getJTextFieldCertificateValidity().setText(null);
//						getJTextFieldCertificateName().setText(null);
//						getJTextFieldCertificatePath().setText(null);
					} else {
						// ------- Get Certificate Path --------------------
						String certificatePath = getJTextFieldCertificatePath().getText() + File.separator +getJTextFieldCertificateName().getText();
//						String validity = getJTextFieldCertificateValidity().getText();
						// ------- Get KeySTore information ----------------
						String alias = getKeyStoreController().getFirstCertificateProperties().getAlias();
						// ------- Generate the Certificate -----------------
						getKeyStoreController().exportCertificate(alias, certificatePath);
						String msg = Language.translate("Your certificate has been created successfully!",Language.EN);
						String title1 = Language.translate("Certificate generated",Language.EN);
						JOptionPane.showMessageDialog(this, msg, title1, JOptionPane.INFORMATION_MESSAGE);
						getJTextFieldCertificateValidity().setText(null);
						getJTextFieldCertificateName().setText(null);
						getJTextFieldCertificatePath().setText(null);
					}
				}
			}
		} else if (ae.getSource() == this.getButtonCertificatePath()) {
			// --- Open a JFileChooser to select the certificate -------
			this.getJFileChooserOpen();
			int result = this.getJFileChooserOpen().showOpenDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				// ---- Get the certificate's path ---------------------
				String certificatePath = this.getJFileChooserOpen().getSelectedFile().getPath();
				getJTextFieldCertificatePath().setText(certificatePath);
			}
		}
	}
}
