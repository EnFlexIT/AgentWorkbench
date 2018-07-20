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
package org.agentgui.gui.swing.project;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.config.GlobalInfo.MtpProtocol;
import agentgui.core.gui.components.JComboBoxMtpProtocol;
import agentgui.core.gui.options.https.HttpsConfigWindow;
import agentgui.core.network.NetworkAddresses;
import agentgui.core.project.PlatformJadeConfig;
import agentgui.core.project.PlatformJadeConfig.MTP_Creation;
import agentgui.core.project.Project;

/**
 * Represents the JPanel/Tab 'Configuration' - 'JADE-Configuration'
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JadeSetupMTP extends JPanel implements ActionListener, Observer, ItemListener {

	private static final long serialVersionUID = -7016775471452161527L;

	private Project currProject;
//	private HttpsConfigWindow httpsConfigWindow;

	private boolean pauseObserver;
//	private boolean pauseDocumentListener;

	private JLabel jLabelPort;
	private JLabel jLabelPortExplain;
	private JTextField jTextFieldDefaultPort;
	private JButton jButtonSetPortDefault;
	private JButton jButtonSetPort;
	private JLabel jLabelMTP;
	private JRadioButton jRadioButtonMtpAutoConfig;
	private JRadioButton jRadioButtonMtpIP;
	
	private JTextField jTextFieldDefaultPortMTP;
	private JButton jButtonSetPortMTP;
	private JButton jButtonSetPortMTPDefault;
	private JLabel jLabelMTPport;
	
	private JLabel jLabelIP;
	private JTextField jTextFieldIPAddress;
	private JButton jButtonIPedit;
	
	private JLabel jLabelKeyStore;
	private JLabel jLabelTrustStore;
	private JLabel jLabelMtpProtocol;
	private JTextField jTextFieldKeyStoreFile;
	private JTextField jTextFieldTrustStoreFile;
	
	private JButton jButtonEditSecurityStores;
	private JComboBoxMtpProtocol jComboBoxMtpProtocol;
	
	private JPanel jPanelMTPaddress;
	private JPanel jPanelProtocol;
	private JPanel jPanelPorts;
	private JSeparator jSeparatorB;
	private JSeparator jSeparatorC;
	private JSeparator jSeparatorA;
	private JCheckBox jCheckBoxSkipUserRequestForJadeStart;
	private JButton jButtonDeleteSecurityStores;
	private JLabel jLabeSecurityPathInfo;
	
	/**
	 * Constructor of this class
	 * @param project
	 */
	public JadeSetupMTP(Project project) {
		super();
		this.currProject = project;
		this.currProject.addObserver(this);
		this.initialize();
		
		this.refreshDataView();
		
		// --- configure translation ------------
		jLabelPort.setText(Language.translate("Starte JADE über Port-Nr.:"));
		jLabelPortExplain.setText(Language.translate("(Falls bereits verwendet, wird versucht den nächst höheren Port zu nutzen)"));
		jLabelMtpProtocol.setText(Language.translate("MTP-Protokoll:"));

		jButtonSetPort.setToolTipText(Language.translate("JADE-Port bearbeiten"));
		jButtonSetPortDefault.setToolTipText(Language.translate("Standard verwenden"));
		
		jLabelMTP.setText(Language.translate("MTP-Adresse") + ":");
		jRadioButtonMtpAutoConfig.setText(Language.translate("JADE-Automatik verwenden"));
		jRadioButtonMtpIP.setText(Language.translate("IP-Adresse verwenden"));
		jButtonIPedit.setToolTipText(Language.translate("IP auswählen"));
		
		jButtonSetPortMTP.setToolTipText(Language.translate("JADE-Port bearbeiten"));
		jButtonSetPortMTPDefault.setToolTipText(Language.translate("Standard verwenden"));
		
	}
	
	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		GridBagLayout gblMain = new GridBagLayout();
		gblMain.columnWidths = new int[]{0, 0};
		gblMain.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gblMain.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gblMain.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		this.setLayout(gblMain);
		
		GridBagConstraints gbc_jCheckBoxSkipUserRequestForJadeStart = new GridBagConstraints();
		gbc_jCheckBoxSkipUserRequestForJadeStart.anchor = GridBagConstraints.WEST;
		gbc_jCheckBoxSkipUserRequestForJadeStart.insets = new Insets(15, 10, 5, 10);
		gbc_jCheckBoxSkipUserRequestForJadeStart.gridx = 0;
		gbc_jCheckBoxSkipUserRequestForJadeStart.gridy = 0;
		add(getJCheckBoxSkipUserRequestForJadeStart(), gbc_jCheckBoxSkipUserRequestForJadeStart);
		GridBagConstraints gbc_jSeparatorA = new GridBagConstraints();
		gbc_jSeparatorA.fill = GridBagConstraints.HORIZONTAL;
		gbc_jSeparatorA.insets = new Insets(5, 10, 5, 10);
		gbc_jSeparatorA.gridx = 0;
		gbc_jSeparatorA.gridy = 1;
		add(getJSeparatorA(), gbc_jSeparatorA);
		GridBagConstraints gbc_jPanelPorts = new GridBagConstraints();
		gbc_jPanelPorts.insets = new Insets(10, 10, 5, 10);
		gbc_jPanelPorts.fill = GridBagConstraints.BOTH;
		gbc_jPanelPorts.gridx = 0;
		gbc_jPanelPorts.gridy = 2;
		add(getJPanelPorts(), gbc_jPanelPorts);
		GridBagConstraints gbc_jSeparatorB = new GridBagConstraints();
		gbc_jSeparatorB.fill = GridBagConstraints.HORIZONTAL;
		gbc_jSeparatorB.insets = new Insets(5, 10, 5, 10);
		gbc_jSeparatorB.gridx = 0;
		gbc_jSeparatorB.gridy = 3;
		add(getJSeparatorB(), gbc_jSeparatorB);
		GridBagConstraints gbc_jPanelMTPaddress = new GridBagConstraints();
		gbc_jPanelMTPaddress.insets = new Insets(10, 10, 5, 10);
		gbc_jPanelMTPaddress.fill = GridBagConstraints.BOTH;
		gbc_jPanelMTPaddress.gridx = 0;
		gbc_jPanelMTPaddress.gridy = 4;
		add(getJPanelMTPaddress(), gbc_jPanelMTPaddress);
		GridBagConstraints gbc_jSeparatorC = new GridBagConstraints();
		gbc_jSeparatorC.fill = GridBagConstraints.HORIZONTAL;
		gbc_jSeparatorC.insets = new Insets(5, 10, 5, 10);
		gbc_jSeparatorC.gridx = 0;
		gbc_jSeparatorC.gridy = 5;
		add(getJSeparatorC(), gbc_jSeparatorC);
		GridBagConstraints gbc_jPanelProtocol = new GridBagConstraints();
		gbc_jPanelProtocol.insets = new Insets(10, 10, 0, 10);
		gbc_jPanelProtocol.anchor = GridBagConstraints.WEST;
		gbc_jPanelProtocol.fill = GridBagConstraints.VERTICAL;
		gbc_jPanelProtocol.gridx = 0;
		gbc_jPanelProtocol.gridy = 6;
		add(getJPanelProtocol(), gbc_jPanelProtocol);
		
		// --- Create the ButtonGroup for the radio buttons ---------
		ButtonGroup bGroup = new ButtonGroup();
		bGroup.add(this.getJRadioButtonMtpAutoConfig());
		bGroup.add(this.getJRadioButtonMtpIP());
	}

	/**
	 * Gets the JCheckBox that indicates to skip user request for the JADE start.
	 * @return the JCheckBox that indicates to skip user request for the JADE start
	 */
	private JCheckBox getJCheckBoxSkipUserRequestForJadeStart() {
		if (jCheckBoxSkipUserRequestForJadeStart == null) {
			jCheckBoxSkipUserRequestForJadeStart = new JCheckBox(Language.translate("JADE direkt starten ohne nachzufragen."));
			jCheckBoxSkipUserRequestForJadeStart.setFont(new Font("Dialog", Font.BOLD, 12));
			jCheckBoxSkipUserRequestForJadeStart.addActionListener(this);
		}
		return jCheckBoxSkipUserRequestForJadeStart;
	}
	private JSeparator getJSeparatorA() {
		if (jSeparatorA == null) {
			jSeparatorA = new JSeparator();
		}
		return jSeparatorA;
	}

	/**
	 * Gets the jPanelMTPaddress.
	 * @return thejPanelMTPaddress
	 */
	private JPanel getJPanelMTPaddress() {
		if (jPanelMTPaddress == null) {
			jPanelMTPaddress = new JPanel();
			GridBagLayout gbl_jPanelMTPaddress = new GridBagLayout();
			gbl_jPanelMTPaddress.columnWidths = new int[]{0, 0, 0, 0, 0};
			gbl_jPanelMTPaddress.rowHeights = new int[]{0, 0, 0};
			gbl_jPanelMTPaddress.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
			gbl_jPanelMTPaddress.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
			jPanelMTPaddress.setLayout(gbl_jPanelMTPaddress);
			GridBagConstraints gbc_jLabelMTP = new GridBagConstraints();
			gbc_jLabelMTP.anchor = GridBagConstraints.WEST;
			gbc_jLabelMTP.insets = new Insets(0, 0, 5, 5);
			gbc_jLabelMTP.gridx = 0;
			gbc_jLabelMTP.gridy = 0;
			jPanelMTPaddress.add(getJLabelMTP(), gbc_jLabelMTP);
			GridBagConstraints gbc_jRadioButtonMtpAutoConfig = new GridBagConstraints();
			gbc_jRadioButtonMtpAutoConfig.insets = new Insets(0, 0, 5, 5);
			gbc_jRadioButtonMtpAutoConfig.gridx = 1;
			gbc_jRadioButtonMtpAutoConfig.gridy = 0;
			jPanelMTPaddress.add(getJRadioButtonMtpAutoConfig(), gbc_jRadioButtonMtpAutoConfig);
			GridBagConstraints gbc_jRadioButtonMtpIP = new GridBagConstraints();
			gbc_jRadioButtonMtpIP.insets = new Insets(0, 0, 5, 5);
			gbc_jRadioButtonMtpIP.gridx = 2;
			gbc_jRadioButtonMtpIP.gridy = 0;
			jPanelMTPaddress.add(getJRadioButtonMtpIP(), gbc_jRadioButtonMtpIP);
			GridBagConstraints gbc_jButtonIPedit = new GridBagConstraints();
			gbc_jButtonIPedit.anchor = GridBagConstraints.EAST;
			gbc_jButtonIPedit.insets = new Insets(0, 0, 5, 0);
			gbc_jButtonIPedit.gridx = 3;
			gbc_jButtonIPedit.gridy = 0;
			jPanelMTPaddress.add(getJButtonIPedit(), gbc_jButtonIPedit);
			GridBagConstraints gbc_jLabelIP = new GridBagConstraints();
			gbc_jLabelIP.anchor = GridBagConstraints.WEST;
			gbc_jLabelIP.insets = new Insets(0, 0, 0, 5);
			gbc_jLabelIP.gridx = 0;
			gbc_jLabelIP.gridy = 1;
			jPanelMTPaddress.add(getJLabelIP(), gbc_jLabelIP);
			GridBagConstraints gbc_jTextFieldIPAddress = new GridBagConstraints();
			gbc_jTextFieldIPAddress.fill = GridBagConstraints.HORIZONTAL;
			gbc_jTextFieldIPAddress.gridwidth = 3;
			gbc_jTextFieldIPAddress.gridx = 1;
			gbc_jTextFieldIPAddress.gridy = 1;
			jPanelMTPaddress.add(getJTextFieldIPAddress(), gbc_jTextFieldIPAddress);
		}
		return jPanelMTPaddress;
	}
	
	/**
	 * Gets the jPanelProtocol.
	 * @return the jPanelProtocol
	 */
	private JPanel getJPanelProtocol() {
		if (jPanelProtocol == null) {
			jPanelProtocol = new JPanel();
			GridBagLayout gbl_jPanelProtocol = new GridBagLayout();
			gbl_jPanelProtocol.columnWidths = new int[]{0, 0, 0, 0, 0};
			gbl_jPanelProtocol.rowHeights = new int[]{0, 0, 0, 0, 0};
			gbl_jPanelProtocol.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelProtocol.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			jPanelProtocol.setLayout(gbl_jPanelProtocol);
			GridBagConstraints gbc_jLabelMtpProtocol = new GridBagConstraints();
			gbc_jLabelMtpProtocol.anchor = GridBagConstraints.WEST;
			gbc_jLabelMtpProtocol.insets = new Insets(0, 0, 5, 5);
			gbc_jLabelMtpProtocol.gridx = 0;
			gbc_jLabelMtpProtocol.gridy = 0;
			jPanelProtocol.add(getJLabelMtpProtocol(), gbc_jLabelMtpProtocol);
			GridBagConstraints gbc_jComboBoxMtpProtocol = new GridBagConstraints();
			gbc_jComboBoxMtpProtocol.insets = new Insets(0, 0, 5, 5);
			gbc_jComboBoxMtpProtocol.gridx = 1;
			gbc_jComboBoxMtpProtocol.gridy = 0;
			jPanelProtocol.add(getJComboBoxMtpProtocol(), gbc_jComboBoxMtpProtocol);
			GridBagConstraints gbc_jButtonDeleteSecurityStores = new GridBagConstraints();
			gbc_jButtonDeleteSecurityStores.insets = new Insets(0, 0, 5, 5);
			gbc_jButtonDeleteSecurityStores.anchor = GridBagConstraints.EAST;
			gbc_jButtonDeleteSecurityStores.gridx = 2;
			gbc_jButtonDeleteSecurityStores.gridy = 0;
			jPanelProtocol.add(getJButtonDeleteSecurityStores(), gbc_jButtonDeleteSecurityStores);
			GridBagConstraints gbc_jButtonEditSecurityStores = new GridBagConstraints();
			gbc_jButtonEditSecurityStores.anchor = GridBagConstraints.EAST;
			gbc_jButtonEditSecurityStores.insets = new Insets(0, 0, 5, 0);
			gbc_jButtonEditSecurityStores.gridx = 3;
			gbc_jButtonEditSecurityStores.gridy = 0;
			jPanelProtocol.add(getJButtonEditSecurityStores(), gbc_jButtonEditSecurityStores);
			GridBagConstraints gbc_jLabelKeyStore = new GridBagConstraints();
			gbc_jLabelKeyStore.anchor = GridBagConstraints.WEST;
			gbc_jLabelKeyStore.insets = new Insets(0, 0, 5, 5);
			gbc_jLabelKeyStore.gridx = 0;
			gbc_jLabelKeyStore.gridy = 1;
			jPanelProtocol.add(getJLabelKeyStore(), gbc_jLabelKeyStore);
			GridBagConstraints gbc_jTextFieldKeyStoreFile = new GridBagConstraints();
			gbc_jTextFieldKeyStoreFile.anchor = GridBagConstraints.WEST;
			gbc_jTextFieldKeyStoreFile.gridwidth = 3;
			gbc_jTextFieldKeyStoreFile.insets = new Insets(0, 0, 5, 0);
			gbc_jTextFieldKeyStoreFile.gridx = 1;
			gbc_jTextFieldKeyStoreFile.gridy = 1;
			jPanelProtocol.add(getJTextFieldKeyStoreFile(), gbc_jTextFieldKeyStoreFile);
			GridBagConstraints gbc_jLabelTrustStore = new GridBagConstraints();
			gbc_jLabelTrustStore.anchor = GridBagConstraints.WEST;
			gbc_jLabelTrustStore.insets = new Insets(0, 0, 5, 5);
			gbc_jLabelTrustStore.gridx = 0;
			gbc_jLabelTrustStore.gridy = 2;
			jPanelProtocol.add(getJLabelTrustStore(), gbc_jLabelTrustStore);
			GridBagConstraints gbc_jTextFieldTrustStoreFile = new GridBagConstraints();
			gbc_jTextFieldTrustStoreFile.insets = new Insets(0, 0, 5, 0);
			gbc_jTextFieldTrustStoreFile.anchor = GridBagConstraints.WEST;
			gbc_jTextFieldTrustStoreFile.gridwidth = 3;
			gbc_jTextFieldTrustStoreFile.gridx = 1;
			gbc_jTextFieldTrustStoreFile.gridy = 2;
			jPanelProtocol.add(getJTextFieldTrustStoreFile(), gbc_jTextFieldTrustStoreFile);
			GridBagConstraints gbc_jLabeSecurityPathInfo = new GridBagConstraints();
			gbc_jLabeSecurityPathInfo.gridwidth = 3;
			gbc_jLabeSecurityPathInfo.anchor = GridBagConstraints.WEST;
			gbc_jLabeSecurityPathInfo.gridx = 1;
			gbc_jLabeSecurityPathInfo.gridy = 3;
			jPanelProtocol.add(getJLabeSecurityPathInfo(), gbc_jLabeSecurityPathInfo);
		}
		return jPanelProtocol;
	}
	
	/**
	 * Gets the jPanelPorts.
	 * @return the jPanelPorts
	 */
	private JPanel getJPanelPorts() {
		if (jPanelPorts == null) {
			jPanelPorts = new JPanel();
			GridBagLayout gbl_jPanelPorts = new GridBagLayout();
			gbl_jPanelPorts.columnWidths = new int[]{0, 0, 0, 0, 0};
			gbl_jPanelPorts.rowHeights = new int[]{0, 0, 0, 0};
			gbl_jPanelPorts.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			gbl_jPanelPorts.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
			jPanelPorts.setLayout(gbl_jPanelPorts);
			
			jLabelPort = new JLabel();
			GridBagConstraints gbc_jLabelPort = new GridBagConstraints();
			gbc_jLabelPort.anchor = GridBagConstraints.WEST;
			gbc_jLabelPort.insets = new Insets(0, 0, 5, 5);
			gbc_jLabelPort.gridx = 0;
			gbc_jLabelPort.gridy = 0;
			jPanelPorts.add(jLabelPort, gbc_jLabelPort);
			jLabelPort.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelPort.setText("Starte JADE über Port-Nr.:");
			GridBagConstraints gbc_jTextFieldDefaultPort = new GridBagConstraints();
			gbc_jTextFieldDefaultPort.insets = new Insets(0, 0, 5, 5);
			gbc_jTextFieldDefaultPort.gridx = 1;
			gbc_jTextFieldDefaultPort.gridy = 0;
			jPanelPorts.add(getJTextFieldDefaultPort(), gbc_jTextFieldDefaultPort);
			GridBagConstraints gbc_jButtonSetPort = new GridBagConstraints();
			gbc_jButtonSetPort.insets = new Insets(0, 0, 5, 5);
			gbc_jButtonSetPort.gridx = 2;
			gbc_jButtonSetPort.gridy = 0;
			jPanelPorts.add(getJButtonSetPort(), gbc_jButtonSetPort);
			GridBagConstraints gbc_jButtonSetPortDefault = new GridBagConstraints();
			gbc_jButtonSetPortDefault.anchor = GridBagConstraints.WEST;
			gbc_jButtonSetPortDefault.insets = new Insets(0, 0, 5, 0);
			gbc_jButtonSetPortDefault.gridx = 3;
			gbc_jButtonSetPortDefault.gridy = 0;
			jPanelPorts.add(getJButtonSetPortDefault(), gbc_jButtonSetPortDefault);
			GridBagConstraints gbc_jLabelMTPport = new GridBagConstraints();
			gbc_jLabelMTPport.anchor = GridBagConstraints.WEST;
			gbc_jLabelMTPport.insets = new Insets(0, 0, 5, 5);
			gbc_jLabelMTPport.gridx = 0;
			gbc_jLabelMTPport.gridy = 1;
			jPanelPorts.add(getJLabelMTPport(), gbc_jLabelMTPport);
			GridBagConstraints gbc_jTextFieldDefaultPortMTP = new GridBagConstraints();
			gbc_jTextFieldDefaultPortMTP.insets = new Insets(0, 0, 5, 5);
			gbc_jTextFieldDefaultPortMTP.gridx = 1;
			gbc_jTextFieldDefaultPortMTP.gridy = 1;
			jPanelPorts.add(getJTextFieldDefaultPortMTP(), gbc_jTextFieldDefaultPortMTP);
			GridBagConstraints gbc_jButtonSetPortMTP = new GridBagConstraints();
			gbc_jButtonSetPortMTP.insets = new Insets(0, 0, 5, 5);
			gbc_jButtonSetPortMTP.gridx = 2;
			gbc_jButtonSetPortMTP.gridy = 1;
			jPanelPorts.add(getJButtonSetPortMTP(), gbc_jButtonSetPortMTP);
			GridBagConstraints gbc_jButtonSetPortMTPDefault = new GridBagConstraints();
			gbc_jButtonSetPortMTPDefault.anchor = GridBagConstraints.WEST;
			gbc_jButtonSetPortMTPDefault.insets = new Insets(0, 0, 5, 0);
			gbc_jButtonSetPortMTPDefault.gridx = 3;
			gbc_jButtonSetPortMTPDefault.gridy = 1;
			jPanelPorts.add(getJButtonSetPortMTPDefault(), gbc_jButtonSetPortMTPDefault);
			
			jLabelPortExplain = new JLabel();
			jLabelPortExplain.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabelPortExplain.setText("(Falls bereits verwendet, wird versucht den nächst höheren Port zu nutzen)");
			GridBagConstraints gbc_jLabelPortExplain = new GridBagConstraints();
			gbc_jLabelPortExplain.anchor = GridBagConstraints.WEST;
			gbc_jLabelPortExplain.gridwidth = 4;
			gbc_jLabelPortExplain.insets = new Insets(0, 0, 0, 5);
			gbc_jLabelPortExplain.gridx = 0;
			gbc_jLabelPortExplain.gridy = 2;
			jPanelPorts.add(jLabelPortExplain, gbc_jLabelPortExplain);
			
		}
		return jPanelPorts;
	}
	/**
	 * Gets the jSeparatorA.
	 * @return the jSeparatorA
	 */
	private JSeparator getJSeparatorB() {
		if (jSeparatorB == null) {
			jSeparatorB = new JSeparator();
		}
		return jSeparatorB;
	}
	/**
	 * Gets the jSeparatorB.
	 * @return the jSeparatorB
	 */
	private JSeparator getJSeparatorC() {
		if (jSeparatorC == null) {
			jSeparatorC = new JSeparator();
		}
		return jSeparatorC;
	}
	/**
	 * This method initializes jTextFieldDefaultPort	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldDefaultPort() {
		if (jTextFieldDefaultPort == null) {
			jTextFieldDefaultPort = new JTextField();
			jTextFieldDefaultPort.setFont(new Font("Dialog", Font.BOLD, 12));
			jTextFieldDefaultPort.setPreferredSize(new Dimension(71, 26));
			jTextFieldDefaultPort.setEditable(false);			
		}
		return jTextFieldDefaultPort;
	}
	/**
	 * This method initializes jButtonSetPort	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSetPort() {
		if (jButtonSetPort == null) {
			jButtonSetPort = new JButton();
			jButtonSetPort.setIcon(GlobalInfo.getInternalImageIcon("edit.png"));
			jButtonSetPort.setPreferredSize(new Dimension(45, 26));
			jButtonSetPort.setToolTipText("JADE-Port bearbeiten");
			jButtonSetPort.setActionCommand("SetPort");
			jButtonSetPort.addActionListener(this);
		}
		return jButtonSetPort;
	}
	/**
	 * This method initializes jButtonSetPortDefault	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSetPortDefault() {
		if (jButtonSetPortDefault == null) {
			jButtonSetPortDefault = new JButton();
			jButtonSetPortDefault.setIcon(GlobalInfo.getInternalImageIcon("MBreset.png"));
			jButtonSetPortDefault.setPreferredSize(new Dimension(45, 26));
			jButtonSetPortDefault.setToolTipText("Standard verwenden");
			jButtonSetPortDefault.setActionCommand("SetPortDefault");
			jButtonSetPortDefault.addActionListener(this);
		}
		return jButtonSetPortDefault;
	}
	/**
	 * Gets the JLabel MTP.
	 * @return the JLabel MTP
	 */
	private JLabel getJLabelMTP() {
		if (jLabelMTP == null) {
			jLabelMTP = new JLabel("MTP-Address:");
			jLabelMTP.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelMTP;
	}
	/**
	 * Gets the JRadioButton auto configuration.
	 * @return the JRadioButton auto configuration
	 */
	private JRadioButton getJRadioButtonMtpAutoConfig() {
		if (jRadioButtonMtpAutoConfig == null) {
			jRadioButtonMtpAutoConfig = new JRadioButton("Auto-Config");
			jRadioButtonMtpAutoConfig.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRadioButtonMtpAutoConfig.addActionListener(this);
		}
		return jRadioButtonMtpAutoConfig;
	}
	/**
	 * Gets the JRadioButton for IP usage.
	 * @return the JRadioButton IP usage 
	 */
	private JRadioButton getJRadioButtonMtpIP() {
		if (jRadioButtonMtpIP == null) {
			jRadioButtonMtpIP = new JRadioButton("Use IP-Address");
			jRadioButtonMtpIP.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRadioButtonMtpIP.addActionListener(this);
		}
		return jRadioButtonMtpIP;
	}
	
	/**
	 * Gets the jLabelIP.
	 * @return the jLabelIP
	 */
	private JLabel getJLabelIP() {
		if (jLabelIP == null) {
			jLabelIP = new JLabel("IP:");
			jLabelIP.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelIP;
	}
	
	
	/**
	 * Gets the jTextFieldIPAddress.
	 * @param pauseDocumentListener set true to pause the document listener
	 * @return the jTextFieldIPAddress
	 */
	private JTextField getJTextFieldIPAddress() {
		if (jTextFieldIPAddress==null) {
			jTextFieldIPAddress = new JTextField();
			jTextFieldIPAddress.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldIPAddress.setPreferredSize(new Dimension(400, 26));
			jTextFieldIPAddress.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void removeUpdate(DocumentEvent de) {
					this.setIPAddress();
				}
				@Override
				public void insertUpdate(DocumentEvent de) {
					this.setIPAddress();
				}
				@Override
				public void changedUpdate(DocumentEvent de) {
					this.setIPAddress();
				}
				private void setIPAddress() {
					String newValue = getJTextFieldIPAddress().getText().trim();
					if (newValue.equals(currProject.getJadeConfiguration().getMtpIpAddress())==false) {
						pauseObserver = true;
						currProject.getJadeConfiguration().setMtpIpAddress(newValue);
						pauseObserver = false;
					}
				}
			});
		}
		return jTextFieldIPAddress;
	}
	
	/**
	 * Gets the jButtonIPedit.
	 * @return the jButtonIPedit
	 */
	private JButton getJButtonIPedit() {
		if (jButtonIPedit == null) {
			jButtonIPedit = new JButton();
			jButtonIPedit.setToolTipText("IP auswählen");
			jButtonIPedit.setPreferredSize(new Dimension(45, 26));
			jButtonIPedit.setIcon(GlobalInfo.getInternalImageIcon("edit.png"));
			jButtonIPedit.addActionListener(this);
		}
		return jButtonIPedit;
	}
	
	/**
	 * Gets the JLabel mtp port.
	 * @return the JLabel mtp port
	 */
	private JLabel getJLabelMTPport() {
		if (jLabelMTPport == null) {
			jLabelMTPport = new JLabel("MTP-Port:");
			jLabelMTPport.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelMTPport;
	}

	private JTextField getJTextFieldDefaultPortMTP() {
		if (jTextFieldDefaultPortMTP == null) {
			jTextFieldDefaultPortMTP = new JTextField();
			jTextFieldDefaultPortMTP.setText((String) null);
			jTextFieldDefaultPortMTP.setPreferredSize(new Dimension(71, 26));
			jTextFieldDefaultPortMTP.setFont(new Font("Dialog", Font.BOLD, 12));
			jTextFieldDefaultPortMTP.setEditable(false);
		}
		return jTextFieldDefaultPortMTP;
	}
	private JButton getJButtonSetPortMTP() {
		if (jButtonSetPortMTP == null) {
			jButtonSetPortMTP = new JButton();
			jButtonSetPortMTP.setToolTipText("JADE-Port bearbeiten");
			jButtonSetPortMTP.setPreferredSize(new Dimension(45, 26));
			jButtonSetPortMTP.setIcon(GlobalInfo.getInternalImageIcon("edit.png"));
			jButtonSetPortMTP.setActionCommand("SetPortMTP");
			jButtonSetPortMTP.addActionListener(this);
		}
		return jButtonSetPortMTP;
	}
	private JButton getJButtonSetPortMTPDefault() {
		if (jButtonSetPortMTPDefault == null) {
			jButtonSetPortMTPDefault = new JButton();
			jButtonSetPortMTPDefault.setToolTipText("Standard verwenden");
			jButtonSetPortMTPDefault.setPreferredSize(new Dimension(45, 26));
			jButtonSetPortMTPDefault.setActionCommand("SetPortDefault");
			jButtonSetPortMTPDefault.setIcon(GlobalInfo.getInternalImageIcon("MBreset.png"));
			jButtonSetPortMTPDefault.addActionListener(this);
		}
		return jButtonSetPortMTPDefault;
	}
	private JLabel getJLabelKeyStore() {
		if (jLabelKeyStore == null) {
			jLabelKeyStore = new JLabel("KeyStore:");
			jLabelKeyStore.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelKeyStore;
	}
	private JLabel getJLabelTrustStore() {
		if (jLabelTrustStore == null) {
			jLabelTrustStore = new JLabel("TrustStore:");
			jLabelTrustStore.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelTrustStore;
	}
	private JTextField getJTextFieldKeyStoreFile() {
		if (jTextFieldKeyStoreFile == null) {
			jTextFieldKeyStoreFile = new JTextField();
			jTextFieldKeyStoreFile.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldKeyStoreFile.setPreferredSize(new Dimension(400, 26));
			jTextFieldKeyStoreFile.setEditable(false);
		}
		return jTextFieldKeyStoreFile;
	}
	private JTextField getJTextFieldTrustStoreFile() {
		if (jTextFieldTrustStoreFile == null) {
			jTextFieldTrustStoreFile = new JTextField();
			jTextFieldTrustStoreFile.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldTrustStoreFile.setPreferredSize(new Dimension(400, 26));
			jTextFieldTrustStoreFile.setEditable(false);
		}
		return jTextFieldTrustStoreFile;
	}
	private JLabel getJLabeSecurityPathInfo() {
		if (jLabeSecurityPathInfo == null) {
			jLabeSecurityPathInfo = new JLabel("(" + Language.translate("located in the projects 'security' directory", Language.EN) + ")");
			jLabeSecurityPathInfo.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabeSecurityPathInfo;
	}
	
	/**
	 * This method initializes jLabelMtpProtocol	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getJLabelMtpProtocol() {
		if (jLabelMtpProtocol == null) {
			jLabelMtpProtocol = new JLabel();
			jLabelMtpProtocol.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelMtpProtocol;
	}
	private JButton getJButtonDeleteSecurityStores() {
		if (jButtonDeleteSecurityStores == null) {
			jButtonDeleteSecurityStores = new JButton();
			jButtonDeleteSecurityStores.setToolTipText(Language.translate("Delete security store settings", Language.EN));
			jButtonDeleteSecurityStores.setPreferredSize(new Dimension(45, 26));
			jButtonDeleteSecurityStores.setIcon(GlobalInfo.getInternalImageIcon("Delete.png"));
			jButtonDeleteSecurityStores.addActionListener(this);
		}
		return jButtonDeleteSecurityStores;
	}
	/**
	 * This method initializes jButtonEditMtpProtocol	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonEditSecurityStores() {
		if (jButtonEditSecurityStores == null) {
			jButtonEditSecurityStores = new JButton();
			jButtonEditSecurityStores.setToolTipText(Language.translate("Edit security store settings", Language.EN));
			jButtonEditSecurityStores.setIcon(GlobalInfo.getInternalImageIcon("edit.png"));
			jButtonEditSecurityStores.setPreferredSize(new Dimension(45, 26));
			jButtonEditSecurityStores.addActionListener(this);
		}
		return jButtonEditSecurityStores;
	}
	/**
	 * This method initializes jComboBoxMtpProtocol	
	 * @return JComboBoxMtpProtocol	
	 */
	private JComboBoxMtpProtocol getJComboBoxMtpProtocol(){
		if( jComboBoxMtpProtocol == null){
			jComboBoxMtpProtocol = new JComboBoxMtpProtocol();
			jComboBoxMtpProtocol.setPreferredSize(new Dimension(130, 26));
			jComboBoxMtpProtocol.addItemListener(this);
		}
		return jComboBoxMtpProtocol;
	}
	
	/**
	 * This method can be called in order to refresh the view 
	 */
	private void refreshDataView() {
		
		// --- Skip JADE start request ------------------------------
		this.getJCheckBoxSkipUserRequestForJadeStart().setSelected(this.currProject.getJadeConfiguration().isSkipUserRequestForJadeStart());
		
		// --- JADE port --------------------------------------------
		Integer currPort = this.currProject.getJadeConfiguration().getLocalPort();
		if (currPort==null || currPort==0) {
			currPort = Application.getGlobalInfo().getJadeLocalPort();
			currProject.getJadeConfiguration().setLocalPort(currPort);
		}
		this.getJTextFieldDefaultPort().setText(currPort.toString());
		
		// --- MTP port ---------------------------------------------
		Integer currPortMTP = this.currProject.getJadeConfiguration().getLocalPortMTP();
		if (currPortMTP==null || currPortMTP==0) {
			currPortMTP = Application.getGlobalInfo().getJadeLocalPortMTP();
			this.currProject.getJadeConfiguration().setLocalPortMTP(currPortMTP);
		}
		this.getJTextFieldDefaultPortMTP().setText(currPortMTP.toString());
		
		// --- MTP address creation ---------------------------------
		MTP_Creation mTP_Creation = this.currProject.getJadeConfiguration().getMtpCreation();
		this.getJRadioButtonMtpAutoConfig().setSelected(mTP_Creation==MTP_Creation.ConfiguredByJADE);
		this.getJRadioButtonMtpIP().setSelected(mTP_Creation==MTP_Creation.ConfiguredByIPandPort);
		
		// --- MTP address setting or IP address --------------------
		this.getJTextFieldIPAddress().setText(this.currProject.getJadeConfiguration().getMtpIpAddress());
		this.refreshMTPView();
		
		// --- MTP protocol -------------------------------
		MtpProtocol mtpProtocol = this.currProject.getJadeConfiguration().getMtpProtocol();
		this.getJComboBoxMtpProtocol().removeItemListener(this);
		this.getJComboBoxMtpProtocol().setSelectedProtocol(mtpProtocol);
		this.getJComboBoxMtpProtocol().addItemListener(this);
		
		this.setFileInformationToTextFields();
		
	}

	/**
	 * Refreshes the MTP view.
	 */
	private void refreshMTPView() {
		MTP_Creation mTP_Creation = currProject.getJadeConfiguration().getMtpCreation();
		switch (mTP_Creation) {
		case ConfiguredByJADE:
			this.getJLabelMTPport().setEnabled(false);
			this.getJTextFieldDefaultPortMTP().setEnabled(false);
			this.getJButtonSetPortMTP().setEnabled(false);
			this.getJButtonSetPortMTPDefault().setEnabled(false);
			this.getJLabelIP().setEnabled(false);
			this.getJTextFieldIPAddress().setEnabled(false);
			this.getJButtonIPedit().setEnabled(false);
			this.getJTextFieldIPAddress().setText(PlatformJadeConfig.MTP_IP_AUTO_Config);
			break;

		case ConfiguredByIPandPort:
			this.getJLabelMTPport().setEnabled(true);
			this.getJTextFieldDefaultPortMTP().setEnabled(true);
			this.getJButtonSetPortMTP().setEnabled(true);
			this.getJButtonSetPortMTPDefault().setEnabled(true);
			this.getJLabelIP().setEnabled(true);
			this.getJTextFieldIPAddress().setEnabled(true);
			this.getJButtonIPedit().setEnabled(true);
			break;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable observable, Object updateObject) {
		if (updateObject==Project.CHANGED_JadeConfiguration && this.pauseObserver==false) {
			this.refreshDataView();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		Object trigger = ae.getSource();
		if (trigger==this.getJCheckBoxSkipUserRequestForJadeStart()) {
			// --- Skip the request for the JADE start ---- 
			this.currProject.getJadeConfiguration().setSkipUserRequestForJadeStart(this.getJCheckBoxSkipUserRequestForJadeStart().isSelected());
			
		} else if (trigger==this.getJButtonSetPort()) {
			// --- Open Dialog ------------------
			JadeSetupNewPort newPort = new JadeSetupNewPort(Application.getMainWindow(), currProject.getProjectName(), true, this.currProject.getJadeConfiguration().getLocalPort(), jTextFieldDefaultPort.getLocationOnScreen());
			newPort.setVisible(true);
			// === Go ahead =====================
			if (newPort.isCanceled() == false) {
				Integer oldLocalPort = currProject.getJadeConfiguration().getLocalPort();
				Integer newLocalPort = newPort.getNewLocalPort4Jade();
				if (newLocalPort!=oldLocalPort) {
					// --- Set changes ----------
					currProject.getJadeConfiguration().setLocalPort(newLocalPort);
					jTextFieldDefaultPort.setText(newLocalPort.toString());
				}
			}
			newPort.dispose();
			newPort = null;	
			
			
		} else if (trigger==this.getJButtonSetPortDefault()) {
			this.currProject.getJadeConfiguration().setLocalPort(Application.getGlobalInfo().getJadeLocalPort());
			this.getJTextFieldDefaultPort().setText( Application.getGlobalInfo().getJadeLocalPort().toString() );

		} else if (trigger==this.getJRadioButtonMtpAutoConfig()) {
			// --- Switch to MTP-auto configuration -------
			currProject.getJadeConfiguration().setMtpCreation(MTP_Creation.ConfiguredByJADE);
			this.refreshMTPView();
			
		} else if (trigger==this.getJRadioButtonMtpIP()) {
			// --- Switch to MTP-IP usage -----------------			
			currProject.getJadeConfiguration().setMtpCreation(MTP_Creation.ConfiguredByIPandPort);
			this.refreshMTPView();
			
		} else if (trigger==this.getJButtonIPedit()) {
			NetworkAddresses netAddresses = new NetworkAddresses();
			JPopupMenu popUp = netAddresses.getJPopupMenu4NetworkAddresses(this);
			popUp.show(this.getJTextFieldIPAddress(), 0, this.getJTextFieldIPAddress().getHeight());
			
		} else if (trigger==this.getJButtonSetPortMTP()) {
			// --- Set MTP port ---------------------------
			JadeSetupNewPort newPort = new JadeSetupNewPort(Application.getMainWindow(), this.currProject.getProjectName(), true, this.currProject.getJadeConfiguration().getLocalPortMTP(), this.getJTextFieldDefaultPortMTP().getLocationOnScreen());
			newPort.setVisible(true);
			// === Go ahead =====================
			if (newPort.isCanceled() == false) {
				Integer oldLocalPortMTP = currProject.getJadeConfiguration().getLocalPortMTP();
				Integer newLocalPortMTP = newPort.getNewLocalPort4Jade();
				if (newLocalPortMTP!=oldLocalPortMTP) {
					// --- Set changes ----------
					currProject.getJadeConfiguration().setLocalPortMTP(newLocalPortMTP);
					jTextFieldDefaultPortMTP.setText(newLocalPortMTP.toString());
				}
			}
			newPort.dispose();
			newPort = null;	
			
		} else if (trigger==this.getJButtonSetPortMTPDefault()) {
			// --- Set default MTP port -------------------
			this.currProject.getJadeConfiguration().setLocalPortMTP(7778);
			this.getJTextFieldDefaultPortMTP().setText("7778");
			
		} else if (trigger instanceof JMenuItem) {
			// --- Trigger from JPopoupMenue for the IP-Addresses ---
			JMenuItem menuItem = (JMenuItem) trigger;
			String actCMD = menuItem.getActionCommand();
			currProject.getJadeConfiguration().setMtpIpAddress(actCMD);
			this.getJTextFieldIPAddress().setText(actCMD);
			
		} else if (trigger==this.getJButtonDeleteSecurityStores()){
			// --- Delete the key and trust store settings --------------------
			if (this.currProject.getJadeConfiguration().getKeyStoreFile()!=null || this.currProject.getJadeConfiguration().getTrustStoreFile()!=null) {
				// --- Ask the suer to delete the current settings ------------
				String title = Language.translate("Delete MTP security references?", Language.EN);
				String message = Language.translate("Would you like to delete the KeyStore and TrustStore references?", Language.EN);
				int userAnswer = JOptionPane.showConfirmDialog(Application.getMainWindow(), message, title, JOptionPane.YES_NO_OPTION);
				if (userAnswer==JOptionPane.YES_OPTION) {
					this.currProject.getJadeConfiguration().setKeyStoreFile(null);
					this.currProject.getJadeConfiguration().setKeyStorePassword(null);
					this.currProject.getJadeConfiguration().setTrustStoreFile(null);
					this.currProject.getJadeConfiguration().setTrustStorePassword(null);
				}
			}
			
		} else if (trigger==this.getJButtonEditSecurityStores()){
			// --- Open the HttpsConfigWindow ---------------------------------
			this.editHTTPSsettings(this.getJComboBoxMtpProtocol().getSelectedProtocol());
			
		}
	}
	
	/**
	 * Opens the HttpsCinfogWindow to configure the HTTPS MTP.
	 * @param configuredMtpProtocol the configured MTP protocol
	 */
	private void editHTTPSsettings(MtpProtocol configuredMtpProtocol) {
		
		// --- Get the current paths for key and trust store ------------------
		String keyStoreFilePath = this.currProject.getJadeConfiguration().getKeyStoreFile();
		String trustStoreFilePath = this.currProject.getJadeConfiguration().getTrustStoreFile();
		
		if (keyStoreFilePath==null || trustStoreFilePath==null || new File(keyStoreFilePath).exists()==false || new File(trustStoreFilePath).exists()==false) {
			// --- Case of invalid settings -----------------------------------
			HttpsConfigWindow httpsConfigWindow = new HttpsConfigWindow(Application.getMainWindow());
			httpsConfigWindow.setPreferredDirector(this.currProject.getProjectSecurityFolderFullPath());
			httpsConfigWindow.setVisible(true);
			// - - Wait for the user - - - - - - - - - - - - -
			if (httpsConfigWindow.isCanceled()==false) {
				this.setHttpsConfigWindowSettingsToProject(configuredMtpProtocol, httpsConfigWindow);
			} else {
				// ---- If the Button Cancel is pressed -----------------------
				this.getJComboBoxMtpProtocol().setSelectedProtocol(MtpProtocol.HTTP);
			}
			
		} else {
			// --- In case that the user choose to edit the HTTPS MTP ----------
			File keyStoreFile = new File(keyStoreFilePath);
			File trustStoreFile = new File(trustStoreFilePath);
			String keyStorePassword = this.currProject.getJadeConfiguration().getKeyStorePassword();
			String trustStorePassword = this.currProject.getJadeConfiguration().getTrustStorePassword();
			// --- Open the HttpsConfigWindow ----------------------------------
			HttpsConfigWindow httpsConfigWindow = new HttpsConfigWindow(Application.getMainWindow(), keyStoreFile, keyStorePassword, trustStoreFile, trustStorePassword);
			httpsConfigWindow.setPreferredDirector(this.currProject.getProjectSecurityFolderFullPath());
			httpsConfigWindow.setVisible(true);
			// --- Wait for the user -------------------------------------------
			if (httpsConfigWindow.isCanceled()==false) {
				this.setHttpsConfigWindowSettingsToProject(configuredMtpProtocol, httpsConfigWindow);
			} 
			
		}
	
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	@Override
	public void itemStateChanged(ItemEvent event) {
		
		if (event.getStateChange()==ItemEvent.SELECTED && event.getSource()==this.getJComboBoxMtpProtocol()){
			
			MtpProtocol protocolSelected = this.getJComboBoxMtpProtocol().getSelectedProtocol(); 
			switch (protocolSelected) {
			case HTTP:
				// ---- Switch to HTTP ----------------------------------
				this.currProject.getJadeConfiguration().setMtpProtocol(protocolSelected);
				break;
			case HTTPS:
			case PROXIEDHTTPS:
				// ---- Switch to HTTPS ------------------------------
				this.currProject.getJadeConfiguration().setMtpProtocol(protocolSelected);
				this.editHTTPSsettings(protocolSelected);
				break;
			}
		}
	}
	
	/**
	 * Sets the settings form the HttpsConfigWindow to project and visualization.
	 *
	 * @param configuredMtpProtocol the configured MTP protocol
	 * @param httpsConfigWindow the current instance of the {@link HttpsConfigWindow} 
	 */
	private void setHttpsConfigWindowSettingsToProject(MtpProtocol configuredMtpProtocol, HttpsConfigWindow httpsConfigWindow) {
		
		this.currProject.getJadeConfiguration().setMtpProtocol(configuredMtpProtocol);
		
		this.currProject.getJadeConfiguration().setKeyStoreFile(httpsConfigWindow.getKeyStoreFile().getAbsolutePath());
		this.currProject.getJadeConfiguration().setKeyStorePassword(httpsConfigWindow.getKeyStorePassword());

		this.currProject.getJadeConfiguration().setTrustStoreFile(httpsConfigWindow.getTrustStoreFile().getAbsolutePath());
		this.currProject.getJadeConfiguration().setTrustStorePassword(httpsConfigWindow.getTrustStorePassword());
		
		this.setFileInformationToTextFields();
	}
	
	/**
	 * Sets the file information to text fields.
	 */
	private void setFileInformationToTextFields() {
		
		this.getJTextFieldKeyStoreFile().setText(this.currProject.getJadeConfiguration().getKeyStoreFileInternal());
		this.getJTextFieldKeyStoreFile().setToolTipText(this.currProject.getJadeConfiguration().getKeyStoreFile());
		
		this.getJTextFieldTrustStoreFile().setText(this.currProject.getJadeConfiguration().getTrustStoreFileInternal());
		this.getJTextFieldTrustStoreFile().setToolTipText(this.currProject.getJadeConfiguration().getTrustStoreFile());
	}

}  
