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
public class Calc9ParameterDisplay extends CalcParameterDisplay implements CalcParameterListener {

	private static final long serialVersionUID = 1L;

	private final Dimension idealSize = new Dimension(395, 134);  //  @jve:decl-index=0:
	private String headerText = "HeaderText";  //  @jve:decl-index=0:
	
	private JLabel jLabelHeader = null;
	private Calc3ParameterDisplay calcParameterRange1 = null;
	private Calc3ParameterDisplay calcParameterRange2 = null;
	private Calc3ParameterDisplay calcParameterRange3 = null;

	
	/**
	 * This is the default constructor.
	 * @param headerText the header text
	 */
	public Calc9ParameterDisplay(String headerText) {
		super();
		this.headerText = headerText;
		this.initialize();
	}

	/**
	 * This method initializes this.
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
		gridBagConstraints16.gridwidth = 3;
		gridBagConstraints16.ipadx = 2;
		gridBagConstraints16.gridy = 0;
		gridBagConstraints16.weightx = 0.0;
		gridBagConstraints16.insets = new Insets(5, 7, 5, 5);
		gridBagConstraints16.anchor = GridBagConstraints.WEST;
		gridBagConstraints16.gridx = 0;
		GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
		gridBagConstraints15.gridx = 2;
		gridBagConstraints15.insets = new Insets(0, 5, 5, 5);
		gridBagConstraints15.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints15.gridy = 1;
		GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
		gridBagConstraints14.gridx = 1;
		gridBagConstraints14.insets = new Insets(0, 5, 5, 0);
		gridBagConstraints14.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints14.gridy = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.insets = new Insets(0, 5, 5, 0);
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.weighty = 0.0;
		gridBagConstraints.gridx = 0;
		
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 0.0};
		this.setLayout(gridBagLayout);
		this.setSize(this.idealSize);
		this.setPreferredSize(this.idealSize);
		
		this.add(getJLabelHeader(), gridBagConstraints16);
		this.add(getCalcParameterRange1(), gridBagConstraints);
		this.add(getCalcParameterRange2(), gridBagConstraints14);
		this.add(getCalcParameterRange3(), gridBagConstraints15);
		
	}
	/**
	 * This method initializes jTextFieldHeader.
	 * @return javax.swing.JTextField
	 */
	private JLabel getJLabelHeader() {
		if (jLabelHeader == null) {
			jLabelHeader = new JLabel(this.headerText);
			jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		}
		return jLabelHeader;
	}
	/**
	 * Gets the calc parameter range1.
	 * @return the calc parameter range1
	 */
	private Calc3ParameterDisplay getCalcParameterRange1() {
		if (calcParameterRange1==null) {
			calcParameterRange1 = new Calc3ParameterDisplay(1);
			calcParameterRange1.setSize(calcParameterRange1.getIdealSize());
			calcParameterRange1.setPreferredSize(calcParameterRange1.getIdealSize());
			calcParameterRange1.addCalcParameterListener(this);
		}
		return calcParameterRange1;
	}
	/**
	 * Gets the calc parameter range2.
	 * @return the calc parameter range2
	 */
	private Calc3ParameterDisplay getCalcParameterRange2() {
		if (calcParameterRange2==null) {
			calcParameterRange2 = new Calc3ParameterDisplay(4);
			calcParameterRange2.setSize(calcParameterRange2.getIdealSize());
			calcParameterRange2.setPreferredSize(calcParameterRange2.getIdealSize());
			calcParameterRange2.addCalcParameterListener(this);
		}
		return calcParameterRange2;
	}
	/**
	 * Gets the calc parameter range3.
	 * @return the calc parameter range3
	 */
	private Calc3ParameterDisplay getCalcParameterRange3() {
		if (calcParameterRange3==null) {
			calcParameterRange3 = new Calc3ParameterDisplay(7);
			calcParameterRange3.setSize(calcParameterRange3.getIdealSize());
			calcParameterRange3.setPreferredSize(calcParameterRange3.getIdealSize());
			calcParameterRange3.addCalcParameterListener(this);
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
	 * Gets the parameter.
	 *
	 * @param noOfParameter the no of parameter
	 * @return the parameter
	 */
	public Float getParameter(int noOfParameter) {
		
		Float currValue = null;
		if (noOfParameter>0 && noOfParameter<=3) {
			currValue = this.getCalcParameterRange1().getParameter(noOfParameter);
		} else if (noOfParameter>3 && noOfParameter<=6) {
			currValue = this.getCalcParameterRange2().getParameter(noOfParameter);
		} else if (noOfParameter>6 && noOfParameter<=9) {
			currValue = this.getCalcParameterRange3().getParameter(noOfParameter);
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
		if (noOfParameter>0 && noOfParameter<=3) {
			this.getCalcParameterRange1().setParameter(noOfParameter, value);
		} else if (noOfParameter>3 && noOfParameter<=6) {
			this.getCalcParameterRange2().setParameter(noOfParameter, value);
		} else if (noOfParameter>6 && noOfParameter<=9) {
			this.getCalcParameterRange3().setParameter(noOfParameter, value);
		}
	}
	
	/**
	 * Inform listener about changes.
	 * @param textField the text field
	 */
	private void informListener(int noOfParameter, Float value) {
		if (this.calcParameterListener!=null) {
			for (int i = 0; i < calcParameterListener.size(); i++) {
				CalcParameterListener listener = calcParameterListener.get(i);
				listener.parameterChanged(this, noOfParameter, value);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see gasmas.compStat.display.CalcParameterListener#parameterChanged(int, java.lang.Float)
	 */
	@Override
	public void parameterChanged(CalcParameterDisplay display, int noOfParameter, Float value) {
		this.informListener(noOfParameter, value);
		System.out.println("Changed " + noOfParameter + " - value: " + value);
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
