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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTextField;

import agentgui.core.common.KeyAdapter4Numbers;

/**
 * The abstract Class CalcParameterDisplay.
 */
public abstract class ParameterDisplay extends JPanel {

	private static final long serialVersionUID = 849112163626006292L;

	protected Vector<ParameterListener> parameterListener = new Vector<ParameterListener>(); 
	
	private KeyAdapter keyAdapter4Changes = null;  
	private KeyAdapter4Numbers keyAdapter4FloatNumbers = null;
	
	
	/**
	 * Adds a ParameterListener to this instance.
	 */
	public void addParameterListener(ParameterListener listener) {
		if (this.parameterListener==null) {
			this.parameterListener = new Vector<ParameterListener>();
		}
		this.parameterListener.add(listener);
	}
	
	/**
	 * Inform listener about changes.
	 * @param textField the text field
	 */
	protected void informListener(String parameterDescription, Object value) {
		if (this.parameterListener!=null && parameterDescription!=null) {
			for (int i = 0; i < parameterListener.size(); i++) {
				ParameterListener listener = parameterListener.get(i);
				listener.subParameterChanged(this, parameterDescription, value);
			}
		}
	}
	
	/**
	 * Will be invoked if a value changed in a JTextField and if this JTextField
	 * uses the local KeyAdapter for changes. If you want to react on such change
	 * event, you have to overwrite this method.  
	 * 
	 * {@see #getKeyAdapter4Changes()}
	 * @param sourceTextField the source
	 */
	protected void valueChangedInJTextField(JTextField sourceTextField) {
	}
	
	/**
	 * Returns a parameter value.
	 *
	 * @param parameterDescription the parameter description
	 * @return the parameter
	 */
	public abstract Object getParameter(String parameterDescription);
	
	/**
	 * Sets a parameter value.
	 *
	 * @param parameterDescription the parameter description
	 * @param value the value
	 * @return the parameter
	 */
	public abstract void setParameter(String parameterDescription, Object value);
	
	
	/**
	 * Gets the key adapter that is listening to changes in a JComponent.
	 * Basically this can be applied to JTextFields 
	 * @return the key adapter for changes
	 */
	protected KeyAdapter getKeyAdapter4Changes() {
		if (keyAdapter4Changes==null) {
			keyAdapter4Changes = new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent ke) {
					super.keyReleased(ke);
					if (ke.getSource() instanceof JTextField) {
						valueChangedInJTextField((JTextField) ke.getSource());
					}
				}
			};
		}
		return keyAdapter4Changes;
	}
	
	/**
	 * Gets the key adapter for float numbers.
	 * @return the key adapter for float numbers
	 */
	protected KeyAdapter4Numbers getKeyAdapter4FloatNumbers() {
		if (keyAdapter4FloatNumbers==null) {
			keyAdapter4FloatNumbers = new KeyAdapter4Numbers(true);
		}
		return keyAdapter4FloatNumbers;
	}
	
	/**
	 * Tries to parse a String into a Float. If parsing is not possible the mehthod
	 * returns 0.0F
	 *
	 * @param valueString the value string
	 * @return the float
	 */
	protected float parseFloat(String valueString) {
		Float floatValue = null;
		try {
			floatValue = Float.parseFloat(valueString);
		} catch (Exception ex) {
			floatValue = 0.0F;
		}
		return floatValue;
	}
	
}
