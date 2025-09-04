package de.enflexit.awb.timeSeriesDataProvider.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeriesConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSource;
import de.enflexit.awb.timeSeriesDataProvider.TimeValuePair;
import de.enflexit.common.ServiceFinder;
import de.enflexit.db.hibernate.HibernateDatabaseService;

/**
 * This class implements a JDBC-based time series data source
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class JDBCDataSource extends AbstractDataSource {
	
	public enum ConnectionState{
		NOT_CONNECTED, CONNECTED, ERROR
	}
	
	private JDBCDataScourceConfiguration sourceConfiguration;
	
	private Connection connection;
	private ConnectionState connectionState;
	
	private HashMap<String, JDBCDataSeries> dataSeriesMap;
	
	private Long firstTimeStamp;
	private Long lastTimeStamp;
	
	/**
	 * Instantiates a new JDBC data source.
	 * @param sourceConfiguration the source configuration
	 */
	public JDBCDataSource(JDBCDataScourceConfiguration sourceConfiguration) {
		this.sourceConfiguration = sourceConfiguration;
		for(AbstractDataSeriesConfiguration seriesConfiguration : this.sourceConfiguration.getDataSeriesConfigurations()) {
			if (seriesConfiguration instanceof JDBCDataSeriesConfiguration) {
				JDBCDataSeries series = new JDBCDataSeries(this);
				series.setName(seriesConfiguration.getName());
				series.setDataColumn(((JDBCDataSeriesConfiguration)seriesConfiguration).getDataColumn());
				
				this.getDataSeriesMap().put(series.getName(), series);
			}
		}
		this.setConnectionState(ConnectionState.NOT_CONNECTED);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.AbstractDataSource#getSourceConfiguration()
	 */
	@Override
	public JDBCDataScourceConfiguration getSourceConfiguration() {
		return sourceConfiguration;
	}

	/**
	 * Sets the source configuration.
	 * @param sourceConfiguration the new source configuration
	 */
	public void setSourceConfiguration(JDBCDataScourceConfiguration sourceConfiguration) {
		this.sourceConfiguration = sourceConfiguration;
	}
	
	/**
	 * Gets the connection state.
	 * @return the connection state
	 */
	public ConnectionState getConnectionState() {
		return connectionState;
	}
	/**
	 * Sets the connection state.
	 * @param connectionState the new connection state
	 */
	public void setConnectionState(ConnectionState connectionState) {
		this.connectionState = connectionState;
	}
	
	/**
	 * Gets the database connection.
	 * @return The connection. Might be null if not connected yet.
	 * @throws SQLException 
	 */
	public Connection getConnection() {
		if (connection==null) {
			
			HibernateDatabaseService dbService = this.getDbService(this.sourceConfiguration.getDbmsName());
			if (dbService!=null) {
				Properties databaseSettings = this.getSourceConfiguration().getDatabaseSettings();
				connection = dbService.getDatabaseConnection(databaseSettings, new  Vector<String>(), false, false);
				this.setConnectionState(connection!=null ? ConnectionState.CONNECTED : ConnectionState.ERROR);
			}
		}
		return connection;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.AbstractDataSource#getValue(java.lang.String, long)
	 */
	@Override
	public Double getValue(String seriesName, long timestamp) {
		JDBCDataSeries dataSeries = this.getDataSeriesMap().get(seriesName);
		if (dataSeries!=null) {
			TimeValuePair tvp = dataSeries.getValueForTime(timestamp);
			if (tvp!=null) {
				return dataSeries.getValueForTime(timestamp).getValue();
			}
		} else {
			System.err.println("[" + this.getClass().getSimpleName() + "] No time series named " + seriesName + " found for data source " + this.getSourceConfiguration().getName());
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.AbstractDataSource#getDataSeriesValues(java.lang.String)
	 */
	@Override
	public List<Double> getDataSeriesValues(String seriesName) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.AbstractDataSource#getValuesForTimeRange(java.lang.String, long, long)
	 */
	@Override
	public List<TimeValuePair> getValuesForTimeRange(String seriesName, long timestampFrom, long timestampTo) {
		JDBCDataSeries dataSeries = this.getDataSeriesMap().get(seriesName);
		if (dataSeries!=null) {
			return dataSeries.getValuesForTimeRange(timestampFrom, timestampTo);
		} else {
			System.err.println("[" + this.getClass().getSimpleName() + "] No time series named " + seriesName + " found for data source " + this.getSourceConfiguration().getName());
			return null;
		}
	}

	/**
	 * Gets the data series map.
	 * @return the data series map
	 */
	private HashMap<String, JDBCDataSeries> getDataSeriesMap() {
		if (dataSeriesMap==null) {
			dataSeriesMap = new HashMap<String, JDBCDataSeries>();
		}
		return dataSeriesMap;
	}
	
	/**
	 * Adds a new {@link JDBCDataSeries} to this source.
	 * @param dataSeries the data series
	 */
	public void addDataSeries(JDBCDataSeries dataSeries) {
		this.getDataSeriesMap().put(dataSeries.getName(), dataSeries);
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.AbstractDataSource#getDataSeries(java.lang.String)
	 */
	@Override
	public JDBCDataSeries getDataSeries(String seriesName) {
		return this.getDataSeriesMap().get(seriesName);
	}

	/**
	 * Gets the {@link HibernateDatabaseService} implementation for the specified DBMS.
	 * @param dbmsName the dbms name
	 * @return the service implementation, null if not found
	 */
	private HibernateDatabaseService getDbService(String dbmsName) {
		List<HibernateDatabaseService> serviceList =  ServiceFinder.findServices(HibernateDatabaseService.class);
		for (HibernateDatabaseService dbService : serviceList) {
			if (dbService.getDatabaseSystemName().equals(dbmsName)) {
				return dbService;
			}
		}
		
		return null;
	}
	
	/**
	 * Gets the name of the database table or view for this data source.
	 * @return the database table
	 */
	public String getDatabaseTable() {
		return this.getSourceConfiguration().getTableName();
	}
	
	/**
	 * Gets the name of the date time column.
	 * @return the column name
	 */
	public String getDateTimeColumn() {
		return this.getSourceConfiguration().getDateTimeColumn();
	}
	
	/**
	 * Gets the number of rows for the table/view assigned to this data source.
	 * @return the number of rows
	 */
	public int getNumberOfRows() {
		int numOfEntries = 0;
		try {
			String querySQL = "SELECT COUNT(*) FROM " + this.getDatabaseTable();
			Statement statement = this.getConnection().createStatement();
			ResultSet resultSet = statement.executeQuery(querySQL);
			if (resultSet.next()) {
				numOfEntries = resultSet.getInt(1);
			} else {
				System.err.println("[" + this.getClass().getSimpleName() + "] The database query did not return any results!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return numOfEntries;
	}
	
	/**
	 * Gets the earliest timestamp from the tables date/time column.
	 * @return the first timestamp
	 */
	public long getFirstTimestamp() {
		if (this.firstTimeStamp==null) {
			if (this.getDateTimeColumn()!=null) {
				try {
					String querySQL = "SELECT MIN(" + this.getDateTimeColumn() + ") FROM " + this.getDatabaseTable();
					Statement statement = this.getConnection().createStatement();
					ResultSet resultSet = statement.executeQuery(querySQL);
					if (resultSet.next()) {
						this.firstTimeStamp = resultSet.getTimestamp(1).getTime();
					} else {
						System.err.println("[" + this.getClass().getSimpleName() + "] The database query did not return any results!");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (this.firstTimeStamp!=null) {
			return this.firstTimeStamp.longValue();
		} else {
			System.err.println("[" + this.getClass().getSimpleName() + "] Unable to determine the first time stamp for data source " + this.getName());
			return -1;
		}
	}
	
	/**
	 * Gets the latest timestamp from the tables date/time column.
	 * @return the last timestamp
	 */
	public long getLastTimestamp() {
		
		if (this.lastTimeStamp==null) {
			if (this.getDateTimeColumn()!=null) {
				try {
					String querySQL = "SELECT MAX(" + this.getDateTimeColumn() + ") FROM " + this.getDatabaseTable();
					Statement statement = this.getConnection().createStatement();
					ResultSet resultSet = statement.executeQuery(querySQL);
					if (resultSet.next()) {
						this.lastTimeStamp = resultSet.getTimestamp(1).getTime();
					} else {
						System.err.println("[" + this.getClass().getSimpleName() + "] The database query did not return any results!");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (this.lastTimeStamp!=null) {
			return this.lastTimeStamp.longValue();
		} else {
			System.err.println("[" + this.getClass().getSimpleName() + "] Unable to determine the last time stamp for data source " + this.getName());
			return -1;
		}
		
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.AbstractDataSource#isAvailable()
	 */
	@Override
	public boolean isAvailable() {
		return (this.getConnection()!=null);
	}

	
}
