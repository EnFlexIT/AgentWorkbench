package agas.adapter;

import java.util.Vector;

import javax.swing.JComponent;

import agentgui.envModel.graph.networkModel.NetworkComponentAdapter;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel;


public class ControlValveAdapter extends NetworkComponentAdapter {

	
	@Override
	public NetworkComponentAdapter4DataModel getNewDataModelAdapter() {
		return new ControlValveDataModelAdapter();
	}

	@Override
	public Vector<JComponent> getJPopupMenuElements() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
