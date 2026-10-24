package de.enflexit.df.core.processing.transformation.filter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;

public class DateTimeInputParser {
	private static final DateTimeFormatter DATE = formatter("dd.MM.uuuu");

	// --- Define the accepted date formats -----------
	private static final List<DateTimeFormatter> DATE_TIME_FORMATS = List.of(formatter("dd.MM.uuuu HH:mm:ss"),
			formatter("dd.MM.uuuu HH:mm"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

	/**
	 * Instantiates a new date time input parser.
	 */
	private DateTimeInputParser() {
	}

	/**
	 * Formatter.
	 * @param pattern the pattern
	 * @return the date time formatter
	 */
	private static DateTimeFormatter formatter(String pattern) {
		return DateTimeFormatter.ofPattern(pattern).withResolverStyle(ResolverStyle.STRICT);
	}
	
	/**
	 * Parses the provided input String as LocalDate object.
	 * @param input the input
	 * @return the local date
	 */
	public static LocalDate parseDate(String input) {
		String text = input == null ? "" : input.trim();

		if (text.isEmpty()) {
			throw new DateTimeParseException("Can't parse an empty string!", text, 0);
		}
		
		try {
			return LocalDate.parse(input, DATE);
		} catch (DateTimeParseException ignored) {
			// --- No matching format found ---------------------
			throw new DateTimeParseException("Unable to parse date/time! Supported formats: dd.MM.yyyy, dd.MM.yyyy HH:mm, dd.MM.yyyy HH:mm:ss or ISO date time", text, 0);
		}
		
	}

	/**
	 * Parses the provided input String as LocalDateTime object.
	 * @param input the input
	 * @return the local date time
	 */
	public static LocalDateTime parseDateTime(String input) {
		String text = input == null ? "" : input.trim();

		if (text.isEmpty()) {
			throw new DateTimeParseException("Can't parse an empty string!", text, 0);
		}

		// --- Try date/time formats, starting with the most detailed one
		for (DateTimeFormatter formatter : DATE_TIME_FORMATS) {
			try {
				return LocalDateTime.parse(text, formatter);
			} catch (DateTimeParseException ignored) {
				// --- Nothing to do here, trying the next format in the next iteration
			}
		}
		
		// --- Try date only format -----------------------------
		try {
			return LocalDate.parse(input, DATE).atStartOfDay();
		} catch (DateTimeParseException ignored) {
			// --- No matching format found ---------------------
			throw new DateTimeParseException("Unable to parse date/time! Supported formats: dd.MM.yyyy, dd.MM.yyyy HH:mm, dd.MM.yyyy HH:mm:ss or ISO date time", text, 0);
		}
		
	}
}
