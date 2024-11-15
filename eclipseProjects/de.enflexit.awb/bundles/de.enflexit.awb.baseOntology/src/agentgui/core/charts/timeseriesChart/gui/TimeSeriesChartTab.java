package agentgui.core.charts.timeseriesChart.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.axis.DateAxis;
import org.jfree.data.time.TimeSeriesCollection;

import agentgui.core.charts.DataModel;
import agentgui.core.charts.gui.ChartTab;
import agentgui.core.charts.timeseriesChart.TimeSeriesDataModel;
import de.enflexit.common.GlobalRuntimeValues;
import de.enflexit.common.swing.TimeZoneDateFormat;

/**
 * Time series chart visualization component
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TimeSeriesChartTab extends ChartTab {

	private static final long serialVersionUID = -1998969136744482400L;
	
	/**
	 * Instantiates a new time series chart tab.
	 *
	 * @param model The data model for this time series
	 * @param parent the parent
	 */
	public TimeSeriesChartTab(TimeSeriesDataModel model, TimeSeriesChartEditorJPanel parent){
		
		super(ChartFactory.createTimeSeriesChart(
				model.getTimeSeriesOntologyModel().getChartSettings().getChartTitle(), 
				model.getTimeSeriesOntologyModel().getChartSettings().getXAxisLabel(), 
				model.getTimeSeriesOntologyModel().getChartSettings().getYAxisLabel(), 
				model.getTimeSeriesChartModel().getTimeSeriesCollection(), 
				true, false, false
		), parent);
		
		this.dataModel = model;
		this.applySettings();
		model.getChartModel().addObserver(this);
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.gui.ChartTab#replaceModel(agentgui.core.charts.DataModel)
	 */
	@Override
	public void replaceModel(DataModel newModel) {
		this.dataModel = newModel;
		
		TimeSeriesCollection tsc = ((TimeSeriesDataModel)this.dataModel).getTimeSeriesChartModel().getTimeSeriesCollection();
		this.chartPanel.getChart().getXYPlot().setDataset(tsc);
		dataModel.getChartModel().addObserver(this);
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.gui.ChartTab#applySettings()
	 */
	@Override
	protected void applySettings() {
		super.applySettings();
		DateAxis da = (DateAxis) this.chartPanel.getChart().getXYPlot().getDomainAxis();
		String formatString = ((TimeSeriesDataModel)dataModel).getTimeFormat();
		TimeZoneDateFormat tzdf = new TimeZoneDateFormat(formatString, GlobalRuntimeValues.getZoneId());
		da.setDateFormatOverride(tzdf);
	}
	
	/**
	 * Sets the time format for the time axis label ticks
	 * @param timeFormat
	 */
	void setTimeFormat(String timeFormat){
		TimeZoneDateFormat tzdf = new TimeZoneDateFormat(timeFormat, GlobalRuntimeValues.getZoneId());
		DateAxis da = (DateAxis) this.chartPanel.getChart().getXYPlot().getDomainAxis();
		da.setDateFormatOverride(tzdf);
	}
	
}