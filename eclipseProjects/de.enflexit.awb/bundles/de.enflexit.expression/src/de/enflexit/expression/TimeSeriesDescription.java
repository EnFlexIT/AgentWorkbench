package de.enflexit.expression;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import de.enflexit.expression.ExpressionData.DataType;

/**
 * The Class TimeSeriesDescription can be used to produce {@link ExpressionData}
 * that represent a Time Series with different data column types.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class TimeSeriesDescription {

	private ZoneId zoneID;
	private String timeColumnName;
	
	private List<String> dataColumnNames; 
	private List<DataType> dataTypes;

	
	/**
	 * Instantiates a new time series description.
	 */
	public TimeSeriesDescription() {
		this(null, null);
	}
	/**
	 * Instantiates a new time series description.
	 *
	 * @param timeColumnName the time column name
	 * @param zoneID the zone ID
	 */
	public TimeSeriesDescription(String timeColumnName, ZoneId zoneID) {
		this.setTimeColumnName(timeColumnName);
		this.setZoneID(zoneID);
	}
	
	/**
	 * Returns the time column name.
	 * @return the time column name
	 */
	public String getTimeColumnName() {
		if (this.timeColumnName==null || timeColumnName.isBlank()==true) {
			return "Time";
		}
		return timeColumnName;
	}
	/**
	 * Sets the time column name.
	 * @param timeColumName the new time column name
	 */
	public void setTimeColumnName(String timeColumName) {
		this.timeColumnName = timeColumName;
	}
	
	/**
	 * Gets the zone ID.
	 * @return the zone ID
	 */
	public ZoneId getZoneID() {
		if (zoneID==null) {
			zoneID = ZoneId.systemDefault();
		}
		return zoneID;
	}
	/**
	 * Sets the zone ID.
	 * @param zoneID the new zone ID
	 */
	public void setZoneID(ZoneId zoneID) {
		this.zoneID = zoneID;
	}
	
	/**
	 * Adds the data column.
	 *
	 * @param dataColumnName the data column name
	 * @param dataType the corresponding data type
	 */
	public void addDataColumn(String dataColumnName, DataType dataType) {
		
		if (dataColumnName==null || dataColumnName.isBlank()==true) {
			throw new IllegalArgumentException("The data column is not allowed to be null");
		}
		if (dataType==null) {
			throw new NullPointerException("The data type is not allowed to be null");
		}
		
		this.getDataColumnNames().add(dataColumnName);
		this.getDataTypes().add(dataType);
	}
	
	/**
	 * Returns the data column names.
	 * @return the data column names
	 */
	public List<String> getDataColumnNames() {
		if (dataColumnNames==null) {
			dataColumnNames = new ArrayList<>();
		}
		return dataColumnNames;
	}

	/**
	 * Returns the data types.
	 * @return the data types
	 */
	public List<DataType> getDataTypes() {
		if (dataTypes==null) {
			dataTypes = new ArrayList<>();
		}
		return dataTypes;
	}
	/**
	 * Returns the number of data columns.
	 * @return the number of data columns
	 */
	public int getNumberOfDataColumns() {
		return this.getDataTypes().size();
	}
	
}
