package agentgui.core.charts.timeseriesChart;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import jade.util.leap.Iterator;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import agentgui.core.charts.NoSuchSeriesException;
import agentgui.core.charts.SeriesSettings;
import agentgui.core.charts.timeseriesChart.gui.TimeSeriesChartTab;
import agentgui.ontology.TimeSeries;
import agentgui.ontology.TimeSeriesChart;

/**
 * Container class managing the data models for the different time series representations.
 * @author Nils
 *
 */
public class TimeSeriesDataModel implements TableModelListener{
	/**
	 * These colors will be used for newly added series
	 */
	public static final Color[] DEFAULT_COLORS = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.CYAN, Color.YELLOW};
	/**
	 * This line width will be used for newly added series
	 */
	public static final float DEFAULT_LINE_WIDTH = 1.0f;
	/**
	 * This label with appended seriesCount+1 will be used for newly added series
	 */
	public static final String DEFAULT_SERIES_LABEL = "Time Series";
	/**
	 * This title will be used for the chart if none is specified
	 */
	public static final String DEFAULT_CHART_TITLE = "Time Series Chart";
	/**
	 * This x axis label will be used for the chart if none is specified
	 */
	public static final String DEFAULT_X_AXIS_LABEL = "Time";
	/**
	 * This y axis label will be used for the chart if none is specified
	 */
	public static final String DEFAULT_Y_AXIS_LABEL = "Value";
	/**
	 * This will be the basic date for relative time values
	 */
	private Date startDate;
	/**
	 * The ontology representation of the series data
	 */
	private TimeSeriesOntologyModel ontologyModel;
	/**
	 * The JFreeChart representation of the series data
	 */
	private TimeSeriesChartModel chartModel;
	/**
	 * The JTable representation of the series data
	 */
	private TimeSeriesTableModel tableModel;
	/**
	 * Contains the settings for this chart
	 */
	private TimeSeriesChartSettings chartSettings;
	/**
	 * The number of series in this data model
	 */
	private int seriesCount = 0;
	
	/**
	 * Constructor
	 * @param timeSeriesChart The ontology representation of the series to be displayed
	 */
	public TimeSeriesDataModel(TimeSeriesChart timeSeriesChart){
		
		// Initialize the start date
		Calendar cal = Calendar.getInstance();
		cal.set(1970, 0, 1, 0, 0, 0);
		this.startDate = cal.getTime();
		
		// Initialize the three sub models
		this.ontologyModel = new TimeSeriesOntologyModel(timeSeriesChart);
		chartModel = new TimeSeriesChartModel();
		tableModel = new TimeSeriesTableModel();
		
		if(this.ontologyModel.getTimeSeriesChart().isEmpty()){
			
			// The ontology model is empty. Do some initialization work.
			
			// Remove the empty elements created by DynForm from the lists 
			this.ontologyModel.getTimeSeriesChart().getTimeSeriesChartData().clear();
			this.ontologyModel.getGeneralSettings().getYAxisColors().clear();
			this.ontologyModel.getGeneralSettings().getYAxisLineWidth().clear();
			
			// Set some default values
			this.ontologyModel.getGeneralSettings().setChartTitle(DEFAULT_CHART_TITLE);
			this.ontologyModel.getGeneralSettings().setXAxisLabel(DEFAULT_X_AXIS_LABEL);
			this.ontologyModel.getGeneralSettings().setYAxisLabel(DEFAULT_Y_AXIS_LABEL);
			this.ontologyModel.getGeneralSettings().setRendererType(TimeSeriesChartTab.DEFAULT_RENDERER);
			
		}else{
			
			// The ontology model contains data, add it to the other sub models 
			Iterator dataSeries = this.ontologyModel.getTimeSeriesChart().getAllTimeSeriesChartData();
			while(dataSeries.hasNext()){
				TimeSeries nextSeries = (TimeSeries) dataSeries.next();
				
				if(seriesCount > this.ontologyModel.getGeneralSettings().getYAxisColors().size()){
					this.ontologyModel.getGeneralSettings().addYAxisColors(""+DEFAULT_COLORS[seriesCount % DEFAULT_COLORS.length].getRGB());
				}
				if(seriesCount > this.ontologyModel.getGeneralSettings().getYAxisLineWidth().size()){
					this.ontologyModel.getGeneralSettings().addYAxisLineWidth(DEFAULT_LINE_WIDTH);
				}
				chartModel.addSeries(nextSeries);
				tableModel.addSeries(nextSeries);
				seriesCount++;
			}
		}
		
		// Register for table model events
		tableModel.addTableModelListener(this);
		
		this.chartSettings = new TimeSeriesChartSettings(timeSeriesChart);
	}

	/**
	 * @return the ontologyModel
	 */
	public TimeSeriesOntologyModel getOntologyModel() {
		return ontologyModel;
	}

	/**
	 * @param ontologyModel the ontologyModel to set
	 */
	public void setOntologyModel(TimeSeriesOntologyModel ontologyModel) {
		this.ontologyModel = ontologyModel;
	}

	/**
	 * @return the chartModel
	 */
	public TimeSeriesChartModel getChartModel() {
		return chartModel;
	}

	/**
	 * @param chartModel the chartModel to set
	 */
	public void setChartModel(TimeSeriesChartModel chartModel) {
		this.chartModel = chartModel;
	}

	/**
	 * @return the tableModel
	 */
	public TimeSeriesTableModel getTableModel() {
		return tableModel;
	}

	/**
	 * @param tableModel the tableModel to set
	 */
	public void setTableModel(TimeSeriesTableModel tableModel) {
		this.tableModel = tableModel;
	}

	public TimeSeriesChartSettings getChartSettings() {
		return chartSettings;
	}

	public void setChartSettings(TimeSeriesChartSettings chartSettings) {
		this.chartSettings = chartSettings;
	}

	@Override
	public void tableChanged(TableModelEvent tme) {
		System.out.println("TableModelEvent type "+tme.getType()+", Row "+tme.getFirstRow()+", Col "+tme.getColumn());
		if(tme.getSource() == tableModel && tme.getFirstRow() >= 0){
			
			if(tme.getType() == 0){
			
				if(tme.getColumn() > 0){
					
					// A single value was edited
					int seriesIndex = tme.getColumn()-1; // First column contains the time stamps.
					long timeStamp = (Long) tableModel.getValueAt(tme.getFirstRow(), 0);
					Float value = (Float) tableModel.getValueAt(tme.getFirstRow(), tme.getColumn());
					
					try {
						if(value != null){
							chartModel.addOrUpdateValuePair(seriesIndex, timeStamp, value);
							ontologyModel.addOrUpdateValuePair(seriesIndex, timeStamp, value);
						}else{
							chartModel.removeValuePair(seriesIndex, timeStamp);
							ontologyModel.removeValuePair(seriesIndex, timeStamp);
						}
					} catch (NoSuchSeriesException e) {
						System.err.println("Error updating data model: Series "+seriesIndex+" does mot exist!");
						e.printStackTrace();
					}
				}else{
					
					// The time stamp was edited
					
					long oldTimestamp = (Long) tableModel.getLatestChangedValue();
					long newTimeStamp = (Long) tableModel.getValueAt(tme.getFirstRow(), 0);
					
					ontologyModel.updateTimeStamp(oldTimestamp, newTimeStamp);
					chartModel.updateTimeStamp(oldTimestamp, newTimeStamp);
				}
			}
		}
	}
	
	/**
	 * Adds a new series to the data model
	 * @param series The new series
	 */
	public void addSeries(TimeSeries series){
		
		// Set the default label if none is specified in the series
		if(series.getLabel() == null || series.getLabel().length() == 0){
			series.setLabel(DEFAULT_SERIES_LABEL+" "+(getSeriesCount()+1));
		}
		
		// Add the data to the sub models
		ontologyModel.addSeries(series);
		chartModel.addSeries(series);
		tableModel.addSeries(series);
		
		// Apply default settings
		ontologyModel.getGeneralSettings().addYAxisColors(""+DEFAULT_COLORS[getSeriesCount() % DEFAULT_COLORS.length].getRGB());
		ontologyModel.getGeneralSettings().addYAxisLineWidth(DEFAULT_LINE_WIDTH);
		
		SeriesSettings settings = new SeriesSettings(series.getLabel(), DEFAULT_COLORS[getSeriesCount() % DEFAULT_COLORS.length], DEFAULT_LINE_WIDTH);
		chartSettings.addSeriesSettings(settings);
		
		seriesCount++;

	}
	
	/**
	 * Removes a series from the data model
	 * @param seriesIndex The index of the series to be removed
	 * @throws NoSuchSeriesException Thrown if there is no series with that index
	 */
	public void removeSeries(int seriesIndex) throws NoSuchSeriesException{
		ontologyModel.removeSeries(seriesIndex);
		chartModel.removeSeries(seriesIndex);
		tableModel.removeSeries(seriesIndex);
		
		chartSettings.removeSeriesSettings(seriesIndex);
		
		seriesCount--;
	}
	
	
	/**
	 * Removes the value pair with the given time stamp from every series that contains one. 
	 * @param timestamp The time stamp
	 */
	public void removeValuePairsFromAllSeries(long timestamp){
		for(int i=0; i<getSeriesCount(); i++){
			try {
				ontologyModel.removeValuePair(i, timestamp);
				chartModel.removeValuePair(i, timestamp);
				tableModel.removeRowByTimestamp(timestamp);
			} catch (NoSuchSeriesException e) {
				System.err.println("Trying to remove value pair from non-existant series "+i);
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @return The number of series in this data model.
	 */
	public int getSeriesCount() {
		return seriesCount;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	
}
