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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.border.EtchedBorder;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.config.GlobalInfo.ExecutionMode;
import agentgui.core.config.GlobalInfo.MtpProtocol;

/**
 * On this JPanel the starting options of AgentGUI can be set.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class StartOptions extends AbstractOptionTab implements ActionListener {

	private static final long serialVersionUID = -5837814050254569584L;
	
	private OptionDialog optionDialog;

	private JPanel jPanelTop;
	private JRadioButton jRadioButtonRunAsApplication;
	private JRadioButton jRadioButtonRunAsServer;
	private JRadioButton jRadioButtonRunAsDeviceService;
	
	private JLabel jLabelRunsAs;
	private JButton jButtonApply;
	private JButton jButtonUseDefaults;
	
	private ExecutionMode executionModeOld = Application.getGlobalInfo().getExecutionMode();
	private ExecutionMode executionModeNew = Application.getGlobalInfo().getExecutionMode();

	
	private JPanel jPanel4ScrollPane;
	private JScrollPane jScrollPaneConfig;
	private JPanel jPanelConfig;

	private JPanelMasterConfiguration jPanelMasterConfiguration;
	private JPanelOwnMTP jPanelOwnMTP;
	private JPanelBackgroundSystem jPanelBackgroundService;
	private JPanelDatabase jPanelDatabase;
	private JPanelEmbeddedSystemAgent jPanelEmbeddedSystemAgent;
	private JPanelMTPConfig jPanelMTPConfig;

	private List<AbstractJPanelForOptions> optionPanels;
	

	/**
	 * This is the Constructor
	 */
	public StartOptions(OptionDialog optionDialog) {
		super(optionDialog);
		
		this.optionDialog = optionDialog;
		this.initialize();
		this.setGlobalData2Form();
		
		// --- Translate ----------------------------------
		jLabelRunsAs.setText(Application.getGlobalInfo().getApplicationTitle() + " - " + Language.translate("Starte als:"));
		
		jRadioButtonRunAsApplication.setText(Language.translate("Anwendung"));
		jRadioButtonRunAsServer.setText(Language.translate("Hintergrundsystem (Master / Slave)"));
		jRadioButtonRunAsDeviceService.setText(Language.translate("Dienst / Embedded System Agent"));
		jButtonApply.setText(Language.translate("Anwenden"));
		
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractOptionTab#getTitleAddition()
	 */
	@Override
	public String getTitle() {
		return Language.translate("Programmstart");
	}
	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractOptionTab#getTabToolTipText()
	 */
	@Override
	public String getTabToolTipText() {
		return Language.translate("Programmstart");
	}

	/**
	 * Gets the option panels.
	 * @return the option panels
	 */
	public List<AbstractJPanelForOptions> getOptionPanels() {
		if (optionPanels==null) {
			optionPanels = new ArrayList<AbstractJPanelForOptions>();
		}
		return optionPanels;
	}
	/**
	 * Register option panel.
	 * @param optionPanel the option panel
	 */
	private void registerOptionPanel(AbstractJPanelForOptions optionPanel) {
		this.getOptionPanels().add(optionPanel);
	}
	
	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		// --- Initiate all sub panels --------------------
		this.registerOptionPanel(this.getJPanelMasterConfiguration());
		this.registerOptionPanel(this.getJPanelOwnMTP());
		this.registerOptionPanel(this.getJPanelMTPConfig());
		this.registerOptionPanel(this.getJPanelBackgroundSystem());
		this.registerOptionPanel(this.getJPanelDatabase());
		this.registerOptionPanel(this.getJPanelEmbeddedSystemAgent());
		
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 0;
		gridBagConstraints3.fill = GridBagConstraints.BOTH;
		gridBagConstraints3.weightx = 1.0;
		gridBagConstraints3.weighty = 1.0;
		gridBagConstraints3.insets = new Insets(10, 10, 10, 10);
		gridBagConstraints3.gridy = 1;
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridx = 0;
		gridBagConstraints21.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints21.insets = new Insets(20, 20, 0, 20);
		gridBagConstraints21.anchor = GridBagConstraints.WEST;
		gridBagConstraints21.weightx = 1.0;
		gridBagConstraints21.weighty = 0.0;
		gridBagConstraints21.gridy = 0;
		
		this.setSize(770, 440);
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		this.add(this.getJPanelTop(), gridBagConstraints21);
		this.add(this.getJPanel4ScrollPane(), gridBagConstraints3);
		
	}
	/**
	 * This method initializes jPanel4ScrollPane	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel4ScrollPane() {
		if (jPanel4ScrollPane == null) {
			jPanel4ScrollPane = new JPanel();
			jPanel4ScrollPane.setLayout(new BorderLayout());
			jPanel4ScrollPane.add(this.getJScrollPaneConfig(), BorderLayout.CENTER);
		}
		return jPanel4ScrollPane;
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
			jRadioButtonRunAsApplication.setPreferredSize(new Dimension(300, 24));
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
			jRadioButtonRunAsServer.setText("Hintergrundsystem (Master / Slave)");
			jRadioButtonRunAsServer.setPreferredSize(new Dimension(300, 24));
			jRadioButtonRunAsServer.setActionCommand("runAsServer");
			jRadioButtonRunAsServer.addActionListener(this);
		}
		return jRadioButtonRunAsServer;
	}
	/**
	 * This method initializes jRadioButtonRunAsDevice	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadioButtonRunAsDeviceService() {
		if (jRadioButtonRunAsDeviceService == null) {
			jRadioButtonRunAsDeviceService = new JRadioButton();
			jRadioButtonRunAsDeviceService.setText("Dienst / Embedded System Agent");
			jRadioButtonRunAsDeviceService.setPreferredSize(new Dimension(300, 24));
			jRadioButtonRunAsDeviceService.setActionCommand("runAsEmbeddedSystemAgent");
			jRadioButtonRunAsDeviceService.addActionListener(this);
		}
		return jRadioButtonRunAsDeviceService;
	}
	
	/**
	 * This method initializes jPanelTop	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelTop() {
		if (jPanelTop == null) {
			GridBagConstraints gridBagConstraints28 = new GridBagConstraints();
			gridBagConstraints28.gridx = 1;
			gridBagConstraints28.anchor = GridBagConstraints.WEST;
			gridBagConstraints28.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints28.gridwidth = 1;
			gridBagConstraints28.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints28.gridy = 2;
			
			GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
			gridBagConstraints26.insets = new Insets(0, 2, 0, 0);
			gridBagConstraints26.gridy = 0;
			gridBagConstraints26.weightx = 0.0;
			gridBagConstraints26.gridx = 4;
			GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
			gridBagConstraints25.anchor = GridBagConstraints.EAST;
			gridBagConstraints25.gridx = 3;
			gridBagConstraints25.gridy = 0;
			gridBagConstraints25.weightx = 0.0;
			gridBagConstraints25.insets = new Insets(0, 20, 0, 0);
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.anchor = GridBagConstraints.WEST;
			gridBagConstraints18.gridx = 1;
			gridBagConstraints18.gridy = 0;
			gridBagConstraints18.insets = new Insets(10, 20, 0, 10);
			GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
			gridBagConstraints24.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints24.gridy = 1;
			gridBagConstraints24.ipadx = 0;
			gridBagConstraints24.anchor = GridBagConstraints.WEST;
			gridBagConstraints24.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints24.gridx = 1;
			GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
			gridBagConstraints23.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints23.gridy = 0;
			gridBagConstraints23.ipadx = 0;
			gridBagConstraints23.anchor = GridBagConstraints.SOUTH;
			gridBagConstraints23.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints23.weightx = 1.0;
			gridBagConstraints23.gridx = 1;
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.insets = new Insets(2, 0, 0, 0);
			gridBagConstraints20.gridy = 0;
			gridBagConstraints20.ipadx = 0;
			gridBagConstraints20.anchor = GridBagConstraints.WEST;
			gridBagConstraints20.weightx = 0.0;
			gridBagConstraints20.gridx = 0;
			
			jLabelRunsAs = new JLabel();
			jLabelRunsAs.setText("starte als:");
			jLabelRunsAs.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jPanelTop = new JPanel();
			jPanelTop.setLayout(new GridBagLayout());
			jPanelTop.add(jLabelRunsAs, gridBagConstraints20);
			jPanelTop.add(getJRadioButtonRunAsApplication(), gridBagConstraints23);
			jPanelTop.add(getJRadioButtonRunAsServer(), gridBagConstraints24);
			jPanelTop.add(getJButtonApply(), gridBagConstraints25);
			jPanelTop.add(getJButtonUseDefaults(), gridBagConstraints26);
			jPanelTop.add(getJRadioButtonRunAsDeviceService(), gridBagConstraints28);
			
			ButtonGroup runAsGroup = new ButtonGroup();
			runAsGroup.add(jRadioButtonRunAsApplication);
			runAsGroup.add(jRadioButtonRunAsServer);
			runAsGroup.add(jRadioButtonRunAsDeviceService);
			
		}
		return jPanelTop;
	}

	/**
	 * Gets the JPanelMasterConfiguration.
	 * @return the JPanelMasterConfiguration
	 */
	private JPanelMasterConfiguration getJPanelMasterConfiguration() {
		if (jPanelMasterConfiguration == null) {
			jPanelMasterConfiguration = new JPanelMasterConfiguration(this.optionDialog, this);
		}
		return jPanelMasterConfiguration;
	}
	/**
	 * Gets the JPanelOwnMTP.
	 * @return the JPanelOwnMTP
	 */
	private JPanelOwnMTP getJPanelOwnMTP() {
		if (jPanelOwnMTP == null) {
			jPanelOwnMTP = new JPanelOwnMTP(this.optionDialog, this);
		}
		return jPanelOwnMTP;
	}
	/**
	 * Gets the JPanelBackgroundSystem.
	 * @return the JPanelBackgroundSystem
	 */
	private JPanelBackgroundSystem getJPanelBackgroundSystem() {
		if (jPanelBackgroundService == null) {
			jPanelBackgroundService = new JPanelBackgroundSystem(this.optionDialog, this);
		}
		return jPanelBackgroundService;
	}
	/**
	 * Gets the JPanelDatabase.
	 * @return the JPanelDatabasee
	 */
	private JPanelDatabase getJPanelDatabase() {
		if (jPanelDatabase==null) {
			jPanelDatabase = new JPanelDatabase(this.optionDialog, this);
		}
		return jPanelDatabase;
	}
	/**
	 * Gets the JPanelEmbeddedSystemAgent.
	 * @return the JPanelEmbeddedSystemAgent
	 */
	private JPanelEmbeddedSystemAgent getJPanelEmbeddedSystemAgent() {
		if (jPanelEmbeddedSystemAgent == null) {
			jPanelEmbeddedSystemAgent = new JPanelEmbeddedSystemAgent(this.optionDialog, this);
		}
		return jPanelEmbeddedSystemAgent;
	}

	/**
	 * Gets the JPanelMTPConfig.
	 * @return the JPanelMTPConfig
	 */
	private JPanelMTPConfig getJPanelMTPConfig() {
		if (jPanelMTPConfig == null) {
			jPanelMTPConfig = new JPanelMTPConfig(this.optionDialog, this);
		}
		return jPanelMTPConfig;
	}
	
	
	
	/**
	 * This method initializes jButtonUpdateSiteDefault	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonUseDefaults() {
		if (jButtonUseDefaults == null) {
			jButtonUseDefaults = new JButton();
			jButtonUseDefaults.setPreferredSize(new Dimension(45, 26));
			jButtonUseDefaults.setIcon(GlobalInfo.getInternalImageIcon("MBreset.png"));
			jButtonUseDefaults.setToolTipText(Language.translate("Standard verwenden"));
			jButtonUseDefaults.setActionCommand("resetSettings");
			jButtonUseDefaults.addActionListener(this);
		}
		return jButtonUseDefaults;
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
	
	/**
	 * This method initializes jScrollPaneConfig	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPaneConfig() {
		if (jScrollPaneConfig == null) {
			jScrollPaneConfig = new JScrollPane();
			jScrollPaneConfig.setPreferredSize(new Dimension(100, 100));
			jScrollPaneConfig.getVerticalScrollBar().setUnitIncrement(10);
			jScrollPaneConfig.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		}
		return jScrollPaneConfig;
	}
	/**
	 * This method initializes jPanelConfig	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelConfig() {
		if (jPanelConfig == null) {
			jPanelConfig = new JPanel();
			jPanelConfig.setLayout(new GridBagLayout());
			jPanelConfig.setSize(new Dimension(600, 20));
			jPanelConfig.setPreferredSize(new Dimension(600, 20));
		}
		return jPanelConfig;
	}
	/**
	 * Resets the jPanelConfig
	 */
	private void resetJPanelConfigReset() {
		this.getJPanelConfig().removeAll();
		this.getJPanelConfig().setSize(new Dimension(600, 20));
		this.getJPanelConfig().setPreferredSize(new Dimension(600, 20));
	}
	/**
	 * Adds a new component to the jPanelConfig panel in vertical direction.
	 * @param newComponent the new component
	 */
	private void addToConfigPanel(Component newComponent) {
		
		int compsThere = this.getJPanelConfig().getComponentCount();
		
		GridBagConstraints gridBagC = new GridBagConstraints();
		gridBagC.gridx = 0;
		gridBagC.gridy = compsThere;
		gridBagC.fill = GridBagConstraints.NONE;
		gridBagC.insets = new Insets(10, 10, 5, 10);
		gridBagC.weightx = 1.0;
		gridBagC.anchor = GridBagConstraints.WEST;
		
		// --- Get old panel height -------------
		int oldPanelHeight = this.getJPanelConfig().getHeight();
		int oldPanelWidth = this.getJPanelConfig().getWidth();
		
		// --- new Component height -------------
		int newCompHeight = newComponent.getHeight();
		int newCompPrefHeight = (int) newComponent.getPreferredSize().getHeight();
		if (newCompPrefHeight>newCompHeight) newCompHeight = newCompPrefHeight;
		int newPanelHeight = oldPanelHeight + newCompHeight + gridBagC.insets.top + gridBagC.insets.bottom + 10; 
		
		// --- Set size and add component -------
		this.getJPanelConfig().setSize(new Dimension(oldPanelWidth, newPanelHeight));
		this.getJPanelConfig().setPreferredSize(new Dimension(oldPanelWidth, newPanelHeight));
		this.getJPanelConfig().add(newComponent, gridBagC);
		this.addHorizontalSeparator();
		
		this.getJPanelConfig().revalidate();
		this.getJPanelConfig().repaint();
	}
	/**
	 * Adds a horizontal separator to the local config panel.
	 */
	private void addHorizontalSeparator() {
		
		int compsThere = this.getJPanelConfig().getComponentCount();
		
		GridBagConstraints gridBagC = new GridBagConstraints();
		gridBagC.fill = GridBagConstraints.HORIZONTAL;
		gridBagC.gridx = 0;
		gridBagC.gridy = compsThere;
		gridBagC.fill = GridBagConstraints.HORIZONTAL;
		gridBagC.insets = new Insets(0, 10, 0, 10);
		this.getJPanelConfig().add(new JSeparator(), gridBagC);
	}
	/**
	 * Adds the to config panel completion.
	 */
	private void addToConfigPanelCompletion() {
		
		int compsThere = this.getJPanelConfig().getComponentCount();
		
		GridBagConstraints gridBagC = new GridBagConstraints();
		gridBagC.gridx = 0;
		gridBagC.gridy = compsThere;
		gridBagC.fill = GridBagConstraints.BOTH;
		gridBagC.insets = new Insets(10, 10, 5, 10);
		gridBagC.weightx = 1.0;
		gridBagC.weighty = 1.0;

		JLabel jLabelDummy = new JLabel("");

		this.getJPanelConfig().add(jLabelDummy, gridBagC);
		this.getJPanelConfig().revalidate();
		this.getJPanelConfig().repaint();
	}
	
	/**
	 * Returns the selected execution mode.
	 * @return the selected execution mode
	 */
	public ExecutionMode getSelectedExecutionMode() {
		return executionModeNew;
	}
	/**
	 * This method handles and refreshes the view 
	 */
	private void refreshView() {
		
		// --- Reset the configuration panel first --------
		this.resetJPanelConfigReset();
		
		// --- Add the components as needed ---------------
		switch (this.getSelectedExecutionMode()) {
		case APPLICATION:
			this.addToConfigPanel(this.getJPanelMasterConfiguration());
			this.addToConfigPanel(this.getJPanelOwnMTP());
			this.addToConfigPanel(this.getJPanelMTPConfig());
			break;

		case SERVER:
		case SERVER_MASTER:
		case SERVER_SLAVE:
			this.addToConfigPanel(this.getJPanelMasterConfiguration());
			this.addToConfigPanel(this.getJPanelOwnMTP());
			this.addToConfigPanel(this.getJPanelMTPConfig());
			this.addToConfigPanel(this.getJPanelBackgroundSystem());
			this.addToConfigPanel(this.getJPanelDatabase());
			break;
			
		case DEVICE_SYSTEM:
			this.addToConfigPanel(this.getJPanelMasterConfiguration());
			this.addToConfigPanel(this.getJPanelOwnMTP());
			this.addToConfigPanel(this.getJPanelMTPConfig());
			this.addToConfigPanel(this.getJPanelEmbeddedSystemAgent());
			break;

		}

		// --- Refresh view of registered option panels ---
		for (AbstractJPanelForOptions optionPanel : this.getOptionPanels()) {
			optionPanel.refreshView();
		}
		
		// --- Add completion dummy JLabel ----------------
		this.addToConfigPanelCompletion();
		
		// --- Refresh the view in the scroll pane --------
		this.getJScrollPaneConfig().setViewportView(this.getJPanelConfig());
		this.getJScrollPaneConfig().revalidate();
		this.getJScrollPaneConfig().repaint();
	}
	
	/**
	 * This method sets the Data from the global Area to the Form.
	 */
	private void setGlobalData2Form(){
		
		switch (Application.getGlobalInfo().getExecutionMode()) {
		case APPLICATION:
			this.getJRadioButtonRunAsApplication().setSelected(true);
			this.getJRadioButtonRunAsServer().setSelected(false);
			this.getJRadioButtonRunAsDeviceService().setSelected(false);
			break;
			
		case SERVER:
		case SERVER_MASTER:
		case SERVER_SLAVE:
			this.getJRadioButtonRunAsApplication().setSelected(false);
			this.getJRadioButtonRunAsServer().setSelected(true);
			this.getJRadioButtonRunAsDeviceService().setSelected(false);
			break;

		case DEVICE_SYSTEM:
			this.getJRadioButtonRunAsApplication().setSelected(false);
			this.getJRadioButtonRunAsServer().setSelected(false);
			this.getJRadioButtonRunAsDeviceService().setSelected(true);
			break;
		}
		
		// --- Do the same action in the option panels ----
		for (AbstractJPanelForOptions optionPanel : this.getOptionPanels()) {
			optionPanel.setGlobalData2Form();
		}
		this.refreshView();
	}
	
	/**
	 * This method writes the data back from the form to the global area.
	 */
	private void setFormData2Global() {
		
		ExecutionMode newMode = null;
		if (this.jRadioButtonRunAsApplication.isSelected()) {
			newMode = ExecutionMode.APPLICATION;
		} else if (this.jRadioButtonRunAsServer.isSelected()) {
			newMode = ExecutionMode.SERVER;
		} else if (this.jRadioButtonRunAsDeviceService.isSelected()) {
			newMode = ExecutionMode.DEVICE_SYSTEM;
		}
		if (newMode!=null) {
			Application.getGlobalInfo().setExecutionMode(newMode);
		}
		
		// --- Do the same action in the option panels ----
		for (AbstractJPanelForOptions optionPanel : this.getOptionPanels()) {
			optionPanel.setFormData2Global();
		}
	}
	
	/**
	 * This method doe's the Error-Handling for this Dialog.
	 * @return true or false
	 */
	private boolean errorFound() {
		// --- Do the same action in the option panels ----
		for (AbstractJPanelForOptions optionPanel : this.getOptionPanels()) {
			if (optionPanel.errorFound()) return true;
		}
		return false;
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
			String msgHead = "";
			String msgText = "";
			
			msgHead += Application.getGlobalInfo().getApplicationTitle() + ": " + Language.translate("Anwendung umschalten ?");
			msgText += Language.translate("Progamm umschalten auf") + " '" + executionModeTextNew + "':" + newLine; 	
			msgText += Language.translate("Möchten Sie die Anwendung nun umschalten ?");

			int msgAnswer = JOptionPane.showConfirmDialog(this.optionDialog, msgText, msgHead, JOptionPane.YES_NO_OPTION);
			if (msgAnswer==JOptionPane.NO_OPTION) {
				return;
			}
			// ----------------------------------------------------------------
		}
		this.setFormData2Global();
		Application.getGlobalInfo().doSaveConfiguration();
		// --------------------------------------------------------------------
		this.applySettings();
		// --------------------------------------------------------------------
		this.optionDialog.setVisible(false);
	}

	/**
	 * Apply settings.
	 */
	private void applySettings(){
		
		if (this.executionModeNew==this.executionModeOld) {
			// ------------------------------------------------------
			// --- Same ExecutionMode -------------------------------
			// ------------------------------------------------------
			switch (this.executionModeOld) {
			case APPLICATION:
				// --- Do nothing in this case ----------------------
				break;
			
			case SERVER:
			case SERVER_MASTER:
			case SERVER_SLAVE:
				// --- Background System Modus ----------------------
				System.out.println("\n" + Language.translate("Neustart des Server-Dienstes") + " ...");
				Application.getJadePlatform().stop();
				Application.removeTrayIcon();
				Application.startAgentWorkbench();
				break;
				
			case DEVICE_SYSTEM:
				// --- Device / Embedded System Agent ---------------
				System.out.println("\n" + Language.translate("Neustart") + " " + Application.getGlobalInfo().getExecutionModeDescription(this.executionModeNew) + " ...");
				Application.getJadePlatform().stop();
				if (Application.getProjectsLoaded()!= null) {
					if (Application.getProjectsLoaded().closeAll(this.optionDialog, true)==false) return;	
				}		
				Application.setMainWindow(null);
				Application.removeTrayIcon();	
				Application.startAgentWorkbench();
				break;
			}
			
		} else {
			// ------------------------------------------------------
			// --- New ExecutionMode --------------------------------
			// ------------------------------------------------------
			String textPrefix = Language.translate("Umschalten von");
			String executionModeTextOld = Application.getGlobalInfo().getExecutionModeDescription(this.executionModeOld);
			String textMiddle = Language.translate("auf");
			String executionModeTextNew = Application.getGlobalInfo().getExecutionModeDescription(this.executionModeNew);
			System.out.println(textPrefix + " '" + executionModeTextOld + "' " + textMiddle + " '" + executionModeTextNew + "'");
			
			// ------------------------------------------------------
			// --- Controlled shutdown of the current execution -----
			Application.getJadePlatform().stop();
			
			// --- Case separation for current ExecutionMode --------
			switch (this.executionModeOld) {
			case APPLICATION:
				// --- Application Modus ----------------------------
				if (Application.getProjectsLoaded()!= null) {
					if (Application.getProjectsLoaded().closeAll(this.optionDialog) == false ) return;	
				}		
				// --- Close main window and TrayIcon ---------------
				Application.setMainWindow(null);
				Application.removeTrayIcon();
				break;

			case SERVER:
			case SERVER_MASTER:
			case SERVER_SLAVE:
				// --- Background System Modus ----------------------
				Application.removeTrayIcon();
				break;
				
			case DEVICE_SYSTEM:
				// --- Device / Embedded System Agent ---------------
				if (Application.getProjectsLoaded()!= null) {
					if (Application.getProjectsLoaded().closeAll(this.optionDialog, true)==false) return;	
				}		
				Application.setMainWindow(null);
				Application.removeTrayIcon();	
				Application.setLogFileWriter(null);
				break;
			}
			// --- Restart ------------------------------------------
			Application.startAgentWorkbench();
		}
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
			} else if (getJRadioButtonRunAsDeviceService().isSelected()) {
				this.executionModeNew = ExecutionMode.DEVICE_SYSTEM;
			}
			this.refreshView();
			
		} else if (actCMD.equalsIgnoreCase("resetSettings")) {
			this.executionModeNew = this.executionModeOld;
			this.setGlobalData2Form();
			this.getJPanelMTPConfig().getJComboBoxMtpProtocol().setSelectedProtocol(MtpProtocol.HTTP);
			this.getJPanelMTPConfig().hideCertificateSettings();
			
		} else if (actCMD.equalsIgnoreCase("applySettings")) {
			if (getJPanelMTPConfig().getJComboBoxMtpProtocol().getSelectedProtocol()!= getJPanelMasterConfiguration().getJcomboboxMtpProtocol().getSelectedProtocol()) {
				String title = Language.translate("Different MTP-protocols configured!", Language.EN); 
				String msg = Language.translate("Please, choose the same Protocol for the server.master and the MTP-protocol!", Language.EN);
				JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
			} else {
				this.doOkAction();
			}
		} else {
			System.err.println(Language.translate("Unbekannt: ") + "ActionCommand => " + actCMD);
		}
	}
}  
