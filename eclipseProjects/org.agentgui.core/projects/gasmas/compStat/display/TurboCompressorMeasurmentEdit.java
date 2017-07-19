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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import agentgui.core.config.GlobalInfo;
import gasmas.ontology.ValueType;


/**
 * The Class TurboCompressorMeasurmentEdit.
 */
public class TurboCompressorMeasurmentEdit extends JDialog implements ParameterListener, ActionListener {

	private static final long serialVersionUID = 2855499013196571575L;
	
	public static final String rowHeaderSettleline = "Settleline";
	
	private Image imageAgentGUI = GlobalInfo.getInternalImage("AgentGUI.png");
	
	private Dimension valueTypeDimension = new Dimension(120, 26);
	private Dimension buttonDimension = new Dimension(80, 26);
	
	private boolean canceled = false;
	private Vector<Object> rowVector = null;  //  @jve:decl-index=0:
	
	private JPanel jContentPane = null;
	private JTextField jTextFieldRowHeader = null;
	private JLabel jLabelRowHeader = null;

	private ValueTypeDisplay valueTypeDisplaySpeed = null;
	private ValueTypeDisplay valueTypeDisplayFlowRate = null;  //  @jve:decl-index=0:visual-constraint="487,137"
	private ValueTypeDisplay valueTypeDisplayAdiabaticHead = null;

	private JPanel jPanelRowHeaderOption = null;
	private ButtonGroup rowHeaderButtonGroup = null;  //  @jve:decl-index=0:
	private JRadioButton jRadioButtonRowHeaderEff = null;
	private JRadioButton jRadioButtonRowHeaderSettleline = null;

	private JPanel jPanelButtons = null;
	private JButton jButtonApply = null;
	private JButton jButtonCancel = null;

	private JLabel jLabelDummy = null;


	/**
	 * This is the xxx default constructor
	 */
	public TurboCompressorMeasurmentEdit(Vector<Object> row) {
		super();
		this.rowVector = row;
		this.initialize();
		this.setMeasurement(this.rowVector);
	}
	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		this.setModal(true);
		this.setTitle("Turbocompressor Measurement");
		this.setIconImage(imageAgentGUI);
		this.setSize(400, 254);
		this.setContentPane(getJContentPane());
		
		// --- Center Dialog ------------------------------
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int top = (screenSize.height - this.getHeight()) / 2; 
	    int left = (screenSize.width - this.getWidth()) / 2; 
	    this.setLocation(left, top);			
	    this.registerEscapeKeyStroke();
	    
	}

	/**
	 * Sets the measurement.
	 * @param rowVector the measurement coming from the table view
	 */
	public void setMeasurement(Vector<Object> rowVector) {
		this.rowVector = rowVector;
		
		String rowHeader = (String) this.rowVector.get(0);
		this.getJTextFieldRowHeader().setText(rowHeader);
		if (rowHeader.equals(rowHeaderSettleline)) {
			this.getJRadioButtonRowHeaderEff().setSelected(false);
			this.getJRadioButtonSettleline().setSelected(true);
			this.getJTextFieldRowHeader().setEditable(false);
		} else {
			this.getJRadioButtonSettleline().setSelected(false);
			this.getJRadioButtonRowHeaderEff().setSelected(true);
		}
		
		this.getValueTypeDisplaySpeed().setValueType((ValueType) this.rowVector.get(1));
		this.getValueTypeDisplayFlowRate().setValueType((ValueType) this.rowVector.get(2));
		this.getValueTypeDisplayAdiabaticHead().setValueType((ValueType) this.rowVector.get(3));
		
	}
	/**
	 * Gets the measurement.
	 * @return the measurement
	 */
	public Vector<Object> getMeasurement() {
		if (rowVector==null) {
			rowVector = new Vector<Object>();
			rowVector.add(new String());
			rowVector.add(new ValueType());
			rowVector.add(new ValueType());
			rowVector.add(new ValueType());
		}
		rowVector.set(0, this.getJTextFieldRowHeader().getText());
		rowVector.set(1, this.getValueTypeDisplaySpeed().getValueType());
		rowVector.set(2, this.getValueTypeDisplayFlowRate().getValueType());
		rowVector.set(3, this.getValueTypeDisplayAdiabaticHead().getValueType());
		return this.rowVector;
	}
	/**
	 * Sets the canceled.
	 * @param canceled the new canceled
	 */
	private void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	/**
	 * Checks if is canceled.
	 * @return true, if is canceled
	 */
	public boolean isCanceled() {
		return canceled;
	}
	/**
     * Registers the escape key stroke in order to close this dialog.
     */
    private void registerEscapeKeyStroke() {
    	final ActionListener listener = new ActionListener() {
            public final void actionPerformed(final ActionEvent e) {
    			setVisible(false);
            }
        };
        final KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true);
        this.getRootPane().registerKeyboardAction(listener, keyStroke, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }
	
	/**
	 * This method initializes jContentPane
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			
			GridBagConstraints gbRowHeaderOption = new GridBagConstraints();
			gbRowHeaderOption.gridx = 1;
			gbRowHeaderOption.fill = GridBagConstraints.NONE;
			gbRowHeaderOption.anchor = GridBagConstraints.WEST;
			gbRowHeaderOption.insets = new Insets(15, 5, 0, 5);
			gbRowHeaderOption.gridy = 0;

			GridBagConstraints gbJLableRowHeader = new GridBagConstraints();
			gbJLableRowHeader.gridx = 0;
			gbJLableRowHeader.insets = new Insets(5, 10, 0, 0);
			gbJLableRowHeader.anchor = GridBagConstraints.WEST;
			gbJLableRowHeader.gridy = 2;
			
			GridBagConstraints gbJTextFieldHeader = new GridBagConstraints();
			gbJTextFieldHeader.fill = GridBagConstraints.BOTH;
			gbJTextFieldHeader.gridy = 2;
			gbJTextFieldHeader.weightx = 1.0;
			gbJTextFieldHeader.gridx = 1;
			gbJTextFieldHeader.insets = new Insets(5, 5, 0, 10);
			
			GridBagConstraints gbValueTypeSpeed = new GridBagConstraints();
			gbValueTypeSpeed.gridx = 0;
			gbValueTypeSpeed.fill = GridBagConstraints.HORIZONTAL;
			gbValueTypeSpeed.gridwidth = 2;
			gbValueTypeSpeed.insets = new Insets(5, 10, 0, 10);
			gbValueTypeSpeed.gridy = 3;
			
			GridBagConstraints gbValueTypeFlowRate = new GridBagConstraints();
			gbValueTypeFlowRate.gridx = 0;
			gbValueTypeFlowRate.fill = GridBagConstraints.HORIZONTAL;
			gbValueTypeFlowRate.gridy = 4;
			gbValueTypeFlowRate.insets = new Insets(5, 10, 0, 10);
			gbValueTypeFlowRate.gridwidth = 2;
			
			GridBagConstraints gbValueTypeAdiabativHead = new GridBagConstraints();
			gbValueTypeAdiabativHead.gridx = 0;
			gbValueTypeAdiabativHead.gridwidth = 2;
			gbValueTypeAdiabativHead.fill = GridBagConstraints.HORIZONTAL;
			gbValueTypeAdiabativHead.insets = new Insets(5, 10, 0, 10);
			gbValueTypeAdiabativHead.gridy = 5;
			
			GridBagConstraints gbButtons = new GridBagConstraints();
			gbButtons.gridx = 1;
			gbButtons.insets = new Insets(15, 0, 5, 10);
			gbButtons.anchor = GridBagConstraints.WEST;
			gbButtons.gridwidth = 1;
			gbButtons.fill = GridBagConstraints.HORIZONTAL;
			gbButtons.weighty = 0.0;
			gbButtons.gridy = 6;

			GridBagConstraints gbDummy = new GridBagConstraints();
			gbDummy.gridx = 0;
			gbDummy.weighty = 1.0;
			gbDummy.fill = GridBagConstraints.BOTH;
			gbDummy.gridy = 7;
			
			jLabelRowHeader = new JLabel();
			jLabelRowHeader.setText("Efficiency / Settleline");
			jLabelRowHeader.setSize(this.valueTypeDimension);
			jLabelRowHeader.setPreferredSize(this.valueTypeDimension);
			
			jLabelDummy = new JLabel();
			jLabelDummy.setText("");
			
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(jLabelRowHeader, gbJLableRowHeader);
			jContentPane.add(getJTextFieldRowHeader(), gbJTextFieldHeader);
			jContentPane.add(getValueTypeDisplaySpeed(), gbValueTypeSpeed);
			jContentPane.add(getValueTypeDisplayFlowRate(), gbValueTypeFlowRate);
			jContentPane.add(getValueTypeDisplayAdiabaticHead(), gbValueTypeAdiabativHead);
			jContentPane.add(getJPanelRowHeaderOption(), gbRowHeaderOption);
			jContentPane.add(getJPanelButtons(), gbButtons);
			jContentPane.add(jLabelDummy, gbDummy);
			
		}
		return jContentPane;
	}
	/**
	 * This method initializes jTextFieldHeader	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldRowHeader() {
		if (jTextFieldRowHeader == null) {
			jTextFieldRowHeader = new JTextField();
			jTextFieldRowHeader.setSize(new Dimension(120, 26));
			jTextFieldRowHeader.setPreferredSize(new Dimension(120, 26));
		}
		return jTextFieldRowHeader;
	}
	/**
	 * Gets the value type display speed.
	 * @return the value type display speed
	 */
	private ValueTypeDisplay getValueTypeDisplaySpeed() {
		if (valueTypeDisplaySpeed == null) {
			valueTypeDisplaySpeed = new ValueTypeDisplay("Speed [1/min]", valueTypeDimension);
			valueTypeDisplaySpeed.addParameterListener(this);
		}
		return valueTypeDisplaySpeed;
	}
	/**
	 * Gets the value type display flow rate.
	 * @return the value type display flow rate
	 */
	private ValueTypeDisplay getValueTypeDisplayFlowRate() {
		if (valueTypeDisplayFlowRate== null) {
			valueTypeDisplayFlowRate = new ValueTypeDisplay("Vol. Flow Rate [m²/s]", valueTypeDimension);
			valueTypeDisplayFlowRate.addParameterListener(this);
		}
		return valueTypeDisplayFlowRate;
	}
	/**
	 * Gets the value type display adiabatic head.
	 * @return the value type display adiabatic head
	 */
	private ValueTypeDisplay getValueTypeDisplayAdiabaticHead() {
		if (valueTypeDisplayAdiabaticHead== null) {
			valueTypeDisplayAdiabaticHead = new ValueTypeDisplay("Ad. Head [kJ/kg]", valueTypeDimension);
			valueTypeDisplayAdiabaticHead.addParameterListener(this);
		}
		return valueTypeDisplayAdiabaticHead;
	}
	
	/**
	 * This method initializes jPanelRowHeaderOption	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelRowHeaderOption() {
		if (jPanelRowHeaderOption == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.insets = new Insets(0, 40, 0, 0);
			gridBagConstraints2.gridy = 0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = -1;
			gridBagConstraints.gridy = -1;
			
			rowHeaderButtonGroup = new ButtonGroup();
		    rowHeaderButtonGroup.add(getJRadioButtonRowHeaderEff());
		    rowHeaderButtonGroup.add(getJRadioButtonSettleline());
		    
			jPanelRowHeaderOption = new JPanel();
			jPanelRowHeaderOption.setLayout(new GridBagLayout());
			jPanelRowHeaderOption.add(getJRadioButtonRowHeaderEff(), gridBagConstraints);
			jPanelRowHeaderOption.add(getJRadioButtonSettleline(), gridBagConstraints2);
		}
		return jPanelRowHeaderOption;
	}
	/**
	 * This method initializes jRadioButtonRowHeaderEff	
	 * @return javax.swing.JRadioButton	
	 */
	private JRadioButton getJRadioButtonRowHeaderEff() {
		if (jRadioButtonRowHeaderEff == null) {
			jRadioButtonRowHeaderEff = new JRadioButton();
			jRadioButtonRowHeaderEff.setText("Efficiency");
			jRadioButtonRowHeaderEff.addActionListener(this);
		}
		return jRadioButtonRowHeaderEff;
	}
	/**
	 * Gets the radio button for the settleline.
	 * @return the radio button for the settleline
	 */
	private JRadioButton getJRadioButtonSettleline () {
		if (jRadioButtonRowHeaderSettleline == null) {
			jRadioButtonRowHeaderSettleline = new JRadioButton();
			jRadioButtonRowHeaderSettleline.setText("Settleline");
			jRadioButtonRowHeaderSettleline.addActionListener(this);
		}
		return jRadioButtonRowHeaderSettleline;
	}
	
	/**
	 * This method initializes jPanelButtons	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.insets = new Insets(0, 25, 0, 0);
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.insets = new Insets(0, 0, 0, 25);
			jPanelButtons = new JPanel();
			jPanelButtons.setLayout(new GridBagLayout());
			jPanelButtons.add(getJButtonApply(), gridBagConstraints1);
			jPanelButtons.add(getJButtonCancel(), gridBagConstraints3);
		}
		return jPanelButtons;
	}
	/**
	 * This method initializes jButtonApply	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonApply() {
		if (jButtonApply == null) {
			jButtonApply = new JButton();
			jButtonApply.setText("Apply");
			jButtonApply.setSize(buttonDimension);
			jButtonApply.setPreferredSize(buttonDimension);
			jButtonApply.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonApply.setForeground(new Color(0, 153, 0));
			jButtonApply.addActionListener(this);
		}
		return jButtonApply;
	}
	/**
	 * This method initializes jButtonCancel	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonCancel() {
		if (jButtonCancel == null) {
			jButtonCancel = new JButton();
			jButtonCancel.setText("Cancel");
			jButtonCancel.setSize(buttonDimension);
			jButtonCancel.setPreferredSize(buttonDimension);
			jButtonCancel.setFont(new Font("Dialog", Font.BOLD, 12));
			jButtonCancel.setForeground(new Color(153, 0, 0));
			jButtonCancel.addActionListener(this);
		}
		return jButtonCancel;
	}
	
	/* (non-Javadoc)
	 * @see gasmas.compStat.display.ParameterListener#subParameterChanged(gasmas.compStat.display.ParameterDisplay, java.lang.String, java.lang.Object)
	 */
	@Override
	public void subParameterChanged(ParameterDisplay display, String parameterDescription, Object value) {
		
		if (display==this.getValueTypeDisplaySpeed()) {
			this.getMeasurement().set(1, value);
		} else if (display==this.getValueTypeDisplayFlowRate()) {
			this.getMeasurement().set(2, value);
		} else if (display==this.getValueTypeDisplayAdiabaticHead()) {
			this.getMeasurement().set(3, value);
		}
		
	}
	@Override
	public void actionPerformed(ActionEvent ae) {
		
		Object actor = ae.getSource();
		if (actor==this.getJRadioButtonRowHeaderEff()) {
			if (this.getJTextFieldRowHeader().getText().equals(rowHeaderSettleline)) {
				this.getJTextFieldRowHeader().setText(null);
			}
			this.getJTextFieldRowHeader().setEditable(true);
			
		} else if (actor==this.getJRadioButtonSettleline()) {
			this.getJTextFieldRowHeader().setText(rowHeaderSettleline);
			this.getJTextFieldRowHeader().setEditable(false);
			
		} else if (actor==this.getJButtonCancel()) {
			this.setCanceled(true);
			this.setVisible(false);
			
		} else if (actor==this.getJButtonApply()) {
			// --- Error handling first -------------------
			String title = null;
			String msg = null;
			String rowHeader = this.getJTextFieldRowHeader().getText();

			if (rowHeader==null || rowHeader.equals("")) {
				title = "Missing value!";
				msg = "The value for the Efficiency has to be set.";
				this.getJTextFieldRowHeader().requestFocus();
				
			} else {
				if (this.getJRadioButtonRowHeaderEff().isSelected()) {
					// --- The  rowHeader should be a float now -----
					try {
						Float.parseFloat(rowHeader);
						
					} catch (Exception ex) {
						//ex.printStackTrace();
						title = "Wrong value type!";
						msg = "The value for the Efficiency has to be set as a Float number.";
					}

				} else if (this.getJRadioButtonSettleline().isSelected()) {
					// --- This is a settle line measurement --------
					this.getMeasurement().set(0, rowHeaderSettleline);
				}
			}
			
			if (msg!=null) {
				JOptionPane.showMessageDialog(this, msg, title, JOptionPane.ERROR_MESSAGE);
				return;
			}
			this.setCanceled(false);
			this.setVisible(false);
		}
		
	}

	
}  //  @jve:decl-index=0:visual-constraint="10,10"
