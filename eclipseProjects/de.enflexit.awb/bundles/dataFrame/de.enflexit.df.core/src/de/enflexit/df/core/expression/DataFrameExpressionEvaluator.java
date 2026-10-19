package de.enflexit.df.core.expression;

import java.util.List;

import de.enflexit.df.core.model.DataController;
import de.enflexit.expression.Expression;
import de.enflexit.expression.ExpressionContext;
import de.enflexit.expression.ExpressionResult;
import de.enflexit.expression.ExpressionServiceEvaluator;
import de.enflexit.expression.UnknownExpressionException;
import tech.tablesaw.api.Table;

/**
 * The Class DataFrameExpressionEvaluator.
 *
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataFrameExpressionEvaluator implements ExpressionServiceEvaluator {

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionServiceEvaluator#evaluate(de.enflexit.expression.Expression, de.enflexit.expression.ExpressionContext)
	 */
	@Override
	public ExpressionResult evaluate(Expression expression, ExpressionContext context) throws UnknownExpressionException {
		
		if (context==null) return null;
		
		Table dataTable = null;
		List<DataController> dataControllerList = context.getContextObject(DataController.class);
		// --- Search to get the result table -----------------------
		for (DataController dc : dataControllerList) {

			String dfExpression = expression.getExpressionString();
			dataTable = dc.getData(dfExpression);
			if (dataTable!=null) {
				break;
			}
		}
		if (dataTable==null) return null;
		
		// --- Translate Table into ExpressionResult ----------------
		ExpressionResult eResult = new ExpressionResult();
		
		
		
		return eResult;
	}

}
