package de.enflexit.df.core.processing.transformation.join;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import tech.tablesaw.api.DateTimeColumn;
import tech.tablesaw.api.InstantColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

public class TemporalColumnJoinHelper {

	public enum InterpolationMode {
		STEP, LINEAR
	}

	public Table joinTables(Map<Table, Column<?>> tables, InterpolationMode mode) {

		//TODO convert to instants
		LocalDateTime firstCommonTimeStamp = maxFirst(tables);
		LocalDateTime lastCommonTimeStamp = minLast(tables);
		
		List<LocalDateTime> commonTimeAxis = createTargetTimes(tables, firstCommonTimeStamp, lastCommonTimeStamp);
		
		//TODO Interpolate values (linear/step)
		//TODO perform the actual join
		
		return null;
	}
	
	/**
	 * For a map of time series tables, this method finds the highest first timestamp (maximum of minimums), 
	 * which is the earliest point in time that is covered by all involved time series.  
	 * @param tables the tables
	 * @return the local date time
	 */
	private static LocalDateTime maxFirst(Map<Table, Column<?>> tables) {
		return tables.entrySet().stream().map(e -> ((DateTimeColumn) e.getValue()).min()).max(LocalDateTime::compareTo).orElseThrow();
	}

	/**
	 * For a map of time series tables, this method finds the lowest last timestamp (minimum of maximums), 
	 * which is the latest point in time that is covered by all involved time series.
	 * @param tables the tables
	 * @return the local date time
	 */
	private static LocalDateTime minLast(Map<Table, Column<?>> tables) {
		return tables.entrySet().stream().map(e -> ((DateTimeColumn) e.getValue()).max()).min(LocalDateTime::compareTo).orElseThrow();
	}
	
	private static List<LocalDateTime> createTargetTimes(Map<Table, Column<?>> tables, LocalDateTime start,	LocalDateTime end) {

		Set<LocalDateTime> times = new TreeSet<>();

		for (Column<?> column : tables.values()) {

			DateTimeColumn timeColumn = (DateTimeColumn) column;

			for (LocalDateTime time : timeColumn) {
				if (!time.isBefore(start) && !time.isAfter(end)) {
					times.add(time);
				}
			}
		}

		return new ArrayList<>(times);
	}
	
	private static Instant toInstant(Object value, ZoneId zone) {

		// --- Already an instant -> return unchanged
		if (value instanceof Instant instant) {
			return instant;
		}

		// --- LocalDateTime -> convert to instant
		if (value instanceof LocalDateTime localDateTime) {
			return localDateTime.atZone(zone).toInstant();
		}

		throw new IllegalArgumentException("Unsupported temporal type: " + value.getClass().getSimpleName());
	}
	
	private static List<Instant> readTimesAsInstant(Column<?> column, ZoneId zone) {

		// --- Check if the time column is of a supported type ----------------
		if (!(column instanceof InstantColumn) && !(column instanceof DateTimeColumn)) {
			throw new IllegalArgumentException(
					"Zeitspalte muss InstantColumn oder " + "DateTimeColumn sein: " + column.name());
		}

		// --- Go over the timestamps, convert to instant if necessary --------
		List<Instant> times = new ArrayList<>();

		for (int i = 0; i < column.size(); i++) {
			if (column.isMissing(i)) {
				throw new IllegalArgumentException("Missing timestap in " + column.name() + ", row " + i);
			}

			times.add(toInstant(column.get(i), zone));
		}

		return times;
	}
}
