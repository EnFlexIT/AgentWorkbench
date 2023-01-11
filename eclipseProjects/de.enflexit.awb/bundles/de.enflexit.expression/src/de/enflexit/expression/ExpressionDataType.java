package de.enflexit.expression;

/**
 * The Enumeration ExpressionDataType specifies all data types that 
 * can be handled within the {@link ExpressionService}.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public enum ExpressionDataType {
	
	Boolean,
	Integer,
	Double,
	
	BooleanArray, 	// boolean[] 
	IntegerArray,	// int[]
	DoubleArray,	// double[]
	
	BooleanMatrix, 	// boolean[][] 
	IntegerMatrix,	// int[][]
	DoubleMatrix	// double[][]
	
}



