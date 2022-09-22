package de.enflexit.expression;

public interface ExpressionEvaluatorService {
	public double evaluate(Expression expressionString) throws UnknownExpressionException;
}
