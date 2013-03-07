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
import gasmas.ontology.TurboCompressor;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import agentgui.core.application.Language;

public class TurboCompressorDisplay extends JTabbedPane implements ParameterListener {

	private static final long serialVersionUID = 1083283970878896562L;

	private CompressorStationModel compressorStationModel = null; // @jve:decl-index=0:
	private String turboCompressorID = null;
	private TurboCompressor myTurboCompressor = null;  //  @jve:decl-index=0:

	private JPanel jPanelCharacteristicDiagram = null;
	private JScrollPane jScrollPaneData = null;
	private TurboCompressorDisplayData jPanelData = null;
	

	/**
	 * Instantiates a new turbo compressor display.
	 * @param turboCompressor the turbo compressor
	 */
	public TurboCompressorDisplay(CompressorStationModel compressorStationModel, String turboCompressorID) {
		this.compressorStationModel = compressorStationModel;
		this.turboCompressorID = turboCompressorID;
		this.initialize();
		this.setTurboCompressor((TurboCompressor) this.compressorStationModel.getComponent(this.turboCompressorID));
	}
	
	/**
	 * This method initializes this
	 */
	private void initialize() {
        this.setSize(new Dimension(554, 427));
        this.setTabPlacement(JTabbedPane.BOTTOM);
		this.addTab(Language.translate("Characteristic Diagram", Language.EN), null, getJPanelCharacteristicDiagram(), null);	
		this.addTab(Language.translate("Data", Language.EN), null, getJScrollPaneData(), null);
	}
	
	/**
	 * Sets the turbo compressor.
	 * @param turboCompressor the new turbo compressor
	 */
	public void setTurboCompressor(TurboCompressor turboCompressor) {
		this.myTurboCompressor = turboCompressor;
		this.turboCompressorID = this.myTurboCompressor.getID();
		this.getJPanelData().setTurboCompressor(this.myTurboCompressor);
	}
	/**
	 * Gets the turbo compressor.
	 * @return the turbo compressor
	 */
	public TurboCompressor getTurboCompressor() {
		return myTurboCompressor;
	}

	/**
	 * This method initializes jPanelCharacteristicDiagram	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelCharacteristicDiagram() {
		if (jPanelCharacteristicDiagram == null) {
			jPanelCharacteristicDiagram = new JPanel();
			jPanelCharacteristicDiagram.setLayout(new GridBagLayout());
		}
		return jPanelCharacteristicDiagram;
	}

	/**
	 * This method initializes jScrollPaneData	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPaneData() {
		if (jScrollPaneData == null) {
			jScrollPaneData = new JScrollPane();
			jScrollPaneData.setViewportView(this.getJPanelData());
		}
		return jScrollPaneData;
	}
	/**
	 * This method initializes jPanelData	
	 * @return javax.swing.JPanel	
	 */
	private TurboCompressorDisplayData getJPanelData() {
		if (jPanelData == null) {
			jPanelData = new TurboCompressorDisplayData(compressorStationModel, turboCompressorID);
			jPanelData.addParameterListener(this);
		}
		return jPanelData;
	}

	@Override
	public void subParameterChanged(ParameterDisplay display, String parameterDescription, Object value) {
		
		if (display == this.getJPanelData()) {
			// --- An update in the TurboCompressor was set ---------
			TurboCompressor newTurboCompressor = (TurboCompressor) value;
			this.myTurboCompressor = newTurboCompressor;
			this.compressorStationModel.updateComponent(this.myTurboCompressor);
		}
		
	}
	
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
