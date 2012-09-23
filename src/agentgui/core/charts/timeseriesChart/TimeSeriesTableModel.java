package agentgui.core.charts.timeseriesChart;

import jade.util.leap.Iterator;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import agentgui.core.charts.NoSuchSeriesException;
import agentgui.ontology.TimeSeries;
import agentgui.ontology.TimeSeriesValuePair;

/**
 * Data model for the table representation of a time series
 * @author Nils
 */
public class TimeSeriesTableModel extends AbstractTableModel {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 7951673309652098789L;
	
	private static final String DEFAULT_TIME_COLUMN_TITLE = "Time";
	
	private Vector<String> columnTitles;
	/**
	 * The table data
	 */
	private Vector<Vector<Object>> tableData;
	/**
	 * The old value of the last changed table cell
	 */
	private Object latestChangedValue;
	/**
	 * Constructor
	 */
	public TimeSeriesTableModel(){
		columnTitles = new Vector<String>();
		columnTitles.add(DEFAULT_TIME_COLUMN_TITLE);
		tableData = new Vector<Vector<Object>>();
	}

	@Override
	public int getColumnCount() {
		if(tableData.size() > 0){
			return tableData.get(0).size();
		}else{
			return 0;
		}
	}

	@Override
	public int getRowCount() {
		return tableData.size();
	}

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
	 * Add a new time series to the table model
	 * @param newSeries
	 */
	public void addSeries(TimeSeries newSeries){
		
		columnTitles.add(newSeries.getLabel());
		
		Iterator valuePairs = newSeries.getAllTimeSeriesValuePairs();
		 
		if(getRowCount() == 0){
			
			// tableData is empty, add a new Row for each value pair
			
			while(valuePairs.hasNext()){
				TimeSeriesValuePair vp = (TimeSeriesValuePair) valuePairs.next();
				
				Vector<Object> newRow = new Vector<Object>();
				newRow.add(vp.getTimestamp().getLongValue());
				newRow.add(vp.getValue().getFloatValue());
				
				tableData.add(newRow);
			}
		}else{
			
			// tableData is not empty, integrate the new series
			
			int oldColCount = getColumnCount();
			
			while(valuePairs.hasNext()){
				TimeSeriesValuePair vp = (TimeSeriesValuePair) valuePairs.next();
				
				// Check if there is a row with this time stamp
				int rowIndex = getRowIndexByTimestamp(vp.getTimestamp().getLongValue());
				
				if(rowIndex >= 0){
					
					// There is one, append the new value
					tableData.get(rowIndex).add(vp.getValue().getFloatValue());
					
				}else{
					// There is not, add a new row
					Vector<Object> newRow = new Vector<Object>();
					
					// First column contains the time stamp
					newRow.add(vp.getTimestamp().getLongValue());
					
					// There is no value for this time stamp in the old series
					while(newRow.size() < oldColCount){
						newRow.add(null);
					}
					// Append the new value at the end of the row
					newRow.add(vp.getValue().getFloatValue());
					
					// Find the right place to insert the new row:
					int insertAt = getRowCount();	// At the end of the table...
					for(int i=0;i<getRowCount();i++){
						long compareValue = (Long) tableData.get(i).get(0);
						if(compareValue > vp.getTimestamp().getLongValue()){
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
		if(seriesIndex < this.getColumnCount()-1){
			columnTitles.remove(seriesIndex);
			java.util.Iterator<Vector<Object>> rows = tableData.iterator();
			while(rows.hasNext()){
				rows.next().remove(seriesIndex);
			}
		}else{
			throw new NoSuchSeriesException();
		}
		
		fireTableStructureChanged();
	}
	
	/**
	 * Gets the value with the specified time stamp from the series with the specified index
	 * @param seriesIndex The index of the series to get the value from
	 * @param timestamp The time stamp of the desired value
	 * @return The value for the given time stamp
	 * @throws NoSuchSeriesException Will be thrown if there is no series with the specified index
	 */
	public Float getValue(int seriesIndex, long timestamp) throws NoSuchSeriesException{
		if(seriesIndex < this.getColumnCount()-1){
			int rowIndex = getRowIndexByTimestamp(timestamp);
			return (Float) getValueAt(rowIndex, seriesIndex);
		}else{
			throw new NoSuchSeriesException();
		}
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
	 * @return the defaultTimeColumnTitle
	 */
	public static String getDefaultTimeColumnTitle() {
		return DEFAULT_TIME_COLUMN_TITLE;
	}

	/**
	 * Returns the index of the row with the given time stamp, or -1 if the time stamp does 
	 * not exist in the table. 
	 * @param timestamp
	 * @return
	 */
	private int getRowIndexByTimestamp(long timestamp){
		for(int i=0;i<getRowCount();i++){
			if(((Long)tableData.get(i).get(0)) == timestamp){
				return i;
			}
		}
		return -1;
	}
	
	public void setSeriesLabel(int seriesIndex, String label) throws NoSuchSeriesException{
		if(seriesIndex < getColumnCount()-1 ){
			columnTitles.remove(seriesIndex+1);
			columnTitles.add(seriesIndex+1, label);
			fireTableStructureChanged();
		}else{
			throw new NoSuchSeriesException();
		}
	}
	
	public void setTimeColumnLabel(String label){
		columnTitles.remove(0);
		columnTitles.add(0, label);
		fireTableStructureChanged();
	}
	
	public void addEmptyRow(long timestamp){
		
		// Create a new row with the given time stamp and empty fields for all series
		Vector<Object> newRow = new Vector<Object>();
		newRow.add(timestamp);
		for(int i=1; i<getColumnCount(); i++){
			newRow.add(null);
		}
		
		// Find the right position to insert the row
		int insertPos = 0;
		while(insertPos < getRowCount() && (Long)getValueAt(insertPos, 0) < timestamp){
			insertPos++;
		}
		
		// Add the new row
		tableData.add(insertPos, newRow);
	}

}
