package de.enflexit.expression.functions;

import de.enflexit.expression.ExpressionContext;
import de.enflexit.expression.ExpressionEditorTreeNode;
import de.enflexit.expression.ExpressionService;
import de.enflexit.expression.ExpressionServiceEvaluator;
import de.enflexit.expression.ExpressionType;

/**
 * The Class FunctionExpressionService.
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class FunctionExpressionService implements ExpressionService {
	
	private ExpressionEditorTreeNode expressionEditorRootNode;
	private ExpressionFunctions expressionFunctions;
	

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionService#getExpressionType()
	 */
	@Override
	public ExpressionType getExpressionType() {
		return FunctionExpressionType.getInstance();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionService#getExpressionServiceEvaluator()
	 */
	@Override
	public ExpressionServiceEvaluator getExpressionServiceEvaluator() {
		return new FunctionExpressionEvaluator();
	}
	
	/**
	 * Holds and returns all known expression functions.
	 * @return the expression functions
	 */
	public ExpressionFunctions getExpressionFunctions() {
		if (expressionFunctions==null) {
			expressionFunctions = new ExpressionFunctions();
		}
		return expressionFunctions;
	}
	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionService#getExpressionEditorNode(de.enflexit.expression.ExpressionContext)
	 */
	@Override
	public ExpressionEditorTreeNode getExpressionEditorNode(ExpressionContext context) {
		if (expressionEditorRootNode==null) {
			expressionEditorRootNode = new ExpressionEditorTreeNode("Function");
			expressionEditorRootNode.setExpressionTemplates(this.getExpressionFunctions().getFunctionTreeMap());
		}
		return expressionEditorRootNode;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionService#getInsertString(java.lang.String)
	 */
	@Override
	public String getInsertString(String libraryExpression) {
		
		String insertString = libraryExpression;
		switch (libraryExpression) {
		// --- Operator -------------------------
		// TODO

		// --- Everything else ------------------
		default:
			insertString = libraryExpression + "(<>)";
			break;
		}
		return insertString;
	}
	
	
	
}
