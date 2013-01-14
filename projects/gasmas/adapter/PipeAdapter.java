package gasmas.adapter;

import java.util.Vector;

import javax.swing.JComponent;

import agentgui.envModel.graph.networkModel.NetworkComponentAdapter;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel;


public class PipeAdapter extends NetworkComponentAdapter {

	private PipeDataModelAdapter pipeDataModelAdapter=null;
	
	@Override
	public NetworkComponentAdapter4DataModel getDataModelAdapter() {
		if (pipeDataModelAdapter==null) {
			pipeDataModelAdapter=new PipeDataModelAdapter();
		}
		return pipeDataModelAdapter;
	}

	@Override
	public Vector<JComponent> getJPopupMenuElements() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
