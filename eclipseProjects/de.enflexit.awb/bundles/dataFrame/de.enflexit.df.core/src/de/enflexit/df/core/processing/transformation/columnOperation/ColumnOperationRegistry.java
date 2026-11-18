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
	
	private static ColumnOperationRegistry instance;

	/**
	 * Instantiates a new column operation registry.
	 */
	private ColumnOperationRegistry() {
		
		// --- Register supported operations --------------
		this.register(new NumericColumnOperation(NumericColumnOperation.Operation.ADD));
		this.register(new NumericColumnOperation(NumericColumnOperation.Operation.SUBTRACT));
		this.register(new NumericColumnOperation(NumericColumnOperation.Operation.MULTIPLY));
		this.register(new NumericColumnOperation(NumericColumnOperation.Operation.DIVIDE));
		this.register(new TemporalColumnOperation(TemporalColumnOperation.Operation.ADD));
		this.register(new TemporalColumnOperation(TemporalColumnOperation.Operation.SUBTRACT));
	}
	
	public static ColumnOperationRegistry getInstance() {
		if (instance==null) {
			instance = new ColumnOperationRegistry();
		}
		return instance;
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