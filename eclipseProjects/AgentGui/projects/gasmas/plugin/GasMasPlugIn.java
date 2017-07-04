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
package gasmas.plugin;

import agentgui.core.plugin.PlugIn;
import agentgui.core.project.Project;
import agentgui.envModel.graph.controller.BasicGraphGui.ToolBarSurrounding;
import agentgui.envModel.graph.controller.BasicGraphGui.ToolBarType;
import agentgui.envModel.graph.controller.CustomToolbarComponentDescription;
import agentgui.envModel.graph.controller.GraphEnvironmentController;
import gasmas.compStat.CompressorStationVisualisation;
import gasmas.transfer.zib.OGE_Importer;


/**
 * The Class GasMasPlugIn.
 * 
 * @author Christian Derksen - DAWIS - ICB University of Duisburg - Essen
 */
public class GasMasPlugIn extends PlugIn {

	private GraphEnvironmentController graphController = null;
	
	public GasMasPlugIn(Project currProject) {
		super(currProject);
		graphController = (GraphEnvironmentController) this.project.getEnvironmentController();
	}

	@Override
	public String getName() {
		return "PlugIn for MAS of Natural Gas Network";
	}

	@Override
	public void onPlugIn() {
		super.onPlugIn();
	
		// --- Add the customised import methods to the import methods --------
		OGE_Importer ogeImporter = new OGE_Importer(this.graphController, "net", "OGE / ZIB-Gas Netzwerk");
		this.graphController.getImportAdapter().add(ogeImporter);
		
		// --- Add the visual representation for compressor stations ----------
		this.addOntologyClassVisualisation(CompressorStationVisualisation.class.getName());
	
		// --- Test for a custom tollbar button for the graph environment ----- 
		this.graphController.addCustomToolbarComponentDescription(new CustomToolbarComponentDescription(ToolBarType.ViewControl, ToolBarSurrounding.RuntimeOnly, CustomToolbarButton.class, null, true));
	}

	
}
