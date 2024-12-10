package de.enflexit.awb.simulation.environment.time;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import de.enflexit.common.ontology.OntologyVisualisationConfiguration;

/**
 * The Class TimeModelDateBased.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public abstract class TimeModelDateBased extends TimeModel {

	private static final long serialVersionUID = 6116787943288451141L;

	public static final String DEFAULT_TIME_FORMAT = "dd.MM.yyyy HH:mm:ss.SSS";
	
	protected long timeStart = System.currentTimeMillis();
	protected long timeStop = System.currentTimeMillis() + 1000 * 60 * 60 * 24;
	protected String timeFormat = "dd.MM.yyyy HH:mm:ss.SSS";
	protected ZoneId zoneId;
	
	public abstract long getTime();
	
	/**
	 * Sets the start time .
	 * @param timeStart the new start time
	 */
	public void setTimeStart(long timeStart) {
		this.timeStart = timeStart;
	}
	/**
	 * Returns the start time.
	 * @return the start time 
	 */
	public long getTimeStart() {
		return timeStart;
	}
	
	/**
	 * Sets the stop time.
	 * @param timeStop the new stop time
	 */
	public void setTimeStop(long timeStop) {
		this.timeStop = timeStop;
	}
	/**
	 * Returns the stop time.
	 * @return the stop time 
	 */
	public long getTimeStop() {
		return timeStop;
	}
	
	/**
	 * Sets the time format.
	 * @param timeFormat the new time format
	 */
	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
		// --- Forward new format to the ontology visualization -----
		OntologyVisualisationConfiguration.setTimeFormat(timeFormat);
	}
	/**
	 * Returns the time format.
	 * @return the time format
	 */
	public String getTimeFormat() {
		if (this.timeFormat==null || this.timeFormat.equals("")==true) {
			this.timeFormat = TimeModelDateBased.DEFAULT_TIME_FORMAT;
			// --- Forward new format to the ontology visualization -----
			OntologyVisualisationConfiguration.setTimeFormat(timeFormat);
		}
		return this.timeFormat;
	}
	
	/**
	 * Returns the time models {@link ZoneId}.
	 * @return the zone id
	 */
	public ZoneId getZoneId() {
		if (zoneId==null) {
			return ZoneId.systemDefault();
		}
		return zoneId;
	}
	/**
	 * Sets the time models {@link ZoneId}.
	 * @param newZoneId the new zone id
	 */
	public void setZoneId(ZoneId newZoneId) {
		if (newZoneId!=null && newZoneId.equals(ZoneId.systemDefault())==true) {
			this.zoneId = null;
		} else {
			this.zoneId = newZoneId;
		}
	}
	
	
	
	/**
	 * Returns the midnight date for the specified time stamp.
	 * @param timeStamp the time stamp
	 * @return the date for midnight
	 */
	public Date getDateForMidnight(long timeStamp) {
		return this.getDateForMidnight(new Date(timeStamp));
	}
	/**
	 * Returns the midnight date for the specified date.
	 * @param date the date to adjust
	 * @return the date for midnight
	 */
	public Date getDateForMidnight(Date date) {
		
		Calendar startCalenderMerged = Calendar.getInstance();
		
		startCalenderMerged.setTime(date);
		startCalenderMerged.set(Calendar.HOUR_OF_DAY, 0);
		startCalenderMerged.set(Calendar.MINUTE, 0);
		startCalenderMerged.set(Calendar.SECOND, 0);
		startCalenderMerged.set(Calendar.MILLISECOND, 0);
		
		Date newDate = startCalenderMerged.getTime();
		return newDate;
	}
	
}
