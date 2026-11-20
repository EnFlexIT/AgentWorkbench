package de.enflexit.df.core.processing.transformation.columnOperation.ui;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import de.enflexit.common.properties.Properties;
import de.enflexit.df.core.BundleHelper;
import de.enflexit.df.core.processing.transformation.DataTransformationService;
import de.enflexit.df.core.processing.transformation.columnOperation.DataTransformationServiceColumnOperation;
import de.enflexit.df.core.processing.transformation.columnOperation.ColumnOperationDefinition;
import de.enflexit.df.core.processing.transformation.graph.DataTableNode;
import de.enflexit.df.core.processing.transformation.ui.AbstractTransformationParameterConfigurationPanel;
import de.enflexit.df.core.processing.transformation.ui.DataTransformationConfigurationPanel;
import tech.tablesaw.api.Table;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JToolBar;
import javax.swing.table.TableColumnModel;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;

/**
 * A UI panel to configure data manipulation transformations.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ColumnOperationConfigurationPanel extends AbstractTransformationParameterConfigurationPanel implements ActionListener {

	private static final long serialVersionUID = -2642757287072217301L;
	
	public static final String COMBOBOX_NULL_PLACEHOLDER = "please select";
	
	private JToolBar jToolBarColumnOperations;
	private JScrollPane jScrollPaneColumnOperations;
	private JTable jTableColumnOperations;
	private JButton jButtonAddOperation;
	private JButton jButtonRemoveOperation;
	
	private ColumnOperationTableModel tableModel;
	
	private DataTableNode tableNode;
	
	/**
	 * Instantiates a new column operation configuration panel.
	 * @param transformation the transformation
	 */
	public ColumnOperationConfigurationPanel(DataTransformationService transformation) {
		super(transformation);
		initialize();
	}
	private void initialize() {
		setLayout(new BorderLayout(0, 0));
		add(getJToolBarColumnOperations(), BorderLayout.NORTH);
		add(getJScrollPaneColumnOperations(), BorderLayout.CENTER);
	}

	private JToolBar getJToolBarColumnOperations() {
		if (jToolBarColumnOperations == null) {
			jToolBarColumnOperations = new JToolBar();
			jToolBarColumnOperations.add(getJButtonAddOperation());
			jToolBarColumnOperations.add(getJButtonRemoveOperation());
		}
		return jToolBarColumnOperations;
	}
	private JScrollPane getJScrollPaneColumnOperations() {
		if (jScrollPaneColumnOperations == null) {
			jScrollPaneColumnOperations = new JScrollPane();
			jScrollPaneColumnOperations.setViewportView(getJTableColumnOperations());
		}
		return jScrollPaneColumnOperations;
	}
	private JTable getJTableColumnOperations() {
		if (jTableColumnOperations == null) {
			jTableColumnOperations = new JTable();
			jTableColumnOperations.setModel(this.getTableModel());
			jTableColumnOperations.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
			
		}
		return jTableColumnOperations;
	}
	private ColumnOperationTableModel getTableModel() {
		if (tableModel==null) {
			tableModel = new ColumnOperationTableModel(this.getTableSawTable());
		}
		return tableModel;
	}
	private JButton getJButtonAddOperation() {
		if (jButtonAddOperation == null) {
			jButtonAddOperation = new JButton(BundleHelper.getImageIcon("ListPlus.png"));
			jButtonAddOperation.addActionListener(this);
		}
		return jButtonAddOperation;
	}
	private JButton getJButtonRemoveOperation() {
		if (jButtonRemoveOperation == null) {
			jButtonRemoveOperation = new JButton(BundleHelper.getImageIcon("ListMinus.png"));
			jButtonRemoveOperation.addActionListener(this);
		}
		return jButtonRemoveOperation;
	}
	
	
	/**
	 * Gets the table saw table.
	 * @return the table saw table
	 */
	private Table getTableSawTable() {
		return this.tableNode!=null ? tableNode.getDataTable() : null;
	}
	
	/**
	 * Resets the table model and editors according to the currently selected table saw table.
	 */
	private void resetTableModelAndEditors() {
		this.tableModel = new ColumnOperationTableModel(this.getTableSawTable());
		this.getJTableColumnOperations().setModel(this.tableModel);
		
		TableColumnModel columnModel = this.getJTableColumnOperations().getColumnModel(); 
		columnModel.getColumn(0).setCellEditor(new TableCellEditorColumnSelection(this.tableNode.getDataTable().columns()));
		columnModel.getColumn(0).setCellRenderer(new TableCellRendererColumnSelection());
		columnModel.getColumn(1).setCellEditor(new TableCellEditorOperationSelection(tableModel));
		columnModel.getColumn(1).setCellRenderer(new TableCellRendererOperationSelection());
		columnModel.getColumn(2).setCellEditor(new TableCellEditorParameter(this.getJTableColumnOperations(), tableModel));
		columnModel.getColumn(3).setCellEditor(new TableCellEditorResultHandlingSelection(tableModel));
		columnModel.getColumn(3).setCellRenderer(new TableCellRendererResultHandlingSelection());
	}
	

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.ui.AbstractTransformationParameterConfigurationPanel#getConfiguredParameters()
	 */
	@Override
	public Properties getConfiguredParameters() {
		List<ColumnOperationDefinition> operationsList =  this.getTableModel().getRules();
		ArrayList<String> operationStrings = new ArrayList<String>();
		
		for (ColumnOperationDefinition operation : operationsList) {
			operationStrings.add(operation.toString());
		}
		
		String operationsListString = operationStrings.stream().collect(Collectors.joining(","));
		
		Properties paramProperties = new Properties();
		paramProperties.setStringValue(DataTransformationServiceColumnOperation.PARAM_NAME_TRANSFORMATIONS_LIST, operationsListString);
		return paramProperties;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.ui.AbstractTransformationParameterConfigurationPanel#setConfiguredParameters(de.enflexit.common.properties.Properties)
	 */
	@Override
	public void setConfiguredParameters(Properties parametersAsProperties) {
		String operationsListString = parametersAsProperties.getStringValue(DataTransformationServiceColumnOperation.PARAM_NAME_TRANSFORMATIONS_LIST);
		List<String> operationStrings = Arrays.asList(operationsListString.split(","));
		
		ArrayList<ColumnOperationDefinition> operationsList = new ArrayList<ColumnOperationDefinition>();
		for (String operationString : operationStrings) {
			operationsList.add(ColumnOperationDefinition.fromString(operationString));
		}
		this.getTableModel().setOperationsList(operationsList);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJButtonAddOperation()) {
			this.getTableModel().addRule();
		} else if (ae.getSource()==this.getJButtonRemoveOperation()) {
			int selectedRow = this.getJTableColumnOperations().getSelectedRow();
			if (selectedRow>=0) {
				this.getTableModel().removeRule(this.getJTableColumnOperations().convertRowIndexToModel(selectedRow));
			}
		}
	}
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent pce) {
		if(pce.getPropertyName().equals(DataTransformationConfigurationPanel.EVENT_ID_TABLE_SELECTED)) {
			DataTableNode affectedNode = (DataTableNode) pce.getNewValue();
			this.tableNode = affectedNode;
			this.resetTableModelAndEditors();
		}
	}
}
