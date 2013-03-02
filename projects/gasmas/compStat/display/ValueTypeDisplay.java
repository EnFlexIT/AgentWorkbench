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

import gasmas.ontology.ValueType;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;

import agentgui.core.common.KeyAdapter4Numbers;

import java.awt.Insets;
import java.awt.event.KeyAdapter;

public class ValueTypeDisplay extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private ValueType myValueType = null;  //  @jve:decl-index=0:
	
	private JLabel jLabelValue = null;
	private JTextField jTextFieldValue = null;
	private JLabel jLabelUnit = null;
	private JTextField jTextFieldUnit = null;

	private KeyAdapter4Numbers keyAdapter4FloatNumbers = null;  //  @jve:decl-index=0:
	
	/**
	 * This is the default constructor
	 */
	public ValueTypeDisplay() {
		super();
		initialize();
	}

	/**
	 * Sets the value type.
	 * @param valueType the new value type
	 */
	public void setValueType(ValueType valueType) {
		this.myValueType = valueType;
		if (this.myValueType ==null) {
			this.getJTextFieldValue().setText("0.0");
			this.getJTextFieldUnit().setText(null);
		} else {
			this.getJTextFieldValue().setText(((Float)this.myValueType.getValue()).toString());
			this.getJTextFieldUnit().setText(this.myValueType.getUnit());
		}
	}
	/**
	 * Gets the value type.
	 * @return the value type
	 */
	public ValueType getValueType() {
		if (this.myValueType==null) {
			this.myValueType = new ValueType();
		}
		if (this.getJTextFieldValue().getText()==null || this.getJTextFieldValue().getText().equals("")) {
			this.myValueType.setValue(0.0F);
		} else {
			this.myValueType.setValue(Float.parseFloat(this.getJTextFieldValue().getText()));
		}
		this.myValueType.setUnit(this.getJTextFieldUnit().getText());
		return this.myValueType;
	}
	
	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints25 = new GridBagConstraints();
		gridBagConstraints25.fill = GridBagConstraints.BOTH;
		gridBagConstraints25.gridy = 0;
		gridBagConstraints25.weightx = 1.0;
		gridBagConstraints25.insets = new Insets(0, 5, 0, 5);
		gridBagConstraints25.gridx = 3;
		GridBagConstraints gridBagConstraints24 = new GridBagConstraints();
		gridBagConstraints24.gridx = 2;
		gridBagConstraints24.insets = new Insets(0, 10, 0, 0);
		gridBagConstraints24.gridy = 0;
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.fill = GridBagConstraints.BOTH;
		gridBagConstraints1.gridy = 0;
		gridBagConstraints1.weightx = 1.0;
		gridBagConstraints1.insets = new Insets(0, 5, 0, 0);
		gridBagConstraints1.gridx = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.insets = new Insets(0, 5, 0, 0);
		gridBagConstraints.gridy = 0;

		jLabelUnit = new JLabel();
		jLabelUnit.setText("Unit");

		jLabelValue = new JLabel();
		jLabelValue.setText("Value");
		
		this.setSize(220, 26);
		this.setLayout(new GridBagLayout());
		this.add(jLabelValue, gridBagConstraints);
		this.add(getJTextFieldValue(), gridBagConstraints1);
		this.add(jLabelUnit, gridBagConstraints24);
		this.add(getJTextFieldUnit(), gridBagConstraints25);
	}

	/**
	 * This method initializes jTextFieldValue	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldValue() {
		if (jTextFieldValue == null) {
			jTextFieldValue = new JTextField();
			jTextFieldValue.setSize(new Dimension(100,26));
			jTextFieldValue.addKeyListener(this.getKeyAdapter4FloatNumbers());
		}
		return jTextFieldValue;
	}
	/**
	 * This method initializes jTextFieldUnit	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldUnit() {
		if (jTextFieldUnit == null) {
			jTextFieldUnit = new JTextField();
			jTextFieldUnit.setSize(new Dimension(100,26));
		}
		return jTextFieldUnit;
	}

	/**
	 * Returns a key adapter for integer numbers.
	 * @return the key adapter for integer numbers
	 */
	private KeyAdapter getKeyAdapter4FloatNumbers(){
		if (keyAdapter4FloatNumbers==null) {
			keyAdapter4FloatNumbers = new KeyAdapter4Numbers(true);
		}
		return keyAdapter4FloatNumbers;
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
