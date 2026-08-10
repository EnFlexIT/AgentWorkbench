package de.enflexit.df.impl.db;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * The Class TableDictionary.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class TableDictionary extends HashMap<String, List<TableColumn>> {

	private static final long serialVersionUID = 1L;

	private boolean isDebug = false;
	
	private String catalog;
	private DatabaseConnection databaseConnection;

	private HashMap<String, HashSet<String>> columnsToTableHashMap;
	private HashMap<String, HashMap<String, String>> sqlToColumnTableHashMap;
	
	
	/**
	 * Instantiates a new table dictionary.
	 *
	 * @param databaseConnection the {@link DatabaseConnection} instance to use
	 * @param catalog the catalog to evaluate
	 */
	public TableDictionary(DatabaseConnection databaseConnection, String catalog) {
		this.databaseConnection = databaseConnection;
		this.catalog = catalog;
		this.evaluateTables();
	}

	/**
	 * Evaluate tables.
	 */
	public void evaluateTables() {
		
		this.clear();
		this.getColumnsToTableHashMap().clear();
		
		try {
			
			DatabaseMetaData metaData = this.databaseConnection.getConnection().getMetaData();
			
			ResultSet tables = metaData.getTables(this.catalog, null, null, new String[]{"TABLE", "VIEW"});
			while (tables.next()) {
				// --- Check each table -----------------------------
				String catalog = tables.getString("TABLE_CAT");
				String schema = tables.getString("TABLE_SCHEM");
				String tableName = tables.getString("TABLE_NAME");
				String type = tables.getString("TABLE_TYPE");
				if (this.isDebug==true) {
					System.out.printf("%s - %s: %s.%s%n", catalog, type, schema, tableName);
				}
				
				List<TableColumn> colList = this.getTableColumns(metaData, catalog, schema, tableName);
				if (colList!=null) {
					this.put(tableName, colList);
				}
			}
			
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
	}
	/**
	 * Returns the table columns.
	 *
	 * @param metaData the meta data
	 * @param catalog the catalog
	 * @param schema the schema
	 * @param tableName the table name
	 * @return the table columns
	 */
	private List<TableColumn> getTableColumns(DatabaseMetaData metaData, String catalog, String schema, String tableName) {
		
		List<TableColumn> tbColumnList = new ArrayList<>();
		try  {
			ResultSet columns = metaData.getColumns(catalog, schema, tableName, "%");
			while (columns.next()) {
				// --- Remind each column ---------------------------
				String columnName = columns.getString("COLUMN_NAME");
				String typeName = columns.getString("TYPE_NAME");
				int size = columns.getInt("COLUMN_SIZE");
				boolean nullable = "YES".equals(columns.getString("IS_NULLABLE"));
				if (this.isDebug==true) {
					System.out.printf("    %s, %s (%d), nullable=%s%n", columnName, typeName, size, nullable);
				}

				tbColumnList.add(new TableColumn(columnName, typeName, size, nullable));
				this.putColumnToTable(columnName, tableName);
			}
	
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
		
		if (tbColumnList.size()==0) {
			tbColumnList = null;
		}
		return tbColumnList;
	}
	
	
	/**
	 * Returns the columns to table hash map.
	 * @return the columns to table hash map
	 */
	public HashMap<String, HashSet<String>> getColumnsToTableHashMap() {
		if (columnsToTableHashMap==null) {
			columnsToTableHashMap = new HashMap<>();
		}
		return columnsToTableHashMap;
	}
	/**
	 * Puts and reminds the relation column name to table name .
	 *
	 * @param columnName the column name
	 * @param tableName the table name
	 */
	private void putColumnToTable(String columnName, String tableName) {
		
		HashSet<String> tableList = this.getColumnsToTableHashMap().get(columnName);
		if (tableList==null) {
			tableList = new HashSet<>();
			this.getColumnsToTableHashMap().put(columnName, tableList);
		}
		if (tableList.contains(tableName)==false) {
			tableList.add(tableName);
		}
	}
	
	/**
	 * Returns the list of tables and views.
	 * @return the list of tables and views
	 */
	public List<String> getTablesAndViews() {
		List<String> tableList = new ArrayList<>(this.keySet());
		Collections.sort(tableList);
		return tableList;
	}
	/**
	 * Returns the or creates the list of {@link TableColumn}s that belong to the specified table.
	 *
	 * @param tableName the table name
	 * @return the or create table column list
	 */
	public List<TableColumn> getOrCreateTableColumnList(String tableName) {
		
		List<TableColumn> tbColumns = this.get(tableName);
		if (tbColumns==null) {
			tbColumns = new ArrayList<>();
			this.put(tableName, tbColumns);
		}
		return tbColumns;
	}
	
	/**
	 * Will guess the table name of the specified column name.
	 *
	 * @param sqlStatement the sql statement
	 * @param columnName the column name
	 * @return the list of possible table names or <code>null</code>if nothing was found
	 */
	public List<String> guessTable(String sqlStatement, String columnName) {

		HashSet<String> tableNameHashSet = this.getColumnsToTableHashMap().get(columnName);
		if (tableNameHashSet==null || tableNameHashSet.size()==0) return null;
		
		List<String> tableNameList = new ArrayList<>(tableNameHashSet);
		Collections.sort(tableNameList);
		
		if (tableNameList.size()>1) {
			// --- Try to use the statement ResultSet analysis ------
			HashMap<String, String> columnTableHashMap = this.getSqlToColumnTableHashMap().get(sqlStatement);
			if (columnTableHashMap!=null) {
				String tableName = columnTableHashMap.get(columnName);
				if (tableNameList.contains(tableName)==true) {
					tableNameList.remove(tableName);
					tableNameList.addFirst(tableName);
					tableNameList.add(1, null);
				}
			}
		}
		return tableNameList;
	}

	// --------------------------------------------------------------
	// --- From here, ResultSet check for an SQL statement ---------- 
	// --------------------------------------------------------------
	/**
	 * Returns the SQL to column table.
	 * @return the SQL to column table
	 */
	private HashMap<String, HashMap<String, String>> getSqlToColumnTableHashMap() {
		if (sqlToColumnTableHashMap==null) {
			sqlToColumnTableHashMap = new HashMap<>();
		}
		return sqlToColumnTableHashMap;
	}
	
	/**
	 * Updates the dictionary.
	 *
	 * @param sqlStatement the base SQL statement
	 * @param resSet the ResultSet
	 */
	public void updateDictionary(String sqlStatement, ResultSet resSet) {

		if (sqlStatement==null || resSet==null) return;
		
		HashMap<String, String> sqlColumnTable = this.getSqlToColumnTableHashMap().get(sqlStatement);
		if (sqlColumnTable!=null) return;
		
		try {
			// --- Define relational HashMap ------------------------
			HashMap<String, String> columnTableHashMap = new HashMap<>();
			// --- Evaluate meta data -------------------------------
			ResultSetMetaData meta = resSet.getMetaData();
			for (int i = 1; i <= meta.getColumnCount(); i++) {
				columnTableHashMap.put(meta.getColumnName(i), meta.getTableName(i));
			}
			// --- Remind for later usage ---------------------------
			if (columnTableHashMap.size()>0) {
				this.getSqlToColumnTableHashMap().put(sqlStatement, columnTableHashMap);
			}
			
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
	}
	
}
