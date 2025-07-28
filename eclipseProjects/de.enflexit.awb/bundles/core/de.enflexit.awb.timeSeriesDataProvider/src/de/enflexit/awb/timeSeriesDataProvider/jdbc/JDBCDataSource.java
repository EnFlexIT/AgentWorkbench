package de.enflexit.awb.timeSeriesDataProvider.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeriesConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSource;
import de.enflexit.awb.timeSeriesDataProvider.TimeValuePair;

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

	/**
	 * Gets the source configuration.
	 * @return the source configuration
	 */
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
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Connect to the database.
	 * @return true, if successful
	 */
	public boolean connectToDatabase() {
		boolean success = false;
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(this.sourceConfiguration.getJdbcURL());
			this.setConnectionState(ConnectionState.CONNECTED);
			success = true;
		} catch (SQLException e) {
			this.setConnectionState(ConnectionState.ERROR);
			System.err.println("[" + this.getClass().getSimpleName() + "] Connection could not be established!");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			this.setConnectionState(ConnectionState.ERROR);
			System.err.println("[" + this.getClass().getSimpleName() + "] Driver class not found");
			e.printStackTrace();
		}
		return success;
	}
	

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.AbstractDataSource#getValue(java.lang.String, long)
	 */
	@Override
	public Double getValue(String seriesName, long timestamp) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return null;
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
}
