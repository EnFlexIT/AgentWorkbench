/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://sourceforge.net/projects/agentgui/
 * http://www.dawis.wiwi.uni-due.de/ 
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
package agentgui.graphEnvironment.controller;

import jade.content.Concept;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.Dialog;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import agentgui.core.application.Application;
import agentgui.core.application.Language;
import agentgui.core.application.Project;
import agentgui.core.gui.ClassSelector;
import agentgui.core.gui.ClassSelectorTableCellEditor;
import agentgui.core.gui.components.ClassNameListCellRenderer;
import agentgui.core.gui.components.ClassNameTableCellRenderer;
import agentgui.core.gui.components.ImageSelectorTableCellEditor;
import agentgui.core.gui.imaging.MissingIcon;
import agentgui.graphEnvironment.networkModel.ComponentTypeSettings;
import agentgui.graphEnvironment.prototypes.GraphElementPrototype;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * GUI dialog for configuring network component types 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen 
 * @author <br>Satyadeep Karnati - CSE - Indian Institute of Technology, Guwahati 
 * 
 */
public class ClassSelectionDialog extends JDialog implements ActionListener{
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The content pane
	 */
	private JPanel jContentPane = null;
	/**
	 * The confirm button
	 */
	private JButton jButtonConfirm = null;
	/**
	 * The cancel button
	 */
	private JButton jButtonCancel = null;
	/**
	 * The JScrollPane containing the component type table
	 */
	private JScrollPane jScrollPaneClassTable = null;
	/**
	 * The component type table
	 */
	private JTable jTableComponentTypes = null;
	/**
	 * The add row button
	 */
	private JButton jButtonAddRow = null;
	/**
	 * The remove row button
	 */
	private JButton jButtonRemoveRow = null;
	/**
	 * The ClassSelector instance used for assigning a graph node class
	 */
	private ClassSelector nodeClassSelector = null;
	/**
	 * JComboBox used as cell editor for the agent classes column
	 */
	private JComboBox jComboBoxAgentClasses = null;
	
	/**
	 * Cell editor for the prototype classes column
	 */
	private ClassSelectorTableCellEditor prototypeClassesCellEditor = null;
	/**
	 * All available agent classes, accessible by simple class name
	 */
	private HashMap<String, Class<?>> availableAgentClasses = null;
	
	/**
	 * The GraphEnvironmentControllerGUI that started this dialog
	 */
	private GraphEnvironmentControllerGUI parent = null;
	/**
	 * The current AgentGUI project
	 */
	private Project project = null;
	/**
	 * The label for the node class text field
	 */
	private JLabel jLabelNodeClass = null;
	/**
	 * The JTextField specifying the graph node class
	 */
	private JTextField jTextFieldNodeClass = null;
	/**
	 * Button invoking the nodeClassSelector
	 */
	private JButton jButtonSelectNodeClass = null;
	
	/**
	 * Application image folder path
	 */
	private final String pathImage = Application.RunInfo.PathImageIntern(); 
	/**
	 * This is the default constructor
	 * @param parent The parent GUI
	 */
	public ClassSelectionDialog(GraphEnvironmentControllerGUI parent) {
		super(Application.MainWindow, Dialog.ModalityType.APPLICATION_MODAL);
		this.parent = parent;
		project = parent.getController().getProject();
		initialize();
		//System.out.println(project.getProjectFolderFullPath());
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(400, 300);
		this.setContentPane(getJContentPane());
		this.setTitle(Language.translate("Komponententyp-Definition"));
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 3;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.gridy = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.gridx = 2;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.gridwidth = 2;
			gridBagConstraints12.gridy = 1;
			jLabelNodeClass = new JLabel();
			jLabelNodeClass.setText(Language.translate("Verbindungspunkte"));
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints11.gridy = 3;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints1.gridy = 3;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.fill = GridBagConstraints.BOTH;
			gridBagConstraints10.gridy = 2;
			gridBagConstraints10.weightx = 1.0;
			gridBagConstraints10.weighty = 1.0;
			gridBagConstraints10.gridwidth = 4;
			gridBagConstraints10.gridx = 0;
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 3;
			gridBagConstraints9.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints9.gridy = 3;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.gridx = 2;
			gridBagConstraints8.weightx = 1.0D;
			gridBagConstraints8.anchor = GridBagConstraints.EAST;
			gridBagConstraints8.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints8.gridy = 3;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridy = 0;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getJButtonConfirm(), gridBagConstraints8);
			jContentPane.add(getJButtonCancel(), gridBagConstraints9);
			jContentPane.add(getJScrollPaneClassTable(), gridBagConstraints10);
			jContentPane.add(getJButtonAddRow(), gridBagConstraints1);
			jContentPane.add(getJButtonRemoveRow(), gridBagConstraints11);
			jContentPane.add(jLabelNodeClass, gridBagConstraints12);
			jContentPane.add(getJTextFieldNodeClass(), gridBagConstraints3);
			jContentPane.add(getJButtonSelectNodeClass(), gridBagConstraints4);
		}
		return jContentPane;
	}
	
	/**
	 * This method initializes the nodeClassSelector
	 * @return The nodeClassSelector
	 */
	private ClassSelector getNodeClassSelector(){
		if(nodeClassSelector == null){
			Class<?> superClass = Concept.class;
			String currValue = null;
			String defaultValue = null;
			String description = Language.translate("Ontologie-Klasse für Übergabepunkte");
			nodeClassSelector = new ClassSelector(Application.MainWindow, superClass, currValue, defaultValue, description);
		}
		return nodeClassSelector;
	}

	/**
	 * This method initializes jButtonConfirm	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonConfirm() {
		if (jButtonConfirm == null) {
			jButtonConfirm = new JButton();
			jButtonConfirm.setText(Language.translate("Übernehmen"));
			jButtonConfirm.addActionListener(this);
		}
		return jButtonConfirm;
	}

	/**
	 * This method initializes jButtonCancel	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setText(Language.translate("Abbrechen"));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}

	/**
	 * This method initializes jScrollPaneClassTable	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPaneClassTable() {
		if (jScrollPaneClassTable == null) {
			jScrollPaneClassTable = new JScrollPane();
			jScrollPaneClassTable.setViewportView(getJTableClasses());
		}
		return jScrollPaneClassTable;
	}

	/**
	 * This method initializes jTableClasses	
	 * 	
	 * @return javax.swing.JTable	
	 */
	private JTable getJTableClasses() {
		if (jTableComponentTypes == null) {
			jTableComponentTypes = new JTable();
			jTableComponentTypes.setFillsViewportHeight(true);
			jTableComponentTypes.setShowGrid(true);
			jTableComponentTypes.setRowHeight(20);
			jTableComponentTypes.setModel(getClassesTableModel());
			
			TableColumn agentClassColumn = jTableComponentTypes.getColumnModel().getColumn(1);
			agentClassColumn.setCellEditor(new DefaultCellEditor(getJComboBoxAgentClasses()));
			agentClassColumn.setCellRenderer(new ClassNameTableCellRenderer());
			
			TableColumn prototypeClassColumn = jTableComponentTypes.getColumnModel().getColumn(2);
			prototypeClassColumn.setCellEditor(getPrototypeClassesCellEditor());
			prototypeClassColumn.setCellRenderer(new ClassNameTableCellRenderer());
			
			TableColumn imageIconColumn = jTableComponentTypes.getColumnModel().getColumn(3);
			imageIconColumn.setCellEditor(new ImageSelectorTableCellEditor(project));			
		}
		return jTableComponentTypes;
	}
	
	/**
	 * This method initiates the jTableClasses' TableModel
	 * @return The TableModel
	 */
	private TableModel getClassesTableModel(){
		
		// The ComboBoxModels must be initiated before adding rows 
		getJComboBoxAgentClasses();
		
		// Headlines
		Vector<String> titles = new Vector<String>();
		titles.add(Language.translate("Typ-Bezeichner"));
		titles.add(Language.translate("Agentenklasse"));
		titles.add(Language.translate("Graph-Prototyp"));
		titles.add(Language.translate("Image",Language.EN));
		
		Vector<Vector<Object>> dataRows = new Vector<Vector<Object>>();
		
		// Set table entries for defined assignments, if any
		HashMap<String, ComponentTypeSettings> etsHash = parent.getController().getComponentTypeSettings();
		if(etsHash != null){
			Iterator<String> etsIter = etsHash.keySet().iterator();
			while(etsIter.hasNext()){
				String etName = etsIter.next();
				if(!etName.equals("node")){	// The node class is defined in the JTextField, not in the table
					Vector<Object> newRow = new Vector<Object>();
					newRow.add(etName);
					newRow.add(etsHash.get(etName).getAgentClass());
					newRow.add(etsHash.get(etName).getGraphPrototype());

					String imagePath = etsHash.get(etName).getEdgeImage();
					//The description is used to store the path along with the ImageIcon
					newRow.add(createImageIcon(imagePath, imagePath));
					
					dataRows.add(newRow);
				}
			}
		}
		
		return new DefaultTableModel(dataRows, titles){
			private static final long serialVersionUID = 1L;

			public Class<?> getColumnClass(int c) {
		            return getValueAt(0, c).getClass();
		        }
		};
	}
	
	/**
	 * This method adds a new row to the jTableClasses' TableModel
	 */
	private void addRow(){
		((DefaultTableModel)getJTableClasses().getModel()).addRow(new Vector<String>());
		getJTableClasses().changeSelection(getJTableClasses().getRowCount()-1, 0, false, false);
		getJTableClasses().editCellAt(getJTableClasses().getRowCount()-1, 0);
	}
	
	/**
	 * This method removes a row from the jTableClasses' TableModel
	 * @param rowNum
	 */
	private void removeRow(int rowNum){
		((DefaultTableModel)getJTableClasses().getModel()).removeRow(rowNum);
	}

	/**
	 * This method initializes jButtonAddRow	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonAddRow() {
		if (jButtonAddRow == null) {
			jButtonAddRow = new JButton();
			jButtonAddRow.setIcon(new ImageIcon(getClass().getResource(pathImage + "ListPlus.png")));
			jButtonAddRow.addActionListener(this);
		}
		return jButtonAddRow;
	}
	
	/**
	 * This method initializes cellEditorAgentClass
	 * 
	 * @return @return javax.swing.JComboBox
	 */
	private JComboBox getJComboBoxAgentClasses(){
		if(jComboBoxAgentClasses == null){
			jComboBoxAgentClasses = new JComboBox(getAgentComboBoxModel());
			jComboBoxAgentClasses.setRenderer(new ClassNameListCellRenderer());
		}
		return jComboBoxAgentClasses;
	}
	
	private ClassSelectorTableCellEditor getPrototypeClassesCellEditor(){
		if(prototypeClassesCellEditor == null){
			prototypeClassesCellEditor = new ClassSelectorTableCellEditor(Application.MainWindow, GraphElementPrototype.class, "", "", Language.translate("Graph-Prototypen"));
		}
		return prototypeClassesCellEditor;
	}
	
	/**
	 * This method builds a vector for initiating the cellEditorAgentClass' ComboBox model, and initiates the agentClasses HashMap.	
	 * @return Vector containing the simple names of the projects agent classes.
	 */
	private Vector<String> getAgentComboBoxModel(){
		Vector<String> agentClassNames = new Vector<String>();
		agentClassNames.add(Language.translate("Nicht definiert"));
		
		availableAgentClasses = new HashMap<String, Class<?>>();
		availableAgentClasses.put(Language.translate("Nicht definiert"), null);
		
		// Get all classes extending jade.core.Agent in the current project 
		Iterator<Class<?>> agentClassesIterator= Application.ClassDetector.csAgents.getClassesFound(true).iterator();
		// Build a HashMap containing the simple names as keys and the full class names as values
		while(agentClassesIterator.hasNext()){
			Class<?> agentClass = agentClassesIterator.next();
			availableAgentClasses.put(agentClass.getSimpleName(), agentClass);
			agentClassNames.add(agentClass.getName());
		}
		return agentClassNames;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		// Add a new row to the component types table
		if(event.getSource().equals(getJButtonAddRow())){
			addRow();
		// Remove a row from the component types table
		}else if(event.getSource().equals(getJButtonRemoveRow())){
			if(getJTableClasses().getSelectedRow() > -1){
				removeRow(getJTableClasses().getSelectedRow());
			}
		// Confirmed, apply changes
		}else if(event.getSource().equals(getJButtonConfirm())){
			
			JTable jtc = getJTableClasses();
			
			int rowNum = jtc.getRowCount();
			HashMap<String, ComponentTypeSettings> etsVector = new HashMap<String, ComponentTypeSettings>();
			// Get the component type definitions from the table
			for(int row=0; row<rowNum; row++){
				ImageIcon imageIcon = (ImageIcon)jtc.getValueAt(row,3);
				ComponentTypeSettings ets = new ComponentTypeSettings(
						(String)jtc.getValueAt(row, 1), 
						(String)jtc.getValueAt(row, 2),
						imageIcon.getDescription());
				// Use name as key
				etsVector.put((String) jtc.getValueAt(row, 0), ets);
			}
			// Add the graph node class definition
			etsVector.put("node", new ComponentTypeSettings(getJTextFieldNodeClass().getText(), null, null));
			// Set the GraphEnvironmentController's componentTypeSettings
			parent.getController().setComponentTypeSettings(etsVector);
			
			this.dispose();
		// Canceled, discard changes
		}else if(event.getSource().equals(getJButtonCancel())){
			this.dispose();
		}
		
	}

	/**
	 * This method initializes jButtonRemoveRow	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonRemoveRow() {
		if (jButtonRemoveRow == null) {
			jButtonRemoveRow = new JButton();
			jButtonRemoveRow.setIcon(new ImageIcon(getClass().getResource(pathImage + "ListMinus.png")));
			jButtonRemoveRow.addActionListener(this);
		}
		return jButtonRemoveRow;
	}

	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldNodeClass() {
		if (jTextFieldNodeClass == null) {
			jTextFieldNodeClass = new JTextField();
			
			ComponentTypeSettings nodeSettings = parent.getController().getComponentTypeSettings().get("node");
			
			if(nodeSettings != null){
				jTextFieldNodeClass.setText(nodeSettings.getAgentClass());
			}
		}
		return jTextFieldNodeClass;
	}

	/**
	 * This method initializes jButtonNodeClassSelector	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonSelectNodeClass() {
		if (jButtonSelectNodeClass == null) {
			jButtonSelectNodeClass = new JButton();
			jButtonSelectNodeClass.setText("...");
			jButtonSelectNodeClass.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					getNodeClassSelector().setVisible(true);
					if(! getNodeClassSelector().isCanceled()){
						getJTextFieldNodeClass().setText(getNodeClassSelector().getClassSelected());
					}
				}
			});
		}
		return jButtonSelectNodeClass;
	}
	
	/** 
	 * Returns an ImageIcon, or a default MissingIcon(a red X) if image not found.
	 * @param path
	 * @param description
	 * @return ImageIcon 
	 */
	protected ImageIcon createImageIcon(String path,
	                                           String description) {
		if(path!=null ){			
		    java.net.URL imgURL = getClass().getResource(path);
		    if (imgURL != null) {
		        return new ImageIcon(imgURL, description);
		    } else {
		        System.err.println("Couldn't find file: " + path);
		        return (new MissingIcon(description));
		    }
		}
		else{
		    return (new MissingIcon(description));		    
		}
			
	}

}
