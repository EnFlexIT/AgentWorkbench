package de.enflexit.expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Enumeration ExpressionDataType specifies all data types that 
 * can be handled within the {@link ExpressionService}.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ExpressionData {
	
	public enum DataType {
		Boolean,
		Integer,
		Double,
	}
	
	private List<DataColumn> dataColumnList;
	
	
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
	 * @param boolValue the int value
	 */
	public ExpressionData(String name, int intValue) { 
		this.addDataColumn(name, DataType.Boolean, 1).setColumnData(intValue);
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
		this.addDataColumn(name, DataType.Boolean, 1).setColumnData(doubleValue);
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
		this.addDataColumn(name, DataType.Boolean, 1).setColumnData(intArray);
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
		this.addDataColumn(name, DataType.Boolean, 1).setColumnData(doubleArray);
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
			case Double:
				double[] dblArray = new double[rows];
				dc = new DataColumn(name, dataType, dblArray);
				break;
			}
		}
		
		// --- Add the data column ------------------------
		if (dc!=null) {
			this.getDataColumnList().add(dc);
		}
		return dc;
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
	 * If this ExpressionData instance was specified with as single {@link DataColumn}, this
	 * method returns, if the current value represents an array or not.
	 * @return true, if the value is an array or <code>null</code> if the current instance contains less or more that one data column
	 */
	public Boolean isArray() {
		if (this.isSingleDataColumnResult()==false) return null;
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
	 * @return the boolean value or <code>null</code>
	 */
	public Integer getIntegerValue() {
		if (this.isSingleDataColumnResult()==false) return null;
		return this.getDataColumnList().get(0).getIntegerValue();
	}
	/**
	 * Returns the single Double value if the class was initiated with as single double value.
	 * @return the boolean value or <code>null</code>
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
	 * @return the boolean array or <code>null</code>
	 */
	public Integer[] getIntegerArray() {
		if (this.isSingleDataColumnResult()==false) return null;
		return this.getDataColumnList().get(0).getIntegerArray();
	}
	/**
	 * Returns the Double array if the class only contains as single double array.
	 * @return the boolean array or <code>null</code>
	 */
	public Double[] getDoubleArray() {
		if (this.isSingleDataColumnResult()==false) return null;
		return this.getDataColumnList().get(0).getDoubleArray();
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
		private Object colData;
		private DataType dataType;
		
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
			if (objArray!=null) {
				return Arrays.asList(objArray).toArray(new Boolean[objArray.length]);
			}
			return null;
		}
		/**
		 * If the current column data is defined so, returns it as Integer array.
		 * @return the integer array
		 */
		public Integer[] getIntegerArray() {
			Object[] objArray = this.getObjectArray();
			if (objArray!=null) {
				return Arrays.asList(objArray).toArray(new Integer[objArray.length]);
			}
			return null;
		}
		/**
		 * If the current column data is defined so, returns it as Double array.
		 * @return the double array
		 */
		public Double[] getDoubleArray() {
			Object[] objArray = this.getObjectArray();
			if (objArray!=null) {
				return Arrays.asList(objArray).toArray(new Double[objArray.length]);
			}
			return null;
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
	}
	
}
