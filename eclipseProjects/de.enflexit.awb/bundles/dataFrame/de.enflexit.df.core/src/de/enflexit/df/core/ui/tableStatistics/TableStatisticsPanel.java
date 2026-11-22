package de.enflexit.df.core.ui.tableStatistics;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JPanel;

import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Font;
import javax.swing.JLabel;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import de.enflexit.df.core.model.TablesawTableModel;
import de.enflexit.df.core.processing.ui.JTableHelper;

import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * This panel shows some general statistics and information about a tablesaw table.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TableStatisticsPanel extends JPanel {
	
	private static final long serialVersionUID = 6198592514559084651L;
	
	private JLabel jLabelTableStatistics;
	private JScrollPane jScrollPaneStatisticsTable;
	private JTable jTableStatisticsTable;
	private Table dataTable;
	
	private HashMap<Table, Table> statisticsTableCache;
	
	/**
	 * Instantiates a new table details view panel.
	 */
	public TableStatisticsPanel() {
		initialize();
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent ce) {
				// --- Create and set the statistics table model when the tab is shown
				TableStatisticsPanel.this.setStatisticsTableModel();
			}
		});
	}
	
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelTableOverview = new GridBagConstraints();
		gbc_jLabelTableOverview.anchor = GridBagConstraints.WEST;
		gbc_jLabelTableOverview.insets = new Insets(5, 5, 5, 0);
		gbc_jLabelTableOverview.gridx = 0;
		gbc_jLabelTableOverview.gridy = 0;
		add(getJLabelTableStatistics(), gbc_jLabelTableOverview);
		GridBagConstraints gbc_jScrollPaneTextArea = new GridBagConstraints();
		gbc_jScrollPaneTextArea.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneTextArea.gridx = 0;
		gbc_jScrollPaneTextArea.gridy = 1;
		add(getJScrollPaneStatisticsTable(), gbc_jScrollPaneTextArea);
	}
	
	/**
	 * Sets the data table to show statistics for.
	 * @param dataTable the new data table
	 */
	public void setDataTable(Table dataTable) {
		this.dataTable = dataTable;
		if (dataTable!=null) {
			if (this.isShowing()==true) {
				// --- No need to create and show the statistics table if the tab is not visible
				this.setStatisticsTableModel();
			}
		}
	}
	
	/**
	 * Sets the statistics table model.
	 */
	private void setStatisticsTableModel() {
		TablesawTableModel summaryTableModel = new TablesawTableModel(this.getStatisticsTable(this.dataTable));
		this.getJTableStatisticsTable().setModel(summaryTableModel);
	}

	/**
	 * Creates the summary table, describing all columns of the original table.
	 * @param dataTable the table
	 * @return the table
	 */
	private Table createStatisticsTable(Table dataTable) {
		
		Map<String, Map<String, String>> statistics = new LinkedHashMap<>();
		Map<String, String> columnTypes = new LinkedHashMap<>();

		// --- Iterate over all columns -------------------
		for (Column<?> column : this.dataTable.columns()) {

			String columnName = column.name();
			columnTypes.put(columnName, column.type().name());

			// --- Create the summary table for this column  
			//TODO customize shown information
			Table summary = column.summary();

			if (summary.columnCount() < 2) {
				continue;
			}

			Column<?> labels = summary.column(0);
			Column<?> values = summary.column(1);

			for (int i = 0; i < summary.rowCount(); i++) {

				String label = labels.getString(i);
				String value = values.getString(i);

				statistics.computeIfAbsent(label, key -> new LinkedHashMap<>()).put(columnName, value);
			}
		}

		// --- Create the result table ------------------------------
		Table result = Table.create("Column summary");
		
		// --- First column contains the names of the features displayed in the current row. 
		StringColumn statisticValueNames = StringColumn.create("Measure");
		result.addColumns(statisticValueNames);

		// --- Further columns: One column per column of the original table
		for (String columnName : columnTypes.keySet()) {
			result.addColumns(StringColumn.create(columnName));
		}

		// --- First row: Type of each column -----------------------
		statisticValueNames.append("Type");
		for (String columnName : columnTypes.keySet()) {
			result.stringColumn(columnName).append(columnTypes.get(columnName));
		}

		// --- One row per statistic value to be displayed ----------
		for (Map.Entry<String, Map<String, String>> entry : statistics.entrySet()) {

			statisticValueNames.append(entry.getKey());

			// --- Append the value for the current measurement to the corresponding column
			for (String columnName : columnTypes.keySet()) {
				String value = entry.getValue().get(columnName);
				result.stringColumn(columnName).append(value == null ? "" : value);
			}
		}
		
		return result;
	}
	
	private JLabel getJLabelTableStatistics() {
		if (jLabelTableStatistics == null) {
			jLabelTableStatistics = new JLabel("Table Statistics");
			jLabelTableStatistics.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelTableStatistics;
	}
	private JScrollPane getJScrollPaneStatisticsTable() {
		if (jScrollPaneStatisticsTable == null) {
			jScrollPaneStatisticsTable = new JScrollPane();
			jScrollPaneStatisticsTable.setViewportView(getJTableStatisticsTable());
		}
		return jScrollPaneStatisticsTable;
	}
	private JTable getJTableStatisticsTable() {
		if (jTableStatisticsTable == null) {
			jTableStatisticsTable = new JTable();
			jTableStatisticsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			JTableHelper.autoResizeColumns(jTableStatisticsTable);
		}
		return jTableStatisticsTable;
	}
	
	/**
	 * Gets the statistics table cache.
	 * @return the statistics table cache
	 */
	private HashMap<Table, Table> getStatisticsTableCache() {
		if (statisticsTableCache==null) {
			statisticsTableCache = new HashMap<Table, Table>();
		}
		return statisticsTableCache;
	}
	
	/**
	 * Gets the statistics table for the provided data table.
	 * @param dataTable the data table
	 * @return the statistics table
	 */
	private Table getStatisticsTable(Table dataTable) {
		// --- Check the cache first --------------------------------
		Table statisticsTable = this.getStatisticsTableCache().get(dataTable);
		if (statisticsTable==null) {
			// --- If not found, create a statistics table ----------
			statisticsTable = this.createStatisticsTable(dataTable);
			// --- Remember for later re-user -----------------------
			this.getStatisticsTableCache().put(dataTable, statisticsTable);
		}
		return statisticsTable;
	}
}
