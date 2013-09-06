/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.core.charts;

import jade.util.leap.List;

import java.awt.Color;
import java.util.Vector;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import agentgui.core.charts.timeseriesChart.TimeSeriesChartSettingModel;
import agentgui.core.charts.timeseriesChart.TimeSeriesOntologyModel;
import agentgui.ontology.DataSeries;
import agentgui.ontology.ValuePair;

/**
 * This class manages the data models for different chart visualizations. 
 * This is an abstract super class that provides general functionality. As
 * different chart types might require different data types and functionality
 * there must be chart type specific implementation for every type of chart.
 *  
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 */
public abstract class DataModel implements TableModelListener {
	
	/** These colors will be used for newly added series. */
	public static final Color[] DEFAULT_COLORS = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.YELLOW, Color.PINK, Color.CYAN, Color.MAGENTA, Color.LIGHT_GRAY, Color.DARK_GRAY, Color.BLACK};
	/** This line width will be used for newly added series. */
	public static final float DEFAULT_LINE_WIDTH = 1.0f;
	
	/** The ontology representation of the series data. */
	protected OntologyModel ontologyModel;
	/** The JTable representation of the series data. */
	protected TableModel tableModel;
	/** The JFreeChart representation of the series data. */
	protected ChartModel chartModel;
	/** The model for the chart settings */
	protected ChartSettingModel chartSettingModel;
	
	/** The number of series in this data model. */
	protected int seriesCount = 0;
	
	
	/**
	 * Creates a new data series of the correct type for the precise type of chart.
	 *
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
	 * Updates the key of the value pair.
	 *
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
	
	/**
	 * Gets the default series label.
	 * @return the default series label
	 */
	public abstract String getDefaultSeriesLabel();
	
	
	/**
	 * Edits the data series by adding data.
	 *
	 * @param series the series
	 * @param targetDataSeriesIndex the target data series index
	 * @param editOntology true, if the ontology has to be edited
	 */
	public abstract void editDataSeriesAddData(DataSeries series, int targetDataSeriesIndex, boolean editOntology) throws NoSuchSeriesException;
	/**
	 * Edits the data series by adding or exchanging data.
	 *
	 * @param series the series
	 * @param targetDataSeriesIndex the target data series index
	 * @param editOntology true, if the ontology has to be edited
	 */
	public abstract void editDataSeriesAddOrExchangeData(DataSeries series, int targetDataSeriesIndex, boolean editOntology) throws NoSuchSeriesException;
	/**
	 * Edits the data series by exchanging data.
	 *
	 * @param series the series
	 * @param targetDataSeriesIndex the target data series index
	 * @param editOntology true, if the ontology has to be edited
	 */
	public abstract void editDataSeriesExchangeData(DataSeries series, int targetDataSeriesIndex, boolean editOntology) throws NoSuchSeriesException;
	
	/**
	 * Edits the data series by remove data.
	 *
	 * @param series the series
	 * @param targetDataSeriesIndex the target data series index
	 * @param editOntology true, if the ontology has to be edited
	 * @throws NoSuchSeriesException the no such series exception
	 */
	public abstract void editDataSeriesRemoveData(DataSeries series, int targetDataSeriesIndex, boolean editOntology) throws NoSuchSeriesException;
	
	
	/**
	 * Returns the ontology model.
	 * @return the ontologyModel
	 */
	public OntologyModel getOntologyModel() {
		return ontologyModel;
	}
	/**
	 * Sets the ontology model.
	 * @param ontologyModel the ontologyModel to set
	 */
	public void setOntologyModel(OntologyModel ontologyModel) {
		this.ontologyModel = ontologyModel;
	}
	
	/**
	 * Gets the table model.
	 * @return the tableModel
	 */
	public TableModel getTableModel() {
		return tableModel;
	}
	/**
	 * Sets the table model.
	 * @param tableModel the tableModel to set
	 */
	public void setTableModel(TableModel tableModel) {
		this.tableModel = tableModel;
	}
	
	/**
	 * Gets the chart model.
	 * @return the chartModel
	 */
	public ChartModel getChartModel() {
		return chartModel;
	}
	/**
	 * Sets the chart model.
	 * @param chartModel the chartModel to set
	 */
	public void setChartModel(ChartModel chartModel) {
		this.chartModel = chartModel;
	}

	/**
	 * Returns the chart setting model.
	 * @return the chart setting model
	 */
	public ChartSettingModel getChartSettingModel() {
		if (chartSettingModel==null) {
			if (this.ontologyModel instanceof TimeSeriesOntologyModel) {
				chartSettingModel = new TimeSeriesChartSettingModel(this);
			} else {
				chartSettingModel = new ChartSettingModel(this);	
			}
		}
		return chartSettingModel;
	}
	/**
	 * Sets the chart setting model.
	 * @param chartSettingModel the new chart setting model
	 */
	public void setChartSettingModel(ChartSettingModel chartSettingModel) {
		this.chartSettingModel = chartSettingModel;
	}
	
	/**
	 * Gets the series count.
	 * @return the seriesCount
	 */
	public int getSeriesCount() {
		return seriesCount;
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	@Override
	public void tableChanged(TableModelEvent tme) {
		
		if(tme.getSource()==tableModel && tme.getFirstRow()>=0){
			
			if(tme.getType()==TableModelEvent.UPDATE){
				// --- Update Events in the table ---------
				if(tme.getColumn() > 0){
					// --- A single value was edited ------------------------------------
					int seriesIndex = tme.getColumn()-1; // First column contains the time stamps.
					int rowIndex = tme.getFirstRow();
					
					Number key = (Number) tableModel.getValueAt(rowIndex, 0);
					Number value = (Number) tableModel.getValueAt(rowIndex, tme.getColumn());
					Vector<Object> rowVector = tableModel.getRow(rowIndex);
					
					try {
						if(value!=null){
							// --- Update new entry in chart and ontology model ---------
							chartModel.addOrUpdateValuePair(seriesIndex, key, value);
							ontologyModel.addOrUpdateValuePair(seriesIndex, key, value);
						
						} else {
							if (tableModel.isEmptyTableModelRow(rowVector)==false) {
								// --- Rewrite the data row -----------------------------
								for (int i=1; i < rowVector.size(); i++) {
									Number seriesValue = (Number) rowVector.get(i);
									int series = i-1;
									if (seriesValue!=null) {
										chartModel.addOrUpdateValuePair(series, key, seriesValue);
										ontologyModel.addOrUpdateValuePair(series, key, seriesValue);
									} else {
										chartModel.removeValuePair(series, key);
										ontologyModel.removeValuePair(series, key);
									}
								} // end for
								
							} else {
								// --- Empty row, delete ValuePair ----------------------
								chartModel.removeValuePair(seriesIndex, key);
								ontologyModel.removeValuePair(seriesIndex, key);
							}
						}
						
					} catch (NoSuchSeriesException e) {
						System.err.println("Error updating data model: Series "+seriesIndex+" does mot exist!");
						e.printStackTrace();
					}
				
				} else if(tme.getColumn() == 0) {
					// --- The key value (e.g. the time stamp) was edited ---------------
					Number oldKey = (Number) tableModel.getLatestChangedValue();
					Number newKey = (Number) tableModel.getValueAt(tme.getFirstRow(), 0);
					
					ontologyModel.updateKey(oldKey, newKey);
					chartModel.updateKey(oldKey, newKey);
					
				}
				
			} else {
				// --- Insert or Delete events in the table ---------
			}
		}

	}
	
	/**
	 * Adds a new series to the data model.
	 *
	 * @param series The new series
	 */
	public void addSeries(DataSeries series){
		
		// Set the default label if none is specified in the series
		if(series.getLabel() == null || series.getLabel().length() == 0){
			series.setLabel(getDefaultSeriesLabel());
		}
		
		// Add the data to the sub models
		ontologyModel.addSeries(series);
		// Apply default settings
		ontologyModel.getChartSettings().addYAxisColors(""+DEFAULT_COLORS[getSeriesCount() % DEFAULT_COLORS.length].getRGB());
		ontologyModel.getChartSettings().addYAxisLineWidth(DEFAULT_LINE_WIDTH);
		
		chartModel.addSeries(series);
		tableModel.addSeries(series);
		this.getChartSettingModel().refresh();
		
		seriesCount++;

	}
	
	/**
	 * Adds or exchanges a data series from the current chart.
	 *
	 * @param series the series
	 * @param targetDataSeriesIndex the target data series index
	 */
	public void addOrExchangeSeries(DataSeries series, int targetDataSeriesIndex) throws NoSuchSeriesException {

		if (targetDataSeriesIndex<=(this.getSeriesCount()-1)) {
			// --- Exchange DataSeries --------------------
			this.exchangeSeries(series, targetDataSeriesIndex);
		} else {
			// --- Add this series as new series ----------
			this.addSeries(series);
		}
	}
	
	/**
	 * Adds or exchanges a data series from the current chart. If the
	 * specified series does not exists, nothing will be done.
	 *
	 * @param series the series
	 * @param seriesIndex the target data series index
	 */
	public void exchangeSeries(DataSeries series, int seriesIndex) throws NoSuchSeriesException {
		if (seriesIndex<=(this.getSeriesCount()-1)) {
			ontologyModel.exchangeSeries(seriesIndex, series);
			chartModel.exchangeSeries(seriesIndex, series);
			tableModel.exchangeSeries(seriesIndex, series);
			this.getChartSettingModel().refresh();
		} else {
			throw new NoSuchSeriesException();
		}
	}
	
	/**
	 * Removes a series from the data model.
	 *
	 * @param seriesIndex The index of the series to be removed
	 * @throws NoSuchSeriesException Thrown if there is no series with that index
	 */
	public void removeSeries(int seriesIndex) throws NoSuchSeriesException{
		ontologyModel.removeSeries(seriesIndex);
		chartModel.removeSeries(seriesIndex);
		tableModel.removeSeries(seriesIndex);
		this.getChartSettingModel().refresh();
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

			} catch (NoSuchSeriesException e) {
				System.err.println("Trying to remove value pair from non-existant series "+i);
				e.printStackTrace();
			}
		}
		
		tableModel.removeRowByKey(key);
	}
	
}
