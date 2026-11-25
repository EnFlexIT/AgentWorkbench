package de.enflexit.df.core.ui.tableStatistics;

import static tech.tablesaw.aggregate.AggregateFunctions.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.swing.JPanel;

import tech.tablesaw.api.BooleanColumn;
import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.DateTimeColumn;
import tech.tablesaw.api.InstantColumn;
import tech.tablesaw.api.NumericColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.api.TimeColumn;
import tech.tablesaw.columns.Column;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Font;
import javax.swing.JLabel;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
	
	private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss", Locale.GERMANY);
	
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
	 * 
	 * @param dataTable the table
	 * @return the table
	 */
	private Table createStatisticsTable(Table dataTable) {

		Map<String, Map<String, String>> statistics = new LinkedHashMap<>();
		Map<String, String> columnTypes = new LinkedHashMap<>();

		// --- Iterate over all columns -------------------
		for (Column<?> column : dataTable.columns()) {

			String columnName = column.name();
			columnTypes.put(columnName, column.type().name());

			// --- Create customized column summary
			Table summary = summarizeColumn(dataTable, column);

			if (summary.columnCount() == 0 || summary.rowCount() == 0) {
				continue;
			}

			// --- Read the aggregated values
			for (Column<?> statisticColumn : summary.columns()) {

				String label = statisticColumn.name();

				// Remove the source column suffix (e.g. "Mean [Temperature]" -> "Mean")
				int suffixIndex = label.lastIndexOf(" [");
				if (suffixIndex >= 0) {
					label = label.substring(0, suffixIndex);
				}

				label = adjustStatisticName(label);

				String value = this.formatStatisticValue(column, statisticColumn.getString(0));

				statistics.computeIfAbsent(label, key -> new LinkedHashMap<>()).put(columnName, value);
			}
		}

		// --- Create result table ------------------------------
		Table result = Table.create("Column summary");

		StringColumn statisticValueNames = StringColumn.create("Measure");
		result.addColumns(statisticValueNames);

		// --- One column per original column
		for (String columnName : columnTypes.keySet()) {
			result.addColumns(StringColumn.create(columnName));
		}

		// --- First row: Type of each column
		statisticValueNames.append("Type");

		for (String columnName : columnTypes.keySet()) {
			result.stringColumn(columnName).append(columnTypes.get(columnName));
		}

		// --- One row per statistic value
		for (Map.Entry<String, Map<String, String>> entry : statistics.entrySet()) {

			statisticValueNames.append(entry.getKey());

			for (String columnName : columnTypes.keySet()) {
				String value = entry.getValue().get(columnName);
				result.stringColumn(columnName).append(value == null ? "" : value);
			}
		}

		return result;
	}
	
	/**
	 * Provides a summary table for one column. Contained values depend on the column type.
	 * @param dataTable the data table
	 * @param column the column
	 * @return the table
	 */
	private Table summarizeColumn(Table dataTable, Column<?> column) {

		// --- Decide which statistics to use, depending on the column type ---
		if (column instanceof NumericColumn<?>) {
			return dataTable.summarize(column, count, countUnique, countMissing, min, max, range, mean, median, stdDev).apply();
		}
		if (column instanceof DateColumn) {
			return dataTable.summarize(column, count, countUnique, countMissing, earliestDate, latestDate).apply();
		}
		if (column instanceof DateTimeColumn) {
			return dataTable.summarize(column, count, countUnique, countMissing, earliestDateTime, latestDateTime).apply();
		}
		if (column instanceof InstantColumn) {
			return dataTable.summarize(column, count, countUnique, countMissing, minInstant, maxInstant).apply();
		}
		if (column instanceof TimeColumn timeColumn) {

			Table summary = dataTable.summarize(column, count, countUnique, countMissing).apply();

			// --- Provided by the column, but not as AggregateFunction implementation.
			summary.addColumns(
					StringColumn.create("Min",new String[] { timeColumn.isEmpty() ? "" : timeColumn.min().toString() }),
					StringColumn.create("Max",new String[] { timeColumn.isEmpty() ? "" : timeColumn.max().toString() })
			);

			return summary;
		}
		if (column instanceof BooleanColumn) {
			return dataTable.summarize(column, count, countUnique, countMissing, countTrue, countFalse, allTrue, anyTrue, noneTrue).apply();
		}
		if (column instanceof StringColumn) {
			return dataTable.summarize(column, count, countUnique, countMissing).apply();
		}

		return Table.create("Empty summary");
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
	
	
	/**
	 * This method can be used to adjust the statistic names provided from TableSaw. 
	 * @param name the name
	 * @return the string
	 */
	private String adjustStatisticName(String name) {
		return switch (name) {
		// --- Unify temporal and numeric min and max value. 
		case "Earliest", "Min Instant" -> "Min";
		case "Latest", "Max Instant" -> "Max";
		case "Count Unique" -> "Unique Values";
		// --- Default: Leave unchanged ----------......... 
		default -> name;
		};
	}
	
	private String formatStatisticValue(Column<?> column, String value) {

		if (value == null || value.isBlank()) {
			return "";
		}

		try {
			if (column instanceof DateTimeColumn) {
				return LocalDateTime.parse(value).format(DATE_TIME_FORMAT);
			}

			if (column instanceof InstantColumn) {
				return Instant.parse(value).atZone(ZoneId.of("Europe/Berlin")).format(DATE_TIME_FORMAT);
			}

			if (column instanceof DateColumn) {
				return LocalDate.parse(value).atStartOfDay().format(DATE_TIME_FORMAT);
			}

		} catch (DateTimeParseException dte) {
			// Return original value if parsing fails
		}

		return value;
	}
}
