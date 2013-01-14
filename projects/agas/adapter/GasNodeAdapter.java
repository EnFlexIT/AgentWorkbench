package agas.adapter;

import java.util.Vector;

import javax.swing.JMenuItem;

import agentgui.envModel.graph.networkModel.NetworkComponentAdapter;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel;

public class GasNodeAdapter extends NetworkComponentAdapter {

	private GasNodeDataModelAdapter gasNodeDataModelAdapter = null;
	
	@Override
	public NetworkComponentAdapter4DataModel getDataModelAdapter() {
		if (gasNodeDataModelAdapter==null) {
			gasNodeDataModelAdapter = new GasNodeDataModelAdapter();
		}
		return gasNodeDataModelAdapter;
	}

	@Override
	public Vector<JMenuItem> getJPopupMenuElements() {
		// TODO Auto-generated method stub
		return null;
	}

}
