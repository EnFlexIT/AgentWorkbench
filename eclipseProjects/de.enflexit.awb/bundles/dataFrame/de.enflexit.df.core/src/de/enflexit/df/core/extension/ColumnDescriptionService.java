package de.enflexit.df.core.extension;

import de.enflexit.df.core.workbook.DataWorkbook;

/**
 * The Interface ColumnDescriptionService.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public interface ColumnDescriptionService {

	/**
	 * Has to return the column description service name.
	 * @return the column description service name
	 */
	public String getColumnDescriptionServiceName();
	
	/**
	 * Has to return a column description for the specified table and column name.
	 *
	 * @param dataworkbook the data workbook for which the description is requested 
	 * @param tableName the table name
	 * @param columnName the column name
	 * @return the column description
	 */
	public ColumnDescription getColumnDescription(DataWorkbook dataWorkbook, String tableName, String columnName);
	
}
