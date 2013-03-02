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
package gasmas.compStat.display;

import java.util.Vector;

import javax.swing.JPanel;

/**
 * The abstract Class CalcParameterDisplay.
 */
public abstract class ParameterDisplay extends JPanel {

	private static final long serialVersionUID = 849112163626006292L;

	protected Vector<ParameterListener> parameterListener = new Vector<ParameterListener>(); 
	
	
	/**
	 * Adds a CalcParameterListener to this instance.
	 */
	public void addCalcParameterListener(ParameterListener listener) {
		if (this.parameterListener==null) {
			this.parameterListener = new Vector<ParameterListener>();
		}
		this.parameterListener.add(listener);
	}
	
	
	/**
	 * Returns a parameter value.
	 *
	 * @param noOfParameter the no of parameter
	 * @return the parameter
	 */
	public abstract Float getParameter(int positionOfParameter);
	
	/**
	 * Sets a parameter value.
	 *
	 * @param noOfParameter the no of parameter
	 * @return the parameter
	 */
	public abstract void setParameter(int positionOfParameter, Float value);
	
	
	
}
