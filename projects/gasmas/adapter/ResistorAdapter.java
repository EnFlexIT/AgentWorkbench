package gasmas.adapter;

import java.util.Vector;

import javax.swing.JComponent;

import agentgui.envModel.graph.controller.GraphEnvironmentController;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel;


public class ResistorAdapter extends NetworkComponentAdapter {

	public ResistorAdapter(GraphEnvironmentController graphEnvironmentController) {
		super(graphEnvironmentController);
	}

	@Override
	public NetworkComponentAdapter4DataModel getNewDataModelAdapter() {
		return new ResistorDataModelAdapter(this.graphController);
	}

	@Override
	public Vector<JComponent> getJPopupMenuElements() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
