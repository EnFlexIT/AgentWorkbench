package de.enflexit.df.core.processing.ui;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import tech.tablesaw.api.Table;

/**
 * A simple data view panel for tablesaw table data.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataTransformationTableViewPanel extends JPanel {
	
	private static final long serialVersionUID = -6601106600724743876L;
	
	private JScrollPane jScrollPaneDataTable;
	private JTable jTableData;
	
	public DataTransformationTableViewPanel() {
		initialize();
	}
	private void initialize() {
		setLayout(new BorderLayout(0, 0));
		add(getJScrollPaneDataTable(), BorderLayout.CENTER);
	}

	private JScrollPane getJScrollPaneDataTable() {
		if (jScrollPaneDataTable == null) {
			jScrollPaneDataTable = new JScrollPane();
			jScrollPaneDataTable.setViewportView(getJTableData());
		}
		return jScrollPaneDataTable;
	}
	private JTable getJTableData() {
		if (jTableData == null) {
			jTableData = new JTable();
		}
		return jTableData;
	}
	
	public void setDataTable(Table dataTable) {
		
	}
}
