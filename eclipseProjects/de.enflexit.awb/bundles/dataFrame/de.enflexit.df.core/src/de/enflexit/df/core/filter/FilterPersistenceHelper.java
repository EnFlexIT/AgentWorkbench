package de.enflexit.df.core.filter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.enflexit.df.core.processing.transformation.filter.DateTimeInputParser;
import de.enflexit.df.core.processing.transformation.filter.Filters;
import de.enflexit.df.core.processing.transformation.filter.Filters.BetweenFilter;
import de.enflexit.df.core.processing.transformation.filter.Filters.ComparisonFilter;
import de.enflexit.df.core.processing.transformation.filter.Filters.FilterCondition;
import de.enflexit.df.core.processing.transformation.filter.Filters.FilterExpression;
import de.enflexit.df.core.processing.transformation.filter.Filters.FilterGroup;
import de.enflexit.df.core.processing.transformation.filter.Filters.FilterOperator;
import de.enflexit.df.core.processing.transformation.filter.Filters.NullFilter;
import de.enflexit.df.core.processing.transformation.filter.Filters.TableFilter;
import tech.tablesaw.api.BooleanColumn;
import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.DateTimeColumn;
import tech.tablesaw.api.DoubleColumn;
import tech.tablesaw.api.FloatColumn;
import tech.tablesaw.api.InstantColumn;
import tech.tablesaw.api.IntColumn;
import tech.tablesaw.api.LongColumn;
import tech.tablesaw.api.ShortColumn;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

public class FilterPersistenceHelper {
	
	private static Gson gson = new GsonBuilder().registerTypeAdapter(AbstractFilterDescription.class, new FilterDescriptionAdapter()).create();
	
	/**
	 * Creates a JSON representation of the provided {@link FilterExpression}.
	 * @param filterExpression the filter expression
	 * @param tableUUID the table UUID
	 * @return the string
	 */
	public static String toJsonString(FilterExpression filterExpression, String tableUUID) {
		return gson.toJson(toFilterDescription(filterExpression, tableUUID), AbstractFilterDescription.class);
	}
	
	/**
	 * Creates an intermediate format for describing filters in a unified way.
	 * @param filterExpression the filter expression
	 * @param tableUUID the table UUID
	 * @return the abstract filter description
	 */
	public static AbstractFilterDescription toFilterDescription(FilterExpression filterExpression, String tableUUID) {
		
		if (filterExpression instanceof FilterGroup fg) {
			
			GroupFilterDescription groupFilterDescription = new GroupFilterDescription();
			groupFilterDescription.setLogicalOperator(fg.operator());
			
			for (FilterExpression subExpression : fg.expressions()) {
				AbstractFilterDescription subFilterDescription = toFilterDescription(subExpression, tableUUID);
				groupFilterDescription.getSubFilters().add(subFilterDescription);
			}
			
			return groupFilterDescription;
			
		} else if (filterExpression instanceof FilterCondition fc) {
			SimpleFilterDescription simpleFilterDescription = new SimpleFilterDescription();
			
			TableFilter tf = fc.filter();

			simpleFilterDescription.setColumnId(tf.column());
			
			if (tf instanceof NullFilter nf) {
				simpleFilterDescription.setFilterOperator(nf.isNull() ? FilterOperator.IS_NULL : FilterOperator.IS_NOT_NULL);
			} else if (tf instanceof BetweenFilter bf) {
				simpleFilterDescription.setFilterOperator(FilterOperator.BETWEEN);
				simpleFilterDescription.setValue(bf.lower().toString());
				simpleFilterDescription.setUpperValue(bf.upper().toString());
			} else if (tf instanceof ComparisonFilter cf) {
				simpleFilterDescription.setFilterOperator(cf.operator());
				simpleFilterDescription.setValue(cf.value().toString());
			}
			
			return simpleFilterDescription;
			
		} else {
			throw new IllegalArgumentException("Unknown filter tpe: " + filterExpression.getClass().getName());
		}
	}
	
	
	/**
	 * Creates a filter expression from the provided JSON string.
	 * @param jsonString the JSON string
	 * @param dataController the data controller
	 * @return the filter expression
	 */
	public static FilterExpression fromJsonString(String jsonString, Table table) {
		AbstractFilterDescription filterDescription = gson.fromJson(jsonString, AbstractFilterDescription.class);
		return fromFilterDescription(filterDescription, table);
	}
	
	/**
	 * Creates a filter expression from the provided filter description.
	 * @param filterDescription the filter description
	 * @param dataController the data controller
	 * @return the filter expression
	 */
	public static FilterExpression fromFilterDescription(AbstractFilterDescription filterDescription, Table table) {
		
		// --- Handle group filters ---------------------------------
		if (filterDescription instanceof GroupFilterDescription gfd) {
			
			// --- Collect the expressions for the sub filters ------
			ArrayList<FilterExpression> subFilterExpressions = new ArrayList<Filters.FilterExpression>();
			for (AbstractFilterDescription subFilterDescription : gfd.getSubFilters()) {
				FilterExpression subFilterExpression = fromFilterDescription(subFilterDescription, table);
				subFilterExpressions.add(subFilterExpression);
			}
			
			// --- Create the correct group filter ------------------
			switch(gfd.getLogicalOperator()) {
			case AND:
				return Filters.and(subFilterExpressions);
			case OR:
				return Filters.or();
			}
			
		// --- Handle simple filters --------------------------------
		} else if (filterDescription instanceof SimpleFilterDescription sfd) {

			String columnName = sfd.getColumnId();
			Column<?> column = table.column(columnName);
			if (column==null) {
				throw new IllegalArgumentException("Column " + columnName +" not found for table " + table.name());
			}
			
			// --- Create the corresponding filter expression based on the filter operation
			FilterExpression tf = null;
			switch(sfd.getFilterOperator()) {
			case IS_NULL:
				tf = Filters.isNull(columnName);
				break;
			case IS_NOT_NULL:
				tf = Filters.isNotNull(columnName);
				break;
			case BETWEEN:
				Object lowerValue = getFilterValueFromString(column, sfd.getValue());
				Object upperValue = getFilterValueFromString(column, sfd.getUpperValue());
				tf = Filters.between(columnName, lowerValue, upperValue);
				break;
			default:
				Object compareValue = getFilterValueFromString(column, sfd.getValue());
				tf = Filters.compare(columnName, sfd.getFilterOperator(), compareValue);
			}
			
			return tf;
		}
		throw new IllegalArgumentException("Unexpected filter description type: " + filterDescription.getClass().getSimpleName());
	}
	
	/**
	 * Gets the filter value object according to the target column type from the value string.
	 * @param targetColumn the target column
	 * @param valueString the value string
	 * @return the filter object
	 */
	private static Object getFilterValueFromString(Column<?> targetColumn, String valueString) {
		
		switch(targetColumn) {
		case StringColumn c : return valueString;
		case BooleanColumn c : return Boolean.parseBoolean(valueString);
		case DoubleColumn c: return Double.parseDouble(valueString);
		case FloatColumn c: return Float.parseFloat(valueString);
		case IntColumn c: return Integer.parseInt(valueString);
		case LongColumn c: return Long.parseLong(valueString);
		case ShortColumn c: return Short.parseShort(valueString);
		case DateColumn c: return DateTimeInputParser.parseDate(valueString);
		case DateTimeColumn c: return DateTimeInputParser.parseDateTime(valueString);
		case InstantColumn c: return toInstant(DateTimeInputParser.parseDateTime(valueString));
		default:
			throw new IllegalArgumentException("Unexpected column type: " + targetColumn.getClass().getSimpleName());
		}
	}
	
	private static Instant toInstant(LocalDateTime ldt) {
		return ldt.atZone(ZoneId.systemDefault()).toInstant();
	}
	
	
}
