package de.enflexit.df.core.processing.transformation.join;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import de.enflexit.df.core.processing.transformation.join.DataTransformationJoinTables.InterpolationMode;
import tech.tablesaw.api.BooleanColumn;
import tech.tablesaw.api.DateTimeColumn;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.InstantColumn;
import tech.tablesaw.api.NumericColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

/**
 * THis class provides methods to properly join time series tables with different time ranges and steps.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TimeSeriesJoinHelper {
	
	/**
	 * Joins the provided tables on their time columns. Times will be unified: 
	 * - The resulting table will contain the common time range that is covered by all input tables
	 * - It will contain values for all timestamps from all tables. Missing values will be determined    
	 *   according to the specified {@link InterpolationMode}:
	 * @param inputTables the original tables
	 * @param interpolationMode the interpolation mode to use
	 * @param timeZoneID the time zone to use for converting {@link LocalDateTime}s to {@link Instant}s.
	 * @return the resulting table
	 */
	public static Table joinTables(Map<Table, Column<?>> inputTables, InterpolationMode interpolationMode, ZoneId timeZoneID) {

		if (inputTables == null || inputTables.isEmpty()) {
			throw new IllegalArgumentException("This operation requires at least one table.");
		}

		if (interpolationMode == null || timeZoneID == null) {
			throw new IllegalArgumentException("Interpolation mode and time zone must not be null!");
		}

		// --- Perform several checks, put tables that pass to a new map
		Map<Table, Column<?>> inputs = new LinkedHashMap<>();
		for (Map.Entry<Table, Column<?>> entry : inputTables.entrySet()) {
			Table table = entry.getKey();
			Column<?> timeColumn = entry.getValue();

			if (table == null || timeColumn == null) {
				throw new IllegalArgumentException("Table and time column must not be null!");
			}

			if (!table.columnNames().contains(timeColumn.name())) {
				throw new IllegalArgumentException("Time column '" + timeColumn.name() + "' not found in table '" + table.name() + "'.");
			}

			if (!(timeColumn instanceof InstantColumn) && !(timeColumn instanceof DateTimeColumn)) {
				throw new IllegalArgumentException("Unsupported temporal column type: " + timeColumn.type());
			}

			if (table.rowCount() == 0) {
				throw new IllegalArgumentException("Table '" + table.name() + "' is empty.");
			}

			inputs.put(table, timeColumn);
		}
		
		String joinKey = createUniqueJoinKey(inputs);

		// --- Collect the time entries from all tables -------------
		Map<Table, List<Instant>> timesByTable = new LinkedHashMap<>();
		for (Map.Entry<Table, Column<?>> entry : inputs.entrySet()) {
			
			// --- Make sure the table is sorted --------------------
			Table sorted = entry.getKey().sortOn(entry.getValue().name());
			Column<?> sortedTimeColumn = sorted.column(entry.getValue().name());
			
			// --- Convert to instant for unified handling ----------
			List<Instant> times = readTimesAsInstant(sortedTimeColumn, timeZoneID);
			validateTimes(times, sorted.name());

            timesByTable.put(entry.getKey(), times);
		}
		
		// --- Determine the common time range for all tables -------
		Instant commonStart = findCommonStartTime(timesByTable);
		Instant commonEnd = findCommonEndTime(timesByTable);

		if (commonStart.isAfter(commonEnd)) {
			throw new IllegalArgumentException("The time ranges covered by the tables are not overlapping!");
		}
		
		// --- Collect all timestamps within the common range from all tables 
		Set<Instant> targetSet = new TreeSet<>();	// Using set to eliminate duplicates
		for (List<Instant> times : timesByTable.values()) {
			for (Instant time : times) {
				// --- Only add timestamps within the common range
				if (time.isBefore(commonStart)==false && time.isAfter(commonEnd)==false) {
					targetSet.add(time);
				}
			}
		}
		List<Instant> targetTimes = new ArrayList<>(targetSet);
		
		// --- Map all tables to the common time axis ---------------
		List<Table> normalizedTables = new ArrayList<>();

		for (Map.Entry<Table, Column<?>> entry : inputs.entrySet()) {
			Table normalized = resample(entry.getKey(), entry.getValue().name(), targetTimes, interpolationMode, timeZoneID, joinKey);
			normalizedTables.add(normalized);
	     }
		
		// --- Join the normalized tables on their time columns -----
		Table result = normalizedTables.get(0);
		for (int i = 1; i < normalizedTables.size(); i++) {
			result = result.joinOn(joinKey).rightJoinColumns(joinKey).allowDuplicateColumnNames(true).with(normalizedTables.get(i)).join();
		}

		return result;
	}
	
	/**
	 * Finds the first timestamp that all tables have in common.
	 * @param timesByTable the times by table
	 * @return the instant
	 */
	private static Instant findCommonStartTime(Map<Table, List<Instant>> timesByTable) {
		Instant commonStart = null;

		// --- Iterate over the timestamp lists of all tables ------
		for (List<Instant> tableTimes : timesByTable.values()) {
			
			// --- Find the latest first entry (= the earliest point in time that is covered by all tables).
			Instant firstTimestamp = tableTimes.get(0);
			if (commonStart == null || firstTimestamp.isAfter(commonStart)) {
				commonStart = firstTimestamp;
			}
		}
		
		if (commonStart == null) {
			throw new IllegalStateException("No timestamps found!");
		}
		
		return commonStart;
	}
	
	/**
	 * Find common end time.
	 * @param timesByTable the times by table
	 * @return the instant
	 */
	private static Instant findCommonEndTime(Map<Table, List<Instant>> timesByTable) {
		Instant commonEnd = null;

		// --- Iterate over the timestamp lists of all tables ------
		for (List<Instant> tableTimes : timesByTable.values()) {
			
			// --- Find the earliest last time stamp (= the latest point in time that is covered by all tables).
			Instant lastTimestamp = tableTimes.get(tableTimes.size()-1);
			if (commonEnd == null || lastTimestamp.isBefore(commonEnd)) {
				commonEnd = lastTimestamp;
			}
		}
		
		if (commonEnd == null) {
			throw new IllegalStateException("No timestamps found!");
		}
		
		return commonEnd;
	}
	
	/**
	 * Create a unique name for the join column..
	 * @param inputs the input tables and their time columns
	 * @return the string
	 */
	private static String createUniqueJoinKey(Map<Table, Column<?>> inputs) {

		Set<String> existingNames = new HashSet<>();

		for (Table table : inputs.keySet()) {
			existingNames.addAll(table.columnNames());
		}

		String candidate = "join_time";
		int suffix = 1;

		while (existingNames.contains(candidate)) {
			candidate = "join_time_" + suffix++;
		}

		return candidate;
	}
	
	/**
	 * Read times as instant.
	 * @param column the column
	 * @param zone the zone
	 * @return the list
	 */
	private static List<Instant> readTimesAsInstant(Column<?> column, ZoneId zone) {

		List<Instant> times = new ArrayList<>();

		for (int i = 0; i < column.size(); i++) {
			if (column.isMissing(i)) {
				throw new IllegalArgumentException("Missing time entry in column '" + column.name() + "', row " + i);
			}

			Object value = column.get(i);
			// --- Already an instant, use as it is -------
			if (value instanceof Instant instant) {
				times.add(instant);

			// --- LocalDateTime, convert to instant ------
			} else if (value instanceof LocalDateTime local) {
				times.add(local.atZone(zone).toInstant());

			} else {
				throw new IllegalArgumentException("Unexpected time entry: " + value);
			}
		}

		return times;
	}
	
	/**
	 * Performs some checks on the time entries-
	 * @param times the times
	 * @param tableName the table name
	 */
	private static void validateTimes(List<Instant> times, String tableName) {

		if (times.isEmpty()) {
			throw new IllegalArgumentException("No time entries in table " + tableName);
		}

		for (int i = 1; i < times.size(); i++) {
			int comparison = times.get(i - 1).compareTo(times.get(i));

			if (comparison == 0) {
				throw new IllegalArgumentException("Identical tile entries in " + tableName + ": " + times.get(i));
			}

			if (comparison > 0) {
				throw new IllegalStateException("Time enrties not sorted!");
			}
		}
	}
	
	/**
	 * Resamples the provided source table to the provided target timestamps. Missing 
	 * values will be determined according to the specified {@link InterpolationMode}.
	 * @param sourceTable the source table
	 * @param timeColumnName the time column name
	 * @param targetTimes the target times
	 * @param interpolationMode the interpolation mode
	 * @param timeZoneID the time zone ID
	 * @param joinKey the join key
	 * @return the table
	 */
	private static Table resample(Table sourceTable, String timeColumnName, List<Instant> targetTimes, InterpolationMode interpolationMode, ZoneId timeZoneID, String joinKey) {

		Table sorted = sourceTable.sortOn(timeColumnName);
		List<Instant> sourceTimes = readTimesAsInstant(sorted.column(timeColumnName), timeZoneID);

		// --- Create the result table with a common time column ---- 
		Table resultTable = Table.create(sourceTable.name() + "_resampled");
		InstantColumn resultTimeColumn = InstantColumn.create(joinKey);
		resultTable.addColumns(resultTimeColumn);

		// --- Manage source and result columns --------------------- 
		List<Column<?>> sourceColumns = new ArrayList<>();
		List<Column<?>> targetColumns = new ArrayList<>();

		for (Column<?> column : sorted.columns()) {

			// --- Skip time column ---------------------------------
			if (column.name().equals(timeColumnName)) {
				continue;
			}
			
			// --- Check if a column is empty -----------------------
		    boolean allMissing = true;
		    for (int row = 0; row < column.size(); row++) {
		        if (!column.isMissing(row)) {
		            allMissing = false;
		            break;
		        }
		    }

		    // --- Skip empty columns -------------------------------
		    if (allMissing) {
		        continue;
		    }

			// --- Check for supported column types -----------------
			Column<?> output;

			if (column instanceof NumericColumn<?>) {
				output = DoubleColumn.create(column.name());
			} else if (column instanceof BooleanColumn) {
				output = BooleanColumn.create(column.name());
			} else if (column instanceof StringColumn) {
				output = StringColumn.create(column.name());
			} else {
				throw new IllegalArgumentException("Unsupported column type '" + column.type() + "' for column '" + column.name() + "' in table '" + sourceTable.name() + "'");
			}

			// --- Reject columns with missing values ---------------
			for (int row = 0; row < column.size(); row++) {
				if (column.isMissing(row)) {
					throw new IllegalArgumentException("Missing value in column '" + column.name() + "', row " + row
							+ " of table '" + sourceTable.name() + "'");
				}
			}

			sourceColumns.add(column);
			targetColumns.add(output);
			resultTable.addColumns(output);
		}

		int rightIndex = 0;

		// --- Iterate over all time steps --------------------------
		for (Instant target : targetTimes) {

			while (rightIndex < sourceTimes.size() && sourceTimes.get(rightIndex).isBefore(target)) {
				rightIndex++;
			}

			if (rightIndex >= sourceTimes.size()) {
				throw new IllegalArgumentException("Target time exceeds the time range of " + sourceTable.name() + ": " + target);
			}

			Instant rightTime = sourceTimes.get(rightIndex);

			// --- Exact match: Keep the original value -------------
			if (rightTime.equals(target)) {
				resultTimeColumn.append(target);

				for (int c = 0; c < sourceColumns.size(); c++) {
					
					Column<?> sourceColumn = sourceColumns.get(c);
					Column<?> targetColumn = targetColumns.get(c);
					
					if (sourceColumn instanceof NumericColumn<?> numeric) {
						((DoubleColumn) targetColumn).append(numeric.getDouble(rightIndex));

					} else if (sourceColumn instanceof BooleanColumn bool) {
						((BooleanColumn) targetColumn).append(bool.get(rightIndex));

					} else if (sourceColumn instanceof StringColumn string) {
						((StringColumn) targetColumn).append(string.get(rightIndex));

					} else {
						throw new IllegalArgumentException("Unsupported column type: " + sourceColumn.name());
					}
				}

				continue;
			}

			// --- Target time is between two entries ---------------
			int leftIndex = rightIndex - 1;

			if (leftIndex < 0) {
				throw new IllegalArgumentException("Unable to interpolate: no earlier measurement before " + target + " in table '" + sourceTable.name() + "'");
			}

			Instant leftTime = sourceTimes.get(leftIndex);

			resultTimeColumn.append(target);

			// --- Adjust values according to their data type and the chosen interpolation mode
			for (int c = 0; c < sourceColumns.size(); c++) {
				Column<?> source = sourceColumns.get(c);
				Column<?> output = targetColumns.get(c);

				// --- NUmeric - step or linear ---------------------
				if (source instanceof NumericColumn<?> numeric) {
					double leftValue = numeric.getDouble(leftIndex);
					double value;

					// --- Step -> just take the previous value ----- 
					if (interpolationMode == InterpolationMode.STEP) {
						value = leftValue;
						
					// --- Linear -> interpolate between previous and next
					} else {
						
						// --- Calculate the interpolation factor ---
						double fraction = 0.0;
						long totalNanos = Duration.between(leftTime, rightTime).toNanos();
						long targetNanos = Duration.between(leftTime, target).toNanos();
						fraction = (double) targetNanos / totalNanos;
						
						// --- Calculate the interpolated value -----
						double rightValue = numeric.getDouble(rightIndex);
						value = leftValue + fraction * (rightValue - leftValue);
					}

					((DoubleColumn) output).append(value);

				// --- Boolean or String -> always use step mode ----
				} else if (source instanceof BooleanColumn bool) {
					((BooleanColumn) output).append(bool.get(leftIndex));

				} else if (source instanceof StringColumn string) {
					((StringColumn) output).append(string.get(leftIndex));
				}
			}
		}

		return resultTable;
	}

}
