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
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import agentgui.core.application.Language;
import agentgui.core.config.DeviceAgentDescription;
import agentgui.core.config.GlobalInfo;
import agentgui.core.project.Project;
import de.enflexit.common.classSelection.ClassSelectionDialog;
import de.enflexit.common.classSelection.JListClassSearcher;
import jade.core.Agent;

public class JPanelEmbeddedSystemAgentTable extends JPanel implements ActionListener {

	private static final long serialVersionUID = 5875253790841004106L;

	private OptionDialog optionDialog;
	private JPanelEmbeddedSystemAgent esaPanel;
	
	private JLabel jLabelHeader;
	private JButton jButtonAddAgent;
	private JButton jButtonSelectAgentClass;
	
	private ClassSelectionDialog esaClassSelector;
	private Project esaProject;
	private JButton jButtonRemoveAgent;

	private JScrollPane jSscrollPaneAgents;
	private JTable jTableAgents;
	private DefaultTableModel tableModel;

	
	
	/**
	 * Instantiates a new j panel embedded system agent table.
	 */
	public JPanelEmbeddedSystemAgentTable() {
		this.initialize();
	}
	/**
	 * Instantiates a new JPanel for the table of embedded system agents.
	 *
	 * @param optionDialog the option dialog
	 * @param esaPanel the parent JPanelEmbeddedSystemAgent
	 */
	public JPanelEmbeddedSystemAgentTable(OptionDialog optionDialog, JPanelEmbeddedSystemAgent esaPanel) {
		this.optionDialog = optionDialog;
		this.esaPanel = esaPanel;
		this.initialize();
	}
	private void initialize() {

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{26, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
		gbc_jLabelHeader.fill = GridBagConstraints.VERTICAL;
		gbc_jLabelHeader.anchor = GridBagConstraints.WEST;
		gbc_jLabelHeader.insets = new Insets(0, 0, 5, 5);
		gbc_jLabelHeader.gridx = 0;
		gbc_jLabelHeader.gridy = 0;
		add(getJLabelHeader(), gbc_jLabelHeader);
		
		GridBagConstraints gbc_jButtonAddAgent = new GridBagConstraints();
		gbc_jButtonAddAgent.fill = GridBagConstraints.BOTH;
		gbc_jButtonAddAgent.insets = new Insets(0, 0, 5, 5);
		gbc_jButtonAddAgent.gridx = 1;
		gbc_jButtonAddAgent.gridy = 0;
		add(getJButtonAddAgent(), gbc_jButtonAddAgent);
		
		GridBagConstraints gbc_jButtonSelectAgentClass = new GridBagConstraints();
		gbc_jButtonSelectAgentClass.fill = GridBagConstraints.BOTH;
		gbc_jButtonSelectAgentClass.insets = new Insets(0, 0, 5, 5);
		gbc_jButtonSelectAgentClass.gridx = 2;
		gbc_jButtonSelectAgentClass.gridy = 0;
		add(getJButtonSelectAgentClass(), gbc_jButtonSelectAgentClass);
		
		GridBagConstraints gbc_jButtonRemoveAgent = new GridBagConstraints();
		gbc_jButtonRemoveAgent.fill = GridBagConstraints.BOTH;
		gbc_jButtonRemoveAgent.insets = new Insets(0, 0, 5, 0);
		gbc_jButtonRemoveAgent.gridx = 3;
		gbc_jButtonRemoveAgent.gridy = 0;
		add(getJButtonRemoveAgent(), gbc_jButtonRemoveAgent);
		
		GridBagConstraints gbc_jSscrollPaneAgents = new GridBagConstraints();
		gbc_jSscrollPaneAgents.gridwidth = 4;
		gbc_jSscrollPaneAgents.fill = GridBagConstraints.BOTH;
		gbc_jSscrollPaneAgents.gridx = 0;
		gbc_jSscrollPaneAgents.gridy = 1;
		add(getJSscrollPaneAgents(), gbc_jSscrollPaneAgents);
	}
	
	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel("Embedded System Agents");
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeader;
	}
	private JButton getJButtonAddAgent() {
		if (jButtonAddAgent == null) {
			jButtonAddAgent = new JButton("");
			jButtonAddAgent.setIcon(GlobalInfo.getInternalImageIcon("ListPlus.png"));
			jButtonAddAgent.setPreferredSize(new Dimension(26, 26));
			jButtonAddAgent.setSize(new Dimension(26, 26));
			jButtonAddAgent.addActionListener(this);
		}
		return jButtonAddAgent;
	}
	private JButton getJButtonSelectAgentClass() {
		if (jButtonSelectAgentClass == null) {
			jButtonSelectAgentClass = new JButton("");
			jButtonSelectAgentClass.setToolTipText(Language.translate("Agenten auswählen"));
			jButtonSelectAgentClass.setPreferredSize(new Dimension(45, 26));
			jButtonSelectAgentClass.setIcon(GlobalInfo.getInternalImageIcon("Search.png"));
			jButtonSelectAgentClass.setActionCommand("esaSelectAgent");
			jButtonSelectAgentClass.addActionListener(this);
		}
		return jButtonSelectAgentClass;
	}
	private JButton getJButtonRemoveAgent() {
		if (jButtonRemoveAgent == null) {
			jButtonRemoveAgent = new JButton("");
			jButtonRemoveAgent.setIcon(GlobalInfo.getInternalImageIcon("ListMinus.png"));
			jButtonRemoveAgent.setPreferredSize(new Dimension(26, 26));
			jButtonRemoveAgent.setSize(new Dimension(26, 26));
			jButtonRemoveAgent.addActionListener(this);
		}
		return jButtonRemoveAgent;
	}
	private JScrollPane getJSscrollPaneAgents() {
		if (jSscrollPaneAgents == null) {
			jSscrollPaneAgents = new JScrollPane();
			jSscrollPaneAgents.setViewportView(this.getJTableAgents());
		}
		return jSscrollPaneAgents;
	}
	private JTable getJTableAgents() {
		if (jTableAgents == null) {
			jTableAgents = new JTable(this.getTableModel());
			jTableAgents.setFillsViewportHeight(true);
			jTableAgents.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTableAgents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
			jTableAgents.getTableHeader().setReorderingAllowed(false);
			jTableAgents.getColumnModel().getColumn(0).setMinWidth(160);
			jTableAgents.getColumnModel().getColumn(0).setMaxWidth(240);
			
		}
		return jTableAgents;
	}
	private DefaultTableModel getTableModel() {
		if (tableModel==null) {
			Vector<String> header = new Vector<String>();
			header.add("Agent Name");
			header.add("Agent Class");
			tableModel = new DefaultTableModel(null, header) {
				private static final long serialVersionUID = 948447712730592649L;
				@Override
				public boolean isCellEditable(int row, int column) {
					if (column==1) {
						return false;
					}
					return true;
				}
			};
		}
		return tableModel;
	}
	/**
	 * Returns the selected table model row index.
	 * @return the selected table model row index
	 */
	private int getSelectedTableModelRowIndex() {
		int selectedModelIndex = -1;
		int selectedTableIndex = this.getJTableAgents().getSelectedRow();
		if (selectedTableIndex!=-1) {
			selectedModelIndex = this.getJTableAgents().convertRowIndexToModel(selectedTableIndex);
		}
		return selectedModelIndex;
	}
	/**
	 * Gets the selected agent class.
	 * @return the selected agent class
	 */
	private String getSelectedAgentClass() {
		String selectedAgentClass = null;
		int selectedModelRowIndex = this.getSelectedTableModelRowIndex();
		if (selectedModelRowIndex!=-1) {
			selectedAgentClass = (String) this.getTableModel().getValueAt(selectedModelRowIndex, 1);
		}
		return selectedAgentClass;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		this.getJLabelHeader().setEnabled(enabled);
		this.getJButtonAddAgent().setEnabled(enabled);
		this.getJButtonSelectAgentClass().setEnabled(enabled);
		this.getJButtonRemoveAgent().setEnabled(enabled);
		this.getJTableAgents().setEnabled(enabled);
	}

	
	/**
	 * Sets the agent names and classes.
	 *
	 * @param deviceServiceAgentNames the new agent names
	 * @param deviceServiceAgentClassNames the device service agent class names
	 */
	public void setDeviceServiceAgents(Vector<DeviceAgentDescription> deviceServiceAgents) {
		for (int i = 0; i < deviceServiceAgents.size(); i++) {
			DeviceAgentDescription dad = deviceServiceAgents.get(i);
			this.getTableModel().addRow(new String[] {dad.getAgentName(), dad.getAgentClass()});
		} 
	}
	/**
	 * Returns the device service agents.
	 * @return the device service agents
	 */
	public Vector<DeviceAgentDescription> getDeviceServiceAgents() {
		Vector<DeviceAgentDescription> dadVector = new Vector<>();
		for (int i = 0; i < this.getTableModel().getRowCount(); i++) {
			String agentName = (String) this.getTableModel().getValueAt(i, 0);
			String agentClass = (String) this.getTableModel().getValueAt(i, 1);
			dadVector.add(new DeviceAgentDescription(agentName, agentClass));
		}
		return dadVector;
	}
	
	/**
	 * Removes all table model entries.
	 */
	public void removeTableEntries() {
		if (this.getJTableAgents().getCellEditor()!=null) {
			this.getJTableAgents().getCellEditor().cancelCellEditing();
		}
		while (this.getTableModel().getRowCount()>0) {
			this.getTableModel().removeRow(0);
		}
		if (this.esaClassSelector!=null) {
			this.esaClassSelector.dispose();
			this.esaClassSelector = null;	
		}
	}
	/**
	 * Sets the selected project.
	 * @param selectedProject the new selected project
	 */
	public void setSelectedProject(Project selectedProject) {
		this.getClassSelector4ProjectAgents(selectedProject);
	}
	/**
	 * Gets the class selector for project agents.
	 * @param project the project
	 * @return the class selector for project agents
	 */
	private ClassSelectionDialog getClassSelector4ProjectAgents(Project project) {

		String currAgentClass = this.getSelectedAgentClass();
		if (project==null) {
			this.esaClassSelector = null;
			this.esaProject = project;
		} else if (this.esaClassSelector==null || project!=this.esaProject) {
			JListClassSearcher jListClassSearcher = new JListClassSearcher(Agent.class, project.getBundleNames());
			this.esaClassSelector = new ClassSelectionDialog(this.optionDialog, jListClassSearcher, currAgentClass, null, Language.translate("Bitte wählen Sie den Agenten aus, der gestartet werden soll"), false);
			this.esaProject = project;
		}
		if (this.esaClassSelector!=null && currAgentClass!=null) {
			this.esaClassSelector.setClass2Search4CurrentValue(currAgentClass);
		}
		return this.esaClassSelector;
	}
	
	/**
	 * Displays a selector for the embedded system agent.
	 */
	private String getAgentClassSelection(){
		
		if (this.esaProject==null) {
			String msgHead = Language.translate("Fehlendes Projekt!");
			String msgText = Language.translate("Bitte wählen Sie ein Projekt aus") + "!";	
			JOptionPane.showMessageDialog(this.optionDialog.getContentPane(), msgText, msgHead, JOptionPane.ERROR_MESSAGE);
			this.esaPanel.getJComboBoxProjectSelector().showPopup();
			return null;
		}
		// --- Open search dialog for agents --------------
		ClassSelectionDialog cs = this.getClassSelector4ProjectAgents(this.esaProject);
		cs.setVisible(true);
		// --- act in the dialog ... ----------------------
		if (cs.isCanceled()==false) {
			return cs.getClassSelected();
		}
		return null;
	}
	/**
	 * Check, is an error could be found.
	 * @return false, if no error was found
	 */
	public boolean errorFound() {
		
		if (this.getJTableAgents().getCellEditor()!=null) {
			this.getJTableAgents().getCellEditor().stopCellEditing();
		}
		
		if (this.getTableModel().getRowCount()==0) {
			// --- No agent was defined ---------
			String msgHead = Language.translate("Fehlender Agent!");
			String msgText = Language.translate("Bitte geben Sie den Agenten an, der gestartet werden soll") + "!";	
			JOptionPane.showMessageDialog(this.optionDialog.getContentPane(), msgText, msgHead, JOptionPane.ERROR_MESSAGE);
			return true;
		}
		
		for (int i = 0; i < this.getTableModel().getRowCount(); i++) {
			
			String agentName = (String) this.getTableModel().getValueAt(i, 0);
			String agentClass = (String) this.getTableModel().getValueAt(i, 1);
			int tableRow = this.getJTableAgents().convertRowIndexToView(i);
			
			// --- Agent Name ? -----------------
			if (agentName==null || agentName.equals("")) {
				String msgHead = Language.translate("Fehlender Agent Name!");
				String msgText = Language.translate("Bitte geben Sie einen Agenten-Namen an") + "!";	
				JOptionPane.showMessageDialog(this.optionDialog.getContentPane(), msgText, msgHead, JOptionPane.ERROR_MESSAGE);
				this.getJTableAgents().setRowSelectionInterval(tableRow, tableRow);
				this.getJTableAgents().editCellAt(tableRow, 0);
				this.getJTableAgents().setSurrendersFocusOnKeystroke(true);
				this.getJTableAgents().getEditorComponent().requestFocus();
				return true;
			}
			// --- Agent Class ? ----------------
			if (agentClass==null || agentClass.equals("")) {
				String msgHead = Language.translate("Fehlender Agent!");
				String msgText = Language.translate("Bitte wählen Sie einen Agenten") + "!";	
				JOptionPane.showMessageDialog(this.optionDialog.getContentPane(), msgText, msgHead, JOptionPane.ERROR_MESSAGE);
				this.getJTableAgents().setRowSelectionInterval(tableRow, tableRow);
				this.getJButtonSelectAgentClass().doClick();
				return true;
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {

		if (ae.getSource()==this.getJButtonAddAgent()) {
			// --- Add a further agent row ----------------
			String agentClass = this.getAgentClassSelection();
			if (agentClass!=null && agentClass.isEmpty()==false) {
				int rowCount = this.getTableModel().getRowCount();
				this.getTableModel().addRow(new String[] {null, agentClass});
				int selectionRow = this.getJTableAgents().convertRowIndexToView(rowCount);
				this.getJTableAgents().setRowSelectionInterval(selectionRow, selectionRow);
				this.getJTableAgents().editCellAt(selectionRow, 0);
				this.getJTableAgents().setSurrendersFocusOnKeystroke(true);
				this.getJTableAgents().getEditorComponent().requestFocus();
			}
			
		} else if (ae.getSource()==this.getJButtonRemoveAgent()) {
			// --- Remove an agent row --------------------
			if (this.getTableModel().getRowCount()==0) return;
			int selectedModelRowIndex = this.getSelectedTableModelRowIndex();
			if (selectedModelRowIndex==-1) {
				String msgHead = Language.translate("Fehlende Auswahl!");
				String msgText = Language.translate("Bitte wählen Sie die Tabellenzeile, die gelöscht werden soll") + "!";	
				JOptionPane.showMessageDialog(this.optionDialog.getContentPane(), msgText, msgHead, JOptionPane.WARNING_MESSAGE);
			} else {
				this.getTableModel().removeRow(selectedModelRowIndex);
			}
			
		} else if (ae.getSource()==this.getJButtonSelectAgentClass()) {
			// --- Select an agent class ------------------
			if (this.getTableModel().getRowCount()==0) return;
			int selectedModelRowIndex = this.getSelectedTableModelRowIndex();
			if (selectedModelRowIndex==-1) {
				String msgHead = Language.translate("Fehlende Auswahl!");
				String msgText = Language.translate("Bitte wählen Sie die Zeile, in der ein Agent definiert werden soll") + "!";	
				JOptionPane.showMessageDialog(this.optionDialog.getContentPane(), msgText, msgHead, JOptionPane.WARNING_MESSAGE);

			} else {
				String agentClass = this.getAgentClassSelection();
				if (agentClass!=null && agentClass.isEmpty()==false) {
					this.getTableModel().setValueAt(agentClass, selectedModelRowIndex, 1);
				}	
			}
			
		}
	}

	
}
