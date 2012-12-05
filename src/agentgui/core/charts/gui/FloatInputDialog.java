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
package agentgui.core.charts.gui;

import java.awt.Window;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class FloatInputDialog extends KeyInputDialog{
	
	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	// Swing components
	private JSpinner spFloatInput;
	
	public FloatInputDialog(Window owner, String title, String message) {
		super(owner);
		setTitle(title);
		setModal(true);
		
		initialize(message);
	}
	
	protected JSpinner getInputComponent() {
		if (spFloatInput == null) {
			spFloatInput = new JSpinner(new SpinnerNumberModel(0.0, 0.0, Float.MAX_VALUE, 0.1));
		}
		return spFloatInput;
	}
	
	public Float getValue(){
		return ((Double) spFloatInput.getModel().getValue()).floatValue();
	}

}
