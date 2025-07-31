package de.enflexit.awb.timeSeriesDataProvider.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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
	private String dataColumn;
	
	private PreparedStatement selectSingleValueStatement;
	private PreparedStatement selectValueRangeStatement;
	
	/**
	 * Instantiates a new JDBC data series.
	 * @param dataSource the corresponding data source
	 */
	public JDBCDataSeries(JDBCDataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * Gets the value for a specified point in time.
	 * @param timestamp the timestamp
	 * @return the value
	 */
	public Double getSingleValue(long timestamp) {
		try {
			Timestamp sqlTimestamp = new Timestamp(timestamp);
			this.getSelectSingleValueStatement().setTimestamp(1, sqlTimestamp);
			if (this.getSelectSingleValueStatement().execute()==true) {
				ResultSet resultSet = this.getSelectSingleValueStatement().getResultSet();
				if (resultSet.next()==true) {
					double value = resultSet.getDouble(this.getDataColumn());
					return value;
				} else {
					System.out.println("[" + this.getClass().getSimpleName() + "] No value found for the provided timestamp");
				}
			} else {
				System.err.println("[" + this.getClass().getSimpleName() + "] The database query returned no result!");
			}
		} catch (SQLException e) {
			System.err.println("[" +this.getClass().getSimpleName() + "] An error occured when execuing the database query!");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Gets the value pairs.
	 * @param from the from
	 * @param to the to
	 * @return the value pairs
	 */
	public ArrayList<TimeValuePair> getValuesForTimeRange(long from, long to){
		
		ArrayList<TimeValuePair> resultList = null;
		
		try {
			Timestamp timestampFrom = new Timestamp(from);
			Timestamp timestampTo = new Timestamp(to);
			this.getSelectValueRangeStatement().setTimestamp(1, timestampFrom);
			this.getSelectValueRangeStatement().setTimestamp(2, timestampTo);
			
			if (this.getSelectValueRangeStatement().execute()==true) {
				ResultSet resultSet = this.getSelectValueRangeStatement().getResultSet();
				resultList = new ArrayList<TimeValuePair>();
				while (resultSet.next()) {
					Timestamp timestamp = resultSet.getTimestamp(this.getDataSource().getDateTimeColumn());
					double value = resultSet.getDouble(this.getDataColumn());
					
					resultList.add(new TimeValuePair(timestamp.getTime(), value));
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
		if (selectSingleValueStatement==null) {
			String statementString = "SELECT " + this.getDataColumn() + " FROM " + this.getDatabaseTable() + " WHERE " + this.getDateTimeColumn() + " = ?";
			selectSingleValueStatement = this.getDataSource().getConnection().prepareStatement(statementString);
		}
		return selectSingleValueStatement;
	}
	
	/**
	 * Gets a {@link PreparedStatement} to query a range of values.
	 * @return the statement
	 * @throws SQLException the SQL exception
	 */
	private PreparedStatement getSelectValueRangeStatement() throws SQLException {
		if (selectValueRangeStatement==null) {
			String statementString = "SELECT " + this.getDateTimeColumn() + ", " + this.getDataColumn() + " FROM " + this.getDatabaseTable() + " WHERE " + this.getDateTimeColumn() + " BETWEEN ? and ? ";
			selectValueRangeStatement = this.getDataSource().getConnection().prepareStatement(statementString);
		}
		return selectValueRangeStatement;
	}
	
}
