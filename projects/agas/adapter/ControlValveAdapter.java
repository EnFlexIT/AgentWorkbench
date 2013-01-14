package agas.adapter;

import java.util.Vector;

import javax.swing.JComponent;

import agentgui.envModel.graph.networkModel.NetworkComponentAdapter;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel;


public class ControlValveAdapter extends NetworkComponentAdapter {

	private ControlValveDataModelAdapter controlValveDataModelAdapter=null;
	
	@Override
	public NetworkComponentAdapter4DataModel getDataModelAdapter() {
		if (controlValveDataModelAdapter==null) {
			controlValveDataModelAdapter=new ControlValveDataModelAdapter();
		}
		return controlValveDataModelAdapter;
	}

	@Override
	public Vector<JComponent> getJPopupMenuElements() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
