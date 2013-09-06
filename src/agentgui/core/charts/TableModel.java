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

import java.util.HashMap;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import agentgui.ontology.DataSeries;
import agentgui.ontology.TimeSeriesValuePair;
import agentgui.ontology.ValuePair;

/**
 * Abstract superclass for the table representation of chart data.  
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 */
public abstract class TableModel extends AbstractTableModel {

	private static final long serialVersionUID = -6719770378777635821L;
	
	/** The column titles */
	protected Vector<String> columnTitles;
	/** The table data */
	protected Vector<Vector<Object>> tableData;
	/** The old value of the last changed table cell */
	protected Object latestChangedValue;
	/** The parent ChartDataModel this ChartTableModel is part of. */
	protected DataModel parentDataModel;

	
	/**
	 * Initialises the local table model and (re)sets the local values 
	 * for {@link TableModel#columnTitles} and {@link TableModel#tableData}, .
	 */
	public abstract void initilizeTabelModel();
	
	
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
		
		if (this.getColumnCount()==2) {
			// --- The last series has to be removed ------
			this.initilizeTabelModel();
			
		} else {
			int column2remove = seriesIndex+1;	// Column 0 contains the time stamps
			if(column2remove <= this.getColumnCount()){
				columnTitles.remove(column2remove);
				java.util.Iterator<Vector<Object>> rows = tableData.iterator();
				while(rows.hasNext()){
					rows.next().remove(column2remove);
				}
			}
//			else{
//				throw new NoSuchSeriesException();
//			}
			
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
	 * Adds a new empty row to the table model.
	 * @param jTable the current JTable
	 */
	public void addEmptyRow(JTable jTable){
		
		// --- Get current position in the table model ----
		Vector<Object> rowSelected = null;
		Vector<Object> newRow = null;
		Number currKey = null;
		Number newKey = null;
		Number newValue = 0f;
		
		int tableRowSelected = jTable.getSelectedRow();
		if (tableRowSelected==-1) {
			tableRowSelected = jTable.getRowCount()-1;
		}
		
		
		if (tableData.size()==0) {
			// --- Create new data series -------
			DataSeries newSeries = parentDataModel.createNewDataSeries(parentDataModel.getDefaultSeriesLabel());
			
			ValuePair initialValuePair = parentDataModel.createNewValuePair(0L, newValue);
			parentDataModel.getValuePairsFromSeries(newSeries).add(initialValuePair);
			parentDataModel.addSeries(newSeries);
			
		} else {
			// --- Get current selection --------
			int modelLineSelected = jTable.convertRowIndexToModel(tableRowSelected);
			rowSelected = tableData.get(modelLineSelected);
			currKey = (Number) rowSelected.get(0);
			
			if (currKey instanceof Long || currKey instanceof Integer) {
				newKey = currKey.longValue() + 1L;
			} else {
				newKey = currKey.floatValue() + 1;
			}
			newRow = new Vector<Object>();
			newRow.add(newKey);
			for(int i=1; i<getColumnCount(); i++){
				newRow.add(newValue);
				try {
					parentDataModel.getChartModel().addOrUpdateValuePair(i-1, newKey, newValue);
					parentDataModel.getOntologyModel().addOrUpdateValuePair(i-1, newKey, newValue);
				} catch (NoSuchSeriesException e) {
					e.printStackTrace();
				}
			}
			
		
			// --- Add the new row ----
			tableData.add(modelLineSelected+1, newRow);
			
		}
		fireTableRowsInserted(getRowCount()-1, getRowCount()-1);
		
		if(tableRowSelected >= 0){
			jTable.setRowSelectionInterval(tableRowSelected, tableRowSelected);
		}
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
			if(((Number)tableData.get(i).get(0)).doubleValue()==key.doubleValue()){
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
		List valuePairs = parentDataModel.getValuePairsFromSeries(newSeries); 
		
		if(this.getRowCount()==0){
			// tableData is empty, add a new Row for each value pair
			for (int i=0; i<valuePairs.size(); i++) {
				ValuePair vp = (ValuePair) valuePairs.get(i);
				
				Vector<Object> newRow = new Vector<Object>();
				newRow.add(parentDataModel.getKeyFromPair(vp));
				newRow.add(parentDataModel.getValueFromPair(vp));
				
				tableData.add(newRow);
			}
			
		}else{
			
			// tableData is not empty, integrate the new series
			int oldColCount = getColumnCount();
			for (int i=0; i<valuePairs.size(); i++) {
				ValuePair vp = (ValuePair) valuePairs.get(i);
				
				// Check if there is a row with this time stamp
				int rowIndex = getRowIndexByKey(parentDataModel.getKeyFromPair(vp));
				if(rowIndex >= 0){
					// There is one, append the new value
					tableData.get(rowIndex).add(parentDataModel.getValueFromPair(vp));
					
				}else{
					// There is not, add a new row
					Vector<Object> newRow = new Vector<Object>();
					// First column contains the time stamp
					newRow.add(parentDataModel.getKeyFromPair(vp));
					// There is no value for this time stamp in the old series
					while(newRow.size() < oldColCount){
						newRow.add(null);
					}
					// Append the new value at the end of the row
					newRow.add(parentDataModel.getValueFromPair(vp));
					
					// Find the right place to insert the new row:
					int insertAt = getRowCount();	// At the end of the table...
					for(int j=0;j<this.getRowCount();j++){
						double compareValue = ((Number) tableData.get(j).get(0)).doubleValue();
						if(compareValue > parentDataModel.getKeyFromPair(vp).doubleValue()){
							insertAt = j;			// ... or before the first one with a higher time stamp
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
	 * Exchanges the specified data series with a new data series.
	 *
	 * @param seriesIndex the series index
	 * @param newSeries the new series
	 */
	public void exchangeSeries(int seriesIndex, DataSeries newSeries) {
		
		int valueColumIndex = seriesIndex+1;
		this.columnTitles.set(valueColumIndex, newSeries.getLabel());
		HashMap<Number, Vector<Object>> availableRows = new HashMap<Number, Vector<Object>>(); 
		
		// --- Remove values that are belonging to the old series ---
		for (int i=0; i<getRowCount(); i++) {
			// --- Set value null ------------------------------
			Vector<Object> rowVector = this.getRow(i);
			Number keyValue = (Number) rowVector.get(0);
			availableRows.put(keyValue, rowVector);
			this.getRow(i).set(valueColumIndex, null);
		}
		
		List valuePairs = this.parentDataModel.getValuePairsFromSeries(newSeries);
		for (int i = 0; i < valuePairs.size(); i++) {
			
			ValuePair vp = (ValuePair) valuePairs.get(i);
			Number keyValue = this.parentDataModel.getKeyFromPair(vp);
			Vector<Object> rowVector = availableRows.get(keyValue);
			if (rowVector==null) {
				// No row found, create a new row
				rowVector = new Vector<Object>();
				while(rowVector.size()< this.getColumnCount()){
					rowVector.add(null);
				}
				rowVector.set(0, parentDataModel.getKeyFromPair(vp));
				rowVector.set(valueColumIndex, parentDataModel.getValueFromPair(vp));
				
				// Find the right place to insert the new row:
				int insertAt = getRowCount();	// At the end of the table...
				for(int j=0;j<this.getRowCount();j++){
					double compareValue = ((Number) tableData.get(j).get(0)).doubleValue();
					if(compareValue > parentDataModel.getKeyFromPair(vp).doubleValue()){
						insertAt = j;			// ... or before the first one with a higher time stamp
					}
				}
				tableData.add(insertAt, rowVector);
				
			} else {
				// Row found, exchange the value
				rowVector.set(valueColumIndex, parentDataModel.getValueFromPair(vp));
				
			}
		} 
		
		// --- Remove all empty rows --------------------------------
		this.removeEmptyRows();
		
		this.fireTableStructureChanged();
		
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

	/**
	 * Returns the row specified by the index.
	 * 
-	 * @param rowIndex the row index
	 * @return the row
	 */
	public Vector<Object> getRow(int rowIndex) {
		return this.tableData.get(rowIndex);
	}
	
	/**
	 * Checks if a row, given by it's index is empty.
	 *
	 * @param rowIndex the row index
	 * @return true, if it is an empty TableModel row
	 */
	public boolean isEmptyTableModelRow(int rowIndex) {
		return isEmptyTableModelRow(this.getRow(rowIndex));
	}
	
	/**
	 * Checks if a given row is empty.
	 *
	 * @param rowVector the row vector
	 * @return true, if is empty row
	 */
	public boolean isEmptyTableModelRow(Vector<Object> rowVector) {
		
		boolean empty = false;
		
		if (rowVector==null) {
			empty = true;
			
		} else if (rowVector.size()<=1) {
			empty = true;
			
		} else {
			if (rowVector.get(0)==null) {
				empty = true;
				
			} else {
				// --- Check values -----------------------
				int nullCount = 0;
				for (int i=1; i < rowVector.size(); i++) {
					Object currValue = rowVector.get(i);
					if (currValue==null) {
						nullCount++;
					}
				}
				// --- Finally check, if empty row --------
				if (nullCount>=(rowVector.size()-1)) {
					empty = true;
				}
				
			}
		}
		return empty;
	}
	
	/**
	 * Removes empty rows from the table model.
	 */
	public void removeEmptyRows() {
		for (int i=0; i<getRowCount(); i++) {
			if (isEmptyTableModelRow(this.getRow(i))==true) {
				// --- Remove row ---------------
				this.tableData.remove(i);
				i--;
			}
		}
	}
	
	/**
	 * Gets the table data tree map.
	 * @return the table data tree map
	 */
	public TreeMap<Number, Vector<Object>> getTableDataAsTreeMap() {
		TreeMap<Number, Vector<Object>> treeMap = new TreeMap<Number, Vector<Object>>();
		for (int i = 0; i < tableData.size(); i++) {
			Vector<Object> row = tableData.get(i);
			treeMap.put((Number) row.get(0), row);
		}
		return treeMap;
	}
	
	
	/**
	 * Edits the data series by adding data.
	 * @param series the series
	 * @param targetDataSeriesIndex the target data series index
	 */
	public void editSeriesAddData(DataSeries series, int targetDataSeriesIndex) throws NoSuchSeriesException {
		
		if (targetDataSeriesIndex<=(this.getColumnCount()-1)) {
			
			int targetTbIndex = targetDataSeriesIndex+1;
			List valuePairs = parentDataModel.getValuePairsFromSeries(series); 
			for (int i = 0; i < valuePairs.size(); i++) {
				TimeSeriesValuePair vp = (TimeSeriesValuePair) valuePairs.get(i);
				Vector<Object> newRow = new Vector<Object>();
				newRow.add(parentDataModel.getKeyFromPair(vp));
				while(newRow.size() < this.getColumnCount()){
					newRow.add(null);
				}
				newRow.add(targetTbIndex, parentDataModel.getValueFromPair(vp));
				tableData.add(newRow);
			}
			this.fireTableStructureChanged();
			
		} else {
			throw new NoSuchSeriesException(); 
		}
	}
	/**
	 * Edits the data series by adding or exchanging data.
	 * @param series the series
	 * @param targetDataSeriesIndex the target data series index
	 */
	public void editSeriesAddOrExchangeData(DataSeries series, int targetDataSeriesIndex) throws NoSuchSeriesException {

		if (targetDataSeriesIndex<=(this.getColumnCount()-1)) {
			
			boolean dataWereAdded = false;
			int targetTbIndex = targetDataSeriesIndex+1;
			TreeMap<Number, Vector<Object>> tableDataTreeMap = this.getTableDataAsTreeMap();
			
			List valuePairs = parentDataModel.getValuePairsFromSeries(series);
			for (int i = 0; i < valuePairs.size(); i++) {
				// --- Find the key in the table ----------
				TimeSeriesValuePair vp = (TimeSeriesValuePair) valuePairs.get(i);
				Number key = parentDataModel.getKeyFromPair(vp);
				Number value = parentDataModel.getValueFromPair(vp);

				Vector<Object> editRow = tableDataTreeMap.get(key);
				if (editRow==null) {
					// --- Add a new row ------------------
					editRow = new Vector<Object>();
					while(editRow.size() < this.getColumnCount()){
						editRow.add(null);
					}
					editRow.set(0, key);
					editRow.set(targetTbIndex, value);
					tableDataTreeMap.put(key, editRow);
					dataWereAdded = true;
					
				} else {
					// --- Finally edit the row -----------
					editRow.set(targetTbIndex, value);
				}
			}
			
			// --- Finally update the table, if necessary -
			if (dataWereAdded==true) {
				// --- Rebuild the table ------------------
				Number[] keyArray = new Number[tableDataTreeMap.size()]; 
				tableDataTreeMap.keySet().toArray(keyArray);
				for (int i = 0; i < keyArray.length; i++) {
					Number keyTreeMap = keyArray[i];
					if ((valuePairs.size()-1)<i) {
						// --- Just add new data ----------
						this.tableData.add(i, tableDataTreeMap.get(keyTreeMap));
					} else {
						Number keyValuePair = parentDataModel.getKeyFromPair((ValuePair) valuePairs.get(i));
						if (keyTreeMap.equals(keyValuePair)==false) {
							// --- New data was found -----
							this.tableData.add(i, tableDataTreeMap.get(keyTreeMap));
						}	
					}
				}	
			}
			this.fireTableStructureChanged();
			
		} else {
			throw new NoSuchSeriesException();
		}
	}
	/**
	 * Edits the data series by exchanging data.
	 * @param series the series
	 * @param targetDataSeriesIndex the target data series index
	 */
	public void editSeriesExchangeData(DataSeries series, int targetDataSeriesIndex) throws NoSuchSeriesException {
		//TODO
		System.out.println("ToDo: Edit Tabele model");
	}
	/**
	 * Edits the data series by remove data.
	 * @param series the series
	 * @param targetDataSeriesIndex the target data series index
	 */
	public void editSeriesRemoveData(DataSeries series, int targetDataSeriesIndex) throws NoSuchSeriesException {
		//TODO
		System.out.println("ToDo: Edit Tabele model");
	}
	
}
