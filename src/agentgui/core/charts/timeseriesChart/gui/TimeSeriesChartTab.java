package agentgui.core.charts.timeseriesChart.gui;

import org.jfree.chart.ChartFactory;
import agentgui.core.charts.gui.ChartTab;
import agentgui.core.charts.timeseriesChart.TimeSeriesDataModel;

/**
 * Time series chart visualization component
 * @author Nils
 *
 */
public class TimeSeriesChartTab extends ChartTab {
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -1998969136744482400L;
	
	/**
	 * Constructor
	 * @param model The data model for this time series
	 */
	public TimeSeriesChartTab(TimeSeriesDataModel model){
		super(ChartFactory.createTimeSeriesChart(
				model.getTimeSeriesOntologyModel().getChartSettings().getChartTitle(), 
				model.getTimeSeriesOntologyModel().getChartSettings().getXAxisLabel(), 
				model.getTimeSeriesOntologyModel().getChartSettings().getYAxisLabel(), 
				model.getTimeSeriesChartModel(), 
				true, false, false
		));
		
		this.model = model;
		this.getChart().setBackgroundPaint(this.getBackground());
		
		setRenderer(DEFAULT_RENDERER);	// Use step renderer by default
		
		applyColorSettings();
		applyLineWidthsSettings();
	}
}
