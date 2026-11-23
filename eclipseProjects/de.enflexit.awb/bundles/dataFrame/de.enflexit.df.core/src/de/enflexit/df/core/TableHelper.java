package de.enflexit.df.core;

import java.util.ArrayList;
import java.util.List;

import de.enflexit.df.impl.db.TableColumn;
import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.api.TimeColumn;
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
	 * Checks if the provided {@link TableColumn} is a temporal column.
	 * @param tableColumn the table column
	 * @return true, if is temporal column
	 */
	public static boolean isTemporalColumn(TableColumn tableColumn) {
		return (tableColumn.getDataType().equals("timestamp") || tableColumn.getDataType().equals("timestamptz"));
	}
	
	/**
	 * Checks if the provided TableSaw column is temporal column.
	 * @param column the column
	 * @return true, if is temporal column
	 */
	public static boolean isTemporalColumn(Column<?> column) {
		return column instanceof TemporalColumn<?> || column instanceof DateColumn || column instanceof TimeColumn;
	}
	
	/**
	 * Gets the first temporal column from a list of {@link TableColumn} instances.
	 * @param columnsList the columns list
	 * @return the temporal column, null if none found
	 */
	public static TableColumn getTemporalColumn(List<TableColumn> columnsList) {
		for (TableColumn tc : columnsList) {
			if (isTemporalColumn(tc)) {
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
	 * Gets the temporal columns list.
	 * @param columnsList the columns list
	 * @return the temporal columns list
	 */
	public static List<TableColumn> getTemporalColumnsList(List<TableColumn> columnsList){
		ArrayList<TableColumn> temporalColumnsList = new ArrayList<TableColumn>();
		for (TableColumn tc : columnsList) {
			if (isTemporalColumn(tc)) {
				temporalColumnsList.add(tc);
			}
		}
		return temporalColumnsList;
	}
	
	/**
	 * Gets the first temporal column from a TableSaw table.
	 * @param table the table
	 * @return the temporal column, null if none found
	 */
	public static Column<?> getTemporalColumn(Table table){
		for (Column<?> column : table.columns()) {
			if (isTemporalColumn(column)) {
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
	
	public static List<Column<?>> getTemporalColumnsList(Table table){
		ArrayList<Column<?>> temporalColumnsList = new ArrayList<Column<?>>();
		for (Column<?> tc : table.columns()) {
			if (isTemporalColumn(tc)) {
				temporalColumnsList.add(tc);
			}
		}
		return temporalColumnsList;
	}
}
