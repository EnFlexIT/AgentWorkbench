package de.enflexit.df.descriptionService.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JPanel;
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
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * This panel shows a list of available data columns in a selected data source,
 * and a sub-panel for actually editing the description of the selected column.  
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataColumnDescriptionEditorDetailsPanel extends JPanel implements ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 5825850682049517170L;
	
	private DefaultListModel<String> columnsListModel;

	private DescriptionsController descriptionController;
	
	private DefaultDataSource dataSource;
	private Table table;
	private List<ColumnDescription> descriptionsList;
	
	private DataColumnDescriptionEditorPanel jPanelColumnEditor;
	private JScrollPane jScrollPaneColumnsList;
	private JList<String> jListColumnsList;
	
	/**
	 * Instantiates a new data column description editor details panel.
	 */
	public DataColumnDescriptionEditorDetailsPanel(DescriptionsController descriptionController) {
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
		add(getJPanelColumnEditor(), gbc_jPanelColumnEditor);
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
	private DataColumnDescriptionEditorPanel getJPanelColumnEditor() {
		if (jPanelColumnEditor == null) {
			jPanelColumnEditor = new DataColumnDescriptionEditorPanel();
			jPanelColumnEditor.addChangeListener(this.descriptionController);
		}
		return jPanelColumnEditor;
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
			String selectedColumn = this.getJListColumnsList().getSelectedValue();
			
			// --- Check if there is already a description for this column
			DataColumnDescription columnDescription = this.descriptionController.getColumnDescriptions().get(selectedColumn);
			
			// --- If not, create one with some default values
			if (columnDescription==null) {
				
				ColumnDescription colDesc = this.findMatchingColumnDescription(selectedColumn);
				
				columnDescription = new DataColumnDescription();
				columnDescription.setColumnName(selectedColumn);
				if (colDesc!=null && colDesc.getTableName()!=null) {
					columnDescription.setTableName(colDesc.getColumnName());
				}
				if (colDesc!=null && colDesc.getColumnType()!=null) {
//					columnDescription.setDataType(colDesc.getColumnType());
				}
				columnDescription.setName(selectedColumn);
				
				this.descriptionController.getColumnDescriptions().put(selectedColumn, columnDescription);
			}
			
			this.getJPanelColumnEditor().setDataColumnDescription(columnDescription);
		}
	}
	
	private ColumnDescription findMatchingColumnDescription(String columnName) {
		for (ColumnDescription colDesc : this.getDescriptionsList()) {
			if (colDesc.getColumnName().equals(columnName)) {
				return colDesc;
			}
		}
		return null;
	}
	
}
