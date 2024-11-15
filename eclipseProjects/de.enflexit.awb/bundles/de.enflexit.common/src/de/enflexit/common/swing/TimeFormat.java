package de.enflexit.common.swing;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class TimeFormat describes a predefined format for a date.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class TimeFormat implements Serializable {

	private static final long serialVersionUID = 6691784575006563592L;

	private String format;
	private Date dateDisplay;
	
	
	/**
	 * Instantiates a new time unit.
	 * @param format the String representation of the time format
	 */
	public TimeFormat(String format) {
		this.format = format;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.format + " (" + new SimpleDateFormat(this.format).format(this.getDateDisplay()) + ")";
	}

	/**
	 * Gets the date to display.
	 * @return the date to display
	 */
	private Date getDateDisplay() {
		if (dateDisplay==null) {
			dateDisplay = new Date();
		}
		return dateDisplay;
	}
	
	/**
	 * Sets the format.
	 * @param format the new format
	 */
	public void setFormat(String format) {
		this.format = format;
	}
	/**
	 * Gets the format.
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

}
