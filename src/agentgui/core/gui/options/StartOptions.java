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
package agentgui.core.gui.options;

import jade.core.Agent;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.config.GlobalInfo.DeviceSystemExecutionMode;
import agentgui.core.config.GlobalInfo.EmbeddedSystemAgentVisualisation;
import agentgui.core.config.GlobalInfo.ExecutionMode;
import agentgui.core.gui.ClassSelector;
import agentgui.core.gui.components.JListClassSearcher;
import agentgui.core.project.Project;

/**
 * On this JPanel the starting options of AgentGUI can be set.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class StartOptions extends AbstractOptionTab implements ActionListener {

	private static final long serialVersionUID = -5837814050254569584L;
	
	private GlobalInfo globalInfo = Application.getGlobalInfo();  //  @jve:decl-index=0:
	private String pathImage = globalInfo.PathImageIntern();
	private OptionDialog optionDialog = null;
	
	private GridBagConstraints gridBagConstraints4Configuration = null;  //  @jve:decl-index=0:
	
	private JPanel jPanelAppNServer = null;
	private JPanel jPanelEmbedded = null;  //  @jve:decl-index=0:visual-constraint="20,453"
	
	private JPanel jPanelTop = null;
	private JPanel jPanelMiddle = null;
	private JPanel jPanelBottom = null;
	private JPanel jPanelDummy = null;
	
	private JRadioButton jRadioButtonRunAsApplication = null;
	private JRadioButton jRadioButtonRunAsServer = null;
	private JRadioButton jRadioButtonRunAsEmbeddedSystemAgent = null;
	private JCheckBox jCheckBoxAutoStart = null;
	
	private JLabel jLabelServerHeader = null;
	private JLabel jLabelRunsAs = null;
	private JLabel jLabelJadeConfig = null;
	private JLabel jLabelMasterURL = null;
	private JLabel jLabelMasterPort = null;
	private JLabel jLabelMasterPort4MTP = null;
	private JLabel jLabelPort4MTP = null;
	private JLabel jLabelPort = null;
	
	private JLabel jLabelDummyTop = null;
	private JLabel jLabelDBtitle = null;
	private JLabel jLabelDBHost = null;
	private JLabel jLabelDB = null;
	private JLabel jLabelDBUser = null;
	private JLabel jLabelDBpswd = null;
	
	private JTextField jTextFieldMasterURL = null;
	private JTextField jTextFieldMasterPort = null;
	private JTextField jTextFieldMasterPort4MTP = null;

	private JTextField jTextFieldDBHost = null;
	private JTextField jTextFieldDB = null;
	private JTextField jTextFieldDBUser = null;
	private JTextField jTextFieldDBPswd = null;

	private JButton jButtonApply = null;
	private JButton jButtonUpdateSiteDefault = null;
	
	private ExecutionMode executionModeOld = Application.getGlobalInfo().getExecutionMode();
	private ExecutionMode executionModeNew = Application.getGlobalInfo().getExecutionMode();

	// --- Elements for the Embedded System Agent --------
	private Project esaProjectSelected = null;  //  @jve:decl-index=0:
	
	private JLabel jLabelEmbeddedHeader = null;
	private JLabel jLabelProject = null;
	private JLabel jLabelProjectHeader = null;
	private JComboBox jComboBoxProjectSelector = null;
	
	private JPanel jPanelExecution = null;
	private JLabel jLabelExecuteAs = null;
	private JRadioButton jRadioButtonExecuteAsServerService = null;
	private JRadioButton jRadioButtonExecuteAsDeviceAgent = null;

	private JLabel jLabelSetupHeader = null;
	private JLabel jLabelSetup = null;
	private JComboBox jComboBoxSetupSelector = null;

	private JLabel jLabelAgentHeader = null;
	private JLabel jLabelAgent = null;
	private JTextField jTextFieldAgentClass = null;
	private JButton jButtonSelectAgentClass = null;
	private ClassSelector esaClassSelector = null;
	
	private JLabel jLabelVisConfig = null;
	private JRadioButton jRadioButtonVisNon = null;
	private JRadioButton jRadioButtonVisTrayIcon = null;

	
	/**
	 * This is the Constructor
	 */
	public StartOptions(OptionDialog optionDialog) {
		super(optionDialog);
		
		this.optionDialog = optionDialog;
		this.initialize();
		this.setGlobalData2Form();
		
		// --- Übersetzungen einstellen ----------------------------------------
		jLabelRunsAs.setText(Language.translate("Starte Agent.GUI als:"));
		
		jRadioButtonRunAsApplication.setText(Language.translate("Anwendung"));
		jRadioButtonRunAsServer.setText(Language.translate("Hintergrundsystem (Master / Slave)"));
		jRadioButtonRunAsEmbeddedSystemAgent.setText(Language.translate("Dienst / Emedded System Agent"));
		
		jLabelServerHeader.setText(Application.getGlobalInfo().getApplicationTitle() + " " + Language.translate("Hintergrundsystem - Konfiguration"));
		jCheckBoxAutoStart.setText(" " + Language.translate("Hintergrundsystem beim Programmstart automatisch initialisieren"));
		jLabelJadeConfig.setText(Application.getGlobalInfo().getApplicationTitle() + " " + Language.translate("Hauptserver (server.master)") );
		jLabelDBtitle.setText(Language.translate("MySQL-Datenbank für den Hauptserver (server.master)"));
		jButtonApply.setText(Language.translate("Anwenden"));
	}

	/* (non-Javadoc)
	 * @see agentgui.core.gui.options.AbstractOptionTab#getTitleAddition()
	 */
	@Override
	public String getTitle() {
		return Language.translate("Programmstart");
	}
	/* (non-Javadoc)
	 * @see agentgui.core.gui.options.AbstractOptionTab#getTabToolTipText()
	 */
	@Override
	public String getTabToolTipText() {
		return Language.translate("Programmstart");
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints30 = new GridBagConstraints();
		gridBagConstraints30.gridx = 0;
		gridBagConstraints30.fill = GridBagConstraints.BOTH;
		gridBagConstraints30.weightx = 1.0;
		gridBagConstraints30.weighty = 1.0;
		gridBagConstraints30.insets = new Insets(0, 20, 5, 20);
		gridBagConstraints30.gridy = 2;
		
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridx = 0;
		gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints21.insets = new Insets(20, 20, 5, 20);
		gridBagConstraints21.anchor = GridBagConstraints.WEST;
		gridBagConstraints21.weightx = 1.0;
		gridBagConstraints21.gridy = 0;
		
		gridBagConstraints4Configuration = new GridBagConstraints();
		gridBagConstraints4Configuration.gridx = 0;
		gridBagConstraints4Configuration.weightx = 0.0;
		gridBagConstraints4Configuration.anchor = GridBagConstraints.WEST;
		gridBagConstraints4Configuration.insets = new Insets(10, 20, 0, 110);
		gridBagConstraints4Configuration.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints4Configuration.gridy = 1;
		
		jLabelRunsAs = new JLabel();
		jLabelRunsAs.setText("Starte Agent.GUI als:");
		jLabelRunsAs.setFont(new Font("Dialog", Font.BOLD, 12));

		this.setSize(680, 440);
		this.setLayout(new GridBagLayout());
		this.add(getJPanelTop(), gridBagConstraints21);
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		this.add(getJPanelAppNServer(), gridBagConstraints4Configuration);
		this.add(getJPanelSaveOptions(), gridBagConstraints30);
	}

	/**
	 * This method initializes jRadioButtonRunAsApplication	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadioButtonRunAsApplication() {
		if (jRadioButtonRunAsApplication == null) {
			jRadioButtonRunAsApplication = new JRadioButton();
			jRadioButtonRunAsApplication.setText("Anwendung");
			jRadioButtonRunAsApplication.setSelected(true);
			jRadioButtonRunAsApplication.setPreferredSize(new Dimension(250, 24));
			jRadioButtonRunAsApplication.setActionCommand("runAsApplication");
			jRadioButtonRunAsApplication.addActionListener(this);
		}
		return jRadioButtonRunAsApplication;
	}
	/**
	 * This method initializes jRadioButtonRunAsServer	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadioButtonRunAsServer() {
		if (jRadioButtonRunAsServer == null) {
			jRadioButtonRunAsServer = new JRadioButton();
			jRadioButtonRunAsServer.setPreferredSize(new Dimension(300, 24));
			jRadioButtonRunAsServer.setText("Hintergrundsystem (Master / Slave)");
			jRadioButtonRunAsServer.setActionCommand("runAsServer");
			jRadioButtonRunAsServer.addActionListener(this);
		}
		return jRadioButtonRunAsServer;
	}
	/**
	 * This method initializes jRadioButtonRunAsDevice	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadioButtonRunAsDevice() {
		if (jRadioButtonRunAsEmbeddedSystemAgent == null) {
			jRadioButtonRunAsEmbeddedSystemAgent = new JRadioButton();
			jRadioButtonRunAsEmbeddedSystemAgent.setPreferredSize(new Dimension(300, 24));
			jRadioButtonRunAsEmbeddedSystemAgent.setText("Dienst / Emedded System Agent");
			jRadioButtonRunAsEmbeddedSystemAgent.setActionCommand("runAsEmbeddedSystemAgent");
			jRadioButtonRunAsEmbeddedSystemAgent.addActionListener(this);
		}
		return jRadioButtonRunAsEmbeddedSystemAgent;
	}
	
	/**
	 * This method initializes jPanelTop	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelTop() {
		if (jPanelTop == null) {
			GridBagConstraints gridBagConstraints28 = new GridBagConstraints();
			gridBagConstraints28.gridx = 2;
			gridBagConstraints28.anchor = GridBagConstraints.WEST;
			gridBagConstraints28.insets = new Insets(2, 10, 0, 0);
			gridBagConstraints28.gridwidth = 1;
			gridBagConstraints28.gridy = 2;
			GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
			gridBagConstraints27.gridx = 3;
			gridBagConstraints27.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints27.weightx = 1.0;
			gridBagConstraints27.gridwidth = 2;
			gridBagConstraints27.gridheight = 0;
			gridBagConstraints27.gridy = 2;
			
			GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
			gridBagConstraints26.insets = new Insets(0, 2, 0, 0);
			gridBagConstraints26.gridy = 0;
			gridBagConstraints26.gridx = 5;
			GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
			gridBagConstraints25.anchor = GridBagConstraints.EAST;
			gridBagConstraints25.gridx = 4;
			gridBagConstraints25.gridy = 0;
			gridBagConstraints25.insets = new Insets(0, 20, 0, 0);
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.anchor = GridBagConstraints.WEST;
			gridBagConstraints18.gridx = 1;
			gridBagConstraints18.gridy = 0;
			gridBagConstraints18.insets = new Insets(10, 20, 0, 10);
			GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
			gridBagConstraints24.insets = new Insets(2, 10, 0, 0);
			gridBagConstraints24.gridy = 1;
			gridBagConstraints24.ipadx = 0;
			gridBagConstraints24.anchor = GridBagConstraints.WEST;
			gridBagConstraints24.gridx = 2;
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints23.gridy = 0;
			gridBagConstraints23.ipadx = 0;
			gridBagConstraints23.anchor = GridBagConstraints.WEST;
			gridBagConstraints23.gridx = 2;
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints20.gridy = 0;
			gridBagConstraints20.ipadx = 0;
			gridBagConstraints20.anchor = GridBagConstraints.WEST;
			gridBagConstraints20.gridx = 0;
			
			jLabelDummyTop = new JLabel();
			jLabelDummyTop.setText(" ");
			
			jPanelTop = new JPanel();
			jPanelTop.setLayout(new GridBagLayout());
			jPanelTop.add(jLabelRunsAs, gridBagConstraints20);
			jPanelTop.add(getJRadioButtonRunAsApplication(), gridBagConstraints23);
			jPanelTop.add(getJRadioButtonRunAsServer(), gridBagConstraints24);
			jPanelTop.add(getJButtonApply(), gridBagConstraints25);
			jPanelTop.add(getJButtonUpdateSiteDefault(), gridBagConstraints26);
			jPanelTop.add(jLabelDummyTop, gridBagConstraints27);
			jPanelTop.add(getJRadioButtonRunAsDevice(), gridBagConstraints28);
			
			ButtonGroup runAsGroup = new ButtonGroup();
			runAsGroup.add(jRadioButtonRunAsApplication);
			runAsGroup.add(jRadioButtonRunAsServer);
			runAsGroup.add(jRadioButtonRunAsEmbeddedSystemAgent);
			
		}
		return jPanelTop;
	}

	/**
	 * This method initializes jPanelAppNServer	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelAppNServer() {
		if (jPanelAppNServer == null) {
			GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
			gridBagConstraints19.fill = GridBagConstraints.BOTH;
			gridBagConstraints19.gridx = 0;
			gridBagConstraints19.gridy = 2;
			gridBagConstraints19.ipadx = 0;
			gridBagConstraints19.weightx = 0.0;
			gridBagConstraints19.weighty = 1.0;
			gridBagConstraints19.insets = new Insets(0, 20, 20, 20);
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.anchor = GridBagConstraints.WEST;
			gridBagConstraints7.insets = new Insets(0, 0, 10, 10);
			gridBagConstraints7.gridx = 0;
			gridBagConstraints7.gridy = 1;
			gridBagConstraints7.weightx = 0.0;
			gridBagConstraints7.weighty = 0.0;
			gridBagConstraints7.fill = GridBagConstraints.NONE;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.anchor = GridBagConstraints.WEST;
			gridBagConstraints3.insets = new Insets(0, 0, 10, 10);
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.weighty = 0.0;
			gridBagConstraints3.fill = GridBagConstraints.NONE;
			
			jPanelAppNServer = new JPanel();
			jPanelAppNServer.setLayout(new GridBagLayout());
			jPanelAppNServer.add(getJPanelMiddle(), gridBagConstraints3);
			jPanelAppNServer.add(getJPanelBottom(), gridBagConstraints7);
		}
		return jPanelAppNServer;
	}
	
	/**
	 * This method initializes jPanelMiddle	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelMiddle() {
		if (jPanelMiddle == null) {
			GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
			gridBagConstraints41.gridx = 2;
			gridBagConstraints41.anchor = GridBagConstraints.WEST;
			gridBagConstraints41.insets = new Insets(5, 10, 0, 0);
			gridBagConstraints41.gridy = 5;
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 2;
			gridBagConstraints31.insets = new Insets(5, 10, 0, 0);
			gridBagConstraints31.anchor = GridBagConstraints.WEST;
			gridBagConstraints31.gridy = 6;
			GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
			gridBagConstraints22.gridx = 0;
			gridBagConstraints22.anchor = GridBagConstraints.WEST;
			gridBagConstraints22.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints22.gridy = 6;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints11.gridy = 6;
			gridBagConstraints11.weightx = 0.0;
			gridBagConstraints11.anchor = GridBagConstraints.WEST;
			gridBagConstraints11.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints11.gridx = 1;
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints6.gridy = 5;
			gridBagConstraints6.weightx = 0.0;
			gridBagConstraints6.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints6.anchor = GridBagConstraints.WEST;
			gridBagConstraints6.gridx = 1;
			GridBagConstraints gridBagConstraints51 = new GridBagConstraints();
			gridBagConstraints51.fill = GridBagConstraints.NONE;
			gridBagConstraints51.gridy = 4;
			gridBagConstraints51.weightx = 1.0;
			gridBagConstraints51.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints51.anchor = GridBagConstraints.WEST;
			gridBagConstraints51.gridwidth = 2;
			gridBagConstraints51.gridx = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints4.gridy = 5;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridy = 4;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints1.gridwidth = 3;
			gridBagConstraints1.gridy = 3;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new Insets(5, 0, 0, 10);
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridwidth = 3;
			gridBagConstraints.gridy = 8;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.insets = new Insets(15, 0, 0, 10);
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.gridwidth = 3;
			gridBagConstraints5.gridy = 7;

			jLabelPort = new JLabel();
			jLabelPort.setText("1099 = \"myServer:1099/JADE\"");
			jLabelPort.setPreferredSize(new Dimension(220, 16));
			
			jLabelPort4MTP = new JLabel();
			jLabelPort4MTP.setText("7778 = \"http://myServer:7778/acc\"");
			jLabelPort4MTP.setPreferredSize(new Dimension(220, 16));
			
			jLabelMasterPort4MTP = new JLabel();
			jLabelMasterPort4MTP.setText("Port-MTP");
			jLabelMasterPort4MTP.setPreferredSize(new Dimension(55, 16));
			
			jLabelMasterPort = new JLabel();
			jLabelMasterPort.setText("Port");
			
			jLabelMasterURL = new JLabel();
			jLabelMasterURL.setText("URL / IP");
			
			jLabelJadeConfig = new JLabel();
			jLabelJadeConfig.setText("Agent.GUI Hauptserver (server.master)");
			jLabelJadeConfig.setFont(new Font("Dialog", Font.BOLD, 12));

			jLabelServerHeader = new JLabel();
			jLabelServerHeader.setText("Agent.GUI Hintergrundsystem - Konfiguration");
			jLabelServerHeader.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jPanelMiddle = new JPanel();
			jPanelMiddle.setLayout(new GridBagLayout());
			jPanelMiddle.add(jLabelServerHeader, gridBagConstraints5);
			jPanelMiddle.add(getJCheckBoxAutoStart(), gridBagConstraints);
			jPanelMiddle.add(jLabelJadeConfig, gridBagConstraints1);
			jPanelMiddle.add(jLabelMasterURL, gridBagConstraints2);
			jPanelMiddle.add(jLabelMasterPort, gridBagConstraints4);
			jPanelMiddle.add(getJTextFieldMasterURL(), gridBagConstraints51);
			jPanelMiddle.add(getJTextFieldMasterPort(), gridBagConstraints6);
			jPanelMiddle.add(getJTextFieldMasterPort4MTP(), gridBagConstraints11);
			jPanelMiddle.add(jLabelMasterPort4MTP, gridBagConstraints22);
			jPanelMiddle.add(jLabelPort4MTP, gridBagConstraints31);
			jPanelMiddle.add(jLabelPort, gridBagConstraints41);
		}
		return jPanelMiddle;
	}

	/**
	 * This method initializes jCheckBoxAutoStart	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getJCheckBoxAutoStart() {
		if (jCheckBoxAutoStart == null) {
			jCheckBoxAutoStart = new JCheckBox();
			jCheckBoxAutoStart.setText("Hintergrundsystem beim Programmstart automatisch initialisieren");
		}
		return jCheckBoxAutoStart;
	}

	/**
	 * This method initialises jTextFieldMasterURL	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldMasterURL() {
		if (jTextFieldMasterURL == null) {
			jTextFieldMasterURL = new JTextField();
			jTextFieldMasterURL.setPreferredSize(new Dimension(468, 26));
		}
		return jTextFieldMasterURL;
	}

	/**
	 * This method initializes jTextFieldMasterPort	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldMasterPort() {
		if (jTextFieldMasterPort == null) {
			jTextFieldMasterPort = new JTextField();
			jTextFieldMasterPort.setPreferredSize(new Dimension(100, 26));
			jTextFieldMasterPort.addKeyListener( new KeyAdapter() {
				public void keyTyped(KeyEvent kT) {
					char charackter = kT.getKeyChar();
					String SngChar = Character.toString(charackter);
					// --- Numbers only !!! ------
					if ( SngChar.matches( "[0-9]" ) == false ) {
						kT.consume();	
						return;
					}
				 }				 
			});
		}
		return jTextFieldMasterPort;
	}

	/**
	 * This method initializes jTextFieldMasterPort4MTP	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldMasterPort4MTP() {
		if (jTextFieldMasterPort4MTP == null) {
			jTextFieldMasterPort4MTP = new JTextField();
			jTextFieldMasterPort4MTP.setPreferredSize(new Dimension(100, 26));
			jTextFieldMasterPort4MTP.addKeyListener( new KeyAdapter() {
				public void keyTyped(KeyEvent kT) {
					char charackter = kT.getKeyChar();
					String SngChar = Character.toString(charackter);
					// --- Numbers only !!! ------
					if ( SngChar.matches( "[0-9]" ) == false ) {
						kT.consume();	
						return;
					}
				 }				 
			});
		}
		return jTextFieldMasterPort4MTP;
	}

	/**
	 * This method initializes jPanelBottom	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelBottom() {
		if (jPanelBottom == null) {
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.fill = GridBagConstraints.BOTH;
			gridBagConstraints16.gridy = 2;
			gridBagConstraints16.weightx = 1.0;
			gridBagConstraints16.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints16.anchor = GridBagConstraints.EAST;
			gridBagConstraints16.gridx = 3;
			GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
			gridBagConstraints17.gridx = 0;
			gridBagConstraints17.anchor = GridBagConstraints.WEST;
			gridBagConstraints17.gridwidth = 4;
			gridBagConstraints17.gridy = 0;
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.fill = GridBagConstraints.BOTH;
			gridBagConstraints15.gridy = 1;
			gridBagConstraints15.weightx = 1.0;
			gridBagConstraints15.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints15.anchor = GridBagConstraints.EAST;
			gridBagConstraints15.gridx = 3;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.gridx = 2;
			gridBagConstraints14.anchor = GridBagConstraints.WEST;
			gridBagConstraints14.insets = new Insets(5, 10, 0, 0);
			gridBagConstraints14.gridy = 2;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 2;
			gridBagConstraints13.anchor = GridBagConstraints.WEST;
			gridBagConstraints13.insets = new Insets(5, 10, 0, 0);
			gridBagConstraints13.gridy = 1;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.anchor = GridBagConstraints.WEST;
			gridBagConstraints12.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints12.gridy = 1;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.fill = GridBagConstraints.BOTH;
			gridBagConstraints10.gridy = 2;
			gridBagConstraints10.weightx = 1.0;
			gridBagConstraints10.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints10.anchor = GridBagConstraints.WEST;
			gridBagConstraints10.gridx = 1;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = GridBagConstraints.BOTH;
			gridBagConstraints9.gridy = 1;
			gridBagConstraints9.weightx = 1.0;
			gridBagConstraints9.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints9.anchor = GridBagConstraints.WEST;
			gridBagConstraints9.gridx = 1;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 0;
			gridBagConstraints8.anchor = GridBagConstraints.WEST;
			gridBagConstraints8.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints8.gridy = 2;

			jLabelDBtitle = new JLabel();
			jLabelDBtitle.setText("MySQL-Datenbank für den Hauptserver (server.master)");
			jLabelDBtitle.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jLabelDBpswd = new JLabel();
			jLabelDBpswd.setText("DB-Pswd");
			jLabelDBUser = new JLabel();
			jLabelDBUser.setText("DB-User");

			jLabelDB = new JLabel();
			jLabelDB.setText("DB");
			jLabelDBHost = new JLabel();
			jLabelDBHost.setText("DB-Host");
			jLabelDBHost.setPreferredSize(new Dimension(55, 16));
			
			jPanelBottom = new JPanel();
			jPanelBottom.setLayout(new GridBagLayout());
			jPanelBottom.add(jLabelDBHost, gridBagConstraints12);
			jPanelBottom.add(jLabelDB, gridBagConstraints8);
			jPanelBottom.add(getJTextFieldDBHost(), gridBagConstraints9);
			jPanelBottom.add(getJTextFieldDB(), gridBagConstraints10);
			jPanelBottom.add(jLabelDBUser, gridBagConstraints13);
			jPanelBottom.add(jLabelDBpswd, gridBagConstraints14);
			jPanelBottom.add(getJTextFieldDBUser(), gridBagConstraints15);
			jPanelBottom.add(jLabelDBtitle, gridBagConstraints17);
			jPanelBottom.add(getjTextFieldDBPswd(), gridBagConstraints16);
		}
		return jPanelBottom;
	}
	/**
	 * This method initializes jTextFieldDBHost	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldDBHost() {
		if (jTextFieldDBHost == null) {
			jTextFieldDBHost = new JTextField();
			jTextFieldDBHost.setPreferredSize(new Dimension(200, 26));
		}
		return jTextFieldDBHost;
	}
	/**
	 * This method initializes jTextFieldDB	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldDB() {
		if (jTextFieldDB == null) {
			jTextFieldDB = new JTextField();
			jTextFieldDB.setPreferredSize(new Dimension(200, 26));
		}
		return jTextFieldDB;
	}
	/**
	 * This method initializes jTextFieldDBUser	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldDBUser() {
		if (jTextFieldDBUser == null) {
			jTextFieldDBUser = new JTextField();
			jTextFieldDBUser.setPreferredSize(new Dimension(200, 26));
		}
		return jTextFieldDBUser;
	}
	/**
	 * This method initializes jPasswordFieldDBpswd	
	 * @return javax.swing.JPasswordField	
	 */
	private JTextField getjTextFieldDBPswd() {
		if (jTextFieldDBPswd == null) {
			jTextFieldDBPswd = new JPasswordField();
			jTextFieldDBPswd.setPreferredSize(new Dimension(200, 26));
		}
		return jTextFieldDBPswd;
	}
	
	/**
	 * This method initializes jPanelSaveOptions	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelSaveOptions() {
		if (jPanelDummy == null) {
			jPanelDummy = new JPanel();
			jPanelDummy.setLayout(new GridBagLayout());
		}
		return jPanelDummy;
	}
	
	/**
	 * This method initializes jPanelEmbedded	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelEmbeddedSystemAgent() {
		if (jPanelEmbedded == null) {
			GridBagConstraints gridBagConstraints49 = new GridBagConstraints();
			gridBagConstraints49.gridx = 1;
			gridBagConstraints49.anchor = GridBagConstraints.WEST;
			gridBagConstraints49.gridy = 10;
			GridBagConstraints gridBagConstraints40 = new GridBagConstraints();
			gridBagConstraints40.gridx = 1;
			gridBagConstraints40.anchor = GridBagConstraints.WEST;
			gridBagConstraints40.insets = new Insets(2, 0, 0, 0);
			gridBagConstraints40.gridy = 9;
			GridBagConstraints gridBagConstraints48 = new GridBagConstraints();
			gridBagConstraints48.gridx = 0;
			gridBagConstraints48.gridwidth = 2;
			gridBagConstraints48.anchor = GridBagConstraints.WEST;
			gridBagConstraints48.insets = new Insets(15, 0, 0, 0);
			gridBagConstraints48.gridy = 4;
			GridBagConstraints gridBagConstraints47 = new GridBagConstraints();
			gridBagConstraints47.gridx = 0;
			gridBagConstraints47.anchor = GridBagConstraints.WEST;
			gridBagConstraints47.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints47.gridy = 5;
			GridBagConstraints gridBagConstraints46 = new GridBagConstraints();
			gridBagConstraints46.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints46.gridy = 5;
			gridBagConstraints46.weightx = 1.0;
			gridBagConstraints46.anchor = GridBagConstraints.WEST;
			gridBagConstraints46.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints46.gridx = 1;
			GridBagConstraints gridBagConstraints45 = new GridBagConstraints();
			gridBagConstraints45.gridx = 1;
			gridBagConstraints45.anchor = GridBagConstraints.WEST;
			gridBagConstraints45.fill = GridBagConstraints.NONE;
			gridBagConstraints45.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints45.gridy = 3;
			GridBagConstraints gridBagConstraints43 = new GridBagConstraints();
			gridBagConstraints43.gridx = 0;
			gridBagConstraints43.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints43.gridy = 3;
			GridBagConstraints gridBagConstraints29 = new GridBagConstraints();
			gridBagConstraints29.gridx = 0;
			gridBagConstraints29.anchor = GridBagConstraints.WEST;
			gridBagConstraints29.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints29.gridwidth = 2;
			gridBagConstraints29.gridy = 8;
			GridBagConstraints gridBagConstraints39 = new GridBagConstraints();
			gridBagConstraints39.gridx = 2;
			gridBagConstraints39.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints39.gridy = 7;
			GridBagConstraints gridBagConstraints38 = new GridBagConstraints();
			gridBagConstraints38.fill = GridBagConstraints.BOTH;
			gridBagConstraints38.gridy = 7;
			gridBagConstraints38.weightx = 1.0;
			gridBagConstraints38.anchor = GridBagConstraints.WEST;
			gridBagConstraints38.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints38.gridx = 1;
			GridBagConstraints gridBagConstraints37 = new GridBagConstraints();
			gridBagConstraints37.gridx = 0;
			gridBagConstraints37.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints37.anchor = GridBagConstraints.WEST;
			gridBagConstraints37.gridy = 7;
			GridBagConstraints gridBagConstraints36 = new GridBagConstraints();
			gridBagConstraints36.gridx = 0;
			gridBagConstraints36.insets = new Insets(15, 0, 0, 0);
			gridBagConstraints36.gridwidth = 2;
			gridBagConstraints36.anchor = GridBagConstraints.WEST;
			gridBagConstraints36.gridy = 6;
			GridBagConstraints gridBagConstraints35 = new GridBagConstraints();
			gridBagConstraints35.gridx = 0;
			gridBagConstraints35.gridwidth = 2;
			gridBagConstraints35.anchor = GridBagConstraints.WEST;
			gridBagConstraints35.insets = new Insets(10, 0, 0, 0);
			gridBagConstraints35.gridy = 1;
			GridBagConstraints gridBagConstraints34 = new GridBagConstraints();
			gridBagConstraints34.fill = GridBagConstraints.NONE;
			gridBagConstraints34.gridy = 2;
			gridBagConstraints34.weightx = 0.0;
			gridBagConstraints34.anchor = GridBagConstraints.WEST;
			gridBagConstraints34.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints34.gridx = 1;
			GridBagConstraints gridBagConstraints33 = new GridBagConstraints();
			gridBagConstraints33.gridx = 0;
			gridBagConstraints33.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints33.anchor = GridBagConstraints.WEST;
			gridBagConstraints33.gridy = 2;
			GridBagConstraints gridBagConstraints32 = new GridBagConstraints();
			gridBagConstraints32.gridx = 0;
			gridBagConstraints32.gridwidth = 2;
			gridBagConstraints32.anchor = GridBagConstraints.WEST;
			gridBagConstraints32.gridy = 0;

			jLabelEmbeddedHeader = new JLabel();
			jLabelEmbeddedHeader.setText("Agent.GUI Dienst / Embedded System Agent");
			jLabelEmbeddedHeader.setText(Language.translate(jLabelEmbeddedHeader.getText()));
			jLabelEmbeddedHeader.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jLabelProjectHeader = new JLabel();
			jLabelProjectHeader.setText("Bitte wählen Sie ein Projekt aus");
			jLabelProjectHeader.setText(Language.translate(jLabelProjectHeader.getText()));
			jLabelProjectHeader.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jLabelProject = new JLabel();
			jLabelProject.setText("Projekt");
			jLabelProject.setText(Language.translate(jLabelProject.getText()) + ":");
			
			jLabelExecuteAs = new JLabel();
			jLabelExecuteAs.setText("Ausführen als");
			jLabelExecuteAs.setText(Language.translate(jLabelExecuteAs.getText()) + ":");
			jLabelExecuteAs.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jLabelSetupHeader = new JLabel();
			jLabelSetupHeader.setText("Dienst: Bitte wählen Sie das Setup aus, das gestartet werden soll");
			jLabelSetupHeader.setText(Language.translate(jLabelSetupHeader.getText()));
			jLabelSetupHeader.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jLabelSetup = new JLabel();
			jLabelSetup.setText("Setup");
			jLabelSetup.setText(Language.translate(jLabelSetup.getText()));
			
			jLabelAgentHeader = new JLabel();
			jLabelAgentHeader.setText("Embedded System Agent: Bitte wählen Sie den Agenten aus, der gestartet werden soll");
			jLabelAgentHeader.setText(Language.translate(jLabelAgentHeader.getText()));
			jLabelAgentHeader.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jLabelAgent = new JLabel();
			jLabelAgent.setText("Agent:");
			
			jLabelVisConfig = new JLabel();
			jLabelVisConfig.setText("Agent.GUI - Anwendungsvisualisierungen");
			jLabelVisConfig.setText(Language.translate(jLabelVisConfig.getText()));
			jLabelVisConfig.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jPanelEmbedded = new JPanel();
			jPanelEmbedded.setLayout(new GridBagLayout());
			jPanelEmbedded.setSize(new Dimension(541, 318));
			jPanelEmbedded.add(jLabelEmbeddedHeader, gridBagConstraints32);
			jPanelEmbedded.add(jLabelProject, gridBagConstraints33);
			jPanelEmbedded.add(getJComboBoxProjectSelector(), gridBagConstraints34);
			jPanelEmbedded.add(jLabelProjectHeader, gridBagConstraints35);
			jPanelEmbedded.add(jLabelAgentHeader, gridBagConstraints36);
			jPanelEmbedded.add(jLabelAgent, gridBagConstraints37);
			jPanelEmbedded.add(getJTextFieldAgentClass(), gridBagConstraints38);
			jPanelEmbedded.add(getJButtonEsaSelectAgent(), gridBagConstraints39);
			jPanelEmbedded.add(jLabelVisConfig, gridBagConstraints29);
			jPanelEmbedded.add(jLabelExecuteAs, gridBagConstraints43);
			jPanelEmbedded.add(getJPanelExecution(), gridBagConstraints45);
			jPanelEmbedded.add(getJComboBoxSetupSelector(), gridBagConstraints46);
			jPanelEmbedded.add(jLabelSetup, gridBagConstraints47);
			jPanelEmbedded.add(jLabelSetupHeader, gridBagConstraints48);
			jPanelEmbedded.add(getJRadioButtonVisNon(), gridBagConstraints40);
			jPanelEmbedded.add(getJRadioButtonVisTrayIcon(), gridBagConstraints49);
			
			ButtonGroup visAsGroup = new ButtonGroup();
			visAsGroup.add(getJRadioButtonVisNon());
			visAsGroup.add(getJRadioButtonVisTrayIcon());
			
		}
		return jPanelEmbedded;
	}
	/**
	 * This method initializes jComboBoxProjectSelector	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBoxProjectSelector() {
		if (jComboBoxProjectSelector == null) {
			
			DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel();
			comboBoxModel.addElement("");
			String[] projectFolders = globalInfo.getProjectSubDirectories();
			for (int i = 0; i < projectFolders.length; i++) {
				comboBoxModel.addElement(projectFolders[i]);	
			}
			jComboBoxProjectSelector = new JComboBox(comboBoxModel);
			jComboBoxProjectSelector.setPreferredSize(new Dimension(300, 26));
			jComboBoxProjectSelector.setActionCommand("esaProjectSelected");
			jComboBoxProjectSelector.addActionListener(this);
		}
		return jComboBoxProjectSelector;
	}
	/**
	 * This method initializes jButtonDefaultClassStaticCheck	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonEsaSelectAgent() {
		if (jButtonSelectAgentClass == null) {
			jButtonSelectAgentClass = new JButton();
			jButtonSelectAgentClass.setToolTipText(Language.translate("Agenten auswählen"));
			jButtonSelectAgentClass.setPreferredSize(new Dimension(45, 26));
			jButtonSelectAgentClass.setIcon(new ImageIcon(getClass().getResource(pathImage + "Search.png")));
			jButtonSelectAgentClass.setActionCommand("esaSelectAgent");
			jButtonSelectAgentClass.addActionListener(this);
		}
		return jButtonSelectAgentClass;
	}
	/**
	 * This method initializes jPanelExecution	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelExecution() {
		if (jPanelExecution == null) {
			GridBagConstraints gridBagConstraints44 = new GridBagConstraints();
			gridBagConstraints44.gridx = 1;
			gridBagConstraints44.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints44.gridy = 0;
			GridBagConstraints gridBagConstraints42 = new GridBagConstraints();
			gridBagConstraints42.gridx = 0;
			gridBagConstraints42.gridy = 0;
			
			jPanelExecution = new JPanel();
			jPanelExecution.setLayout(new GridBagLayout());
			jPanelExecution.add(getJRadioButtonExecuteAsDeviceAgent(), gridBagConstraints44);
			jPanelExecution.add(getJRadioButtonExecuteAsService(), gridBagConstraints42);
			
			ButtonGroup executeAsGroup = new ButtonGroup();
			executeAsGroup.add(getJRadioButtonExecuteAsDeviceAgent());
			executeAsGroup.add(getJRadioButtonExecuteAsService());
			
		}
		return jPanelExecution;
	}
	/**
	 * This method initializes jRadioButton	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadioButtonExecuteAsService() {
		if (jRadioButtonExecuteAsServerService == null) {
			jRadioButtonExecuteAsServerService = new JRadioButton();
			jRadioButtonExecuteAsServerService.setText("Dienst");
			jRadioButtonExecuteAsServerService.setText(Language.translate(jRadioButtonExecuteAsServerService.getText()));
			jRadioButtonExecuteAsServerService.setActionCommand("esaExecuteAsServerService");
			jRadioButtonExecuteAsServerService.addActionListener(this);
		}
		return jRadioButtonExecuteAsServerService;
	}
	/**
	 * This method initializes jRadioButtonExecuteAsDeviceAgent	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadioButtonExecuteAsDeviceAgent() {
		if (jRadioButtonExecuteAsDeviceAgent == null) {
			jRadioButtonExecuteAsDeviceAgent = new JRadioButton();
			jRadioButtonExecuteAsDeviceAgent.setText("Embedded System Agent");
			jRadioButtonExecuteAsDeviceAgent.setText(Language.translate(jRadioButtonExecuteAsDeviceAgent.getText()));
			jRadioButtonExecuteAsDeviceAgent.setActionCommand("esaExecuteAsDeviceAgent");
			jRadioButtonExecuteAsDeviceAgent.addActionListener(this);
		}
		return jRadioButtonExecuteAsDeviceAgent;
	}
	/**
	 * This method initializes comboBoxModelSetup	
	 * @return javax.swing.DefaultComboBoxModel	
	 */
	private void setComboBoxModelSetup() {
		if (this.esaProjectSelected!=null) {
			
			Vector<String> setupVector = new Vector<String>(this.esaProjectSelected.getSimulationSetups().keySet());
			Collections.sort(setupVector, String.CASE_INSENSITIVE_ORDER);
			
			DefaultComboBoxModel comboBoxModelSetup = new DefaultComboBoxModel();
			comboBoxModelSetup.addElement("");
			for (int i=0; i<setupVector.size(); i++) {
				comboBoxModelSetup.addElement(setupVector.get(i).trim());
			}
			this.getJComboBoxSetupSelector().setModel(comboBoxModelSetup);
		}
	}
	/**
	 * This method initializes jComboBoxSetupSelector	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBoxSetupSelector() {
		if (jComboBoxSetupSelector == null) {
			jComboBoxSetupSelector = new JComboBox();
			jComboBoxSetupSelector.setPreferredSize(new Dimension(300, 26));
			jComboBoxSetupSelector.setActionCommand("esaSetupSelected");
			jComboBoxSetupSelector.addActionListener(this);
		}
		return jComboBoxSetupSelector;
	}
	/**
	 * Gets the j text field agent class.
	 * @return the j text field agent class
	 */
	private JTextField getJTextFieldAgentClass() {
		if (jTextFieldAgentClass == null) {
			jTextFieldAgentClass = new JTextField();
			jTextFieldAgentClass.setPreferredSize(new Dimension(300, 26));
			jTextFieldAgentClass.setEditable(false);
		}
		return jTextFieldAgentClass;
	}
	/**
	 * This method initializes jRadioButtonVisNon	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadioButtonVisNon() {
		if (jRadioButtonVisNon == null) {
			jRadioButtonVisNon = new JRadioButton();
			jRadioButtonVisNon.setText("Sämtliche visuellen Darstellungen deaktivieren");
			jRadioButtonVisNon.setText(Language.translate(jRadioButtonVisNon.getText()));
			jRadioButtonVisNon.setPreferredSize(new Dimension(300, 24));
			jRadioButtonVisNon.setActionCommand("visNon");
			jRadioButtonVisNon.addActionListener(this);
		}
		return jRadioButtonVisNon;
	}
	/**
	 * This method initializes jRadioButtonVisTrayIcon	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadioButtonVisTrayIcon() {
		if (jRadioButtonVisTrayIcon == null) {
			jRadioButtonVisTrayIcon = new JRadioButton();
			jRadioButtonVisTrayIcon.setText("Agent.GUI - Tray Icon anzeigen");
			jRadioButtonVisTrayIcon.setText(Language.translate(jRadioButtonVisTrayIcon.getText()));
			jRadioButtonVisTrayIcon.setPreferredSize(new Dimension(300, 24));
			jRadioButtonVisTrayIcon.setActionCommand("visTrayIcon");
			jRadioButtonVisTrayIcon.addActionListener(this);
		}
		return jRadioButtonVisTrayIcon;
	}
	
	/**
	 * This method initializes jButtonUpdateSiteDefault	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonUpdateSiteDefault() {
		if (jButtonUpdateSiteDefault == null) {
			jButtonUpdateSiteDefault = new JButton();
			jButtonUpdateSiteDefault.setPreferredSize(new Dimension(45, 26));
			jButtonUpdateSiteDefault.setIcon(new ImageIcon(getClass().getResource(this.pathImage + "MBreset.png")));
			jButtonUpdateSiteDefault.setToolTipText(Language.translate("Standard verwenden"));
			jButtonUpdateSiteDefault.setActionCommand("resetSettings");
			jButtonUpdateSiteDefault.addActionListener(this);
		}
		return jButtonUpdateSiteDefault;
	}
	/**
	 * This method initializes jButtonApply	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonApply() {
		if (jButtonApply == null) {
			jButtonApply = new JButton();
			jButtonApply.setText("Anwenden");
			jButtonApply.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonApply.setForeground(new Color(0, 153, 0));
			jButtonApply.setPreferredSize(new Dimension(100, 26));
			jButtonApply.setActionCommand("applySettings");
			jButtonApply.addActionListener(this);
		}
		return jButtonApply;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		String actCMD = ae.getActionCommand();
		if (actCMD.equalsIgnoreCase("runAsApplication") || actCMD.equalsIgnoreCase("runAsServer") || actCMD.equalsIgnoreCase("runAsEmbeddedSystemAgent")) {
			if (getJRadioButtonRunAsApplication().isSelected()) {
				this.executionModeNew = ExecutionMode.APPLICATION;
			} else if (getJRadioButtonRunAsServer().isSelected()) {
				this.executionModeNew = ExecutionMode.SERVER;
			} else if (getJRadioButtonRunAsDevice().isSelected()) {
				this.executionModeNew = ExecutionMode.DEVICE_SYSTEM;
			}
			this.refreshView();	
			
		} else if (actCMD.equalsIgnoreCase("esaProjectSelected")) {
			this.esaLoadSelectedProject();
		} else if (actCMD.equalsIgnoreCase("esaExecuteAsServerService")) {
			this.refreshView();
		} else if (actCMD.equalsIgnoreCase("esaExecuteAsDeviceAgent")) {
			this.refreshView();
			
		} else if (actCMD.equalsIgnoreCase("esaSetupSelected")) {
			// --- nothing to do here ---------------------
		} else if (actCMD.equalsIgnoreCase("esaSelectAgent")) {
			this.esaSelectAgent();
		} else if (actCMD.equalsIgnoreCase("visNon")) {
			// --- nothing to do here ---------------------
		} else if (actCMD.equalsIgnoreCase("visTrayIcon")) {
			// --- nothing to do here ---------------------
			
		} else if (actCMD.equalsIgnoreCase("resetSettings")) {
			this.setGlobalData2Form();
		} else if (actCMD.equalsIgnoreCase("applySettings")) {
			this.doOkAction();
		} else {
			System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + actCMD);
		}
		
	}
	
	/**
	 * This method handles the view 
	 */
	public void refreshView() {
		
		if (this.getJRadioButtonRunAsDevice().isSelected()==true) {
			// --- Embedded System Agent ------------------
			this.setConfigurationComponent(getJPanelEmbeddedSystemAgent());
			
			if (this.getJComboBoxProjectSelector().getSelectedItem()==null || this.getJComboBoxProjectSelector().getSelectedItem().equals("")==true) {
				
				jLabelExecuteAs.setEnabled(false);
				jLabelSetupHeader.setEnabled(false);
				jLabelSetup.setEnabled(false);
				jLabelAgentHeader.setEnabled(false);
				jLabelAgent.setEnabled(false);
				jLabelVisConfig.setEnabled(false);
				
				this.getJRadioButtonExecuteAsService().setEnabled(false);
				this.getJRadioButtonExecuteAsDeviceAgent().setEnabled(false);
				this.getJComboBoxSetupSelector().setEnabled(false);
				this.getJTextFieldAgentClass().setEnabled(false);
				this.getJButtonEsaSelectAgent().setEnabled(false);
				this.getJRadioButtonVisNon().setEnabled(false);
				this.getJRadioButtonVisTrayIcon().setEnabled(false);
			
			} else {

				jLabelExecuteAs.setEnabled(true);
				this.getJRadioButtonExecuteAsService().setEnabled(true);
				this.getJRadioButtonExecuteAsDeviceAgent().setEnabled(true);
				
				if (this.getJRadioButtonExecuteAsService().isSelected()==false && this.getJRadioButtonExecuteAsDeviceAgent().isSelected()==false) {
					this.getJComboBoxSetupSelector().setSelectedItem(null);
					this.getJTextFieldAgentClass().setText(null);
					
					jLabelSetupHeader.setEnabled(false);
					jLabelSetup.setEnabled(false);
					jLabelAgentHeader.setEnabled(false);
					jLabelAgent.setEnabled(false);
					jLabelVisConfig.setEnabled(false);
					this.getJComboBoxSetupSelector().setEnabled(false);
					this.getJTextFieldAgentClass().setEnabled(false);
					this.getJButtonEsaSelectAgent().setEnabled(false);
					this.getJRadioButtonVisNon().setEnabled(false);
					this.getJRadioButtonVisTrayIcon().setEnabled(false);
					
				} else if (this.getJRadioButtonExecuteAsService().isSelected()) {
					this.getJTextFieldAgentClass().setText(null);
					
					jLabelSetupHeader.setEnabled(true);
					jLabelSetup.setEnabled(true);
					jLabelAgentHeader.setEnabled(false);
					jLabelAgent.setEnabled(false);
					jLabelVisConfig.setEnabled(false);
					this.getJComboBoxSetupSelector().setEnabled(true);
					this.getJTextFieldAgentClass().setEnabled(false);
					this.getJButtonEsaSelectAgent().setEnabled(false);
					this.getJRadioButtonVisNon().setEnabled(false);
					this.getJRadioButtonVisTrayIcon().setEnabled(false);

				} else if (this.getJRadioButtonExecuteAsDeviceAgent().isSelected()) {
					this.getJComboBoxSetupSelector().setSelectedItem(null);
					
					jLabelSetupHeader.setEnabled(false);
					jLabelSetup.setEnabled(false);
					jLabelAgentHeader.setEnabled(true);
					jLabelAgent.setEnabled(true);
					jLabelVisConfig.setEnabled(true);
					this.getJComboBoxSetupSelector().setEnabled(false);
					this.getJTextFieldAgentClass().setEnabled(true);
					this.getJButtonEsaSelectAgent().setEnabled(true);
					this.getJRadioButtonVisNon().setEnabled(true);
					this.getJRadioButtonVisTrayIcon().setEnabled(true);
					
				}
			}
			
		} else {
			// --- Application or Server ------------------			
			this.setConfigurationComponent(getJPanelAppNServer());
			
			// --- Reset local variables ------------------
			this.getJComboBoxProjectSelector().setSelectedItem(null);
			this.getJComboBoxSetupSelector().setSelectedItem(null);
			this.getJTextFieldAgentClass().setText(null);
			this.esaClassSelector = null;
			
			if (jRadioButtonRunAsServer.isSelected()) {
				jLabelServerHeader.setEnabled(true);
				jCheckBoxAutoStart.setEnabled(true);
				jLabelDBtitle.setEnabled(true);
				jLabelDBHost.setEnabled(true);
				jTextFieldDBHost.setEnabled(true);
				jLabelDB.setEnabled(true);
				jTextFieldDB.setEnabled(true);
				jLabelDBUser.setEnabled(true);
				jTextFieldDBUser.setEnabled(true);
				jLabelDBpswd.setEnabled(true);
				jTextFieldDBPswd.setEnabled(true);
				
			} else {
				jLabelServerHeader.setEnabled(false);
				jCheckBoxAutoStart.setEnabled(false);
				jLabelDBtitle.setEnabled(false);
				jLabelDBHost.setEnabled(false);
				jTextFieldDBHost.setEnabled(false);
				jLabelDB.setEnabled(false);
				jTextFieldDB.setEnabled(false);
				jLabelDBUser.setEnabled(false);
				jTextFieldDBUser.setEnabled(false);
				jLabelDBpswd.setEnabled(false);
				jTextFieldDBPswd.setEnabled(false);
			}
		}
		
	}
	/**
	 * Sets the component for the configuration.
	 */
	private void setConfigurationComponent(Component newComp) {
		
		for (int i = 0; i < this.getComponents().length; i++) {
			Component comp = this.getComponents()[i];
			if (newComp==this.getJPanelAppNServer() && comp==this.getJPanelEmbeddedSystemAgent()) {
				this.remove(this.getJPanelEmbeddedSystemAgent());
				this.add(newComp, this.gridBagConstraints4Configuration);
				this.validate();
				this.repaint();
				break;
			} else if (newComp==this.getJPanelEmbeddedSystemAgent() && comp==this.getJPanelAppNServer()) {
				this.remove(this.getJPanelAppNServer());
				this.add(newComp, this.gridBagConstraints4Configuration);
				this.validate();
				this.repaint();
				break;
			}
		}
		
	}
	
	/**
	 * Loads the project instance of the selected sub folder.
	 */
	private void esaLoadSelectedProject() {
		
		String projectFolderSelected = (String) this.getJComboBoxProjectSelector().getSelectedItem();
		if (projectFolderSelected==null || projectFolderSelected.equals("")) {
			this.esaProjectSelected = null;
			this.getJComboBoxSetupSelector().setSelectedItem(null);
			this.getJTextFieldAgentClass().setText(null);
			if (this.esaClassSelector!=null) {
				this.esaClassSelector.dispose();
				this.esaClassSelector = null;	
			}
			
		} else {
			// --- Project was selected -------------------
			Project selectedProject = Application.getProjectsLoaded().getProject(projectFolderSelected);
			if (this.esaProjectSelected==null) {
				this.esaProjectSelected = selectedProject;
				this.setComboBoxModelSetup();
				this.getJTextFieldAgentClass().setText(null);
				this.getClassSelector4ProjectAgents(selectedProject);
				
			} else {
				// --- Is this still the same project? ----
				String selectedProjectFolder = selectedProject.getProjectFolder();
				String currentProjectFolder = this.esaProjectSelected.getProjectFolder();
				if (selectedProjectFolder.equals(currentProjectFolder)==false) {
					// --- Reinitialise settings ---------- 
					this.esaProjectSelected = null;
					this.getJTextFieldAgentClass().setText(null);
					this.esaClassSelector.dispose();
					this.esaClassSelector = null;
					// --- Now reinitialise ---------------
					this.esaProjectSelected = selectedProject;
					this.setComboBoxModelSetup();
					this.getJTextFieldAgentClass().setText(null);
					this.getClassSelector4ProjectAgents(selectedProject);

				}
			}
			
		}
		this.refreshView();
		
	}
	
	/**
	 * Gets the class selector for project agents.
	 * @param project the project
	 * @return the class selector for project agents
	 */
	private ClassSelector getClassSelector4ProjectAgents(Project project) {
		String currAgentClass = this.getJTextFieldAgentClass().getText();
		if (this.esaClassSelector==null) {
			JListClassSearcher jListClassSearcher = new JListClassSearcher(Agent.class, project);
			this.esaClassSelector = new ClassSelector(null, jListClassSearcher, currAgentClass, null, Language.translate("Bitte wählen Sie den Agenten aus, der gestartet werden soll"), false);
		} 
		this.esaClassSelector.setClass2Search4CurrentValue(currAgentClass);
		return this.esaClassSelector;
	}
	
	/**
	 * Displays a selector for the embedded system agent.
	 */
	private void esaSelectAgent(){
		
		if (this.esaProjectSelected==null) {
			String msgHead = Language.translate("Fehlendes Projekt!");
			String msgText = Language.translate("Bitte wählen Sie ein Projekt aus") + "!";	
			JOptionPane.showMessageDialog(this.optionDialog.getContentPane(), msgText, msgHead, JOptionPane.ERROR_MESSAGE);
			this.getJComboBoxProjectSelector().showPopup();
			return;
		}
		// --- Open search dialog for agents --------------
		ClassSelector cs = this.getClassSelector4ProjectAgents(this.esaProjectSelected);
		cs.setVisible(true);
		// --- act in the dialog ... ----------------------
		if (cs.isCanceled()==true) return;
		this.getJTextFieldAgentClass().setText(cs.getClassSelected());
	}
	
	/**
	 * This method sets the Data from the global Area to the Form.
	 */
	private void setGlobalData2Form(){
		
		switch (globalInfo.getExecutionMode()) {
		case APPLICATION:
			this.jRadioButtonRunAsApplication.setSelected(true);
			this.jRadioButtonRunAsServer.setSelected(false);
			this.jRadioButtonRunAsEmbeddedSystemAgent.setSelected(false);
			break;
			
		case SERVER:
		case SERVER_MASTER:
		case SERVER_SLAVE:
			this.jRadioButtonRunAsApplication.setSelected(false);
			this.jRadioButtonRunAsServer.setSelected(true);
			this.jRadioButtonRunAsEmbeddedSystemAgent.setSelected(false);
			break;

		case DEVICE_SYSTEM:
			this.jRadioButtonRunAsApplication.setSelected(false);
			this.jRadioButtonRunAsServer.setSelected(false);
			this.jRadioButtonRunAsEmbeddedSystemAgent.setSelected(true);
			break;
		}
		
		if (globalInfo.isServerAutoRun()== true) {
			this.jCheckBoxAutoStart.setSelected(true);	
		} else {
			this.jCheckBoxAutoStart.setSelected(false);
		}
		this.jTextFieldMasterURL.setText(globalInfo.getServerMasterURL());
		this.jTextFieldMasterPort.setText(globalInfo.getServerMasterPort().toString());
		this.jTextFieldMasterPort4MTP.setText(globalInfo.getServerMasterPort4MTP().toString());
		
		this.jTextFieldDBHost.setText(globalInfo.getServerMasterDBHost());
		this.jTextFieldDB.setText(globalInfo.getServerMasterDBName());
		this.jTextFieldDBUser.setText(globalInfo.getServerMasterDBUser());
		this.jTextFieldDBPswd.setText(globalInfo.getServerMasterDBPswd());
		
		
		this.getJComboBoxProjectSelector().setSelectedItem(globalInfo.getDeviceServiceProjectFolder());
		switch (globalInfo.getDeviceServiceExecutionMode()) {
		case SETUP:
			this.getJRadioButtonExecuteAsService().setSelected(true);
			this.getJRadioButtonExecuteAsDeviceAgent().setSelected(false);
			break;
		case AGENT:
			this.getJRadioButtonExecuteAsService().setSelected(false);
			this.getJRadioButtonExecuteAsDeviceAgent().setSelected(true);
			break;
		}
		this.getJComboBoxSetupSelector().setSelectedItem(globalInfo.getDeviceServiceSetupSelected());
		this.getJTextFieldAgentClass().setText(globalInfo.getDeviceServiceAgentSelected());
		switch (globalInfo.getDeviceServiceAgentVisualisation()) {
		case NONE:
			this.jRadioButtonVisNon.setSelected(true);
			this.jRadioButtonVisTrayIcon.setSelected(false);
			break;
		case TRAY_ICON:
			this.jRadioButtonVisNon.setSelected(false);
			this.jRadioButtonVisTrayIcon.setSelected(true);
			break;
		}
		
		this.refreshView();
	}
	
	/**
	 * This method writes the data back from the form to the global area.
	 */
	private void setFromData2Global() {
		
		if (this.jRadioButtonRunAsApplication.isSelected()) {
			this.globalInfo.setExecutionMode(ExecutionMode.APPLICATION);
		} else if (this.jRadioButtonRunAsServer.isSelected()) {
			this.globalInfo.setExecutionMode(ExecutionMode.SERVER);
		} else if (this.jRadioButtonRunAsEmbeddedSystemAgent.isSelected()) {
			this.globalInfo.setExecutionMode(ExecutionMode.DEVICE_SYSTEM);
		}
		
		this.globalInfo.setServerAutoRun(this.jCheckBoxAutoStart.isSelected());
		this.globalInfo.setServerMasterURL(this.jTextFieldMasterURL.getText().trim());
		
		Integer usePort = Integer.parseInt(this.jTextFieldMasterPort.getText().trim());
		this.globalInfo.setServerMasterPort(usePort);
		Integer usePort4MTP = Integer.parseInt(this.jTextFieldMasterPort4MTP.getText().trim());
		this.globalInfo.setServerMasterPort4MTP(usePort4MTP);
		
		this.globalInfo.setServerMasterDBHost(this.jTextFieldDBHost.getText().trim());
		this.globalInfo.setServerMasterDBName(this.jTextFieldDB.getText().trim());
		this.globalInfo.setServerMasterDBUser(this.jTextFieldDBUser.getText().trim());
		this.globalInfo.setServerMasterDBPswd(this.jTextFieldDBPswd.getText().trim());
		
		
		this.globalInfo.setDeviceServiceProjectFolder((String) this.getJComboBoxProjectSelector().getSelectedItem());
		if (this.getJRadioButtonExecuteAsService().isSelected()) {
			this.globalInfo.setDeviceServiceExecutionMode(DeviceSystemExecutionMode.SETUP);	
		} else if (this.getJRadioButtonExecuteAsDeviceAgent().isSelected()) {
			this.globalInfo.setDeviceServiceExecutionMode(DeviceSystemExecutionMode.AGENT);
		}
		this.globalInfo.setDeviceServiceSetupSelected((String)this.getJComboBoxSetupSelector().getSelectedItem());
		this.globalInfo.setDeviceServiceAgentSelected(this.getJTextFieldAgentClass().getText());
		if (this.getJRadioButtonVisNon().isSelected()) {
			this.globalInfo.setDeviceServiceAgentVisualisation(EmbeddedSystemAgentVisualisation.NONE);			
		} else if (this.getJRadioButtonVisTrayIcon().isSelected()) {
			this.globalInfo.setDeviceServiceAgentVisualisation(EmbeddedSystemAgentVisualisation.TRAY_ICON);	
		}
		
	}
	
	/**
	 * This method doe's the Error-Handling for this Dialog.
	 * @return true or false
	 */
	private boolean errorFound() {
		
		String msgHead = null;
		String msgText = null;
		boolean err = false;
		
		if (this.getJRadioButtonRunAsDevice().isSelected()) {
			// --------------------------------------------------------------
			// --- Error Check for Service and Embedded System Agent --------
			// --------------------------------------------------------------
			
			// --- Project selected ? ---------------------------------------
			if (this.getJComboBoxProjectSelector().getSelectedItem()==null || this.getJComboBoxProjectSelector().getSelectedItem().equals("")==true) {
				msgHead = Language.translate("Fehlendes Projekt!");
				msgText = Language.translate("Bitte wählen Sie ein Projekt aus") + "!";	
				JOptionPane.showMessageDialog(this.optionDialog.getContentPane(), msgText, msgHead, JOptionPane.ERROR_MESSAGE);
				this.getJComboBoxProjectSelector().showPopup();
				return true;
			}
			// --- Case Service or Embedded System Agent? -------------------
			if (this.getJRadioButtonExecuteAsService().isSelected()==false && this.getJRadioButtonExecuteAsDeviceAgent().isSelected()==false) {
				msgHead = Language.translate("Ausführungsart nicht festgelegt!");
				msgText = Language.translate("Bitte legen Sie fest, ob Sie Agent.GUI als Service oder als Embedded System Agent ausführen möchten") + "!";	
				JOptionPane.showMessageDialog(this.optionDialog.getContentPane(), msgText, msgHead, JOptionPane.ERROR_MESSAGE);
				return true;
				
			} else if (this.getJRadioButtonExecuteAsService().isSelected()) {
				// --- Simulation Setup selected? --------------------------- 
				if (this.getJComboBoxSetupSelector().getSelectedItem()==null || this.getJComboBoxSetupSelector().getSelectedItem().equals("")==true) {
					msgHead = Language.translate("Fehlendes Simulations-Setup!");
					msgText = Language.translate("Bitte wählen Sie ein Simulations-Setup") + "!";	
					JOptionPane.showMessageDialog(this.optionDialog.getContentPane(), msgText, msgHead, JOptionPane.ERROR_MESSAGE);
					this.getJComboBoxSetupSelector().showPopup();
					return true;
				}
				
			} else if (this.getJRadioButtonExecuteAsDeviceAgent().isSelected()) {
				// --- Agent Selected ? -------------------------------------
				if (this.getJTextFieldAgentClass().getText()==null || this.getJTextFieldAgentClass().getText().equals("")) {
					msgHead = Language.translate("Fehlender Agent!");
					msgText = Language.translate("Bitte wählen Sie einen Agenten") + "!";	
					JOptionPane.showMessageDialog(this.optionDialog.getContentPane(), msgText, msgHead, JOptionPane.ERROR_MESSAGE);
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							esaSelectAgent();
						}
					});
					return true;
				}
				
			}
			
		} else {
			// --------------------------------------------------------------
			// --- Error Check for Application and Background System --------
			// --------------------------------------------------------------
			String testURL = this.jTextFieldMasterURL.getText().trim();
			String testPortAsString  = this.jTextFieldMasterPort.getText().trim();
			Integer testPortAsInteger = Integer.parseInt( testPortAsString );
					
			String  testPort4MTPAsString  = this.jTextFieldMasterPort4MTP.getText().trim();
			Integer testPort4MTPAsInteger = Integer.parseInt( testPort4MTPAsString );

			// --- Testing URL and Port -------------------------------------
			if ( testURL != null ) {
				if ( testURL.equalsIgnoreCase("")==false  ) {
					// --- Testing the URL ----------------------------------
					if ( testURL.contains(" ") ) {
						msgHead = Language.translate("Fehler: URL oder IP !");
						msgText = Language.translate("Die URL oder IP enthält unzulässige Zeichen!");	
						JOptionPane.showMessageDialog( this.optionDialog.getContentPane(), msgText, msgHead, JOptionPane.ERROR_MESSAGE);
						return true;
					}
					// --- Testing the Port ---------------------------------
					if ( testPortAsInteger.equals(0) ) {
						msgHead = Language.translate("Fehler: Port");
						msgText = Language.translate("Der Port muss einem Wert ungleich 0 entsprechen!");	
						JOptionPane.showMessageDialog( this.optionDialog.getContentPane(), msgText, msgHead, JOptionPane.ERROR_MESSAGE);
						return true;
					}
					// --- Testing the Port 4 MTP ---------------------------
					if ( testPort4MTPAsInteger.equals(0) ) {
						msgHead = Language.translate("Fehler: Port4MTP ");
						msgText = Language.translate("Der Port für die MTP-Adresse muss einem Wert ungleich 0 entsprechen!");	
						JOptionPane.showMessageDialog(this.optionDialog.getContentPane(), msgText, msgHead, JOptionPane.ERROR_MESSAGE);
						return true;
					}
				}
			}

		}
		return err;
	}
	
	/**
	 * Doe's the actions when using the OK-Button.
	 */
	private void doOkAction() {
		
		String newLine = Application.getGlobalInfo().getNewLineSeparator();
		
		// --- Error-Handling -------------------------------------------------
		if (errorFound()==true) {
			return;
		}
		// --- If a change from 'Application' to 'Server' occurs --------------
		if (this.executionModeNew!=this.executionModeOld) {
			// ----------------------------------------------------------------
			// --- Restart application because it was switched ----------------
			// --- between ExecutionModes, but ask the user before ------------
			// ----------------------------------------------------------------
			String executionModeTextNew = Application.getGlobalInfo().getExecutionModeDescription(this.executionModeNew);
			String MsgHead = "";
			String MsgText = "";
			
			MsgHead += Language.translate("Agent.GUI umschalten ?");
			MsgText += Language.translate("Progamm umschalten auf") + " '" + executionModeTextNew + "':" + newLine; 	
			MsgText += Language.translate("Möchten Sie Agent.GUI nun umschalten und neu starten ?");

			Integer MsgAnswer = JOptionPane.showConfirmDialog( this.optionDialog.getContentPane(), MsgText, MsgHead, JOptionPane.YES_NO_OPTION);
			if (MsgAnswer == JOptionPane.NO_OPTION) {
				return;
			}
			// ----------------------------------------------------------------
		}
		this.setFromData2Global();
		Application.getGlobalInfo().getFileProperties().save();
		// --------------------------------------------------------------------
		this.applySettings();
		// --------------------------------------------------------------------
		this.optionDialog.setVisible(false);
		
	}

	/**
	 * Apply settings.
	 */
	private void applySettings(){
		
		boolean serverOld = isBackgroundSystemServer(this.executionModeOld);
		boolean serverNew = isBackgroundSystemServer(this.executionModeNew);
		String executionModeTextOld = Application.getGlobalInfo().getExecutionModeDescription(this.executionModeOld);
		String executionModeTextNew = Application.getGlobalInfo().getExecutionModeDescription(this.executionModeNew);

		if (this.executionModeNew==this.executionModeOld) {
			// --- same ExecutionMode -------------------------------
			if (serverNew==true & serverNew==serverOld) {
				// --- Restart 'Server'------------------------------
				System.out.println(Language.translate("Neustart des Server-Dienstes"));
				Application.getJadePlatform().jadeStop();
				Application.getTrayIcon().remove();
				Application.setTrayIcon(null);
				// --- Restart --------------------------------------
				Application.startAgentGUI();
			}
			
		} else {
			// --- new ExecutionMode --------------------------------
			String textPrefix = Language.translate("Umschalten von");
			String textMiddle = Language.translate("auf");
			System.out.println(textPrefix + " '" + executionModeTextOld + "' " + textMiddle + " '" + executionModeTextNew + "'");
			
			// ------------------------------------------------------
			// --- Controlled shutdown of the current execution -----
			Application.getJadePlatform().jadeStop();
			
			// --- Case separation for current ExecutionMode --------
			switch (this.executionModeOld) {
			case APPLICATION:
				// --- Application Modus ----------------------------
				if (Application.getProjectsLoaded()!= null) {
					if (Application.getProjectsLoaded().closeAll() == false ) return;	
				}		
				// --- Close main window and TrayIcon ---------------
				Application.getMainWindow().dispose();
				Application.setMainWindow(null);
				Application.getTrayIcon().remove();
				Application.setTrayIcon(null);
				break;

			case SERVER:
			case SERVER_MASTER:
			case SERVER_SLAVE:
				// --- Background System Modus ----------------------
				Application.getTrayIcon().remove();
				Application.setTrayIcon(null);
				break;
				
			case DEVICE_SYSTEM:
				// --- Device / Embedded System Agent ---------------
				if (Application.getProjectsLoaded()!= null) {
					if (Application.getProjectsLoaded().closeAll()==false) return;	
				}		
				if (Application.getMainWindow()!=null) {
					Application.getMainWindow().dispose();
					Application.setMainWindow(null);
				}
				if (Application.getTrayIcon()!=null) {
					Application.getTrayIcon().remove();
					Application.setTrayIcon(null);	
				}
				break;
				
			}
			// --- Restart ------------------------------------------
			Application.startAgentGUI();
		}

	}

	/**
	 * Checks if the specified ExecutionMode is a background system server.
	 *
	 * @param executionMode the execution mode
	 * @return true, if is background system server
	 */
	private boolean isBackgroundSystemServer(ExecutionMode executionMode) {
		boolean server = false;
		switch (this.executionModeOld) {
		case SERVER:
		case SERVER_MASTER:
		case SERVER_SLAVE:
			server = true;
			break;
		}
		return server;
	}


}  //  @jve:decl-index=0:visual-constraint="-3,8"
