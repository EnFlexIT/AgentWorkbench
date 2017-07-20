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
package agentgui.core.charts.timeseriesChart;

import jade.util.leap.List;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.RowSorter.SortKey;
import javax.swing.event.TableModelEvent;
import agentgui.core.charts.NoSuchSeriesException;
import agentgui.core.charts.TableModel;
import agentgui.core.charts.TableModelDataVector;
import agentgui.ontology.DataSeries;
import agentgui.ontology.TimeSeries;
import agentgui.ontology.ValuePair;

/**
 * Data model for the table representation of a time series
 * @author Nils
 */
public class TimeSeriesTableModel extends TableModel {

	private static final long serialVersionUID = 7951673309652098789L;
	
	private static final String DEFAULT_TIME_COLUMN_TITLE = "Time";
	
	
	/**
	 * Constructor
	 */
	public TimeSeriesTableModel(TimeSeriesDataModel parent){
		this.parentDataModel = parent;
		this.initilizeTabelModel();
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.TableModel#initilizeTabelModel()
	 */
	public void initilizeTabelModel() {
		columnTitles = new Vector<String>();
		columnTitles.add(DEFAULT_TIME_COLUMN_TITLE);
		tableModelDataVector = new TableModelDataVector(TimeSeries.class.getSimpleName(), false, 0);
		this.addTableModelListener(this);
	}

	/* (non-Javadoc)
	 * @see agentgui.core.charts.TableModel#rebuildTableModel()
	 */
	@Override
	public void rebuildTableModel() {
		
		SortKey sortKey = this.getSortKey();
				
		columnTitles.clear();
		columnTitles.add(DEFAULT_TIME_COLUMN_TITLE);
		
		// Remember the current width of the table columns
		Vector<Integer> columnWidths = this.getColumnWidths();
		
		// Rebuild the table model
		this.tableModelDataVector = new TableModelDataVector(TimeSeries.class.getSimpleName(), this.tableModelDataVector.isActivateRowNumber(), this.tableModelDataVector.getKeyColumnIndex());
		List chartDataSeries = this.parentDataModel.getOntologyModel().getChartData();
		for (int i = 0; i < chartDataSeries.size(); i++) {
			DataSeries chartData = (DataSeries) chartDataSeries.get(i);
			this.addSeries(chartData, false);
		}
		
		// Set the width of the columns like before
		this.setColumnWidths(columnWidths);
		
		this.setSortKey(sortKey);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if(columnIndex == 0){
			return Long.class;
		}else{
			return Float.class;
		}
	}
	
	/**
	 * @return the defaultTimeColumnTitle
	 */
	public static String getDefaultTimeColumnTitle() {
		return DEFAULT_TIME_COLUMN_TITLE;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.TableModel#setSeriesLabel(int, java.lang.String)
	 */
	@Override
	public void setSeriesLabel(int seriesIndex, String label) throws NoSuchSeriesException{
		if(seriesIndex < getColumnCount()-this.tableModelDataVector.getNoOfPrefixColumns()){
			columnTitles.remove(seriesIndex+this.tableModelDataVector.getNoOfPrefixColumns());
			columnTitles.add(seriesIndex+this.tableModelDataVector.getNoOfPrefixColumns(), label);
			fireTableStructureChanged();
		}else{
			throw new NoSuchSeriesException();
		}
	}
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.TableModel#addEmptyRow(javax.swing.JTable)
	 */
	@Override
	public void addEmptyRow(JTable jTable){
		
		// --- Get current position in the table model ----
		Vector<Number> rowSelected = null;
		Vector<Number> newRow = null;
		long currKey = 0L;
		long newKey = 0L;
		float newValue = 0f;
		
		// --- Get the current or the last row ------------
		int modelLineSelected = 0;
		int modelLineSelectedNew = 0;
		int tableRowSelected = 0;
		int tableRowSelectedNew = 0;
			
		tableRowSelected = jTable.getSelectedRow();
		if (tableRowSelected==-1 || tableRowSelected>=jTable.getRowCount()) {
			tableRowSelected = jTable.getRowCount()-1;
		}
		
		if (tableModelDataVector.size()==0) {
			// --- Create new data series -------
			DataSeries newSeries = parentDataModel.createNewDataSeries(parentDataModel.getDefaultSeriesLabel());
			
			ValuePair initialValuePair = parentDataModel.createNewValuePair(0L, newValue);
			parentDataModel.getValuePairsFromSeries(newSeries).add(initialValuePair);
			parentDataModel.addSeries(newSeries);
			
		} else {
			// --- Get current selection --------
			modelLineSelected = jTable.convertRowIndexToModel(tableRowSelected);
			rowSelected = tableModelDataVector.get(modelLineSelected);
			currKey = (Long) rowSelected.get(tableModelDataVector.getKeyColumnIndex());
			
			// --- Find a new key value ---------
			newKey = currKey + 1L;
			while (tableModelDataVector.getKeyRowVectorTreeMap().get(newKey)!=null) {
				newKey = newKey + 1L;
			}
			
			// --- Find new index position ------
			modelLineSelectedNew = tableModelDataVector.size();
			for (int i=0; i < tableModelDataVector.size(); i++) {
				Vector<Number> row = tableModelDataVector.get(i);
				long key = (Long) row.get(0);
				if (key>newKey) {
					modelLineSelectedNew = i;
					break;
				}
			}
			
			// --- Add new row data -------------
			newRow = new Vector<Number>();
			newRow.add(newKey);
			for(int i=1; i<getColumnCount(); i++){
				newRow.add(newValue);
				try {
					this.getChartModel().addOrUpdateValuePair(i-1, newKey, newValue);
					this.getOntologyModel().addOrUpdateValuePair(i-1, newKey, newValue);
					
				} catch (NoSuchSeriesException e) {
					e.printStackTrace();
				}
			}
			// --- Add new row to table model ---
			tableModelDataVector.add(modelLineSelectedNew, newRow);
			
		}
		fireTableRowsInserted(0, getRowCount()-1);
		
		// --- Set new selection ----------------
		tableRowSelectedNew = jTable.convertRowIndexToView(modelLineSelectedNew);
		if(tableRowSelectedNew >= 0){
			jTable.setRowSelectionInterval(tableRowSelectedNew, tableRowSelectedNew);
			jTable.changeSelection(tableRowSelectedNew, 0, false, false);
		}
	}
	
	/**
	 * Adds the specified row vector to the current table model.
	 * @param newRow the new row
	 */
	public void addRow(Vector<Number> newRow) {

		int targetRowIndex = this.getRowIndexByKey(newRow.get(0));
		if (targetRowIndex==-1) {
			targetRowIndex = tableModelDataVector.size();
			tableModelDataVector.add(newRow);
			this.fireTableRowsInserted(targetRowIndex, targetRowIndex);
		} else {
			tableModelDataVector.set(targetRowIndex, newRow);
			this.fireTableRowsUpdated(targetRowIndex, targetRowIndex);
		}
	}
	
	/* (non-Javadoc)
	 * @see agentgui.core.charts.TableModel#removeRow(javax.swing.JTable)
	 */
	@Override
	public void removeRow(JTable jTable){

		int colIndexTable = jTable.getSelectedColumn();
		int rowIndexTable = jTable.getSelectedRow();
		
		int rowIndexModel = 0;
		int rowIndexTableNew = 0;
		int rowIndexModelNew = 0;
		
		// --- Stop editing -----------------
		if(jTable.isEditing()){
			jTable.getCellEditor().cancelCellEditing();
		}
		
		// ---- Last row of data series? ----
		if(jTable.getRowCount()==1){
			while(parentDataModel.getSeriesCount()>0){
				try {
					parentDataModel.removeSeries(0);
				} catch (NoSuchSeriesException e1) {
					// This cannot happen, as there where clause prevents empty models  
				}
			}
			rowIndexModelNew = -1;
			rowIndexTableNew = -1;
			
		}else{
			
			if ((rowIndexTable+1) > jTable.getRowCount()) {
				rowIndexTable = jTable.getRowCount() - 1;
			}
			Number key = (Number) jTable.getValueAt(rowIndexTable, 0);
			rowIndexModel = jTable.convertRowIndexToModel(rowIndexTable);
			((TimeSeriesDataModel)parentDataModel).removeValuePairsFromAllSeries(key);
			
			rowIndexModelNew = rowIndexModel;
			if (rowIndexModelNew>=jTable.getRowCount()) {
				rowIndexModelNew = jTable.getRowCount()-1;
			}
			rowIndexTableNew = jTable.convertRowIndexToView(rowIndexModelNew);
			
		}
		
		// --- Set new selection ------------
		if(rowIndexTableNew==-1){
			jTable.clearSelection();
		} else {
			jTable.setRowSelectionInterval(rowIndexTableNew, rowIndexTableNew);
			jTable.changeSelection(rowIndexTableNew, colIndexTable, false, false);
		}
		
	}
	
	/**
	 * Add a new time series to the table model
	 * @param newSeries
	 */
	public void addSeries(DataSeries newSeries){
		this.addSeries(newSeries, true);
	}
	
	/**
	 * Add a new time series to the table model
	 * @param newSeries
	 * @param restoreSorting Restore sort order afterwards?
	 */
	public void addSeries(DataSeries newSeries, boolean restoreSorting){
		
		SortKey sortKey = this.getSortKey();
		
		List valuePairs = parentDataModel.getValuePairsFromSeries(newSeries); 
		
		int destColIndex = getColumnCount();
		columnTitles.add(newSeries.getLabel());
		for (int i=0; i<valuePairs.size(); i++) {
			
			// --- Check if there is a row with this key value --------------------------
			ValuePair vp = (ValuePair) valuePairs.get(i);
			if (this.getRowCount()==0) {
				// --- Add a first row --------------------------------------------------
				Vector<Number> newRow = new Vector<Number>();
				newRow.add(parentDataModel.getXValueFromPair(vp));
				newRow.add(parentDataModel.getYValueFromValuePair(vp));
				tableModelDataVector.add(newRow);
				destColIndex = 1;
				
			} else {
				// --- TableData is not empty, integrate the new series	-----------------
				int rowIndex = this.getRowIndexByKey(parentDataModel.getXValueFromPair(vp));
				if (rowIndex>=0){
					// --- There is one, append the new value ---------------------------
					Vector<Number> editRow = tableModelDataVector.get(rowIndex);
					while(editRow.size() < (destColIndex+1)){
						editRow.add(null);
					}
					editRow.set(destColIndex, parentDataModel.getYValueFromValuePair(vp));
					
				} else {
					// --- There is not, add a new row ----------------------------------
					Vector<Number> newRow = new Vector<Number>();
					// --- First column contains the time stamp -------------------------
					newRow.add(parentDataModel.getXValueFromPair(vp));
					// --- There is no value for this time stamp in the old series ------
					while(newRow.size() < destColIndex){
						newRow.add(null);
					}
					// --- Append the new value at the end of the row -------------------
					newRow.add(destColIndex, parentDataModel.getYValueFromValuePair(vp));
					
					// ---- Find the right place to insert the new row: -----------------
					int insertAt = getRowCount();	// At the end of the table...
					for(int j = 0; j < this.getRowCount(); j++){
						double compareValue = ((Number) tableModelDataVector.get(j).get(0)).doubleValue();
						if(compareValue > parentDataModel.getXValueFromPair(vp).doubleValue()){
							insertAt = j;			// ... or before the first one with a higher time stamp
						}
					}
					tableModelDataVector.add(insertAt, newRow);
					
				}
			}
		}
		
		// Append empty values to all rows for which the new series did not contain a value 
		for(int i=0; i < getRowCount(); i++){
			if(tableModelDataVector.get(i).size() <= destColIndex){
				tableModelDataVector.get(i).add(null);
			}
		}
		fireTableStructureChanged();
		
		if(restoreSorting == true && this.getColumnCount() > 0){
			this.setSortKey(sortKey);
		}
		
	}
	
	/**
	 * Removes a time series from the table model
	 * @param seriesIndex The index of the series to be removed
	 * @throws NoSuchSeriesException Will be thrown if there is no series with the specified index
	 */
	public void removeSeries(int seriesIndex) throws NoSuchSeriesException {
		
		SortKey sortKey = this.getSortKey();
		
		if (this.getColumnCount()==2) {
			// --- The last series has to be removed ------
			this.initilizeTabelModel();
			
		} else {
			int column2remove = seriesIndex+1;	// Column 0 contains the time stamps
			if(column2remove <= this.getColumnCount()){
				columnTitles.remove(column2remove);
				java.util.Iterator<Vector<Number>> rows = tableModelDataVector.iterator();
				while(rows.hasNext()){
					rows.next().remove(column2remove);
				}
			}
//			else{
//				throw new NoSuchSeriesException();
//			}
			
		}
		fireTableStructureChanged();
		
		if(this.getColumnCount() > 0){
			this.setSortKey(sortKey);
		}
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
		HashMap<Number, Vector<Number>> availableRows = new HashMap<Number, Vector<Number>>(); 
		
		// --- Remove values that are belonging to the old series ---
		for (int i=0; i<getRowCount(); i++) {
			// --- Set value null ------------------------------
			Vector<Number> rowVector = this.getRow(i);
			Number keyValue = (Number) rowVector.get(0);
			availableRows.put(keyValue, rowVector);
			this.getRow(i).set(valueColumIndex, null);
		}
		
		List valuePairs = this.parentDataModel.getValuePairsFromSeries(newSeries);
		for (int i = 0; i < valuePairs.size(); i++) {
			
			ValuePair vp = (ValuePair) valuePairs.get(i);
			Number keyValue = this.parentDataModel.getXValueFromPair(vp);
			Vector<Number> rowVector = availableRows.get(keyValue);
			if (rowVector==null) {
				// No row found, create a new row
				rowVector = new Vector<Number>();
				while(rowVector.size()< this.getColumnCount()){
					rowVector.add(null);
				}
				rowVector.set(0, parentDataModel.getXValueFromPair(vp));
				rowVector.set(valueColumIndex, parentDataModel.getYValueFromValuePair(vp));
				
				// Find the right place to insert the new row:
				int insertAt = getRowCount();	// At the end of the table...
				for(int j=0;j<this.getRowCount();j++){
					double compareValue = ((Number) tableModelDataVector.get(j).get(0)).doubleValue();
					if(compareValue > parentDataModel.getXValueFromPair(vp).doubleValue()){
						insertAt = j;			// ... or before the first one with a higher time stamp
					}
				}
				tableModelDataVector.add(insertAt, rowVector);
				
			} else {
				// Row found, exchange the value
				rowVector.set(valueColumIndex, parentDataModel.getYValueFromValuePair(vp));
				
			}
		} 
		
		// --- Remove all empty rows --------------------------------
		this.removeEmptyRows();
		
		this.fireTableStructureChanged();
		
		if(this.getColumnCount() > 0){
			this.myJTable.getRowSorter().toggleSortOrder(0);
		}
		
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
				ValuePair vp = (ValuePair) valuePairs.get(i);
				Vector<Number> newRow = new Vector<Number>();
				newRow.add(parentDataModel.getXValueFromPair(vp));
				while(newRow.size() < this.getColumnCount()){
					newRow.add(null);
				}
				newRow.add(targetTbIndex, parentDataModel.getYValueFromValuePair(vp));
				tableModelDataVector.add(newRow);
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
			TreeMap<Number, Vector<Number>> tableDataTreeMap = new TreeMap<Number, Vector<Number>>(this.tableModelDataVector.getKeyRowVectorTreeMap()); 
			
			List valuePairs = parentDataModel.getValuePairsFromSeries(series);
			for (int i = 0; i < valuePairs.size(); i++) {
				// --- Find the key in the table ----------
				ValuePair vp = (ValuePair) valuePairs.get(i);
				Number key = parentDataModel.getXValueFromPair(vp);
				Number value = parentDataModel.getYValueFromValuePair(vp);

				Vector<Number> editRow = tableDataTreeMap.get(key);
				if (editRow==null) {
					// --- Add a new row ------------------
					editRow = new Vector<Number>();
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
				for (int i=0; i<keyArray.length; i++) {
					Number keyTreeMap = keyArray[i];
					if ((valuePairs.size()-1)<i) {
						// --- Just add new data ----------
						this.tableModelDataVector.add(i, tableDataTreeMap.get(keyTreeMap));
					} else {
						Number keyValuePair = parentDataModel.getXValueFromPair((ValuePair) valuePairs.get(i));
						if (keyTreeMap.equals(keyValuePair)==false) {
							// --- New data was found -----
							this.tableModelDataVector.add(i, tableDataTreeMap.get(keyTreeMap));
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

		if (targetDataSeriesIndex<=(this.getColumnCount()-1)) {
			
			int targetTbIndex = targetDataSeriesIndex+1;
			List valuePairs = parentDataModel.getValuePairsFromSeries(series);
			for (int i = 0; i < valuePairs.size(); i++) {
				// --- Find the key in the table ----------
				ValuePair vp = (ValuePair) valuePairs.get(i);
				Number key = parentDataModel.getXValueFromPair(vp);
				Number value = parentDataModel.getYValueFromValuePair(vp);

				Vector<Number> editRow = this.tableModelDataVector.getKeyRowVectorTreeMap().get(key);
				if (editRow!=null) {
					// --- Edit the row -------------------
					editRow.set(targetTbIndex, value);
				}
			}
			this.fireTableStructureChanged();
			
		} else {
			throw new NoSuchSeriesException();
		}
	}
	/**
	 * Edits the data series by remove data.
	 * @param series the series
	 * @param targetDataSeriesIndex the target data series index
	 */
	public void editSeriesRemoveData(DataSeries series, int targetDataSeriesIndex) throws NoSuchSeriesException {

		if (targetDataSeriesIndex<=(this.getColumnCount()-1)) {
			
			int targetTbIndex = targetDataSeriesIndex+1;
			HashSet<Number> removeKeys = this.getKeyHashSetFromDataSeries(series);
			
			for (int i = 0; i < this.tableModelDataVector.size(); i++) {
				Vector<Number> rowVector = this.tableModelDataVector.get(i);
				Number keyValue = (Number) rowVector.get(0);
				if (removeKeys.contains(keyValue)) {
					// --- Delete Value ---------
					rowVector.set(targetTbIndex, null);
				}
			}
			this.removeEmptyRows();
			this.fireTableStructureChanged();
			
		} else {
			throw new NoSuchSeriesException();
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.TableModelListener#tableChanged(javax.swing.event.TableModelEvent)
	 */
	@Override
	public void tableChanged(TableModelEvent tme) {
		
		if(tme.getSource()==this && tme.getFirstRow()>=0){
			
			if(tme.getType()==TableModelEvent.UPDATE){
				// --- Update Events in the table ---------
				if(tme.getColumn() > 0){
					// --- A single value was edited ------------------------------------
					int seriesIndex = tme.getColumn()-1; // First column contains the time stamps.
					int rowIndex = tme.getFirstRow();
					
					Number key = (Number) this.getValueAt(rowIndex, 0);
					Number value = (Number) this.getValueAt(rowIndex, tme.getColumn());
					Vector<Number> rowVector = this.getRow(rowIndex);
					
					try {
						if(value!=null){
							// --- Update new entry in chart and ontology model ---------
							this.getChartModel().addOrUpdateValuePair(seriesIndex, key, value);
							this.getOntologyModel().addOrUpdateValuePair(seriesIndex, key, value);
						
						} else {
							if (this.isEmptyTableModelRow(rowVector)==false) {
								// --- Rewrite the data row -----------------------------
								for (int i=1; i < rowVector.size(); i++) {
									Number seriesValue = (Number) rowVector.get(i);
									int series = i-1;
									if (seriesValue!=null) {
										this.getChartModel().addOrUpdateValuePair(series, key, seriesValue);
										this.getOntologyModel().addOrUpdateValuePair(series, key, seriesValue);
									} else {
										this.getChartModel().removeValuePair(series, key);
										this.getOntologyModel().removeValuePair(series, key);
									}
								} // end for
								
							} else {
								// --- Empty row, delete ValuePair ----------------------
								this.getChartModel().removeValuePair(seriesIndex, key);
								this.getOntologyModel().removeValuePair(seriesIndex, key);
							}
						}
						
					} catch (NoSuchSeriesException e) {
						System.err.println("Error updating data model: Series "+seriesIndex+" does mot exist!");
						e.printStackTrace();
					}
				
				} else if(tme.getColumn() == 0) {
					// --- The key value (e.g. the time stamp) was edited ---------------
					Number oldKey = (Number) this.getLatestChangedValue();
					Number newKey = (Number) this.getValueAt(tme.getFirstRow(), 0);
					
					this.getChartModel().updateKey(oldKey, newKey);
					this.getOntologyModel().updateKey(oldKey, newKey);
				}
				
			} 
			
		}

	}

	/**
	 * Returns the corresponding chart model.
	 * @return the chart model
	 */
	private TimeSeriesChartModel getChartModel() {
		return (TimeSeriesChartModel)this.parentDataModel.getChartModel();
	}
	/**
	 * Returns the corresponding ontology model.
	 * @return the ontology model
	 */
	private TimeSeriesOntologyModel getOntologyModel() {
		return (TimeSeriesOntologyModel)this.parentDataModel.getOntologyModel();
	}
	
	public void setTable(JTable table){
		this.myJTable = table;
	}
	
	private SortKey getSortKey(){
		SortKey sortKey = null;
		
		if(myJTable.getRowSorter().getSortKeys().size() > 0){
			sortKey = myJTable.getRowSorter().getSortKeys().get(0);
		}

		return sortKey;
		
	}
	
	private void setSortKey(SortKey sortKey){
		if(sortKey != null){
			Vector<SortKey> keyVector = new Vector<SortKey>();
			keyVector.addElement(sortKey);
			myJTable.getRowSorter().setSortKeys(keyVector);
		}
	}
}
