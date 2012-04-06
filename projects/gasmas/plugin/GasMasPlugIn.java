package gasmas.plugin;

import gasmas.transfer.zib.OGE_Importer;
import agentgui.core.plugin.PlugIn;
import agentgui.core.project.Project;
import agentgui.envModel.graph.controller.GraphEnvironmentController;

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
	
		// --- Add the customized import methods to the import methods --------
		OGE_Importer ogeImporter = new OGE_Importer(this.graphController, "net", "OGE / ZIB-Gas Netzwerk");
		this.graphController.getImportAdapter().add(ogeImporter);
		
		
	}
	
	
}
