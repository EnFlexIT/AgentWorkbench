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
package agentgui.core.gui.projectwindow;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.gui.components.ClassElement2Display;
import agentgui.core.gui.components.JComboBoxMtpProtocol;
import agentgui.core.gui.components.JListClassSearcher;
import agentgui.core.gui.options.https.HttpsConfigWindow;
import agentgui.core.jade.ClassSearcher;
import agentgui.core.network.NetworkAddresses;
import agentgui.core.project.PlatformJadeConfig;
import agentgui.core.project.PlatformJadeConfig.MTP_Creation;
import agentgui.core.project.Project;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;

/**
 * Represents the JPanel/Tab 'Configuration' - 'JADE-Configuration'
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JadeSetup extends JPanel implements ActionListener, Observer, ItemListener {

	private static final long serialVersionUID = -7016775471452161527L;
	private final static String PathImage = Application.getGlobalInfo().getPathImageIntern();
	private Project currProject = null;
	private HttpsConfigWindow httpsConfigWindow ;

	private JLabel jLabelPort = null;
	private JLabel jLabelPortExplain = null;
	private JTextField jTextFieldDefaultPort = null;
	private JButton jButtonSetPortDefault = null;
	private JPanel jPanelJadeIPandPort = null;
	private JPanel jPanelServiceLists = null;
	private JLabel jLabelServicesChosen = null;
	private JLabel jLabelServicesAvailable = null;
	private JScrollPane jScrollPaneServicesChosen = null;
	private JList<String> jListServicesChosen = null;
	private JListClassSearcher jListServicesAvailable = null;

	private JPanel jPanelServiceButtons = null;
	private JPanel jPanelServiceAvailable = null;
	private JButton jButtonDefaultJadeConfig = null;
	private JButton jButtonServiceAdd = null;
	private JButton jButtonServiceRemove = null;
	private JButton jButtonSetPort = null;
	private JLabel jLabelDummyServices = null;
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
	
	private JSeparator jSeparator1;
	private JSeparator jSeparatorHorizontal;
	private JSeparator jSeparator2;
	
	private JLabel jLabelKeyStore;
	private JLabel jLabelTrustStore;
	private JLabel jLabelMtpProtocol;
	private JTextField jTextFieldKeyStoreFile;
	private JTextField jTextFieldTrustStoreFile;
	
	private JButton jButtonEditMtpProtocol;
	private JComboBoxMtpProtocol jComboBoxMtpProtocol;
	
	private String keyStore;
	private String keyStorePassword;
	private String trustStore;
	private String trustStorePassword;
	private String currentMTP;
	private String action;
	
	/**
	 * Constructor of this class
	 * @param project
	 */
	public JadeSetup(Project project) {
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
		
		jButtonSetPortMTP.setToolTipText(Language.translate("JADE-Port bearbeiten"));
		jButtonSetPortMTPDefault.setToolTipText(Language.translate("Standard verwenden"));
		
		jLabelServicesChosen.setText(Language.translate("Ausgewählte JADE-Services"));
		jLabelServicesAvailable.setText(Language.translate("Verfügbare JADE-Services"));
		
		jButtonDefaultJadeConfig.setToolTipText(Language.translate("Standardkonfiguration verwenden"));
		jButtonServiceAdd.setToolTipText(Language.translate("Service hinzufügen"));
		jButtonServiceRemove.setToolTipText(Language.translate("Service entfernen"));
		
	}
	
	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0};
		gridBagLayout.columnWeights = new double[]{1.0};
		this.setLayout(gridBagLayout);
		this.setSize(950, 500);
		
		GridBagConstraints gbc_jPanelJadeIPandPort = new GridBagConstraints();
		gbc_jPanelJadeIPandPort.fill = GridBagConstraints.VERTICAL;
		gbc_jPanelJadeIPandPort.anchor = GridBagConstraints.WEST;
		gbc_jPanelJadeIPandPort.gridx = 0;
		gbc_jPanelJadeIPandPort.gridy = 0;
		gbc_jPanelJadeIPandPort.insets = new Insets(10, 10, 5, 0);
		
		GridBagConstraints gbc_jSeparatorHorizontal = new GridBagConstraints();
		gbc_jSeparatorHorizontal.fill = GridBagConstraints.HORIZONTAL;
		gbc_jSeparatorHorizontal.insets = new Insets(5, 10, 10, 10);
		gbc_jSeparatorHorizontal.gridx = 0;
		gbc_jSeparatorHorizontal.gridy = 1;

		GridBagConstraints gbc_ServiceLists = new GridBagConstraints();
		gbc_ServiceLists.gridx = 0;
		gbc_ServiceLists.fill = GridBagConstraints.BOTH;
		gbc_ServiceLists.insets = new Insets(0, 10, 5, 10);
		gbc_ServiceLists.weightx = 1.0;
		gbc_ServiceLists.weighty = 1.0;
		gbc_ServiceLists.ipadx = 0;
		gbc_ServiceLists.gridy = 2;
		
		this.add(this.getJPanelJadeIPandPort(), gbc_jPanelJadeIPandPort);
		this.add(this.getJSeparatorHorizontal(), gbc_jSeparatorHorizontal);
		this.add(this.getJPanelServiceLists(), gbc_ServiceLists);
		
		// --- Create the ButtonGroup for the radio buttons ---------
		ButtonGroup bGroup = new ButtonGroup();
		bGroup.add(this.getJRadioButtonMtpAutoConfig());
		bGroup.add(this.getJRadioButtonMtpIP());
		
	}


	/**
	 * This method initializes jPanelPort	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelJadeIPandPort() {
		if (jPanelJadeIPandPort == null) {
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.gridx = 2;
			gridBagConstraints15.insets = new Insets(0, 5, 5, 5);
			gridBagConstraints15.gridy = 0;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 3;
			gridBagConstraints3.insets = new Insets(0, 5, 5, 5);
			gridBagConstraints3.gridy = 0;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.insets = new Insets(0, 5, 5, 5);
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			jPanelJadeIPandPort = new JPanel();
			GridBagLayout gbl_jPanelJadeIPandPort = new GridBagLayout();
			gbl_jPanelJadeIPandPort.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
			gbl_jPanelJadeIPandPort.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
			gbl_jPanelJadeIPandPort.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0};
			gbl_jPanelJadeIPandPort.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
			jPanelJadeIPandPort.setLayout(gbl_jPanelJadeIPandPort);
			jLabelPort = new JLabel();
			GridBagConstraints gbc_jLabelPort = new GridBagConstraints();
			gbc_jLabelPort.anchor = GridBagConstraints.WEST;
			gbc_jLabelPort.insets = new Insets(0, 0, 5, 5);
			gbc_jLabelPort.gridx = 0;
			gbc_jLabelPort.gridy = 0;
			jPanelJadeIPandPort.add(jLabelPort, gbc_jLabelPort);
			jLabelPort.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelPort.setText("Starte JADE über Port-Nr.:");
			jPanelJadeIPandPort.add(getJTextFieldDefaultPort(), gridBagConstraints2);
			jPanelJadeIPandPort.add(getJButtonSetPortDefault(), gridBagConstraints3);
			jPanelJadeIPandPort.add(getJButtonSetPort(), gridBagConstraints15);
			GridBagConstraints gbc_jSeparator1 = new GridBagConstraints();
			gbc_jSeparator1.fill = GridBagConstraints.VERTICAL;
			gbc_jSeparator1.gridheight = 6;
			gbc_jSeparator1.insets = new Insets(0, 10, 5, 10);
			gbc_jSeparator1.gridx = 4;
			gbc_jSeparator1.gridy = 0;
			jPanelJadeIPandPort.add(getJSeparator1(), gbc_jSeparator1);
			GridBagConstraints gbc_jLabaleMTP = new GridBagConstraints();
			gbc_jLabaleMTP.anchor = GridBagConstraints.WEST;
			gbc_jLabaleMTP.insets = new Insets(0, 0, 5, 5);
			gbc_jLabaleMTP.gridx = 5;
			gbc_jLabaleMTP.gridy = 0;
			jPanelJadeIPandPort.add(getJLabelMTP(), gbc_jLabaleMTP);
			GridBagConstraints gbc_jRadioButtonMtpAutoConfig = new GridBagConstraints();
			gbc_jRadioButtonMtpAutoConfig.anchor = GridBagConstraints.WEST;
			gbc_jRadioButtonMtpAutoConfig.insets = new Insets(0, 0, 5, 5);
			gbc_jRadioButtonMtpAutoConfig.gridx = 6;
			gbc_jRadioButtonMtpAutoConfig.gridy = 0;
			jPanelJadeIPandPort.add(getJRadioButtonMtpAutoConfig(), gbc_jRadioButtonMtpAutoConfig);
			GridBagConstraints gbc_jRadioButtonMtpIP = new GridBagConstraints();
			gbc_jRadioButtonMtpIP.anchor = GridBagConstraints.WEST;
			gbc_jRadioButtonMtpIP.insets = new Insets(0, 0, 5, 5);
			gbc_jRadioButtonMtpIP.gridx = 7;
			gbc_jRadioButtonMtpIP.gridy = 0;
			jPanelJadeIPandPort.add(getJRadioButtonMtpIP(), gbc_jRadioButtonMtpIP);
			GridBagConstraints gbc_jButtonIPedit = new GridBagConstraints();
			gbc_jButtonIPedit.anchor = GridBagConstraints.EAST;
			gbc_jButtonIPedit.insets = new Insets(0, 0, 5, 0);
			gbc_jButtonIPedit.gridx = 8;
			gbc_jButtonIPedit.gridy = 0;
			jPanelJadeIPandPort.add(getJButtonIPedit(), gbc_jButtonIPedit);
			GridBagConstraints gbc_jLabelMTPport = new GridBagConstraints();
			gbc_jLabelMTPport.insets = new Insets(0, 0, 5, 5);
			gbc_jLabelMTPport.gridx = 0;
			gbc_jLabelMTPport.gridy = 1;
			jPanelJadeIPandPort.add(getJLabelMTPport(), gbc_jLabelMTPport);
			GridBagConstraints gbc_jTextFieldDefaultPortMTP = new GridBagConstraints();
			gbc_jTextFieldDefaultPortMTP.insets = new Insets(0, 5, 5, 5);
			gbc_jTextFieldDefaultPortMTP.gridx = 1;
			gbc_jTextFieldDefaultPortMTP.gridy = 1;
			jPanelJadeIPandPort.add(getJTextFieldDefaultPortMTP(), gbc_jTextFieldDefaultPortMTP);
			GridBagConstraints gbc_jButtonSetPortMTP = new GridBagConstraints();
			gbc_jButtonSetPortMTP.insets = new Insets(0, 5, 5, 5);
			gbc_jButtonSetPortMTP.gridx = 2;
			gbc_jButtonSetPortMTP.gridy = 1;
			jPanelJadeIPandPort.add(getJButtonSetPortMTP(), gbc_jButtonSetPortMTP);
			GridBagConstraints gbc_jButtonSetPortMTPDefault = new GridBagConstraints();
			gbc_jButtonSetPortMTPDefault.insets = new Insets(0, 5, 5, 5);
			gbc_jButtonSetPortMTPDefault.gridx = 3;
			gbc_jButtonSetPortMTPDefault.gridy = 1;
			jPanelJadeIPandPort.add(getJButtonSetPortMTPDefault(), gbc_jButtonSetPortMTPDefault);
			GridBagConstraints gbc_jLabelIP = new GridBagConstraints();
			gbc_jLabelIP.anchor = GridBagConstraints.WEST;
			gbc_jLabelIP.insets = new Insets(0, 0, 5, 5);
			gbc_jLabelIP.gridx = 5;
			gbc_jLabelIP.gridy = 1;
			jPanelJadeIPandPort.add(getJLabelIP(), gbc_jLabelIP);
			GridBagConstraints gbc_jTextFieldIPAddress = new GridBagConstraints();
			gbc_jTextFieldIPAddress.insets = new Insets(0, 0, 5, 0);
			gbc_jTextFieldIPAddress.fill = GridBagConstraints.HORIZONTAL;
			gbc_jTextFieldIPAddress.gridwidth = 3;
			gbc_jTextFieldIPAddress.gridx = 6;
			gbc_jTextFieldIPAddress.gridy = 1;
			jPanelJadeIPandPort.add(getJTextFieldIPAddress(), gbc_jTextFieldIPAddress);
			GridBagConstraints gbc_jSeparator2 = new GridBagConstraints();
			gbc_jSeparator2.fill = GridBagConstraints.HORIZONTAL;
			gbc_jSeparator2.gridwidth = 4;
			gbc_jSeparator2.insets = new Insets(0, 0, 5, 0);
			gbc_jSeparator2.gridx = 5;
			gbc_jSeparator2.gridy = 2;
			jPanelJadeIPandPort.add(getJSeparator2(), gbc_jSeparator2);
			GridBagConstraints gbc_jLabelMtpProtocol = new GridBagConstraints();
			gbc_jLabelMtpProtocol.anchor = GridBagConstraints.EAST;
			gbc_jLabelMtpProtocol.insets = new Insets(0, 0, 5, 5);
			gbc_jLabelMtpProtocol.gridx = 5;
			gbc_jLabelMtpProtocol.gridy = 3;
			jPanelJadeIPandPort.add(getJLabelMtpProtocol(), gbc_jLabelMtpProtocol);
			GridBagConstraints gbc_comboBox = new GridBagConstraints();
			gbc_comboBox.anchor = GridBagConstraints.WEST;
			gbc_comboBox.insets = new Insets(0, 0, 5, 5);
			gbc_comboBox.gridx = 6;
			gbc_comboBox.gridy = 3;
			jPanelJadeIPandPort.add(getJcomboBoxMtpProtocol(), gbc_comboBox);
			GridBagConstraints gbc_jButtonEditMtpProtocol = new GridBagConstraints();
			gbc_jButtonEditMtpProtocol.insets = new Insets(0, 0, 5, 0);
			gbc_jButtonEditMtpProtocol.gridx = 8;
			gbc_jButtonEditMtpProtocol.gridy = 3;
			jPanelJadeIPandPort.add(getJButtonEditMtpProtocol(), gbc_jButtonEditMtpProtocol);
			
			jLabelPortExplain = new JLabel();
			GridBagConstraints gbc_jLabelPortExplain = new GridBagConstraints();
			gbc_jLabelPortExplain.anchor = GridBagConstraints.WEST;
			gbc_jLabelPortExplain.gridwidth = 4;
			gbc_jLabelPortExplain.insets = new Insets(0, 0, 5, 5);
			gbc_jLabelPortExplain.gridx = 0;
			gbc_jLabelPortExplain.gridy = 4;
			jPanelJadeIPandPort.add(jLabelPortExplain, gbc_jLabelPortExplain);
			jLabelPortExplain.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabelPortExplain.setText("(Falls bereits verwendet, wird versucht den nächst höheren Port zu nutzen)");
			GridBagConstraints gbc_jLabelKeyStore = new GridBagConstraints();
			gbc_jLabelKeyStore.anchor = GridBagConstraints.WEST;
			gbc_jLabelKeyStore.insets = new Insets(0, 0, 5, 5);
			gbc_jLabelKeyStore.gridx = 5;
			gbc_jLabelKeyStore.gridy = 4;
			jPanelJadeIPandPort.add(getJLabelKeyStore(), gbc_jLabelKeyStore);
			GridBagConstraints gbc_jTextFieldKeyStoreFile = new GridBagConstraints();
			gbc_jTextFieldKeyStoreFile.gridwidth = 3;
			gbc_jTextFieldKeyStoreFile.insets = new Insets(0, 0, 5, 0);
			gbc_jTextFieldKeyStoreFile.fill = GridBagConstraints.HORIZONTAL;
			gbc_jTextFieldKeyStoreFile.gridx = 6;
			gbc_jTextFieldKeyStoreFile.gridy = 4;
			jPanelJadeIPandPort.add(getJTextFieldKeyStoreFile(), gbc_jTextFieldKeyStoreFile);
			GridBagConstraints gbc_jLabelTrustStore = new GridBagConstraints();
			gbc_jLabelTrustStore.anchor = GridBagConstraints.WEST;
			gbc_jLabelTrustStore.insets = new Insets(5, 0, 0, 5);
			gbc_jLabelTrustStore.gridx = 5;
			gbc_jLabelTrustStore.gridy = 5;
			jPanelJadeIPandPort.add(getJLabelTrustStore(), gbc_jLabelTrustStore);
			GridBagConstraints gbc_jTextFieldTrustStoreFile = new GridBagConstraints();
			gbc_jTextFieldTrustStoreFile.gridwidth = 3;
			gbc_jTextFieldTrustStoreFile.fill = GridBagConstraints.HORIZONTAL;
			gbc_jTextFieldTrustStoreFile.gridx = 6;
			gbc_jTextFieldTrustStoreFile.gridy = 5;
			jPanelJadeIPandPort.add(getJTextFieldTrustStoreFile(), gbc_jTextFieldTrustStoreFile);
		}
		return jPanelJadeIPandPort;
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
			jButtonSetPort.setIcon(new ImageIcon(getClass().getResource(PathImage + "edit.png")));
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
			jButtonSetPortDefault.setIcon(new ImageIcon(getClass().getResource(PathImage + "MBreset.png")));
			jButtonSetPortDefault.setPreferredSize(new Dimension(45, 26));
			jButtonSetPortDefault.setToolTipText("Standard verwenden");
			jButtonSetPortDefault.setActionCommand("SetPortDefault");
			jButtonSetPortDefault.addActionListener(this);
		}
		return jButtonSetPortDefault;
	}
	private JSeparator getJSeparator1() {
		if (jSeparator1 == null) {
			jSeparator1 = new JSeparator();
			jSeparator1.setOrientation(SwingConstants.VERTICAL);
		}
		return jSeparator1;
	}
	/**
	 * Gets the JLabel mtp.
	 * @return the JLabel mtp
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
	private JLabel getJLabelIP() {
		if (jLabelIP == null) {
			jLabelIP = new JLabel("IP:");
			jLabelIP.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelIP;
	}
	private JTextField getJTextFieldIPAddress() {
		if (jTextFieldIPAddress==null) {
			jTextFieldIPAddress = new JTextField();
			jTextFieldIPAddress.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldIPAddress.setPreferredSize(new Dimension(300, 26));
		}
		return jTextFieldIPAddress;
	}
	private JButton getJButtonIPedit() {
		if (jButtonIPedit == null) {
			jButtonIPedit = new JButton();
			jButtonIPedit.setToolTipText("IP auswählen");
			jButtonIPedit.setPreferredSize(new Dimension(45, 26));
			jButtonIPedit.setIcon(new ImageIcon(getClass().getResource(PathImage + "edit.png")));
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

	/**
	 * Gets the JTextFielddefault port mtp.
	 * @return the JTextField default port mtp
	 */
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
	/**
	 * Gets the JButton set port mtp.
	 * @return the JButton set port mtp
	 */
	private JButton getJButtonSetPortMTP() {
		if (jButtonSetPortMTP == null) {
			jButtonSetPortMTP = new JButton();
			jButtonSetPortMTP.setToolTipText("JADE-Port bearbeiten");
			jButtonSetPortMTP.setPreferredSize(new Dimension(45, 26));
			jButtonSetPortMTP.setIcon(new ImageIcon(getClass().getResource(PathImage + "edit.png")));
			jButtonSetPortMTP.setActionCommand("SetPortMTP");
			jButtonSetPortMTP.addActionListener(this);
		}
		return jButtonSetPortMTP;
	}
	/**
	 * Gets the JButton set port mtp default.
	 * @return the JButton set port mtp default
	 */
	private JButton getJButtonSetPortMTPDefault() {
		if (jButtonSetPortMTPDefault == null) {
			jButtonSetPortMTPDefault = new JButton();
			jButtonSetPortMTPDefault.setToolTipText("Standard verwenden");
			jButtonSetPortMTPDefault.setPreferredSize(new Dimension(45, 26));
			jButtonSetPortMTPDefault.setActionCommand("SetPortDefault");
			jButtonSetPortMTPDefault.setIcon(new ImageIcon(getClass().getResource(PathImage + "MBreset.png")));
			jButtonSetPortMTPDefault.addActionListener(this);
		}
		return jButtonSetPortMTPDefault;
	}
	
	private JSeparator getJSeparatorHorizontal() {
		if (jSeparatorHorizontal == null) {
			jSeparatorHorizontal = new JSeparator();
		}
		return jSeparatorHorizontal;
	}
	
	/**
	 * This method initializes jPanelServiceLists	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelServiceLists() {
		if (jPanelServiceLists == null) {
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 2;
			gridBagConstraints14.fill = GridBagConstraints.BOTH;
			gridBagConstraints14.insets = new Insets(0, 5, 0, 0);
			gridBagConstraints14.weightx = 0.0;
			gridBagConstraints14.weighty = 1.0;
			gridBagConstraints14.gridy = 1;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 2;
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.insets = new Insets(0, 15, 5, 0);
			gridBagConstraints12.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints12.weightx = 1.0;
			gridBagConstraints12.gridy = 0;
			jLabelServicesAvailable = new JLabel();
			jLabelServicesAvailable.setText("Verfügbare JADE-Services");
			jLabelServicesAvailable.setPreferredSize(new Dimension(156, 16));
			jLabelServicesAvailable.setFont(new Font("Dialog", Font.BOLD, 12));
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 0;
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.insets = new Insets(0, 5, 5, 10);
			gridBagConstraints11.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints11.weightx = 1.0;
			gridBagConstraints11.gridy = 0;
			jLabelServicesChosen = new JLabel();
			jLabelServicesChosen.setText("Ausgewählte JADE-Services");
			jLabelServicesChosen.setPreferredSize(new Dimension(156, 16));
			jLabelServicesChosen.setFont(new Font("Dialog", Font.BOLD, 12));
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.insets = new Insets(0, 0, 0, 5);
			gridBagConstraints7.fill = GridBagConstraints.BOTH;
			gridBagConstraints7.gridy = 1;
			gridBagConstraints7.weightx = 0.0;
			gridBagConstraints7.gridx = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.BOTH;
			gridBagConstraints4.weighty = 1.0;
			gridBagConstraints4.insets = new Insets(0, 0, 0, 5);
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.weightx = 0.0;
			jPanelServiceLists = new JPanel();
			jPanelServiceLists.setLayout(new GridBagLayout());
			jPanelServiceLists.add(jLabelServicesChosen, gridBagConstraints11);
			jPanelServiceLists.add(getJScrollPaneServicesChosen(), gridBagConstraints4);
			jPanelServiceLists.add(getJPanelServiceButtons(), gridBagConstraints7);
			jPanelServiceLists.add(jLabelServicesAvailable, gridBagConstraints12);
			jPanelServiceLists.add(getJPanelServiceAvailable(), gridBagConstraints14);
			
		}
		return jPanelServiceLists;
	}

	/**
	 * This method initializes jScrollPaneServicesChosen	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPaneServicesChosen() {
		if (jScrollPaneServicesChosen == null) {
			jScrollPaneServicesChosen = new JScrollPane();
			jScrollPaneServicesChosen.setPreferredSize(new Dimension(60, 150));
			jScrollPaneServicesChosen.setViewportView(getJListServicesChosen());
		}
		return jScrollPaneServicesChosen;
	}


	/**
	 * This method initializes jListServicesChosen	
	 * @return javax.swing.JList	
	 */
	private JList<String> getJListServicesChosen() {
		if (jListServicesChosen == null) {
			jListServicesChosen = new JList<String>();
			jListServicesChosen.setModel(currProject.getJadeConfiguration().getListModelServices());
		}
		return jListServicesChosen;
	}

	/**
	 * This method initializes jPanelServiceAvailable	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelServiceAvailable() {
		if (jPanelServiceAvailable == null) {
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.BOTH;
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 0;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.weighty = 1.0;
			gridBagConstraints6.insets = new Insets(0, 0, 0, 0);
			jPanelServiceAvailable = new JPanel();
			jPanelServiceAvailable.setLayout(new GridBagLayout());
			jPanelServiceAvailable.setPreferredSize(new Dimension(60, 150));
			jPanelServiceAvailable.add(getJListServicesAvailable(), gridBagConstraints6);
		}
		return jPanelServiceAvailable;
	}
	
	/**
	 * This method initializes jListServicesAvailable	
	 * @return javax.swing.JList	
	 */
	private JListClassSearcher getJListServicesAvailable() {
		if (jListServicesAvailable == null) {
			jListServicesAvailable = new JListClassSearcher(ClassSearcher.CLASSES_BASESERVICE);
			//jListServicesAvailable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		}
		return jListServicesAvailable;
	}

	/**
	 * This method initializes jPanelServiceButtons	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelServiceButtons() {
		if (jPanelServiceButtons == null) {
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.anchor = GridBagConstraints.CENTER;
			gridBagConstraints13.insets = new Insets(0, 0, 30, 0);
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.gridy = 0;
			gridBagConstraints13.fill = GridBagConstraints.NONE;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints8.weighty = 1.0;
			gridBagConstraints8.gridy = 3;
			jLabelDummyServices = new JLabel();
			jLabelDummyServices.setText(" ");
			jLabelDummyServices.setPreferredSize(new Dimension(16, 16));
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.insets = new Insets(0, 0, 10, 0);
			gridBagConstraints10.gridy = 2;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.insets = new Insets(0, 0, 10, 0);
			gridBagConstraints9.gridy = 1;
			jPanelServiceButtons = new JPanel();
			jPanelServiceButtons.setLayout(new GridBagLayout());
			jPanelServiceButtons.add(getJButtonAdd(), gridBagConstraints9);
			jPanelServiceButtons.add(getJButtonRemove(), gridBagConstraints10);
			jPanelServiceButtons.add(jLabelDummyServices, gridBagConstraints8);
			jPanelServiceButtons.add(getJButtonDefaultJadeConfig(), gridBagConstraints13);
		}
		return jPanelServiceButtons;
	}

	/**
	 * This method initializes jButtonDefaultJadeConfig	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonDefaultJadeConfig() {
		if (jButtonDefaultJadeConfig == null) {
			jButtonDefaultJadeConfig = new JButton();
			jButtonDefaultJadeConfig.setIcon(new ImageIcon(getClass().getResource(PathImage + "MBreset.png")));
			jButtonDefaultJadeConfig.setPreferredSize(new Dimension(45, 26));
			jButtonDefaultJadeConfig.setToolTipText("Standardkonfiguration verwenden");
			jButtonDefaultJadeConfig.addActionListener(this);
		}
		return jButtonDefaultJadeConfig;
	}
	/**
	 * This method initializes jButtonAdd	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonAdd() {
		if (jButtonServiceAdd == null) {
			jButtonServiceAdd = new JButton();
			jButtonServiceAdd.setIcon(new ImageIcon(getClass().getResource(PathImage + "ArrowLeft.png")));
			jButtonServiceAdd.setPreferredSize(new Dimension(45, 27));
			jButtonServiceAdd.setToolTipText("Service hinzufügen");
			jButtonServiceAdd.addActionListener(this);
		}
		return jButtonServiceAdd;
	}
	/**
	 * This method initializes jButtonRemove	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonRemove() {
		if (jButtonServiceRemove == null) {
			jButtonServiceRemove = new JButton();
			jButtonServiceRemove.setIcon(new ImageIcon(getClass().getResource(PathImage + "ArrowRight.png")));
			jButtonServiceRemove.setPreferredSize(new Dimension(45, 27));
			jButtonServiceRemove.setToolTipText("Service entfernen");
			jButtonServiceRemove.addActionListener(this);
		}
		return jButtonServiceRemove;
	}
	/**
	 * This method initializes jSeparator2	
	 * @return javax.swing.JSeparator	
	 */
	private JSeparator getJSeparator2() {
		if (jSeparator2 == null) {
			jSeparator2 = new JSeparator();
		}
		return jSeparator2;
	}
	/**
	 * This method initializes jLabelKeyStore	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getJLabelKeyStore() {
		if (jLabelKeyStore == null) {
			jLabelKeyStore = new JLabel("KeyStore:");
			jLabelKeyStore.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelKeyStore;
	}
	/**
	 * This method initializes jLabelTrustStore	
	 * @return javax.swing.JLabel	
	 */
	private JLabel getJLabelTrustStore() {
		if (jLabelTrustStore == null) {
			jLabelTrustStore = new JLabel("TrustStore:");
			jLabelTrustStore.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelTrustStore;
	}
	/**
	 * This method initializes jTextFieldKeyStoreFile	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldKeyStoreFile() {
		if (jTextFieldKeyStoreFile == null) {
			jTextFieldKeyStoreFile = new JTextField();
			jTextFieldKeyStoreFile.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldKeyStoreFile.setPreferredSize(new Dimension(300, 26));
		}
		return jTextFieldKeyStoreFile;
	}
	/**
	 * This method initializes jTextFieldTrustStoreFile	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldTrustStoreFile() {
		if (jTextFieldTrustStoreFile == null) {
			jTextFieldTrustStoreFile = new JTextField();
			jTextFieldTrustStoreFile.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextFieldTrustStoreFile.setPreferredSize(new Dimension(300, 26));
		}
		return jTextFieldTrustStoreFile;
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
	/**
	 * This method initializes jButtonEditMtpProtocol	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonEditMtpProtocol() {
		if (jButtonEditMtpProtocol == null) {
			jButtonEditMtpProtocol = new JButton();
			jButtonEditMtpProtocol.setIcon(new ImageIcon(getClass().getResource(PathImage + "edit.png")));
			jButtonEditMtpProtocol.setPreferredSize(new Dimension(45, 26));
			jButtonEditMtpProtocol.addActionListener(this);
		}
		return jButtonEditMtpProtocol;
	}
	/**
	 * This method initializes jComboBoxMtpProtocol	
	 * @return JComboBoxMtpProtocol	
	 */
	private JComboBoxMtpProtocol getJcomboBoxMtpProtocol(){
		if( jComboBoxMtpProtocol == null){
			jComboBoxMtpProtocol = new JComboBoxMtpProtocol();
			jComboBoxMtpProtocol.setPreferredSize(new Dimension(80, 26));
			jComboBoxMtpProtocol.addItemListener(this);
		}
		return jComboBoxMtpProtocol;
	}
	/**
	 * This method initializes keyStore.
	 * @return keyStore
	 */
	private String getKeyStore() {
		return keyStore;
	}
	/**
	 * Sets keyStore.
	 * @param keyStore
	 */
	private void setKeyStore(String keyStore) {
		this.keyStore = keyStore;
	}
	/**
	 * This method initializes keyStorePassword.
	 * @return keyStorePassword
	 */
	private String getKeyStorePassword() {
		return keyStorePassword;
	}
	/**
	 * Sets keyStorePassword.
	 * @param keyStorePassword
	 */
	private void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}
	/**
	 * This method initializes trustStore.
	 * @return trustStore
	 */
	private String getTrustStore() {
		return trustStore;
	}
	/**
	 * Sets trustStore.
	 * @param trustStore
	 */
	private void setTrustStore(String trustStore) {
		this.trustStore = trustStore;
	}
	/**
	 * This method initializes trustStorePassword.
	 * @return trustStorePassword
	 */
	private String getTrustStorePassword() {
		return trustStorePassword;
	}
	/**
	 * Sets trustStorePasswords.
	 * @param trustStorePassword
	 */
	private void setTrustStorePassword(String trustStorePassword) {
		this.trustStorePassword = trustStorePassword;
	}
	/**
	 * Sets JLabels and JTextFields visible true in case choosing HTTPS MTP.
	 */
	private void setEnabledTrue(){
		this.getJTextFieldKeyStoreFile().setEnabled(true);
		this.getJTextFieldTrustStoreFile().setEnabled(true);
		this.getJButtonEditMtpProtocol().setEnabled(true);
	}
	/**
	 * Sets JLabels and JTextFields visible false in case choosing HTTP MTP.
	 */
	private void setEnabledFalse(){
		this.getJTextFieldKeyStoreFile().setEnabled(false);
		this.getJTextFieldTrustStoreFile().setEnabled(false);
		this.getJButtonEditMtpProtocol().setEnabled(false);
	}
	/**
	 * Opens the HttpsCinfogWindow to configure the HTTPS MTP
	 */
	private void editHTTPSsettings() {
		if (this.action == "BUTTON") {
			// --- In case that the user choose to edit the HTTPS MTP ----------
			// --- Open the HttpsConfigWindow ----------------------------------
			httpsConfigWindow = new HttpsConfigWindow(getKeyStore(), getKeyStorePassword(), getTrustStore(),
					getTrustStorePassword());
			// --- Wait for the user -------------------------------------------
			if (httpsConfigWindow.isCanceled() == false) {
				// ---- Return the KeyStore and TrustStore chosen by the user --
				this.setKeyStore(httpsConfigWindow.getKeyStorefilepath());
				this.setTrustStore(httpsConfigWindow.getTrustStorefilepath());
				this.setKeyStorePassword(httpsConfigWindow.getKeyStorePassword());
				this.setTrustStorePassword(httpsConfigWindow.getTrustStorePassword());
				this.getJTextFieldKeyStoreFile().setText(this.getKeyStore());
				this.getJTextFieldTrustStoreFile().setText(this.getTrustStore());
			} 
		} else if (this.action == "COMBO") {
			// --- In case that the user choose to configure new HTTPS MTP ------
			httpsConfigWindow = new HttpsConfigWindow();
			// - - Wait for the user - - - - - - - - - - - - -
			if (httpsConfigWindow.isCanceled() == false) {
				// ---- Return the KeyStore and TrustStore chosen by the user ---
				this.setKeyStore(httpsConfigWindow.getKeyStorefilepath());
				this.setTrustStore(httpsConfigWindow.getTrustStorefilepath());
				this.setKeyStorePassword(httpsConfigWindow.getKeyStorePassword());
				this.setTrustStorePassword(httpsConfigWindow.getTrustStorePassword());
				this.getJTextFieldKeyStoreFile().setText(this.getKeyStore());
				this.getJTextFieldTrustStoreFile().setText(this.getTrustStore());
			} else {
				// ---- If the Button Cancel is pressed -------------------------
				getJcomboBoxMtpProtocol().setSelectedProtocol("HTTP");
				this.setEnabledFalse();
			}
		}
	}

	/**
	 * This method can be called in order to refresh the view 
	 */
	private void refreshDataView() {
		
		// --- JADE port ------------------------
		Integer currPort = currProject.getJadeConfiguration().getLocalPort();
		if (currPort==null || currPort==0) {
			currPort = Application.getGlobalInfo().getJadeLocalPort();
			currProject.getJadeConfiguration().setLocalPort(currPort);
		}
		jTextFieldDefaultPort.setText(currPort.toString());
		
		// --- MTP address creation -------------
		MTP_Creation mTP_Creation = currProject.getJadeConfiguration().getMtpCreation();
		switch (mTP_Creation) {
		case ConfiguredByJADE:
			this.getJRadioButtonMtpAutoConfig().setSelected(true);
			this.getJRadioButtonMtpIP().setSelected(false);
			break;

		case ConfiguredByIPandPort:
			this.getJRadioButtonMtpAutoConfig().setSelected(false);
			this.getJRadioButtonMtpIP().setSelected(true);
			break;
		}
		this.getJTextFieldIPAddress().setText(currProject.getJadeConfiguration().getMtpIpAddress());
		this.refreshMTPView();
		
		// --- MTP port -------------------------	
		Integer currPortMTP = currProject.getJadeConfiguration().getLocalPortMTP();
		if (currPortMTP==null || currPortMTP==0) {
			currPortMTP = Application.getGlobalInfo().getJadeLocalPortMTP();
			currProject.getJadeConfiguration().setLocalPortMTP(currPortMTP);
		}
		this.getJTextFieldDefaultPortMTP().setText(currPortMTP.toString());

		// --- Model for the services used ------
		this.jListServicesChosen.setModel(currProject.getJadeConfiguration().getListModelServices());
		
		String mtpProtocol = currProject.getJadeConfiguration().getMtpProtocol();
		if (mtpProtocol.equals("HTTPS")) {
			this.getJcomboBoxMtpProtocol().setSelectedProtocol("HTTPS");
			this.setEnabledTrue();
			this.setKeyStore(currProject.getJadeConfiguration().getKeyStoreFile());
			this.setTrustStore(currProject.getJadeConfiguration().getTrustStoreFile());
			this.setKeyStorePassword(currProject.getJadeConfiguration().getKeyStorePassword());
			this.setTrustStorePassword(currProject.getJadeConfiguration().getTrustStorePassword());
			this.getJTextFieldKeyStoreFile().setText(this.getKeyStore());
			this.getJTextFieldTrustStoreFile().setText(this.getTrustStore());
			this.currentMTP = "HTTPS";
		} else {
			this.getJcomboBoxMtpProtocol().setSelectedProtocol("HTTP");
			this.setEnabledFalse();
			this.currentMTP = "HTTP";
		}
		
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
	
	@Override
	public void update(Observable observable, Object updateObject) {
		
		if (updateObject==Project.CHANGED_JadeConfiguration) {
			this.refreshDataView();
		} else if (updateObject == Project.CHANGED_ProjectResources ) {
			this.getJListServicesAvailable().reSetListModel();
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		Object trigger = ae.getSource();
		if (trigger==jButtonSetPort) {
			
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
			
			
		} else if (trigger==jButtonSetPortDefault) {
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
			
		} else if (trigger==jButtonServiceAdd) {
			if (jListServicesAvailable.getSelectedValue()!=null) {
				Object[] selections = jListServicesAvailable.getSelectedValuesList().toArray();
				for (int i = 0; i < selections.length; i++) {
					ClassElement2Display serviceElement = (ClassElement2Display) selections[i];
					currProject.getJadeConfiguration().addService(serviceElement.toString());	
				}
			}
			
		} else if (trigger==jButtonServiceRemove) {
			if (jListServicesChosen.getSelectedValue()!=null) {
				List<String> selections = jListServicesChosen.getSelectedValuesList();
				for (int i = 0; i < selections.size(); i++) {
					String serviceReference = (String) selections.get(i);
					currProject.getJadeConfiguration().removeService(serviceReference);
				}
			}
			
		} else if (trigger==jButtonDefaultJadeConfig) {
			
			// --- Get the default profile configuration ------------
			PlatformJadeConfig defaultConfig = Application.getGlobalInfo().getJadeDefaultPlatformConfig();

			// --- Clean current profile configuration --------------
			PlatformJadeConfig currConfig = currProject.getJadeConfiguration();
			
			// --- Set the current model to the default one ---------
			currConfig.setLocalPort(defaultConfig.getLocalPort());
			currConfig.setMtpCreation(defaultConfig.getMtpCreation());
			currConfig.setMtpIpAddress(defaultConfig.getMtpIpAddress());
			currConfig.setLocalPortMTP(defaultConfig.getLocalPortMTP());
			
			// --- Reset services -----------------------------------
			currConfig.removeAllServices();
			DefaultListModel<String> delimo = defaultConfig.getListModelServices();
			for (int i = 0; i < delimo.size(); i++) {
				String serviceRef = (String) delimo.get(i);
				currConfig.addService(serviceRef);
			}
			this.refreshDataView();
		
		} else if (trigger instanceof JMenuItem) {
			// --- Trigger from JPopoupMenue for the IP-Addresses ---
			JMenuItem menuItem = (JMenuItem) trigger;
			String actCMD = menuItem.getActionCommand();
			currProject.getJadeConfiguration().setMtpIpAddress(actCMD);
			this.getJTextFieldIPAddress().setText(actCMD);
		}else if (trigger == jButtonEditMtpProtocol){
			// --- Open the HttpsConfigWindow ---------------------------------
			this.action = "BUTTON";
			editHTTPSsettings();
		}
		
	}
	@Override
	public void itemStateChanged(ItemEvent event) {
		if ( event.getSource() == this.getJcomboBoxMtpProtocol()){
			this.action = "COMBO";
			if (this.currentMTP == "HTTPS") {
				// ---- switch from HTTPS to HTTP ----------------------------------
				this.setEnabledFalse();
				this.currProject.getJadeConfiguration().setMtpProtocol("HTTP");
				this.currentMTP = "HTTP";
			} else if (this.currentMTP == "HTTP") {
				// ---- switch between HTTP and HTTPS ------------------------------
				if (event.getStateChange() == ItemEvent.SELECTED) {
					Object item = event.getItem();
					if (item.equals("HTTPS")) {
						// ---- If the user choose HTTPS ---------------------------
						this.setEnabledTrue();
						this.editHTTPSsettings();
						this.currProject.getJadeConfiguration().setMtpProtocol("HTTPS");
					} else {
						// ---- If the user choose HTTP ---------------------------
						this.setEnabledFalse();
						this.currProject.getJadeConfiguration().setMtpProtocol("HTTP");
					}
				}
			}
		}
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
