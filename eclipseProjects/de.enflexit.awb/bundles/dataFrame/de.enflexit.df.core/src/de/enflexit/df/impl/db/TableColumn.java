package de.enflexit.df.impl.db;

import de.enflexit.common.StringHelper;

/**
 * The Class TableColumn.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class TableColumn {

	private String columnName;
	private String dataType;
	private int size;
	private boolean isNullable;
	
	
	/**
	 * Instantiates a new table column.
	 *
	 * @param columnName the column name
	 * @param dataType the data type
	 */
	public TableColumn(String columnName, String dataType, int size, boolean isNullable) {
		super();
		this.columnName = columnName;
		this.dataType = dataType;
		this.size = size;
		this.isNullable = isNullable;
	}

	/**
	 * Returns the column name.
	 * @return the column name
	 */
	public String getColumnName() {
		return columnName;
	}
	/**
	 * Sets the column name.
	 * @param columnName the new column name
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	/**
	 * Returns the data type.
	 * @return the data type
	 */
	public String getDataType() {
		return dataType;
	}
	/**
	 * Sets the data type.
	 * @param dataType the new data type
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	/**
	 * Returns the size.
	 * @return the size
	 */
	public int getSize() {
		return size;
	}
	/**
	 * Sets the size.
	 * @param size the new size
	 */
	public void setSize(int size) {
		this.size = size;
	}
	
	/**
	 * Checks if is nullable.
	 * @return true, if is nullable
	 */
	public boolean isNullable() {
		return isNullable;
	}
	/**
	 * Sets the nullable.
	 * @param isNullable the new nullable
	 */
	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object compObj) {
		
		if (compObj==null) return false;
		if (compObj==this) return true;
		
		if (compObj instanceof TableColumn tbCol) {
			if (StringHelper.isEqualString(tbCol.getColumnName(), this.getColumnName())==false) return false;
			if (StringHelper.isEqualString(tbCol.getDataType(), this.getDataType())==false) return false;
			return true;
		}
		return false;
	}
	
}
