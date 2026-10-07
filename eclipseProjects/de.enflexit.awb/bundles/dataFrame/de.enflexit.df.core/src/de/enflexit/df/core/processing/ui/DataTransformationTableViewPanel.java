package de.enflexit.df.core.processing.ui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import de.enflexit.df.core.model.TablesawTableModel;
import de.enflexit.df.core.processing.transformationGraph.DataTableNode;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import java.awt.Font;

/**
 * A simple data view panel for tablesaw table data.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataTransformationTableViewPanel extends JPanel {
	
	private static final long serialVersionUID = -6601106600724743876L;
	
	private JScrollPane jScrollPaneDataTable;
	private JTable jTableData;
	private JLabel jLabelTableName;
	
	/**
	 * Instantiates a new data transformation table view panel.
	 */
	public DataTransformationTableViewPanel() {
		initialize();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize() {
		setLayout(new BorderLayout(0, 0));
		add(getJScrollPaneDataTable(), BorderLayout.CENTER);
		add(getJLabelTableName(), BorderLayout.NORTH);
	}

	/**
	 * Gets the j scroll pane data table.
	 * @return the j scroll pane data table
	 */
	private JScrollPane getJScrollPaneDataTable() {
		if (jScrollPaneDataTable == null) {
			jScrollPaneDataTable = new JScrollPane();
			jScrollPaneDataTable.setViewportView(getJTableData());
		}
		return jScrollPaneDataTable;
	}
	
	/**
	 * Gets the j table data.
	 * @return the j table data
	 */
	private JTable getJTableData() {
		if (jTableData == null) {
			jTableData = new JTable();
			jTableData.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		}
		return jTableData;
	}
	
	/**
	 * Sets the data table to show.
	 * @param dataTableNode the new data table
	 */
	public void setDataTable(DataTableNode dataTableNode) {
		TablesawTableModel tableModel = new TablesawTableModel(dataTableNode.getDataTable());
		this.getJTableData().setModel(tableModel);
		this.getJLabelTableName().setText(dataTableNode.getLabelText());
	}
	private JLabel getJLabelTableName() {
		if (jLabelTableName == null) {
			jLabelTableName = new JLabel("<TableName>");
			jLabelTableName.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelTableName.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		}
		return jLabelTableName;
	}
}
