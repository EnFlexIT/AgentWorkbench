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
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;

/**
 * The Class Calc9Parameter.
 */
public class ParameterArray9Display extends ParameterDisplay implements ParameterListener {

	private static final long serialVersionUID = 1L;

	private final Dimension idealSize = new Dimension(354, 108);  //  @jve:decl-index=0:
	private String headerText = "HeaderText";  //  @jve:decl-index=0:
	
	private JLabel jLabelHeader = null;
	private ParameterArray3Display calcParameterRange1 = null;
	private ParameterArray3Display calcParameterRange2 = null;
	private ParameterArray3Display calcParameterRange3 = null;

	
	/**
	 * This is the default constructor.
	 * @param headerText the header text
	 */
	public ParameterArray9Display(String headerText) {
		super();
		this.headerText = headerText;
		this.initialize();
	}

	/**
	 * This method initializes this.
	 * @return void
	 */
	private void initialize() {
		
		GridBagConstraints gridBagConstraintsHeader = new GridBagConstraints();
		gridBagConstraintsHeader.gridx = 0;
		gridBagConstraintsHeader.gridy = 0;
		gridBagConstraintsHeader.gridwidth = 3;
		gridBagConstraintsHeader.anchor = GridBagConstraints.WEST;
		gridBagConstraintsHeader.ipadx = 3;
		gridBagConstraintsHeader.weightx = 1.0;
		gridBagConstraintsHeader.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraintsHeader.insets = new Insets(0, 0, 2 , 0);
		
		GridBagConstraints gridBagConstraintsRange1 = new GridBagConstraints();
		gridBagConstraintsRange1.insets = new Insets(0, 0, 0, 0);
		gridBagConstraintsRange1.gridy = 1;
		gridBagConstraintsRange1.fill = GridBagConstraints.VERTICAL;
		gridBagConstraintsRange1.weightx = 0.0;
		gridBagConstraintsRange1.weighty = 0.0;
		gridBagConstraintsRange1.gridx = 0;
		
		GridBagConstraints gridBagConstraintsRange2 = new GridBagConstraints();
		gridBagConstraintsRange2.gridx = 1;
		gridBagConstraintsRange2.insets = new Insets(0, 0, 2, 2);
		gridBagConstraintsRange2.fill = GridBagConstraints.VERTICAL;
		gridBagConstraintsRange2.gridy = 1;
		
		GridBagConstraints gridBagConstraintsRange3 = new GridBagConstraints();
		gridBagConstraintsRange3.gridx = 2;
		gridBagConstraintsRange3.insets = new Insets(0, 0, 0, 0);
		gridBagConstraintsRange3.fill = GridBagConstraints.VERTICAL;
		gridBagConstraintsRange3.gridy = 1;

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0};
		
		this.setLayout(gridBagLayout);
		this.setSize(this.idealSize);
		this.setPreferredSize(this.idealSize);
		this.add(getJLabelHeader(), gridBagConstraintsHeader);
		this.add(getCalcParameterRange1(), gridBagConstraintsRange1);
		this.add(getCalcParameterRange2(), gridBagConstraintsRange2);
		this.add(getCalcParameterRange3(), gridBagConstraintsRange3);
		
	}
	/**
	 * This method initializes jTextFieldHeader.
	 * @return javax.swing.JTextField
	 */
	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel(this.headerText);
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
			jLabelHeader.setSize(new Dimension(200, 16));
			jLabelHeader.setPreferredSize(new Dimension(200, 16));
		}
		return jLabelHeader;
	}
	/**
	 * Gets the calc parameter range1.
	 * @return the calc parameter range1
	 */
	private ParameterArray3Display getCalcParameterRange1() {
		if (calcParameterRange1==null) {
			calcParameterRange1 = new ParameterArray3Display(1);
			calcParameterRange1.setSize(calcParameterRange1.getIdealSize());
			calcParameterRange1.setPreferredSize(calcParameterRange1.getIdealSize());
			calcParameterRange1.addParameterListener(this);
		}
		return calcParameterRange1;
	}
	/**
	 * Gets the calc parameter range2.
	 * @return the calc parameter range2
	 */
	private ParameterArray3Display getCalcParameterRange2() {
		if (calcParameterRange2==null) {
			calcParameterRange2 = new ParameterArray3Display(4);
			calcParameterRange2.setSize(calcParameterRange2.getIdealSize());
			calcParameterRange2.setPreferredSize(calcParameterRange2.getIdealSize());
			calcParameterRange2.addParameterListener(this);
		}
		return calcParameterRange2;
	}
	/**
	 * Gets the calc parameter range3.
	 * @return the calc parameter range3
	 */
	private ParameterArray3Display getCalcParameterRange3() {
		if (calcParameterRange3==null) {
			calcParameterRange3 = new ParameterArray3Display(7);
			calcParameterRange3.setSize(calcParameterRange3.getIdealSize());
			calcParameterRange3.setPreferredSize(calcParameterRange3.getIdealSize());
			calcParameterRange3.addParameterListener(this);
		}
		return calcParameterRange3;
	}

	/**
	 * Returns the ideal size.
	 * @return the ideal size
	 */
	public Dimension getIdealSize() {
		return idealSize;
	}
	
	/**
	 * Returns a parameter value specified by its display number as String.
	 *
	 * @param parameterDescription the parameter description
	 * @return the parameter
	 */
	public Object getParameter(String parameterDescription) {
		Integer noOfParameter = Integer.parseInt(parameterDescription);
		Float currValue = null;
		if (noOfParameter>0 && noOfParameter<=3) {
			currValue = (Float) this.getCalcParameterRange1().getParameter(noOfParameter.toString());
		} else if (noOfParameter>3 && noOfParameter<=6) {
			currValue = (Float) this.getCalcParameterRange2().getParameter(noOfParameter.toString());
		} else if (noOfParameter>6 && noOfParameter<=9) {
			currValue = (Float) this.getCalcParameterRange3().getParameter(noOfParameter.toString());
		}
		return currValue;
	}

	/**
	 * Sets a parameter value specified by its display number as String.
	 *
	 * @param parameterDescription the parameter description
	 * @param value the value
	 */
	public void setParameter(String parameterDescription, Object value) {
		Integer noOfParameter = Integer.parseInt(parameterDescription);
		if (noOfParameter>0 && noOfParameter<=3) {
			this.getCalcParameterRange1().setParameter(noOfParameter.toString(), value);
		} else if (noOfParameter>3 && noOfParameter<=6) {
			this.getCalcParameterRange2().setParameter(noOfParameter.toString(), value);
		} else if (noOfParameter>6 && noOfParameter<=9) {
			this.getCalcParameterRange3().setParameter(noOfParameter.toString(), value);
		}
	}
	
	/* (non-Javadoc)
	 * @see gasmas.compStat.display.ParameterListener#parameterChanged(gasmas.compStat.display.ParameterDisplay, java.lang.String, java.lang.Object)
	 */
	@Override
	public void subParameterChanged(ParameterDisplay display, String parameterDescription, Object value) {
		this.informListener(parameterDescription, value);
	}


}  //  @jve:decl-index=0:visual-constraint="10,10"
