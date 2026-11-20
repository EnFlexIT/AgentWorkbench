package de.enflexit.df.core.processing.transformation.columnOperation;

public class TemporalParameter {

	private long value;
	private TimeUnit unit;

	public TemporalParameter(long value, TimeUnit unit) {
		this.value = value;
		this.unit = unit;
	}

	/**
	 * Gets the value.
	 * @return the value
	 */
	public long getValue() {
		return value;
	}
	/**
	 * Sets the value.
	 * @param value the new value
	 */
	public void setValue(long value) {
		this.value = value;
	}

	/**
	 * Gets the unit.
	 * @return the unit
	 */
	public TimeUnit getUnit() {
		return unit;
	}
	/**
	 * Sets the unit.
	 * @param unit the new unit
	 */
	public void setUnit(TimeUnit unit) {
		this.unit = unit;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return value + " " + unit.name();
	}
	/**
	 * Parses the provided string as a {@link TemporalParameter}.
	 * @param temporalParameterString the string to parse
	 * @return the temporal parameter
	 */
	public static TemporalParameter fromString(String temporalParameterString) {
		String[] parts = temporalParameterString.trim().split("\\s+");

		if (parts.length != 2) {
			throw new IllegalArgumentException("Invalid temporal parameter: " + temporalParameterString);
		}

		long amount = Long.parseLong(parts[0]);
		TimeUnit unit = TimeUnit.valueOf(parts[1]);

		return new TemporalParameter(amount, unit);
	}
}