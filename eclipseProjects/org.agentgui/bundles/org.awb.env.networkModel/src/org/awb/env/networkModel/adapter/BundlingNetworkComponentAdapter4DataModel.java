package org.awb.env.networkModel.adapter;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;
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
	
	private JTabbedPane eomJTabbedPane;
	
	/**
	 * Instantiates a new bundling network component adapter for the actual data models.
	 *
	 * @param graphController the graph controller
	 * @param dataModelAdapterMap the data model adapter map
	 */
	public BundlingNetworkComponentAdapter4DataModel(GraphEnvironmentController graphController, TreeMap<String, NetworkComponentAdapter4DataModel> dataModelAdapterMap) {
		super();
		this.dataModelAdapterMap = dataModelAdapterMap;
		this.setGraphEnvironmentController(graphController);
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
	 * @see org.awb.env.networkModel.adapter.NetworkComponentAdapter4DataModel#setVisualizationComponent(javax.swing.JComponent)
	 */
	@Override
	public void setVisualizationComponent(JComponent visualizationComponent) {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.NetworkComponentAdapter4DataModel#getVisualizationComponent(org.awb.env.networkModel.controller.ui.BasicGraphGuiProperties)
	 */
	@Override
	public JComponent getVisualizationComponent(BasicGraphGuiProperties internalPropertyFrame) {
		return this.getJTabbedPaneVisualization(internalPropertyFrame);
	}
	/**
	 * Returns the JTabbedPane for the visualization.
	 * @return the JTabbedPane visualization
	 */
	public JTabbedPane getJTabbedPaneVisualization(BasicGraphGuiProperties internalPropertyFrame) {
		if (eomJTabbedPane==null ) {
			// --- Create the JTabbedPane -------------------------------------
			eomJTabbedPane = new JTabbedPane();
			eomJTabbedPane.setFont(new Font("Dialog", Font.BOLD, 12));
			eomJTabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
			
			for (int i = 0; i < this.getTitleList().size(); i++) {
				String title = this.getTitleList().get(i);
				NetworkComponentAdapter4DataModel dmAdapter = this.dataModelAdapterMap.get(title);
				if (dmAdapter!=null) {
					JComponent tabContent = dmAdapter.getVisualizationComponent(internalPropertyFrame);
					eomJTabbedPane.addTab(title, tabContent);
				}
			}
			// --- Add a listener to the tab selection ------------------------
			eomJTabbedPane.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent ce) {
					// TODO Required ???
				}
			});
		}
		return eomJTabbedPane;
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.NetworkComponentAdapter4DataModel#save()
	 */
	@Override
	public boolean save() {

		// --- Default return value ---------------------------------
		boolean savedSuccessful = true;
		
		// --- Distribute over Sub-NetworkComponentAdapter ---------- 
		for (int i = 0; i < this.getTitleList().size(); i++) {
			String title = this.getTitleList().get(i);
			NetworkComponentAdapter4DataModel dmAdapter = this.dataModelAdapterMap.get(title);
			if (dmAdapter!=null) {
				boolean success = dmAdapter.save();
				if (success==false && savedSuccessful==true) {
					savedSuccessful = success; 
				}
			}
		}
		return savedSuccessful;
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

	// --------------------------------------------------------------
	// --- ToolBar elements for the property dialog -----------------
	// --------------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.NetworkComponentAdapter4DataModel#getToolBarElements()
	 */
	@Override
	public Vector<JComponent> getToolBarElements() {

		Vector<JComponent> menuElements = new Vector<>();
		
		// --- Call in Sub-NetworkComponentAdapter ------------------
		for (int i = 0; i < this.getTitleList().size(); i++) {
			String title = this.getTitleList().get(i);
			NetworkComponentAdapter4DataModel dmAdapter = this.dataModelAdapterMap.get(title);
			if (dmAdapter!=null) {
				// --- Get the sub menu elements -------------------- 
				Vector<JComponent> subMenuElements = dmAdapter.getToolBarElements();
				if (subMenuElements!=null && subMenuElements.size()>0) {
					// --- Add to overall menu list -----------------
					menuElements.addAll(subMenuElements);
					// --- Check the last menu element --------------
					JComponent lastMenuElement = menuElements.get(menuElements.size()-1); 
					if ( ! (lastMenuElement instanceof JSeparator)) {
						// --- Add a JSepartaor ---------------------
						menuElements.add(new JSeparator());
					}
				}
			}
		}

		// --- Prepare return value ---------------------------------
		if (menuElements.size()>0) {
			return menuElements;
		}
		return null;
	}

	
	// --------------------------------------------------------------
	// --- Some setter methods that has be overwritten --------------
	// --------------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.NetworkComponentAdapter4DataModel#setGraphEnvironmentController(org.awb.env.networkModel.controller.GraphEnvironmentController)
	 */
	@Override
	public void setGraphEnvironmentController(GraphEnvironmentController graphController) {
		// --- Call in super class ----------------------------------
		super.setGraphEnvironmentController(graphController);
		// --- Distribute over Sub-NetworkComponentAdapter ---------- 
		for (int i = 0; i < this.getTitleList().size(); i++) {
			String title = this.getTitleList().get(i);
			NetworkComponentAdapter4DataModel dmAdapter = this.dataModelAdapterMap.get(title);
			if (dmAdapter!=null) {
				dmAdapter.setGraphEnvironmentController(graphController);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.NetworkComponentAdapter4DataModel#setNetworkComponentAdapter(org.awb.env.networkModel.adapter.NetworkComponentAdapter)
	 */
	@Override
	public void setNetworkComponentAdapter(NetworkComponentAdapter networkComponentAdapter) {
		// --- Call in super class ----------------------------------
		super.setNetworkComponentAdapter(networkComponentAdapter);
		// --- Distribute over Sub-NetworkComponentAdapter ---------- 
		for (int i = 0; i < this.getTitleList().size(); i++) {
			String title = this.getTitleList().get(i);
			NetworkComponentAdapter4DataModel dmAdapter = this.dataModelAdapterMap.get(title);
			if (dmAdapter!=null) {
				dmAdapter.setNetworkComponentAdapter(networkComponentAdapter);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.NetworkComponentAdapter4DataModel#setNetworkComponent(org.awb.env.networkModel.NetworkComponent)
	 */
	@Override
	public void setNetworkComponent(NetworkComponent networkComponent) {
		// --- Call in super class ----------------------------------
		super.setNetworkComponent(networkComponent);
		// --- Distribute over Sub-NetworkComponentAdapter ---------- 
		for (int i = 0; i < this.getTitleList().size(); i++) {
			String title = this.getTitleList().get(i);
			NetworkComponentAdapter4DataModel dmAdapter = this.dataModelAdapterMap.get(title);
			if (dmAdapter!=null) {
				dmAdapter.setNetworkComponent(networkComponent);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.NetworkComponentAdapter4DataModel#setGraphNode(org.awb.env.networkModel.GraphNode)
	 */
	@Override
	public void setGraphNode(GraphNode graphNode) {
		// --- Call in super class ----------------------------------
		super.setGraphNode(graphNode);
		// --- Distribute over Sub-NetworkComponentAdapter ---------- 
		for (int i = 0; i < this.getTitleList().size(); i++) {
			String title = this.getTitleList().get(i);
			NetworkComponentAdapter4DataModel dmAdapter = this.dataModelAdapterMap.get(title);
			if (dmAdapter!=null) {
				dmAdapter.setGraphNode(graphNode);
			}
		}
	}
	
}
