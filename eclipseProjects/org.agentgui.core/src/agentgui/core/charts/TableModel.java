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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import agentgui.ontology.DataSeries;
import agentgui.ontology.ValuePair;

/**
 * Abstract superclass for the table representation of chart data.  
 * 
 * @author Nils Loose - DAWIS - ICB University of Duisburg - Essen
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class TableModel extends AbstractTableModel implements TableModelListener {

	private static final long serialVersionUID = -6719770378777635821L;
	
	protected JTable myJTable;
	
	/** The column titles */
	protected Vector<String> columnTitles;
	/** The table data */
	protected TableModelDataVector tableModelDataVector;
	/** The old value of the last changed table cell */
	protected Object latestChangedValue;
	
	/** The parent ChartDataModel this ChartTableModel is part of. */
	protected DataModel parentDataModel;

	
	/**
	 * Initialises the local table model and (re)sets the local values 
	 * for {@link TableModel#columnTitles} and {@link TableModel#tableModelDataVector}, .
	 */
	public abstract void initilizeTabelModel();
	
	/**
	 * Rebuilds the current table model.
	 */
	public abstract void rebuildTableModel();
	
	/**
	 * Gets the table model data vector.
	 * @return the table model data vector
	 */
	public TableModelDataVector getTableModelDataVector() {
		return tableModelDataVector;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		if(tableModelDataVector.size() > 0){
			return tableModelDataVector.get(0).size();
		}else{
			return 0;
		}
	}
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		if (tableModelDataVector==null) {
			return 0;
		}
		return tableModelDataVector.size();
	}
	
	/**
	 * Returns the row specified by the index.
	 * 
-	 * @param rowIndex the row index
	 * @return the row
	 */
	public Vector<Number> getRow(int rowIndex) {
		return this.tableModelDataVector.get(rowIndex);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return tableModelDataVector.get(rowIndex).get(columnIndex);
	}
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
	 */
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		this.setLatestChangedValue(getValueAt(rowIndex, columnIndex));
		tableModelDataVector.get(rowIndex).remove(columnIndex);
		tableModelDataVector.get(rowIndex).add(columnIndex, (Number) aValue);
		fireTableCellUpdated(rowIndex, columnIndex);
	}
	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		return columnTitles.get(column);
	}
	
	/**
	 * Sets the label for the table column containing the data series with the given index.
	 * @param seriesIndex The series index
	 * @param label The label
	 * @throws NoSuchSeriesException Thrown if there is no series with this index
	 */
	public abstract void setSeriesLabel(int seriesIndex, String label) throws NoSuchSeriesException;

	/**
	 * Sets the label for the first table column containing the keys / x values
	 * @param label The label
	 */
	public void setXColumnLabel(String label){
		if (this.getRowCount()>0) {
			int xLabelIndex = 0;
			if (this.tableModelDataVector.isActivateRowNumber()==true) {
				xLabelIndex = 1;
			}
			columnTitles.remove(xLabelIndex);
			columnTitles.add(xLabelIndex, label);
			fireTableStructureChanged();
		}
	}
	
	/**
	 * Gets the latest changed value.
	 * @return the latestChangedValue
	 */
	public Object getLatestChangedValue() {
		return latestChangedValue;
	}
	/**
	 * Sets the latest changed value.
	 * @param latestChangedValue the latestChangedValue to set
	 */
	public void setLatestChangedValue(Object latestChangedValue) {
		this.latestChangedValue = latestChangedValue;
	}

	
	/**
	 * Adds a new empty row to the table model.
	 * @param jTable the current JTable
	 */
	public abstract void addEmptyRow(JTable jTable);
	/**
	 * Removes the row.
	 * @param jTable the j table
	 */
	public abstract void removeRow(JTable jTable);
	
	
	/**
	 * Add a data series to the table model
	 * @param newSeries
	 */
	public abstract void addSeries(DataSeries newSeries);
	/**
	 * Removes the data series from table model.
	 *
	 * @param seriesIndex the series index
	 * @throws NoSuchSeriesException the no such series exception
	 */
	public abstract void removeSeries(int seriesIndex) throws NoSuchSeriesException;
	/**
	 * Exchanges the specified data series with a new data series.
	 *
	 * @param seriesIndex the series index
	 * @param newSeries the new series
	 */
	public abstract void exchangeSeries(int seriesIndex, DataSeries newSeries) throws NoSuchSeriesException;
	
	
	/**
	 * Returns the index of the row with the given key / x value, or -1 if the 
	 * time stamp does not exist in the table.
	 *
	 * @param key the key
	 * @return the row index by key
	 */
	public int getRowIndexByKey(Number key){
		Integer indexPosition = this.tableModelDataVector.getKeyIndexHashMap().get(key);
		if (indexPosition==null) {
			return -1;
		} 
		return indexPosition;
	}
	
	/**
	 * Gets the value with the specified key / x value from the series with the specified index
	 * @param seriesIndex The index of the series to get the value from
	 * @param key The key / x value of the desired value
	 * @return The value for the given key / x value
	 * @throws NoSuchSeriesException Will be thrown if there is no series with the specified index
	 */
	public Float getValueByKey(int seriesIndex, Number key) throws NoSuchSeriesException{
		if(seriesIndex < this.getColumnCount()-this.tableModelDataVector.getNoOfPrefixColumns()){
			Vector<Number> rowVector = this.tableModelDataVector.getKeyRowVectorTreeMap().get(key);
			return (Float) rowVector.get(seriesIndex + this.tableModelDataVector.getNoOfPrefixColumns());
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	/**
	 * Remove the table row with the given key / x value.
	 * @param key The key / x value
	 */
	public void removeRowByKey(Number key){
		int rowIndex=getRowIndexByKey(key);
		if(rowIndex>=0){
			tableModelDataVector.remove(rowIndex);
			fireTableRowsDeleted(rowIndex, rowIndex);
		}
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
	public boolean isEmptyTableModelRow(Vector<Number> rowVector) {
		
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
				for (int i=this.tableModelDataVector.getNoOfPrefixColumns(); i < rowVector.size(); i++) {
					Object currValue = rowVector.get(i);
					if (currValue==null) {
						nullCount++;
					}
				}
				// --- Finally check, if empty row --------
				if (nullCount>=(rowVector.size()-this.tableModelDataVector.getNoOfPrefixColumns())) {
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
				this.tableModelDataVector.remove(i);
				i--;
			}
		}
	}
	
	/**
	 * Returns a hash set that contains the keys of a DataSeries .
	 *
	 * @param dataSeries the data series
	 * @return the key hash set
	 */
	public HashSet<Number> getKeyHashSetFromDataSeries(DataSeries dataSeries) {
		HashSet<Number> hashSet = new HashSet<Number>();
		List seriesVP = this.parentDataModel.getValuePairsFromSeries(dataSeries);
		for (int i = 0; i < seriesVP.size(); i++) {
			hashSet.add(this.parentDataModel.getXValueFromPair((ValuePair) seriesVP.get(i)));
		}
		return hashSet;
	}
	
	/**
	 * Converts the table data to a Vector of Object Vectors
	 * @param addHeaders If true, an additional vector containing the column headers will be added
	 * @return The table data as a Vector of Object Vectors
	 */
	public Vector<Vector<Object>> getTableDataAsObjectVector(boolean addHeaders){
		return this.getTableDataAsObjectVector(addHeaders, false);
	}
	
	/**
	 * Converts the table data to a Vector of Object Vectors
	 * @param addHeaders If true, an additional vector containing the column headers will be added
	 * @param isTimeSeriesData If true, the first column is interpreted as timestamps and converted to a time format
	 * @return The table data as a Vector of Object Vectors
	 */
	public Vector<Vector<Object>> getTableDataAsObjectVector(boolean addHeaders, boolean isTimeSeriesData){
		Vector<Vector<Object>> dataVector = new Vector<Vector<Object>>();
		
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		
		if(addHeaders == true){
			dataVector.addElement(new Vector<Object>(Arrays.asList(columnTitles.toArray())));
		}
		
		
		Collection<Vector<Number>> treeMapData = this.getTableModelDataVector().getKeyRowVectorTreeMap().values();
		for(Vector<Number> numberVector : treeMapData){
			Vector<Object> objectVector = new Vector<Object>(Arrays.asList(numberVector.toArray()));
			
			if(isTimeSeriesData == true){
				long timestamp = (long) objectVector.get(0);
				String dateTime = dateFormat.format(new Date(timestamp));
				objectVector.remove(0);
				objectVector.insertElementAt(dateTime, 0);
			}
			
			dataVector.addElement(objectVector);
		}
		return dataVector;
	}
	
	/**
	 * Creates a vector containing the current width of all table columns
	 * @return The column width array
	 */
	protected Vector<Integer> getColumnWidths(){
		
		if(this.myJTable == null){
			return null;
		}
		
		TableColumnModel tcm = this.myJTable.getColumnModel();

		// Array based old approach
//		int[] columnWidths = new int[this.getColumnCount()];
//		for(int i=0; i<this.getColumnCount(); i++){
//			TableColumn tc = tcm.getColumn(i);
//			if(tc != null){
//				columnWidths[i] = tc.getWidth();
//			}
//		}
		
		Vector<Integer> cwVector = new Vector<Integer>();
		for(TableColumn tc : Collections.list(tcm.getColumns())){
			cwVector.addElement(tc.getWidth());
		}
		
		return cwVector;
	}
	
	/**
	 * Sets the width of the table columns according to the integer array
	 * @param columnWidths Array containing the widths to set
	 */
	protected void setColumnWidths(Vector<Integer> columnWidths){
		if(columnWidths != null){
			TableColumnModel tcm = this.myJTable.getColumnModel();
			for(int i=0; i<this.getColumnCount() && i<columnWidths.size(); i++){
				TableColumn tc = tcm.getColumn(i);
				tc.setPreferredWidth(columnWidths.get(i));
			}
		}
	}
}
