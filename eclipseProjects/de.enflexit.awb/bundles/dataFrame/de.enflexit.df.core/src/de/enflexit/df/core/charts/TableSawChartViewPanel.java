package de.enflexit.df.core.charts;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;

import javax.swing.JPanel;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import javax.swing.JButton;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.swing.ChartPanel;
import org.jfree.data.xy.XYSeriesCollection;

import de.enflexit.common.swing.AwbThemeColor;
import de.enflexit.common.swing.OwnerDetection;
import de.enflexit.df.core.processing.ui.DataColumnSelectionWindow;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Font;

/**
 * Swing panel to show a chart representation of TableSaw data.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TableSawChartViewPanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 6403889699630701143L;
	
	private ChartPanel chartPanel;
	private JFreeChart chart;
	
	private Table table;
	private Column<?> xAxisColumn;
	private String chartTitle;
	private JPanel buttonPanel;
	private JButton jButtonSelectSeries;
	
	private XYSeriesCollection<String> seriesCollection;
	private XYPlot<String> xyPlot;
	
	
	
	/**
	 * Instantiates a new table saw chart view panel.
	 */
	public TableSawChartViewPanel() {
		initialize();
	}
	
	/**
	 * Instantiates a new table saw chart view panel.
	 * @param table the table
	 * @param chartTitle the chart title
	 */
	public TableSawChartViewPanel(Table table, Column<?> xAxisColumn, String chartTitle) {
		super();
		this.table = table;
		this.xAxisColumn = xAxisColumn;
		this.chartTitle = chartTitle;
		this.initialize();
	}

	/**
	 * Initializes the UI elements.
	 */
	private void initialize() {
		setLayout(new BorderLayout(0, 0));
		add(getChartPanel(), BorderLayout.CENTER);
		add(getButtonPanel(), BorderLayout.SOUTH);
	}
	
	/**
	 * Gets the chart.
	 * @return the chart
	 */
	private JFreeChart getChart() {
		if (chart==null) {
			chart = ChartFactory.createXYLineChart(chartTitle, xAxisColumn.name(), "Values", this.getSeriesCollection());
		}
		return chart;
	}

	/**
	 * Gets the chart panel.
	 * @return the chart panel
	 */
	private ChartPanel getChartPanel() {
		if (chartPanel == null) {
			if (this.table != null) {
				
				this.getChart().setBackgroundPaint(AwbThemeColor.Chart_Background.getColor());
				this.getChart().setBorderPaint(AwbThemeColor.Chart_GridLine.getColor());
				
				this.getChart().getLegend().setBackgroundPaint(AwbThemeColor.Chart_Background.getColor());
				this.getChart().getLegend().setItemPaint(AwbThemeColor.Chart_GridLine.getColor());
				
				this.getXyPlot().setBackgroundPaint(AwbThemeColor.Chart_Background.getColor());  
				this.getXyPlot().setDomainGridlinePaint(AwbThemeColor.Chart_GridLine.getColor());
				this.getXyPlot().setRangeGridlinePaint(AwbThemeColor.Chart_GridLine.getColor());

				if (TableSawChartHelper.isTemporalColumn(this.xAxisColumn)) {
					DateAxis axis = new DateAxis(xAxisColumn.name());
					axis.setDateFormatOverride(new SimpleDateFormat("dd.MM.yyyy HH:mm"));
					this.getXyPlot().setDomainAxis(axis);
					XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, false);
					this.getXyPlot().setRenderer(renderer);
				}

				this.applyColorAndLineWidthSettings();
				chartPanel = new ChartPanel(this.getChart());
			}
		}
		return chartPanel;
	}
	
	/**
	 * Gets the series collection.
	 * @return the series collection
	 */
	private XYSeriesCollection<String> getSeriesCollection() {
		if (seriesCollection==null) {
			seriesCollection = TableSawChartHelper.createJFreeChartSeriesCollection(this.table);
		}
		return seriesCollection;
	}
	
	/**
	 * Gets the xy plot.
	 * @return the xy plot
	 */
	@SuppressWarnings("unchecked")
	private XYPlot<String> getXyPlot(){
		if (xyPlot==null) {
			if (this.getChart().getPlot() instanceof XYPlot<?>) {
				xyPlot = (XYPlot<String>) this.getChart().getPlot();
			} else {
				throw new IllegalArgumentException("Unexpected plot type: " + this.getChart().getPlot().getClass().getName());
			}
		}
		return xyPlot;
	}
	
	/**
	 * Applies the color and the line width settings from class {@link GlobalInfo}.
	 */
	private void applyColorAndLineWidthSettings(){
		int colorCounter = 0;
		XYSeriesCollection<String> tsc = this.getSeriesCollection();
		XYItemRenderer renderer = this.getXyPlot().getRenderer();
		for (int iTS = 0; iTS < tsc.getSeriesCount(); iTS++) {
			renderer.setSeriesPaint(iTS, TableSawChartHelper.getChartColor(colorCounter));
			renderer.setSeriesStroke(iTS, new BasicStroke(TableSawChartHelper.getDefaultChartLineWidth()));
			colorCounter++;
		}
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource()==this.getJButtonSelectSeries()) {
			DataColumnSelectionWindow columnSelection = new DataColumnSelectionWindow(OwnerDetection.getOwnerWindowForComponent(this.getJButtonSelectSeries()), this);
			int posX = (int) getJButtonSelectSeries().getLocationOnScreen().getX() + getJButtonSelectSeries().getWidth()  - columnSelection.getWidth();
			int posY = (int) getJButtonSelectSeries().getLocationOnScreen().getY() + getJButtonSelectSeries().getHeight() - columnSelection.getHeight(); 
			columnSelection.setLocation(posX, posY);
		}
	}
	
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			GridBagLayout gbl_buttonPanel = new GridBagLayout();
			gbl_buttonPanel.columnWidths = new int[]{0, 0};
			gbl_buttonPanel.rowHeights = new int[]{0, 0};
			gbl_buttonPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_buttonPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			buttonPanel.setLayout(gbl_buttonPanel);
			GridBagConstraints gbc_jButtonSelectSeries = new GridBagConstraints();
			gbc_jButtonSelectSeries.anchor = GridBagConstraints.EAST;
			gbc_jButtonSelectSeries.gridx = 0;
			gbc_jButtonSelectSeries.gridy = 0;
			buttonPanel.add(getJButtonSelectSeries(), gbc_jButtonSelectSeries);
		}
		return buttonPanel;
	}
	private JButton getJButtonSelectSeries() {
		if (jButtonSelectSeries == null) {
			jButtonSelectSeries = new JButton("Select Series");
			jButtonSelectSeries.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonSelectSeries.addActionListener(this);
		}
		return jButtonSelectSeries;
	}
	
	/**
	 * Sets the visibility for the series with the provided name.
	 * @param seriesName the series name
	 * @param visible the visible
	 */
	public void setSeriesVisible(String seriesName, boolean visible) {
		
		@SuppressWarnings("unchecked")
		XYPlot<String> plot = (XYPlot<String>) this.getChart().getPlot();
		XYSeriesCollection<String> dataset = (XYSeriesCollection<String>) plot.getDataset();
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();

		for (int i = 0; i < dataset.getSeriesCount(); i++) {
			if (dataset.getSeriesKey(i).equals(seriesName)) {
				renderer.setSeriesVisible(i, visible);
				return;
			}
		}
	}
	
	/**
	 * Gets the table.
	 * @return the table
	 */
	public Table getTable() {
		return table;
	}
}
