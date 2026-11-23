package de.enflexit.df.core;

import java.util.List;

import de.enflexit.df.impl.db.TableColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;
import tech.tablesaw.columns.temporal.TemporalColumn;

/**
 * This class provides static helper methods for working with and analyzing table data. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TableHelper {
	
	// --------------------------------------------------------------
	// --- Methods for finding temporal columns in a table ----------
	
	/**
	 * Gets the first temporal column from a list of {@link TableColumn} instances.
	 * @param columnsList the columns list
	 * @return the temporal column, null if none found
	 */
	public static TableColumn getTemporalColumn(List<TableColumn> columnsList) {
		for (TableColumn tc : columnsList) {
			if (tc.getDataType().equals("timestamp") || tc.getDataType().equals("timestamptz")) {
				return tc;
			}
		}
		return null;
	}
	
	/**
	 * Gets the name of the first temporal column from a list of {@link TableColumn} instances.
	 * @param columnsList the columns list
	 * @return the name of the temporal column, null if none found
	 */
	public static String getTemporalColumnName(List<TableColumn> columnsList) {
		TableColumn temporalColumn = getTemporalColumn(columnsList);
		return temporalColumn!=null ? temporalColumn.getColumnName() : null;
	}
	
	/**
	 * Gets the first temporal column from a TableSaw table.
	 * @param table the table
	 * @return the temporal column, null if none found
	 */
	public static Column<?> getTemporalColumn(Table table){
		for (Column<?> column : table.columns()) {
			if (column instanceof TemporalColumn<?>) {
				return column;
			}
		}
		return null;
	}
	
	/**
	 * Gets the name of the first temporal column from a TableSaw table.
	 * @param table the table
	 * @return the name of the temporal column, null if none found.
	 */
	public static String getTemporalColumnName(Table table) {
		Column<?> temporalColumn = getTemporalColumn(table);
		return temporalColumn!=null ? temporalColumn.name() : null;
	}
}
