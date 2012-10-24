package agentgui.core.charts;

import java.awt.Color;

import jade.util.leap.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import agentgui.ontology.DataSeries;
import agentgui.ontology.ValuePair;

public abstract class DataModel implements TableModelListener {
	
	/**
	 * These colors will be used for newly added series
	 */
	public static final Color[] DEFAULT_COLORS = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.CYAN, Color.YELLOW};
	/**
	 * This line width will be used for newly added series
	 */
	public static final float DEFAULT_LINE_WIDTH = 1.0f;
	
	/**
	 * The ontology representation of the series data
	 */
	protected OntologyModel ontologyModel;
	/**
	 * The JFreeChart representation of the series data
	 */
	protected ChartModel chartModel;
	/**
	 * The JTable representation of the series data
	 */
	protected TableModel tableModel;
	/**
	 * Contains the settings for this chart
	 */
	protected ChartSettings chartSettings;
	/**
	 * The number of series in this data model
	 */
	protected int seriesCount = 0;
	
	/**
	 * Creates a new data series of the correct type for the precise type of chart 
	 * @param label The new data series label
	 * @return The new data series
	 */
	public abstract DataSeries createNewDataSeries(String label);
	/**
	 * Creates a new value pair for the specific type of chart.
	 * Allows value pair creation in super class methods without knowing the exact class of the value pair. 
	 * @param key The key / x value
	 * @param value The (y) value
	 * @return The value pair
	 */
	public abstract ValuePair createNewValuePair(Number key, Number value);
	
	/**
	 * Gets the key / x value from the value pair.
	 * Allows accessing the key / x value from superclass methods without knowing the exact class of value pair and key / x value.
	 * @param vp The value pair
	 * @return The key / x value
	 */
	public abstract Number getKeyFromPair(ValuePair vp);
	
	/**
	 * Gets the (y) value from the value pair.
	 * Allows accessing the (y) value from superclass methods without knowing the exact class of value pair and (y) value.
	 * @param vp The value pair
	 * @return The (y) value
	 */
	public abstract Number getValueFromPair(ValuePair vp);
	/**
	 * Updates the key of the value pair
	 * @param key The new key
	 * @param vp The value pair to be updated
	 */
	public abstract void setKeyForPair(Number key, ValuePair vp);
	/**
	 * Updates the value of the key value pair.
	 * @param value The new value
	 * @param vp The value pair to be updated
	 */
	public abstract void setValueForPair(Number value, ValuePair vp);
	
	/**
	 * Gets a list containing all value pairs from the given DataSeries.
	 * Allows accessing the value pairs from superclass methods without knowing the precise class of the series.  
	 * @param series The DataSeries
	 * @return The List of ValuePairs
	 */
	public abstract List getValuePairsFromSeries(DataSeries series);
	
	public abstract String getDefaultSeriesLabel();
	
	/**
	 * @return the ontologyModel
	 */
	public OntologyModel getOntologyModel() {
		return ontologyModel;
	}

	/**
	 * @param ontologyModel the ontologyModel to set
	 */
	public void setOntologyModel(OntologyModel ontologyModel) {
		this.ontologyModel = ontologyModel;
	}

	/**
	 * @return the chartModel
	 */
	public ChartModel getChartModel() {
		return chartModel;
	}

	/**
	 * @param chartModel the chartModel to set
	 */
	public void setChartModel(ChartModel chartModel) {
		this.chartModel = chartModel;
	}

	/**
	 * @return the tableModel
	 */
	public TableModel getTableModel() {
		return tableModel;
	}

	/**
	 * @param tableModel the tableModel to set
	 */
	public void setTableModel(TableModel tableModel) {
		this.tableModel = tableModel;
	}

	/**
	 * @return the chartSettings
	 */
	public ChartSettings getChartSettings() {
		return chartSettings;
	}

	/**
	 * @return the seriesCount
	 */
	public int getSeriesCount() {
		return seriesCount;
	}

	@Override
	public void tableChanged(TableModelEvent tme) {
		if(tme.getSource() == tableModel && tme.getFirstRow() >= 0){
			
			if(tme.getType() == 0){
			
				if(tme.getColumn() > 0){
					
					// A single value was edited
					int seriesIndex = tme.getColumn()-1; // First column contains the time stamps.
					Number key = (Number) tableModel.getValueAt(tme.getFirstRow(), 0);
					Number value = (Number) tableModel.getValueAt(tme.getFirstRow(), tme.getColumn());
					
					try {
						if(value != null){
							chartModel.addOrUpdateValuePair(seriesIndex, key, value);
							ontologyModel.addOrUpdateValuePair(seriesIndex, key, value);
						}else{
							chartModel.removeValuePair(seriesIndex, key);
							ontologyModel.removeValuePair(seriesIndex, key);
						}
					} catch (NoSuchSeriesException e) {
						System.err.println("Error updating data model: Series "+seriesIndex+" does mot exist!");
						e.printStackTrace();
					}
				}else{
					
					// The time stamp was edited
					
					Number oldKey = (Number) tableModel.getLatestChangedValue();
					Number newKey = (Number) tableModel.getValueAt(tme.getFirstRow(), 0);
					
					ontologyModel.updateKey(oldKey, newKey);
					chartModel.updateKey(oldKey, newKey);
				}
			}
		}

	}
	
	/**
	 * Adds a new series to the data model
	 * @param series The new series
	 */
	public void addSeries(DataSeries series){
		
		// Set the default label if none is specified in the series
		if(series.getLabel() == null || series.getLabel().length() == 0){
			series.setLabel(getDefaultSeriesLabel()+" "+(getSeriesCount()+1));
		}
		
		// Add the data to the sub models
		ontologyModel.addSeries(series);
		chartModel.addSeries(series);
		tableModel.addSeries(series);
		
		// Apply default settings
		ontologyModel.getChartSettings().addYAxisColors(""+DEFAULT_COLORS[getSeriesCount() % DEFAULT_COLORS.length].getRGB());
		ontologyModel.getChartSettings().addYAxisLineWidth(DEFAULT_LINE_WIDTH);
		
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
	 * @param key The time stamp
	 */
	public void removeValuePairsFromAllSeries(Number key){
		for(int i=0; i<getSeriesCount(); i++){
			try {
				ontologyModel.removeValuePair(i, key);
				chartModel.removeValuePair(i, key);
				tableModel.removeRowByKey(key);
			} catch (NoSuchSeriesException e) {
				System.err.println("Trying to remove value pair from non-existant series "+i);
				e.printStackTrace();
			}
		}
	}
	
}
