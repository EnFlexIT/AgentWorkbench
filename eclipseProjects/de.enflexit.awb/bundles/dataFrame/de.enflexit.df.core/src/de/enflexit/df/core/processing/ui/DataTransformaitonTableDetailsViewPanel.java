package de.enflexit.df.core.processing.ui;

import javax.swing.JTabbedPane;

import de.enflexit.df.core.processing.transformationGraph.DataTableNode;

/**
 * This panel shows the actual table data, a chart representation  and some chart details.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataTransformaitonTableDetailsViewPanel extends JTabbedPane {

	private static final long serialVersionUID = 4093993807670949275L;
	
	private TableAndChartViewPanel tableAndChartPanel;
	private TableDetailsViewPanel detailsPanel;
	
	private DataTableNode tableNode;

	public DataTransformaitonTableDetailsViewPanel(DataTableNode tableNode) {
		this.tableNode = tableNode;
		this.initialize();
	}
	
	private void initialize() {
		this.addTab("Data View", this.getTableAndChartPanel());
		this.addTab("Table Info", this.getDetailsPanel());
	}
	
	public TableAndChartViewPanel getTableAndChartPanel() {
		if (tableAndChartPanel==null) {
			tableAndChartPanel = new TableAndChartViewPanel(this.tableNode);
		}
		return tableAndChartPanel;
	}
	
	private TableDetailsViewPanel getDetailsPanel() {
		if (detailsPanel==null) {
			detailsPanel = new TableDetailsViewPanel(tableNode.getDataTable());
			
		}
		return detailsPanel;
	}

}
