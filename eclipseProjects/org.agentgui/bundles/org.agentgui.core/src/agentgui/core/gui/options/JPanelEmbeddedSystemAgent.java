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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo;
import agentgui.core.config.GlobalInfo.DeviceSystemExecutionMode;
import agentgui.core.config.GlobalInfo.EmbeddedSystemAgentVisualisation;
import agentgui.core.config.GlobalInfo.ExecutionMode;
import agentgui.core.project.Project;
import de.enflexit.common.classSelection.ClassSelectionDialog;
import de.enflexit.common.classSelection.JListClassSearcher;
import jade.core.Agent;

/**
 * The Class JPanelEmbeddedSystemAgent.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class JPanelEmbeddedSystemAgent extends AbstractJPanelForOptions implements ActionListener {

	private static final long serialVersionUID = 524638435001219047L;

	private Project esaProjectSelected;
	
	private JLabel jLabelEmbeddedHeader;
	private JLabel jLabelProject;
	private JComboBox<String> jComboBoxProjectSelector;
	
	private JPanel jPanelExecution;
	private JLabel jLabelExecuteAs;
	private JRadioButton jRadioButtonExecuteAsServerService;
	private JRadioButton jRadioButtonExecuteAsDeviceAgent;

	private JLabel jLabelSetupHeader;
	private JLabel jLabelSetup;
	private JComboBox<String> jComboBoxSetupSelector;

	private JLabel jLabelAgentHeader;
	private JLabel jLabelAgent;
	private JTextField jTextFieldAgentClass;;
	private JButton jButtonSelectAgentClass;
	private ClassSelectionDialog esaClassSelector;
	
	private JLabel jLabelVisConfig;
	private JRadioButton jRadioButtonVisNon;
	private JRadioButton jRadioButtonVisTrayIcon;
	private JLabel jLabelAgentName;
	private JTextField jTextFieldAgentName;
	
	
	/**
	 * This is the Constructor.
	 * @param optionDialog the option dialog
	 */
	public JPanelEmbeddedSystemAgent(OptionDialog optionDialog, StartOptions startOptions) {
		super(optionDialog, startOptions);
		this.initialize();
		// --- Translate ----------------------------------
	}
	/**
	 * Initialise.
	 */
	private void initialize() {
		
		
		this.setPreferredSize(new Dimension(570, 340));
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{82, 430, 45, 0};
		gridBagLayout.rowHeights = new int[]{16, 26, 23, 16, 26, 16, 26, 26, 16, 24, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		this.setLayout(gridBagLayout);
		
		jLabelEmbeddedHeader = new JLabel(this.getGlobalInfo().getApplicationTitle() + " - " + Language.translate("Dienst") + " / Embedded System Agent");
		jLabelEmbeddedHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		GridBagConstraints gbc_jLabelEmbeddedHeader = new GridBagConstraints();
		gbc_jLabelEmbeddedHeader.anchor = GridBagConstraints.WEST;
		gbc_jLabelEmbeddedHeader.insets = new Insets(0, 0, 15, 0);
		gbc_jLabelEmbeddedHeader.gridwidth = 2;
		gbc_jLabelEmbeddedHeader.gridx = 0;
		gbc_jLabelEmbeddedHeader.gridy = 0;
		this.add(jLabelEmbeddedHeader, gbc_jLabelEmbeddedHeader);
		
		jLabelProject = new JLabel();
		jLabelProject.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelProject.setText("Projekt");
		jLabelProject.setText(Language.translate(jLabelProject.getText()) + ":");
		GridBagConstraints gbc_jLabelProject = new GridBagConstraints();
		gbc_jLabelProject.anchor = GridBagConstraints.WEST;
		gbc_jLabelProject.insets = new Insets(0, 0, 5, 5);
		gbc_jLabelProject.gridx = 0;
		gbc_jLabelProject.gridy = 1;
		this.add(jLabelProject, gbc_jLabelProject);
		GridBagConstraints gbc_jComboBoxProjectSelector = new GridBagConstraints();
		gbc_jComboBoxProjectSelector.anchor = GridBagConstraints.WEST;
		gbc_jComboBoxProjectSelector.insets = new Insets(0, 0, 5, 5);
		gbc_jComboBoxProjectSelector.gridx = 1;
		gbc_jComboBoxProjectSelector.gridy = 1;
		this.add(getJComboBoxProjectSelector(), gbc_jComboBoxProjectSelector);
		
		jLabelExecuteAs = new JLabel();
		jLabelExecuteAs.setText("Ausführen als");
		jLabelExecuteAs.setText(Language.translate(jLabelExecuteAs.getText()) + ":");
		jLabelExecuteAs.setFont(new Font("Dialog", Font.BOLD, 12));
		GridBagConstraints gbc_jLabelExecuteAs = new GridBagConstraints();
		gbc_jLabelExecuteAs.anchor = GridBagConstraints.WEST;
		gbc_jLabelExecuteAs.insets = new Insets(0, 0, 5, 5);
		gbc_jLabelExecuteAs.gridx = 0;
		gbc_jLabelExecuteAs.gridy = 2;
		this.add(jLabelExecuteAs, gbc_jLabelExecuteAs);
		GridBagConstraints gbc_jPanelExecution = new GridBagConstraints();
		gbc_jPanelExecution.anchor = GridBagConstraints.WEST;
		gbc_jPanelExecution.insets = new Insets(0, 0, 5, 5);
		gbc_jPanelExecution.gridx = 1;
		gbc_jPanelExecution.gridy = 2;
		this.add(getJPanelExecution(), gbc_jPanelExecution);
		
		jLabelSetupHeader = new JLabel();
		jLabelSetupHeader.setText("Dienst");
		jLabelSetupHeader.setText(Language.translate(jLabelSetupHeader.getText()));
		jLabelSetupHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		GridBagConstraints gbc_jLabelSetupHeader = new GridBagConstraints();
		gbc_jLabelSetupHeader.anchor = GridBagConstraints.WEST;
		gbc_jLabelSetupHeader.insets = new Insets(10, 0, 5, 5);
		gbc_jLabelSetupHeader.gridx = 0;
		gbc_jLabelSetupHeader.gridy = 3;
		this.add(jLabelSetupHeader, gbc_jLabelSetupHeader);
		
		jLabelSetup = new JLabel();
		jLabelSetup.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelSetup.setText("Setup:");
		jLabelSetup.setText(Language.translate(jLabelSetup.getText()));
		GridBagConstraints gbc_jLabelSetup = new GridBagConstraints();
		gbc_jLabelSetup.anchor = GridBagConstraints.WEST;
		gbc_jLabelSetup.insets = new Insets(0, 0, 5, 5);
		gbc_jLabelSetup.gridx = 0;
		gbc_jLabelSetup.gridy = 4;
		this.add(jLabelSetup, gbc_jLabelSetup);
		GridBagConstraints gbc_jComboBoxSetupSelector = new GridBagConstraints();
		gbc_jComboBoxSetupSelector.anchor = GridBagConstraints.WEST;
		gbc_jComboBoxSetupSelector.fill = GridBagConstraints.VERTICAL;
		gbc_jComboBoxSetupSelector.insets = new Insets(0, 0, 5, 5);
		gbc_jComboBoxSetupSelector.gridx = 1;
		gbc_jComboBoxSetupSelector.gridy = 4;
		this.add(getJComboBoxSetupSelector(), gbc_jComboBoxSetupSelector);
		
		jLabelAgentHeader = new JLabel();
		jLabelAgentHeader.setText("Embedded System Agent");
		jLabelAgentHeader.setText(Language.translate(jLabelAgentHeader.getText()));
		jLabelAgentHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		GridBagConstraints gbc_jLabelAgentHeader = new GridBagConstraints();
		gbc_jLabelAgentHeader.anchor = GridBagConstraints.WEST;
		gbc_jLabelAgentHeader.insets = new Insets(10, 0, 5, 5);
		gbc_jLabelAgentHeader.gridwidth = 2;
		gbc_jLabelAgentHeader.gridx = 0;
		gbc_jLabelAgentHeader.gridy = 5;
		this.add(jLabelAgentHeader, gbc_jLabelAgentHeader);
		
		jLabelAgent = new JLabel();
		jLabelAgent.setFont(new Font("Dialog", Font.BOLD, 12));
		jLabelAgent.setText("Agent:");
		GridBagConstraints gbc_jLabelAgent = new GridBagConstraints();
		gbc_jLabelAgent.anchor = GridBagConstraints.WEST;
		gbc_jLabelAgent.insets = new Insets(0, 0, 5, 5);
		gbc_jLabelAgent.gridx = 0;
		gbc_jLabelAgent.gridy = 6;
		this.add(jLabelAgent, gbc_jLabelAgent);
		GridBagConstraints gbc_jTextFieldAgentClass = new GridBagConstraints();
		gbc_jTextFieldAgentClass.fill = GridBagConstraints.BOTH;
		gbc_jTextFieldAgentClass.insets = new Insets(0, 0, 5, 5);
		gbc_jTextFieldAgentClass.gridx = 1;
		gbc_jTextFieldAgentClass.gridy = 6;
		this.add(getJTextFieldAgentClass(), gbc_jTextFieldAgentClass);
		GridBagConstraints gbc_jButtonSelectAgentClass = new GridBagConstraints();
		gbc_jButtonSelectAgentClass.insets = new Insets(0, 0, 5, 0);
		gbc_jButtonSelectAgentClass.gridx = 2;
		gbc_jButtonSelectAgentClass.gridy = 6;
		this.add(getJButtonEsaSelectAgent(), gbc_jButtonSelectAgentClass);
		
		jLabelAgentName = new JLabel("Agent Name:");
		jLabelAgentName.setFont(new Font("Dialog", Font.BOLD, 12));
		GridBagConstraints gbc_jLabelAgentName = new GridBagConstraints();
		gbc_jLabelAgentName.anchor = GridBagConstraints.WEST;
		gbc_jLabelAgentName.insets = new Insets(0, 0, 5, 5);
		gbc_jLabelAgentName.gridx = 0;
		gbc_jLabelAgentName.gridy = 7;
		this.add(jLabelAgentName, gbc_jLabelAgentName);
	
		GridBagConstraints gbc_jTextFieldAgentName = new GridBagConstraints();
		gbc_jTextFieldAgentName.fill = GridBagConstraints.BOTH;
		gbc_jTextFieldAgentName.insets = new Insets(0, 0, 5, 5);
		gbc_jTextFieldAgentName.gridx = 1;
		gbc_jTextFieldAgentName.gridy = 7;
		this.add(getJTextFieldAgentName(), gbc_jTextFieldAgentName);
		
		jLabelVisConfig = new JLabel();
		jLabelVisConfig.setText("Agent.GUI - Anwendungsvisualisierungen");
		jLabelVisConfig.setText(Language.translate(jLabelVisConfig.getText()));
		jLabelVisConfig.setFont(new Font("Dialog", Font.BOLD, 12));
		GridBagConstraints gbc_jLabelVisConfig = new GridBagConstraints();
		gbc_jLabelVisConfig.anchor = GridBagConstraints.WEST;
		gbc_jLabelVisConfig.insets = new Insets(10, 0, 5, 5);
		gbc_jLabelVisConfig.gridwidth = 2;
		gbc_jLabelVisConfig.gridx = 0;
		gbc_jLabelVisConfig.gridy = 8;
		this.add(jLabelVisConfig, gbc_jLabelVisConfig);
		GridBagConstraints gbc_jRadioButtonVisTrayIcon = new GridBagConstraints();
		gbc_jRadioButtonVisTrayIcon.anchor = GridBagConstraints.WEST;
		gbc_jRadioButtonVisTrayIcon.insets = new Insets(0, 0, 5, 5);
		gbc_jRadioButtonVisTrayIcon.gridx = 1;
		gbc_jRadioButtonVisTrayIcon.gridy = 9;
		this.add(getJRadioButtonVisTrayIcon(), gbc_jRadioButtonVisTrayIcon);
		
		ButtonGroup visAsGroup = new ButtonGroup();
		visAsGroup.add(getJRadioButtonVisNon());
		visAsGroup.add(getJRadioButtonVisTrayIcon());
		GridBagConstraints gbc_jRadioButtonVisNon = new GridBagConstraints();
		gbc_jRadioButtonVisNon.anchor = GridBagConstraints.WEST;
		gbc_jRadioButtonVisNon.insets = new Insets(0, 0, 0, 5);
		gbc_jRadioButtonVisNon.gridx = 1;
		gbc_jRadioButtonVisNon.gridy = 10;
		this.add(getJRadioButtonVisNon(), gbc_jRadioButtonVisNon);
	}
	
	
	/**
	 * This method initializes jComboBoxProjectSelector	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox<String> getJComboBoxProjectSelector() {
		if (jComboBoxProjectSelector == null) {
			
			DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<String>();
			comboBoxModel.addElement("");
			String[] projectFolders = getGlobalInfo().getProjectSubDirectories();
			if (projectFolders!=null && projectFolders.length>0) {
				for (int i = 0; i < projectFolders.length; i++) {
					comboBoxModel.addElement(projectFolders[i]);	
				}	
			}
			jComboBoxProjectSelector = new JComboBox<String>(comboBoxModel);
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
			jButtonSelectAgentClass.setIcon(GlobalInfo.getInternalImageIcon("Search.png"));
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
			gridBagConstraints44.anchor = GridBagConstraints.WEST;
			gridBagConstraints44.gridx = 1;
			gridBagConstraints44.insets = new Insets(0, 10, 0, 0);
			gridBagConstraints44.gridy = 0;
			GridBagConstraints gridBagConstraints42 = new GridBagConstraints();
			gridBagConstraints42.anchor = GridBagConstraints.WEST;
			gridBagConstraints42.gridx = 0;
			gridBagConstraints42.gridy = 0;
			
			jPanelExecution = new JPanel();
			jPanelExecution.setPreferredSize(new Dimension(300, 26));
			GridBagLayout gbl_jPanelExecution = new GridBagLayout();
			gbl_jPanelExecution.columnWeights = new double[]{1.0, 1.0};
			jPanelExecution.setLayout(gbl_jPanelExecution);
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
			jRadioButtonExecuteAsServerService.setFont(new Font("Dialog", Font.PLAIN, 12));
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
			jRadioButtonExecuteAsDeviceAgent.setFont(new Font("Dialog", Font.PLAIN, 12));
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
			
			DefaultComboBoxModel<String> comboBoxModelSetup = new DefaultComboBoxModel<String>();
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
	private JComboBox<String> getJComboBoxSetupSelector() {
		if (jComboBoxSetupSelector == null) {
			jComboBoxSetupSelector = new JComboBox<String>();
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
	 * Gets the jTextFieldAgentName.
	 * @return the jTextFieldAgentName
	 */
	private JTextField getJTextFieldAgentName(){
		if( jTextFieldAgentName == null){
			jTextFieldAgentName = new JTextField();
			jTextFieldAgentName.setPreferredSize(new Dimension(300, 26));
		}
		return jTextFieldAgentName;
	}
	/**
	 * This method initializes jRadioButtonVisNon	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadioButtonVisNon() {
		if (jRadioButtonVisNon == null) {
			jRadioButtonVisNon = new JRadioButton();
			jRadioButtonVisNon.setFont(new Font("Dialog", Font.PLAIN, 12));
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
			jRadioButtonVisTrayIcon.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRadioButtonVisTrayIcon.setText("Agent.GUI - Tray Icon anzeigen");
			jRadioButtonVisTrayIcon.setText(Language.translate(jRadioButtonVisTrayIcon.getText()));
			jRadioButtonVisTrayIcon.setPreferredSize(new Dimension(300, 24));
			jRadioButtonVisTrayIcon.setActionCommand("visTrayIcon");
			jRadioButtonVisTrayIcon.addActionListener(this);
		}
		return jRadioButtonVisTrayIcon;
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
			this.getJTextFieldAgentName().setText(null);
			if (this.esaClassSelector!=null) {
				this.esaClassSelector.dispose();
				this.esaClassSelector = null;	
			}
			
		} else {
			// --- Project was selected -------------------
			Project selectedProject = Project.load(projectFolderSelected);
			if (this.esaProjectSelected==null) {
				this.esaProjectSelected = selectedProject;
				this.setComboBoxModelSetup();
				this.getJTextFieldAgentClass().setText(null);
				this.getJTextFieldAgentName().setText(null);
				this.getClassSelector4ProjectAgents(selectedProject);
				
			} else {
				// --- Is this still the same project? ----
				String selectedProjectFolder = selectedProject.getProjectFolder();
				String currentProjectFolder = this.esaProjectSelected.getProjectFolder();
				if (selectedProjectFolder.equals(currentProjectFolder)==false) {
					// --- Reinitialise settings ---------- 
					this.esaProjectSelected = null;
					this.getJTextFieldAgentClass().setText(null);
					this.getJTextFieldAgentName().setText(null);
					this.esaClassSelector.dispose();
					this.esaClassSelector = null;
					// --- Now reinitialise ---------------
					this.esaProjectSelected = selectedProject;
					this.setComboBoxModelSetup();
					this.getJTextFieldAgentClass().setText(null);
					this.getJTextFieldAgentName().setText(null);
					this.getClassSelector4ProjectAgents(selectedProject);

				}
			}
			
		}
		this.refreshViewDeviceSystem();
	}
	
	/**
	 * Gets the class selector for project agents.
	 * @param project the project
	 * @return the class selector for project agents
	 */
	private ClassSelectionDialog getClassSelector4ProjectAgents(Project project) {
		String currAgentClass = this.getJTextFieldAgentClass().getText();
		if (this.esaClassSelector==null) {
			JListClassSearcher jListClassSearcher = new JListClassSearcher(Agent.class, project.getBundleNames());
			this.esaClassSelector = new ClassSelectionDialog(this.optionDialog, jListClassSearcher, currAgentClass, null, Language.translate("Bitte wählen Sie den Agenten aus, der gestartet werden soll"), false);
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
		ClassSelectionDialog cs = this.getClassSelector4ProjectAgents(this.esaProjectSelected);
		cs.setVisible(true);
		// --- act in the dialog ... ----------------------
		if (cs.isCanceled()==true) return;
		this.getJTextFieldAgentClass().setText(cs.getClassSelected());
		this.getJTextFieldAgentName().setText(null);
	}
	
	
	/**
	 * Refresh the view of the device system configuration.
	 */
	private void refreshViewDeviceSystem() {
		
		// --- Component enable / disable actions ----- 
		if (this.getJComboBoxProjectSelector().getSelectedItem()==null || this.getJComboBoxProjectSelector().getSelectedItem().equals("")==true) {
			
			jLabelExecuteAs.setEnabled(false);
			jLabelSetupHeader.setEnabled(false);
			jLabelSetup.setEnabled(false);
			jLabelAgentHeader.setEnabled(false);
			jLabelAgent.setEnabled(false);
			jLabelAgentName.setEnabled(false);
			jLabelVisConfig.setEnabled(false);
			
			this.getJRadioButtonExecuteAsService().setEnabled(false);
			this.getJRadioButtonExecuteAsDeviceAgent().setEnabled(false);
			this.getJComboBoxSetupSelector().setEnabled(false);
			this.getJTextFieldAgentClass().setEnabled(false);
			this.getJTextFieldAgentName().setEnabled(false);
			this.getJButtonEsaSelectAgent().setEnabled(false);
			this.getJRadioButtonVisNon().setEnabled(false);
			this.getJRadioButtonVisTrayIcon().setEnabled(false);
		
		} else {

			jLabelExecuteAs.setEnabled(true);
			jLabelSetupHeader.setEnabled(true);
			jLabelSetup.setEnabled(true);
			this.getJRadioButtonExecuteAsService().setEnabled(true);
			this.getJRadioButtonExecuteAsDeviceAgent().setEnabled(true);
			this.getJComboBoxSetupSelector().setEnabled(true);
			
			if (this.getJRadioButtonExecuteAsService().isSelected()==false && this.getJRadioButtonExecuteAsDeviceAgent().isSelected()==false) {
				this.getJTextFieldAgentClass().setText(null);
				this.getJTextFieldAgentName().setText(null);
				
				jLabelAgentHeader.setEnabled(false);
				jLabelAgent.setEnabled(false);
				jLabelAgentName.setEnabled(false);
				jLabelVisConfig.setEnabled(false);

				this.getJTextFieldAgentClass().setEnabled(false);
				this.getJTextFieldAgentName().setEnabled(false);
				this.getJButtonEsaSelectAgent().setEnabled(false);
				this.getJRadioButtonVisNon().setEnabled(false);
				this.getJRadioButtonVisTrayIcon().setEnabled(false);
				
			} else if (this.getJRadioButtonExecuteAsService().isSelected()) {
				this.getJTextFieldAgentClass().setText(null);
				this.getJTextFieldAgentName().setText(null);

				jLabelAgentHeader.setEnabled(false);
				jLabelAgent.setEnabled(false);
				jLabelAgentName.setEnabled(false);
				jLabelVisConfig.setEnabled(false);
				this.getJTextFieldAgentClass().setEnabled(false);
				this.getJTextFieldAgentName().setEnabled(false);
				this.getJButtonEsaSelectAgent().setEnabled(false);
				this.getJRadioButtonVisNon().setEnabled(false);
				this.getJRadioButtonVisTrayIcon().setEnabled(false);
				
				this.getJComboBoxSetupSelector().setEnabled(true);

			} else if (this.getJRadioButtonExecuteAsDeviceAgent().isSelected()) {
				
				jLabelAgentHeader.setEnabled(true);
				jLabelAgent.setEnabled(true);
				jLabelAgentName.setEnabled(true);
				jLabelVisConfig.setEnabled(true);
				this.getJTextFieldAgentClass().setEnabled(true);
				this.getJTextFieldAgentName().setEnabled(true);
				this.getJButtonEsaSelectAgent().setEnabled(true);
				this.getJRadioButtonVisNon().setEnabled(true);
				this.getJRadioButtonVisTrayIcon().setEnabled(true);
				
				this.getJComboBoxSetupSelector().setEnabled(false);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractJPanelForOptions#refreshView()
	 */
	@Override
	public void refreshView() {

		// --- Add the components as needed ---------------
		switch (this.getSelectedExecutionMode()) {
		case APPLICATION:
		default:
			// --- Reset DEVICE_SYSTEM variables ----------
			this.getJComboBoxProjectSelector().setSelectedItem(null);
			this.getJComboBoxSetupSelector().setSelectedItem(null);
			this.getJTextFieldAgentClass().setText(null);
			this.getJTextFieldAgentName().setText(null);
			this.esaClassSelector = null;
			break;

		case SERVER:
			// --- Reset DEVICE_SYSTEM variables ----------
			this.getJComboBoxProjectSelector().setSelectedItem(null);
			this.getJComboBoxSetupSelector().setSelectedItem(null);
			this.getJTextFieldAgentClass().setText(null);
			this.getJTextFieldAgentName().setText(null);
			this.esaClassSelector = null;
			break;
			
		case DEVICE_SYSTEM:
			this.refreshViewDeviceSystem();
			break;

		}
	}
	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractJPanelForOptions#setGlobalData2Form()
	 */
	@Override
	public void setGlobalData2Form() {
		
		if (getGlobalInfo().getExecutionMode()==ExecutionMode.DEVICE_SYSTEM) {
			// --- In case of a Service / Embedded System Agent ---------		
			this.getJComboBoxProjectSelector().setSelectedItem(getGlobalInfo().getDeviceServiceProjectFolder());
			switch (getGlobalInfo().getDeviceServiceExecutionMode()) {
			case SETUP:
				this.getJRadioButtonExecuteAsService().setSelected(true);
				this.getJRadioButtonExecuteAsDeviceAgent().setSelected(false);
				break;
			case AGENT:
				this.getJRadioButtonExecuteAsService().setSelected(false);
				this.getJRadioButtonExecuteAsDeviceAgent().setSelected(true);
				break;
			}
			this.getJComboBoxSetupSelector().setSelectedItem(getGlobalInfo().getDeviceServiceSetupSelected());
			this.getJTextFieldAgentClass().setText(getGlobalInfo().getDeviceServiceAgentClassName());
			this.getJTextFieldAgentName().setText(getGlobalInfo().getDeviceServiceAgentName());
			switch (getGlobalInfo().getDeviceServiceAgentVisualisation()) {
			case NONE:
				this.getJRadioButtonVisNon().setSelected(true);
				this.getJRadioButtonVisTrayIcon().setSelected(false);
				break;
			case TRAY_ICON:
				this.getJRadioButtonVisNon().setSelected(false);
				this.getJRadioButtonVisTrayIcon().setSelected(true);
				break;
			}
			
		} else {
			// --- Just set some default values ---------------------
			this.getJRadioButtonExecuteAsService().setSelected(true);
			this.getJRadioButtonExecuteAsDeviceAgent().setSelected(false);
			this.getJRadioButtonVisNon().setSelected(false);
			this.getJRadioButtonVisTrayIcon().setSelected(true);
			
		}
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractJPanelForOptions#setFromData2Global()
	 */
	@Override
	public void setFormData2Global() {

		this.getGlobalInfo().setDeviceServiceProjectFolder((String) this.getJComboBoxProjectSelector().getSelectedItem());
		if (this.getJRadioButtonExecuteAsService().isSelected()) {
			this.getGlobalInfo().setDeviceServiceExecutionMode(DeviceSystemExecutionMode.SETUP);	
		} else if (this.getJRadioButtonExecuteAsDeviceAgent().isSelected()) {
			this.getGlobalInfo().setDeviceServiceExecutionMode(DeviceSystemExecutionMode.AGENT);
		}
		this.getGlobalInfo().setDeviceServiceSetupSelected((String)this.getJComboBoxSetupSelector().getSelectedItem());
		this.getGlobalInfo().setDeviceServiceAgentClassName(this.getJTextFieldAgentClass().getText());
		this.getGlobalInfo().setDeviceServiceAgentName(this.getJTextFieldAgentName().getText());
		if (this.getJRadioButtonVisNon().isSelected()) {
			this.getGlobalInfo().setDeviceServiceAgentVisualisation(EmbeddedSystemAgentVisualisation.NONE);			
		} else if (this.getJRadioButtonVisTrayIcon().isSelected()) {
			this.getGlobalInfo().setDeviceServiceAgentVisualisation(EmbeddedSystemAgentVisualisation.TRAY_ICON);	
		}
	}

	/* (non-Javadoc)
	 * @see de.enflexit.common.swing.options.AbstractJPanelForOptions#errorFound()
	 */
	@Override
	public boolean errorFound() {

		String msgHead = null;
		String msgText = null;
		boolean err = false;

		if (this.getSelectedExecutionMode()==ExecutionMode.DEVICE_SYSTEM) {
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
				// --- Agent Name ? -------------------------------------
				if (this.getJTextFieldAgentName().getText()==null || this.getJTextFieldAgentName().getText().equals("")) {
					msgHead = Language.translate("Fehlender Agent Name!");
					msgText = Language.translate("Bitte geben Sie Agenten Name") + "!";	
					JOptionPane.showMessageDialog(this.optionDialog.getContentPane(), msgText, msgHead, JOptionPane.ERROR_MESSAGE);
					return true;
				}
				
			}
			
		} 
		return err;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		String actCMD = ae.getActionCommand();
		if (actCMD.equalsIgnoreCase("esaProjectSelected")) {
			this.esaLoadSelectedProject();
		} else if (actCMD.equalsIgnoreCase("esaExecuteAsServerService")) {
			this.refreshViewDeviceSystem();
		} else if (actCMD.equalsIgnoreCase("esaExecuteAsDeviceAgent")) {
			this.refreshViewDeviceSystem();
			
		} else if (actCMD.equalsIgnoreCase("esaSetupSelected")) {
			// --- nothing to do here ---------------------
		} else if (actCMD.equalsIgnoreCase("esaSelectAgent")) {
			this.esaSelectAgent();
		} else if (actCMD.equalsIgnoreCase("visNon")) {
			// --- nothing to do here ---------------------
		} else if (actCMD.equalsIgnoreCase("visTrayIcon")) {
			// --- nothing to do here, yet ----------------
		}
	}

}
