package agentgui.math.calculation;

import java.util.HashMap;
import java.util.Iterator;

import org.nfunk.jep.JEP;

/**
 * Class for calculations using the JEP Parser
 * @author Nils
 *
 */
public class Formula implements CalcExpression {
	/**
	 * The formula to calculate
	 */
	private String formula;
	/**
	 * List of parameters.
	 * key = parameter name
	 * value = parameter value 
	 */
	private HashMap<String, CalcExpression> parameters;
	
	/**
	 * This method adds a parameter to the formula's parameter list.
	 * @param name The parameter name
	 * @param expression The parameter value
	 */
	public void addParameter(String name, CalcExpression expression){
		if(parameters == null){
			parameters = new HashMap<String, CalcExpression>();
		}
		parameters.put(name, expression);
	}
	

	/**
	 * @return the formula
	 */
	public String getFormula() {
		return formula;
	}


	/**
	 * This method sets the formula
	 * @param formula the formula to set
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}


	
	@Override
	public double getValue() throws CalculationException {
		// If no formula is set, throw an exception.
		if(formula == null){
			throw new FormulaNotSetException();
		}
		
		// JEP parser instance
		JEP parser = new JEP();
		parser.addStandardConstants();
		parser.addStandardFunctions();
		// Initiate parameters
		Iterator<String> params = parameters.keySet().iterator();
		while(params.hasNext()){
			String key = params.next();
			parser.addVariable(key, parameters.get(key).getValue());
		}
		
		// Set the expression to parse
		parser.parseExpression(formula);
		
		// Get the result
		double result = parser.getValue();
		if(Double.isNaN(result)){
			throw new ParameterNotSetException();
		}
		return result;
	}
	
}
