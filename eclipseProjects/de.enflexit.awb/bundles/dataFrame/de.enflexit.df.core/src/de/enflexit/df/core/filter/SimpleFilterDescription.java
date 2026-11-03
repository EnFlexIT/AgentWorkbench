package de.enflexit.df.core.filter;

import de.enflexit.df.core.processing.transformation.filter.Filters.FilterOperator;

public class SimpleFilterDescription extends AbstractFilterDescription {
	
	private String columnId;
	private FilterOperator filterOperator;
	private String value;
	private String upperValue;
	
	public String getColumnId() {
		return columnId;
	}
	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	public FilterOperator getFilterOperator() {
		return filterOperator;
	}
	public void setFilterOperator(FilterOperator filterOperator) {
		this.filterOperator = filterOperator;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getUpperValue() {
		return upperValue;
	}
	public void setUpperValue(String upperValue) {
		this.upperValue = upperValue;
	}
	
}
