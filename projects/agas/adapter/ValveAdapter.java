package agas.adapter;

import java.util.Vector;

import javax.swing.JComponent;

import agentgui.envModel.graph.networkModel.NetworkComponentAdapter;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel;


public class ValveAdapter extends NetworkComponentAdapter {

	private ValveDataModelAdapter valveDataModelAdapter=null;
	
	@Override
	public NetworkComponentAdapter4DataModel getDataModelAdapter() {
		if (valveDataModelAdapter==null) {
			valveDataModelAdapter=new ValveDataModelAdapter();
		}
		return valveDataModelAdapter;
	}

	@Override
	public Vector<JComponent> getJPopupMenuElements() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
