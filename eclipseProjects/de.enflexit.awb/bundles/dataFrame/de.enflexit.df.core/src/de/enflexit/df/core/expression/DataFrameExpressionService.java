package de.enflexit.df.core.expression;

import de.enflexit.expression.ExpressionContext;
import de.enflexit.expression.ExpressionEditorTreeNode;
import de.enflexit.expression.ExpressionService;
import de.enflexit.expression.ExpressionServiceEvaluator;
import de.enflexit.expression.ExpressionType;

/**
 * The Class DataFrameExpressionService.
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class DataFrameExpressionService implements ExpressionService {

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionService#getExpressionType()
	 */
	@Override
	public ExpressionType getExpressionType() {
		return DataFrameExpressionType.getInstance();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionService#getExpressionServiceEvaluator()
	 */
	@Override
	public ExpressionServiceEvaluator getExpressionServiceEvaluator() {
		return new DataFrameExpressionEvaluator();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionService#getExpressionEditorNode(de.enflexit.expression.ExpressionContext)
	 */
	@Override
	public ExpressionEditorTreeNode getExpressionEditorNode(ExpressionContext context) {
		return new DataFrameExpressionNodeEvaluator(new ExpressionEditorTreeNode("Data Frame"), context).getDataFrameRootNode();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionService#getInsertString(java.lang.String)
	 */
	@Override
	public String getInsertString(String libraryExpression) {
		
		// --------------------------------------------------------------------
		// --- This is basically a copy of the EOM solution at this point -----
		// --------------------------------------------------------------------
		
		// --- Remove type description --------------------
		int cut = libraryExpression.indexOf('}');
		if (cut>0) {
			libraryExpression = libraryExpression.substring(cut+1).trim();
		}
		// --- Return expression to insert ----------------
		return ExpressionService.EXPRESSION_OPENING_DELIMITER + DataFrameExpressionConstants.DF_EXPRESSION_PREFIX + libraryExpression + ExpressionService.EXPRESSION_CLOSING_DELIMITER;
	}

}
