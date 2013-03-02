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

import gasmas.ontology.CompStatCompressor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * The Class TurboCompressorDisplayData.
 */
public class CompressorDisplayData extends JPanel {

	private static final long serialVersionUID = -6812298324933666113L;
	
	private CompStatCompressor myCompressor = null;  //  @jve:decl-index=0:
	
	private JPanel jPanelSpeedPanel = null;
	private JPanel jPanelGeneralInfo = null;
	private JLabel jLabelID;
	private JLabel jLabelAlias = null;
	private JLabel jLabelDrive = null;
	
	private JTextField jTextFieldID;
	private JTextField jTextFieldAlias = null;

	private JLabel jLabelSpeedMin = null;
	private JLabel jLabelSpeedMax = null;

	private ValueTypeDisplay valueTypeDisplaySpeedMin = null;
	private ValueTypeDisplay valueTypeDisplaySpeedMax = null;

	private JComboBox jComboBoxDrive = null;
	private JButton jButtonDriveNew = null;
	
	
	/**
	 * This is the default constructor
	 */
	public CompressorDisplayData() {
		super();
		this.initialize();
	}
	
	/**
	 * Sets the information of the compressor (TurboCompressor or PistonCompressor).
	 * @param compressor the new compressor
	 */
	public void setCompStatCompressor(CompStatCompressor compressor) {
		this.myCompressor = compressor;
		this.getJTextFieldID().setText(myCompressor.getID());
		this.getJTextFieldAlias().setText(myCompressor.getAlias());
		this.getJTextFieldSpeedMin().setValueType(myCompressor.getSpeedMin());
		this.getJTextFieldSpeedMax().setValueType(myCompressor.getSpeedMax());
		this.getJComboBoxDrive().setSelectedItem(myCompressor.getDrive());
	}
	
	/**
	 * Gets the current compressor.
	 * @param compressor the compressor
	 * @return the compressor
	 */
	public CompStatCompressor getCompStatCompressor(CompStatCompressor compressor) {
		myCompressor.setID(this.getJTextFieldID().getText());
		myCompressor.setAlias(this.getJTextFieldAlias().getText());
		myCompressor.setSpeedMin(this.getJTextFieldSpeedMin().getValueType());
		myCompressor.setSpeedMax(this.getJTextFieldSpeedMax().getValueType());
		myCompressor.setDrive((String) this.getJComboBoxDrive().getSelectedItem());
		return this.myCompressor;
	}
	
	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		GridBagConstraints gridBagConstraints23 = new GridBagConstraints();
		gridBagConstraints23.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints23.insets = new Insets(5, 5, 0, 5);
		gridBagConstraints23.gridx = -1;
		gridBagConstraints23.gridy = -1;
		gridBagConstraints23.weightx = 1.0;
		gridBagConstraints23.fill = GridBagConstraints.HORIZONTAL;
		GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
		gridBagConstraints22.anchor = GridBagConstraints.NORTHWEST;
		gridBagConstraints22.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints22.gridx = -1;
		gridBagConstraints22.gridy = -1;
		gridBagConstraints22.weightx = 0.5;
		gridBagConstraints22.fill = GridBagConstraints.BOTH;
		
		this.setSize(515, 126);
		this.setLayout(new GridBagLayout());
		this.add(getJPanelGeneralInfo(), gridBagConstraints22);
		this.add(getJPanelSpeedPanel(), gridBagConstraints23);
	}
	/**
	 * This method initializes jPanelGeneralInfo	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelGeneralInfo() {
		if (jPanelGeneralInfo == null) {
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 2;
			gridBagConstraints12.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints12.gridy = 2;
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.fill = GridBagConstraints.BOTH;
			gridBagConstraints11.gridy = 2;
			gridBagConstraints11.weightx = 0.1;
			gridBagConstraints11.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints11.gridx = 1;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.anchor = GridBagConstraints.WEST;
			gridBagConstraints10.gridy = 2;
			gridBagConstraints10.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints10.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints10.gridx = 0;
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = GridBagConstraints.BOTH;
			gridBagConstraints8.gridy = 1;
			gridBagConstraints8.weightx = 0.0;
			gridBagConstraints8.gridwidth = 2;
			gridBagConstraints8.insets = new Insets(5, 5, 0, 0);
			gridBagConstraints8.gridx = 1;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.anchor = GridBagConstraints.WEST;
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridy = 1;
			gridBagConstraints5.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints5.insets = new Insets(5, 0, 0, 0);
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			gridBagConstraints3.gridx = 1;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.gridwidth = 2;
			gridBagConstraints3.insets = new Insets(0, 5, 0, 0);
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.anchor = GridBagConstraints.WEST;
			gridBagConstraints2.gridx = -1;
			gridBagConstraints2.gridy = -1;
			gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.insets = new Insets(0, 0, 0, 0);
			
			jLabelID = new JLabel();
			jLabelID.setText("ID");
			jLabelAlias = new JLabel();
			jLabelAlias.setText("Alias");
			jLabelDrive = new JLabel();
			jLabelDrive.setText("Drive");

			jPanelGeneralInfo = new JPanel();
			jPanelGeneralInfo.setSize(new Dimension(100, 88));
			jPanelGeneralInfo.setPreferredSize(new Dimension(100, 88));
			jPanelGeneralInfo.setBorder(BorderFactory.createEmptyBorder());
			jPanelGeneralInfo.setLayout(new GridBagLayout());
			jPanelGeneralInfo.add(jLabelID, gridBagConstraints2);
			jPanelGeneralInfo.add(getJTextFieldID(), gridBagConstraints3);
			jPanelGeneralInfo.add(jLabelAlias, gridBagConstraints5);
			jPanelGeneralInfo.add(getJTextFieldAlias(), gridBagConstraints8);
			jPanelGeneralInfo.add(jLabelDrive, gridBagConstraints10);
			jPanelGeneralInfo.add(getJComboBoxDrive(), gridBagConstraints11);
			jPanelGeneralInfo.add(getJButtonDriveNew(), gridBagConstraints12);
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
			jTextFieldID.setSize(new Dimension(80, 26));
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
			jTextFieldAlias.setSize(new Dimension(100, 26));
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
			jComboBoxDrive.setSize(new Dimension(100, 26));
		}
		return jComboBoxDrive;
	}
	/**
	 * This method initializes jButtonDriveNew	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonDriveNew() {
		if (jButtonDriveNew == null) {
			jButtonDriveNew = new JButton();
			jButtonDriveNew.setSize(new Dimension(26, 26));
			jButtonDriveNew.setText(" + ");
		}
		return jButtonDriveNew;
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
			gridBagConstraints9.insets = new Insets(5, 0, 0, 0);
			gridBagConstraints9.gridx = 1;
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.gridy = 0;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.gridwidth = 1;
			gridBagConstraints7.insets = new Insets(0, 0, 0, 0);
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
			jPanelSpeedPanel.setBorder(BorderFactory.createEmptyBorder());
			jPanelSpeedPanel.setLayout(new GridBagLayout());
			jPanelSpeedPanel.add(getJTextFieldSpeedMin(), gridBagConstraints7);
			jPanelSpeedPanel.add(getJTextFieldSpeedMax(), gridBagConstraints9);
			jPanelSpeedPanel.add(jLabelSpeedMax, gridBagConstraints1);
			jPanelSpeedPanel.add(jLabelSpeedMin, gridBagConstraints4);
		}
		return jPanelSpeedPanel;
	}
	/**
	 * This method initializes jTextFieldSpeedMin	
	 * @return javax.swing.JTextField	
	 */
	private ValueTypeDisplay getJTextFieldSpeedMin() {
		if (valueTypeDisplaySpeedMin == null) {
			valueTypeDisplaySpeedMin = new ValueTypeDisplay();
			valueTypeDisplaySpeedMin.setSize(new Dimension(100, 36));
		}
		return valueTypeDisplaySpeedMin;
	}
	/**
	 * This method initializes jTextFieldSpeedMax	
	 * @return javax.swing.JTextField	
	 */
	private ValueTypeDisplay getJTextFieldSpeedMax() {
		if (valueTypeDisplaySpeedMax == null) {
			valueTypeDisplaySpeedMax = new ValueTypeDisplay();
			valueTypeDisplaySpeedMax.setSize(new Dimension(100, 36));
		}
		return valueTypeDisplaySpeedMax;
	}
	
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
