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

import gasmas.compStat.CompressorStationModel;
import gasmas.ontology.CompStatCompressor;
import gasmas.ontology.PistonCompressor;
import gasmas.ontology.TurboCompressor;
import gasmas.ontology.ValueType;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import agentgui.core.application.Language;

/**
 * The Class TurboCompressorDisplayData.
 */
public class CompressorGeneralDisplayData extends ParameterDisplay implements ParameterListener {

	private static final long serialVersionUID = -6812298324933666113L;
	
	public static final String changeDescription = "GeneralCompressorDescription";  //  @jve:decl-index=0:
	
	private CompressorStationModel compressorStationModel = null; // @jve:decl-index=0:
	private String compressorID = null;
	private CompStatCompressor myCompressor = null;  //  @jve:decl-index=0:
	
	private JPanel jPanelSpeedPanel = null;
	private JPanel jPanelGeneralInfo = null;
	
	private JLabel jLabelHeader = null;
	private JLabel jLabelID;
	private JLabel jLabelAlias = null;
	private JLabel jLabelDrive = null;
	
	private JTextField jTextFieldID;
	private JTextField jTextFieldAlias = null;
	private JComboBox jComboBoxDrive = null;
	
	private final Dimension speedDescriptionSize = new Dimension(65, 26);
	private ValueTypeDisplay valueTypeDisplaySpeedMin = null;
	private ValueTypeDisplay valueTypeDisplaySpeedMax = null;

	
	/**
	 * Instantiates a new compressor display data.
	 */
	public CompressorGeneralDisplayData() {
		this.initialize();
	}
	/**
	 * This is the default constructor.
	 *
	 * @param compressorStationEditorPanel the compressor station editor panel
	 * @param compressor the compressor
	 */
	public CompressorGeneralDisplayData(CompressorStationModel compressorStationModel, String compressorID) {
		this.compressorStationModel = compressorStationModel;
		this.compressorID = compressorID;
		this.initialize();
		this.setCompStatCompressor((CompStatCompressor) this.compressorStationModel.getComponent(this.compressorID));
	}
	
	/**
	 * Sets the information of the compressor (TurboCompressor or PistonCompressor).
	 * @param compressor the new compressor
	 */
	public void setCompStatCompressor(CompStatCompressor compressor) {
		this.myCompressor = compressor;
		this.getJTextFieldID().setText(compressor.getID());
		this.getJTextFieldAlias().setText(compressor.getAlias());
		this.getValueTypeDisplaySpeedMin().setValueType(compressor.getSpeedMin());
		this.getValueTypeDisplaySpeedMax().setValueType(compressor.getSpeedMax());
		this.getJComboBoxDrive().setSelectedItem(compressor.getDrive());
	}
	
	/**
	 * Gets the current compressor.
	 * @param compressor the compressor
	 * @return the compressor
	 */
	public CompStatCompressor getCompStatCompressor(CompStatCompressor compressor) {
		this.myCompressor.setID(this.getJTextFieldID().getText());
		this.myCompressor.setAlias(this.getJTextFieldAlias().getText());
		this.myCompressor.setSpeedMin(this.getValueTypeDisplaySpeedMin().getValueType());
		this.myCompressor.setSpeedMax(this.getValueTypeDisplaySpeedMax().getValueType());
		this.myCompressor.setDrive((String) this.getJComboBoxDrive().getSelectedItem());
		return this.myCompressor;
	}
	
	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		GridBagConstraints gridBagConstraintsHeader = new GridBagConstraints();
		gridBagConstraintsHeader.gridx = 0;
		gridBagConstraintsHeader.anchor = GridBagConstraints.WEST;
		gridBagConstraintsHeader.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraintsHeader.gridwidth = 2;
		gridBagConstraintsHeader.gridy = 0;
		
		GridBagConstraints gridBagConstraintsGeneral = new GridBagConstraints();
		gridBagConstraintsGeneral.anchor = GridBagConstraints.NORTH;
		gridBagConstraintsGeneral.insets = new Insets(5, 0, 0, 0);
		gridBagConstraintsGeneral.gridx = -1;
		gridBagConstraintsGeneral.gridy = 1;
		gridBagConstraintsGeneral.weightx = 0.4;
		gridBagConstraintsGeneral.gridheight = 2;
		gridBagConstraintsGeneral.fill = GridBagConstraints.BOTH;

		GridBagConstraints gridBagConstraintsSpeed = new GridBagConstraints();
		gridBagConstraintsSpeed.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraintsSpeed.insets = new Insets(5, 5, 0, 0);
		gridBagConstraintsSpeed.gridx = -1;
		gridBagConstraintsSpeed.gridy = 1;
		gridBagConstraintsSpeed.weightx = 0.6;
		gridBagConstraintsSpeed.fill = GridBagConstraints.HORIZONTAL;
		
		jLabelHeader = new JLabel();
		if (this.myCompressor instanceof TurboCompressor) {
			jLabelHeader.setText("Turbo Compressor");	
		} else if (this.myCompressor instanceof PistonCompressor) {
			jLabelHeader.setText("Piston Compressor");
		} else {
			jLabelHeader.setText("Compressor");
		}
		jLabelHeader.setText(Language.translate(jLabelHeader.getText(), Language.EN));
		jLabelHeader.setFont(new Font("Dialog", Font.BOLD, 12));
		
		this.setSize(500, 109);
		this.setLayout(new GridBagLayout());
		this.add(jLabelHeader, gridBagConstraintsHeader);
		this.add(getJPanelGeneralInfo(), gridBagConstraintsGeneral);
		this.add(getJPanelSpeedPanel(), gridBagConstraintsSpeed);
		
		
	}
	/**
	 * This method initializes jPanelGeneralInfo	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelGeneralInfo() {
		if (jPanelGeneralInfo == null) {
			
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.insets = new Insets(0, 0, 0, 0);
			
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.insets = new Insets(0, 5, 0, 0);
			
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.insets = new Insets(5, 0, 0, 0);
			
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = GridBagConstraints.BOTH;
			gridBagConstraints8.gridx = 1;
			gridBagConstraints8.gridy = 1;
			gridBagConstraints8.weightx = 0.0;
			gridBagConstraints8.insets = new Insets(5, 5, 0, 0);
			
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.anchor = GridBagConstraints.WEST;
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.gridy = 2;
			gridBagConstraints10.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints10.fill = GridBagConstraints.HORIZONTAL;
			
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.BOTH;
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.gridy = 2;
			gridBagConstraints11.weightx = 0.1;
			gridBagConstraints11.insets = new Insets(5, 5, 0, 0);
			
			jLabelID = new JLabel();
			jLabelID.setText("ID");
			jLabelAlias = new JLabel();
			jLabelAlias.setText("Alias");
			jLabelDrive = new JLabel();
			jLabelDrive.setText("Drive");

			jPanelGeneralInfo = new JPanel();
			jPanelGeneralInfo.setSize(new Dimension(80, 88));
			jPanelGeneralInfo.setBorder(BorderFactory.createEmptyBorder());
			jPanelGeneralInfo.setLayout(new GridBagLayout());
			jPanelGeneralInfo.add(jLabelID, gridBagConstraints2);
			jPanelGeneralInfo.add(getJTextFieldID(), gridBagConstraints3);
			jPanelGeneralInfo.add(jLabelAlias, gridBagConstraints5);
			jPanelGeneralInfo.add(getJTextFieldAlias(), gridBagConstraints8);
			jPanelGeneralInfo.add(jLabelDrive, gridBagConstraints10);
			jPanelGeneralInfo.add(getJComboBoxDrive(), gridBagConstraints11);
		}
		return jPanelGeneralInfo;
	}
	/**
	 * Gets the JTextField for the ID.
	 * @return the JTextField for the ID
	 */
	private JTextField getJTextFieldID() {
		if (jTextFieldID==null) {
			jTextFieldID = new JTextField();
			jTextFieldID.setSize(new Dimension(60, 26));
			jTextFieldID.setPreferredSize(new Dimension(60, 26));
			jTextFieldID.addKeyListener(getKeyAdapter4Changes());
		}
		return jTextFieldID;
	}
	/**
	 * This method initializes jTextFieldAlias	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldAlias() {
		if (jTextFieldAlias == null) {
			jTextFieldAlias = new JTextField();
			jTextFieldAlias.setSize(new Dimension(80, 26));
			jTextFieldAlias.setPreferredSize(new Dimension(80, 26));
			jTextFieldAlias.addKeyListener(getKeyAdapter4Changes());
		}
		return jTextFieldAlias;
	}
	/**
	 * This method initializes jComboBoxDrive	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJComboBoxDrive() {
		if (jComboBoxDrive == null) {
			jComboBoxDrive = new JComboBox();
			jComboBoxDrive.setSize(new Dimension(60, 26));
		}
		return jComboBoxDrive;
	}
	/**
	 * This method initializes jPanelSpeedPanel	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelSpeedPanel() {
		if (jPanelSpeedPanel == null) {
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints9.gridy = 1;
			gridBagConstraints9.weightx = 0.0;
			gridBagConstraints9.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints9.gridx = 1;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.gridy = 0;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.gridwidth = 1;
			gridBagConstraints7.insets = new Insets(0, 0, 5, 0);
			gridBagConstraints7.gridx = 1;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.anchor = GridBagConstraints.WEST;
			gridBagConstraints4.gridy = -1;
			gridBagConstraints4.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints4.gridx = -1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.anchor = GridBagConstraints.WEST;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints1.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints1.gridx = 0;
			
			jPanelSpeedPanel = new JPanel();
			jPanelSpeedPanel.setSize(new Dimension(100, 57));
			jPanelSpeedPanel.setPreferredSize(new Dimension(100, 57));
			jPanelSpeedPanel.setBorder(BorderFactory.createEmptyBorder());
			jPanelSpeedPanel.setLayout(new GridBagLayout());
			jPanelSpeedPanel.add(getValueTypeDisplaySpeedMin(), gridBagConstraints7);
			jPanelSpeedPanel.add(getValueTypeDisplaySpeedMax(), gridBagConstraints9);
		}
		return jPanelSpeedPanel;
	}
	/**
	 * This method initializes jTextFieldSpeedMin	
	 * @return javax.swing.JTextField	
	 */
	private ValueTypeDisplay getValueTypeDisplaySpeedMin() {
		if (valueTypeDisplaySpeedMin == null) {
			valueTypeDisplaySpeedMin = new ValueTypeDisplay(Language.translate("Speed Min", Language.EN), this.speedDescriptionSize);
			valueTypeDisplaySpeedMin.setSize(new Dimension(100, 26));
			valueTypeDisplaySpeedMin.addParameterListener(this);
		}
		return valueTypeDisplaySpeedMin;
	}
	/**
	 * This method initializes jTextFieldSpeedMax	
	 * @return javax.swing.JTextField	
	 */
	private ValueTypeDisplay getValueTypeDisplaySpeedMax() {
		if (valueTypeDisplaySpeedMax == null) {
			valueTypeDisplaySpeedMax = new ValueTypeDisplay(Language.translate("Speed Max", Language.EN), this.speedDescriptionSize);
			valueTypeDisplaySpeedMax.setSize(new Dimension(100, 26));
			valueTypeDisplaySpeedMax.addParameterListener(this);
		}
		return valueTypeDisplaySpeedMax;
	}
	
	/* (non-Javadoc)
	 * @see gasmas.compStat.display.ParameterListener#parameterChanged(gasmas.compStat.display.ParameterDisplay, java.lang.String, java.lang.Object)
	 */
	@Override
	public void subParameterChanged(ParameterDisplay display, String parameterDescription, Object value) {
		
		if (display==this.getValueTypeDisplaySpeedMin()) {
			ValueType valueType = (ValueType) value;
			this.myCompressor.setSpeedMin(valueType);
			
		} else if (display==this.getValueTypeDisplaySpeedMax()) {
			ValueType valueType = (ValueType) value;
			this.myCompressor.setSpeedMax(valueType);
		}
		this.informListener(changeDescription, this.myCompressor);
	}
	
	/* (non-Javadoc)
	 * @see gasmas.compStat.display.ParameterDisplay#valueChangedInJTextField(javax.swing.JTextField)
	 */
	@Override
	public void valueChangedInJTextField(JTextField source) {
		
		if (source == this.getJTextFieldID()) {
			this.myCompressor.setID(this.getJTextFieldID().getText());
			
		} else if (source == this.getJTextFieldAlias()) {
			this.myCompressor.setAlias(this.getJTextFieldAlias().getText());
		}
		this.informListener(changeDescription, this.myCompressor);
	}
	
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
