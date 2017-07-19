/**
 * ***************************************************************
 * Agent.GUI is a framework to develop Multi-agent based simulation 
 * applications based on the JADE - Framework in compliance with the 
 * FIPA specifications. 
 * Copyright (C) 2010 Christian Derksen and DAWIS
 * http://www.dawis.wiwi.uni-due.de
 * http://sourceforge.net/projects/agentgui/
 * http://www.agentgui.org 
 *
 * GNU Lesser General Public License
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation,
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 * **************************************************************
 */
package agentgui.math.calculation;

import java.util.HashMap;
import java.util.Iterator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.nfunk.jep.JEP;

import agentgui.math.calculation.CalcParameter.ExpressionType;

/**
 * Class for calculations using the JEP Parser.
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CalcFormula extends CalcExpression {
	
	private static final long serialVersionUID = -669801540602778489L;

	
	/** The formula to calculate. */
	private String formula;
	
	/**
	 * List of parameters (key = parameter name,  value = parameter value). 
	 */
	private HashMap<String, CalcParameter> parameters = new HashMap<String, CalcParameter>();
	

	/**
	 * Gets the formula.
	 * @return the formula
	 */
	public String getFormula() {
		return formula;
	}
	/**
	 * This method sets the formula.
	 * @param formula the formula to set
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}
	
	/**
	 * This method adds a parameter to the formula's parameter list.
	 * @param name The parameter name
	 * @param expression The parameter value
	 */
	public void addParameter(String name, CalcExpression expression){
		CalcParameter parameter = null;
		if (expression instanceof CalcConstant) {
			parameter = new CalcParameter(ExpressionType.CONSTANT_VALUE, expression);
		} else if (expression instanceof CalcFormula) {
			parameter = new CalcParameter(ExpressionType.FORMULA, expression);
		}
		this.parameters.put(name, parameter);
	}
	/**
	 * Gets the parameters.
	 * @return the parameters
	 */
	public HashMap<String, CalcParameter> getParameters() {
		return this.parameters;
	}
	/**
	 * Sets the parameters.
	 * @param parameters the parameters to set
	 */
	public void setParameters(HashMap<String, CalcParameter> parameters) {
		this.parameters = parameters;
	}

	
	/* (non-Javadoc)
	 * @see agentgui.math.calculation.CalcExpression#getValue()
	 */
	@Override
	public double getValue() throws CalcExeption {
		// If no formula is set, throw an exception.
		if(formula == null){
			throw new CalcFormulaNotSetException();
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
		parser.getSymbolTable().toString();
		
		// Get the result
		double result = parser.getValue();
		if(Double.isNaN(result)){
			throw new CalcParameterNotSetException();
		}
		return result;
	}
	
}
