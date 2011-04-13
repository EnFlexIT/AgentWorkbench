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
import agentgui.core.gui.ClassSelector;
import agentgui.core.gui.ClassSelectorTableCellEditor;
import agentgui.core.gui.components.ClassNameListCellRenderer;
import agentgui.core.gui.components.ClassNameTableCellRenderer;
import agentgui.core.jade.ClassSearcherSingle;
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
 * GUI dialog for assigning ontology and agent classes
 * @author Nils
 *
 */
public class ClassSelectionDialog extends JDialog implements ActionListener{
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
	private ClassSelector nodeClassSelector = null;
	/**
	 * JComboBox used as cell editor for the agent classes column
	 */
	private JComboBox jComboBoxAgentClasses = null;
	
	private JComboBox jComboBoxPrototypeClasses = null; 
	/**
	 * Cell editor for the prototype classes column
	 */
	private ClassSelectorTableCellEditor prototypeClassesCellEditor = null;
	/**
	 * All available agent classes, accessible by simple class name
	 */
	private HashMap<String, Class<?>> availableAgentClasses = null;
	
	private HashMap<String, Class<?>> availablePrototypeClasses = null;
	
	private ClassSearcherSingle csPrototypeClasses = null;
	/**
	 * The GraphEnvironmentControllerGUI that started this dialog
	 */
	private GraphEnvironmentControllerGUI parent = null;
	private JLabel jLabelNodeClass = null;
	private JTextField jTextFieldNodeClass = null;
	private JButton jButtonSelectNodeClass = null;
	/**
	 * This is the default constructor
	 */
	public ClassSelectionDialog(GraphEnvironmentControllerGUI parent) {
		super(Application.MainWindow, Dialog.ModalityType.APPLICATION_MODAL);
		
		csPrototypeClasses = new ClassSearcherSingle(GraphElementPrototype.class);
		csPrototypeClasses.startSearch();
		
		this.parent = parent;
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
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 3;
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.gridy = 2;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.gridy = 2;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.gridx = 2;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.gridwidth = 2;
			gridBagConstraints12.gridy = 2;
			jLabelNodeClass = new JLabel();
			jLabelNodeClass.setText("Übergabepunkte");
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
			jContentPane.add(jLabelNodeClass, gridBagConstraints12);
			jContentPane.add(getJTextFieldNodeClass(), gridBagConstraints3);
			jContentPane.add(getJButtonSelectNodeClass(), gridBagConstraints4);
		}
		return jContentPane;
	}
	
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
		if (jTableClasses == null) {
			jTableClasses = new JTable();
			jTableClasses.setFillsViewportHeight(true);
			jTableClasses.setShowGrid(true);
			jTableClasses.setModel(getClassesTableModel());
			TableColumn agentClassColumn = jTableClasses.getColumnModel().getColumn(1);
			agentClassColumn.setCellEditor(new DefaultCellEditor(getJComboBoxAgentClasses()));
			agentClassColumn.setCellRenderer(new ClassNameTableCellRenderer());
			TableColumn prototypeClassColumn = jTableClasses.getColumnModel().getColumn(2);
			prototypeClassColumn.setCellEditor(getPrototypeClassesCellEditor());
			prototypeClassColumn.setCellRenderer(new ClassNameTableCellRenderer());
		}
		return jTableClasses;
	}
	
	/**
	 * This method initiates the jTableClasses' TableModel
	 * @return The TableModel
	 */
	private TableModel getClassesTableModel(){
		
		// The ComboBoxModels must be initiated before adding rows 
		getJComboBoxAgentClasses();
		getJComboBoxPrototypeClasses();
		
		// Headlines
		Vector<String> titles = new Vector<String>();
		titles.add(Language.translate("Element"));
//		titles.add(Language.translate("Ontologieklasse"));
		titles.add(Language.translate("Agentenklasse"));
		titles.add(Language.translate("Graph-Prototyp"));
		Vector<Vector<String>> dataRows = new Vector<Vector<String>>();
		
		// Set table entries for defined assignments, if any
		HashMap<String, GraphElementSettings> etsHash = parent.getController().getGraphElementSettings();
		if(etsHash != null){
			Iterator<String> etsIter = etsHash.keySet().iterator();
			while(etsIter.hasNext()){
				String etName = etsIter.next();
				Vector<String> newRow = new Vector<String>();
				newRow.add(etName);
				newRow.add(etsHash.get(etName).getAgentClass());
				newRow.add(etsHash.get(etName).getGraphPrototype());
				dataRows.add(newRow);
			}
		}
		
		return new DefaultTableModel(dataRows, titles);
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
		
		Iterator<Class<?>> agentClassesIterator= Application.ClassDetector.csAgents.getClassesFound(true).iterator();
		while(agentClassesIterator.hasNext()){
			Class<?> agentClass = agentClassesIterator.next();
			availableAgentClasses.put(agentClass.getSimpleName(), agentClass);
			agentClassNames.add(agentClass.getName());
		}
		return agentClassNames;
	}
	
	private JComboBox getJComboBoxPrototypeClasses(){
		if(jComboBoxPrototypeClasses == null){
			jComboBoxPrototypeClasses = new JComboBox(getPrototypesComboBoxModel());
		}
		return jComboBoxPrototypeClasses;
	}
	
	private Vector<String> getPrototypesComboBoxModel(){
		availablePrototypeClasses = new HashMap<String, Class<?>>();
		availablePrototypeClasses.put(Language.translate("Undefiniert"), null);
		Iterator<Class<?>> ptClassesIterator = csPrototypeClasses.getClassesFound(false).iterator();
		while(ptClassesIterator.hasNext()){
			Class<?> ptClass = ptClassesIterator.next();
			availablePrototypeClasses.put(ptClass.getSimpleName(), ptClass);
		}
		return new Vector<String>(availablePrototypeClasses.keySet());
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
			
			JTable jtc = getJTableClasses();
			
			int rowNum = jtc.getRowCount();
			HashMap<String, GraphElementSettings> etsVector = new HashMap<String, GraphElementSettings>();
			for(int row=0; row<rowNum; row++){
				
				GraphElementSettings ets = new GraphElementSettings(
						(String)jtc.getValueAt(row, 1), 
						(String)jtc.getValueAt(row, 2));
				// Use name as key
				etsVector.put((String) jtc.getValueAt(row, 0), ets);
			}
			etsVector.put("node", new GraphElementSettings(getJTextFieldNodeClass().getText(), null));
			parent.getController().setGraphElementSettings(etsVector);
			
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

	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldNodeClass() {
		if (jTextFieldNodeClass == null) {
			jTextFieldNodeClass = new JTextField();
			GraphElementSettings nodeSettings = parent.getController().getGraphElementSettings().get("node");
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

}
