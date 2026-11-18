package de.enflexit.df.core.processing.transformation.columnOperation;

import java.time.temporal.ChronoUnit;

import tech.tablesaw.api.DateColumn;
import tech.tablesaw.api.DateTimeColumn;
import tech.tablesaw.api.InstantColumn;
import tech.tablesaw.api.TimeColumn;
import tech.tablesaw.columns.Column;
import tech.tablesaw.columns.temporal.TemporalColumn;

/**
 * {@link DataOperation} implementation for simple temporal operations. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TemporalColumnOperation implements DataOperation {
	
	public enum Operation {
        ADD("temporal.add"),
        SUBTRACT("temporal.subtract");
		
		public final String operationID;
		
		private Operation(String operationID) {
			this.operationID = operationID;
		}
    }

    private final Operation operation;

    /**
     * Instantiates a new temporal data operation.
     * @param operation the actual operation
     */
    public TemporalColumnOperation(Operation operation) {
        this.operation = operation;
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
		return operation == Operation.ADD ? "Add" : "Subtract";
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.manipulation.DataOperation#supports(tech.tablesaw.columns.Column)
	 */
	@Override
	public boolean supports(Column<?> column) {
		return column instanceof TemporalColumn<?>;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.manipulation.DataOperation#parseParameter(java.lang.String, tech.tablesaw.columns.Column)
	 */
	@Override
	public TemporalParameter parseParameter(String parameterString) {
		return TemporalParameter.fromString(parameterString);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.manipulation.DataOperation#apply(tech.tablesaw.columns.Column, java.lang.Object)
	 */
	@Override
	public Column<?> apply(Column<?> column, Object parameter) {
		
		if (isSupportedTemporalColumn(column)==false) {
			throw new IllegalArgumentException("Invalid column type: " + column.getClass().getSimpleName());
		}
		
		if (parameter instanceof TemporalParameter==false) {
			throw new IllegalArgumentException("Invalid parameter type: " + parameter.getClass().getSimpleName());
		}
		
		TemporalParameter tempParam = (TemporalParameter) parameter;
		
		Long value = tempParam.getValue();
		if (this.operation==Operation.SUBTRACT) {
			// --- TableSaw's temporal columns have only a plus method -> add the negative value for subtraction 
			value *= -1;
		}
		
		ChronoUnit unit = tempParam.getUnit().getChronoUnit();

		// --- Since the plus method is not defined in the common super type, separate handling is necessary 
		if (column instanceof DateTimeColumn dtc) {
			return dtc.plus(value, unit);
		} else if (column instanceof InstantColumn ic) {
			return ic.plus(value, unit); 
		} else if (column instanceof DateColumn dc) {
			return dc.plus(value.intValue(), unit);
		} else if (column instanceof TimeColumn tc) {
			return tc.plus(value.intValue(), unit);
		}
		
		return null;

	}
	
	/**
	 * Checks if the column is of a supported temporal type.
	 * @param column the column
	 * @return true, if is temporal column
	 */
	public static boolean isSupportedTemporalColumn(Column<?> column) {
		return (column instanceof TemporalColumn<?> || column instanceof DateColumn || column instanceof TimeColumn);
	}

}
