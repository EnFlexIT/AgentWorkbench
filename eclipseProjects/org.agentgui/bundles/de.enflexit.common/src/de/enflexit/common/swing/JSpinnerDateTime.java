package de.enflexit.common.swing;

import java.time.ZoneId;
import java.util.TimeZone;

import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;


/**
 * The Class JSpinnerDateTime extends the regular {@link JSpinner} and is designated for
 * the configuration of Date and Time values. In addition to the regular JSpinner with an
 * {@link SpinnerDateModel}, this class allows to change the {@link TimeZone} that will
 * immediately update the visual representation of the current Date (which is in UTC time).  
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class JSpinnerDateTime extends JSpinner {

	private static final long serialVersionUID = 5867268976574412548L;

	private String dateFormatPattern;
	private DateEditor dateEditor;
	
	
	/**
	 * Instantiates a new JSpinnerDateTime with the specified date format patern.
	 * @param dateFormatPattern the date format pattern (e.g. dd.MM.yyyy or HH:mm:ss)
	 */
	public JSpinnerDateTime(String dateFormatPattern) {
		super(new SpinnerDateModel());
		this.setDateFormatPattern(dateFormatPattern);
		this.setEditor(this.getDateEditor());
		
	}
	
	/**
	 * Returns the date format pattern.
	 * @return the date format pattern
	 */
	public String getDateFormatPattern() {
		if (dateFormatPattern==null || dateFormatPattern.trim().isEmpty()) {
			dateFormatPattern = "dd.MM.yyyy HH:mm:ss";
		}
		return dateFormatPattern;
	}
	/**
	 * Sets the date format pattern.
	 * @param dateFormatPattern the new date format pattern
	 */
	public void setDateFormatPattern(String dateFormatPattern) {
		this.dateFormatPattern = dateFormatPattern;
	}
	
	/**
	 * Returns the date editor of the JSpinner.
	 * @return the date editor
	 */
	private DateEditor getDateEditor() {
		if (dateEditor==null) {
			dateEditor = new JSpinner.DateEditor(this, this.getDateFormatPattern()); 	
		}
		return dateEditor;
	}
	
	/**
	 * Sets the time zone.
	 * @param newTimeZone the new time zone
	 */
	public void setTimeZone(TimeZone newTimeZone) {
		if (newTimeZone!=null && newTimeZone.equals(this.getTimeZone())==false) {
			this.getDateEditor().getFormat().setTimeZone(newTimeZone);
			this.fireStateChanged();
		}
	}
	/**
	 * Returns the currently used time zone.
	 * @return the time zone
	 */
	public TimeZone getTimeZone() {
		return this.getDateEditor().getFormat().getTimeZone();
	}
	
	
	/**
	 * Sets the ZoneId to the current spinner.
	 * @param newZoneId the new zone ID
	 */
	public void setZoneId(ZoneId newZoneId) {
		this.setTimeZone(TimeZone .getTimeZone(newZoneId));
	}
	/**
	 * Returns the currently used ZoneId of the spinner.
	 * @return the zone id
	 */
	public ZoneId getZoneId() {
		return this.getTimeZone().toZoneId();
	}
	
}
