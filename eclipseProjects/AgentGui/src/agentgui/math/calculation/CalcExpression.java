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

import jade.util.leap.Serializable;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * This is the abstract super class CalcExpression that can be either
 * used to generate a complex calculation expression by using the classes
 * {@link CalcConstant}, {@link CalcParameter} or {@link CalcFormula}.
 * On the other hand, this class can just be extended in order to
 * generate a calculating class, where calculations are don within the
 * Java class itself. 
 * 
 * @author Nils Loose - DAWIS - ICB - University of Duisburg - Essen
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
@XmlRootElement(namespace="http://www.dawis.wiwi.uni-due.de/CalcExpression")
public abstract class CalcExpression implements Serializable {

	private static final long serialVersionUID = 3777486633158645068L;

	private String unit;
	
	
	/**
	 * Returns or calculates the value of the CalcExpression.
	 *
	 * @return the value
	 * @throws CalcExeption the CalcExeption
	 */
	public abstract double getValue() throws CalcExeption;

	
	/**
	 * Sets the unit.
	 * @param unit the new unit
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}
	/**
	 * Returns the unit.
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		try {
			return this.getValue() + " " + this.getUnit();
		} catch (CalcExeption ex) {
			ex.printStackTrace();
		}
		return "#ERROR"; 
	}

}
