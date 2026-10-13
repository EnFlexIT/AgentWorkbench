package de.enflexit.df.core.processing.transformation.filter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

import de.enflexit.df.core.processing.transformation.filter.Filters.BetweenFilter;
import de.enflexit.df.core.processing.transformation.filter.Filters.ComparisonFilter;
import de.enflexit.df.core.processing.transformation.filter.Filters.FilterCondition;
import de.enflexit.df.core.processing.transformation.filter.Filters.FilterExpression;
import de.enflexit.df.core.processing.transformation.filter.Filters.FilterGroup;
import de.enflexit.df.core.processing.transformation.filter.Filters.FilterOperator;
import de.enflexit.df.core.processing.transformation.filter.Filters.LogicalOperator;
import de.enflexit.df.core.processing.transformation.filter.Filters.NullFilter;
import de.enflexit.df.core.processing.transformation.filter.Filters.TableFilter;
import tech.tablesaw.api.BooleanColumn;
import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.DateTimeColumn;
import tech.tablesaw.api.InstantColumn;
import tech.tablesaw.api.NumericColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;
import tech.tablesaw.selection.Selection;

/**
 * This class is responsible to apply the general filter concept from {@link Filters} on TableSaw tables.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public final class TableSawFilter {
	
	private static final ZoneId SYSTEM_ZONE = ZoneId.systemDefault();
	
	/**
	 * Instantiates a new table saw filter.
	 */
	private TableSawFilter() {
	}

	/**
	 * Filters the provided table, using the specified filter expression.
	 * @param table the table
	 * @param expression the expression
	 * @return the table
	 */
	public static Table filter(Table table, FilterExpression expression) {
		Selection selection = toSelection(table, expression);
		return table.where(selection);
	}

	/**
	 * Creates a TableSaw Selection from the provided filter expression.
	 * @param table the table
	 * @param expression the expression
	 * @return the selection
	 */
	public static Selection toSelection(Table table, FilterExpression expression) {
		return switch (expression) {
			case FilterCondition condition -> toSelection(table, condition.filter());
			case FilterGroup group -> toSelection(table, group);
		};
	}

	/**
	 * Creates a TableSaw Selection for a FilterGroup.
	 * @param table the table
	 * @param filterGroup the group
	 * @return the selection
	 */
	private static Selection toSelection(Table table, FilterGroup filterGroup) {
		List<Selection> selections = filterGroup.expressions().stream().map(expression -> toSelection(table, expression))
				.toList();

		Selection result = selections.getFirst();

		for (int i = 1; i < selections.size(); i++) {
			Selection next = selections.get(i);

			if (filterGroup.operator() == LogicalOperator.AND) {
				result.and(next);
			} else {
				result.or(next);
			}
		}

		return result;
	}

	/**
	 * Creates a TableSaw Selection for a single table filter.
	 * @param table the table
	 * @param filter the filter
	 * @return the selection
	 */
	private static Selection toSelection(Table table, TableFilter filter) {
		Column<?> column = getColumn(table, filter.column());

		return switch (column) {
			case StringColumn c -> filterString(c, filter);
			case BooleanColumn c -> filterBoolean(c, filter);
			case NumericColumn<?> c -> filterNumeric(c, filter);
			case DateColumn c -> filterDate(c, filter);
			case DateTimeColumn c -> filterDateTime(c, filter);
			case InstantColumn c -> filterInstant(c, filter);
			default -> throw unsupportedColumn(column);
		};
	}

	/**
	 * Gets the column with the specified name from the provided table.
	 * @param table the table
	 * @param columnName the column name
	 * @return the column
	 */
	private static Column<?> getColumn(Table table, String columnName) {
		if (table.columnNames().contains(columnName)==false) {
			throw new IllegalArgumentException("Unknown column: " + columnName);
		}
		return table.column(columnName);
	}

	/**
	 * Creates a TableSaw Selection for a string columns.
	 * @param column the column
	 * @param filter the filter
	 * @return the selection
	 */
	private static Selection filterString(StringColumn column, TableFilter filter) {
		
		// --- Return a selection for null or non-null values -------
		if (filter instanceof NullFilter nullFilter) {
			return nullFilter.isNull() ? column.isMissing() : column.isNotMissing();
		}

		// --- Between filter makes no sense for string columns -----
		if (!(filter instanceof ComparisonFilter comparison)) {
			throw unsupportedFilter(filter, "String");
		}

		// --- Greater and less make no sense for string columns ----
		if (comparison.operator() != FilterOperator.EQUALS) {
			throw unsupportedOperator(comparison.operator(), "String");
		}

		// --- Perform the actual string comparison------------------
		String value = requireType(comparison.value(), String.class);
		return column.isEqualTo(value);
	}

	/**
	 * Creates a TableSaw Selection for boolean columns.
	 * @param column the column
	 * @param filter the filter
	 * @return the selection
	 */
	private static Selection filterBoolean(BooleanColumn column, TableFilter filter) {
		
		// --- Return a selection for null or non-null values -------
		if (filter instanceof NullFilter nullFilter) {
			return nullFilter.isNull() ? column.isMissing() : column.isNotMissing();
		}

		// --- Between filter makes no sense for boolean columns ----
		if (!(filter instanceof ComparisonFilter comparison)) {
			throw unsupportedFilter(filter, "Boolean");
		}

		// --- Greater and less make no sense for boolean columns ---
		if (comparison.operator() != FilterOperator.EQUALS) {
			throw unsupportedOperator(comparison.operator(), "Boolean");
		}

		// --- Perform the actual boolean comparison ----------------
		boolean value = requireType(comparison.value(), Boolean.class);
		return value ? column.isTrue() : column.isFalse();
	}
	
	/**
	 * Creates a TableSaw Selection for date columns.
	 * @param column the column
	 * @param filter the filter
	 * @return the selection
	 */
	private static Selection filterDate(DateColumn column, TableFilter filter) {
		
		// --- Return a selection for null or non-null values -------
		if (filter instanceof NullFilter nullFilter) {
			return nullFilter.isNull() ? column.isMissing() : column.isNotMissing();
		}

		// --- Handle comparison filters using the TableSaw mechanisms --------
		if (filter instanceof ComparisonFilter comparison) {

			LocalDate value = requireTemporalType(comparison.value(), LocalDate.class);
			return switch (comparison.operator()) {
				case EQUALS -> column.isEqualTo(value);
				case GREATER_THAN -> column.isAfter(value);
				case LESS_THAN -> column.isBefore(value);
				case GREATER_OR_EQUAL -> column.isOnOrAfter(value);
				case LESS_OR_EQUAL -> column.isOnOrBefore(value);
				default -> throw unsupportedOperator(comparison.operator(), "LocalDate");
			};
		}

		// --- Handle between filters using the TableSaw mechanisms -----------
		if (filter instanceof BetweenFilter between) {
			LocalDate lower = requireTemporalType(between.lower(), LocalDate.class);
			LocalDate upper = requireTemporalType(between.upper(), LocalDate.class);
			return column.isBetweenIncluding(lower, upper);
		}

		throw unsupportedFilter(filter, "LocalDate");
	}

	/**
	 * Creates a TableSaw Selection for date time columns.
	 * @param column the column
	 * @param filter the filter
	 * @return the selection
	 */
	private static Selection filterDateTime(DateTimeColumn column, TableFilter filter) {
		
		// --- Return a selection for null or non-null values -------
		if (filter instanceof NullFilter nullFilter) {
			return nullFilter.isNull() ? column.isMissing() : column.isNotMissing();
		}

		// --- Handle comparison filters using the TableSaw mechanisms --------
		if (filter instanceof ComparisonFilter comparison) {

			LocalDateTime value = requireTemporalType(comparison.value(), LocalDateTime.class);
			return switch (comparison.operator()) {
				case EQUALS -> column.isEqualTo(value);
				case GREATER_THAN -> column.isAfter(value);
				case LESS_THAN -> column.isBefore(value);
				case GREATER_OR_EQUAL -> column.isOnOrAfter(value);
				case LESS_OR_EQUAL -> column.isOnOrBefore(value);
				default -> throw unsupportedOperator(comparison.operator(), "LocalDateTime");
			};
		}

		// --- Handle between filters using the TableSaw mechanisms -----------
		if (filter instanceof BetweenFilter between) {
			LocalDateTime lower = requireTemporalType(between.lower(), LocalDateTime.class);
			LocalDateTime upper = requireTemporalType(between.upper(), LocalDateTime.class);
			return column.isBetweenIncluding(lower, upper);
		}
		throw unsupportedFilter(filter, "LocalDateTime");
	}
	
	private static Selection filterInstant(InstantColumn column, TableFilter filter) {
		
		// --- Return a selection for null or non-null values -------
		if (filter instanceof NullFilter nullFilter) {
			return nullFilter.isNull() ? column.isMissing() : column.isNotMissing();
		}

		// --- Handle comparison filters using the TableSaw mechanisms --------
		if (filter instanceof ComparisonFilter comparison) {

			Instant value = requireTemporalType(comparison.value(), Instant.class);
			return switch (comparison.operator()) {
				case EQUALS -> column.isEqualTo(value);
				case GREATER_THAN -> column.isAfter(value);
				case LESS_THAN -> column.isBefore(value);
				case GREATER_OR_EQUAL -> column.isAfter(value).or(column.isEqualTo(value));
				case LESS_OR_EQUAL -> column.isBefore(value).or(column.isEqualTo(value));
				default -> throw unsupportedOperator(comparison.operator(), "Instant");
			};
		}

		// --- Handle between filters using the TableSaw mechanisms -----------
		if (filter instanceof BetweenFilter between) {
			Instant lower = requireTemporalType(between.lower(), Instant.class);
			Instant upper = requireTemporalType(between.upper(), Instant.class);
			
			Selection lowerBound = column.isAfter(lower).or(column.isEqualTo(lower));
			Selection upperBound = column.isBefore(upper).or(column.isEqualTo(upper));
			
			return lowerBound.and(upperBound);
		}
		throw unsupportedFilter(filter, "Instant");
	}

	
	/**
	 * Creates a TableSaw Selection for numeric columns.
	 * @param column the column
	 * @param filter the filter
	 * @return the selection
	 */
	private static Selection filterNumeric(NumericColumn<?> column, TableFilter filter) {
		
		// --- Return a selection for null or non-null values -------
		if (filter instanceof NullFilter nullFilter) {
			return nullFilter.isNull() ? column.isMissing() : column.isNotMissing();
		}

		// --- Handle comparison filters using the TableSaw mechanisms --------
		if (filter instanceof ComparisonFilter comparison) {
			Number value = requireNumber(comparison.value());

			return switch (comparison.operator()) {
				case EQUALS -> numericEquals(column, value);
				case GREATER_THAN -> numericGreaterThan(column, value);
				case LESS_THAN -> numericLessThan(column, value);
				case GREATER_OR_EQUAL -> numericGreaterOrEqual(column, value);
				case LESS_OR_EQUAL -> numericLessOrEqual(column, value);
				default -> throw unsupportedOperator(comparison.operator(), "numeric");
			};
		}

		// --- Handle between filters using the TableSaw mechanisms -----------
		if (filter instanceof BetweenFilter between) {
			Number lower = requireNumber(between.lower());
			Number upper = requireNumber(between.upper());
			return numericBetween(column, lower, upper);
		}

		throw unsupportedFilter(filter, "numeric");
	}
	
	// ----------------------------------------------------
	// --- Helper methods for numeric filters  ------------

	/**
	 * Numeric equals.
	 * @param column the column
	 * @param value the value
	 * @return the selection
	 */
	private static Selection numericEquals(NumericColumn<?> column, Number value) {
		return column.isNotMissing().and(column.eval(TableSawFilter::numbersEqual, value));
	}

	/**
	 * Numeric greater than.
	 * @param column the column
	 * @param value the value
	 * @return the selection
	 */
	private static Selection numericGreaterThan(NumericColumn<?> column, Number value) {
		return column.isNotMissing().and(column.eval((actual, expected) -> compareNumbers(actual, expected) > 0, value));
	}

	/**
	 * Numeric less than.
	 * @param column the column
	 * @param value the value
	 * @return the selection
	 */
	private static Selection numericLessThan(NumericColumn<?> column, Number value) {
		return column.isNotMissing().and(column.eval((actual, expected) -> compareNumbers(actual, expected) < 0, value));
	}

	/**
	 * Numeric greater or equal.
	 * @param column the column
	 * @param value the value
	 * @return the selection
	 */
	private static Selection numericGreaterOrEqual(NumericColumn<?> column, Number value) {
		return column.isNotMissing().and(column.eval((actual, expected) -> compareNumbers(actual, expected) >= 0, value));
	}

	/**
	 * Numeric less or equal.
	 * @param column the column
	 * @param value the value
	 * @return the selection
	 */
	private static Selection numericLessOrEqual(NumericColumn<?> column, Number value) {
		return column.isNotMissing().and(column.eval((actual, expected) -> compareNumbers(actual, expected) <= 0, value));
	}

	/**
	 * Numeric between.
	 * @param column the column
	 * @param lower the lower
	 * @param upper the upper
	 * @return the selection
	 */
	private static Selection numericBetween(NumericColumn<?> column, Number lower, Number upper) {
		return column.isNotMissing().and(column.eval((actual, ignored) -> compareNumbers(actual, lower) >= 0 && compareNumbers(actual, upper) <= 0, 0));
	}

	/**
	 * Numbers equal.
	 * @param left the left
	 * @param right the right
	 * @return true, if successful
	 */
	private static boolean numbersEqual(Number left, Number right) {
		return compareNumbers(left, right) == 0;
	}

	/**
	 * Compare two numeric values.
	 * @param first the first number
	 * @param second the second number
	 * @return the compare result
	 */
	private static int compareNumbers(Number first, Number second) {
		if (isFloatingPoint(first) || isFloatingPoint(second)) {
			double a = first.doubleValue();
			double b = second.doubleValue();
			return Double.compare(a, b);
		}
		return Long.compare(first.longValue(), second.longValue());
	}
	
	/**
	 * Checks if the provided number is of a floating point type
	 * @param value the number to check
	 * @return true, if is floating point
	 */
	private static boolean isFloatingPoint(Number value) {
		return value instanceof Float || value instanceof Double;
	}
	
	
	// --------------------------------------------------------------
	// --- Helper methods for type checks  and error handling -------

	/**
	 * Returns the numeric value, if the provided object is of a numeric type  .
	 * @param value the value
	 * @return the number
	 */
	private static Number requireNumber(Object value) {
		if (value instanceof Number number) {
			return number;
		}
		throw new IllegalArgumentException("Expected a numeric value, but got: " + getClassName(value));
	}

	
	/**
	 * Casts an object to the specified type if possible. Throws an {@link IllegalArgumentException} otherwise.
	 * @param <T> the generic type
	 * @param value the value
	 * @param expectedType the expected type
	 * @return the casted object
	 */
	private static <T> T requireType(Object value, Class<T> expectedType) {
		if (expectedType.isInstance(value)) {
			return expectedType.cast(value);
		}

		throw new IllegalArgumentException("Expected value of type " + expectedType.getSimpleName() + ", but got " + getClassName(value));
	}
	
	/**
	 * Casts an object to the specified temporal type if possible. Throws an {@link IllegalArgumentException} otherwise.
	 * Includes type conversions between different temporal data types.
	 * @param <T> the generic type
	 * @param value the value
	 * @param targetType the target type
	 * @return the t
	 */
	private static <T> T requireTemporalType(Object value, Class<T> targetType) {

		Objects.requireNonNull(value, "value");

		if (targetType.isInstance(value)) {
			return targetType.cast(value);
		}

		Object converted = null;

		if (targetType == Instant.class) {
			if (value instanceof LocalDateTime dateTime) {
				converted = dateTime.atZone(SYSTEM_ZONE).toInstant();
			} else if (value instanceof LocalDate date) {
				converted = date.atStartOfDay(SYSTEM_ZONE).toInstant();
			}
		} else if (targetType == LocalDateTime.class) {
			if (value instanceof Instant instant) {
				converted = LocalDateTime.ofInstant(instant, SYSTEM_ZONE);
			} else if (value instanceof LocalDate date) {
				converted = date.atStartOfDay();
			}
		} else if (targetType == LocalDate.class) {
			if (value instanceof LocalDateTime dateTime) {
				converted = dateTime.toLocalDate();
			} else if (value instanceof Instant instant) {
				converted = instant.atZone(SYSTEM_ZONE).toLocalDate();
			}
		}

		if (converted != null) {
			return targetType.cast(converted);
		}

		throw new IllegalArgumentException(
				"Cannot convert " + value.getClass().getSimpleName() + " to " + targetType.getSimpleName());
	}

	/**
	 * Gets the class name for the provided object, handles the null case-
	 * @param value the value
	 * @return the string
	 */
	private static String getClassName(Object value) {
		return value == null ? "null" : value.getClass().getName();
	}

	/**
	 * Creates an {@link IllegalArgumentException} if a {@link FilterOperator} is not supported for a column type.
	 * @param filterOperator the filter operator
	 * @param columnType the column type
	 * @return the illegal argument exception
	 */
	private static IllegalArgumentException unsupportedOperator(FilterOperator filterOperator, String columnType) {
		return new IllegalArgumentException("Operator " + filterOperator + " is not supported for " + columnType);
	}

	/**
	 * Creates an {@link IllegalArgumentException} if a {@link TableFilter} is not supported for a column type.
	 * @param tableFilter the table filter
	 * @param columnType the column type
	 * @return the illegal argument exception
	 */
	private static IllegalArgumentException unsupportedFilter(TableFilter tableFilter, String columnType) {
		return new IllegalArgumentException(
				"Filter " + tableFilter.getClass().getSimpleName() + " is not supported for " + columnType);
	}

	/**
	 * Creates an {@link IllegalArgumentException} if a column type is not supported.
	 * @param column the column
	 * @return the illegal argument exception
	 */
	private static IllegalArgumentException unsupportedColumn(Column<?> column) {
		return new IllegalArgumentException("Unsupported TableSaw column type: " + column.getClass().getName());
	}

}
