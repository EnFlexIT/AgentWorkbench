package de.enflexit.df.core.processing.transformation.columnOperation;

import tech.tablesaw.api.NumericColumn;
import tech.tablesaw.columns.Column;

/**
 * {@link DataOperation} for simple numeric operations.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class NumericalDataOperation implements DataOperation {
	
	public enum Operation {
		ADD("numeric.add"),
		SUBTRACT("numeric.subtract"),
		MULTIPLY("numeric.multiply"),
		DIVIDE("numeric.divide");
		
		public final String operationID;
		
		private Operation(String operationID) {
			this.operationID = operationID;
		}
	}
	
	public enum ResultType {
		INTEGER,
	    LONG,
	    FLOAT,
	    DOUBLE;
	}

	private final Operation operation;

	public NumericalDataOperation(Operation type) {
		this.operation = type;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.columnOperation.DataOperation#getOperationID()
	 */
	@Override
	public String getOperationID() {
		return this.operation.operationID;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.manipulation.DataOperation#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return switch (operation) {
		case ADD -> "Add";
		case SUBTRACT -> "Substract";
		case MULTIPLY -> "Multiply";
		case DIVIDE -> "Divide";
		};
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.manipulation.DataOperation#supports(tech.tablesaw.columns.Column)
	 */
	@Override
	public boolean supports(Column<?> column) {
		return column instanceof NumericColumn<?>;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.manipulation.DataOperation#parseParameter(java.lang.String, tech.tablesaw.columns.Column)
	 */
	@Override
	public Object parseParameter(String paramString) {
		Double paramValue = null;
		
		try {
			paramValue = Double.parseDouble(paramString);
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException("The provided param value " + paramString + " is not numeric!");
		}
		
		return paramValue;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.manipulation.DataOperation#apply(tech.tablesaw.columns.Column, java.lang.Object)
	 */
	@Override
	public Column<?> apply(Column<?> column, Object parameter) {
		
		if (parameter instanceof Number == false) {
			throw new IllegalArgumentException("Invalid parameter type!");
		}
		Number paramNumber = (Number) parameter;
		
		if (column instanceof NumericColumn<?> nc) {
			switch(this.operation) {
			case ADD:
				return nc.add(paramNumber);
			case SUBTRACT:
				return nc.subtract(paramNumber);
			case MULTIPLY:
				return nc.multiply(paramNumber);
			case DIVIDE:
				if (paramNumber.doubleValue()==0.0) {
					throw new IllegalArgumentException("Division by zero!");
				}
				return nc.divide(paramNumber);
			}
		} else {
			throw new IllegalArgumentException("Numerical operations are not applicable for column type " + column.getClass().getSimpleName());
		}
		
		return null;
	}

}
