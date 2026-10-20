package de.enflexit.df.core.expression;

import java.util.ArrayList;
import java.util.List;

import de.enflexit.common.NumberHelper;
import de.enflexit.df.core.model.DataController;
import tech.tablesaw.api.Table;

/**
 * The Class DataFrameExpressionParser.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataFrameExpressionParser {

	private DataController dataController;
	private String resourceLink;
	
	private Table table;
	
	/**
	 * Instantiates a new data frame resource parser.
	 * @param dataController the data controller
	 */
	public DataFrameExpressionParser(DataController dataController) { 
		this(dataController, null);
	}
	/**
	 * Instantiates a new data frame resource parser.
	 * @param dataController the data controller
	 * @param resourceLink the resource link
	 */
	public DataFrameExpressionParser(DataController dataController, String resourceLink) {
		this.dataController = dataController;
		this.setResourceLink(resourceLink);
	}
	
	/**
	 * Sets the resource link.
	 * @param resourceLink the new resource link
	 */
	public void setResourceLink(String resourceLink) {
		this.resourceLink = resourceLink;
	}
	/**
	 * Returns the resource link.
	 * @return the resource link
	 */
	public String getResourceLink() {
		return resourceLink;
	}
	
	/**
	 * Returns the data.
	 * @return the data
	 */
	public Table getData() {
		if (table==null) {
			this.parse();
		}
		return table;
	}
	
	/**
	 * Parses the.
	 */
	private void parse() {
		
		if (resourceLink==null || resourceLink.trim().isBlank()==true) return;
		
		// --- Remove square bracket --------------------------------
		String workExp = this.removeSquareBrackets(resourceLink.toLowerCase().trim());
		// --- Starts with 'DF!' ------------------------------------
		workExp = this.removeDataFrameExpressionPrefix(workExp);
		
		// --- Proceed remaining String -----------------------------
		String tableExpression = this.extractTableExpression(workExp); 
		String columnExpression = this.extractColumnExpression(workExp);

		// --- Request table from tree ------------------------------
		Table workTable = this.dataController.getDataTreeModel().getDataTableByExpression(tableExpression);

		// --- Prepare return statements ----------------------------
		if (columnExpression==null) {
			this.table = workTable;
			return;
		} 
		
		// --- Return selected columns only -------------------------
		List<String>  columnNameList  = this.extractColumnNames(columnExpression);
		List<Integer> columnIndexList = this.getColumnIndexList(columnNameList);
		if (columnIndexList!=null) {
			this.table = selectTableColumnsByIndex(workTable, columnIndexList);
		} else {
			this.table = selectTableColumnsByName(workTable, columnNameList);
		}
		
	}
	
	
	/**
	 * Based on the column name list, converts and returns a column index list, if all elements are numeric data types.
	 *
	 * @param columnNameList the column name list
	 * @return the column index list
	 */
	private List<Integer> getColumnIndexList(List<String> columnNameList) {
		
		List<Integer> indexList = new ArrayList<>();
		for (String columnName : columnNameList) {
			Integer columnIndex = NumberHelper.parseInteger(columnName);
			if (columnIndex==null) return null;
			indexList.add(columnIndex);
		}
		return indexList;
	}
	
	/**
	 * Select table columns by index.
	 *
	 * @param table the table
	 * @param colNameList the col name list
	 * @return the table
	 */
	private Table selectTableColumnsByIndex(Table table, List<Integer> colIndexList) {
		int[] indices = colIndexList.stream().mapToInt(Integer::intValue).toArray();;
		return table.selectColumns(indices);
		
	}
	/**
	 * Selects columns from the specified table by the specified names.
	 *
	 * @param table the table
	 * @param colNameList the column name list
	 * @return the table
	 */
	private Table selectTableColumnsByName(Table table, List<String> colNameList) {
	    
		String[] resolved = colNameList.stream()
	        .map(name -> table.columnNames().stream()
	            .filter(col -> col.equalsIgnoreCase(name))
	            .findFirst()
	            .orElseThrow(() -> new IllegalArgumentException("Column '" + name + "' not found. Available Columns are: " + table.columnNames()
	            )))
	        .toArray(String[]::new);

	    return table.selectColumns(resolved);
	}
	
	
	/**
	 * Extract column names.
	 *
	 * @param columnExp the column expression
	 * @return the list of column names
	 */
	private List<String> extractColumnNames(String columnExp) {

		String workExp = columnExp.replace(DataFrameExpressionConstants.DF_EXPRESSION_COLUMN_PREFIX.toLowerCase(), "");
		workExp = this.removeRoundBrackets(workExp);
		
		List<String> columnNameList = new ArrayList<>();
		String[] columnNameArray = workExp.split(",");
		for (String columName : columnNameArray) {
			columnNameList.add(this.removeSingleQuotationMark(columName.trim()));
		}
		return columnNameList;
	}
	
	/**
	 * Extract column expression.
	 *
	 * @param exp the expression to work on
	 * @return the string
	 */
	private String extractColumnExpression(String exp) {
		
		int idxColStart = exp.indexOf("." + DataFrameExpressionConstants.DF_EXPRESSION_COLUMN_PREFIX.toLowerCase());
		if (idxColStart!=-1) {
			return exp.substring(idxColStart+1);
		}
		return null;
	}
	/**
	 * Extract the table expression from the specified String.
	 *
	 * @param exp the expression to work on
	 * @return the string
	 */
	private String extractTableExpression(String exp) {
		
		int idxColStart = exp.indexOf("." + DataFrameExpressionConstants.DF_EXPRESSION_COLUMN_PREFIX.toLowerCase());
		if (idxColStart!=-1) {
			return exp.substring(0, idxColStart);
		}
		return exp;
	}
	/**
	 * If available, removes the data frame expression prefix.
	 *
	 * @param exp the expression to work on
	 * @return the string
	 */
	private String removeDataFrameExpressionPrefix(String exp) {
		return exp.startsWith(DataFrameExpressionConstants.DF_EXPRESSION_PREFIX.toLowerCase()) ? exp.substring(DataFrameExpressionConstants.DF_EXPRESSION_PREFIX.length()) : exp;
	}
	
	/**
	 * Removes the square brackets.
	 *
	 * @param exp the expression to work on
	 * @return the string
	 */
	private String removeSquareBrackets(String exp) {
		return exp.startsWith("[") && exp.endsWith("]") ? exp.substring(1, exp.length() - 1) : exp;
	}
	/**
	 * Removes the round brackets.
	 *
	 * @param exp the expression to work on
	 * @return the string
	 */
	private String removeRoundBrackets(String exp) {
		return exp.startsWith("(") && exp.endsWith(")") ? exp.substring(1, exp.length() - 1) : exp;
	}
	/**
	 * Removes the single quotation mark from the specified string.
	 *
	 * @param exp the expression to work on
	 * @return the string
	 */
	private String removeSingleQuotationMark(String exp) {
		return exp.startsWith("'") && exp.endsWith("'") ? exp.substring(1, exp.length() - 1) : exp;
	}
	
}
