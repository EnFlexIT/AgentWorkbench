package gasmas.adapter;

import java.util.Vector;

import javax.swing.JComponent;

import agentgui.envModel.graph.networkModel.NetworkComponentAdapter;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel;


public class ShortPipeAdapter extends NetworkComponentAdapter {

	private ShortPipeDataModelAdapter shortPipeDataModelAdapter=null;
	@Override
	public NetworkComponentAdapter4DataModel getDataModelAdapter() {
		if (shortPipeDataModelAdapter==null) {
			shortPipeDataModelAdapter=new ShortPipeDataModelAdapter();
		}
		return shortPipeDataModelAdapter;
	}

	@Override
	public Vector<JComponent> getJPopupMenuElements() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
