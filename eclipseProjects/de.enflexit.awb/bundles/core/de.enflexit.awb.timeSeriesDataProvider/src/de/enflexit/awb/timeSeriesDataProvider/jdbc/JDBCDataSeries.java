package de.enflexit.awb.timeSeriesDataProvider.jdbc;

import java.util.ArrayList;

import de.enflexit.awb.timeSeriesDataProvider.TimeSeriesDataProvider;
import de.enflexit.awb.timeSeriesDataProvider.TimeValuePair;

/**
 * This class implements a JDBC-based data series for the {@link TimeSeriesDataProvider}.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class JDBCDataSeries {
	
	private JDBCDataSource dataSource;
	
	private String name;
	private String databaseTable;
	private String dataColumn;
	
	/**
	 * Instantiates a new JDBC data series.
	 * @param dataSource the corresponding data source
	 */
	public JDBCDataSeries(JDBCDataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Gets the value pair for a specified point in time.
	 * @param timestamp the timestamp
	 * @return the value pair
	 */
	public TimeValuePair getValuePair(long timestamp) {
		return null;
	}
	
	/**
	 * Gets the value pairs.
	 * @param from the from
	 * @param to the to
	 * @return the value pairs
	 */
	public ArrayList<TimeValuePair> getValuePairs(long from, long to){
		return null;
	}
	
	/**
	 * Gets the series name.
	 * @return the series name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets the series name.
	 * @param seriesnName the new series name
	 */
	public void setName(String seriesnName) {
		this.name = seriesnName;
	}

	/**
	 * Gets the database table.
	 * @return the database table
	 */
	public String getDatabaseTable() {
		return databaseTable;
	}

	/**
	 * Sets the database table.
	 * @param databaseTable the new database table
	 */
	public void setDatabaseTable(String databaseTable) {
		this.databaseTable = databaseTable;
	}

	/**
	 * Gets the data column.
	 * @return the data column
	 */
	public String getDataColumn() {
		return dataColumn;
	}

	/**
	 * Sets the data column.
	 * @param dataColumn the new data column
	 */
	public void setDataColumn(String dataColumn) {
		this.dataColumn = dataColumn;
	}

	/**
	 * Creates an sql query for a single value.
	 * @param timestamp the timestamp
	 * @return the string
	 */
	private String createSqlQueryForSingleValue(long timestamp) {
		return null;
	}
	
	/**
	 * Creates an SQL query for a time range.
	 * @param timeFrom the first time stamp to include
	 * @param timeTo the last time stamp to include
	 * @return the string
	 */
	private String createSqlQueryForTimeRange(long timeFrom, long timeTo) {
		return null;
	}
}
