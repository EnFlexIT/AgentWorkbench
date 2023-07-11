package de.enflexit.expression;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class ExpressionData is basically used to hold the data of an {@link ExpressionResult}.
 * 
 * @see ExpressionResult#getExpressionData()
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ExpressionData {
	
	/**
	 * The Enumeration ExpressionDataType specifies all data types that 
	 * can be handled within the {@link ExpressionService}.
	 *
	 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
	 */
	public enum DataType {
		Boolean,
		Integer,
		Long,
		Double,
	}
	
	private List<DataColumn> dataColumnList;
	private TimeSeriesDescription timeSeriesDescription;
	
	/**
	 * Instantiates a new expression data instance.
	 */
	public ExpressionData() { }
	
	// --------------------------------------------------------------
	// --- From here, constructors for simple value data types ------
	// --------------------------------------------------------------	
	/**
	 * Instantiates a new expression data instance with a single boolean.
	 * @param boolValue the boolean value
	 */
	public ExpressionData(boolean boolValue) { 
		this(null, boolValue);
	}
	/**
	 * Instantiates a new expression data instance with a single boolean.
	 *
	 * @param name the name of the value (can be <code>null</code>)
	 * @param boolValue the boolean value
	 */
	public ExpressionData(String name, boolean boolValue) { 
		this.addDataColumn(name, DataType.Boolean, 1).setColumnData(boolValue);
	}
	
	/**
	 * Instantiates a new expression data instance with a single int value.
	 * @param boolValue the int value
	 */
	public ExpressionData(int intValue) { 
		this(null, intValue);
	}
	/**
	 * Instantiates a new expression data instance with a single int value.
	 * @param name the name of the value (can be <code>null</code>)
	 * @param intValue the integer value
	 */
	public ExpressionData(String name, int intValue) { 
		this.addDataColumn(name, DataType.Integer, 1).setColumnData(intValue);
	}
	/**
	 * Instantiates a new expression data instance with a single double value.
	 * @param longValue the long value
	 */
	public ExpressionData(long longValue) { 
		this(null, longValue);
	}
	/**
	 * Instantiates a new expression data instance with a single long value.
	 * @param name the name of the value (can be <code>null</code>)
	 * @param longValue the long value
	 */
	public ExpressionData(String name, long longValue) { 
		this.addDataColumn(name, DataType.Long, 1).setColumnData(longValue);
	}
	/**
	 * Instantiates a new expression data instance with a single double value.
	 * @param boolValue the double value
	 */
	public ExpressionData(double doubleValue) { 
		this(null, doubleValue);
	}
	/**
	 * Instantiates a new expression data instance with a single double value.
	 * @param name the name of the value (can be <code>null</code>)
	 * @param boolValue the double value
	 */
	public ExpressionData(String name, double doubleValue) { 
		this.addDataColumn(name, DataType.Double, 1).setColumnData(doubleValue);
	}

	// --------------------------------------------------------------
	// --- From here, constructors for array value types ------------
	// --------------------------------------------------------------	
	/**
	 * Instantiates a new expression data instance with a single boolean array.
	 * @param boolArray the boolean array
	 */
	public ExpressionData(boolean boolArray[]) { 
		this(null, boolArray);
	}
	/**
	 * Instantiates a new expression data instance with a boolean array.
	 *
	 * @param name the name of the value (can be <code>null</code>)
	 * @param boolArray the boolean array
	 */
	public ExpressionData(String name, boolean boolArray[]) { 
		this.addDataColumn(name, DataType.Boolean, 1).setColumnData(boolArray);
	}
	
	/**
	 * Instantiates a new expression data instance with a single int array.
	 * @param intArray the int array
	 */
	public ExpressionData(int intValue[]) { 
		this(null, intValue);
	}
	/**
	 * Instantiates a new expression data instance with a single int value.
	 * @param name the name of the value (can be <code>null</code>)
	 * @param intArray the int array
	 */
	public ExpressionData(String name, int intArray[]) { 
		this.addDataColumn(name, DataType.Integer, 1).setColumnData(intArray);
	}
	/**
	 * Instantiates a new expression data instance with a single double array.
	 * @param doubleArray the double array
	 */
	public ExpressionData(double doubleArray[]) { 
		this(null, doubleArray);
	}
	/**
	 * Instantiates a new expression data instance with a single double array.
	 * @param name the name of the value (can be <code>null</code>)
	 * @param doubleArray the double array
	 */
	public ExpressionData(String name, double doubleArray[]) { 
		this.addDataColumn(name, DataType.Double, 1).setColumnData(doubleArray);
	}
	
	/**
	 * Instantiates a new expression data instance that serves as container for time series.
	 * @param tsd the {@link TimeSeriesDescription} that describes the expression data structure 
	 */
	public ExpressionData(TimeSeriesDescription tsd) {
		if (tsd==null) {
			throw new NullPointerException("The TimeSeriesDescription for defining the ExpressionData instance was Null");
		}
		this.timeSeriesDescription = tsd;
		this.initTimeSeriesStructure();
	}
	/**
	 * Returns the current TimeSeriesDescription, if defined.
	 * @return the time series description or <code>null</code>
	 */
	public TimeSeriesDescription getTimeSeriesDescription() {
		return timeSeriesDescription;
	}
	/**
	 * Builds up the time series structure.
	 */
	private void initTimeSeriesStructure() {
		
		// --- Add the time column ------------------------
		int initialListLength = this.getTimeSeriesDescription().getInitialListLength();
		DataColumn dcTime = new DataColumn(this.getTimeSeriesDescription().getTimeColumnName(), DataType.Long, new ArrayList<Long>(initialListLength));
		this.addDataColumn(dcTime);
		
		// --- Add data columns ---------------------------
		for (int i = 0; i < this.getTimeSeriesDescription().getNumberOfDataColumns(); i++) {
			
			String columnName = this.getTimeSeriesDescription().getDataColumnNames().get(i);
			DataType dataType = this.getTimeSeriesDescription().getDataTypes().get(i);
			
			Object dataContainer = null;
			switch (dataType) {
			case Boolean:
				dataContainer = new ArrayList<Boolean>(initialListLength);
				break;
			case Integer:
				dataContainer = new ArrayList<Integer>(initialListLength);
				break;
			case Long:
				dataContainer = new ArrayList<Long>(initialListLength);
				break;
			case Double:
				dataContainer = new ArrayList<Double>(initialListLength);
				break;
			}
			
			// --- Add as data column ---------------------
			DataColumn dcValue = new DataColumn(columnName, dataType, dataContainer);
			this.addDataColumn(dcValue);
		}
	}
	
	
	// --------------------------------------------------------------
	// --- From here, handling of DataColumns -----------------------
	// --------------------------------------------------------------	
	/**
	 * Returns the list of data columns currently defined.
	 * @return the data column list
	 */
	public List<DataColumn> getDataColumnList() {
		if (dataColumnList==null) {
			dataColumnList = new ArrayList<>();
		}
		return dataColumnList;
	}
	/**
	 * Returns the number of data columns currently defined.
	 * @return the data column count
	 */
	public int getDataColumnCount() {
		return this.getDataColumnList().size();
	}
 	
	/**
	 * Return the DataColumn specified by the column index.
	 *
	 * @param colIndex the column index
	 * @return the data column
	 */
	public DataColumn getDataColumn(int colIndex) {
		int nColumns = this.getDataColumnCount();
		if (nColumns==0 || colIndex > (nColumns-1) ) {
			throw new IndexOutOfBoundsException("Current number of DataColums is " + nColumns);
		}
		return this.getDataColumnList().get(colIndex);
	}
	/**
	 * Returns the DataColumn specified by its name.
	 *
	 * @param name the name of the DataColumn
	 * @return the data column or <code>null</code> if no corresponding DatColumn could be found
	 */
	public DataColumn getDataColumn(String name) {
		
		if (name==null || name.isBlank()==true) return null;
		for (DataColumn dc : this.getDataColumnList()) {
			if (dc.getColName()!=null && dc.getColName().equals(name)==true) {
				return dc;
			}
		}
		return null;
	}
	
	
	/**
	 * Adds the specified data type as new 'column' to the current {@link ExpressionData} instance.
	 * @param dataType the simple data type to create (boolean, int or double only)
	 */
	public DataColumn addDataColumn(DataType dataType) {
		return this.addDataColumn(null, dataType, 1);
	}
	/**
	 *  Adds the specified data type as new 'column' to the current {@link ExpressionData} instance.
	 *
	 * @param name the name of that column
	 * @param dataType the data type
	 */
	public DataColumn addDataColumn(String name, DataType dataType) {
		return this.addDataColumn(name, dataType, 1);
	}
	/**
	 *  Adds the specified data type as new 'column' to the current {@link ExpressionData} instance.
	 *
	 * @param name the name of that column
	 * @param dataType the data type
	 * @param rows the number of rows that should be
	 * @return the DataColumn instance that was added or <code>null</code> if nothing was added
	 */
	public DataColumn addDataColumn(String name, DataType dataType, int rows) {
		
		DataColumn dc = null;
		
		if (rows<=0) {
			throw new IllegalArgumentException("The rows definition value has to be greater or equal 1!");
		
		} else if (rows==1) {
			// --- Single value 'columns' -----------------
			switch (dataType) {
			case Boolean:
				boolean boolValue = false;
				dc = new DataColumn(name, dataType, boolValue);
				break;
			case Integer:
				int intValue = 0;
				dc = new DataColumn(name, dataType, intValue);
				break;
			case Long:
				long lngValue = 0;
				dc = new DataColumn(name, dataType, lngValue);
				break;
			case Double:
				double dblValue = 0.0;
				dc = new DataColumn(name, dataType, dblValue);
				break;
			}
			
		} else {
			// --- Definition of arrays -------------------
			switch (dataType) {
			case Boolean:
				boolean[] booleanArray = new boolean[rows];
				dc = new DataColumn(name, dataType, booleanArray);
				break;
			case Integer:
				int[] intArray = new int[rows];
				dc = new DataColumn(name, dataType, intArray);
				break;
			case Long:
				long[] lngArray = new long[rows];
				dc = new DataColumn(name, dataType, lngArray);
				break;
			case Double:
				double[] dblArray = new double[rows];
				dc = new DataColumn(name, dataType, dblArray);
				break;
			}
		}
		
		// --- Add the data column ------------------------
		if (this.addDataColumn(dc)==true) {
			return dc;
		}
		return null;
	}
	/**
	 *  Adds the specified data type as new 'column' to the current {@link ExpressionData} instance.
	 *
	 * @param dataColumn the data column to add
	 * @return true, if successful
	 */
	public boolean addDataColumn(DataColumn dataColumn) {
		if (dataColumn==null) return false;
		return this.getDataColumnList().add(dataColumn);
	}
	
	// ----------------------------------------------------------------------------------
	// --- From here, simplifying access methods for simple ExpressionData instances ----
	// ----------------------------------------------------------------------------------
	/**
	 * Returns if the current ExpressionData instance consist of a single data column result.
	 * @return true, if is single data column result
	 */
	public boolean isSingleDataColumnResult() {
		return this.getDataColumnList().size()==1;
	}
	/**
	 * Returns if the current ExpressionData instance is a single value result.
	 * @return true, if is single data column result
	 */
	public boolean isSingleValueResult() {
		return this.isSingleDataColumnResult()==true && this.isArray()==false;
	}
	/**
	 * If this ExpressionData instance was specified with as single {@link DataColumn}, this
	 * method returns, if the current value represents an array or not.
	 * @return true, if the value is an array or <code>null</code> if the current instance contains less or more that one data column
	 */
	public boolean isArray() {
		if (this.isSingleDataColumnResult()==false) return false;
		return this.getDataColumnList().get(0).isArray();
	}
	/**
	 * If this ExpressionData instance was specified with as single {@link DataColumn}, this
	 * method returns the specified {@link DataType}.
	 *
	 * @return the data type  or <code>null</code> if the current instance contains less or more that one data column
	 */
	public DataType getDataType() {
		if (this.isSingleDataColumnResult()==false) return null;
		return this.getDataColumnList().get(0).getDataType();
	}
	
	/**
	 * Returns the single boolean value if the class was initiated with as single boolean value.
	 * @return the boolean value or <code>null</code>
	 */
	public Boolean getBooleanValue() {
		if (this.isSingleDataColumnResult()==false) return null;
		return this.getDataColumnList().get(0).getBooleanValue();
	}
	/**
	 * Returns the single integer value if the class was initiated with as single int value.
	 * @return the Integer value or <code>null</code>
	 */
	public Integer getIntegerValue() {
		if (this.isSingleDataColumnResult()==false) return null;
		return this.getDataColumnList().get(0).getIntegerValue();
	}
	/**
	 * Returns the single long value if the class was initiated with as single long value.
	 * @return the Long value or <code>null</code>
	 */
	public Long getLongValue() {
		if (this.isSingleDataColumnResult()==false) return null;
		return this.getDataColumnList().get(0).getLongValue();
	}
	/**
	 * Returns the single Double value if the class was initiated with as single double value.
	 * @return the Double value or <code>null</code>
	 */
	public Double getDoubleValue() {
		if (this.isSingleDataColumnResult()==false) return null;
		return this.getDataColumnList().get(0).getDoubleValue();
	}
	
	
	/**
	 * Returns the boolean array if the class only contains as single boolean array.
	 * @return the boolean array or <code>null</code>
	 */
	public Boolean[] getBooleanArray() {
		if (this.isSingleDataColumnResult()==false) return null;
		return this.getDataColumnList().get(0).getBooleanArray();
	}
	/**
	 * Returns the integer array if the class only contains as single integer array.
	 * @return the integer array or <code>null</code>
	 */
	public Integer[] getIntegerArray() {
		if (this.isSingleDataColumnResult()==false) return null;
		return this.getDataColumnList().get(0).getIntegerArray();
	}
	/**
	 * Returns the Long array if the class only contains as single long array.
	 * @return the Long array or <code>null</code>
	 */
	public Long[] getLongArray() {
		if (this.isSingleDataColumnResult()==false) return null;
		return this.getDataColumnList().get(0).getLongArray();
	}
	/**
	 * Returns the Double array if the class only contains as single double array.
	 * @return the Double array or <code>null</code>
	 */
	public Double[] getDoubleArray() {
		if (this.isSingleDataColumnResult()==false) return null;
		return this.getDataColumnList().get(0).getDoubleArray();
	}
	
	// ----------------------------------------------------------------------------------
	// --- From here, handling of Time series -------------------------------------------
	// ----------------------------------------------------------------------------------
	/**
	 * Checks if the current data represents a time series.
	 * @return true, if this is time series
	 */
	public boolean isTimeSeries() {
		return this.getTimeSeriesDescription()!=null;
	}
	/**
	 * Checks if the current data represents a time series.
	 * @return true, if this is time series, Otherwise an {@link TimeSeriesException} will be thrown
	 * @throws TimeSeriesException 
	 */
	private boolean isTimeSeriesThrowException() {
		if (this.isTimeSeries()==true) return true;
		try {
			throw new TimeSeriesException("The current ExpressionData is not a time series (it is of type " + this.getDataTypeDescription() + ")");
		} catch (TimeSeriesException tsEx) {
			tsEx.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * Adds the specified data row.
	 *
	 * @param timestamp the timestamp
	 * @param cellValues the cell values
	 * @throws TimeSeriesException 
	 */
	public boolean addDataRow(long timestamp, Object ... cellValues) throws TimeSeriesException {
		return this.addDataRow(false, timestamp, cellValues);
	}
	/**
	 * Adds the specified data row to a time series.
	 *
	 * @param doOverwrite the indicator to overwrite already available data
	 * @param timestamp the timestamp of the data
	 * @param cellValues the cell values
	 * @return true, if successful
	 * @throws TimeSeriesException the time series exception
	 */
	public boolean addDataRow(boolean doOverwrite, long timestamp, Object ... cellValues) throws TimeSeriesException {
		
		if (this.isTimeSeriesThrowException()==false) return false;

		// --- Check time -------------------------------------------
		if (timestamp<=0) {
			throw new TimeSeriesException("To add a data row, the time needs to be specified!");
		} else {
			// --- Check if the time stamp is already available -----
			int destinRowIndexTime = this.getRowIndex(timestamp);
			if (destinRowIndexTime!=-1) {
				if (doOverwrite==false) {
					return false;
				} else {
					return this.setDataRow(destinRowIndexTime, timestamp, doOverwrite, cellValues);
				}
			}
		}
		
		// --- Check cellValues -------------------------------------
		if (cellValues==null) {
			throw new TimeSeriesException("No data was specified to be added to the time series!");
		} else if (cellValues.length!=this.getDataColumnCount()-1) {
			throw new TimeSeriesException("No number of data elements specified does not match the number of data columns!");
		}
		
		// --- Add the data -----------------------------------------
		this.getDataColumn(0).getLongList().add(timestamp);
		for (int col=0; col < cellValues.length; col++) {
			switch (this.getDataColumn(col+1).getDataType()) {
			case Boolean:
				this.getDataColumn(col+1).getBooleanList().add((Boolean)cellValues[col]);
				break;
			case Integer:
				this.getDataColumn(col+1).getIntegerList().add((Integer)cellValues[col]);
				break;
			case Long:
				this.getDataColumn(col+1).getLongList().add((Long)cellValues[col]);
				break;
			case Double:
				this.getDataColumn(col+1).getDoubleList().add((Double)cellValues[col]);
				break;
			}
		}
		return true;
	}
	
	/**
	 * Sets the specified data row to the specified index position.
	 *
	 * @param destinRowIndex the destination row index
	 * @param timestamp the timestamp
	 * @param cellValues the cell values
	 * @return true, if successful
	 * @throws TimeSeriesException the time series exception
	 */
	public boolean setDataRow(int destinRowIndex, long timestamp, Object ... cellValues) throws TimeSeriesException {
		return this.setDataRow(destinRowIndex, timestamp, false, cellValues);
	}
	/**
	 * Sets the specified data row to the specified index position.
	 *
	 * @param destinRowIndex the destination row index
	 * @param timestamp the timestamp of the data
	 * @param doOverwrite the indicator to overwrite already available data
	 * @param cellValues the cell values
	 * @return true, if successful
	 * @throws TimeSeriesException the time series exception
	 */
	public boolean setDataRow(int destinRowIndex, long timestamp, boolean doOverwrite, Object ... cellValues) throws TimeSeriesException {
		
		if (this.isTimeSeriesThrowException()==false) return false;

		// --- Check time -------------------------------------------
		if (destinRowIndex<=0) {
			throw new TimeSeriesException("To set a data row, the destination row index needs to be >= 0!");
		} 
		if (timestamp<=0) {
			throw new TimeSeriesException("To set a data row, the time needs to be specified!");
		}
		
		// --- Check cellValues -------------------------------------
		if (cellValues==null) {
			throw new TimeSeriesException("No data was specified to be set to the time series!");
		} else if (cellValues.length!=this.getDataColumnCount()-1) {
			throw new TimeSeriesException("No number of data elements specified does not match the number of data columns!");
		}
		
		// --- Add the data -----------------------------------------
		this.getDataColumn(0).getLongList().set(destinRowIndex, timestamp);
		for (int col=0; col < cellValues.length; col++) {
			switch (this.getDataColumn(col+1).getDataType()) {
			case Boolean:
				this.getDataColumn(col+1).getBooleanList().set(destinRowIndex, (Boolean)cellValues[col]);
				break;
			case Integer:
				this.getDataColumn(col+1).getIntegerList().set(destinRowIndex, (Integer)cellValues[col]);
				break;
			case Long:
				this.getDataColumn(col+1).getLongList().set(destinRowIndex, (Long)cellValues[col]);
				break;
			case Double:
				this.getDataColumn(col+1).getDoubleList().set(destinRowIndex, (Double)cellValues[col]);
				break;
			}
		}
		return true;
	}
	
	
	/**
	 * Returns the data row of a time series including the timestamp as first value.
	 *
	 * @param timestamp the timestamp
	 * @return the data row as Object array including the timestamp as first value.
	 */
	public Object[] getDataRow(long timestamp) {
		if (this.isTimeSeriesThrowException()==false) return null;
		return this.getDataRow(this.getRowIndex(timestamp));
	}
	/**
	 * Returns the data row for the specified row index.
	 *
	 * @param rowIndex the row index
	 * @return the data row as Object array including the timestamp as first value.
	 */
	public Object[] getDataRow(int rowIndex) {
		if (this.isTimeSeriesThrowException()==false) return null;
		if (rowIndex==-1) return null;
		
		Object[] dataRow = new Object[this.getDataColumnCount()];
		for (int col=0; col < this.getDataColumnCount(); col++) {
			dataRow[col] = this.getDataColumn(col).getList().get(rowIndex);
		}
		return dataRow;
	}
	
	/**
	 * Sets the value for the specified time in the specified column.
	 *
	 * @param timestamp the timestamp
	 * @param columnIndex the column index, where '0' represents the timestamp column
	 * @param value the value to set
	 * @return the element previously at the specified position
	 */
	public Object setValueAt(long timestamp, int columnIndex, Object value) {
		if (this.isTimeSeriesThrowException()==false) return false;
		return this.setValueAt(this.getRowIndex(timestamp), columnIndex, value);
	}
	/**
	 * Returns the value for the specified time in the specified column.
	 *
	 * @param timestamp the timestamp
	 * @param columnIndex the column index, where '0' represents the timestamp column
	 * @param value the value to set
	 * @return true, if successful
	 */
	public Object getValueAt(long timestamp, int columnIndex) {
		if (this.isTimeSeriesThrowException()==false) return null;
		return this.getValueAt(this.getRowIndex(timestamp), columnIndex);
	}
	
	/**
	 * Set the value for the specified row and column index.
	 *
	 * @param rowIndex the row index
	 * @param columnIndex the column index, where '0' represents the timestamp column
	 * @param value the value to set
	 * @return the element previously at the specified position
	 */
	public Object setValueAt(int rowIndex, int columnIndex, Object value) {
		
		if (this.isTimeSeriesThrowException()==false) return false;
		if (rowIndex==-1) return false;
		if (columnIndex<0 || columnIndex>this.getDataColumnCount()-1) return false;

		// --- For null values ----------------------------
		if (value==null) return this.getDataColumn(columnIndex).getList().set(rowIndex, null);
		
		// --- For none-null values -----------------------
		switch (this.getDataColumn(columnIndex).getDataType()) {
		case Boolean:
			return this.getDataColumn(columnIndex).getBooleanList().set(rowIndex, (Boolean)value);
		case Integer:
			return this.getDataColumn(columnIndex).getIntegerList().set(rowIndex, (Integer)value);
		case Long:
			return this.getDataColumn(columnIndex).getLongList().set(rowIndex, (Long)value);
		case Double:
			return this.getDataColumn(columnIndex).getDoubleList().set(rowIndex, (Double)value);
		} 
		return null;
	}
	
	/**
	 * Return the value at the specified row and column index..
	 *
	 * @param rowIndex the row index
	 * @param columnIndex the column index
	 * @return the value at
	 */
	public Object getValueAt(int rowIndex, int columnIndex ) {
		
		if (this.isTimeSeriesThrowException()==false) return null;
		if (rowIndex==-1) return null;
		if (columnIndex<0 || columnIndex>this.getDataColumnCount()-1) return null;
		return this.getDataColumn(columnIndex).getList().get(rowIndex);
	}
	
	/**
	 * Returns the row index for the specified timestamp.
	 *
	 * @param timeStamp the time stamp
	 * @return the row index or -1, if no value could be found for the timestamp
	 */
	public int getRowIndex(long timeStamp) {
		
		this.isTimeSeriesThrowException();
		
		List<Long> timeList = this.getDataColumn(0).getLongList();
		if (timeList==null) return -1;

		for (int i = 0; i < timeList.size(); i++) {
			Long timeValueList = timeList.get(i); 
			if (timeValueList!=null && timeValueList==timeStamp) {
				return i;
			}
		}
		return -1;
	}

	
	// ----------------------------------------------------------------------------------
	// --- From here, some description methods ------------------------------------------
	// ----------------------------------------------------------------------------------
	/**
	 * Returns the data type description.
	 * @return the data type description
	 */
	public String getDataTypeDescription() {
		
		String dtDesc = "";
		if (this.getDataColumnCount()==0) {
			// --- No result defined yet ------------------
			dtDesc = "No result type defined";
			
		} else if (this.getDataColumnCount()==1) {
			// --- For single column result ---------------
			dtDesc += this.getDataType().name();
			if (this.isArray()==true) dtDesc += " (Array)";
			
		} else {
			if (this.isTimeSeries()==true) {
				// --- Time series ------------------------
				dtDesc = "Time Series";
				
			} else {
				// --- For multi column results -----------
				dtDesc = "Multi-Column result";
				String dtList = " ("; 
				for (DataColumn dc : this.getDataColumnList()) {
					String dt = dc.getDataType().name();
					dtList += dtList.isEmpty()==true ? dt : ", " + dt;
				}
				dtList += ")";
				dtDesc += " " + dtList;
				
			}
			
		}
		return dtDesc;
	}
	
	/**
	 * Returns the data value description.
	 * @return the data value description
	 */
	public String getDataValueDescription() {
		
		String dvDesc = "";
		if (this.getDataColumnCount()==0) {
			// --- No result defined yet ------------------
			dvDesc = "No result defined";
			
		} else if (this.getDataColumnCount()==1) {
			// --- For single column result ---------------
			if (this.isArray()==false) {
				// --- Single value result ----------------
				switch (this.getDataType()) {
				case Boolean:
					dvDesc = this.getBooleanValue().toString();
					break;
				case Integer:
					dvDesc = this.getIntegerValue().toString();
					break;
				case Long:
					dvDesc = this.getLongValue().toString();
					break;
				case Double:
					dvDesc = this.getDoubleValue().toString();
					break;
				}
				
			} else {
				// --- Multiple value Result --------------
				switch (this.getDataType()) {
				case Boolean:
					dvDesc = this.getBooleanArray().toString();
					break;
				case Integer:
					dvDesc = this.getIntegerArray().toString();
					break;
				case Long:
					dvDesc = this.getLongArray().toString();
					break;
				case Double:
					dvDesc = this.getDoubleArray().toString();
					break;
				}				
			}
			
		} else {
			if (this.isTimeSeries()==true) {
				// --- Time series ------------------------
				dvDesc = "Time series";
				// TODO
				
			} else {
				// --- For multi column results -----------
				dvDesc = "Multi-Column result";
				String dtList = " ("; 
				for (DataColumn dc : this.getDataColumnList()) {
					String dt = dc.getDataType().name();
					dtList += dtList.isEmpty()==true ? dt : ", " + dt;
				}
				dtList += ")";
				dvDesc += " " + dtList;
				
			}
			
		}
		return dvDesc;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getDataTypeDescription() + ", " + this.getDataValueDescription();
	}
	
	
	// --------------------------------------------------------------
	// --- From here, class definition of DataCoumn -----------------
	// --------------------------------------------------------------	
	/**
	 * The Class DataColumn serves as description for a 'column' 
	 * in the current {@link ExpressionData} instance.
	 *
	 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
	 */
	public class DataColumn {
		
		private String colName;
		private DataType dataType;
		private Object colData;
		
		/**
		 * Instantiates a new data column.
		 *
		 * @param colName the column name
		 * @param dataType the data type
		 * @param data the column data instance 
		 */
		public DataColumn(String colName, DataType dataType, Object data) {
			this.setColName(colName);
			this.setColumnData(data);
			this.setDataType(dataType);
		}

		/**
		 * Returns the column name.
		 * @return the column name
		 */
		public String getColName() {
			return colName;
		}
		/**
		 * Sets the column name.
		 * @param colName the new column name
		 */
		public void setColName(String colName) {
			this.colName = colName;
		}
		
		/**
		 * Gets the data type.
		 * @return the data type
		 */
		public DataType getDataType() {
			return dataType;
		}
		/**
		 * Sets the data type.
		 * @param dataType the new data type
		 */
		public void setDataType(DataType dataType) {
			this.dataType = dataType;
		}
		
		/**
		 * Returns the column data.
		 * @return the column data
		 */
		public Object getColumnData() {
			return colData;
		}
		/**
		 * Sets the column data.
		 * @param colData the new column data
		 */
		public void setColumnData(Object colData) {
			this.colData = colData;
		}
		
		/**
		 * Returns the boolean value if the column data is a single boolean value.
		 * @return the boolean value or <code>null</code>
		 */
		public Boolean getBooleanValue() {
			if (this.getColumnData() instanceof Boolean) {
				return (Boolean) this.getColumnData();
			}
			return null;
		}
		/**
		 * Returns the Integer value if the column data is a single int value.
		 * @return the Integer value or <code>null</code>
		 */
		public Integer getIntegerValue() {
			if (this.getColumnData() instanceof Integer) {
				return (Integer) this.getColumnData();
			}
			return null;
		}
		/**
		 * Returns the Long value if the column data is a single long value.
		 * @return the Long value or <code>null</code>
		 */
		public Long getLongValue() {
			if (this.getColumnData() instanceof Long) {
				return (Long) this.getColumnData();
			}
			return null;
		}
		/**
		 * Returns the Double value if the column data is a single integer or a single double value.
		 * @return the Double value or <code>null</code>
		 */
		public Double getDoubleValue() {
			if (this.getColumnData() instanceof Integer || this.getColumnData() instanceof Double) {
				return (Double) this.getColumnData();
			}
			return null;
		}
		
		/**
		 * Checks if the current column data is an array.
		 * @return true, if is array
		 */
		public boolean isArray() {
			if (this.getColumnData()!=null && this.getColumnData().getClass().isArray()==true) {
				return true;
			}
			return false;
		}
		/**
		 * Returns the current column data into an object array.
		 * @return the object array
		 */
		private Object[] getObjectArray() {
			if (this.isArray()==true) {
				return (Object[]) this.getColumnData();
			}
			return null;
		}
		
		/**
		 * If the current column data is defined so, returns it as Boolean array.
		 * @return the boolean array
		 */
		public Boolean[] getBooleanArray() {
			Object[] objArray = this.getObjectArray();
			if (objArray!=null && objArray.length>0 && objArray[0] instanceof Boolean) {
				return (Boolean[]) objArray;
			}
			return null;
		}
		/**
		 * If the current column data is defined so, returns it as Integer array.
		 * @return the integer array
		 */
		public Integer[] getIntegerArray() {
			Object[] objArray = this.getObjectArray();
			if (objArray!=null && objArray.length>0 && objArray[0] instanceof Integer) {
				return (Integer[]) objArray;
			}
			return null;
		}
		/**
		 * If the current column data is defined so, returns it as Long array.
		 * @return the integer array
		 */
		public Long[] getLongArray() {
			Object[] objArray = this.getObjectArray();
			if (objArray!=null && objArray.length>0 && objArray[0] instanceof Long) {
				return (Long[]) objArray;
			}
			return null;
		}
		/**
		 * If the current column data is defined so, returns it as Double array.
		 * @return the double array
		 */
		public Double[] getDoubleArray() {
			Object[] objArray = this.getObjectArray();
			if (objArray!=null && objArray.length>0 && objArray[0] instanceof Double) {
				return (Double[]) objArray;
			}
			return null;
		}
		
		/**
		 * Checks if the local data instance is of type List.
		 * @return true, if it is a list
		 */
		public boolean isList() {
			return this.getColumnData() instanceof List<?>;
		}
		/**
		 * Returns the current column data into an object array.
		 * @return the object array
		 */
		private List<?> getList() {
			if (this.isList()==true) {
				return (List<?>) this.getColumnData();
			}
			return null;
		}
		
		/**
		 * If the current column data is defined so, returns it as List of Boolean.
		 * @return the boolean list
		 */
		@SuppressWarnings("unchecked")
		public List<Boolean> getBooleanList() {
			List<?> list = this.getList();
			if (list!=null && this.getDataType()==DataType.Boolean) {
				return (List<Boolean>) list;
			}
			return null;
		}
		/**
		 * If the current column data is defined so, returns it as List of Integer.
		 * @return the boolean list
		 */
		@SuppressWarnings("unchecked")
		public List<Integer> getIntegerList() {
			List<?> list = this.getList();
			if (list!=null && this.getDataType()==DataType.Integer) {
				return (List<Integer>) list;
			}
			return null;
		}
		/**
		 * If the current column data is defined so, returns it as List of Long.
		 * @return the boolean list
		 */
		@SuppressWarnings("unchecked")
		public List<Long> getLongList() {
			List<?> list = this.getList();
			if (list!=null && this.getDataType()==DataType.Long) {
				return (List<Long>) list;
			}
			return null;
		}
		/**
		 * If the current column data is defined so, returns it as List of Double.
		 * @return the boolean list
		 */
		@SuppressWarnings("unchecked")
		public List<Double> getDoubleList() {
			List<?> list = this.getList();
			if (list!=null && this.getDataType()==DataType.Double) {
				return (List<Double>) list;
			}
			return null;
		}
	} // end sub class

}
