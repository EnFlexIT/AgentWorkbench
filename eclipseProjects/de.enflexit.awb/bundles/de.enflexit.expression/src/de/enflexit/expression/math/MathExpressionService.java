package de.enflexit.expression.math;

import java.util.ArrayList;
import java.util.TreeMap;

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
	 * @see de.enflexit.expression.ExpressionService#getExpressionEditorRootNode()
	 */
	@Override
	public ExpressionEditorTreeNode getExpressionEditorRootNode() {
		if (expressionEditorRootNode==null) {
			expressionEditorRootNode = new ExpressionEditorTreeNode("Mathematical");
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
