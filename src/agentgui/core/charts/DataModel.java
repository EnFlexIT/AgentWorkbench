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

import java.awt.Color;
import java.util.Vector;

import jade.util.leap.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

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
	
	/** These colors will be used for newly added series */
	public static final Color[] DEFAULT_COLORS = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.YELLOW, Color.PINK, Color.CYAN, Color.MAGENTA, Color.LIGHT_GRAY, Color.DARK_GRAY, Color.BLACK};
	/** This line width will be used for newly added series */
	public static final float DEFAULT_LINE_WIDTH = 1.0f;
	
	/** The ontology representation of the series data */
	protected OntologyModel ontologyModel;
	/** The JFreeChart representation of the series data */
	protected ChartModel chartModel;
	/** The JTable representation of the series data */
	protected TableModel tableModel;
	
	/** The number of series in this data model	 */
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
	 * Adds a new series to the data model
	 * @param series The new series
	 */
	public void addSeries(DataSeries series){
		
		// Set the default label if none is specified in the series
		if(series.getLabel() == null || series.getLabel().length() == 0){
			series.setLabel(getDefaultSeriesLabel());
		}
		
		// Add the data to the sub models
		ontologyModel.addSeries(series);
		chartModel.addSeries(series);
		tableModel.addSeries(series);
		
		// Apply default settings
		ontologyModel.getChartSettings().addYAxisColors(""+DEFAULT_COLORS[getSeriesCount() % DEFAULT_COLORS.length].getRGB());
		ontologyModel.getChartSettings().addYAxisLineWidth(DEFAULT_LINE_WIDTH);
		
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
