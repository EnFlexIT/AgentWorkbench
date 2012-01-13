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

/**
 * The Class CalcParameter.
 */
public class CalcParameter extends CalcExpression {

	private static final long serialVersionUID = 1444566766356515442L;

	public static final int CONSTANT_VALUE = 0;
	public static final int FORMULA = 1;
	
	
	/** The parameter type. */
	private int parameterType = 0;
	
	/** 
	 * The current CalcExpression, which can be either 
	 * a constant value or a formula, which has to be calculated. 
	 */
	private CalcExpression calcExpression = null;
	
	
	/**
	 * Instantiates a new parameter for calculations.
	 *
	 * @param parameterType the parameter type
	 * @param calcExpression the CalcExpression
	 */
	public CalcParameter(int parameterType, CalcExpression calcExpression) {
		this.setParameterType(parameterType);
		this.setCalcExpression(calcExpression);
	}
	
	/**
	 * Sets the parameter type.
	 * @param parameterType the parameterType to set
	 */
	public void setParameterType(int parameterType) {
		this.parameterType = parameterType;
	}
	/**
	 * Gets the parameter type.
	 * @return the parameterType
	 */
	public int getParameterType() {
		return parameterType;
	}

	/**
	 * Sets the current CalcExpression which can be either 
	 * a constant value or a formula, which has to be calculated. 
	 * @param calcExpression the calcExpression to set
	 */
	public void setCalcExpression(CalcExpression calcExpression) {
		if (calcExpression instanceof CalcConstant) {
			setParameterType(CalcParameter.CONSTANT_VALUE);
		} else if (calcExpression instanceof CalcFormula) {
			setParameterType(CalcParameter.FORMULA);
		}
		this.calcExpression = calcExpression;
	}

	/**
	 * Gets the current CalcExpression.
	 * @return the calcExpression
	 */
	public CalcExpression getCalcExpression() {
		return this.calcExpression;
	}

	/* (non-Javadoc)
	 * @see agentgui.math.calculation.CalcExpression#getValue()
	 */
	@Override
	public double getValue() throws CalcExeption {
		return this.calcExpression.getValue();
	}

	
	
	
}
