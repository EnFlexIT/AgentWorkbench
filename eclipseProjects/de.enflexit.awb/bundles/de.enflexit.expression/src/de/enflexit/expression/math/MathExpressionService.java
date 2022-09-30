package de.enflexit.expression.math;

import java.util.ArrayList;
import java.util.TreeMap;

import de.enflexit.expression.Expression;
import de.enflexit.expression.ExpressionEditorTreeNode;
import de.enflexit.expression.ExpressionService;
import de.enflexit.expression.ExpressionType;
import de.enflexit.expression.UnknownExpressionException;

/**
 * The Class MathExpressionService.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class MathExpressionService implements ExpressionService {
	
	private ExpressionTypeMath expressionType;
	private ExpressionEditorTreeNode expressionEditorRootNode;

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionService#getExpressionType()
	 */
	@Override
	public ExpressionType getExpressionType() {
		if (expressionType==null) {
			expressionType = ExpressionTypeMath.getInstance();
		}
		return expressionType;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionService#evaluate(de.enflexit.expression.Expression)
	 */
	@Override
	public double evaluate(Expression expression) throws UnknownExpressionException {
		return 42.0;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionService#isValid(de.enflexit.expression.Expression)
	 */
	@Override
	public boolean isValid(Expression expression) {
		// TODO implement validation
		return true;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionService#getErrorMessage()
	 */
	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionService#getExpressionEditorRootNode()
	 */
	@Override
	public ExpressionEditorTreeNode getExpressionEditorRootNode() {
		if (expressionEditorRootNode==null) {
			expressionEditorRootNode = new ExpressionEditorTreeNode();
			expressionEditorRootNode.setUserObject("Mathematical");
			expressionEditorRootNode.setExpressionTemplates(this.buildTemplatesTreeMap());
		}
		return expressionEditorRootNode;
	}
	
	/**
	 * Builds the templates tree map.
	 * @return the tree map
	 */
	private TreeMap<String, ArrayList<String>> buildTemplatesTreeMap(){
		TreeMap<String, ArrayList<String>> expressionTemplates = new TreeMap<>();
		
		ArrayList<String> functionTemplates = new ArrayList<>();
		functionTemplates.add("sin");
		functionTemplates.add("cos");
		functionTemplates.add("tan");
		functionTemplates.add("log");
		expressionTemplates.put("Functions", functionTemplates);
		
		ArrayList<String> constantTemplates = new ArrayList<>();
		constantTemplates.add("Pi");
		expressionTemplates.put("Constants", constantTemplates);
		
		return expressionTemplates;
	}

}
