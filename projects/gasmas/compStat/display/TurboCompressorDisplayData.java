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
import gasmas.ontology.Calc3Parameter;
import gasmas.ontology.Calc9Parameter;
import gasmas.ontology.CompStatCompressor;
import gasmas.ontology.TurboCompressor;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;


/**
 * The Class TurboCompressorDisplayData.
 */
public class TurboCompressorDisplayData extends ParameterDisplay implements ParameterListener {

	private static final long serialVersionUID = -8078405308134894229L;

	public static final String changeDescription = "TurboCompressorDescription";  //  @jve:decl-index=0:

	private CompressorStationModel compressorStationModel = null; // @jve:decl-index=0:
	private String turboCompressorID = null;
	private TurboCompressor myTurboCompressor = null;  //  @jve:decl-index=0:
	
	private CompressorGeneralDisplayData generalCompressorInfo = null;
	private ParameterArray9Display nIsolineCoeff;
	private ParameterArray9Display adiabaticEffCoeff;
	
	private JPanel jPanelChokeLine = null;
	private JLabel jLabelChokeLine = null;
	private ParameterArray3Display chokeLine = null;

	private JPanel jPanelSurgeLine = null;
	private JLabel jLabelSurgeLine = null;
	private ParameterArray3Display surgeLine = null;

	private JLabel jLabelChokeLineEfficiency = null;
	private JTextField jTextFieldChokeLineEfficiency = null;

	private JLabel jLabelDummy = null;
	
	/**
	 * This is the default constructor
	 */
	public TurboCompressorDisplayData(CompressorStationModel compressorStationModel, String turboCompressorID) {
		this.compressorStationModel = compressorStationModel;	
		this.turboCompressorID = turboCompressorID;
		this.initialize();
		this.setTurboCompressor((TurboCompressor) this.compressorStationModel.getComponent(this.turboCompressorID));
	}
	/**
	 * Sets the turbo compressor.
	 * @param turboCompressor the new turbo compressor
	 */
	public void setTurboCompressor(TurboCompressor turboCompressor) {
		this.myTurboCompressor = turboCompressor;
		this.turboCompressorID = this.myTurboCompressor.getID();
		this.getGeneralCompressorInfo().setCompStatCompressor(this.myTurboCompressor);
		
		if (this.myTurboCompressor.getEta_ad_isoline_coeff()==null) this.myTurboCompressor.setEta_ad_isoline_coeff(new Calc9Parameter());
		this.getAdiabaticEffCoeff().setParameter("1", this.myTurboCompressor.getEta_ad_isoline_coeff().getCoeff_1_9());
		this.getAdiabaticEffCoeff().setParameter("2", this.myTurboCompressor.getEta_ad_isoline_coeff().getCoeff_2_9());
		this.getAdiabaticEffCoeff().setParameter("3", this.myTurboCompressor.getEta_ad_isoline_coeff().getCoeff_3_9());
		this.getAdiabaticEffCoeff().setParameter("4", this.myTurboCompressor.getEta_ad_isoline_coeff().getCoeff_4_9());
		this.getAdiabaticEffCoeff().setParameter("5", this.myTurboCompressor.getEta_ad_isoline_coeff().getCoeff_5_9());
		this.getAdiabaticEffCoeff().setParameter("6", this.myTurboCompressor.getEta_ad_isoline_coeff().getCoeff_6_9());
		this.getAdiabaticEffCoeff().setParameter("7", this.myTurboCompressor.getEta_ad_isoline_coeff().getCoeff_7_9());
		this.getAdiabaticEffCoeff().setParameter("8", this.myTurboCompressor.getEta_ad_isoline_coeff().getCoeff_8_9());
		this.getAdiabaticEffCoeff().setParameter("9", this.myTurboCompressor.getEta_ad_isoline_coeff().getCoeff_9_9());
		
		if (this.myTurboCompressor.getN_isoline_coeff()==null) this.myTurboCompressor.setN_isoline_coeff(new Calc9Parameter());
		this.getNisoLineCoeff().setParameter("1", this.myTurboCompressor.getN_isoline_coeff().getCoeff_1_9());
		this.getNisoLineCoeff().setParameter("2", this.myTurboCompressor.getN_isoline_coeff().getCoeff_2_9());
		this.getNisoLineCoeff().setParameter("3", this.myTurboCompressor.getN_isoline_coeff().getCoeff_3_9());
		this.getNisoLineCoeff().setParameter("4", this.myTurboCompressor.getN_isoline_coeff().getCoeff_4_9());
		this.getNisoLineCoeff().setParameter("5", this.myTurboCompressor.getN_isoline_coeff().getCoeff_5_9());
		this.getNisoLineCoeff().setParameter("6", this.myTurboCompressor.getN_isoline_coeff().getCoeff_6_9());
		this.getNisoLineCoeff().setParameter("7", this.myTurboCompressor.getN_isoline_coeff().getCoeff_7_9());
		this.getNisoLineCoeff().setParameter("8", this.myTurboCompressor.getN_isoline_coeff().getCoeff_8_9());
		this.getNisoLineCoeff().setParameter("9", this.myTurboCompressor.getN_isoline_coeff().getCoeff_9_9());
		
		if (this.myTurboCompressor.getChokeline_coeff()==null) this.myTurboCompressor.setChokeline_coeff(new Calc3Parameter());
		this.getChokeLineDisplay().setParameter("1", this.myTurboCompressor.getChokeline_coeff().getCoeff_1_3());
		this.getChokeLineDisplay().setParameter("2", this.myTurboCompressor.getChokeline_coeff().getCoeff_2_3());
		this.getChokeLineDisplay().setParameter("3", this.myTurboCompressor.getChokeline_coeff().getCoeff_3_3());
		
		if (this.myTurboCompressor.getSurgeline_coeff()==null) this.myTurboCompressor.setSurgeline_coeff(new Calc3Parameter());
		this.getSurgeLineDisplay().setParameter("1", this.myTurboCompressor.getSurgeline_coeff().getCoeff_1_3());
		this.getSurgeLineDisplay().setParameter("2", this.myTurboCompressor.getSurgeline_coeff().getCoeff_2_3());
		this.getSurgeLineDisplay().setParameter("3", this.myTurboCompressor.getSurgeline_coeff().getCoeff_3_3());

		this.getJTextFieldChokeLineEfficiency().setText(((Float)this.myTurboCompressor.getEfficiencyOfChokeline()).toString());
		
	}
	/**
	 * Gets the turbo compressor.
	 * @return the turbo compressor
	 */
	public TurboCompressor getTurboCompressor() {
		return myTurboCompressor;
	}

	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.gridy = 5;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 0;
		gridBagConstraints3.insets = new Insets(5, 20, 10, 0);
		gridBagConstraints3.anchor = GridBagConstraints.EAST;
		gridBagConstraints3.gridy = 3;
		GridBagConstraints gridBagConstraintsChokeLineEff = new GridBagConstraints();
		gridBagConstraintsChokeLineEff.fill = GridBagConstraints.VERTICAL;
		gridBagConstraintsChokeLineEff.gridy = 3;
		gridBagConstraintsChokeLineEff.weightx = 1.0;
		gridBagConstraintsChokeLineEff.insets = new Insets(5, 36, 10, 5);
		gridBagConstraintsChokeLineEff.anchor = GridBagConstraints.WEST;
		gridBagConstraintsChokeLineEff.gridx = 1;
		GridBagConstraints gridBagConstraintsSurgeLine = new GridBagConstraints();
		gridBagConstraintsSurgeLine.gridx = 1;
		gridBagConstraintsSurgeLine.insets = new Insets(4, 20, 0, 5);
		gridBagConstraintsSurgeLine.fill = GridBagConstraints.NONE;
		gridBagConstraintsSurgeLine.anchor = GridBagConstraints.WEST;
		gridBagConstraintsSurgeLine.gridy = 4;
		GridBagConstraints gridBagConstraintsChokeLine = new GridBagConstraints();
		gridBagConstraintsChokeLine.gridx = 1;
		gridBagConstraintsChokeLine.fill = GridBagConstraints.NONE;
		gridBagConstraintsChokeLine.insets = new Insets(4, 20, 0, 5);
		gridBagConstraintsChokeLine.anchor = GridBagConstraints.WEST;
		gridBagConstraintsChokeLine.gridy = 1;
		GridBagConstraints gridBagConstraintsGeneral = new GridBagConstraints();
		gridBagConstraintsGeneral.gridx = 0;
		gridBagConstraintsGeneral.gridy = 0;
		gridBagConstraintsGeneral.anchor = GridBagConstraints.WEST;
		gridBagConstraintsGeneral.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraintsGeneral.gridwidth = 3;
		gridBagConstraintsGeneral.weightx = 1.0;
		gridBagConstraintsGeneral.insets = new Insets(5, 5, 0, 5);
		GridBagConstraints gridBagConstraintsNisoLineCoeff = new GridBagConstraints();
		gridBagConstraintsNisoLineCoeff.gridx = 0;
		gridBagConstraintsNisoLineCoeff.gridy = 1;
		gridBagConstraintsNisoLineCoeff.anchor = GridBagConstraints.WEST;
		gridBagConstraintsNisoLineCoeff.insets = new Insets(5, 5, 0, 0);
		GridBagConstraints gridBagConstraintsAdiabaticEffCoeff = new GridBagConstraints();
		gridBagConstraintsAdiabaticEffCoeff.gridx = 0;
		gridBagConstraintsAdiabaticEffCoeff.gridy = 4;
		gridBagConstraintsAdiabaticEffCoeff.anchor = GridBagConstraints.WEST;
		gridBagConstraintsAdiabaticEffCoeff.insets = new Insets(5, 5, 0, 0);
		
		jLabelChokeLineEfficiency = new JLabel();
		jLabelChokeLineEfficiency.setText("Efficiency of Choke line");
		jLabelChokeLineEfficiency.setFont(new Font("Dialog", Font.BOLD, 12));
		
		jLabelDummy = new JLabel();
		jLabelDummy.setText("");
		
		this.setSize(540, 500);
		this.setLayout(new GridBagLayout());
		this.add(getGeneralCompressorInfo(), gridBagConstraintsGeneral);
		this.add(getNisoLineCoeff(), gridBagConstraintsNisoLineCoeff);
		this.add(getAdiabaticEffCoeff(), gridBagConstraintsAdiabaticEffCoeff);
		this.add(getJPanelChokeLine(), gridBagConstraintsChokeLine);
		this.add(getJPanelSurgeLine(), gridBagConstraintsSurgeLine);
		this.add(jLabelChokeLineEfficiency, gridBagConstraints3);
		this.add(getJTextFieldChokeLineEfficiency(), gridBagConstraintsChokeLineEff);
		this.add(jLabelDummy, gridBagConstraints);
		
	}
	
	/**
	 * This method initializes jPanelTop	
	 * @return javax.swing.JPanel	
	 */
	private CompressorGeneralDisplayData getGeneralCompressorInfo() {
		if (generalCompressorInfo == null) {
			generalCompressorInfo = new CompressorGeneralDisplayData(compressorStationModel, turboCompressorID);
			generalCompressorInfo.addParameterListener(this);
		}
		return generalCompressorInfo;
	}
	
	/**
	 * Gets the n isoline coeff.
	 * @return the n isoline coeff
	 */
	private ParameterArray9Display getNisoLineCoeff() {
		if (nIsolineCoeff==null) {
			nIsolineCoeff = new ParameterArray9Display("Coefficients for the isolines of speed");
			nIsolineCoeff.addParameterListener(this);
		}
		return nIsolineCoeff;
	}
	/**
	 * Gets the adiabatic eff coeff.
	 * @return the adiabatic eff coeff
	 */
	private ParameterArray9Display getAdiabaticEffCoeff() {
		if (adiabaticEffCoeff==null) {
			adiabaticEffCoeff = new ParameterArray9Display("Coefficients for the isolines of the adiabatic efficiency");
			adiabaticEffCoeff.addParameterListener(this);
		}
		return adiabaticEffCoeff;
	}
	
	/**
	 * This method initializes jPanelChokeLine	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelChokeLine() {
		if (jPanelChokeLine == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.insets = new Insets(0, 0, 1, 0);
			gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.anchor = GridBagConstraints.NORTHWEST;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.weightx = 0.0;
			
			jLabelChokeLine = new JLabel();
			jLabelChokeLine.setText("Choke line");
			jLabelChokeLine.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jPanelChokeLine = new JPanel();
			jPanelChokeLine.setLayout(new GridBagLayout());
			jPanelChokeLine.add(jLabelChokeLine, gridBagConstraints2);
			jPanelChokeLine.setBorder(BorderFactory.createEmptyBorder());
			jPanelChokeLine.add(getChokeLineDisplay(), gridBagConstraints1);
		}
		return jPanelChokeLine;
	}
	/**
	 * Gets the choke line display.
	 * @return the choke line display
	 */
	private ParameterArray3Display getChokeLineDisplay() {
		if (chokeLine == null) {
			chokeLine = new ParameterArray3Display(1);
			chokeLine.setSize(chokeLine.getIdealSize());
			chokeLine.addParameterListener(this);
		}
		return chokeLine;
	}
	/**
	 * This method initializes jTextFieldChokeLineEfficiency	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldChokeLineEfficiency() {
		if (jTextFieldChokeLineEfficiency == null) {
			jTextFieldChokeLineEfficiency = new JTextField();
			jTextFieldChokeLineEfficiency.setSize(new Dimension(100, 26));
			jTextFieldChokeLineEfficiency.setPreferredSize(new Dimension(100, 26));
			jTextFieldChokeLineEfficiency.addKeyListener(this.getKeyAdapter4FloatNumbers());
			jTextFieldChokeLineEfficiency.addKeyListener(this.getKeyAdapter4Changes());
		}
		return jTextFieldChokeLineEfficiency;
	}
	
	/**
	 * This method initializes jPanelSurgeLine	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelSurgeLine() {
		if (jPanelSurgeLine == null) {
			
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.insets = new Insets(0, 0, 1, 0);
			gridBagConstraints2.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.anchor = GridBagConstraints.NORTHWEST;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = GridBagConstraints.BOTH;
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.weightx = 0.0;

			jLabelSurgeLine = new JLabel();
			jLabelSurgeLine.setText("Surge line");
			jLabelSurgeLine.setFont(new Font("Dialog", Font.BOLD, 12));
			
			jPanelSurgeLine = new JPanel();
			jPanelSurgeLine.setLayout(new GridBagLayout());
			jPanelSurgeLine.add(jLabelSurgeLine, gridBagConstraints2);
			jPanelSurgeLine.setBorder(BorderFactory.createEmptyBorder());
			jPanelSurgeLine.add(getSurgeLineDisplay(), gridBagConstraints1);
		}
		return jPanelSurgeLine;
	}
	/**
	 * Gets the surge line display.
	 * @return the surge line display
	 */
	private ParameterArray3Display getSurgeLineDisplay() {
		if (surgeLine == null) {
			surgeLine = new ParameterArray3Display(1);
			surgeLine.setSize(surgeLine.getIdealSize());
			surgeLine.addParameterListener(this);
		}
		return surgeLine;
	}
	
	/* (non-Javadoc)
	 * @see gasmas.compStat.display.ParameterListener#parameterChanged(gasmas.compStat.display.ParameterDisplay, java.lang.String, java.lang.Object)
	 */
	@Override
	public void subParameterChanged(ParameterDisplay display, String parameterDescription, Object value) {

		if (display==this.getGeneralCompressorInfo()) {
			// --- Changes in the general description -------------------------
			CompStatCompressor generalCompressorInfo = (CompStatCompressor) value; 
			this.myTurboCompressor.setID(generalCompressorInfo.getID());
			this.myTurboCompressor.setAlias(generalCompressorInfo.getAlias());
			this.myTurboCompressor.setDrive(generalCompressorInfo.getDrive());
			this.myTurboCompressor.setSpeedMin(generalCompressorInfo.getSpeedMin());
			this.myTurboCompressor.setSpeedMax(generalCompressorInfo.getSpeedMax());
			
		} else if (display==this.getNisoLineCoeff()) {
			// --- Changes in the rounds per minutes settings -----------------
			Calc9Parameter calc9 = this.myTurboCompressor.getN_isoline_coeff();
			int parNo = Integer.parseInt(parameterDescription);
			Float floatValue = (Float) value;
			switch (parNo) {
			case 1:
				calc9.setCoeff_1_9(floatValue);
				break;
			case 2:
				calc9.setCoeff_2_9(floatValue);
				break;
			case 3:
				calc9.setCoeff_3_9(floatValue);
				break;
			case 4:
				calc9.setCoeff_4_9(floatValue);
				break;
			case 5:
				calc9.setCoeff_5_9(floatValue);
				break;
			case 6:
				calc9.setCoeff_6_9(floatValue);
				break;
			case 7:
				calc9.setCoeff_7_9(floatValue);
				break;
			case 8:
				calc9.setCoeff_8_9(floatValue);
				break;
			case 9:
				calc9.setCoeff_9_9(floatValue);
				break;
			}
			this.myTurboCompressor.setN_isoline_coeff(calc9);
			
		} else if (display==this.getAdiabaticEffCoeff()) {
			// --- Changes in the adiabatic efficiency coefficients -----------   
			Calc9Parameter calc9 = this.myTurboCompressor.getEta_ad_isoline_coeff();
			int parNo = Integer.parseInt(parameterDescription);
			Float floatValue = (Float) value;
			switch (parNo) {
			case 1:
				calc9.setCoeff_1_9(floatValue);
				break;
			case 2:
				calc9.setCoeff_2_9(floatValue);
				break;
			case 3:
				calc9.setCoeff_3_9(floatValue);
				break;
			case 4:
				calc9.setCoeff_4_9(floatValue);
				break;
			case 5:
				calc9.setCoeff_5_9(floatValue);
				break;
			case 6:
				calc9.setCoeff_6_9(floatValue);
				break;
			case 7:
				calc9.setCoeff_7_9(floatValue);
				break;
			case 8:
				calc9.setCoeff_8_9(floatValue);
				break;
			case 9:
				calc9.setCoeff_9_9(floatValue);
				break;
			}
			this.myTurboCompressor.setEta_ad_isoline_coeff(calc9);
			
		} else if (display == this.getChokeLineDisplay()) {
			// --- Changes in the chokeline coefficients ----------------------
			Calc3Parameter calc3 = this.myTurboCompressor.getChokeline_coeff();
			int parNo = Integer.parseInt(parameterDescription);
			Float floatValue = (Float) value;
			switch (parNo) {
			case 1:
				calc3.setCoeff_1_3(floatValue);
				break;
			case 2:
				calc3.setCoeff_2_3(floatValue);
				break;
			case 3:
				calc3.setCoeff_3_3(floatValue);
				break;
			}
			this.myTurboCompressor.setChokeline_coeff(calc3);
			
		} else if (display == this.getSurgeLineDisplay()) {
			// --- Changes in the surgeline coefficients ----------------------
			Calc3Parameter calc3 = this.myTurboCompressor.getSurgeline_coeff();
			int parNo = Integer.parseInt(parameterDescription);
			Float floatValue = (Float) value;
			switch (parNo) {
			case 1:
				calc3.setCoeff_1_3(floatValue);
				break;
			case 2:
				calc3.setCoeff_2_3(floatValue);
				break;
			case 3:
				calc3.setCoeff_3_3(floatValue);
				break;
			}
			this.myTurboCompressor.setSurgeline_coeff(calc3);
			
		}
		// --- Inform about the changes ---------------------------------------
		this.informListener(changeDescription, this.myTurboCompressor);
		
	}

	/* (non-Javadoc)
	 * @see gasmas.compStat.display.ParameterDisplay#valueChangedInJTextField(javax.swing.JTextField)
	 */
	@Override
	protected void valueChangedInJTextField(JTextField sourceTextField) {
		
		if (sourceTextField==this.getJTextFieldChokeLineEfficiency()) {
			Float floatValue = this.parseFloat(this.getJTextFieldChokeLineEfficiency().getText());
			this.myTurboCompressor.setEfficiencyOfChokeline(floatValue);
			
		}
		// --- Inform about the changes ---------------------------------------
		this.informListener(changeDescription, this.myTurboCompressor);
		
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
