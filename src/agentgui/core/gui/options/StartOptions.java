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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;

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
	
	private JPanel jPanelTop = null;
	private JPanel jPanelMiddle = null;
	private JPanel jPanelBottom = null;
	private JPanel jPanelDummy = null;
	
	private JRadioButton jRadioButtonRunAsApplication = null;
	private JRadioButton jRadioButtonRunAsServer = null;
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
	
	private boolean forceRestart = false;
	private boolean isServerOld = Application.isRunningAsServer();
	private boolean isServerNew = Application.isRunningAsServer();


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
		jRadioButtonRunAsServer.setText(Language.translate("Server-Dienst (Master / Slave)"));
		jLabelServerHeader.setText("Agent.GUI " + Language.translate("Server-Konfiguration"));
		jCheckBoxAutoStart.setText(" " + Language.translate("Server-Dienst beim Programmstart automatisch initialisieren"));
		jLabelJadeConfig.setText( Application.getGlobalInfo().getApplicationTitle() + " " + Language.translate("Hauptserver (server.master)") );
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
		GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
		gridBagConstraints19.gridx = 0;
		gridBagConstraints19.weightx = 0.0;
		gridBagConstraints19.weighty = 1.0;
		gridBagConstraints19.insets = new Insets(0, 20, 20, 20);
		gridBagConstraints19.fill = GridBagConstraints.BOTH;
		gridBagConstraints19.ipadx = 0;
		gridBagConstraints19.gridy = 5;
		GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
		gridBagConstraints7.gridx = 0;
		gridBagConstraints7.insets = new Insets(0, 20, 10, 10);
		gridBagConstraints7.fill = GridBagConstraints.NONE;
		gridBagConstraints7.weightx = 0.0;
		gridBagConstraints7.anchor = GridBagConstraints.WEST;
		gridBagConstraints7.weighty = 0.0;
		gridBagConstraints7.gridy = 3;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 0;
		gridBagConstraints3.fill = GridBagConstraints.NONE;
		gridBagConstraints3.insets = new Insets(0, 20, 10, 10);
		gridBagConstraints3.weighty = 0.0;
		gridBagConstraints3.anchor = GridBagConstraints.WEST;
		gridBagConstraints3.weightx = 1.0;
		gridBagConstraints3.gridy = 2;
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridx = 0;
		gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints21.insets = new Insets(20, 20, 10, 20);
		gridBagConstraints21.anchor = GridBagConstraints.WEST;
		gridBagConstraints21.weightx = 0.0;
		gridBagConstraints21.gridy = 0;
		jLabelRunsAs = new JLabel();
		jLabelRunsAs.setText("Starte Agent.GUI als:");
		jLabelRunsAs.setFont(new Font("Dialog", Font.BOLD, 12));

		this.setSize(680, 399);
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		this.add(getJPanelTop(), gridBagConstraints21);
		this.add(getJPanelMiddle(), gridBagConstraints3);
		this.add(getJPanelBottom(), gridBagConstraints7);
		this.add(getJPanelSaveOptions(), gridBagConstraints19);
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
			jRadioButtonRunAsApplication.setPreferredSize(new Dimension(120, 24));
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
			jRadioButtonRunAsServer.setPreferredSize(new Dimension(200, 24));
			jRadioButtonRunAsServer.setText("Server-Dienst (Master / Slave)");
			jRadioButtonRunAsServer.setActionCommand("runAsServer");
			jRadioButtonRunAsServer.addActionListener(this);
		}
		return jRadioButtonRunAsServer;
	}

	/**
	 * This method initializes jPanelTop	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelTop() {
		if (jPanelTop == null) {
			GridBagConstraints gridBagConstraints27 = new GridBagConstraints();
			gridBagConstraints27.gridx = 3;
			gridBagConstraints27.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints27.weightx = 1.0;
			gridBagConstraints27.gridy = 0;
			jLabelDummyTop = new JLabel();
			jLabelDummyTop.setText("");
			GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
			gridBagConstraints26.insets = new Insets(0, 2, 0, 0);
			gridBagConstraints26.gridy = 0;
			gridBagConstraints26.gridx = 5;
			GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
			gridBagConstraints25.anchor = GridBagConstraints.WEST;
			gridBagConstraints25.gridx = 4;
			gridBagConstraints25.gridy = 0;
			gridBagConstraints25.insets = new Insets(0, 20, 0, 0);
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.anchor = GridBagConstraints.WEST;
			gridBagConstraints18.gridx = 1;
			gridBagConstraints18.gridy = 0;
			gridBagConstraints18.insets = new Insets(10, 20, 0, 10);
			GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
			gridBagConstraints24.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints24.gridy = 0;
			gridBagConstraints24.ipadx = 0;
			gridBagConstraints24.gridx = 2;
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.insets = new Insets(0, 20, 0, 0);
			gridBagConstraints23.gridy = 0;
			gridBagConstraints23.ipadx = 0;
			gridBagConstraints23.anchor = GridBagConstraints.WEST;
			gridBagConstraints23.gridx = 1;
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints20.gridy = 0;
			gridBagConstraints20.ipadx = 0;
			gridBagConstraints20.anchor = GridBagConstraints.WEST;
			gridBagConstraints20.gridx = 0;
			jPanelTop = new JPanel();
			jPanelTop.setLayout(new GridBagLayout());
			//jPanelTop.setPreferredSize(new Dimension(500, 25));
			jPanelTop.add(jLabelRunsAs, gridBagConstraints20);
			jPanelTop.add(getJRadioButtonRunAsApplication(), gridBagConstraints23);
			jPanelTop.add(getJRadioButtonRunAsServer(), gridBagConstraints24);
			jPanelTop.add(getJButtonApply(), gridBagConstraints25);
			jPanelTop.add(getJButtonUpdateSiteDefault(), gridBagConstraints26);
			jPanelTop.add(jLabelDummyTop, gridBagConstraints27);
			
			ButtonGroup runAsGroup = new ButtonGroup();
			runAsGroup.add(jRadioButtonRunAsApplication);
			runAsGroup.add(jRadioButtonRunAsServer);
			
		}
		return jPanelTop;
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
			gridBagConstraints51.weightx = 0.0;
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
			gridBagConstraints1.insets = new Insets(10, 0, 0, 0);
			gridBagConstraints1.gridwidth = 3;
			gridBagConstraints1.gridy = 3;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 0;
			gridBagConstraints.insets = new Insets(5, 0, 0, 10);
			gridBagConstraints.anchor = GridBagConstraints.WEST;
			gridBagConstraints.gridwidth = 3;
			gridBagConstraints.gridy = 2;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.insets = new Insets(10, 0, 0, 10);
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.gridwidth = 3;
			gridBagConstraints5.gridy = 0;

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
			jLabelServerHeader.setText("Agent.GUI Server-Konfiguration");
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
			jCheckBoxAutoStart.setText("Server-Dienst beim Programmstart automatisch initialisieren");
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
			jTextFieldMasterURL.setPreferredSize(new Dimension(400, 26));
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
			gridBagConstraints16.fill = GridBagConstraints.VERTICAL;
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
			gridBagConstraints15.fill = GridBagConstraints.VERTICAL;
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
			gridBagConstraints9.fill = GridBagConstraints.VERTICAL;
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
			jTextFieldDBHost.setPreferredSize(new Dimension(214, 26));
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
			jTextFieldDBUser.setPreferredSize(new Dimension(120, 26));
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
			jTextFieldDBPswd.setPreferredSize(new Dimension(120, 26));
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
		if (actCMD.equalsIgnoreCase("runAsApplication")) {
			this.refreshView();	
		} else if (actCMD.equalsIgnoreCase("runAsServer")) {
			this.refreshView();
		} else if (actCMD.equalsIgnoreCase("applySettings")) {
			this.doOkAction();
		} else if (actCMD.equalsIgnoreCase("resetSettings")) {
			this.setGlobalData2Form();
		} else {
			System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + actCMD);
		}
		
		
	}
	/**
	 * This method handles the view 
	 */
	public void refreshView() {
		
		if (jRadioButtonRunAsServer.isSelected()) {
			jCheckBoxAutoStart.setEnabled(true);
			jTextFieldDBHost.setEnabled(true);
			jTextFieldDB.setEnabled(true);
			jTextFieldDBUser.setEnabled(true);
			jTextFieldDBPswd.setEditable(true);
		} else {
			jCheckBoxAutoStart.setEnabled(false);
			jTextFieldDBHost.setEnabled(false);
			jTextFieldDB.setEnabled(false);
			jTextFieldDBUser.setEnabled(false);
			jTextFieldDBPswd.setEditable(false);
		}
	}

	/**
	 * This method sets the Data from the global Area to the Form.
	 */
	private void setGlobalData2Form(){
		
		// --- Panel "Programstart" (optionsStart) ------------------
		if (globalInfo.isRunAsServer()== true) {
			this.jRadioButtonRunAsServer.setSelected(true);
			this.jRadioButtonRunAsApplication.setSelected(false);
		} else {
			this.jRadioButtonRunAsServer.setSelected(false);
			this.jRadioButtonRunAsApplication.setSelected(true);
		}
		if (globalInfo.isServerAutoRun()==  true) {
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
		
		this.refreshView();
	}
	
	/**
	 * This method writes the data back from the form to the global area.
	 */
	private void setFromData2Global() {
		
		// --- Panel "Programstart" (optionsStart) ------------------
		globalInfo.setRunAsServer(this.jRadioButtonRunAsServer.isSelected());
		globalInfo.setServerAutoRun(this.jCheckBoxAutoStart.isSelected());
		globalInfo.setServerMasterURL(this.jTextFieldMasterURL.getText().trim());
		
		Integer usePort = Integer.parseInt(this.jTextFieldMasterPort.getText().trim());
		globalInfo.setServerMasterPort(usePort);
		Integer usePort4MTP = Integer.parseInt(this.jTextFieldMasterPort4MTP.getText().trim());
		globalInfo.setServerMasterPort4MTP(usePort4MTP);
		
		globalInfo.setServerMasterDBHost(this.jTextFieldDBHost.getText().trim());
		globalInfo.setServerMasterDBName(this.jTextFieldDB.getText().trim());
		globalInfo.setServerMasterDBUser(this.jTextFieldDBUser.getText().trim());
		globalInfo.setServerMasterDBPswd(this.jTextFieldDBPswd.getText().trim());
		
	}
	
	/**
	 * This method doe's the Error-Handling for this Dialog.
	 * @return true or false
	 */
	private boolean errorFound() {
		
		String MsgHead = null;
		String MsgText = null;
		boolean err = false;
		
		String  testURL = this.jTextFieldMasterURL.getText().trim();
		String  testPortAsString  = this.jTextFieldMasterPort.getText().trim();
		Integer testPortAsInteger = Integer.parseInt( testPortAsString );
				
		String  testPort4MTPAsString  = this.jTextFieldMasterPort4MTP.getText().trim();
		Integer testPort4MTPAsInteger = Integer.parseInt( testPort4MTPAsString );

		// --- Testing URL and Port ---------------------------------
		if ( testURL != null ) {
			if ( testURL.equalsIgnoreCase("")==false  ) {
				// --- Testing the URL ----------------------------------
				if ( testURL.contains(" ") ) {
					MsgHead = Language.translate("Fehler: URL oder IP !");
					MsgText = Language.translate("Die URL oder IP enthält unzulässige Zeichen!");	
					JOptionPane.showMessageDialog( this.optionDialog.getContentPane(), MsgText, MsgHead, JOptionPane.ERROR_MESSAGE);
					return true;
				}
				// --- Testing the Port ---------------------------------
				if ( testPortAsInteger.equals(0) ) {
					MsgHead = Language.translate("Fehler: Port");
					MsgText = Language.translate("Der Port muss einem Wert ungleich 0 entsprechen!");	
					JOptionPane.showMessageDialog( this.optionDialog.getContentPane(), MsgText, MsgHead, JOptionPane.ERROR_MESSAGE);
					return true;
				}
				// --- Testing the Port 4 MTP ---------------------------
				if ( testPort4MTPAsInteger.equals(0) ) {
					MsgHead = Language.translate("Fehler: Port4MTP ");
					MsgText = Language.translate("Der Port für die MTP-Adresse muss einem Wert ungleich 0 entsprechen!");	
					JOptionPane.showMessageDialog(this.optionDialog.getContentPane(), MsgText, MsgHead, JOptionPane.ERROR_MESSAGE);
					return true;
				}
			}
		}
		return err;
	}
	
	/**
	 * Doe's the actions when using the OK-Button.
	 */
	private void doOkAction() {
		
		isServerOld = Application.getGlobalInfo().isRunAsServer();
		isServerNew = this.jRadioButtonRunAsServer.isSelected();
		
		String newLine = Application.getGlobalInfo().getNewLineSeparator();
		String forceRestartTo = null;
		
		// --- Fehlerbehnaldung -------------------------------------
		if ( errorFound() == true) {
			return;
		}
		// --- If a change from 'Application' to 'Server' occures --- 
		if (isServerNew!=isServerOld) {
			if (isServerNew==true) {
				forceRestartTo = Language.translate("Server");
			} else {
				forceRestartTo = Language.translate("Anwendung");
			}
			
			// --------------------------------------------------------------
			// --- Neustart der Anwendung einleiten, weil von Server --------
			// --- auf Application umgestellt wurde oder umgekehrt ----------
			// --- Wenn der User das möchte !! ------------------------------
			// --------------------------------------------------------------
			String MsgHead = "";
			String MsgText = "";
			
			MsgHead += Language.translate("Agent.GUI umschalten ?");
			MsgText += Language.translate("Progamm umschalten auf") + " '" + forceRestartTo + "':" + newLine; 	
			MsgText += Language.translate("Möchten Sie Agent.GUI nun umschalten und neu starten ?");

			Integer MsgAnswer = JOptionPane.showConfirmDialog( this.optionDialog.getContentPane(), MsgText, MsgHead, JOptionPane.YES_NO_OPTION);
			if (MsgAnswer == JOptionPane.YES_OPTION ) {
				forceRestart = true;			
			} else {
				forceRestart = false;
				if (this.jRadioButtonRunAsServer.isSelected()) {
					this.jRadioButtonRunAsApplication.setSelected(true);
				} else {
					this.jRadioButtonRunAsServer.setSelected(true);
				}
				MsgHead = Language.translate("Umschaltung rückgängig gemacht!");
				MsgText =  Language.translate("Ihre Umschaltung zwischen 'Anwendung' und 'Server' wurde rückgängig gemacht.") + newLine;
				MsgText += Language.translate("Bitte wiederholen Sie den Vorgang bei Bedarf und bestätigen Sie dann mit 'Ja'.");
				JOptionPane.showMessageDialog(this.optionDialog.getContentPane(), MsgText, MsgHead, JOptionPane.OK_OPTION);
				return;
			}
			// --------------------------------------------------------------
		}
		this.setFromData2Global();
		Application.getGlobalInfo().getFileProperties().save();

		// ------------------------------------------------------------------
		this.applySettings();
		// ------------------------------------------------------------------
		this.optionDialog.setVisible(false);
		
	}

	/**
	 * Apply settings.
	 */
	private void applySettings(){
		
		if ((isServerOld==true && isServerNew==true) || forceRestart==true) { 
			// --- JADE beenden -------------------------------------
			Application.getJadePlatform().jadeStop();			
			// --- Anwendung neu starten ----------------------------
			if (isServerOld == true) {
				if (isServerNew==true) {
					// --- Neustart 'Server'-------------------------
					System.out.println(Language.translate("Neustart des Server-Dienstes"));
				} else {
					// --- Umschalten von 'Server' auf 'Application' ----
					System.out.println(Language.translate("Umschalten von 'Server' auf 'Anwendung'"));
				}				
				// --- Tray-Icon entfernen / schliessen -------------
				Application.getTrayIcon().remove();
				Application.setTrayIcon(null);

			} else {
				// --- Umschalten von 'Application' auf 'Server' ----
				System.out.println(Language.translate("Umschalten von 'Anwendung' auf 'Server'"));
				// --- Noch offene Projekte schließen ---------------
				if (Application.getProjectsLoaded()!= null) {
					if ( Application.getProjectsLoaded().closeAll() == false ) return;	
				}		
				// --- Anwendungsfenster schliessen -----------------
				Application.getMainWindow().dispose();
				Application.setMainWindow(null);
				// --- Tray-Icon schliessen -------------------------
				Application.getTrayIcon().remove();
				Application.setTrayIcon(null);
			}
			// --- Restart ------------------------------------------
			Application.startAgentGUI();
		}

	}

}  //  @jve:decl-index=0:visual-constraint="-3,8"
