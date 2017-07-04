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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * The Class CalcParameter can bes used in order to set a Parameter for a
 * formula. Possible {@link CalcExpression} Types can be {@link CalcConstant}
 * and {@link CalcFormula}
 * 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CalcParameter extends CalcExpression {

	private static final long serialVersionUID = 1444566766356515442L;

	/**
	 * The Enumeration ExpressionType provides a selection 
	 * of possible parameters that can be set.
	 */
	public enum ExpressionType {
		CONSTANT_VALUE,
		FORMULA
		
	}
	
	/** The parameter type. */
	private ExpressionType currExpressionType;
	
	/** 
	 * The current CalcExpression, which can be either 
	 * a constant value or a formula, which has to be calculated. 
	 */
	private CalcExpression calcExpression;
	
	
	
	/**
	 * Instantiates a new parameter for calculations.
	 */
	public CalcParameter() {
		
	}
	
	/**
	 * Instantiates a new parameter for calculations.
	 *
	 * @param expressionType the parameter type
	 * @param calcExpression the CalcExpression
	 */
	public CalcParameter(ExpressionType expressionType, CalcExpression calcExpression) {
		this.setExpressionType(expressionType);
		this.setCalcExpression(calcExpression);
	}
	
	/**
	 * Sets the current ExpressionType.
	 * @param expressionType the current ExpressionType to set
	 */
	public void setExpressionType(ExpressionType expressionType) {
		this.currExpressionType = expressionType;
	}
	/**
	 * Returns the current ExpressionType.
	 * @return the current ExpressionType 
	 */
	public ExpressionType getExpressionType() {
		return currExpressionType;
	}

	/**
	 * Sets the current CalcExpression which can be either 
	 * a constant value or a formula, which has to be calculated. 
	 * @param calcExpression the calcExpression to set
	 */
	public void setCalcExpression(CalcExpression calcExpression) {
		if (calcExpression instanceof CalcConstant) {
			setExpressionType(ExpressionType.CONSTANT_VALUE);
		} else if (calcExpression instanceof CalcFormula) {
			setExpressionType(ExpressionType.FORMULA);
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
