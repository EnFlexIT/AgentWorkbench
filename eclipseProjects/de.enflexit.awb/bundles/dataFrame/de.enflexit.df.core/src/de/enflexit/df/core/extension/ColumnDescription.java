package de.enflexit.df.core.extension;

import java.util.ArrayList;
import java.util.List;

import de.enflexit.common.StringHelper;
import de.enflexit.df.core.dataSources.DefaultDataSource;
import de.enflexit.df.core.workbook.DataWorkbook;

/**
 * The Class ColumnDescription.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class ColumnDescription {

	private DataWorkbook dataWorkbook;
	private DefaultDataSource dataSource;
	private String tableName;
	private String columnName;
	private String columnType;
	
	/**
	 * Instantiates a new column description.
	 */
	public ColumnDescription() { }
	
	/**
	 * Instantiates a new column description.
	 *
	 * @param dataWorkbook the data workbook
	 * @param dataSource the data source
	 * @param tableName the table name
	 * @param columnName the column name
	 * @param columnType the column type
	 */
	public ColumnDescription(DataWorkbook dataWorkbook, DefaultDataSource dataSource, String tableName, String columnName, String columnType) {
		this.dataWorkbook = dataWorkbook;
		this.dataSource = dataSource;
		this.tableName = tableName;
		this.columnName = columnName;
		this.columnType = columnType;
	}

	/**
	 * Returns the data workbook.
	 * @return the data workbook
	 */
	public DataWorkbook getDataWorkbook() {
		return dataWorkbook;
	}
	/**
	 * Sets the data workbook.
	 * @param dataWorkbook the new data workbook
	 */
	public void setDataWorkbook(DataWorkbook dataWorkbook) {
		this.dataWorkbook = dataWorkbook;
	}
	
	/**
	 * Returns the data source.
	 * @return the data source
	 */
	public DefaultDataSource getDataSource() {
		return dataSource;
	}
	/**
	 * Sets the data source.
	 * @param dataSource the new data source
	 */
	public void setDataSource(DefaultDataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	/**
	 * Returns the table name.
	 * @return the table name
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * Sets the table name.
	 * @param tableName the new table name
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
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
	 * Returns the column type.
	 * @return the column type
	 */
	public String getColumnType() {
		return columnType;
	}
	/**
	 * Sets the column type.
	 * @param columnType the new column type
	 */
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	
	/**
	 * Returns the default description.
	 * @return the default description
	 */
	protected final String getDefaultDescription() {
		
		List<String> descriptionPartList = new ArrayList<>();
		
		descriptionPartList.add("Data-Workbook: " + this.getDataWorkbook().getName());
		descriptionPartList.add("\nData Source: " + this.getDataSource().getName());

		if (this.getTableName()!=null && this.getTableName().isBlank()==false) {
			descriptionPartList.add("\nTable: " + this.getTableName());
		}
		descriptionPartList.add("\nColumn: " + this.getColumnName() + " (" + this.getColumnType() + ")");
		
		return String.join(", ", descriptionPartList);
	}
	/**
	 * Returns the textual description of the current {@link ColumnDescription}. 
	 * Maybe overwritten to provide individual or extended information  
	 * @return the description
	 */
	public String getDescription() {
		return this.getDefaultDescription();
	}
	/**
	 * Returns the tool tip.
	 * @return the tool tip
	 */
	public String getToolTip() {
		return this.getDefaultDescription();
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getDescription();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (obj==null) return false;
		if (obj==this) return true;
		
		if (obj instanceof ColumnDescription cDescComp) {
			if (this.getDataWorkbook().equals(cDescComp.getDataWorkbook())==false) return false;
			if (StringHelper.isEqualString(this.getTableName(), cDescComp.getTableName())==false) return false; 
			if (StringHelper.isEqualString(this.getColumnName(), cDescComp.getColumnName())==false) return false;
			return true;
		}
		return false;
	}
	
	
}
