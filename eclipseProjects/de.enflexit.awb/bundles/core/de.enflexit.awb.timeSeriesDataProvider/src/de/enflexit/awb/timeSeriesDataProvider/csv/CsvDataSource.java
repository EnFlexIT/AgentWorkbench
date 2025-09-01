package de.enflexit.awb.timeSeriesDataProvider.csv;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSource;
import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSourceConfiguration;
import de.enflexit.awb.timeSeriesDataProvider.TimeValuePair;
import de.enflexit.common.GlobalRuntimeValues;
import de.enflexit.common.NumberHelper;

/**
 * This class manages a single CSV data source.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class CsvDataSource extends AbstractDataSource {
	
	private Vector<Vector<Number>> csvData;
	private int lastUsedRowIndex;
	
	private CsvDataSourceConfiguration sourceConfiguration;
	private DateTimeFormatter dateTimeFormatter;
	
	private HashMap<String, Integer> dataColumns;
	private HashMap<String, CsvDataSeries> dataSeriesHashMap;
	
	private ConcurrentHashMap<Long, HashMap<String, Double>> valuesByTimeHashMap;
	
	/**
	 * Instantiates a new csv data source.
	 * @param sourceConfiguration the source configuration
	 */
	public CsvDataSource(CsvDataSourceConfiguration sourceConfiguration) {
		this.sourceConfiguration = sourceConfiguration;
	}

	/* (non-Javadoc)
	 * @see net.peak.weatherDataProvider.AbstractDataSource#getValue(java.lang.String, long)
	 */
	@Override
	public Double getValue(String seriesName, long timestamp) {
		
		// --- Try the HashMap first --------------------------------
		HashMap<String, Double> valuesForTimestamp = this.getValuesForTimestamp(timestamp);
		Double value = valuesForTimestamp.get(seriesName);
		
		if (value==null) {
			
			// --- Make sure only the first accessing thread gets the value from theCSV data set
			synchronized (this.getCsvData()) {
				
				// --- Check if the value was put to the HashMap while waiting
				value = valuesForTimestamp.get(seriesName);
				if (value==null) {
					// --- Get the value from the CSV data instead ----------
					TimeValuePair tvp = this.getValueFromCsvData(seriesName, timestamp);
					if (tvp!=null) {
						value = tvp.getValue();
					}
					// --- Remember for faster access -----------------------
					valuesForTimestamp.put(seriesName, value);
				}
			}
		}
		
		return value;
	}
	
	/**
	 * Gets the value from the csv data set.
	 * @param seriesName the series name
	 * @param timestamp the timestamp
	 * @return the value
	 */
	private TimeValuePair getValueFromCsvData(String seriesName, long timestamp) {
		TimeValuePair value = null;
		Integer columnIndex = this.getDataColumnForSeries(seriesName);
		if (columnIndex!=null) {
			int rowIndex = this.getDataRowIndex(timestamp);
			if (rowIndex>=0) {
				Vector<Number> dataRow = this.getCsvData().get(rowIndex);
				this.lastUsedRowIndex = rowIndex;
				if (dataRow!=null) {
					if (columnIndex!=null && columnIndex < dataRow.size()) {
						double val = dataRow.get(this.getDataColumnForSeries(seriesName)).doubleValue();
						long csvTimestamp = dataRow.get(0).longValue();
						value = new TimeValuePair(csvTimestamp, val);
					}
				}
			}
		} else {
			System.err.println("[" + this.getClass().getSimpleName() + "] No data column found for series " + seriesName + " in data source " + this.sourceConfiguration.getName());
		}
		return value;
	}
	
	/**
	 * Finds the data row for the specified timestamp.
	 * @param timestamp the timestamp
	 * @return the vector
	 */
	private int getDataRowIndex(long timestamp){
		int rowIndex = -1;
		
		// --- Try to continue from the last used data row, since time series data is usually requested in chronological order.
		int startSearchFrom = this.lastUsedRowIndex;
		if (this.getCsvData().get(startSearchFrom).get(0).doubleValue()>timestamp) {
			// --- Start from the beginning if the requested time stamp is before the last used data row. 
			startSearchFrom = 0;
		}
		
		// --- Look for the row that fits the time stamp ------------
		for(int i=startSearchFrom; i<this.getCsvData().size(); i++) {
			long rowTimestamp = this.getCsvData().get(i).get(0).longValue(); 
			if (rowTimestamp==timestamp) {
				// --- Exact match ----------------------------------
				rowIndex = i;
				break;
			} else if (rowTimestamp>timestamp){
				// --- Beyond the time stamp already -> no exact match -------- 
				if (i>0) {
					// --- The time stamp is between the current and the previous row
					rowIndex = i-1;
					break;
				} else {
					// --- The time stamp is before the first row -> no match 
					break;
				}
			}
		}
		
		return rowIndex;
	}
	
	/**
	 * Import csv data.
	 * @return the vector
	 */
	private Vector<Vector<Number>> importCsvData() {
		Vector<Vector<Number>> importedData = null;
		
		if(this.sourceConfiguration.getCsvFile() == null){
			System.err.println("No CSV file configured");
		}else{
			BufferedReader fileReader = null;
			try {
				
				importedData = new Vector<Vector<Number>>();
				fileReader = new BufferedReader(new FileReader(this.sourceConfiguration.getCsvFile()));

				// --- Skip the first row if it contains headers ----
				if (this.sourceConfiguration.isHeadline()) {
					fileReader.readLine();
				}
				
				// --- Import row by row ---------------------------- 
				String row;
				while((row = fileReader.readLine()) != null){
					String[] tokens = row.split(this.sourceConfiguration.getColumnSeparator());
					Vector<Number> rowData = new Vector<>();
					
					// --- Parse date time column -------------------
					ZonedDateTime zdt = ZonedDateTime.parse(tokens[0], this.getDateTimeFormatter());
					rowData.add(zdt.toInstant().toEpochMilli());

					// --- Parse the numeric values for the data series
					for (int i=1; i<tokens.length; i++) {
						rowData.add(NumberHelper.parseDouble(tokens[i]));
					}
					
					importedData.add(rowData);
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if (fileReader!=null) {
					try {
						fileReader.close();
					} catch (IOException e) {
						System.err.println("Error closing file reader");
						e.printStackTrace();
					}
				}
			}
		}
		
		return importedData;
	}
	
	/**
	 * Gets the date time formatter.
	 * @return the date time formatter
	 */
	private DateTimeFormatter getDateTimeFormatter() {
		if (dateTimeFormatter==null) {
			dateTimeFormatter = DateTimeFormatter.ofPattern(this.sourceConfiguration.getDateTimeFormat()).withZone(GlobalRuntimeValues.getZoneId());
		}
		return dateTimeFormatter;
	}
	
	/**
	 * Gets the csv data vector for this data source.
	 * @return the csv data
	 */
	private Vector<Vector<Number>> getCsvData() {
		if (csvData==null) {
			csvData = this.importCsvData();
		}
		return csvData;
	}
	
	/**
	 * Gets a HashMap to look up the data column for a data series.
	 * @return the data columns
	 */
	private HashMap<String, Integer> getDataColumns() {
		if (dataColumns==null) {
			dataColumns = new HashMap<>();
		}
		return dataColumns;
	}
	
	/**
	 * Gets the data column index for the specified series.
	 * @param seriesName the series name
	 * @return the data column for series
	 */
	private Integer getDataColumnForSeries(String seriesName) {
		// --- Look in the HashMap first ----------------------------
		Integer column = this.getDataColumns().get(seriesName);
		if (column==null) {
			// --- If not found, check the data series configurations ---------
			for (int i=0; i<this.sourceConfiguration.getDataSeriesConfigurations().size(); i++) {
				CsvDataSeriesConfiguration seriesConfig = (CsvDataSeriesConfiguration) this.sourceConfiguration.getDataSeriesConfigurations().get(i);
				if (seriesConfig.getName().equals(seriesName)) {
					column = seriesConfig.getDataColumn();
					// --- Remember for the next access -------------
					this.getDataColumns().put(seriesName, column);
					break;
				}
			}
		}
		return column;
	}

	/* (non-Javadoc)
	 * @see net.peak.timeSeriesDataProvider.AbstractDataSource#getDataSeriesValues(java.lang.String)
	 */
	@Override
	public List<Double> getDataSeriesValues(String seriesName) {
		Integer column = this.getDataColumnForSeries(seriesName);
		if (column!=null) {
			List<Double> valuesList = new ArrayList<>();
			for (int i=0; i<csvData.size(); i++) {
				valuesList.add(this.csvData.get(i).get(column).doubleValue());
			}
			return valuesList;
		}
		return null;
	}
	
	/**
	 * Gets the concurrent HashMap that stores values for faster access  
	 * @return the values by time hash map
	 */
	private ConcurrentHashMap<Long, HashMap<String, Double>> getValuesByTimeHashMap() {
		if (valuesByTimeHashMap==null) {
			valuesByTimeHashMap = new ConcurrentHashMap<>();
		}
		return valuesByTimeHashMap;
	}
	
	/**
	 * Gets the all time Stamps this data source contains values for.
	 * @return the all time Stamps
	 */
	public Collection<Long> getAllTimeStamps(){
		return this.getValuesByTimeHashMap().keySet();
	}
	
	/**
	 * Gets the values for a specific timestamp from the HashMap.
	 * @param timestamp the timestamp
	 * @return the values for timestamp
	 */
	private HashMap<String, Double> getValuesForTimestamp(long timestamp){
		HashMap<String, Double> valuesHashMap = this.getValuesByTimeHashMap().get(timestamp);
		if (valuesHashMap==null) {
			valuesHashMap = new HashMap<>();
			this.getValuesByTimeHashMap().put(timestamp, valuesHashMap);
		}
		return valuesHashMap;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.AbstractDataSource#getValuesForTimeRange(java.lang.String, long, long)
	 */
	@Override
	public List<TimeValuePair> getValuesForTimeRange(String seriesName, long timestampFrom, long timestampTo) {
		List<TimeValuePair> valuesForRange = new ArrayList<TimeValuePair>();
		Integer columnIndex = this.getDataColumnForSeries(seriesName);
		if (columnIndex!=null) {
			
			// --- Collect all values between the specified times -------------
			for (Vector<Number> dataRow : this.getCsvData()) {
				long rowTimestamp = dataRow.get(0).longValue();
				if (rowTimestamp>=timestampFrom && rowTimestamp <= timestampTo) {
					double rowValue = dataRow.get(columnIndex).doubleValue();
					valuesForRange.add(new TimeValuePair(rowTimestamp, rowValue));
				} else if (rowTimestamp>timestampTo) {
					// --- Since the order is ascending, no more matches to be expected
					break;
				}
			}
			
			// --- If there was no exact match, try to include the next earlier value to cover the whole range
			if (valuesForRange.get(0).getTimestamp()>timestampFrom) {
				TimeValuePair tvpBefore = this.getNextEarlierValue(columnIndex, timestampFrom);
				if (tvpBefore!=null) {
					valuesForRange.add(0, tvpBefore);
				} else {
					System.err.println("[" + this.getClass().getSimpleName() + "] This data source contains no values before " + this.getDateTimeFormatter().format(Instant.ofEpochMilli(timestampFrom)));
				}
			}
			// --- If there was no exact match, try to include the next later value to cover the whole range
			if (valuesForRange.get(valuesForRange.size()-1).getTimestamp()<timestampTo) {
				TimeValuePair tvpAfter = this.getNextLaterValue(columnIndex, timestampTo);
				if (tvpAfter!=null) {
					valuesForRange.add(tvpAfter);
				} else {
					System.err.println("[" + this.getClass().getSimpleName() + "] This data source contains no values after " + this.getDateTimeFormatter().format(Instant.ofEpochMilli(timestampTo)));
				}
			}
			
		} else {
			System.err.println("[" + this.getClass().getSimpleName() + "] No data column found for series name " + seriesName);
		}
		return valuesForRange;
	}
	
	/**
	 * Gets the next earlier value before the specified timestamp.
	 * @param columnIndex the column index for the data series
	 * @param timestamp the timestamp
	 * @return the next earlier value
	 */
	private TimeValuePair getNextEarlierValue(int columnIndex, long timestamp) {
		TimeValuePair tvp = null;
		// --- getDataRowIndex already returns the next earlier row index if no exact match was found
		int rowIndexFound = this.getDataRowIndex(timestamp);
		Vector<Number> rowFound = this.getCsvData().get(rowIndexFound);
		if (rowFound.get(0).longValue()<timestamp) {
			// --- Since it could also be an exact match, check if it is really earlier
			long timestampBefore = rowFound.get(0).longValue();
			double valueBefore = rowFound.get(columnIndex).doubleValue();
			tvp = new TimeValuePair(timestampBefore, valueBefore);
		}
		return tvp;
	}
	
	/**
	 * Gets the next later value after the specified timestamp.
	 * @param seriesName the series name
	 * @param timestamp the timestamp
	 * @return the next later value
	 */
	private TimeValuePair getNextLaterValue(int columnIndex, long timestamp) {
		TimeValuePair tvp = null;
		int rowIndexForTimestamp = this.getDataRowIndex(timestamp);
		if (rowIndexForTimestamp<this.getCsvData().size()-1) {
			Vector<Number> rowAfter = this.getCsvData().get(rowIndexForTimestamp+1);
			long timestampAfter = rowAfter.get(0).longValue();
			double valueAfter = rowAfter.get(columnIndex).doubleValue();
			tvp = new TimeValuePair(timestampAfter, valueAfter);
		}
		return tvp;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.AbstractDataSource#getDataSeries(java.lang.String)
	 */
	@Override
	public CsvDataSeries getDataSeries(String seriesName) {
		CsvDataSeries dataSeries = this.getDataSeriesHashMap().get(seriesName);
		if (dataSeries==null && this.sourceConfiguration.getDataSeriesNames().contains(seriesName)) {
			dataSeries = new CsvDataSeries(seriesName, this);
		}
		return dataSeries;
	}
	
	/**
	 * Gets the data series hash map.
	 *
	 * @return the data series hash map
	 */
	private HashMap<String, CsvDataSeries> getDataSeriesHashMap() {
		if (dataSeriesHashMap==null) {
			dataSeriesHashMap = new HashMap<String, CsvDataSeries>();
		}
		return dataSeriesHashMap;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.AbstractDataSource#isAvailable()
	 */
	@Override
	public boolean isAvailable() {
		return (this.getCsvData()!=null);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.AbstractDataSource#getSourceConfiguration()
	 */
	@Override
	public AbstractDataSourceConfiguration getSourceConfiguration() {
		return this.sourceConfiguration;
	}
	
	/**
	 * Gets the first time stamp this data source contains entries for.
	 * @return the first time stamp
	 */
	public long getFirstTimeStamp() {
		Number firstRowTime = this.getCsvData().get(0).get(0);
		return firstRowTime.longValue();
	}
	
	/**
	 * Gets the last time stamp this data source contains entries for.
	 * @return the last time stamp
	 */
	public long getLastTimeStamp() {
		Number lastRowTime = this.getCsvData().get(this.getCsvData().size()-1).get(0);
		return lastRowTime.longValue();
	}
	
}
