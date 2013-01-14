package gasmas.adapter;

import java.util.Vector;

import javax.swing.JComponent;

import agentgui.envModel.graph.networkModel.NetworkComponentAdapter;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel;


public class ResistorAdapter extends NetworkComponentAdapter {

	private ResistorDataModelAdapter resistorDataModelAdapter=null;
	
	@Override
	public NetworkComponentAdapter4DataModel getDataModelAdapter() {
		if (resistorDataModelAdapter==null) {
			resistorDataModelAdapter = new ResistorDataModelAdapter();
		}
		return resistorDataModelAdapter;
	}

	@Override
	public Vector<JComponent> getJPopupMenuElements() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
