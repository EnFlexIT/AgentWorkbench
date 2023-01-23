package de.enflexit.expression.math;

import java.util.ArrayList;
import java.util.TreeMap;

import de.enflexit.expression.ExpressionContext;
import de.enflexit.expression.ExpressionEditorTreeNode;
import de.enflexit.expression.ExpressionService;
import de.enflexit.expression.ExpressionServiceEvaluator;
import de.enflexit.expression.ExpressionType;

/**
 * The Class MathExpressionService.
 * 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public class MathExpressionService implements ExpressionService {
	
	private ExpressionEditorTreeNode expressionEditorRootNode;

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionService#getExpressionType()
	 */
	@Override
	public ExpressionType getExpressionType() {
		return ExpressionTypeMath.getInstance();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionService#getExpressionServiceEvaluator()
	 */
	@Override
	public ExpressionServiceEvaluator getExpressionServiceEvaluator() {
		return new MathExpressionEvaluator();
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionService#getExpressionEditorNode(de.enflexit.expression.ExpressionContext)
	 */
	@Override
	public ExpressionEditorTreeNode getExpressionEditorNode(ExpressionContext context) {
		if (expressionEditorRootNode==null) {
			expressionEditorRootNode = new ExpressionEditorTreeNode("Mathematical");
			expressionEditorRootNode.setExpressionTemplates(this.buildTemplatesTreeMap());
		}
		return expressionEditorRootNode;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionService#getInsertString(java.lang.String)
	 */
	@Override
	public String getInsertString(String libraryExpression) {
		
		String insertString = null;
		switch (libraryExpression) {
		case "pi":
		case "e":
			insertString = libraryExpression;			
			break;

		default:
			insertString = libraryExpression + "(<>)";
			break;
		}
		return insertString;
	}
	
	
	/**
	 * Builds the templates tree map.
	 * @return the tree map
	 */
	private TreeMap<String, ArrayList<String>> buildTemplatesTreeMap(){
		
		TreeMap<String, ArrayList<String>> expressionTemplates = new TreeMap<>();
		
		ArrayList<String> constantExpressions = new ArrayList<>();
		constantExpressions.add("pi");
		constantExpressions.add("e");
		expressionTemplates.put("Constants", constantExpressions);

		ArrayList<String> functionExpressions = new ArrayList<>();
		functionExpressions.add("ln");
		functionExpressions.add("log");
		functionExpressions.add("sqrt");
		functionExpressions.add("cbrt");
		expressionTemplates.put("Numerical Functions", functionExpressions);

		ArrayList<String> trigoExpressions = new ArrayList<>();
		trigoExpressions.add("sin");
		trigoExpressions.add("asin");
		trigoExpressions.add("sinh");
		trigoExpressions.add("cos");
		trigoExpressions.add("acos");
		trigoExpressions.add("cosh");
		trigoExpressions.add("tan");
		trigoExpressions.add("atan");
		trigoExpressions.add("tanh");
		expressionTemplates.put("Trigonometrical", trigoExpressions);
		
		// --- Concluding group that displays all functions / constants at once ---------
		ArrayList<String> allExpressions = new ArrayList<>();
		allExpressions.add("pi");
		allExpressions.add("e");
		
		allExpressions.add("ln");
		allExpressions.add("log");
		allExpressions.add("sqrt");
		allExpressions.add("cbrt");

		allExpressions.add("sin");
		allExpressions.add("asin");
		allExpressions.add("sinh");
		allExpressions.add("cos");
		allExpressions.add("acos");
		allExpressions.add("cosh");
		allExpressions.add("tan");
		allExpressions.add("atan");
		allExpressions.add("tanh");
		expressionTemplates.put("All Functions", allExpressions);
		
		return expressionTemplates;
	}

}
