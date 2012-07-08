package gasmas.adapter;

import java.util.Vector;

import javax.swing.JMenuItem;

import agentgui.envModel.graph.networkModel.NetworkComponentAdapter;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel;


public class CompressorAdapter extends NetworkComponentAdapter {

	@Override
	public NetworkComponentAdapter4DataModel getDataModelAdapter() {
		return new CompressorDataModelAdapter();
	}

	@Override
	public Vector<JMenuItem> getJPopupMenuElements() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
