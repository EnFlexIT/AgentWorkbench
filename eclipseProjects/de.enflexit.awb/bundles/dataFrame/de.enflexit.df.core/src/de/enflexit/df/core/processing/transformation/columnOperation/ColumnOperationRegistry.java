package de.enflexit.df.core.processing.transformation.columnOperation;

import java.util.ArrayList;
import java.util.List;

import tech.tablesaw.columns.Column;

/**
 * This class manages the available column operations.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class ColumnOperationRegistry {

	private final List<DataOperation> operations = new ArrayList<>();

	/**
	 * Instantiates a new column operation registry.
	 */
	public ColumnOperationRegistry() {
		
		// --- Register supported operations --------------
		this.register(new NumericalDataOperation(NumericalDataOperation.Operation.ADD));
		this.register(new NumericalDataOperation(NumericalDataOperation.Operation.SUBTRACT));
		this.register(new NumericalDataOperation(NumericalDataOperation.Operation.MULTIPLY));
		this.register(new NumericalDataOperation(NumericalDataOperation.Operation.DIVIDE));
	}

	/**
	 * Register an operation.
	 * @param operation the operation
	 */
	public void register(DataOperation operation) {
		operations.add(operation);
	}

	/**
	 * Gets the supported operations for the provided column.
	 * @param column the column
	 * @return the operations for column
	 */
	public List<DataOperation> getOperationsForColumn(Column<?> column) {
		return operations.stream().filter(op -> op.supports(column)).toList();
	}
	
	/**
	 * Gets the operation by ID.
	 * @param operationID the operation ID
	 * @return the operation by ID
	 */
	public DataOperation getOperationByID(String operationID) {
		return this.operations.stream().filter(op -> op.getOperationID().equals(operationID)).findFirst().orElse(null);
	}
}