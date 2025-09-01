package de.enflexit.awb.timeSeriesDataProvider.csv;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeries;
import de.enflexit.awb.timeSeriesDataProvider.TimeValuePair;

/**
 * The Class CsvDataSeries.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class CsvDataSeries extends AbstractDataSeries {
	
	private String name;
	private CsvDataSource parentSource;
	
	/**
	 * Instantiates a new csv data series.
	 * @param parentSource the parent csv data source
	 */
	public CsvDataSeries(String name, CsvDataSource parentSource) {
		this.name = name;
		this.parentSource = parentSource;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeries#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeries#getFirstTimeStamp()
	 */
	@Override
	public long getFirstTimeStamp() {
		Collection<Long> timeStamps = this.parentSource.getAllTimeStamps();
		if (timeStamps!=null && timeStamps.isEmpty()==false) {
			return Collections.min(this.parentSource.getAllTimeStamps());
		} else {
			return 0;
		}
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeries#getLastTimeStamp()
	 */
	@Override
	public long getLastTimeStamp() {
		Collection<Long> timeStamps = this.parentSource.getAllTimeStamps();
		if (timeStamps!=null && timeStamps.isEmpty()==false) {
			return Collections.max(this.parentSource.getAllTimeStamps());
		} else {
			return 0;
		}
	}
	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeries#getNumberOfEntries()
	 */
	@Override
	public int getNumberOfEntries() {
		return this.parentSource.getAllTimeStamps().size();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeries#getValue(long)
	 */
	@Override
	public TimeValuePair getValueForTime(long timestamp) {
		// --- Delegate value retrieval to the parent data source
		double value = this.parentSource.getValue(this.getName(), timestamp);
		return new TimeValuePair(timestamp, value);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.awb.timeSeriesDataProvider.AbstractDataSeries#getValuesForTimeRange(long, long)
	 */
	@Override
	public List<TimeValuePair> getValuesForTimeRange(long timestampFrom, long timestampTo) {
		// --- Delegate value retrieval to the parent data source
		return this.parentSource.getValuesForTimeRange(this.getName(), timestampFrom, timestampTo);
	}

}
