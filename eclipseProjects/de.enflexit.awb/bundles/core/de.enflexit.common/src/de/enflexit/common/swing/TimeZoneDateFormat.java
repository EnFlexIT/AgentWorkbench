package de.enflexit.common.swing;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * The Class TimeZoneDateFormat extends a {@link SimpleDateFormat} 
 * and allows to set the {@link ZoneId} to consider the time zone to use.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class TimeZoneDateFormat extends SimpleDateFormat {

	private static final long serialVersionUID = 8617880534857559719L;


	// --------------------------------------------------------------
	// --- From here, reproduction of super class constructors ------ 
	// --------------------------------------------------------------
	/**
     * Constructs a <code>TimeZoneDateFormat</code> using the default pattern and
     * date format symbols for the default
     * {@link java.util.Locale.Category#FORMAT FORMAT} locale.
     * <b>Note:</b> This constructor may not support all locales.
     * For full coverage, use the factory methods in the {@link DateFormat}
     * class.
     */
	public TimeZoneDateFormat() {
		super();
	}
	
	/**
     * Constructs a <code>TimeZoneDateFormat</code> using the given pattern and
     * the default date format symbols for the default
     * {@link java.util.Locale.Category#FORMAT FORMAT} locale.
     * <b>Note:</b> This constructor may not support all locales.
     * For full coverage, use the factory methods in the {@link DateFormat}
     * class.
     * <p>This is equivalent to calling or use a {@link SimpleDateFormat}
     *
     * @see java.util.Locale#getDefault(java.util.Locale.Category)
     * @see java.util.Locale.Category#FORMAT
     * @param pattern the pattern describing the date and time format
     * @exception NullPointerException if the given pattern is null
     * @exception IllegalArgumentException if the given pattern is invalid
     */
	public TimeZoneDateFormat(String pattern) {
		super(pattern);
	}

    /**
     * Constructs a <code>TimeZoneDateFormat</code> using the given pattern and
     * the default date format symbols for the given locale.
     * <b>Note:</b> This constructor may not support all locales.
     * For full coverage, use the factory methods in the {@link DateFormat}
     * class.
     *
     * @param pattern the pattern describing the date and time format
     * @param locale the locale whose date format symbols should be used
     * @exception NullPointerException if the given pattern or locale is null
     * @exception IllegalArgumentException if the given pattern is invalid
     */
	public TimeZoneDateFormat(String pattern, Locale locale) {
		super(pattern, locale);
	}
	
	/**
     * Constructs a <code>TimeZoneDateFormat</code> using the given pattern and
     * date format symbols.
     *
     * @param pattern the pattern describing the date and time format
     * @param formatSymbols the date format symbols to be used for formatting
     * @exception NullPointerException if the given pattern or formatSymbols is null
     * @exception IllegalArgumentException if the given pattern is invalid
     */
    public TimeZoneDateFormat(String pattern, DateFormatSymbols formatSymbols) {
    	super(pattern, formatSymbols);
    }
    
	// --------------------------------------------------------------
	// --- From here, own constructor(s) with ZoneId parameter ------ 
	// --------------------------------------------------------------
    /**
	 * Constructs a <code>TimeZoneDateFormat</code> using the specified {@link ZoneId}.
	 *
	 * @param pattern the pattern describing the date and time format
	 * @param zoneId the ZoneId to be used
	 * @see java.util.Locale#getDefault(java.util.Locale.Category)
	 * @see java.util.Locale.Category#FORMAT
	 * @exception NullPointerException if the given pattern is null
	 * @exception IllegalArgumentException if the given pattern is invalid
	 */
	public TimeZoneDateFormat(String pattern, ZoneId zoneId) {
		super(pattern);
		this.setZoneId(zoneId);
	}

	
    /**
     * Sets the {@link ZoneId}.
     * @param zoneId the new zone id
     */
    public void setZoneId(ZoneId zoneId) {
    	this.setTimeZone(TimeZone.getTimeZone(zoneId));
    }
    /**
     * Returns the {@link ZoneId}.
     * @return the zone id
     */
    public ZoneId getZoneId() {
    	return this.getTimeZone().toZoneId();
    }
 
    
    /**
     * Formats the specified UTC time stamp into a date-time string.
     *
     * @param utcTimeStamp the UTC time stamp value to be formatted into a date-time string.
     * @return the formatted date-time string.
    */
	public String format(long utcTimeStamp) {
		return this.format(new Date(utcTimeStamp));
	}
    
}
