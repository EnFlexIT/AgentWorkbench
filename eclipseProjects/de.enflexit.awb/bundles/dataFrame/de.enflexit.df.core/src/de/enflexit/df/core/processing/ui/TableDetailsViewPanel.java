package de.enflexit.df.core.processing.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import de.enflexit.df.core.model.TablesawTableModel;

import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * This panel shows some general statistics and information about a tablesaw table.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TableDetailsViewPanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 6198592514559084651L;
	
	private JLabel jLabelTableOverview;
	private JScrollPane jScrollPaneTextArea;
	private JTable jTableOverview;
	private Table table;
	
	/**
	 * Instantiates a new table details view panel.
	 */
	public TableDetailsViewPanel() {
		initialize();
	}
	
	/**
	 * Instantiates a new table details view panel.
	 * @param table the table
	 */
	public TableDetailsViewPanel(Table table) {
		this.table = table;
		initialize();
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
		add(getJLabelTableOverview(), gbc_jLabelTableOverview);
		GridBagConstraints gbc_jScrollPaneTextArea = new GridBagConstraints();
		gbc_jScrollPaneTextArea.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneTextArea.gridx = 0;
		gbc_jScrollPaneTextArea.gridy = 1;
		add(getJScrollPaneTextArea(), gbc_jScrollPaneTextArea);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
	}
	
	/**
	 * Creates the summary table, describing all columns of the original table.
	 * @param table the table
	 * @return the table
	 */
	private Table createSummaryTable(Table table) {

		Map<String, Map<String, String>> statistics = new LinkedHashMap<>();
		Map<String, String> columnTypes = new LinkedHashMap<>();

		// --- Iterate over all columns -------------------
		for (Column<?> column : this.table.columns()) {

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

		// --- Create the result table, one describing column for each column of the original table
		StringColumn measure = StringColumn.create("Measure");
		Table result = Table.create("Column summary");
		result.addColumns(measure);

		for (String columnName : columnTypes.keySet()) {
			result.addColumns(StringColumn.create(columnName));
		}

		// --- First row: Column type ------------------------------- 
		measure.append("Type");

		for (String columnName : columnTypes.keySet()) {
			result.stringColumn(columnName).append(columnTypes.get(columnName));
		}

		// --- Further columns: all measurements --------------------
		for (Map.Entry<String, Map<String, String>> entry : statistics.entrySet()) {

			measure.append(entry.getKey());

			for (String columnName : columnTypes.keySet()) {
				String value = entry.getValue().get(columnName);

				result.stringColumn(columnName).append(value == null ? "" : value);
			}
		}

		return result;
	}
	
	private JLabel getJLabelTableOverview() {
		if (jLabelTableOverview == null) {
			jLabelTableOverview = new JLabel("Table Overview");
			jLabelTableOverview.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelTableOverview;
	}
	private JScrollPane getJScrollPaneTextArea() {
		if (jScrollPaneTextArea == null) {
			jScrollPaneTextArea = new JScrollPane();
			jScrollPaneTextArea.setViewportView(getJTableOverview());
		}
		return jScrollPaneTextArea;
	}
	private JTable getJTableOverview() {
		if (jTableOverview == null) {
			jTableOverview = new JTable();
			Table overviewTable = this.createSummaryTable(this.table);
			TablesawTableModel summaryTableModel = new TablesawTableModel(overviewTable);
			jTableOverview.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			jTableOverview.setModel(summaryTableModel);
			JTableHelper.autoResizeColumns(jTableOverview);
		}
		return jTableOverview;
	}
}
