package de.enflexit.df.core.ui;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import javax.swing.table.DefaultTableCellRenderer;

import de.enflexit.awb.core.Application;

/**
 * The Class DateTimeTableCellRenderer.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DateTimeTableCellRenderer extends DefaultTableCellRenderer {

	private static final long serialVersionUID = -4118665509829395892L;

	private String pattern;
	private DateTimeFormatter dateTimeFormatter;
	
	/**
	 * Instantiates a new date time table cell renderer.
	 * @param pattern the pattern
	 */
	public DateTimeTableCellRenderer(String pattern) {
		this.setPattern(pattern);
	}
	
	/**
	 * Sets the pattern.
	 * @param pattern the new pattern
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
		this.dateTimeFormatter = null;
	}
	/**
	 * Gets the pattern.
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}
	
	public DateTimeFormatter getDateTimeFormatter() {
		if (dateTimeFormatter==null) {
			dateTimeFormatter = DateTimeFormatter.ofPattern(this.getPattern());
		}
		return dateTimeFormatter;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableCellRenderer#setValue(java.lang.Object)
	 */
	@Override
	protected void setValue(Object value) {
		
		if (value instanceof TemporalAccessor) {
			
			LocalDateTime ldt = null;
			if (value instanceof Instant dtTimeStamp) {
				ldt = LocalDateTime.ofInstant(dtTimeStamp, Application.getGlobalInfo().getZoneId());
				this.setText((value == null) ? "" : this.getDateTimeFormatter().format(ldt));
				return;
				
			} else if (value instanceof LocalDateTime) {
				ldt = LocalDateTime.from((TemporalAccessor) value);
				this.setText((value == null) ? "" : this.getDateTimeFormatter().format(ldt));
				return;
			}
		}
		super.setValue(value);
	}
	
}
