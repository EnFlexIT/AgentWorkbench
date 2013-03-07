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
import gasmas.ontology.Calc9Parameter;
import gasmas.ontology.CompStatCompressor;
import gasmas.ontology.TurboCompressor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;


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
		
		this.getAdiabaticEffCoeff().setParameter("1", this.myTurboCompressor.getEta_ad_isoline_coeff().getCoeff_1_9());
		this.getAdiabaticEffCoeff().setParameter("2", this.myTurboCompressor.getEta_ad_isoline_coeff().getCoeff_2_9());
		this.getAdiabaticEffCoeff().setParameter("3", this.myTurboCompressor.getEta_ad_isoline_coeff().getCoeff_3_9());
		this.getAdiabaticEffCoeff().setParameter("4", this.myTurboCompressor.getEta_ad_isoline_coeff().getCoeff_4_9());
		this.getAdiabaticEffCoeff().setParameter("5", this.myTurboCompressor.getEta_ad_isoline_coeff().getCoeff_5_9());
		this.getAdiabaticEffCoeff().setParameter("6", this.myTurboCompressor.getEta_ad_isoline_coeff().getCoeff_6_9());
		this.getAdiabaticEffCoeff().setParameter("7", this.myTurboCompressor.getEta_ad_isoline_coeff().getCoeff_7_9());
		this.getAdiabaticEffCoeff().setParameter("8", this.myTurboCompressor.getEta_ad_isoline_coeff().getCoeff_8_9());
		this.getAdiabaticEffCoeff().setParameter("9", this.myTurboCompressor.getEta_ad_isoline_coeff().getCoeff_9_9());
		
		this.getNisoLineCoeff().setParameter("1", this.myTurboCompressor.getN_isoline_coeff().getCoeff_1_9());
		this.getNisoLineCoeff().setParameter("2", this.myTurboCompressor.getN_isoline_coeff().getCoeff_2_9());
		this.getNisoLineCoeff().setParameter("3", this.myTurboCompressor.getN_isoline_coeff().getCoeff_3_9());
		this.getNisoLineCoeff().setParameter("4", this.myTurboCompressor.getN_isoline_coeff().getCoeff_4_9());
		this.getNisoLineCoeff().setParameter("5", this.myTurboCompressor.getN_isoline_coeff().getCoeff_5_9());
		this.getNisoLineCoeff().setParameter("6", this.myTurboCompressor.getN_isoline_coeff().getCoeff_6_9());
		this.getNisoLineCoeff().setParameter("7", this.myTurboCompressor.getN_isoline_coeff().getCoeff_7_9());
		this.getNisoLineCoeff().setParameter("8", this.myTurboCompressor.getN_isoline_coeff().getCoeff_8_9());
		this.getNisoLineCoeff().setParameter("9", this.myTurboCompressor.getN_isoline_coeff().getCoeff_9_9());
		
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
		
		GridBagConstraints gridBagConstraintsGeneral = new GridBagConstraints();
		gridBagConstraintsGeneral.gridx = 0;
		gridBagConstraintsGeneral.gridy = 0;
		gridBagConstraintsGeneral.anchor = GridBagConstraints.WEST;
		gridBagConstraintsGeneral.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraintsGeneral.gridwidth = 2;
		gridBagConstraintsGeneral.weightx = 1.0;
		gridBagConstraintsGeneral.insets = new Insets(5, 5, 0, 5);
		
		GridBagConstraints gridBagConstraintsNisoLineCoeff = new GridBagConstraints();
		gridBagConstraintsNisoLineCoeff.gridx = 0;
		gridBagConstraintsNisoLineCoeff.gridy = 1;
		gridBagConstraintsNisoLineCoeff.anchor = GridBagConstraints.WEST;
		gridBagConstraintsNisoLineCoeff.insets = new Insets(5, 5, 0, 0);

		GridBagConstraints gridBagConstraintsAdiabaticEffCoeff = new GridBagConstraints();
		gridBagConstraintsAdiabaticEffCoeff.gridx = 0;
		gridBagConstraintsAdiabaticEffCoeff.gridy = 2;
		gridBagConstraintsAdiabaticEffCoeff.anchor = GridBagConstraints.WEST;
		gridBagConstraintsAdiabaticEffCoeff.insets = new Insets(5, 5, 0, 0);
		
		this.setSize(500, 540);
		this.setLayout(new GridBagLayout());
		this.add(getGeneralCompressorInfo(), gridBagConstraintsGeneral);
		this.add(getNisoLineCoeff(), gridBagConstraintsNisoLineCoeff);
		this.add(getAdiabaticEffCoeff(), gridBagConstraintsAdiabaticEffCoeff);
		
	}
	
	/**
	 * This method initializes jPanelTop	
	 * @return javax.swing.JPanel	
	 */
	private CompressorGeneralDisplayData getGeneralCompressorInfo() {
		if (generalCompressorInfo == null) {
			generalCompressorInfo = new CompressorGeneralDisplayData(this.compressorStationModel, this.turboCompressorID);
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
		}
		
		// --- Inform about the changes ---------------------------------------
		this.informListener(changeDescription, this.myTurboCompressor);
		
	}
	@Override
	public Object getParameter(String parameterDescription) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setParameter(String parameterDescription, Object value) {
		// TODO Auto-generated method stub
		
	}
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
