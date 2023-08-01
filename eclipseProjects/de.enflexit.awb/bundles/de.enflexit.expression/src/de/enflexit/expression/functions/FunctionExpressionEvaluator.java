package de.enflexit.expression.functions;

import java.util.ArrayList;
import de.enflexit.expression.Expression;
import de.enflexit.expression.ExpressionContext;
import de.enflexit.expression.ExpressionData;
import de.enflexit.expression.ExpressionResult;
import de.enflexit.expression.ExpressionServiceEvaluator;
import de.enflexit.expression.UnknownExpressionException;

/**
 * The Class FunctionExpressionEvaluator.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class FunctionExpressionEvaluator implements ExpressionServiceEvaluator {

	
	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionServiceEvaluator#evaluate(de.enflexit.expression.Expression, de.enflexit.expression.ExpressionContext)
	 */
	@Override
	public ExpressionResult evaluate(Expression expression, ExpressionContext context) throws UnknownExpressionException {
		
		ExpressionResult expResult = null;
		
		// --- Get the ExpressionFunction to work on ---------------- 
		ExpressionFunction expFckt = ExpressionFunction.getExpressionFunction(expression.getExpressionString());
		if (expFckt==null) {
			String msg = "[" + this.getClass().getSimpleName() + "] The expression '" + expression.getExpressionString() + "' does not start with a known expression function indicator.";
			System.err.println(msg);
			return null;
		}
		
		// --- Evaluate Expression ---------------------------------- 
		switch (expFckt) {
		case SUM:
		case AVG:
		case MAX:
		case MIN:
			// --- Get the required data ----------------------------
			ExpressionData data = this.getSubExpressionData(expression, context, 0);
			if (data!=null) {
				expResult = new ArrayFunctionEvaluator(expFckt, data).getExpressionResult();
			}
			break;
			
		case TimeSeriesDiscretization:
			ArrayList<ExpressionData> parameters = this.getAllSubExpressionData(expression, context);
			if (parameters!=null) {
				expResult = new TimeSeriesFunctionEvaluator(expFckt, parameters).getExpressionResult();
			}
			break;
			
		default:
			System.err.println("[" + this.getClass().getSimpleName() + "] Unimplemented: evaluation for function " + expFckt.name());
			break;
		}
		return expResult;
	}

	/**
	 * Return the sub expression data, if available.
	 *
	 * @param expression the expression
	 * @param context the context
	 * @param subExpIndex the sub expression index
	 * @return the sub expression data
	 */
	private ExpressionData getSubExpressionData(Expression expression, ExpressionContext context, int subExpIndex) {
		
		Expression subExpression = expression.getSubExpressions().get(subExpIndex);
		if (subExpression!=null) {
			ExpressionResult subExpResult = subExpression.getExpressionResult(context);
			if (subExpResult!=null) {
				return subExpResult.getExpressionData();
			}
		}
		return null;
	}
	
	private ArrayList<ExpressionData> getAllSubExpressionData(Expression expression, ExpressionContext context){
		ArrayList<ExpressionData> subExpressionData = new ArrayList<>();
		for (Expression subExpression : expression.getSubExpressions()) {
			ExpressionResult subExpResult = subExpression.getExpressionResult(context);
			if (subExpResult!=null) {
				subExpressionData.add(subExpResult.getExpressionData());
			}
		}
		return subExpressionData;
	}
	
}
