package de.enflexit.expression;

import java.util.ArrayList;
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
		this.addDataType(name, DataType.Boolean, 1).setColumnData(boolValue);
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
		this.addDataType(name, DataType.Boolean, 1).setColumnData(intValue);
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
		this.addDataType(name, DataType.Boolean, 1).setColumnData(doubleValue);
	}

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
	 * Adds the specified data type as new 'column' to the current {@link ExpressionData} instance.
	 * @param dataType the simple data type to create (boolean, int or double only)
	 */
	public DataColumn addDataType(DataType dataType) {
		return this.addDataType(null, dataType, 1);
	}
	/**
	 *  Adds the specified data type as new 'column' to the current {@link ExpressionData} instance.
	 *
	 * @param name the name of that column
	 * @param dataType the data type
	 */
	public DataColumn addDataType(String name, DataType dataType) {
		return this.addDataType(name, dataType, 1);
	}
	/**
	 *  Adds the specified data type as new 'column' to the current {@link ExpressionData} instance.
	 *
	 * @param name the name of that column
	 * @param dataType the data type
	 * @param rows the number of rows that should be
	 * @return the DataColumn instance that was added or <code>null</code> if nothing was added
	 */
	public DataColumn addDataType(String name, DataType dataType, int rows) {
		
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

	/**
	 * Returns the single boolean value if the class was initiated with as single boolean value.
	 * @return the boolean value or <code>null</code>
	 */
	public Boolean getBooleanValue() {
		if (this.getDataColumnCount()!=1) return null;
		return this.getDataColumnList().get(0).getBooleanValue();
	}
	/**
	 * Returns the single integer value if the class was initiated with as single int value.
	 * @return the boolean value or <code>null</code>
	 */
	public Integer getIntegerValue() {
		if (this.getDataColumnCount()!=1) return null;
		return this.getDataColumnList().get(0).getIntegerValue();
	}
	/**
	 * Returns the single Double value if the class was initiated with as single double value.
	 * @return the boolean value or <code>null</code>
	 */
	public Double getDoubleValue() {
		if (this.getDataColumnCount()!=1) return null;
		return this.getDataColumnList().get(0).getDoubleValue();
	}
	
	
	
	
	
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
