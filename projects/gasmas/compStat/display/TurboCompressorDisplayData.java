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

import gasmas.compStat.CompressorStationEditorPanel;
import gasmas.compStat.CompressorStationModel;
import gasmas.ontology.TurboCompressor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;


/**
 * The Class TurboCompressorDisplayData.
 */
public class TurboCompressorDisplayData extends JPanel implements ParameterListener {

	private static final long serialVersionUID = 1L;

	private CompressorStationEditorPanel compressorStationEditorPanel = null; 
	private CompressorStationModel compressorStationModel = null; // @jve:decl-index=0:
	private TurboCompressor turboCompressor = null; 
	
	private CompressorGeneralDisplayData generalCompressorInfo = null;
	private Calc9ParameterDisplay nIsolineCoeff;
	private Calc9ParameterDisplay adiabaticEffCoeff;
	
	
	/**
	 * This is the default constructor
	 */
	public TurboCompressorDisplayData(CompressorStationEditorPanel compressorStationEditorPanel, TurboCompressor turboCompressor) {
		this.compressorStationEditorPanel = compressorStationEditorPanel;
		if (this.compressorStationEditorPanel!=null) {
			this.compressorStationModel = this.compressorStationEditorPanel.getCompressorStationModel();	
		}
		this.turboCompressor = turboCompressor;
		initialize();
	}
	/**
	 * This method initializes this
	 * @return void
	 */
	private void initialize() {
		
		GridBagConstraints gridBagConstraintsGeneralInfo = new GridBagConstraints();
		gridBagConstraintsGeneralInfo.gridx = 0;
		gridBagConstraintsGeneralInfo.weightx = 1.0;
		gridBagConstraintsGeneralInfo.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraintsGeneralInfo.anchor = GridBagConstraints.WEST;
		gridBagConstraintsGeneralInfo.gridy = 0;
		
		GridBagConstraints gridBagConstraintsNisoLineCoeff = new GridBagConstraints();
		gridBagConstraintsNisoLineCoeff.gridx = 0;
		gridBagConstraintsNisoLineCoeff.anchor = GridBagConstraints.WEST;
		gridBagConstraintsNisoLineCoeff.insets = new Insets(5, 0, 0, 0);
		gridBagConstraintsNisoLineCoeff.gridy = 1;

		GridBagConstraints gridBagConstraintsAdiabaticEffCoeff = new GridBagConstraints();
		gridBagConstraintsAdiabaticEffCoeff.gridx = 0;
		gridBagConstraintsAdiabaticEffCoeff.gridy = 2;
		gridBagConstraintsAdiabaticEffCoeff.anchor = GridBagConstraints.WEST;
		gridBagConstraintsAdiabaticEffCoeff.insets = new Insets(5, 0, 0, 0);
		
		this.setSize(531, 410);
		this.setLayout(new GridBagLayout());
		this.add(getGeneralCompressorInfo(), gridBagConstraintsGeneralInfo);
		this.add(getNisoLineCoeff(), gridBagConstraintsNisoLineCoeff);
		this.add(getAdiabaticEffCoeff(), gridBagConstraintsAdiabaticEffCoeff);
		
	}
	/**
	 * This method initializes jPanelTop	
	 * @return javax.swing.JPanel	
	 */
	private CompressorGeneralDisplayData getGeneralCompressorInfo() {
		if (generalCompressorInfo == null) {
			generalCompressorInfo = new CompressorGeneralDisplayData(this.compressorStationEditorPanel, this.turboCompressor);
		}
		return generalCompressorInfo;
	}
	
	/**
	 * Gets the n isoline coeff.
	 * @return the n isoline coeff
	 */
	private Calc9ParameterDisplay getNisoLineCoeff() {
		if (nIsolineCoeff==null) {
			nIsolineCoeff = new Calc9ParameterDisplay("Coefficients for the isolines of speed");
			nIsolineCoeff.addCalcParameterListener(this);
		}
		return nIsolineCoeff;
	}
	/**
	 * Gets the adiabatic eff coeff.
	 * @return the adiabatic eff coeff
	 */
	private Calc9ParameterDisplay getAdiabaticEffCoeff() {
		if (adiabaticEffCoeff==null) {
			adiabaticEffCoeff = new Calc9ParameterDisplay("Coefficients for the isolines of the adiabatic efficiency");
			adiabaticEffCoeff.addCalcParameterListener(this);
		}
		return adiabaticEffCoeff;
	}
	
	@Override
	public void parameterChanged(ParameterDisplay display, int noOfParameter, Float value) {

	}

	
}  //  @jve:decl-index=0:visual-constraint="10,10"
