package de.enflexit.df.core.processing.transformation;

import java.util.List;
import java.util.Objects;

/**
 * This class provides basic constructs and factory methods to generate table row filters. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public final class Filters {
	
	/**
	 * The possible filter operations.
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 */
	public enum FilterOperator {
		EQUALS,

		GREATER_THAN, LESS_THAN, GREATER_OR_EQUAL, LESS_OR_EQUAL,

		BETWEEN,

		IS_NULL, IS_NOT_NULL
	}
	
	/**
	 * Logical operators to combine filters.
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 */
	public enum LogicalOperator {
		AND, OR
	}
	
	
	/**
	 * Shared interface for all types of filters.
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 */
	public sealed interface TableFilter permits ComparisonFilter, BetweenFilter, NullFilter {
		String column();
	}
	
	/**
	 * Filter for all operations that compare to one value (equal, greater, less etc).
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 * @param column the column
	 * @param operator the operator
	 * @param value the value
	 */
	public record ComparisonFilter(String column, FilterOperator operator, Object value) implements TableFilter {
		
		/**
		 * Instantiates a new ComparsonFilter.
		 * @param column the column to apply the filter on
		 * @param operator the filter operator
		 * @param value the value
		 */
		public ComparisonFilter {
			Objects.requireNonNull(column, "column");
			Objects.requireNonNull(operator, "operator");
			Objects.requireNonNull(value, "value");

			if (operator == FilterOperator.BETWEEN || operator == FilterOperator.IS_NULL
					|| operator == FilterOperator.IS_NOT_NULL) {
				throw new IllegalArgumentException("Operator " + operator + " is not valid for a ComparisonFilter");
			}
		}
	}
	
	/**
	 * Filter for between checks. 
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 * @param column the column to apply the filter on
	 * @param lower the lower bound
	 * @param upper the upper bound
	 */
	public record BetweenFilter(String column, Object lower, Object upper) implements TableFilter {
		
		/**
		 * Instantiates a new between filter.
		 * @param column the column to apply the filter on
		 * @param lower the lower bound
		 * @param upper the upper bound
		 */
		public BetweenFilter {
			Objects.requireNonNull(column, "column");
			Objects.requireNonNull(lower, "lower");
			Objects.requireNonNull(upper, "upper");
		}
	}
	
	/**
	 * Filter for null and not null checks.
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 * @param column the column
	 * @param isNull the is null
	 */
	public record NullFilter(String column, boolean isNull) implements TableFilter {
		
		/**
		 * Instantiates a new null filter.
		 * @param column the column to apply the filter on
		 * @param isNull the is null
		 */
		public NullFilter {
			Objects.requireNonNull(column, "column");
		}

		/**
		 * Creates a new filter for null values.
		 * @param column the column to apply the filter on
		 * @return the filter
		 */
		public static NullFilter isNull(String column) {
			return new NullFilter(column, true);
		}

		/**
		 * Creates a new filter for non-null values.
		 * @param column the column to apply the filter on
		 * @return the filter
		 */
		public static NullFilter isNotNull(String column) {
			return new NullFilter(column, false);
		}
	}
	
	/**
	 * Common interface for single filters or filter groups
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 */
	public sealed interface FilterExpression permits FilterCondition, FilterGroup {
	}

	/**
	 * The Record FilterCondition.
	 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
	 * @param filter the filter
	 */
	public record FilterCondition(TableFilter filter) implements FilterExpression {
	}

	public record FilterGroup(LogicalOperator operator, List<FilterExpression> expressions)	implements FilterExpression {

		public FilterGroup {
			Objects.requireNonNull(operator);
			Objects.requireNonNull(expressions);

			if (expressions.isEmpty()) {
				throw new IllegalArgumentException("A filter group must not be empty");
			}
		}
	}
	
	// --------------------------------------------------------------
	// --- Factory methods for the different filter types -----------
	
	
	/**
	 * Instantiates a new filters.
	 */
	private Filters() {}

	/**
	 * Creates a new equals filter
	 * @param column the column to apply the filter on
	 * @param value the value to compare with
	 * @return the filter expression
	 */
	public static FilterExpression equals(String column, Object value) {
		return new FilterCondition(new ComparisonFilter(column, FilterOperator.EQUALS, value));
	}

	/**
	 * Creates a new  greater than filter.
	 * @param column the column to apply the filter on
	 * @param value the value to compare with
	 * @return the filter expression
	 */
	public static FilterExpression greaterThan(String column, Object value) {
		return new FilterCondition(new ComparisonFilter(column, FilterOperator.GREATER_THAN, value));
	}

	/**
	 * Creates a new  greater or equal filter.
	 * @param column the column to apply the filter on
	 * @param value the value to compare with
	 * @return the filter expression
	 */
	public static FilterExpression greaterOrEqual(String column, Object value) {
		return new FilterCondition(new ComparisonFilter(column, FilterOperator.GREATER_OR_EQUAL, value));
	}

	/**
	 * Creates a new  less than filter.
	 * @param column the column to apply the filter on
	 * @param value the value to compare with
	 * @return the filter expression
	 */
	public static FilterExpression lessThan(String column, Object value) {
		return new FilterCondition(new ComparisonFilter(column, FilterOperator.LESS_THAN, value));
	}

	/**
	 * Creates a new less or equal filter.
	 * @param column the column to apply the filter on
	 * @param value the value to compare with
	 * @return the filter expression
	 */
	public static FilterExpression lessOrEqual(String column, Object value) {
		return new FilterCondition(new ComparisonFilter(column, FilterOperator.LESS_OR_EQUAL, value));
	}

	/**
	 * Creates a new between filter.
	 * @param column the column to apply the filter on
	 * @param lower the lower bound
	 * @param upper the upper bound
	 * @return the filter expression
	 */
	public static FilterExpression between(String column, Object lower, Object upper) {
		return new FilterCondition(new BetweenFilter(column, lower, upper));
	}

	/**
	 * Creates a new null filter.
	 * @param column the column to apply the filter on
	 * @return the filter expression
	 */
	public static FilterExpression isNull(String column) {
		return new FilterCondition(NullFilter.isNull(column));
	}

	/**
	 * Creates a new not null filter.
	 * @param column the column to apply the filter on
	 * @return the filter expression
	 */
	public static FilterExpression isNotNull(String column) {
		return new FilterCondition(NullFilter.isNotNull(column));
	}

	/**
	 * Creates an and concatenation of filters-
	 * @param expressions the expressions
	 * @return the filter expression
	 */
	public static FilterExpression and(FilterExpression... expressions) {
		return new FilterGroup(LogicalOperator.AND, List.of(expressions));
	}

	/**
	 * Creates an or concatenation of filters.
	 * @param expressions the expressions
	 * @return the filter expression
	 */
	public static FilterExpression or(FilterExpression... expressions) {
		return new FilterGroup(LogicalOperator.OR, List.of(expressions));
	}

}
