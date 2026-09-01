package de.enflexit.df.descriptionService.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;

import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.Table;

import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.enflexit.df.core.dataSources.DefaultDataSource;
import de.enflexit.df.core.extension.ColumnDescription;
import de.enflexit.df.descriptionService.DescriptionsController;
import de.enflexit.df.descriptionService.db.DataColumnDescription;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * This panel shows a list of available data columns in a selected data source,
 * and a sub-panel for actually editing the description of the selected column.  
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DescriptionEditorColumnSelectionPanel extends JPanel implements ActionListener, ListSelectionListener {
	
	private enum Decision {
		APPLY, DISCARD, CANCEL
	}

	private static final long serialVersionUID = 5825850682049517170L;
	
	private static final String REGEX_REMOVE_ALSO_AVAILABLE = "\\s\\(also available in:\\s.+\\)";
	
	private DefaultListModel<String> columnsListModel;

	private DescriptionsController descriptionController;
	
	private DefaultDataSource dataSource;
	private Table table;
	private List<ColumnDescription> descriptionsList;
	
	private DescriptionEditorColumnDetailsPanel columnDetailsEditorPanel;
	private JScrollPane jScrollPaneColumnsList;
	private JList<String> jListColumnsList;
	
	private String currentSelection;
	
	private boolean pauseSelectionListener;
	
	/**
	 * Instantiates a new data column description editor details panel.
	 */
	public DescriptionEditorColumnSelectionPanel(DescriptionsController descriptionController) {
		this.descriptionController = descriptionController;
		this.initialize();
	}
	
	/**
	 * Initializes the UI components.
	 */
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jPanelColumnEditor = new GridBagConstraints();
		gbc_jPanelColumnEditor.insets = new Insets(5, 10, 5, 5);
		gbc_jPanelColumnEditor.weightx = 2.0;
		gbc_jPanelColumnEditor.fill = GridBagConstraints.BOTH;
		gbc_jPanelColumnEditor.gridx = 1;
		gbc_jPanelColumnEditor.gridy = 0;
		add(getColumnDetailsEditorPanel(), gbc_jPanelColumnEditor);
		GridBagConstraints gbc_jScrollPaneColumnsList = new GridBagConstraints();
		gbc_jScrollPaneColumnsList.weightx = 1.0;
		gbc_jScrollPaneColumnsList.insets = new Insets(5, 5, 10, 5);
		gbc_jScrollPaneColumnsList.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneColumnsList.gridx = 0;
		gbc_jScrollPaneColumnsList.gridy = 0;
		add(getJScrollPaneColumnsList(), gbc_jScrollPaneColumnsList);
	}

	/**
	 * Gets the j panel column editor.
	 * @return the j panel column editor
	 */
	private DescriptionEditorColumnDetailsPanel getColumnDetailsEditorPanel() {
		if (columnDetailsEditorPanel == null) {
			columnDetailsEditorPanel = new DescriptionEditorColumnDetailsPanel();
			columnDetailsEditorPanel.addChangeListener(this.descriptionController);
		}
		return columnDetailsEditorPanel;
	}

	/**
	 * Gets the j scroll pane columns list.
	 * @return the j scroll pane columns list
	 */
	private JScrollPane getJScrollPaneColumnsList() {
		if (jScrollPaneColumnsList == null) {
			jScrollPaneColumnsList = new JScrollPane();
			jScrollPaneColumnsList.setViewportView(getJListColumnsList());
		}
		return jScrollPaneColumnsList;
	}

	/**
	 * Gets the j list columns list.
	 * @return the j list columns list
	 */
	private JList<String> getJListColumnsList() {
		if (jListColumnsList == null) {
			jListColumnsList = new JList<>();
			jListColumnsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jListColumnsList.addListSelectionListener(this);
		}
		return jListColumnsList;
	}

	/**
	 * Gets the columns list model.
	 * @return the columns list model
	 */
	private DefaultListModel<String> getColumnsListModel() {
		if (columnsListModel==null) {
			columnsListModel = new DefaultListModel<String>();
			if (this.table!=null) {
				for (String columnName : this.table.columnNames()) {
					columnsListModel.addElement(columnName);
				}
			} else {
				columnsListModel.addElement("No data source selected");
			}
		}
		return columnsListModel;
	}

	/**
	 * Resets the column  list model.
	 */
	private void resetListModel() {
		this.columnsListModel = null;
		this.getJListColumnsList().setModel(this.getColumnsListModel());
	}

	/**
	 * Gets the table.
	 * @return the table
	 */
	public Table getTable() {
		return table;
	}
	/**
	 * Sets the table.
	 * @param table the new table
	 */
	public void setTable(Table table) {
		if (table!=this.table) {
			this.table = table;
			this.resetListModel();
		}
	}
	
	
	
	public List<ColumnDescription> getDescriptionsList() {
		return descriptionsList;
	}

	public void setDescriptionsList(List<ColumnDescription> descriptionsList) {
		this.descriptionsList = descriptionsList;
	}

	public DefaultDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DefaultDataSource dataSource) {
		this.dataSource = dataSource;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent lse) {
		if (lse.getSource()==this.getJListColumnsList() && lse.getValueIsAdjusting()==false) {

			// --- Listener currently disabled
			if (this.pauseSelectionListener==true) return;
			
			// --- Selection not changed
			String newSelection = this.getJListColumnsList().getSelectedValue();
			if (newSelection==this.currentSelection) return;
			
			// --- Check for unsaved changes, ask user if necessary
			if (this.checkAllowLeaveSelection()==false) {
				this.restoreSelection();
				return;
			}
			
			// --- Check if there is already a description for this column
			DataColumnDescription columnDescription = this.descriptionController.getColumnDescriptions().get(newSelection);
			
			// --- If not, create one with some default values
			if (columnDescription==null) {
				columnDescription = this.createNewColumnDescription(newSelection);
			}
			
			// --- Set the description object to the editor panel
			this.getColumnDetailsEditorPanel().setDataColumnDescription(columnDescription);

			// --- Remember the current selection
			this.currentSelection = newSelection;
		}
	}
	
	private DataColumnDescription createNewColumnDescription(String columnName) {
		
		if (columnName==null) return null;
		
		ColumnDescription colDesc = this.findMatchingColumnDescription(columnName);
		DataColumnDescription columnDescription = new DataColumnDescription();
		columnDescription.setColumnName(columnName);
		columnDescription.setDataSource(this.dataSource);
		if (colDesc!=null && colDesc.getTableName()!=null) {
			String tableNameOnly =colDesc.getTableName().replaceAll(REGEX_REMOVE_ALSO_AVAILABLE, ""); 
			columnDescription.setTableName(tableNameOnly);
		}
		
		ColumnType columnType = this.getColumnTypeForPrinterFriendlyName(colDesc.getColumnType());
		columnDescription.setColumnType(columnType!=null ? columnType.name() : null);
		
		this.descriptionController.getColumnDescriptions().put(columnName, columnDescription);
		return columnDescription;
	}
	
	private ColumnType getColumnTypeForPrinterFriendlyName(String printerFriendlyName) {
		for (ColumnType columnType : ColumnType.values()) {
			if (columnType.getPrinterFriendlyName().equals(printerFriendlyName)) {
				return columnType;
			}
		}
		return null;
	}
	
	/**
	 * Finds the column description for the provided name.
	 * @param columnName the column name
	 * @return the column description
	 */
	private ColumnDescription findMatchingColumnDescription(String columnName) {
		for (ColumnDescription colDesc : this.getDescriptionsList()) {
			if (colDesc.getColumnName().equals(columnName)) {
				return colDesc;
			}
		}
		return null;
	}
	
	/**
	 * Check allow selection change.
	 * @return true, if successful
	 */
	public boolean checkAllowLeaveSelection() {
		
		// --- No pending changes -------------------------
		if (this.getColumnDetailsEditorPanel().isDirty()==false) {
			return true;
		} else {
			switch (this.askUserForConfirmaiton()) {
			case APPLY:
				// --- Apply the changes, then allow selection change
				this.getColumnDetailsEditorPanel().applyChanges();
				return true;
			case DISCARD:
				// --- Discard changes -> no need to apply, allow selection change 
				this.getColumnDetailsEditorPanel().revertChanges();
				return true;
			case CANCEL:
			default:
				return false;
			}
		}
		
	}
	
	/**
	 * Ask for user confirmation in case of unsaved changes.
	 * @return the decision
	 */
	private Decision askUserForConfirmaiton() {
		Object[] options = {"Apply", "Revert", "Cancel"};
		int result = JOptionPane.showOptionDialog(this, "There are unsaved changes!", "Unsaved Changes", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
		return switch (result) {
			case 0 -> Decision.APPLY;
			case 1 -> Decision.DISCARD;
			default -> Decision.CANCEL;
		};
	}
	
	private void restoreSelection() {
		this.pauseSelectionListener = true;
		this.getJListColumnsList().setSelectedValue(currentSelection, true);
		this.pauseSelectionListener = false;
	}
	
}
