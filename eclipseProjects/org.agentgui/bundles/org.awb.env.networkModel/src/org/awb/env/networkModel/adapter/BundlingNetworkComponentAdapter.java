package org.awb.env.networkModel.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JSeparator;

import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.controller.GraphEnvironmentController;

/**
 * The Class BundlingNetworkComponentAdapter is able to handle multiple {@link NetworkComponentAdapter}.
 * Its intention basically lies in the handling of data models for {@link GraphNode}s that are connected
 * with more than one domain, but the general concept of the class is not restricted to that.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class BundlingNetworkComponentAdapter extends NetworkComponentAdapter {

	private List<String> titleList;
	private TreeMap<String, NetworkComponentAdapter> netCompAdapterMap;
	
	
	/**
	 * Instantiates a new bundling network component adapter.
	 * @param netCompAdapterMap the map of titles (e.g. domains) combined the actual NetworkComponentAdapter to use
	 */
	public BundlingNetworkComponentAdapter(TreeMap<String, NetworkComponentAdapter> netCompAdapterMap) {
		if (netCompAdapterMap==null || netCompAdapterMap.isEmpty()) {
			throw new IllegalArgumentException("The TreeMap of NetworkComponentAdapter to use is not allowed to be null or empty!");
		}
		this.netCompAdapterMap = netCompAdapterMap;
	}
	
	/**
	 * Returns the list of titles in an ascending order.
	 * @return the title list
	 */
	private List<String> getTitleList() {
		if (titleList==null) {
			titleList = new ArrayList<>(this.netCompAdapterMap.keySet());
			Collections.sort(titleList);
		}
		return titleList;
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.NetworkComponentAdapter#getNewDataModelAdapter()
	 */
	@Override
	public NetworkComponentAdapter4DataModel getNewDataModelAdapter() {
		
		// --- Create treeMap for bundling data model adapter -------  
		TreeMap<String, NetworkComponentAdapter4DataModel> netCompAdapterDataModelMap = new TreeMap<>();
		for (int i = 0; i < this.getTitleList().size(); i++) {
			// --- Get everything together --------------------------
			String title = this.getTitleList().get(i);
			NetworkComponentAdapter netCompAdapter = this.netCompAdapterMap.get(title);
			NetworkComponentAdapter4DataModel dmAdapter = netCompAdapter.getNewDataModelAdapter();
			// --- Put to TreeMap -----------------------------------
			netCompAdapterDataModelMap.put(title, dmAdapter);
		}
		// --- Create and return the bundling data model adapter ----
		return new BundlingNetworkComponentAdapter4DataModel(getGraphEnvironmentController(), netCompAdapterDataModelMap);
	}

	
	// --------------------------------------------------------------
	// --- Context menu generation ----------------------------------
	// --------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.NetworkComponentAdapter#getJPopupMenuElements()
	 */
	@Override
	public Vector<JComponent> getJPopupMenuElements() {

		Vector<JComponent> menuElements = new Vector<>();
		
		// --- Call in Sub-NetworkComponentAdapter ------------------
		for (int i = 0; i < this.getTitleList().size(); i++) {
			String title = this.getTitleList().get(i);
			NetworkComponentAdapter netCompAdapter = this.netCompAdapterMap.get(title);
			if (netCompAdapter!=null) {
				// --- Get the sub menu elements -------------------- 
				Vector<JComponent> subMenuElements = netCompAdapter.getJPopupMenuElements();
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
	// --- Action / Void methods to overwrite -----------------------
	// --------------------------------------------------------------
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.NetworkComponentAdapter#initialize()
	 */
	@Override
	public void initialize() {
		// --- Call in Sub-NetworkComponentAdapter ------------------
		for (int i = 0; i < this.getTitleList().size(); i++) {
			String title = this.getTitleList().get(i);
			NetworkComponentAdapter netCompAdapter = this.netCompAdapterMap.get(title);
			if (netCompAdapter!=null) {
				netCompAdapter.initialize();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.NetworkComponentAdapter#resetStoredDataModelAdapter()
	 */
	@Override
	public void resetStoredDataModelAdapter() {
		// --- Call in Sub-NetworkComponentAdapter first ------------ 
		for (int i = 0; i < this.getTitleList().size(); i++) {
			String title = this.getTitleList().get(i);
			NetworkComponentAdapter netCompAdapter = this.netCompAdapterMap.get(title);
			if (netCompAdapter!=null) {
				netCompAdapter.resetStoredDataModelAdapter();
			}
		}
		// --- Call in super class ----------------------------------
		super.resetStoredDataModelAdapter();
	}
	
	// --------------------------------------------------------------
	// --- Some setter methods that has be overwritten --------------
	// --------------------------------------------------------------
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.NetworkComponentAdapter#setGraphEnvironmentController(org.awb.env.networkModel.controller.GraphEnvironmentController)
	 */
	@Override
	public void setGraphEnvironmentController(GraphEnvironmentController graphController) {
		// --- Call in super class ----------------------------------
		super.setGraphEnvironmentController(graphController);
		// --- Distribute over Sub-NetworkComponentAdapter ---------- 
		for (int i = 0; i < this.getTitleList().size(); i++) {
			String title = this.getTitleList().get(i);
			NetworkComponentAdapter netCompAdapter = this.netCompAdapterMap.get(title);
			if (netCompAdapter!=null) {
				netCompAdapter.setGraphEnvironmentController(graphController);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.NetworkComponentAdapter#setNetworkComponent(org.awb.env.networkModel.NetworkComponent)
	 */
	@Override
	public void setNetworkComponent(NetworkComponent networkComponent) {
		// --- Call in super class ----------------------------------
		super.setNetworkComponent(networkComponent);
		// --- Distribute over Sub-NetworkComponentAdapter ---------- 
		for (int i = 0; i < this.getTitleList().size(); i++) {
			String title = this.getTitleList().get(i);
			NetworkComponentAdapter netCompAdapter = this.netCompAdapterMap.get(title);
			if (netCompAdapter!=null) {
				netCompAdapter.setNetworkComponent(networkComponent);;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.NetworkComponentAdapter#setGraphNode(org.awb.env.networkModel.GraphNode)
	 */
	@Override
	public void setGraphNode(GraphNode graphNode) {
		// --- Call in super class ----------------------------------
		super.setGraphNode(graphNode);
		// --- Distribute over Sub-NetworkComponentAdapter ---------- 
		for (int i = 0; i < this.getTitleList().size(); i++) {
			String title = this.getTitleList().get(i);
			NetworkComponentAdapter netCompAdapter = this.netCompAdapterMap.get(title);
			if (netCompAdapter!=null) {
				netCompAdapter.setGraphNode(graphNode);
			}
		}
	}
	
}
