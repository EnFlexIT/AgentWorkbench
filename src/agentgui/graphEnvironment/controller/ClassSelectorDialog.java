package agentgui.graphEnvironment.controller;

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

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.ImageIcon;
/**
 * GUI dialog for assigning ontology and agent classes
 * @author Nils
 *
 */
public class ClassSelectorDialog extends JDialog implements ActionListener{
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JButton jButtonConfirm = null;
	private JButton jButtonCancel = null;
	private JScrollPane jScrollPaneClassTable = null;
	private JTable jTableClasses = null;
	private JButton jButtonAddRow = null;
	private JButton jButtonRemoveRow = null;
	/**
	 * JComboBox used as cell editor for the agent classes column
	 */
	private JComboBox cellEditorAgentClass = null;
	/**
	 * All available agent classes, accessible by simple class name
	 */
	private HashMap<String, Class<?>> availableAgentClasses = null;
	/**
	 * Assigned agent classes. Key = user specified String, value = simple class name
	 */
	private HashMap<String, String> assignedAgentClasses = null;
	/**
	 * The GraphEnvironmentControllerGUI that started this dialog
	 */
	private GraphEnvironmentControllerGUI parent = null;
	
	/**
	 * This is the default constructor
	 */
	public ClassSelectorDialog(GraphEnvironmentControllerGUI parent, HashMap<String, String> agentClasses) {
		super(Application.MainWindow, Dialog.ModalityType.APPLICATION_MODAL);
		this.parent = parent;
		this.assignedAgentClasses = agentClasses;
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(400, 300);
		this.setContentPane(getJContentPane());
		this.setTitle(Language.translate("Klassenzuordnung"));
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.insets = new Insets(5, 5, 5, 5);
			gridBagConstraints11.gridy = 3;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 3;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.fill = GridBagConstraints.BOTH;
			gridBagConstraints10.gridy = 1;
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
		}
		return jContentPane;
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
		if (jTableClasses == null) {
			jTableClasses = new JTable();
			jTableClasses.setFillsViewportHeight(true);
			jTableClasses.setShowGrid(true);
			jTableClasses.setModel(getClassesTableModel());
			TableColumn agentClassColumn = jTableClasses.getColumnModel().getColumn(1);
			agentClassColumn.setCellEditor(new DefaultCellEditor(getCellEditorAgentClass()));
		}
		return jTableClasses;
	}
	
	/**
	 * This method initiates the jTableClasses' TableModel
	 * @return The TableModel
	 */
	private TableModel getClassesTableModel(){
		
		// The ComboBoxModels must be initiated before adding rows 
		getCellEditorAgentClass();
		
		// Headlines
		Vector<String> titles = new Vector<String>();
		titles.add(Language.translate("Datenfeld"));
//		titles.add(Language.translate("Ontologieklasse"));
		titles.add(Language.translate("Agentenklasse"));
		Vector<Vector<String>> dataRows = new Vector<Vector<String>>();
		
		// Set table entries for defined assignments, if any
		if(assignedAgentClasses != null){
			Iterator<String> dataFieldEntries = assignedAgentClasses.keySet().iterator();
			
			while(dataFieldEntries.hasNext()){
				// GraphML data field entry
				String dataEntry = dataFieldEntries.next();
				Vector<String> newRow = new Vector<String>();
				newRow.add(dataEntry);
				
				// Agent class assigned to this GraphML data field entry
				String agentClassName = assignedAgentClasses.get(dataEntry);
				
				if(agentClassName != null){
					// Class name without package
					String agentClassSimpleName = agentClassName.substring(agentClassName.lastIndexOf('.')+1);
					newRow.add(agentClassSimpleName);
				}else{
					newRow.add(Language.translate("Nicht definiert"));
				}
				dataRows.add(newRow);
			}
		}
		
		return new DefaultTableModel(dataRows, titles);
	}
	
	/**
	 * This method adds a new row to the jTableClasses' TableModel
	 */
	private void addRow(){
		Vector<String> rowData = new Vector<String>();
		rowData.add(Language.translate("Nicht definiert"));
//		rowData.add(Language.translate("Nicht definiert"));
		rowData.add(Language.translate("Nicht definiert"));
		((DefaultTableModel)getJTableClasses().getModel()).addRow(rowData);
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
			jButtonAddRow.setIcon(new ImageIcon(getClass().getResource("/agentgui/core/gui/img/ListPlus.png")));
			jButtonAddRow.addActionListener(this);
		}
		return jButtonAddRow;
	}
	
	/**
	 * This method initializes cellEditorAgentClass
	 * 
	 * @return @return javax.swing.JComboBox
	 */
	private JComboBox getCellEditorAgentClass(){
		if(cellEditorAgentClass == null){
			cellEditorAgentClass = new JComboBox(getAgentComboBoxModel());
		}
		return cellEditorAgentClass;
	}
	
	/**
	 * THis method builds a vector for initiating the cellEditorAgentClass' ComboBox model, and initiates the agentClasses HashMap.	
	 * @return Vector containing the simple names of the projects agent classes.
	 */
	private Vector<String> getAgentComboBoxModel(){
		Vector<String> agentClassNames = new Vector<String>();
		agentClassNames.add(Language.translate("Nicht definiert"));
		
		availableAgentClasses = new HashMap<String, Class<?>>();
		availableAgentClasses.put(Language.translate("Nicht definiert"), null);
		
		Iterator<Class<?>> agentClassesIterator= Application.ClassDetector.csAgents.getClassesFound(true).iterator();
		while(agentClassesIterator.hasNext()){
			Class<?> agentClass = agentClassesIterator.next();
			availableAgentClasses.put(agentClass.getSimpleName(), agentClass);
			agentClassNames.add(agentClass.getSimpleName());
		}
		return agentClassNames;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource().equals(getJButtonAddRow())){
			addRow();
		}else if(event.getSource().equals(getJButtonRemoveRow())){
			if(getJTableClasses().getSelectedRow() > -1){
				removeRow(getJTableClasses().getSelectedRow());
			}
		}else if(event.getSource().equals(getJButtonConfirm())){
//			HashMap<String, String> ontologyClasses = new HashMap<String, String>();
			HashMap<String, String> agentClasses = new HashMap<String, String>();
			
			JTable jtc = getJTableClasses();
			
			int rowNum = jtc.getRowCount();
			for(int i=0; i<rowNum; i++){
				agentClasses.put((String) jtc.getValueAt(i, 0), this.availableAgentClasses.get(jtc.getValueAt(i, 1)).getName());
			}
			
			parent.getController().setAgentClasses(agentClasses);
			
			this.setVisible(false);
		}else if(event.getSource().equals(getJButtonCancel())){
			// Roll back changes
			this.setVisible(false);
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
			jButtonRemoveRow.setIcon(new ImageIcon(getClass().getResource("/agentgui/core/gui/img/ListMinus.png")));
			jButtonRemoveRow.addActionListener(this);
		}
		return jButtonRemoveRow;
	}

}
