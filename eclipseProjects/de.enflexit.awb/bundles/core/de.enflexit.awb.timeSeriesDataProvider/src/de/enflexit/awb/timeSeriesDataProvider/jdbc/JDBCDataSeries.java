package de.enflexit.awb.timeSeriesDataProvider.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeries;
import de.enflexit.awb.timeSeriesDataProvider.TimeSeriesDataProvider;
import de.enflexit.awb.timeSeriesDataProvider.TimeValuePair;

/**
 * This class implements a JDBC-based data series for the {@link TimeSeriesDataProvider}.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class JDBCDataSeries extends AbstractDataSeries {
	
	private JDBCDataSource dataSource;
	
	private String name;
	private String dataColumn;
	
	private PreparedStatement statementSelectSingleValue;
	private PreparedStatement statementSelectValueRange;
	private PreparedStatement statementSelectClosestValueBefore;
	private PreparedStatement statementSelectClosestValueAfter;
	
	private HashMap<Long, TimeValuePair> valueCache;
	
	/**
	 * Instantiates a new JDBC data series.
	 * @param dataSource the corresponding data source
	 */
	public JDBCDataSeries(JDBCDataSource dataSource) {
		this.dataSource = dataSource;
	}

	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeries#getValue(long)
	 */
	@Override
	public TimeValuePair getValueForTime(long timestamp) {
		
		// --- Check the value cache first --------------------------
		TimeValuePair tvp = this.getValueCache().get(timestamp);
		
		// --- If no cached value was found, get it from the database
		if (tvp==null) {
			
			// --- Check if the series contains data for the specified time stamp
			if (timestamp<this.getDataSource().getFirstTimestamp() || timestamp>this.getDataSource().getLastTimestamp()) {
				System.err.println("[" + this.getClass().getSimpleName() + "] The specified timestamp is outside the time range covered by this data series!");
				return null;
			} 
			
			// --- Query the database for the requested value
			try {
				Timestamp sqlTimestamp = new Timestamp(timestamp);
				this.getSelectSingleValueStatement().setTimestamp(1, sqlTimestamp);
				if (this.getSelectSingleValueStatement().execute()==true) {
					ResultSet resultSet = this.getSelectSingleValueStatement().getResultSet();
					if (resultSet.next()==true) {
						// --- Exact match found
						long resultTime = resultSet.getTimestamp(this.getDateTimeColumn()).getTime();
						double resultValue = resultSet.getDouble(this.getDataColumn());
						tvp = new TimeValuePair(resultTime, resultValue);
					} else {
						// --- No exact match, get closest earlier value
						TimeValuePair nextEarlierValue = this.getClosestValueBefore(sqlTimestamp);
						if (nextEarlierValue!=null) {
							tvp = this.getClosestValueBefore(sqlTimestamp);
						}
					}
					
					// --- Remember the value for faster access
					if (tvp!=null) {
						this.getValueCache().put(timestamp, tvp);
					} else {
						System.err.println("[" + this.getClass().getSimpleName() + "] No value found for the specified time!");
					}
				} else {
					System.err.println("[" + this.getClass().getSimpleName() + "] The database query returned no result!");
				}
				
			} catch (SQLException e) {
				System.err.println("[" +this.getClass().getSimpleName() + "] An error occured when execuing the database query!");
				e.printStackTrace();
			}
		}
		return tvp;
	}
	
	/**
	 * Gets the closest value before before a specified time.
	 * @param timestamp the timestamp
	 * @return The closest value before the specified timestamp. May be null if there is no such value.
	 * @throws SQLException the SQL exception
	 */
	private TimeValuePair getClosestValueBefore(Timestamp timestamp) throws SQLException {
		TimeValuePair tvp = null;
		this.getStatementSelectClosestValueBefore().setTimestamp(1, timestamp);
		if (this.getStatementSelectClosestValueBefore().execute()==true) {
			ResultSet resultSet = this.getStatementSelectClosestValueBefore().getResultSet(); 
			if (resultSet.next()==true) {
				// --- Next earlier value
				long resultTime = resultSet.getTimestamp(this.getDateTimeColumn()).getTime();
				double resultValue = resultSet.getDouble(this.getDataColumn());
				tvp = new TimeValuePair(resultTime, resultValue);
			} else {
				System.out.println("[" + this.getClass().getSimpleName() + "] No value found for the provided timestamp");
			}
		}
		
		return tvp;
	}
	
	/**
	 * Gets the closest value before a specified time.
	 * @param timestamp the timestamp
	 * @return The closest value after the specified timestamp. May be null if there is no such value.
	 * @throws SQLException the SQL exception
	 */
	private TimeValuePair getClosestValueAfter(Timestamp timestamp) throws SQLException {
		TimeValuePair tvp = null;
		this.getStatementSelectClosestValueAfter().setTimestamp(1, timestamp);
		if (this.getStatementSelectClosestValueAfter().execute()==true) {
			ResultSet resultSet = this.getStatementSelectClosestValueAfter().getResultSet(); 
			if (resultSet.next()==true) {
				// --- Next later value
				long resultTime = resultSet.getTimestamp(this.getDateTimeColumn()).getTime();
				double resultValue = resultSet.getDouble(this.getDataColumn());
				tvp = new TimeValuePair(resultTime, resultValue);
			} else {
				System.out.println("[" + this.getClass().getSimpleName() + "] No value found for the provided timestamp");
			}
		}
		
		return tvp;
	}
	
	/**
	 * Gets the value cache.
	 * @return the value cache
	 */
	private HashMap<Long, TimeValuePair> getValueCache() {
		if (valueCache==null) {
			valueCache = new HashMap<Long, TimeValuePair>();
		}
		return valueCache;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeries#getValuesForTimeRange(long, long)
	 */
	@Override
	public ArrayList<TimeValuePair> getValuesForTimeRange(long timeFrom, long timeTo){
		
		ArrayList<TimeValuePair> resultList = null;
		
		try {
			
			// --- Set the query parameters to define the time range ----------
			Timestamp timestampFrom = new Timestamp(timeFrom);
			Timestamp timestampTo = new Timestamp(timeTo);
			this.getSelectValueRangeStatement().setTimestamp(1, timestampFrom);
			this.getSelectValueRangeStatement().setTimestamp(2, timestampTo);
			
			// --- Execute the query, process the results ---------------------
			if (this.getSelectValueRangeStatement().execute()==true) {
				ResultSet resultSet = this.getSelectValueRangeStatement().getResultSet();
				resultList = new ArrayList<TimeValuePair>();
				while (resultSet.next()) {
					Timestamp timestamp = resultSet.getTimestamp(this.getDataSource().getDateTimeColumn());
					double value = resultSet.getDouble(this.getDataColumn());
					resultList.add(new TimeValuePair(timestamp.getTime(), value));
				}
				
				// --- Check if the time range boundaries are actually part of the result
				TimeValuePair firstEntry = resultList.get(0);
				if (firstEntry.getTimestamp()>timeFrom) {
					// --- Add the next earlier value to cover the whole time range
					TimeValuePair nextEarlierValue = this.getClosestValueBefore(timestampFrom);
					if (nextEarlierValue!=null) {
						resultList.add(0, nextEarlierValue);
					}
				}
				
				TimeValuePair lastEntry = resultList.get(resultList.size()-1);
				if (lastEntry.getTimestamp()<timeTo) {
					// --- Add the next later value to cover the whole time range
					TimeValuePair nextLaterEntry = this.getClosestValueAfter(timestampTo);
					if (nextLaterEntry!=null) {
						resultList.add(nextLaterEntry);
					}
				}
				
				
			} else {
				System.err.println("[" + this.getClass().getSimpleName() + "] The database query returned no result!");
			}
			
		} catch (SQLException e) {
			System.err.println("[" +this.getClass().getSimpleName() + "] An error occured when execuing the database query!");
			e.printStackTrace();
		}
		return resultList;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeries#getName()
	 */
	@Override
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
	 * Gets the parent data source.
	 * @return the data source
	 */
	public JDBCDataSource getDataSource() {
		return dataSource;
	}

	/**
	 * Sets the parent data source.
	 * @param dataSource the new data source
	 */
	public void setDataSource(JDBCDataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	/**
	 * Gets the name of the database table or view.
	 * @return the database table
	 */
	private String getDatabaseTable() {
		return this.getDataSource().getDatabaseTable();
	}
	
	/**
	 * Gets the name of the date time column.
	 * @return the date time column
	 */
	private String getDateTimeColumn() {
		return this.getDataSource().getDateTimeColumn();
	}

	/**
	 * Gets a {@link PreparedStatement} to query a single value.
	 * @return the statement
	 * @throws SQLException the SQL exception
	 */
	private  PreparedStatement getSelectSingleValueStatement() throws SQLException {
		if (statementSelectSingleValue==null) {
			String statementString = "SELECT " + this.getDateTimeColumn() + ", " + this.getDataColumn() + " FROM " + this.getDatabaseTable() + " WHERE " + this.getDateTimeColumn() + " = ?";
			statementSelectSingleValue = this.getDataSource().getConnection().prepareStatement(statementString);
		}
		return statementSelectSingleValue;
	}
	
	/**
	 * Gets a {@link PreparedStatement} to select closest value before a specified time.
	 * @return the statement
	 * @throws SQLException the SQL exception
	 */
	private PreparedStatement getStatementSelectClosestValueBefore() throws SQLException {
		if (statementSelectClosestValueBefore==null) {
			String statementString = "SELECT " + this.getDateTimeColumn() + ", " + this.getDataColumn() + " FROM " + this.getDatabaseTable() + " WHERE " + this.getDateTimeColumn() + " < ? ORDER BY " + this.getDateTimeColumn() + " DESC LIMIT 1";
			statementSelectClosestValueBefore = this.getDataSource().getConnection().prepareStatement(statementString);
		}
		return statementSelectClosestValueBefore;
	}
	
	/**
	 * Gets a {@link PreparedStatement} to select closest value after a specified time.
	 * @return the statement
	 * @throws SQLException the SQL exception
	 */
	private PreparedStatement getStatementSelectClosestValueAfter() throws SQLException {
		if (statementSelectClosestValueAfter==null) {
			String statementString = "SELECT " + this.getDateTimeColumn() + ", " + this.getDataColumn() + " FROM " + this.getDatabaseTable() + " WHERE " + this.getDateTimeColumn() + " > ? ORDER BY " + this.getDateTimeColumn() + " ASC LIMIT 1";
			statementSelectClosestValueAfter = this.getDataSource().getConnection().prepareStatement(statementString);
		}
		return statementSelectClosestValueAfter;
	}
	
	/**
	 * Gets a {@link PreparedStatement} to query a range of values.
	 * @return the statement
	 * @throws SQLException the SQL exception
	 */
	private PreparedStatement getSelectValueRangeStatement() throws SQLException {
		if (statementSelectValueRange==null) {
			String statementString = "SELECT " + this.getDateTimeColumn() + ", " + this.getDataColumn() + " FROM " + this.getDatabaseTable() + " WHERE " + this.getDateTimeColumn() + " BETWEEN ? and ? ORDER BY " + this.getDateTimeColumn() + " ASC";
			statementSelectValueRange = this.getDataSource().getConnection().prepareStatement(statementString);
		}
		return statementSelectValueRange;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeries#getFirstTimeStamp()
	 */
	@Override
	public long getFirstTimeStamp() {
		long firstTimeLong = 0;
		String statementString = "SELECT MIN(" + this.getDateTimeColumn() + ") FROM " + this.getDatabaseTable();
		try {
			ResultSet queryResult = this.executeQuery(statementString);
			if (queryResult!=null && queryResult.next()) {
				Timestamp firstTimeStamp = queryResult.getTimestamp(1);
				firstTimeLong = firstTimeStamp.getTime();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return firstTimeLong;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeries#getLastTimeStamp()
	 */
	@Override
	public long getLastTimeStamp() {
		long lastTimeLong = 0;
		String statementString = "SELECT MAX(" + this.getDateTimeColumn() + ") FROM " + this.getDatabaseTable();
		try {
			ResultSet queryResult = this.executeQuery(statementString);
			if (queryResult!=null && queryResult.next()) {
				Timestamp lastTimeStamp = queryResult.getTimestamp(1);
				lastTimeLong = lastTimeStamp.getTime();
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lastTimeLong;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeries#getNumberOfEntries()
	 */
	@Override
	public int getNumberOfEntries() {
		int numEntries = 0;
		String statementString = "SELECT COUNT(*) FROM " + this.getDatabaseTable();
		try {
			ResultSet queryResult = this.executeQuery(statementString);
			if (queryResult!=null && queryResult.next()) {
				numEntries = queryResult.getInt(1);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return numEntries;
	}
	
	/**
	 * Executes the specified query.
	 * @param queryString the query to execute
	 * @return the result
	 * @throws SQLException in case of database-related error
	 */
	private ResultSet executeQuery(String queryString) throws SQLException {
		PreparedStatement queryStatement = this.getDataSource().getConnection().prepareStatement(queryString);
		if (queryStatement.execute()==true) {
			return queryStatement.getResultSet();
		} else {
			return null;
		}
		
	}
	
}
