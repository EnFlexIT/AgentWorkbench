package gasmas.adapter;

import java.util.Vector;

import javax.swing.JComponent;

import agentgui.envModel.graph.networkModel.NetworkComponentAdapter;
import agentgui.envModel.graph.networkModel.NetworkComponentAdapter4DataModel;


public class CompressorAdapter extends NetworkComponentAdapter {

	private CompressorDataModelAdapter compressorDataModelAdapter=null;
	
	@Override
	public NetworkComponentAdapter4DataModel getDataModelAdapter() {
		if (compressorDataModelAdapter==null) {
			compressorDataModelAdapter=new CompressorDataModelAdapter();
		}
		return compressorDataModelAdapter;
	}

	@Override
	public Vector<JComponent> getJPopupMenuElements() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
