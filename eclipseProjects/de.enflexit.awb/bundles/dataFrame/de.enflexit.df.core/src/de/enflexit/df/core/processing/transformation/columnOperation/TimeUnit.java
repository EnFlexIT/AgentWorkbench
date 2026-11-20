package de.enflexit.df.core.processing.transformation.columnOperation;

import java.time.temporal.ChronoUnit;

/**
 * This enumeration defines the supported time units- 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public enum TimeUnit {

	MILLISECONDS("Milliseconds", ChronoUnit.MILLIS),
	SECONDS("Seconds", ChronoUnit.SECONDS),
	MINUTES("Minutes", ChronoUnit.MINUTES),
	HOURS("Hours", ChronoUnit.HOURS),
	DAYS("Days", ChronoUnit.DAYS),
	WEEKS("Weeks", ChronoUnit.WEEKS),
	MONTHS("Months", ChronoUnit.MONTHS), 
	YEARS("Years", ChronoUnit.YEARS);

	private final String label;
	private final ChronoUnit chronoUnit;

	/**
	 * Instantiates a new time unit.
	 * @param label the label
	 * @param calendarBased the calendar based
	 */
	private TimeUnit(String label, ChronoUnit chronoUnit) {
		this.label = label;
		this.chronoUnit = chronoUnit;
	}

	/**
	 * Gets the label.
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * Gets the {@link ChronoUnit}.
	 * @return the chrono unit
	 */
	public ChronoUnit getChronoUnit() {
		return chronoUnit;
	}


	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return label;
	}
}
