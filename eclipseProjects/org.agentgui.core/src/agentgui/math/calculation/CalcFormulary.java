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

import jade.util.leap.Serializable;

/**
 * The Class CalcFormulary.
 */
public class CalcFormulary implements Serializable {

	private static final long serialVersionUID = 3737871113281364105L;

	private HashMap<String, CalcFormula> formulary = new HashMap<String, CalcFormula>();
	
	
	/**
	 * Instantiates a new formulary of possible calculations.
	 */
	public CalcFormulary() {
	
	}

	/**
	 * Sets the formulary.
	 * @param formulary the formulary to set
	 */
	public void setFormulary(HashMap<String, CalcFormula> formulary) {
		this.formulary = formulary;
	}
	/**
	 * Gets the formulary.
	 * @return the formulary
	 */
	public HashMap<String, CalcFormula> getFormulary() {
		return formulary;
	}
	
}
