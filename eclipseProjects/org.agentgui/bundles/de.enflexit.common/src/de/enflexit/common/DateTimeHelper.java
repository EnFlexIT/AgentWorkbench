package de.enflexit.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class DateTimeHelper provides some static methods to handle date and time values (e.g. as long).
 * 
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public class DateTimeHelper {

	public static final String DEFAULT_TIME_FORMAT_PATTERN = "dd.MM.yy HH:mm:ss";
	
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
	 * @param date the date
	 * @param formatPattern the format pattern
	 * @return the date as string
	 */
	public static String getDateAsString(Date date) {
		return DateTimeHelper.getDateAsString(date, null);
	}
	/**
	 * Return the specified date as formatted string.
	 *
	 * @param date the date
	 * @param formatPattern the format pattern
	 * @return the date as string
	 */
	public static String getDateAsString(Date date, String formatPattern) {
		if (date!=null) {
			String pattern = formatPattern;
			if (pattern==null || pattern.isEmpty()==true) {
				pattern = DEFAULT_TIME_FORMAT_PATTERN;
			}
			return new SimpleDateFormat(pattern).format(date);
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
	
	
}
