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
 * This class implements constant values.
 *
 * @author Nils
 */
public class CalcConstant extends CalcExpression {
	
	private static final long serialVersionUID = 953391125028424724L;
	
	/**
	 * The value of the constant.
	 */
	private double value;
	
	/**
	 * Instantiates a new constant value.
	 * @param value the value
	 */
	public CalcConstant(double value){
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see agentgui.core.gui.calculation.CalcExpression#getValue()
	 */
	@Override
	public double getValue() {
		return this.value;
	}
	
	/**
	 * Sets the value.
	 * @param newValue the new value
	 */
	public void setValue(double newValue) {
		this.value = newValue;
	}
	
}
