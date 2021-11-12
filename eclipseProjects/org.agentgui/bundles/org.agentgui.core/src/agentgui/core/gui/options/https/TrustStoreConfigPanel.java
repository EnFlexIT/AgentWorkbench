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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import de.enflexit.common.crypto.CertificateProperties;
import de.enflexit.common.crypto.TrustStoreController;

/**
 * This JPanel allows the user to : 
 * 1- create new TrustStore and protect its integrity with a password 
 * 2- update his TrustStore informations 
 * 3- add or delete certificates from TrustStore
 * @see HttpsConfigWindow
 * 
 * @author Mohamed Amine JEDIDI <mohamedamine_jedidi@outlook.com>
 * @version 1.0
 * @since 05-04-2016
 */

public class TrustStoreConfigPanel extends JPanel implements ActionListener,MouseListener {

	private static final long serialVersionUID = -4676410555112580221L;

	private HttpsConfigWindow httpsConfigWindow;
	private TrustStoreController trustStoreController;
	private JPanel jPanelButtons;
	private JPanel jPanelCertificateInfo;

	private JLabel jLabelTrustStoreInformations;
	private JLabel jLabelTruststoreName;
	private JLabel jLabelTrustStorePassword;
	private JLabel jLabelTrustStoreConfirmPassword;
	private JLabel jLabelCertificatesList;
	private JLabel jLabelCertificateAlias;
	private JLabel jLabelCertificateValidity;
	private JLabel jLabelOwnerInformations;
	private JLabel jLabelCertificateInformations;
	private JLabel jLabelFullname;
	private JLabel jLabelOrganization;
	private JLabel jLabelOrginazationalUnit;
	private JLabel jLabelCity;
	private JLabel jLabelStateOrProvince;
	private JLabel jLabelCountryCode;
	private JLabel jLabelAlias;
	private JLabel jLabelValidity;
	private JLabel jLabelName;
	private JLabel jLabelOrg;
	private JLabel jLabelOrgUnit;
	private JLabel jLabelCityOrLocality;
	private JLabel jLabelState;
	private JLabel jLabelCountry;
	
	private JTextField jTextFieldTrustStoreName;
	private JPasswordField jPasswordFieldPassword;
	private JPasswordField jPasswordFieldConfirmPassword;

	private JButton jButtonAddCertificate;
	private JButton jButtonRemoveCertificate;
	private JButton jButtonApplyTrustStore;
	
	private JScrollPane jscrollPane;
	private JTable jTableTrusTedCertificates;
	
	private JFileChooser jFileChooser;
	private JFileChooser jFileChooserFile;

	private final Dimension fieldSize = new Dimension(120, 26);

	/**
	 * Create the application.
	 */
	public TrustStoreConfigPanel(HttpsConfigWindow httpsConfigWindow) {
		this.httpsConfigWindow = httpsConfigWindow;
		this.initialize();
	}
	/**
	 * Initialize the contents of the Panel.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0 };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelTrustStoreInformations = new GridBagConstraints();
		gbc_jLabelTrustStoreInformations.fill = GridBagConstraints.VERTICAL;
		gbc_jLabelTrustStoreInformations.anchor = GridBagConstraints.WEST;
		gbc_jLabelTrustStoreInformations.insets = new Insets(15, 10, 15, 5);
		gbc_jLabelTrustStoreInformations.gridx = 0;
		gbc_jLabelTrustStoreInformations.gridy = 0;
		add(getJLabelTrustStoreInformations(), gbc_jLabelTrustStoreInformations);
		GridBagConstraints gbc_ButtonApplyTrustStore = new GridBagConstraints();
		gbc_ButtonApplyTrustStore.anchor = GridBagConstraints.EAST;
		gbc_ButtonApplyTrustStore.insets = new Insets(5, 0, 5, 10);
		gbc_ButtonApplyTrustStore.gridx = 2;
		gbc_ButtonApplyTrustStore.gridy = 0;
		add(getJButtonApplyTrustStore(), gbc_ButtonApplyTrustStore);
		GridBagConstraints gbc_jLabelTruststoreName = new GridBagConstraints();
		gbc_jLabelTruststoreName.anchor = GridBagConstraints.WEST;
		gbc_jLabelTruststoreName.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelTruststoreName.gridx = 0;
		gbc_jLabelTruststoreName.gridy = 1;
		add(getJLabelTruststoreName(), gbc_jLabelTruststoreName);
		GridBagConstraints gbc_jLabelTrustStorePassword = new GridBagConstraints();
		gbc_jLabelTrustStorePassword.anchor = GridBagConstraints.WEST;
		gbc_jLabelTrustStorePassword.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelTrustStorePassword.gridx = 0;
		gbc_jLabelTrustStorePassword.gridy = 2;
		add(getJLabelTrustStorePassword(), gbc_jLabelTrustStorePassword);
		GridBagConstraints gbc_jLabelTrustStoreConfirmPassword = new GridBagConstraints();
		gbc_jLabelTrustStoreConfirmPassword.anchor = GridBagConstraints.WEST;
		gbc_jLabelTrustStoreConfirmPassword.insets = new Insets(0, 10, 5, 5);
		gbc_jLabelTrustStoreConfirmPassword.gridx = 0;
		gbc_jLabelTrustStoreConfirmPassword.gridy = 3;
		add(getJLabelTrustStoreConfirmPassword(), gbc_jLabelTrustStoreConfirmPassword);
		GridBagConstraints gbc_jTextFieldTrustStoreName = new GridBagConstraints();
		gbc_jTextFieldTrustStoreName.gridwidth = 2;
		gbc_jTextFieldTrustStoreName.fill = GridBagConstraints.HORIZONTAL;
		gbc_jTextFieldTrustStoreName.insets = new Insets(0, 0, 5, 10);
		gbc_jTextFieldTrustStoreName.gridx = 1;
		gbc_jTextFieldTrustStoreName.gridy = 1;
		add(getJTextFieldTrustStoreName(), gbc_jTextFieldTrustStoreName);
		GridBagConstraints gbc_jPasswordFieldTrustStorePassword = new GridBagConstraints();
		gbc_jPasswordFieldTrustStorePassword.gridwidth = 2;
		gbc_jPasswordFieldTrustStorePassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_jPasswordFieldTrustStorePassword.insets = new Insets(0, 0, 5, 10);
		gbc_jPasswordFieldTrustStorePassword.gridx = 1;
		gbc_jPasswordFieldTrustStorePassword.gridy = 2;
		add(getJPasswordFieldPassword(), gbc_jPasswordFieldTrustStorePassword);
		GridBagConstraints gbc_jPasswordFieldTrustStoreConfirmPassword = new GridBagConstraints();
		gbc_jPasswordFieldTrustStoreConfirmPassword.gridwidth = 2;
		gbc_jPasswordFieldTrustStoreConfirmPassword.fill = GridBagConstraints.HORIZONTAL;
		gbc_jPasswordFieldTrustStoreConfirmPassword.insets = new Insets(0, 0, 5, 10);
		gbc_jPasswordFieldTrustStoreConfirmPassword.gridx = 1;
		gbc_jPasswordFieldTrustStoreConfirmPassword.gridy = 3;
		add(getJPasswordFieldConfirmPassword(), gbc_jPasswordFieldTrustStoreConfirmPassword);
		GridBagConstraints gbc_LabelCertificatesList = new GridBagConstraints();
		gbc_LabelCertificatesList.anchor = GridBagConstraints.WEST;
		gbc_LabelCertificatesList.insets = new Insets(5, 10, 5, 5);
		gbc_LabelCertificatesList.gridx = 0;
		gbc_LabelCertificatesList.gridy = 4;
		add(getJLabelCertificatesList(), gbc_LabelCertificatesList);
		GridBagConstraints gbc_jPanelButtons = new GridBagConstraints();
		gbc_jPanelButtons.anchor = GridBagConstraints.WEST;
		gbc_jPanelButtons.insets = new Insets(0, 0, 5, 5);
		gbc_jPanelButtons.fill = GridBagConstraints.VERTICAL;
		gbc_jPanelButtons.gridx = 1;
		gbc_jPanelButtons.gridy = 4;
		add(getJPanelButtons(), gbc_jPanelButtons);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 3;
		gbc_scrollPane.insets = new Insets(0, 10, 10, 10);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 5;
		add(getScrollPane(), gbc_scrollPane);
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
	}
	/**
	 * This method initializes jPanelButtons.
	 */
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			GridBagLayout gbl_jPanelButtons = new GridBagLayout();
			gbl_jPanelButtons.columnWidths = new int[] { 0, 0, 0 };
			gbl_jPanelButtons.rowHeights = new int[] { 32, 0 };
			gbl_jPanelButtons.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
			gbl_jPanelButtons.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
			jPanelButtons.setLayout(gbl_jPanelButtons);
			GridBagConstraints gbc_jButtonAddCertificate = new GridBagConstraints();
			gbc_jButtonAddCertificate.insets = new Insets(0, 0, 0, 5);
			gbc_jButtonAddCertificate.gridx = 0;
			gbc_jButtonAddCertificate.gridy = 0;
			jPanelButtons.add(getJButtonAddCertificate(), gbc_jButtonAddCertificate);
			GridBagConstraints gbc_jButtonRemoveCertificate = new GridBagConstraints();
			gbc_jButtonRemoveCertificate.gridx = 1;
			gbc_jButtonRemoveCertificate.gridy = 0;
			jPanelButtons.add(getJButtonRemoveCertificate(), gbc_jButtonRemoveCertificate);
		}
		return jPanelButtons;
	}
	
	private JPanel getJPanelCertificateInfo(){
		if (jPanelCertificateInfo == null){
			jPanelCertificateInfo = new JPanel();
			
			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWidths = new int[]{0, 0, 0};
			gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
			gridBagLayout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			jPanelCertificateInfo.setLayout(gridBagLayout);
			GridBagConstraints gbc_jLabelCertificateInformations = new GridBagConstraints();
			gbc_jLabelCertificateInformations.anchor = GridBagConstraints.WEST;
			gbc_jLabelCertificateInformations.gridwidth = 2;
			gbc_jLabelCertificateInformations.insets = new Insets(0, 5, 5, 0);
			gbc_jLabelCertificateInformations.gridx = 0;
			gbc_jLabelCertificateInformations.gridy = 0;
			jPanelCertificateInfo.add(getJLabelCertificateInformations(), gbc_jLabelCertificateInformations);
			GridBagConstraints gbc_jLabelCertificateAlias = new GridBagConstraints();
			gbc_jLabelCertificateAlias.anchor = GridBagConstraints.WEST;
			gbc_jLabelCertificateAlias.insets = new Insets(0, 5, 5, 5);
			gbc_jLabelCertificateAlias.gridx = 0;
			gbc_jLabelCertificateAlias.gridy = 1;
			jPanelCertificateInfo.add(getJLabelCertificateAlias(), gbc_jLabelCertificateAlias);
			GridBagConstraints gbc_jLabelAlias = new GridBagConstraints();
			gbc_jLabelAlias.anchor = GridBagConstraints.WEST;
			gbc_jLabelAlias.insets = new Insets(0, 0, 5, 0);
			gbc_jLabelAlias.gridx = 1;
			gbc_jLabelAlias.gridy = 1;
			jPanelCertificateInfo.add(getJLabelAlias(), gbc_jLabelAlias);
			GridBagConstraints gbc_jLabelCertificateValidity = new GridBagConstraints();
			gbc_jLabelCertificateValidity.anchor = GridBagConstraints.WEST;
			gbc_jLabelCertificateValidity.insets = new Insets(0, 5, 5, 5);
			gbc_jLabelCertificateValidity.gridx = 0;
			gbc_jLabelCertificateValidity.gridy = 2;
			jPanelCertificateInfo.add(getJLabelCertificateExpirationdDate(), gbc_jLabelCertificateValidity);
			GridBagConstraints gbc_jLabelValidity = new GridBagConstraints();
			gbc_jLabelValidity.anchor = GridBagConstraints.WEST;
			gbc_jLabelValidity.insets = new Insets(0, 0, 5, 0);
			gbc_jLabelValidity.gridx = 1;
			gbc_jLabelValidity.gridy = 2;
			jPanelCertificateInfo.add(getJLabelValidity(), gbc_jLabelValidity);
			GridBagConstraints gbc_jLabelOwnerInformations = new GridBagConstraints();
			gbc_jLabelOwnerInformations.gridwidth = 2;
			gbc_jLabelOwnerInformations.insets = new Insets(10, 5, 5, 0);
			gbc_jLabelOwnerInformations.anchor = GridBagConstraints.WEST;
			gbc_jLabelOwnerInformations.gridx = 0;
			gbc_jLabelOwnerInformations.gridy = 3;
			jPanelCertificateInfo.add(getJLabelOwnerInformations(), gbc_jLabelOwnerInformations);
			GridBagConstraints gbc_jLabelFullname = new GridBagConstraints();
			gbc_jLabelFullname.insets = new Insets(0, 5, 5, 5);
			gbc_jLabelFullname.anchor = GridBagConstraints.WEST;
			gbc_jLabelFullname.gridx = 0;
			gbc_jLabelFullname.gridy = 4;
			jPanelCertificateInfo.add(getJLabelFullname(), gbc_jLabelFullname);
			GridBagConstraints gbc_jLabelName = new GridBagConstraints();
			gbc_jLabelName.anchor = GridBagConstraints.WEST;
			gbc_jLabelName.insets = new Insets(0, 0, 5, 0);
			gbc_jLabelName.gridx = 1;
			gbc_jLabelName.gridy = 4;
			jPanelCertificateInfo.add(getJLabelName(), gbc_jLabelName);
			GridBagConstraints gbc_jLabelOrganization = new GridBagConstraints();
			gbc_jLabelOrganization.anchor = GridBagConstraints.WEST;
			gbc_jLabelOrganization.insets = new Insets(0, 5, 5, 5);
			gbc_jLabelOrganization.gridx = 0;
			gbc_jLabelOrganization.gridy = 5;
			jPanelCertificateInfo.add(getJLabelOrganization(), gbc_jLabelOrganization);
			GridBagConstraints gbc_jLabelOrg = new GridBagConstraints();
			gbc_jLabelOrg.anchor = GridBagConstraints.WEST;
			gbc_jLabelOrg.insets = new Insets(0, 0, 5, 0);
			gbc_jLabelOrg.gridx = 1;
			gbc_jLabelOrg.gridy = 5;
			jPanelCertificateInfo.add(getJLabelOrg(), gbc_jLabelOrg);
			GridBagConstraints gbc_jLabelOrginazationalUnit = new GridBagConstraints();
			gbc_jLabelOrginazationalUnit.insets = new Insets(0, 5, 5, 5);
			gbc_jLabelOrginazationalUnit.anchor = GridBagConstraints.WEST;
			gbc_jLabelOrginazationalUnit.gridx = 0;
			gbc_jLabelOrginazationalUnit.gridy = 6;
			jPanelCertificateInfo.add(getJLabelOrginazationalUnit(), gbc_jLabelOrginazationalUnit);
			GridBagConstraints gbc_jLabelOrgUnit = new GridBagConstraints();
			gbc_jLabelOrgUnit.anchor = GridBagConstraints.WEST;
			gbc_jLabelOrgUnit.insets = new Insets(0, 0, 5, 0);
			gbc_jLabelOrgUnit.gridx = 1;
			gbc_jLabelOrgUnit.gridy = 6;
			jPanelCertificateInfo.add(getJLabelOrgUnit(), gbc_jLabelOrgUnit);
			GridBagConstraints gbc_jLabelCity = new GridBagConstraints();
			gbc_jLabelCity.insets = new Insets(0, 5, 5, 5);
			gbc_jLabelCity.anchor = GridBagConstraints.WEST;
			gbc_jLabelCity.gridx = 0;
			gbc_jLabelCity.gridy = 7;
			jPanelCertificateInfo.add(getJLabelCity(), gbc_jLabelCity);
			GridBagConstraints gbc_jLabelCityOrLocality = new GridBagConstraints();
			gbc_jLabelCityOrLocality.anchor = GridBagConstraints.WEST;
			gbc_jLabelCityOrLocality.insets = new Insets(0, 0, 5, 0);
			gbc_jLabelCityOrLocality.gridx = 1;
			gbc_jLabelCityOrLocality.gridy = 7;
			jPanelCertificateInfo.add(getJLabelCityOrLocality(), gbc_jLabelCityOrLocality);
			GridBagConstraints gbc_jLabelStateOrProvince = new GridBagConstraints();
			gbc_jLabelStateOrProvince.anchor = GridBagConstraints.WEST;
			gbc_jLabelStateOrProvince.insets = new Insets(0, 5, 5, 5);
			gbc_jLabelStateOrProvince.gridx = 0;
			gbc_jLabelStateOrProvince.gridy = 8;
			jPanelCertificateInfo.add(getJLabelStateOrProvince(), gbc_jLabelStateOrProvince);
			GridBagConstraints gbc_jLabelState = new GridBagConstraints();
			gbc_jLabelState.anchor = GridBagConstraints.WEST;
			gbc_jLabelState.insets = new Insets(0, 0, 5, 0);
			gbc_jLabelState.gridx = 1;
			gbc_jLabelState.gridy = 8;
			jPanelCertificateInfo.add(getJLabelState(), gbc_jLabelState);
			GridBagConstraints gbc_jLabelCountryCode = new GridBagConstraints();
			gbc_jLabelCountryCode.insets = new Insets(0, 5, 0, 5);
			gbc_jLabelCountryCode.anchor = GridBagConstraints.WEST;
			gbc_jLabelCountryCode.gridx = 0;
			gbc_jLabelCountryCode.gridy = 9;
			jPanelCertificateInfo.add(getJLabelCountryCode(), gbc_jLabelCountryCode);
			GridBagConstraints gbc_jLabelCountry = new GridBagConstraints();
			gbc_jLabelCountry.anchor = GridBagConstraints.WEST;
			gbc_jLabelCountry.gridx = 1;
			gbc_jLabelCountry.gridy = 9;
			jPanelCertificateInfo.add(getJLabelCountry(), gbc_jLabelCountry);
		}
		return jPanelCertificateInfo;
	}
	
	/**
	 * This method initializes jLabelCertificateAlias.
	 */
	private JLabel getJLabelCertificateAlias() {
		if (jLabelCertificateAlias == null) {
			jLabelCertificateAlias = new JLabel("Alias:");
			jLabelCertificateAlias.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return jLabelCertificateAlias;
	}
	/**
	 * This method initializes jLabelCertificateValidity.
	 */
	private JLabel getJLabelCertificateExpirationdDate() {
		if (jLabelCertificateValidity == null) {
			jLabelCertificateValidity = new JLabel("Expiration date:");
			jLabelCertificateValidity.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return jLabelCertificateValidity;
	}
	/**
	 * This method initializes jLabelOwnerInformations.
	 */
	private JLabel getJLabelOwnerInformations() {
		if (jLabelOwnerInformations == null) {
			jLabelOwnerInformations = new JLabel("Owner Information");
			jLabelOwnerInformations.setFont(new Font("Dialog", Font.BOLD, 11));
		}
		return jLabelOwnerInformations;
	}
	/**
	 * This method initializes jLabelCertificateInformations.
	 */
	private JLabel getJLabelCertificateInformations() {
		if (jLabelCertificateInformations == null) {
			jLabelCertificateInformations = new JLabel("Certificate Informations");
			jLabelCertificateInformations.setFont(new Font("Dialog", Font.BOLD, 11));
		}
		return jLabelCertificateInformations;
	}
	/**
	 * This method initializes jLabelFullname.
	 */
	private JLabel getJLabelFullname() {
		if (jLabelFullname == null) {
			jLabelFullname = new JLabel("Full Name:");
			jLabelFullname.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return jLabelFullname;
	}
	/**
	 * This method initializes jLabelOrganization.
	 */
	private JLabel getJLabelOrganization() {
		if (jLabelOrganization == null) {
			jLabelOrganization = new JLabel("Organization:");
			jLabelOrganization.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return jLabelOrganization;
	}
	/**
	 * This method initializes jLabelOrginazationalUnit.
	 */
	private JLabel getJLabelOrginazationalUnit() {
		if (jLabelOrginazationalUnit == null) {
			jLabelOrginazationalUnit = new JLabel("Orginazational Unit:");
			jLabelOrginazationalUnit.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return jLabelOrginazationalUnit;
	}
	/**
	 * This method initializes jLabelCity.
	 */
	private JLabel getJLabelCity() {
		if (jLabelCity == null) {
			jLabelCity = new JLabel("City Or Locality:");
			jLabelCity.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return jLabelCity;
	}
	/**
	 * This method initializes jLabelStateOrProvince.
	 */
	private JLabel getJLabelStateOrProvince() {
		if (jLabelStateOrProvince == null) {
			jLabelStateOrProvince = new JLabel("State Or Province:");
			jLabelStateOrProvince.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return jLabelStateOrProvince;
	}
	/**
	 * This method initializes jLabelCountryCode.
	 */
	private JLabel getJLabelCountryCode() {
		if (jLabelCountryCode == null) {
			jLabelCountryCode = new JLabel(Language.translate("Country Code:","EN"));
			jLabelCountryCode.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return jLabelCountryCode;
	}
	/**
	 * This method initializes jLabelAlias.
	 */
	private JLabel getJLabelAlias() {
		if (jLabelAlias == null) {
			jLabelAlias = new JLabel("");
		}
		return jLabelAlias;
	}
	/**
	 * This method initializes jLabelValidity.
	 */
	private JLabel getJLabelValidity() {
		if (jLabelValidity == null) {
			jLabelValidity = new JLabel("");
		}
		return jLabelValidity;
	}
	/**
	 * This method initializes jLabelName.
	 */
	private JLabel getJLabelName() {
		if (jLabelName == null) {
			jLabelName = new JLabel("");
		}
		return jLabelName;
	}
	/**
	 * This method initializes jLabelOrg.
	 */
	private JLabel getJLabelOrg() {
		if (jLabelOrg == null) {
			jLabelOrg = new JLabel("");
		}
		return jLabelOrg;
	}
	/**
	 * This method initializes jLabelOrgUnit.
	 */
	private JLabel getJLabelOrgUnit() {
		if (jLabelOrgUnit == null) {
			jLabelOrgUnit = new JLabel("");
		}
		return jLabelOrgUnit;
	}
	/**
	 * This method initializes jLabelCityOrLocality.
	 */
	private JLabel getJLabelCityOrLocality() {
		if (jLabelCityOrLocality == null) {
			jLabelCityOrLocality = new JLabel("");
		}
		return jLabelCityOrLocality;
	}
	/**
	 * This method initializes jLabelState.
	 */
	private JLabel getJLabelState() {
		if (jLabelState == null) {
			jLabelState = new JLabel("");
		}
		return jLabelState;
	}
	/**
	 * This method initializes jLabelCountry.
	 */
	private JLabel getJLabelCountry() {
		if (jLabelCountry == null) {
			jLabelCountry = new JLabel("");
		}
		return jLabelCountry;
	}
	
	/**
	 * This method initializes TrustStoreController.
	 */
	protected TrustStoreController getTrustStoreController() {
		if (trustStoreController == null) {
			trustStoreController = new TrustStoreController(this.httpsConfigWindow);
		}
		return trustStoreController;
	}
	/**
	 * This method initializes jLabelTrustStoreInformations.
	 */
	private JLabel getJLabelTrustStoreInformations() {
		if (jLabelTrustStoreInformations == null) {
			jLabelTrustStoreInformations = new JLabel(Language.translate("TrustStore Information",Language.EN));
			jLabelTrustStoreInformations.setFont(new Font("Dialog", Font.BOLD, 11));
		}
		return jLabelTrustStoreInformations;
	}
	/**
	 * This method initializes jLabelTruststoreName.
	 */
	private JLabel getJLabelTruststoreName() {
		if (jLabelTruststoreName == null) {
			jLabelTruststoreName = new JLabel(Language.translate("TrustStore Name",Language.EN));
			jLabelTruststoreName.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return jLabelTruststoreName;
	}
	/**
	 * This method initializes jLabelTrustStorePassword.
	 */
	private JLabel getJLabelTrustStorePassword() {
		if (jLabelTrustStorePassword == null) {
			jLabelTrustStorePassword = new JLabel(Language.translate("Password",Language.EN));
			jLabelTrustStorePassword.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return jLabelTrustStorePassword;
	}
	/**
	 * This method initializes jLabelTrustStoreConfirmPassword.
	 */
	private JLabel getJLabelTrustStoreConfirmPassword() {
		if (jLabelTrustStoreConfirmPassword == null) {
			jLabelTrustStoreConfirmPassword = new JLabel(Language.translate("Confirm Password",Language.EN));
			jLabelTrustStoreConfirmPassword.setFont(new Font("Dialog", Font.PLAIN, 11));
		}
		return jLabelTrustStoreConfirmPassword;
	}
	/**
	 * This method initializes jTextFieldTrustStoreName.
	 */
	protected JTextField getJTextFieldTrustStoreName() {
		if (jTextFieldTrustStoreName == null) {
			jTextFieldTrustStoreName = new JTextField();
			jTextFieldTrustStoreName.setEnabled(false);
			jTextFieldTrustStoreName.setPreferredSize(this.fieldSize);
		}
		return jTextFieldTrustStoreName;
	}
	/**
	 * This method initializes jPasswordFieldTrustStorePassword.
	 */
	protected JPasswordField getJPasswordFieldPassword() {
		if (jPasswordFieldPassword == null) {
			jPasswordFieldPassword = new JPasswordField();
			jPasswordFieldPassword.setEnabled(false);
			jPasswordFieldPassword.setPreferredSize(this.fieldSize);
		}
		return jPasswordFieldPassword;
	}
	/**
	 * This method initializes jPasswordFieldTrustStoreConfirmPassword.
	 */
	protected JPasswordField getJPasswordFieldConfirmPassword() {
		if (jPasswordFieldConfirmPassword == null) {
			jPasswordFieldConfirmPassword = new JPasswordField();
			jPasswordFieldConfirmPassword.setEnabled(false);
			jPasswordFieldConfirmPassword.setPreferredSize(this.fieldSize);
		}
		return jPasswordFieldConfirmPassword;
	}
	/**
	 * This method initializes jLabelCertificatesList.
	 */
	protected JLabel getJLabelCertificatesList() {
		if (jLabelCertificatesList == null) {
			jLabelCertificatesList = new JLabel(Language.translate("Trusted Certificates",Language.EN));
			jLabelCertificatesList.setFont(new Font("Dialog", Font.BOLD, 11));
		}
		return jLabelCertificatesList;
	}
	/**
	 * This method initializes jButtonApplyTrustStore.
	 */
	private JButton getJButtonApplyTrustStore() {
		if (jButtonApplyTrustStore == null) {
			jButtonApplyTrustStore = new JButton("");
			jButtonApplyTrustStore.setToolTipText(Language.translate("Save",Language.EN));
			jButtonApplyTrustStore.setForeground(new Color(0, 0, 255));
			jButtonApplyTrustStore.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonApplyTrustStore.setPreferredSize(new Dimension(26, 26));
			jButtonApplyTrustStore.setIcon(GlobalInfo.getInternalImageIcon("MBsave.png"));
			jButtonApplyTrustStore.addActionListener(this);
		}
		return jButtonApplyTrustStore;
	}
	/**
	 * This method initializes jButtonAddCertificate.
	 */
	protected JButton getJButtonAddCertificate() {
		if (jButtonAddCertificate == null) {
			jButtonAddCertificate = new JButton();
			jButtonAddCertificate.setToolTipText(Language.translate("Add Certificate",Language.EN));
			jButtonAddCertificate.setPreferredSize(new Dimension(35, 26));
			jButtonAddCertificate.setFont(new Font("SansSerif", Font.BOLD, 15));
			jButtonAddCertificate.setIcon(GlobalInfo.getInternalImageIcon("ListPlus.png"));
			jButtonAddCertificate.addActionListener(this);
		}
		return jButtonAddCertificate;
	}
	/**
	 * This method initializes jButtonRemoveCertificate.
	 */
	protected JButton getJButtonRemoveCertificate() {
		if (jButtonRemoveCertificate == null) {
			jButtonRemoveCertificate = new JButton();
			jButtonRemoveCertificate.setToolTipText(Language.translate("Remove Certificate",Language.EN));
			jButtonRemoveCertificate.setPreferredSize(new Dimension(35, 26));
			jButtonRemoveCertificate.setFont(new Font("SansSerif", Font.BOLD, 15));
			jButtonRemoveCertificate.setIcon(GlobalInfo.getInternalImageIcon("ListMinus.png"));
			jButtonRemoveCertificate.addActionListener(this);
		}
		return jButtonRemoveCertificate;
	}
	/**
	 * This method initializes scrollPane.
	 */
	protected JScrollPane getScrollPane() {
		if (jscrollPane == null) {
			jscrollPane = new JScrollPane();
			jscrollPane.setViewportView(getjTableTrusTedCertificates());
			jscrollPane.setPreferredSize(new Dimension(200, 170));
		}
		return jscrollPane;
	}
	/**
	 * This method initializes jFileChooser.
	 */
	protected JFileChooser getJFileChooser() {
		if (jFileChooser == null) {
			jFileChooser = new JFileChooser();
			jFileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
			jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
		return jFileChooser;
	}
	/**
	 * This method initializes jFileChooser.
	 */
	protected JFileChooser getJFileChooserFile() {
		if (jFileChooserFile == null) {
			jFileChooserFile = new JFileChooser();
			jFileChooserFile.setDialogType(JFileChooser.SAVE_DIALOG);
			jFileChooserFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
		}
		return jFileChooserFile;
	}
	
	/**
	 * Gets the jTableTrusTedCertificates.
	 * @return the jTableTrusTedCertificates
	 */
	public JTable getjTableTrusTedCertificates() {
		if (jTableTrusTedCertificates == null) {
			jTableTrusTedCertificates = new JTable(getTrustStoreController().getTableModel());
			jTableTrusTedCertificates.addMouseListener(this);
		}
		return jTableTrusTedCertificates;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		// ---- Save changes in case of creating of editing TrustStore ----
		if (ae.getSource() == this.getJButtonApplyTrustStore()) {
			// ----- Create TrustStore ------------------------------------
			if (this.httpsConfigWindow.getTrustStoreButtonPressed() == "CreateTrustStore") {
				// ---- Verify if all the fields are filled ---------------
				if (this.getJTextFieldTrustStoreName().getText().equals("")  || this.getJPasswordFieldPassword().getPassword().length==0 || this.getJPasswordFieldConfirmPassword().getPassword().length==0) {
					String msg = Language.translate("You must fill out all required fields!",Language.EN);
					String title = Language.translate("Required fields",Language.EN);
					JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
				}
				/* --------------------------------------------------------
				 * ----- Verify if The password and its confirm -----------
				 * ---------------- are the same --------------------------
				 */
				else if (!Arrays.equals(this.getJPasswordFieldPassword().getPassword(),this.getJPasswordFieldConfirmPassword().getPassword())) {
					String msg = Language.translate("The password and its confirm are not the same!",Language.EN);
					String title = Language.translate("Warning",Language.EN);
					JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
				}
				/* --------------------------------------------------------
				 * ----- Verify if the password is at least ---------------
				 * ------------- 6 characters in length -------------------
				 */
				else if (this.getJPasswordFieldPassword().getPassword().length < 6) {
					String msg = Language.translate("The password should be at least 6 characters in length!",Language.EN);
					String title = Language.translate("Password length",Language.EN);
					JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
				}else {
					String trustStoreName = this.getJTextFieldTrustStoreName().getText() + HttpsConfigWindow.TRUSTSTORE_FILENAME;
					String trustStorePassword = new String (this.getJPasswordFieldPassword().getPassword());
					this.httpsConfigWindow.setTrustStorePassword(trustStorePassword);
					getJFileChooser();
					int jfile = jFileChooser.showSaveDialog(null);
					if (jfile == JFileChooser.APPROVE_OPTION) {
						this.httpsConfigWindow.setTrustStoreFile(new File(jFileChooser.getSelectedFile().getAbsoluteFile().getAbsolutePath() + File.separator +trustStoreName)); 
						this.getTrustStoreController().createTrustStore(this.httpsConfigWindow.getTrustStoreFile(),this.httpsConfigWindow.getTrustStorePassword());
						String msg = Language.translate("Your TrustStore has been created successfully!",Language.EN);
						String title = Language.translate("TrustStore created",Language.EN);
						JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);
						this.httpsConfigWindow.getJTextFieldTrustStoreLocationPath().setText(this.httpsConfigWindow.getTrustStoreFile().getAbsolutePath());
						this.getJTextFieldTrustStoreName().setEnabled(false);
						this.getJLabelCertificatesList().setVisible(true);
						this.getScrollPane().setVisible(true);
						this.getJButtonAddCertificate().setVisible(true);
						this.getJButtonRemoveCertificate().setVisible(true);
						this.getJPasswordFieldConfirmPassword().setText(null);
						this.getJPasswordFieldPassword().setText(trustStorePassword);
						this.getJTextFieldTrustStoreName().setText(this.httpsConfigWindow.getTrustStoreFile().getAbsolutePath());
						this.getTrustStoreController().clearTableModel();
						this.getTrustStoreController().getTrustedCertificatesList();
						this.getjTableTrusTedCertificates().setModel(getTrustStoreController().getTableModel());
						this.httpsConfigWindow.setTrustStoreButtonPressed("EditTrustStore");
					}
				}

			} else if (this.httpsConfigWindow.getTrustStoreButtonPressed() == "EditTrustStore") {
				// ---- Verify if all the fields are filled ----------
				if (this.getJTextFieldTrustStoreName().getText().equals("") || this.getJPasswordFieldPassword().getPassword().length==0 || this.getJPasswordFieldConfirmPassword().getPassword().length==0) {
					String msg = Language.translate("You must fill out all required fields!",Language.EN);
					String title = Language.translate("Required fields",Language.EN);
					JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
				}
				/* ---------------------------------------------------
				 * ----- Verify if The password and its confirm ------
				 * ---------------- are the same ---------------------
				 */
				else if (!Arrays.equals(this.getJPasswordFieldPassword().getPassword(),this.getJPasswordFieldConfirmPassword().getPassword())) {
					String msg = Language.translate("The password and its confirm are not the same!",Language.EN);
					String title = Language.translate("Warning",Language.EN);
					JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
				}
				/* ---------------------------------------------------
				 * ----- Verify if the password is at least ----------
				 * ------------- 6 characters in length --------------
				 */
				else if (this.getJPasswordFieldPassword().getPassword().length < 6) {
					String msg = Language.translate("The password should be at least 6 characters in length!",Language.EN);
					String title = Language.translate("Password length",Language.EN);
					JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
				} else {
					String newTrustStorePassword = new String(this.getJPasswordFieldPassword().getPassword());
					String keystoreName = this.httpsConfigWindow.getTrustStoreFile().getName();
					// --- Create JOptionPane to enter the old TrustStore password ---
					JPanel jPanelPassword = new JPanel();
					String msg1 = Language.translate("Please enter the old password for  ",Language.EN);
					JLabel jLabelEnterPassword = new JLabel( msg1 + keystoreName + "  :");
					jLabelEnterPassword.setFont(new Font("Dialog", Font.BOLD, 11));
					JPasswordField jPasswordField = new JPasswordField(10);
					jPanelPassword.add(jLabelEnterPassword);
					jPanelPassword.add(jPasswordField);
					String title = Language.translate("Password",Language.EN);
					String[] options = new String[] { "OK", "Cancel" };
					int option = JOptionPane.showOptionDialog(null, jPanelPassword, title, JOptionPane.NO_OPTION,
							JOptionPane.PLAIN_MESSAGE, null, options, jPasswordField);
					String oldPassword = new String(jPasswordField.getPassword());
					if (option == 0) {
						// ------------ Press OK -------------------------------
						if (this.httpsConfigWindow.getTrustStorePassword().equals(oldPassword)){
							this.getTrustStoreController().changeTrustStorePassword(newTrustStorePassword);
							String msg = Language.translate("Your TrustStore has been updated successfully!",Language.EN);
							String title1 = Language.translate("TrustStore updated",Language.EN);
							JOptionPane.showMessageDialog(this, msg, title1, JOptionPane.INFORMATION_MESSAGE); 
							this.httpsConfigWindow.setTrustStorePassword(newTrustStorePassword);
							this.getJPasswordFieldConfirmPassword().setText(null);
							this.getJPasswordFieldPassword().setText(newTrustStorePassword);
						}else{
							String msg = Language.translate("The password you entered is incorrect. Please try again!",Language.EN);
							String title2 = Language.translate("Password incorrect",Language.EN);
							JOptionPane.showMessageDialog(this, msg, title2, JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			}
		}else if (ae.getSource() == this.getJButtonAddCertificate()) {
			// ----- Add certificate to TrustStore ----------------------
			// ----- Verify if there is a TrustStore opened ----
			if (this.httpsConfigWindow.getTrustStoreFile() == null) {
				String msg = Language.translate("Please open a Truststore first!",Language.EN);
				String title = Language.translate("Warning message",Language.EN);
				JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);

			} else {
				// ---- open JfileChooser ---------------------------------
				this.httpsConfigWindow.getJFileChooser();
				int result = this.httpsConfigWindow.getJFileChooser().showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					// ---- Choose the certificate to add ------------------------------
					String certificateToAdd = this.httpsConfigWindow.getJFileChooser().getSelectedFile().getPath();
					// ----- Generate a JOptionDialog to enter certificate alias -------
					JPanel jPanelCertificate = new JPanel();
					JLabel jLabelCertificate = new JLabel(Language.translate("Please, enter Certificate's alias ",Language.EN)+":");
					jLabelCertificate.setFont(new Font("Dialog", Font.BOLD, 11));
					JTextField jTextFieldAlias = new JTextField(10);
					jPanelCertificate.add(jLabelCertificate);
					jPanelCertificate.add(jTextFieldAlias);
					String title = "Certificate";
					String[] options = new String[] { "OK", "Cancel" };
					int option = JOptionPane.showOptionDialog(null, jPanelCertificate, title, JOptionPane.NO_OPTION,JOptionPane.PLAIN_MESSAGE, null, options, jTextFieldAlias);
					String certificateAlias = jTextFieldAlias.getText();
					if (option == 0) {
						if (certificateAlias.equals("")) {
							String msg = Language.translate("Please, enter your certificate's alias!",Language.EN);
							String title1 = Language.translate("Certificate alias",Language.EN);
							JOptionPane.showMessageDialog(this, msg, title1, JOptionPane.INFORMATION_MESSAGE);
						} else {
							// ------------ Press OK Button --------------------
							this.getTrustStoreController().addCertificateToTrustStore(certificateToAdd, certificateAlias);
							this.getTrustStoreController().clearTableModel();
							this.getTrustStoreController().getTrustedCertificatesList();
							this.getjTableTrusTedCertificates().setModel(this.getTrustStoreController().getTableModel());
						}
					}
				} else {
					this.httpsConfigWindow.getJFileChooser().remove(this.httpsConfigWindow.getJFileChooser());
				}
			}
		}else if (ae.getSource() == this.getJButtonRemoveCertificate()) {
			// ----- Delete certificate to TrustStore ----------------------
			// ----- Verify if there is a TrustStore opened ----------------
			if (this.httpsConfigWindow.getTrustStoreFile() == null) {
				String msg = Language.translate("Please, open a Truststore first!",Language.EN);
				String title = Language.translate("Warning message",Language.EN);
				JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
			} else {
				if (getjTableTrusTedCertificates().getSelectedRow() == -1) {
					String msg = Language.translate("You should select a certificate!",Language.EN);
					String title = Language.translate("Warning message",Language.EN);
					JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
				} else {
					// ------ Get the certificate's alias to delete ----
					int RowSelected = jTableTrusTedCertificates.getSelectedRow();
					String CertificateAliasToDelete = jTableTrusTedCertificates.getValueAt(RowSelected, 0).toString();
					// --------- Generate a ConfirmDialog --------------
					String[] options = new String[] { "Yes", "No" };
					String msg = Language.translate("Would You Like to delete this Certificate?",Language.EN);
					String title = Language.translate("Warning message",Language.EN);
					int option = JOptionPane.showOptionDialog(null, msg,title, JOptionPane.NO_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
					
					if (option == JOptionPane.YES_OPTION) {
						// ----- press OK Button ----------------------
						this.getTrustStoreController().deleteCertificateFromTrustStore(CertificateAliasToDelete);
						this.getTrustStoreController().clearTableModel();
						this.getTrustStoreController().getTrustedCertificatesList();
						this.getjTableTrusTedCertificates().setModel(this.getTrustStoreController().getTableModel());
					}
				}
			}
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource()== this.getjTableTrusTedCertificates()){
			if (e.getClickCount() == 2) {
				// ---- Get alias of selected certificate ----------------------
				int row = this.getjTableTrusTedCertificates().getSelectedRow();
				String alias = this.getjTableTrusTedCertificates().getValueAt(row, 0).toString();
				// ---- Get informations of selected certificate --------------
				CertificateProperties certificateProperties = this.getTrustStoreController().getCertificateProperties(alias);
				
				this.getJLabelAlias().setText(alias);
				this.getJLabelValidity().setText(certificateProperties.getValidity());
				this.getJLabelName().setText(certificateProperties.getCommonName());
				this.getJLabelOrg().setText(certificateProperties.getOrganization());
				this.getJLabelOrgUnit().setText(certificateProperties.getOrganizationalUnit());
				this.getJLabelCityOrLocality().setText(certificateProperties.getCityOrLocality());
				this.getJLabelState().setText(certificateProperties.getStateOrProvince());
				this.getJLabelCountry().setText(certificateProperties.getCountryCode());
				// ---- Display certificate informations ----------------------
				String title = Language.translate("Certificate informations", Language.EN);
				JOptionPane.showMessageDialog(null, getJPanelCertificateInfo(), title, JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}
	@Override
	public void mousePressed(MouseEvent e) {
	}
	@Override
	public void mouseReleased(MouseEvent e) {
	}
}
