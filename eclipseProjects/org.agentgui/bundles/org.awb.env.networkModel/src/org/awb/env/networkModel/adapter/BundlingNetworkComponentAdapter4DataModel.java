package org.awb.env.networkModel.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JComponent;

import org.awb.env.networkModel.controller.GraphEnvironmentController;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiProperties;

/**
 * The Class BundlingNetworkComponentAdapter4DataModel is used from a {@link BundlingNetworkComponentAdapter}
 * to organize the aggregation of multiple {@link NetworkComponentAdapter4DataModel}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class BundlingNetworkComponentAdapter4DataModel extends NetworkComponentAdapter4DataModel {

	private TreeMap<String, NetworkComponentAdapter4DataModel> dataModelAdapterMap;
	private List<String> titleList;
	
	
	/**
	 * Instantiates a new bundling network component adapter for the actual data models.
	 *
	 * @param graphController the graph controller
	 * @param dataModelAdapterMap the data model adapter map
	 */
	public BundlingNetworkComponentAdapter4DataModel(GraphEnvironmentController graphController, TreeMap<String, NetworkComponentAdapter4DataModel> dataModelAdapterMap) {
		super(graphController);
		this.dataModelAdapterMap = dataModelAdapterMap;
	}

	/**
	 * Returns the list of titles in an ascending order.
	 * @return the title list
	 */
	private List<String> getTitleList() {
		if (titleList==null) {
			titleList = new ArrayList<>(this.dataModelAdapterMap.keySet());
			Collections.sort(titleList);
		}
		return titleList;
	}
	
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.NetworkComponentAdapter4DataModel#getVisualizationComponent(org.awb.env.networkModel.controller.ui.BasicGraphGuiProperties)
	 */
	@Override
	public JComponent getVisualizationComponent(BasicGraphGuiProperties internalPropertyFrame) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.NetworkComponentAdapter4DataModel#setVisualizationComponent(javax.swing.JComponent)
	 */
	@Override
	public void setVisualizationComponent(JComponent visualizationComponent) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.NetworkComponentAdapter4DataModel#save()
	 */
	@Override
	public boolean save() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.NetworkComponentAdapter4DataModel#setDataModel(java.lang.Object)
	 */
	@Override
	public void setDataModel(Object dataModel) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.NetworkComponentAdapter4DataModel#getDataModel()
	 */
	@Override
	public Object getDataModel() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.NetworkComponentAdapter4DataModel#getDataModelBase64Encoded(java.lang.Object)
	 */
	@Override
	public Vector<String> getDataModelBase64Encoded(Object dataModel) {
		// TODO Auto-generated method stub
		return super.getDataModelBase64Encoded(dataModel);
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.NetworkComponentAdapter4DataModel#getDataModelBase64Decoded(java.util.Vector)
	 */
	@Override
	public Object getDataModelBase64Decoded(Vector<String> dataModel) {
		// TODO Auto-generated method stub
		return super.getDataModelBase64Decoded(dataModel);
	}
	
}
