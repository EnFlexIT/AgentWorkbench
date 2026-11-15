package de.enflexit.df.core.processing.transformation.metrics.ui;

import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import java.util.List;

import de.enflexit.common.properties.Properties;
import de.enflexit.common.swing.CheckBoxList;
import de.enflexit.df.core.processing.transformation.DataTransformationService;
import de.enflexit.df.core.processing.transformation.graph.DataTableNode;
import de.enflexit.df.core.processing.transformation.metrics.DataTransformationCalculateMetrics.Metrics;
import de.enflexit.df.core.processing.transformation.ui.AbstractTransformationParameterConfigurationPanel;
import de.enflexit.df.core.processing.transformation.ui.DataTransformationConfigurationPanel;
import tech.tablesaw.api.Table;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;

/**
 * The Class MetricsCalculationConfigurationPanel.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class MetricsCalculationConfigurationPanel extends AbstractTransformationParameterConfigurationPanel {

	private static final long serialVersionUID = -2442416016698223341L;
	private JLabel jLabelReferenceColumn;
	private JComboBox<String> jComboBoxReferenceColumn;
	private JLabel jLabelMetrics;
	private JScrollPane jScrollPaneMetricsList;
	private CheckBoxList<Metrics> metricsList;
	private JLabel jLabelCompareColumns;
	private JScrollPane jScrollPaneCompareColumnsList;
	private CheckBoxList<String> compareColumnsList;
	
	private Table table;

	public MetricsCalculationConfigurationPanel(DataTransformationService transformation) {
		super(transformation);
		initialize();
	}
	private void initialize() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		GridBagConstraints gbc_jLabelReferenceColumn = new GridBagConstraints();
		gbc_jLabelReferenceColumn.anchor = GridBagConstraints.WEST;
		gbc_jLabelReferenceColumn.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelReferenceColumn.gridx = 0;
		gbc_jLabelReferenceColumn.gridy = 0;
		add(getJLabelReferenceColumn(), gbc_jLabelReferenceColumn);
		GridBagConstraints gbc_jComboBoxReferenceColumn = new GridBagConstraints();
		gbc_jComboBoxReferenceColumn.insets = new Insets(5, 5, 5, 10);
		gbc_jComboBoxReferenceColumn.fill = GridBagConstraints.HORIZONTAL;
		gbc_jComboBoxReferenceColumn.gridx = 1;
		gbc_jComboBoxReferenceColumn.gridy = 0;
		add(getJComboBoxReferenceColumn(), gbc_jComboBoxReferenceColumn);
		GridBagConstraints gbc_jLabelMetrics = new GridBagConstraints();
		gbc_jLabelMetrics.anchor = GridBagConstraints.NORTHWEST;
		gbc_jLabelMetrics.insets = new Insets(5, 5, 5, 5);
		gbc_jLabelMetrics.gridx = 0;
		gbc_jLabelMetrics.gridy = 1;
		add(getJLabelMetrics(), gbc_jLabelMetrics);
		GridBagConstraints gbc_jScrollPaneMetricsList = new GridBagConstraints();
		gbc_jScrollPaneMetricsList.insets = new Insets(5, 5, 5, 10);
		gbc_jScrollPaneMetricsList.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneMetricsList.gridx = 1;
		gbc_jScrollPaneMetricsList.gridy = 1;
		add(getJScrollPaneMetricsList(), gbc_jScrollPaneMetricsList);
		GridBagConstraints gbc_jLabelCompareColumns = new GridBagConstraints();
		gbc_jLabelCompareColumns.anchor = GridBagConstraints.NORTHWEST;
		gbc_jLabelCompareColumns.insets = new Insets(5, 5, 0, 5);
		gbc_jLabelCompareColumns.gridx = 0;
		gbc_jLabelCompareColumns.gridy = 2;
		add(getJLabelCompareColumns(), gbc_jLabelCompareColumns);
		GridBagConstraints gbc_jScrollPaneCompareColumnsList = new GridBagConstraints();
		gbc_jScrollPaneCompareColumnsList.insets = new Insets(5, 5, 0, 10);
		gbc_jScrollPaneCompareColumnsList.fill = GridBagConstraints.BOTH;
		gbc_jScrollPaneCompareColumnsList.gridx = 1;
		gbc_jScrollPaneCompareColumnsList.gridy = 2;
		add(getJScrollPaneCompareColumnsList(), gbc_jScrollPaneCompareColumnsList);
	}

	private JLabel getJLabelReferenceColumn() {
		if (jLabelReferenceColumn == null) {
			jLabelReferenceColumn = new JLabel("Reference Column");
			jLabelReferenceColumn.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelReferenceColumn;
	}
	private JComboBox<String> getJComboBoxReferenceColumn() {
		if (jComboBoxReferenceColumn == null) {
			jComboBoxReferenceColumn = new JComboBox<>();
		}
		return jComboBoxReferenceColumn;
	}
	private JLabel getJLabelMetrics() {
		if (jLabelMetrics == null) {
			jLabelMetrics = new JLabel("Metrics to apply");
			jLabelMetrics.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelMetrics;
	}
	private JScrollPane getJScrollPaneMetricsList() {
		if (jScrollPaneMetricsList == null) {
			jScrollPaneMetricsList = new JScrollPane();
			getJScrollPaneMetricsList().setViewportView(this.getMetricsList());
		}
		return jScrollPaneMetricsList;
	}
	private CheckBoxList<Metrics> getMetricsList() {
		if (metricsList==null) {
			DefaultListModel<Metrics> metricsListModel = new DefaultListModel<Metrics>();
			metricsListModel.addAll(Arrays.asList(Metrics.values()));
			metricsList = new CheckBoxList<Metrics>(metricsListModel, Metrics::label);
		}
		return metricsList;
	}
	private JLabel getJLabelCompareColumns() {
		if (jLabelCompareColumns == null) {
			jLabelCompareColumns = new JLabel("Columns to compare");
			jLabelCompareColumns.setFont(new Font("Dialog", Font.PLAIN, 12));
		}
		return jLabelCompareColumns;
	}
	private JScrollPane getJScrollPaneCompareColumnsList() {
		if (jScrollPaneCompareColumnsList == null) {
			jScrollPaneCompareColumnsList = new JScrollPane();
			jScrollPaneCompareColumnsList.setViewportView(this.getCompareColumnsList());
		}
		return jScrollPaneCompareColumnsList;
	}
	private CheckBoxList<String> getCompareColumnsList() {
		if (compareColumnsList==null) {
			compareColumnsList = new CheckBoxList<String>();
		}
		return compareColumnsList;
	}
	
	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent pce) {
		if (pce.getPropertyName().equals(DataTransformationConfigurationPanel.EVENT_ID_TABLE_SELECTED)) {
			Table selectedTable = ((DataTableNode) pce.getNewValue()).getDataTable();
			if (selectedTable!=this.table) {
				this.setTable(selectedTable);
			}
		}
	}
	
	/**
	 * Sets the table.
	 * @param table the new table
	 */
	private void setTable(Table table) {
		this.table = table;
		this.rebuildListModels();
	}
	
	/**
	 * Rebuilds the list models for table column selection.
	 */
	private void rebuildListModels() {
		List<String> columnsList = this.table.columnNames();
		DefaultListModel<String> checkboxListModel = new DefaultListModel<String>();
		checkboxListModel.addAll(columnsList);
		this.getCompareColumnsList().setModel(checkboxListModel);
		
		DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<String>();
		comboBoxModel.addAll(columnsList);
		this.getJComboBoxReferenceColumn().setModel(comboBoxModel);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.ui.AbstractTransformationParameterConfigurationPanel#getConfiguredParameters()
	 */
	@Override
	public Properties getConfiguredParameters() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.ui.AbstractTransformationParameterConfigurationPanel#setConfiguredParameters(de.enflexit.common.properties.Properties)
	 */
	@Override
	public void setConfiguredParameters(Properties parametersAsProperties) {
		// TODO Auto-generated method stub
	
	}
}
