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
package gasmas.physics.pipes;

import agentgui.math.calculation.CalcExeption;
import agentgui.math.calculation.CalcExpression;
import agentgui.math.calculation.CalcFormula;
import agentgui.math.calculation.CalcParameterNotSetException;

/**
 * This class calculates the Reynolds number (Cerbe Gl 4.2.5)
 * @author Nils
 *
 */
public class Reynolds extends CalcFormula {
	
	private static final long serialVersionUID = -7187520396158504836L;
	
	private CalcExpression diameter;
	private CalcExpression kinematicViscosity;
	private CalcExpression fluidVelocity;

	
	public Reynolds(CalcExpression diameter, CalcExpression kinematicViscosity, CalcExpression fluidVelocity) {
		this.setDiameter(diameter);
		this.setKinematicViscosity(kinematicViscosity);
		this.setFluidVelocity(fluidVelocity);
	}
	
	@Override
	public double getValue() throws CalcExeption {
		if(diameter == null || kinematicViscosity == null || fluidVelocity == null){
			throw new CalcParameterNotSetException();
		}
		return ((fluidVelocity.getValue() * diameter.getValue()) / kinematicViscosity.getValue());
	}

	/* (non-Javadoc)
	 * @see agentgui.math.calculation.CalcFormula#addParameter(java.lang.String, agentgui.math.calculation.CalcExpression)
	 */
	@Override
	public void addParameter(String name, CalcExpression expression) {
		super.addParameter(name, expression);
		if (name.equals("diameter")){
			this.diameter=expression;
		} else if (name.equals("kinematicViscosity")){
			this.kinematicViscosity = expression;
		} else if (name.equals("fluidVelocity")){
			this.fluidVelocity = expression;
		}
	}
	
	/**
	 * @param diameter the diameter to set
	 */
	public void setDiameter(CalcExpression diameter) {
		this.addParameter("diameter", diameter);
	}
	/**
	 * @param kinematicViscosity the kinematicViscosity to set
	 */
	public void setKinematicViscosity(CalcExpression kinematicViscosity) {
		this.addParameter("kinematicViscosity", kinematicViscosity);
	}
	/**
	 * @param fluidVelocity the fluidVelocity to set
	 */
	public void setFluidVelocity(CalcExpression fluidVelocity) {
		this.addParameter("fluidVelocity", fluidVelocity);
	}

}
