package de.enflexit.df.core.processing.transformation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;

/**
 * This {@link AbstractDataTransformation} merges two or more input tables to a single table.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataTransformationJoin extends AbstractDataTransformation {
	
	public static final String PARAM_NAME_JOIN_COLUMNS = "joinColumns";
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.AbstractDataTransformation#performTransformation()
	 */
	@Override
	public Table performTransformation() {
		
		Table resultTable = null;
		
		Object joinColumnsParam = this.getTransformationParameters().get(PARAM_NAME_JOIN_COLUMNS);
		
		if (checkConfigurationParameterParamType(joinColumnsParam)==true) {
			@SuppressWarnings("unchecked")
			HashMap<Table, Column<?>> joinTablesMap = (HashMap<Table, Column<?>>) this.getTransformationParameters().get(PARAM_NAME_JOIN_COLUMNS);
			
			resultTable = this.joinTables(joinTablesMap);
			
		} else {
			System.err.println("[" + this.getClass().getSimpleName() + "] Required parameter " + PARAM_NAME_JOIN_COLUMNS + " is missing or of an unexpected data type (must be HashMap<Table, Column<?>>");
		}
		
		return resultTable;
	}
	
	/**
	 * Joins the provided tables. Expects a HashMap with the tables as keys and the corresponding join columns as values. 
	 * @param joinTables the join tables
	 * @return the joined table
	 */
	private Table joinTables(Map<Table, Column<?>> joinTables) {
		
		Iterator<Map.Entry<Table, Column<?>>> tablesIterator = joinTables.entrySet().iterator();
		
		if (tablesIterator.hasNext()==false) {
			System.err.println("[" + this.getClass().getSimpleName() + "] Empty join tables map, at least one entry is required!");
			return null;
		}
		
		Map.Entry<Table, Column<?>> first = tablesIterator.next();
		Table resultTable = first.getKey();
		String leftJoinColumn = first.getValue().name();
		
		while(tablesIterator.hasNext()) {
			Map.Entry<Table, Column<?>> next = tablesIterator.next();
			Table nextTable = next.getKey();
			String nextJoinColumn = next.getValue().name();
			
			resultTable = resultTable.joinOn(leftJoinColumn).rightJoinColumns(nextJoinColumn).with(nextTable).allowDuplicateColumnNames(true).join();
		}
		
		
		return resultTable;
	}
	
	/**
	 * Checks if the provided object has the correct data type to be used as join configuration. The 
	 * required type is HashMap<Table, Column<?>>, providing the join columns for each involved table.
	 * @param joinConfigurationParameter the parameter object to check
	 * @return true, if successful
	 */
	public static boolean checkConfigurationParameterParamType(Object joinConfigurationParameter) {
		return (
				joinConfigurationParameter!=null 
				&& joinConfigurationParameter instanceof HashMap<?, ?> hm 
				&& hm.keySet().stream().allMatch(Table.class::isInstance) 
				&& hm.values().stream().allMatch(Column.class::isInstance));
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.AbstractDataTransformation#getTransformationName()
	 */
	@Override
	public String getTransformationName() {
		return DataTransformationServiceJoinImpl.TRANSFORMATION_NAME;
	}
}
