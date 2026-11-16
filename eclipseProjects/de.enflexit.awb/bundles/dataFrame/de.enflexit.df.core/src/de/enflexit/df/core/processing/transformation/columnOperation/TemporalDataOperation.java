package de.enflexit.df.core.processing.transformation.columnOperation;

import java.time.Duration;

import tech.tablesaw.columns.Column;
import tech.tablesaw.columns.temporal.TemporalColumn;

/**
 * {@link DataOperation} implementation for simple temporal operations. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TemporalDataOperation implements DataOperation {
	
	public enum Type {
        ADD,
        SUBTRACT
    }

    private final Type type;
    private final String operationID;

    /**
     * Instantiates a new temporal data operation.
     * @param type the type
     */
    public TemporalDataOperation(Type type, String operationID) {
        this.type = type;
        this.operationID = operationID;
    }
    
    /* (non-Javadoc)
     * @see de.enflexit.df.core.processing.transformation.columnOperation.DataOperation#getOperationID()
     */
    @Override
	public String getOperationID() {
		return this.operationID;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.manipulation.DataOperation#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return type == Type.ADD ? "Addition" : "Subtraktion";
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
	public Object parseParameter(String parameterString) {
		//TODO add support for period
		//TODO add time formats
		return Duration.parse(parameterString);
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.manipulation.DataOperation#apply(tech.tablesaw.columns.Column, java.lang.Object)
	 */
	@Override
	public Column<?> apply(Column<?> column, Object parameter) {
		// TODO Auto-generated method stub
		return null;

	}

}
