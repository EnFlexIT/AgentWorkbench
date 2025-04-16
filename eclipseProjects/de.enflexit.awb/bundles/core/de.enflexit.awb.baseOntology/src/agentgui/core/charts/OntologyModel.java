package agentgui.core.charts;

import jade.util.leap.List;
import agentgui.ontology.Chart;
import agentgui.ontology.ChartSettingsGeneral;
import agentgui.ontology.DataSeries;

/**
 * The Class OntologyModel manages the translation between the ontology classes 
 * {@link Chart} and {@link DataModel} and the actual classes for the chart
 * representation.
 */
public abstract class OntologyModel {
	
	
	/**
	 * Gets the number of current data series.
	 * @return the series count
	 */
	public abstract int getSeriesCount();
	
	/**
	 * Gets the list of value pairs for the data series with the specified index.
	 * @param seriesIndex The series index
	 * @return The list of value pairs
	 * @throws NoSuchSeriesException Thrown if there is no series with the specified index
	 */
	public abstract List getSeriesData(int seriesIndex) throws NoSuchSeriesException;
	
	/**
	 * Adds a data series.
	 * @param series the series
	 */
	public abstract void addSeries(DataSeries series);

	/**
	 * Gets the data series.
	 *
	 * @param seriesIndex the series index
	 * @return the series
	 * @throws NoSuchSeriesException the no such series exception
	 */
	public abstract DataSeries getSeries(int seriesIndex) throws NoSuchSeriesException;
	
	/**
	 * Exchange data series.
	 *
	 * @param seriesIndex the series index
	 * @param dataSeries the data series
	 * @throws NoSuchSeriesException the no such series exception
	 */
	public abstract void exchangeSeries(int seriesIndex, DataSeries dataSeries) throws NoSuchSeriesException;
	
	/**
	 * Removes a data series.
	 *
	 * @param seriesIndex the series index
	 * @throws NoSuchSeriesException the no such series exception
	 */
	public abstract void removeSeries(int seriesIndex) throws NoSuchSeriesException;
	
	/**
	 * Gets the chart data.
	 * @return the chart data
	 */
	public abstract List getChartData();

	/**
	 * Gets the chart settings for this chart.
	 * @return the general chart settings
	 */
	public abstract ChartSettingsGeneral getChartSettings();
	
	
}
