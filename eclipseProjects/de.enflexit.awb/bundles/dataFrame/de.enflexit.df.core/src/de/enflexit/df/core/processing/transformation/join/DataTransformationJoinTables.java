package de.enflexit.df.core.processing.transformation.join;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.enflexit.common.properties.Properties;
import de.enflexit.df.core.processing.transformation.AbstractDataTransformation;
import de.enflexit.df.core.processing.transformation.graph.DataTableNode;
import de.enflexit.df.core.processing.transformation.graph.TransformationGraphController;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;
import tech.tablesaw.joining.JoinType;

/**
 * This {@link AbstractDataTransformation} merges two or more input tables to a single table.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataTransformationJoinTables extends AbstractDataTransformation {
	
	public static final String PARAM_NAME_JOIN_TYPE = "joinType";
	public static final String PARAM_NAME_INTERPOLATION_MODE = "interpolationMode";
	public static final String PARAM_NAME_JOIN_COLUMNS = "joinColumns";
	
	private boolean useSpecialTemporalJoin = false;
	
	public enum InterpolationMode {
		NONE, LINEAR, STEP
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.AbstractDataTransformation#performTransformation()
	 */
	@Override
	public Table performTransformation(TransformationGraphController graphController) {
		
		Table resultTable = null;
		String joinColumnsAsString = this.getTransformationParameters().getStringValue(DataTransformationJoinTables.PARAM_NAME_JOIN_COLUMNS);
		JoinType joinType = this.getJoinTypeFromParameterss(this.getTransformationParameters());
		if (joinType==null) {
			joinType = JoinType.INNER;
		}
			
		// --- Convert to Map<Table, Column<?>> -----------------
		HashMap<Table, Column<?>> tablesMap = this.getJoinConfigurationFromParameterString(joinColumnsAsString, graphController);
		resultTable = this.joinTables(tablesMap, joinType);
		
		return resultTable;
	}
	
	private JoinType getJoinTypeFromParameterss(Properties parameterProperties) {
		String joinTypeString = parameterProperties.getStringValue(PARAM_NAME_JOIN_TYPE);
		return joinTypeString!=null ? JoinType.valueOf(joinTypeString) : null;
	}

	/**
	 * Gets the join configuration from parameter string.
	 * @param parameterString the parameter string
	 * @param graphController the graph controller
	 * @return the join configuration from parameter string
	 */
	private HashMap<Table, Column<?>> getJoinConfigurationFromParameterString(String parameterString, TransformationGraphController graphController){
		
		HashMap<Table, Column<?>> joinConfig = new HashMap<Table, Column<?>>();
		
		String[] joinColumnsArray = parameterString.split(",");
		for (String joinColumnDefinition : joinColumnsArray) {
			int startIndex = joinColumnDefinition.startsWith("[") ? 1 : 0;
			int endIndex = joinColumnDefinition.endsWith("]") ? joinColumnDefinition.length()-1 : joinColumnDefinition.length();
			String[] tableAndColumn = joinColumnDefinition.substring(startIndex, endIndex).split(":");
			
			//TODO convert UUID to expression
			Table table = graphController.getDataTableByUUID(tableAndColumn[0]);
			
			if (table!=null) {
				Column<?> column = table.column(tableAndColumn[1]);
				if (column!=null) {
					joinConfig.put(table, column);
				}
			}
			
		}
		return joinConfig;
	}
	
	
	/**
	 * Joins the provided tables. Expects a HashMap with the tables as keys and the corresponding join columns as values. 
	 * @param joinTables the join tables
	 * @return the joined table
	 */
	private Table joinTables(Map<Table, Column<?>> joinTables, JoinType joinType) {
		
		if (this.useSpecialTemporalJoin) {
			// --- The initial, time series specific with integrated time step unification
			//TODO likely to be removed 
			return TimeSeriesJoinHelper.joinTables(joinTables, InterpolationMode.LINEAR, ZoneId.systemDefault());
		} else {
			// --- The more general, configurable 
			// --- Join on non-temporal column --------------------------------
			//TODO Add check for matching join columns (type, values)
			Iterator<Map.Entry<Table, Column<?>>> tablesIterator = joinTables.entrySet().iterator();
			
			if (tablesIterator.hasNext()==false) {
				System.err.println("[" + this.getClass().getSimpleName() + "] Empty join tables map, at least one entry is required!");
				return null;
			}
			
			// --- Start with the first table --------------------------------- 
			Map.Entry<Table, Column<?>> first = tablesIterator.next();
			Table resultTable = first.getKey();
			String resultJoinColumn = first.getValue().name();
			
			// --- Iteratively join the other tables to the first one ---------
			while(tablesIterator.hasNext()) {
				Map.Entry<Table, Column<?>> next = tablesIterator.next();
				Table nextTable = next.getKey();
				String nextJoinColumn = next.getValue().name();
				
				resultTable = resultTable.joinOn(resultJoinColumn).rightJoinColumns(nextJoinColumn).type(joinType).with(nextTable).allowDuplicateColumnNames(true).join();
				
				if (joinType==JoinType.RIGHT_OUTER) {
					// --- For right outer joins, the join column from the right table is kept
					resultJoinColumn = nextJoinColumn;
					// --- Move the join column to the first position
					resultTable = this.putColumnToFront(resultTable, resultJoinColumn);
				}
			}
			return resultTable.sortAscendingOn(resultJoinColumn);
		}
	}
	
	/**
	 * Puts the specified column to the first position.
	 * @param table the table
	 * @param columnName the column name
	 * @return the table
	 */
	private Table putColumnToFront(Table table, String columnName) {
		ArrayList<String> columns = new ArrayList<>(table.columnNames());
		columns.remove(columnName);
		columns.add(0, columnName);
		return table.reorderColumns(columns.toArray(new String[0]));
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
				&& hm.keySet().stream().allMatch(DataTableNode.class::isInstance) 
				&& hm.values().stream().allMatch(Column.class::isInstance));
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.AbstractDataTransformation#getTransformationName()
	 */
	@Override
	public String getTransformationName() {
		return DataTransformationServiceJoinTables.TRANSFORMATION_NAME;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.AbstractDataTransformation#verifyParameters()
	 */
	@Override
	public boolean verifyParameters() {
		// TODO check for at least two tables and matching join column data types
		return true;
	}
}
