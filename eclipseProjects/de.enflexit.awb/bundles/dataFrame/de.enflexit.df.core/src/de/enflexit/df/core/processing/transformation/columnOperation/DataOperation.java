package de.enflexit.df.core.processing.transformation.columnOperation;

import tech.tablesaw.columns.Column;

/**
 * General interface for the implementation of operations on table saw columns.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public interface DataOperation {
	
	/**
	 * Gets the operation ID.
	 * @return the operation ID
	 */
	public String getOperationID();
	
	/**
	 * Gets the display name.
	 * @return the display name
	 */
	public String getDisplayName();

    /**
     * Checks if this operation supports the type of the provided column. 
     * @param column the column
     * @return true, if successful
     */
    public boolean supports(Column<?> column);

    /**
     * Parses the provided parameter accorcing to the type of the provided column.
     * @param input the input
     * @param column the column
     * @return the object
     */
    public Object parseParameter(String input);

    /**
     * Applies the operation on the provided column.
     * @param column the column
     * @param parameter the parameter
     * @return the result column
     */
    public Column<?> apply(Column<?> column, Object parameter);
}
