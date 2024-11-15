package agentgui.core.charts;

import de.enflexit.common.Observable;

import org.jfree.data.general.Series;

import agentgui.ontology.ChartSettingsGeneral;
import agentgui.ontology.DataSeries;

public abstract class ChartModel extends Observable {
	
	/**
	 * Chart Model event types
	 * @author Nils Loose - DAWIS - ICB - University of Duisburg-Essen
	 *
	 */
	public enum EventType{
		SERIES_ADDED, SERIES_REMOVED, SERIES_RENAMED, SERIES_EXCHANGED
	}
	
	/**
	 * Returns the chart settings.
	 * @return the chart settings
	 */
	public abstract ChartSettingsGeneral getChartSettings();
	
	/**
	 * Returns the jfreeChart series specified by the index position.
	 * @param seriesIndex the series index
	 * @return the series
	 */
	public abstract Series getSeries(int seriesIndex);
	
	/**
	 * Returns the jfreeChart series with the specified label.
	 * @param seriesLabel The label
	 * @return The series
	 */
	public abstract Series getSeries(String seriesLabel);
	
	/**
	 * Adds the series.
	 * @param series the series
	 */
	public abstract void addSeries(DataSeries series);
	/**
	 * Removes the data series with the given index from the chart model
	 * @param seriesIndex The series index
	 */
	public abstract void removeSeries(int seriesIndex);
	/**
	 * Sets a new label for the series with the specified index
	 * @param seriesIndex The series index
	 * @param newLabel The new label
	 */
	public abstract void setSeriesLabel(int seriesIndex, String newLabel);

	/**
	 * Exchanges the series specified by the series index with the given DataSeries.
	 * @param seriesIndex the series index
	 * @param series the DataSeries
	 * @throws NoSuchSeriesException the no such series exception
	 */
	public abstract void exchangeSeries(int seriesIndex, DataSeries series) throws NoSuchSeriesException;
	
	
}
