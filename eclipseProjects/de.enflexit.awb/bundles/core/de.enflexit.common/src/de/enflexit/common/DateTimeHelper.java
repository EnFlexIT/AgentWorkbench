package de.enflexit.common;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * The Class DateTimeHelper provides some static methods to handle date and time values (e.g. as long).
 * 
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public class DateTimeHelper {

	public static final String DEFAULT_TIME_FORMAT_PATTERN = "dd.MM.yy HH:mm:ss";
	
	public static final long MILLISECONDS_FOR_SECOND = 1000;
	public static final long MILLISECONDS_FOR_MINUTE = MILLISECONDS_FOR_SECOND * 60;
	public static final long MILLISECONDS_FOR_HOUR = MILLISECONDS_FOR_MINUTE * 60;
	public static final long MILLISECONDS_FOR_DAY = MILLISECONDS_FOR_MINUTE * 24;
	public static final long MILLISECONDS_FOR_WEEK = MILLISECONDS_FOR_DAY * 7;
	public static final long MILLISECONDS_FOR_MONTH_30 = MILLISECONDS_FOR_DAY * 30;
	
	/**
	 * Return the date from the specified time stamp in milliseconds.
	 *
	 * @param timeMillis the time milliseconds
	 * @return the date
	 */
	public static Date getDate(long timeMillis) {
		return new Date(timeMillis);
	}
	
	
	/**
	 * Return the specified date as formatted string.
	 *
	 * @param timeMillis the time milliseconds
	 * @return the date as string
	 */
	public static String getTimeAsString(long timeMillis) {
		return DateTimeHelper.getDateAsString(getDate(timeMillis), null);
	}
	/**
	 * Return the specified date as formatted string.
	 *
	 * @param timeMillis the time milliseconds
	 * @param formatPattern the format pattern
	 * @return the date as string
	 */
	public static String getTimeAsString(long timeMillis, String formatPattern) {
		return DateTimeHelper.getDateAsString(getDate(timeMillis), formatPattern);
	}
	/**
	 * Return the specified date as formatted string.
	 *
	 * @param timeMillis the time milliseconds
	 * @param formatPattern the format pattern
	 * @param additionalText the additional text
	 * @return the date as string
	 */
	public static String getTimeAsString(long timeMillis, String formatPattern, String additionalText) {
		return DateTimeHelper.getDateAsString(getDate(timeMillis), formatPattern, additionalText);
	}
	
	
	/**
	 * Return the specified date as formatted string.
	 *
	 * @param date the date
	 * @return the date as string
	 */
	public static String getDateAsString(Date date) {
		return DateTimeHelper.getDateAsString(date, null);
	}
	/**
	 * Return the specified date as formatted string.
	 *
	 * @param date the date
	 * @param pattern the format pattern
	 * @return the date as string
	 */
	public static String getDateAsString(Date date, String pattern) {
		if (date!=null) {
			return DateTimeHelper.getDateTimeAsString(date, pattern, null);
		}
		return null;
	}
	/**
	 * Return the specified date as formatted string.
	 *
	 * @param date the date
	 * @param formatPattern the format pattern
	 * @param additionalText the additional text
	 * @return the date as string
	 */
	public static String getDateAsString(Date date, String formatPattern, String additionalText) {
		
		String msgString = DateTimeHelper.getDateAsString(date, formatPattern);
		if (additionalText!=null && additionalText.isEmpty()==false) {
			if (msgString==null) {
				msgString = "[No date specified] " + additionalText;
			} else {
				msgString += " " + additionalText; 
			}
		}
		return msgString;
	}
	
	
	/**
	 * Prints the specified date.
	 * @param timeMillis the time in milliseconds
	 */
	public static void printTime(long timeMillis) {
		DateTimeHelper.printDate(getDate(timeMillis), null, null, false);
	}
	/**
	 * Prints the specified date.
	 *
	 * @param timeMillis the time in milliseconds
	 * @param formatPattern the format pattern
	 */
	public static void printTime(long timeMillis, String formatPattern) {
		DateTimeHelper.printDate(getDate(timeMillis), formatPattern, null, false);
	}
	/**
	 * Prints the specified date and the additional text if the overall result is not null.
	 *
	 * @param timeMillis the time in milliseconds
	 * @param formatPattern the format pattern
	 * @param additionalText the additional text
	 * @param isError the is error
	 */
	public static void printTime(long timeMillis, String formatPattern, String additionalText, boolean isError) {
		DateTimeHelper.printDate(getDate(timeMillis), formatPattern, additionalText, isError);
	}
	
	
	/**
	 * Prints the specified date.
	 * @param date the date
	 */
	public static void printDate(Date date) {
		DateTimeHelper.printDate(date, null, null, false);
	}
	/**
	 * Prints the specified date.
	 *
	 * @param date the date
	 * @param formatPattern the format pattern
	 */
	public static void printDate(Date date, String formatPattern) {
		DateTimeHelper.printDate(date, formatPattern, null, false);
	}
	/**
	 * Prints the specified date and the additional text if the overall result is not null.
	 *
	 * @param date the date
	 * @param formatPattern the format pattern
	 * @param additionalText the additional text
	 * @param isError the is error
	 */
	public static void printDate(Date date, String formatPattern, String additionalText, boolean isError) {
		
		String msgString = DateTimeHelper.getDateAsString(date, formatPattern, additionalText);
		if (msgString!=null && msgString.isEmpty()==false) {
			if (isError==true) {
				System.err.println(msgString);
			} else {
				System.out.println(msgString);
			}
		}
	}
	
	
	
	/**
	 * Returns the midnight date for the specified time stamp.
	 * @param timeStamp the time stamp
	 * @return the date for midnight
	 */
	public static Date getDateForMidnight(long timeStamp) {
		return DateTimeHelper.getDateForMidnight(new Date(timeStamp));
	}
	/**
	 * Returns the midnight date for the specified date.
	 * @param date the date
	 * @return the date for midnight
	 */
	public static Date getDateForMidnight(Date date) {
		
		Calendar startCalenderMerged = Calendar.getInstance();
		
		startCalenderMerged.setTime(date);
		startCalenderMerged.set(Calendar.HOUR_OF_DAY, 0);
		startCalenderMerged.set(Calendar.MINUTE, 0);
		startCalenderMerged.set(Calendar.SECOND, 0);
		startCalenderMerged.set(Calendar.MILLISECOND, 0);
		
		Date newDate = startCalenderMerged.getTime();
		return newDate;
	}
	
	
	// ------------------------------------------------------------------------
	// --- From here, static Date (long timestamp) to string methods ----------
	// ------------------------------------------------------------------------
	/**
	 * Returns the specified UTC date / time stamp as string by considering the format pattern and the ZoneId.
	 *
	 * @param date the date to format; <code>null</code> is not allowed. If, <code>null</code> will be returned
	 * @param pattern the pattern; can be <code>null</code>. If, the local {@link #DEFAULT_TIME_FORMAT_PATTERN} is used
	 * @param zoneId the ZoneId; can be <code>null</code>. If,  the system defaults will be used.
	 * @return the date time as string
	 */
	public static String getDateTimeAsString(Date date, String pattern, ZoneId zoneId) {
		if (date==null) return null;
		return DateTimeHelper.getDateTimeAsString(date.getTime(), pattern, zoneId);
	}
	/**
	 * Returns the specified UTC time stamp as string by considering the format pattern and the ZoneId.
	 *
	 * @param utcTimeStamp the UTC time stamp as long 
	 * @param pattern the pattern; can be <code>null</code>. If, the local {@link #DEFAULT_TIME_FORMAT_PATTERN} is used
	 * @param zoneId the ZoneId; can be <code>null</code>. If,  the system defaults will be used.
	 * @return the date time as string
	 */
	public static String getDateTimeAsString(long utcTimeStamp, String pattern, ZoneId zoneId) {
		if (pattern==null || pattern.isBlank()) pattern = DEFAULT_TIME_FORMAT_PATTERN;
		if (zoneId==null) zoneId = ZoneId.systemDefault();
		return ZonedDateTime.ofInstant(Instant.ofEpochMilli(utcTimeStamp), zoneId).format(DateTimeFormatter.ofPattern(pattern));
	}
	
	// ------------------------------------------------------------------------
	// --- From here, methods for parsing DateTime Strings --------------------
	// ------------------------------------------------------------------------
	
	/**
	 * Parses a date/time string, according to the specified format and zone. Returns a {@link ZonedDateTime} instance.
	 * @param dateString the date string
	 * @param dateFormat the date format
	 * @param zoneId the zone id
	 * @return the parsed ZonedDateTime instance 
	 */
	public static ZonedDateTime getZonedDateTimeFromString(String dateString, String dateFormat, ZoneId zoneId) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat).withZone(zoneId);
		return ZonedDateTime.parse(dateString, dtf);
	}
	
	/**
	 * Parses a date/time string, according to the specified format and zone. Returns a long timestamp (epoch millis)
	 * @param dateString the date string
	 * @param dateFormat the date format
	 * @param zoneId the zone id
	 * @return the timestamp
	 */
	public static long getTimestampFromDateTimeString(String dateString, String dateFormat, ZoneId zoneId) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateFormat).withZone(zoneId);
		ZonedDateTime zdt = ZonedDateTime.parse(dateString, dtf);
		return zdt.toInstant().toEpochMilli();
	}
	
}
