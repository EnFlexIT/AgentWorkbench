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

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;

import agentgui.core.common.KeyAdapter4Numbers;

/**
 * The Class Calc3ParameterDisplay.
 */
public class Calc3ParameterDisplay extends ParameterDisplay {

	private static final long serialVersionUID = -8338576946309181639L;

	private final Dimension idealSize = new Dimension(125, 98);  //  @jve:decl-index=0:
	
	private HashMap<Integer, JTextField> parameterFields = new HashMap<Integer, JTextField>();  //  @jve:decl-index=0:
	private int startAtParameter = 1;
	
	private JTextField jTextFieldCoeff_1 = null;
	private JTextField jTextFieldCoeff_2 = null;
	private JTextField jTextFieldCoeff_3 = null;

	private JLabel jLabel1 = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel3 = null;
	
	private KeyAdapter4Numbers keyAdapter4Numbers = null;
	private KeyAdapter keyAdapter4Changes = null;  //  @jve:decl-index=0:
	
	
	/**
	 * This method initializes 
	 */
	public Calc3ParameterDisplay(int startNoOfParameter) {
		super();
		if (startNoOfParameter>0) {
			this.startAtParameter = startNoOfParameter;
		}
		this.keyAdapter4Numbers = new KeyAdapter4Numbers(true);
		this.initialize();
	}

	/**
	 * Gets the parameter.
	 *
	 * @param noOfParameter the no of parameter
	 * @return the parameter
	 */
	public Float getParameter(int noOfParameter) {
		Float currValue = null;
		JTextField jTextFieldCoeff = this.parameterFields.get(noOfParameter);
		if (jTextFieldCoeff!=null) {
			currValue = Float.parseFloat(jTextFieldCoeff.getText());
		}
		return currValue;
	}
	/**
	 * Gets the parameter.
	 *
	 * @param noOfParameter the no of parameter
	 * @return the parameter
	 */
	public void setParameter(int noOfParameter, Float value) {
		JTextField jTextFieldCoeff = this.parameterFields.get(noOfParameter);
		if (jTextFieldCoeff!=null) {
			jTextFieldCoeff.setText(value.toString());
		}
	}
	
	/**
	 * Inform listener about changes.
	 * @param textField the text field
	 */
	private void informListener(JTextField textField) {
		if (this.parameterListener!=null) {
			for (int i = 0; i < parameterListener.size(); i++) {
				ParameterListener listener = parameterListener.get(i);
				int noOfParameter = this.getParameterNo(textField);
				String stringValue = textField.getText();
				Float value = Float.parseFloat(stringValue);
				listener.parameterChanged(this, noOfParameter, value);
			}
		}
	}

	/**
	 * Gets the corresponding parameter no for a given jTextField.
	 *
	 * @param jTextFieldSearch the j text field search
	 * @return the parameter no
	 */
	private int getParameterNo(JTextField jTextFieldSearch) {
		for (int i = 0; i < parameterFields.size(); i++) {
			if (parameterFields.get(i)==jTextFieldSearch) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * This method initializes this
	 */
	private void initialize() {
       
		this.setSize(this.idealSize);
        this.setPreferredSize(this.idealSize);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));
        
        int i = this.startAtParameter;
        jLabel1 = new JLabel();
        jLabel1.setText(i + ":");
        jLabel1.setPreferredSize(new Dimension(18, 16));
        this.add(jLabel1);
        this.add(getJTextFieldCoeff_1());
        this.parameterFields.put(i, getJTextFieldCoeff_1());
        
        i++;
        jLabel2 = new JLabel();
        jLabel2.setText(i + ":");
        jLabel2.setPreferredSize(new Dimension(18, 16));
        this.add(jLabel2);
        this.add(getJTextFieldCoeff_2());
        this.parameterFields.put(i, getJTextFieldCoeff_2());
        
        i++;
        jLabel3 = new JLabel();
        jLabel3.setText(i + ":");
        jLabel3.setPreferredSize(new Dimension(18, 16));
        this.add(jLabel3);
        this.add(getJTextFieldCoeff_3());
        this.parameterFields.put(i, getJTextFieldCoeff_3());
			
	}

	/**
	 * This method initializes jTextFieldCoeff_1	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldCoeff_1() {
		if (jTextFieldCoeff_1 == null) {
			jTextFieldCoeff_1 = new JTextField();
			jTextFieldCoeff_1.setPreferredSize(new Dimension(100,26));
			jTextFieldCoeff_1.addKeyListener(this.keyAdapter4Numbers);
			jTextFieldCoeff_1.addKeyListener(this.getKeyAdapter4Changes());
		}
		return jTextFieldCoeff_1;
	}
	/**
	 * This method initializes jTextFieldCoeff_1	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldCoeff_2() {
		if (jTextFieldCoeff_2 == null) {
			jTextFieldCoeff_2 = new JTextField();
			jTextFieldCoeff_2.setPreferredSize(new Dimension(100,26));
			jTextFieldCoeff_2.addKeyListener(this.keyAdapter4Numbers);
			jTextFieldCoeff_2.addKeyListener(this.getKeyAdapter4Changes());
		}
		return jTextFieldCoeff_2;
	}
	/**
	 * This method initializes jTextFieldCoeff_1	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldCoeff_3() {
		if (jTextFieldCoeff_3 == null) {
			jTextFieldCoeff_3 = new JTextField();
			jTextFieldCoeff_3.setPreferredSize(new Dimension(100,26));
			jTextFieldCoeff_3.addKeyListener(this.keyAdapter4Numbers);
			jTextFieldCoeff_3.addKeyListener(this.getKeyAdapter4Changes());
		}
		return jTextFieldCoeff_3;
	}
	
	/**
	 * Gets the key adapter that is listening to changes.
	 * @return the key adapter for changes
	 */
	private KeyAdapter getKeyAdapter4Changes() {
		if (keyAdapter4Changes==null) {
			keyAdapter4Changes = new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent ke) {
					super.keyReleased(ke);
					if (ke.getSource() instanceof JTextField) {
						informListener((JTextField) ke.getSource());	
					}
				}
			};
		}
		return keyAdapter4Changes;
	}

	/**
	 * Returns the ideal size.
	 * @return the ideal size
	 */
	public Dimension getIdealSize() {
		return idealSize;
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
