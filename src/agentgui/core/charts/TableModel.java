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

import jade.util.leap.Iterator;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import agentgui.ontology.DataSeries;
import agentgui.ontology.ValuePair;

/**
 * Abstract superclass for the table representation of chart data.  
 * 
 * @author Nils
 */
public abstract class TableModel extends AbstractTableModel {


	private static final long serialVersionUID = -6719770378777635821L;
	/**
	 * The column titles
	 */
	protected Vector<String> columnTitles;
	/**
	 * The table data
	 */
	protected Vector<Vector<Object>> tableData;
	/**
	 * The old value of the last changed table cell
	 */
	protected Object latestChangedValue;
	/**
	 * The parent ChartDataModel this ChartTableModel is part of. 
	 */
	protected DataModel parent;

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		if(tableData.size() > 0){
			return tableData.get(0).size();
		}else{
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return tableData.size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return tableData.get(rowIndex).get(columnIndex);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
	 */
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		latestChangedValue = getValueAt(rowIndex, columnIndex);
		tableData.get(rowIndex).remove(columnIndex);
		tableData.get(rowIndex).add(columnIndex, aValue);
		fireTableCellUpdated(rowIndex, columnIndex);
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		return columnTitles.get(column);
	}
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		return true;
	}
	
	/**
	 * Removes a time series from the table model
	 * @param seriesIndex The index of the series to be removed
	 * @throws NoSuchSeriesException Will be thrown if there is no series with the specified index
	 */
	public void removeSeries(int seriesIndex) throws NoSuchSeriesException{
		int column2remove = seriesIndex+1;	// Column 0 contains the time stamps
		if(column2remove < this.getColumnCount()){
			columnTitles.remove(column2remove);
			java.util.Iterator<Vector<Object>> rows = tableData.iterator();
			while(rows.hasNext()){
				rows.next().remove(column2remove);
			}
		}else{
			throw new NoSuchSeriesException();
		}
		
		fireTableStructureChanged();
	}
	
	/**
	 * @return the latestChangedValue
	 */
	public Object getLatestChangedValue() {
		return latestChangedValue;
	}

	/**
	 * @param latestChangedValue the latestChangedValue to set
	 */
	public void setLatestChangedValue(Object latestChangedValue) {
		this.latestChangedValue = latestChangedValue;
	}
	
	/**
	 * Adds a new empty row to the table model
	 * @param key
	 */
	public void addEmptyRow(Number key){
		
		// Create a new row with the given time stamp and empty fields for all series
		Vector<Object> newRow = new Vector<Object>();
		newRow.add(key);
		for(int i=1; i<getColumnCount(); i++){
			newRow.add(null);
		}
		
		// Find the right position to insert the row
		int insertPos = 0;
		while(insertPos < getRowCount() && ((Number)getValueAt(insertPos, 0)).doubleValue() < key.doubleValue()){
			insertPos++;
		}
		
		// Add the new row
		tableData.add(insertPos, newRow);
		
		fireTableRowsInserted(getRowCount()-1, getRowCount()-1);
	}
	
	/**
	 * Returns the index of the row with the given key / x value, or -1 if the 
	 * time stamp does not exist in the table.
	 *
	 * @param key the key
	 * @return the row index by key
	 */
	public int getRowIndexByKey(Number key){
		for(int i=0;i<getRowCount();i++){
			if(((Number)tableData.get(i).get(0)).doubleValue() == key.doubleValue()){
				return i;
			}
		}
		return -1;
	}

	/**
	 * Add a new time series to the table model
	 * @param newSeries
	 */
	public void addSeries(DataSeries newSeries){
		
		columnTitles.add(newSeries.getLabel());
		
		Iterator valuePairs = parent.getValuePairsFromSeries(newSeries).iterator();
		 
		if(getRowCount() == 0){
			
			// tableData is empty, add a new Row for each value pair
			
			while(valuePairs.hasNext()){
				ValuePair vp = (ValuePair) valuePairs.next();
				
				Vector<Object> newRow = new Vector<Object>();
				newRow.add(parent.getKeyFromPair(vp));
				newRow.add(parent.getValueFromPair(vp));
				
				tableData.add(newRow);
			}
		}else{
			
			// tableData is not empty, integrate the new series
			
			int oldColCount = getColumnCount();
			
			while(valuePairs.hasNext()){
				ValuePair vp = (ValuePair) valuePairs.next();
				
				// Check if there is a row with this time stamp
				int rowIndex = getRowIndexByKey(parent.getKeyFromPair(vp));
				
				if(rowIndex >= 0){
					
					// There is one, append the new value
					tableData.get(rowIndex).add(parent.getValueFromPair(vp));
					
				}else{
					// There is not, add a new row
					Vector<Object> newRow = new Vector<Object>();
					
					// First column contains the time stamp
					newRow.add(parent.getKeyFromPair(vp));
					
					// There is no value for this time stamp in the old series
					while(newRow.size() < oldColCount){
						newRow.add(null);
					}
					// Append the new value at the end of the row
					newRow.add(parent.getValueFromPair(vp));
					
					// Find the right place to insert the new row:
					int insertAt = getRowCount();	// At the end of the table...
					for(int i=0;i<getRowCount();i++){
						double compareValue = ((Number) tableData.get(i).get(0)).doubleValue();
						if(compareValue > parent.getKeyFromPair(vp).doubleValue()){
							insertAt = i;			// ... or before the first one with a higher time stamp
						}
					}
					tableData.add(insertAt, newRow);
					
					
				}
				
			}
			
			// Append empty values to all rows for which the new series did not contain a value 
			for(int i=0; i < getRowCount(); i++){
				if(tableData.get(i).size() <= oldColCount){
					tableData.get(i).add(null);
				}
			}
			
		}
		
		fireTableStructureChanged();
	}
	
	/**
	 * Gets the value with the specified key / x value from the series with the specified index
	 * @param seriesIndex The index of the series to get the value from
	 * @param key The key / x value of the desired value
	 * @return The value for the given key / x value
	 * @throws NoSuchSeriesException Will be thrown if there is no series with the specified index
	 */
	public Float getValue(int seriesIndex, Number key) throws NoSuchSeriesException{
		if(seriesIndex < this.getColumnCount()-1){
			int rowIndex = getRowIndexByKey(key);
			return (Float) getValueAt(rowIndex, seriesIndex);
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	/**
	 * Sets the label for the first table column containing the keys / x values
	 * @param label The label
	 */
	public void setKeyColumnLabel(String label){
		columnTitles.remove(0);
		columnTitles.add(0, label);
		fireTableStructureChanged();
	}
	
	/**
	 * Sets the label for the table column containing the data series with the given index.
	 * @param seriesIndex The series index
	 * @param label The label
	 * @throws NoSuchSeriesException Thrown if there is no series with this index
	 */
	public void setSeriesLabel(int seriesIndex, String label) throws NoSuchSeriesException{
		if(seriesIndex < getColumnCount()-1 ){
			columnTitles.remove(seriesIndex+1);
			columnTitles.add(seriesIndex+1, label);
			fireTableStructureChanged();
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	/**
	 * Remove the table row with the given key / x value.
	 * @param key The key / x value
	 */
	public void removeRowByKey(Number key){
		int rowIndex = getRowIndexByKey(key);
		if(rowIndex >= 0){
			tableData.remove(rowIndex);
			fireTableRowsDeleted(rowIndex, rowIndex);
		}
	}
}
