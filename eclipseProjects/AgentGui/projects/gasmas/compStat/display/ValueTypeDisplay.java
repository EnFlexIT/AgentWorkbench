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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ValueTypeDisplay extends ParameterDisplay {

	private static final long serialVersionUID = 1L;
	
	private String descriptionText = null;
	private Dimension descriptionTextSize = new Dimension(60, 26);
	private ValueType myValueType = null;  //  @jve:decl-index=0:
	
	private JPanel jPanelNumberDisplay = null;
	private JLabel jLabelValue = null;
	private JTextField jTextFieldValue = null;
	private JTextField jTextFieldUnit = null;

	/**
	 * This is the default constructor
	 */
	public ValueTypeDisplay(String descriptionText, Dimension descriptionTextSize) {
		this.descriptionText = descriptionText;
		this.descriptionTextSize = descriptionTextSize;
		this.initialize();
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
		return this.myValueType;
	}
	
	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		jLabelValue = new JLabel();
		jLabelValue.setText(this.descriptionText + ":");
		if (this.descriptionTextSize!=null) {
			jLabelValue.setSize(this.descriptionTextSize);
			jLabelValue.setPreferredSize(this.descriptionTextSize);
		}
		
		this.setSize(220, 26);
		this.setLayout(new BorderLayout());
		this.add(jLabelValue, BorderLayout.WEST);
		this.add(getJPanelNumberDisplay(), BorderLayout.CENTER);
	}

	/**
	 * This method initializes jPanelNumberDisplay	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelNumberDisplay() {
		if (jPanelNumberDisplay == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridx = -1;
			gridBagConstraints2.gridy = -1;
			gridBagConstraints2.weightx = 0.5;
			gridBagConstraints2.insets = new Insets(0, 2, 0, 0);
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridx = -1;
			gridBagConstraints1.gridy = -1;
			gridBagConstraints1.weightx = 0.5;
			gridBagConstraints1.insets = new Insets(0, 5, 0, 0);
			
			jPanelNumberDisplay = new JPanel();
			jPanelNumberDisplay.setLayout(new GridBagLayout());
			jPanelNumberDisplay.add(getJTextFieldValue(), gridBagConstraints1);
			jPanelNumberDisplay.add(getJTextFieldUnit(), gridBagConstraints2);
		}
		return jPanelNumberDisplay;
	}
	/**
	 * This method initializes jTextFieldValue	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldValue() {
		if (jTextFieldValue == null) {
			jTextFieldValue = new JTextField();
			jTextFieldValue.setPreferredSize(new Dimension(60, 26));
			jTextFieldValue.addKeyListener(this.getKeyAdapter4FloatNumbers());
			jTextFieldValue.addKeyListener(this.getKeyAdapter4Changes());
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
			jTextFieldUnit.setPreferredSize(new Dimension(60, 26));
			jTextFieldUnit.addKeyListener(this.getKeyAdapter4Changes());
		}
		return jTextFieldUnit;
	}

	/* (non-Javadoc)
	 * @see gasmas.compStat.display.ParameterDisplay#valueChangedInJTextField(java.lang.Object)
	 */
	@Override
	public void valueChangedInJTextField(JTextField source) {
		String parameterDescription = "valueType";
		if (source==this.getJTextFieldValue()) {
			Float floatValue = this.parseFloat(getJTextFieldValue().getText());
			this.getValueType().setValue(floatValue);
			
		} else if (source==this.getJTextFieldUnit()) {
			this.getValueType().setUnit(getJTextFieldUnit().getText());
		}
		this.informListener(parameterDescription, this.getValueType());
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
