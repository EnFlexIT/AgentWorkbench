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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.config.GlobalInfo.DeviceSystemExecutionMode;
import agentgui.core.config.GlobalInfo.EmbeddedSystemAgentVisualisation;
import agentgui.core.config.GlobalInfo.ExecutionMode;
import agentgui.core.gui.ClassSelector;
import agentgui.core.gui.components.JListClassSearcher;
import agentgui.core.project.Project;
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
	private JLabel jLabelProjectHeader;
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
	private ClassSelector esaClassSelector;
	
	private JLabel jLabelVisConfig;
	private JRadioButton jRadioButtonVisNon;
	private JRadioButton jRadioButtonVisTrayIcon;
	
	
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
		
		this.setSize(new Dimension(550, 318));
		this.setLayout(new GridBagLayout());
		
		this.setPreferredSize(new Dimension(550, 297));
		this.add(jLabelEmbeddedHeader, gridBagConstraints32);
		this.add(jLabelProject, gridBagConstraints33);
		this.add(getJComboBoxProjectSelector(), gridBagConstraints34);
		this.add(jLabelProjectHeader, gridBagConstraints35);
		this.add(jLabelAgentHeader, gridBagConstraints36);
		this.add(jLabelAgent, gridBagConstraints37);
		this.add(getJTextFieldAgentClass(), gridBagConstraints38);
		this.add(getJButtonEsaSelectAgent(), gridBagConstraints39);
		this.add(jLabelVisConfig, gridBagConstraints29);
		this.add(jLabelExecuteAs, gridBagConstraints43);
		this.add(getJPanelExecution(), gridBagConstraints45);
		this.add(getJComboBoxSetupSelector(), gridBagConstraints46);
		this.add(jLabelSetup, gridBagConstraints47);
		this.add(jLabelSetupHeader, gridBagConstraints48);
		this.add(getJRadioButtonVisNon(), gridBagConstraints40);
		this.add(getJRadioButtonVisTrayIcon(), gridBagConstraints49);
		
		ButtonGroup visAsGroup = new ButtonGroup();
		visAsGroup.add(getJRadioButtonVisNon());
		visAsGroup.add(getJRadioButtonVisTrayIcon());
	}
	
	
	/**
	 * This method initializes jComboBoxProjectSelector	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox<String> getJComboBoxProjectSelector() {
		if (jComboBoxProjectSelector == null) {
			
			DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<String>();
			comboBoxModel.addElement("");
			String[] projectFolders = globalInfo.getProjectSubDirectories();
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
		this.refreshViewDeviceSystem();
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
			this.esaClassSelector = new ClassSelector(this.optionDialog, jListClassSearcher, currAgentClass, null, Language.translate("Bitte wählen Sie den Agenten aus, der gestartet werden soll"), false);
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
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.gui.options.AbstractJPanelForOptions#refreshView()
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
			this.esaClassSelector = null;
			break;

		case SERVER:
			// --- Reset DEVICE_SYSTEM variables ----------
			this.getJComboBoxProjectSelector().setSelectedItem(null);
			this.getJComboBoxSetupSelector().setSelectedItem(null);
			this.getJTextFieldAgentClass().setText(null);
			this.esaClassSelector = null;
			break;
			
		case DEVICE_SYSTEM:
			this.refreshViewDeviceSystem();
			break;

		}
	}
	/* (non-Javadoc)
	 * @see agentgui.core.gui.options.AbstractJPanelForOptions#setGlobalData2Form()
	 */
	@Override
	public void setGlobalData2Form() {
		
		if (globalInfo.getExecutionMode()==ExecutionMode.DEVICE_SYSTEM) {
			// --- In case of a Service / Embedded System Agent ---------		
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
	 * @see agentgui.core.gui.options.AbstractJPanelForOptions#setFromData2Global()
	 */
	@Override
	public void setFormData2Global() {

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

	/* (non-Javadoc)
	 * @see agentgui.core.gui.options.AbstractJPanelForOptions#errorFound()
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
