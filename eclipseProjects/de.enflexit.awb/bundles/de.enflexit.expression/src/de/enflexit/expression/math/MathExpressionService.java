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
	
	private final String OPERATOR_PLUS = "+ (plus)";
	private final String OPERATOR_MINUS = "- (minus)";
	private final String OPERATOR_MULTIPLY = "* (multiply)";
	private final String OPERATOR_DIVED = "* (divide)";
	private final String OPERATOR_POTENTIATE = "^ (potentiate)";
	
	
	private ExpressionEditorTreeNode expressionEditorRootNode;

	/* (non-Javadoc)
	 * @see de.enflexit.expression.ExpressionService#getExpressionType()
	 */
	@Override
	public ExpressionType getExpressionType() {
		return MathExpressionType.getInstance();
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
		// --- Operator -------------------------
		case OPERATOR_PLUS:
			insertString = "+";
			break;
		case OPERATOR_MINUS:
			insertString = "-";
			break;
		case OPERATOR_MULTIPLY:
			insertString = "*";
			break;
		case OPERATOR_DIVED:
			insertString = "/";
			break;
		case OPERATOR_POTENTIATE:
			insertString = "^";
			break;
		
		// --- Constants ------------------------
		case "pi":
		case "e":
			insertString = libraryExpression;			
			break;

		// --- Everything else ------------------
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
		
		ArrayList<String> operatorExpressions = new ArrayList<>();
		operatorExpressions.add(OPERATOR_PLUS);
		operatorExpressions.add(OPERATOR_MINUS);
		operatorExpressions.add(OPERATOR_MULTIPLY);
		operatorExpressions.add(OPERATOR_DIVED);
		operatorExpressions.add(OPERATOR_POTENTIATE);
		expressionTemplates.put("Operators", operatorExpressions);
		
		ArrayList<String> constantExpressions = new ArrayList<>();
		constantExpressions.add("pi");
		constantExpressions.add("e");
		expressionTemplates.put("Constants", constantExpressions);

		ArrayList<String> functionExpressions = new ArrayList<>();
		functionExpressions.add("ln");
		functionExpressions.add("log");
		functionExpressions.add("sqrt");
		functionExpressions.add("cbrt");
		expressionTemplates.put("Numerical ExpressionFunctions", functionExpressions);

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
		allExpressions.addAll(operatorExpressions);
		allExpressions.addAll(constantExpressions);
		allExpressions.addAll(functionExpressions);
		allExpressions.addAll(trigoExpressions);
		
		expressionTemplates.put("All ExpressionFunctions", allExpressions);
		
		return expressionTemplates;
	}

}
