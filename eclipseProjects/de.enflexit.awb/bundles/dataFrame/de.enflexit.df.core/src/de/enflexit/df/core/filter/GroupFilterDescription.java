package de.enflexit.df.core.filter;

import java.util.ArrayList;

import de.enflexit.df.core.processing.transformation.filter.Filters.LogicalOperator;

public class GroupFilterDescription extends AbstractFilterDescription {
	
	private LogicalOperator logicalOperator;
	private ArrayList<AbstractFilterDescription> subFilters;
	
	public LogicalOperator getLogicalOperator() {
		return logicalOperator;
	}
	public void setLogicalOperator(LogicalOperator logicalOperator) {
		this.logicalOperator = logicalOperator;
	}
	public ArrayList<AbstractFilterDescription> getSubFilters() {
		if (subFilters==null) {
			subFilters = new ArrayList<AbstractFilterDescription>();
		}
		return subFilters;
	}
	public void setSubFilters(ArrayList<AbstractFilterDescription> subFilters) {
		this.subFilters = subFilters;
	}
	
}
