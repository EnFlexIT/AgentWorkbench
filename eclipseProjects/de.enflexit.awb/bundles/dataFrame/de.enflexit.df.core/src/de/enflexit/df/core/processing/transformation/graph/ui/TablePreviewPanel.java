package de.enflexit.df.core.processing.transformation.graph.ui;

import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import de.enflexit.df.core.model.TablesawTableModel;
import de.enflexit.df.core.processing.transformation.graph.DataTableNode;
import de.enflexit.df.core.processing.ui.JTableHelper;
import de.enflexit.df.core.ui.DateTimeTableCellRenderer;

/**
 * This class combines a table view and an optional chart view of the table data.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TablePreviewPanel extends JPanel {
	
	private static final long serialVersionUID = 8505238319580952731L;
	
	private static final String DEFAULT_TIME_FORMAT = "dd.MM.yyyy HH:mm";
	
	private JPanel jPanelHeader;
	private JLabel jLabelHeader;
	private JScrollPane jScrollPaneTableView;
	private JTable jTableData;
	
	/**
	 * Instantiates a new table and chart view panel.
	 */
	public TablePreviewPanel() {
		initialize();
	}
	
	/**
	 * Instantiates a new table and chart view panel.
	 * @param tableNode the table node
	 */
	public TablePreviewPanel(DataTableNode tableNode) {
		this.initialize();
		this.setDataTable(tableNode);
	}

	/**
	 * Initializes the UI elements.
	 */
	private void initialize() {
		setLayout(new BorderLayout(0, 0));
		add(getJPanelHeader(), BorderLayout.NORTH);
		add(getJScrollPaneTableView(), BorderLayout.CENTER);
	}

	/**
	 * Gets the j panel header.
	 * @return the j panel header
	 */
	private JPanel getJPanelHeader() {
		if (jPanelHeader == null) {
			jPanelHeader = new JPanel();
			GridBagLayout gbl_jPanelHeader = new GridBagLayout();
			gbl_jPanelHeader.columnWidths = new int[]{0, 0};
			gbl_jPanelHeader.rowHeights = new int[]{0, 0};
			gbl_jPanelHeader.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_jPanelHeader.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			jPanelHeader.setLayout(gbl_jPanelHeader);
			GridBagConstraints gbc_jLabelHeader = new GridBagConstraints();
			gbc_jLabelHeader.anchor = GridBagConstraints.WEST;
			gbc_jLabelHeader.insets = new Insets(5, 5, 0, 0);
			gbc_jLabelHeader.gridx = 0;
			gbc_jLabelHeader.gridy = 0;
			jPanelHeader.add(getJLabelHeader(), gbc_jLabelHeader);
		}
		return jPanelHeader;
	}
	
	/**
	 * Gets the j label header.
	 * @return the j label header
	 */
	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel("<TableName>");
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeader;
	}
	
//	/**
//	 * Gets the j split pane table chart.
//	 * @return the j split pane table chart
//	 */
//	private JSplitPane getJSplitPaneTableChart() {
//		if (jSplitPaneTableChart == null) {
//			jSplitPaneTableChart = new JSplitPane();
//			jSplitPaneTableChart.setOrientation(JSplitPane.VERTICAL_SPLIT);
//			jSplitPaneTableChart.setTopComponent(getJScrollPaneTableView());
//			jSplitPaneTableChart.setBottomComponent(null);
//			jSplitPaneTableChart.setResizeWeight(1.0);
//			jSplitPaneTableChart.setDividerLocation(1.0);
//		}
//		return jSplitPaneTableChart;
//	}
	
	/**
	 * Gets the j scroll pane table view.
	 * @return the j scroll pane table view
	 */
	private JScrollPane getJScrollPaneTableView() {
		if (jScrollPaneTableView == null) {
			jScrollPaneTableView = new JScrollPane();
			jScrollPaneTableView.setViewportView(getJTableData());
		}
		return jScrollPaneTableView;
	}
	
	/**
	 * Gets the j table data.
	 * @return the j table data
	 */
	private JTable getJTableData() {
		if (jTableData == null) {
			jTableData = new JTable();
			jTableData.setDefaultRenderer(Object.class, new DateAndNumberTableCellRenderer(DEFAULT_TIME_FORMAT));
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
		this.getJLabelHeader().setText(dataTableNode.getLongText() + " (" + tableModel.getRowCount() + " rows)");
		JTableHelper.autoResizeColumns(this.getJTableData());
	}
	
	/**
	 * This extension of {@link DateTimeTableCellRenderer} adds formating for decimal numbers.
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 */
	private class DateAndNumberTableCellRenderer extends DateTimeTableCellRenderer {
		
		private static final long serialVersionUID = 7142510168344789846L;
		
		private static final int DECIMAL_PLACES = 2;
		
		private NumberFormat numberFormat;

		public DateAndNumberTableCellRenderer(String dateTimeFormat) {
			super(dateTimeFormat);
			this.setHorizontalAlignment(SwingConstants.CENTER);
		}
		
		@Override
		protected void setValue(Object value) {
			if (value instanceof Double || value instanceof Float) {
				super.setValue(this.getNumberFormat().format(((Number)value).doubleValue()));
			} else {
				super.setValue(value);
			}
		}
		
		public NumberFormat getNumberFormat() {
			if (numberFormat==null) {
				numberFormat = NumberFormat.getInstance(Locale.US);	// Use . as separator
				numberFormat.setMinimumFractionDigits(DECIMAL_PLACES);
				numberFormat.setMaximumFractionDigits(DECIMAL_PLACES);
			}
			return numberFormat;
		}
		
	}
}
