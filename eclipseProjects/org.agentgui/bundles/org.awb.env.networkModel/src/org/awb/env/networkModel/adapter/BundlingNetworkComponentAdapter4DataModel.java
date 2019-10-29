package org.awb.env.networkModel.adapter;

import java.awt.Component;
import java.awt.Dimension;
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
import org.awb.env.networkModel.controller.ui.BasicGraphGuiJDesktopPane;
import org.awb.env.networkModel.controller.ui.BasicGraphGuiProperties;
import org.awb.env.networkModel.dataModel.AbstractDataModelStorageHandler;

import de.enflexit.common.ontology.gui.OntologyInstanceViewer;

/**
 * The Class BundlingNetworkComponentAdapter4DataModel is used from a {@link BundlingNetworkComponentAdapter}
 * to organize the aggregation of multiple {@link NetworkComponentAdapter4DataModel}.
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public class BundlingNetworkComponentAdapter4DataModel extends NetworkComponentAdapter4DataModel {

	private TreeMap<String, NetworkComponentAdapter4DataModel> dataModelAdapterMap;
	private List<String> titleList;
	
	private BundlingDataModelStorageHandler storageHandler;

	private BasicGraphGuiJDesktopPane basicGraphGuiDesktop;
	private BasicGraphGuiProperties basicGraphGuiProperties;
	
	private JTabbedPane jTabbedPaneVisualization;
	private int previousTabSelection;
	private TreeMap<String, Dimension> visSizeMap;
	
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
	protected List<String> getTitleList() {
		if (titleList==null) {
			titleList = new ArrayList<>(this.dataModelAdapterMap.keySet());
			Collections.sort(titleList);
		}
		return titleList;
	}
	/**
	 * Returns the data model adapter TreeMap.
	 * @return the data model adapter TreeMap
	 */
	protected TreeMap<String, NetworkComponentAdapter4DataModel> getDataModelAdapterMap() {
		return this.dataModelAdapterMap;
	}
	
	// ------------------------------------------------------------------------
	// --- From here the data model handling is implemented -------------------
	// ------------------------------------------------------------------------
	
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
	@SuppressWarnings("unchecked")
	@Override
	public void setDataModel(Object dataModel) {

		TreeMap<String, Object> dmTreeMap = null;
		if (dataModel!=null && dataModel instanceof TreeMap<?, ?>) {
			dmTreeMap = (TreeMap<String, Object>) dataModel;
		}
		
		for (int i = 0; i < this.getTitleList().size(); i++) {
			String tabTitle = this.getTitleList().get(i);
			NetworkComponentAdapter4DataModel dmAdapter = this.dataModelAdapterMap.get(tabTitle);
			Object partDataModel = null;
			if (dmTreeMap!=null) {
				partDataModel = dmTreeMap.get(tabTitle);
			}
			dmAdapter.setDataModel(partDataModel);
		}
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.NetworkComponentAdapter4DataModel#getDataModel()
	 */
	@Override
	public Object getDataModel() {
		
		TreeMap<String, Object> dmTreeMap = new TreeMap<>();
		for (int i = 0; i < this.getTitleList().size(); i++) {
			String tabTitle = this.getTitleList().get(i);
			NetworkComponentAdapter4DataModel dmAdapter = this.dataModelAdapterMap.get(tabTitle);
			if (dmAdapter instanceof NetworkComponentAdapter4Ontology) dmAdapter.save();
			dmTreeMap.put(tabTitle, dmAdapter.getDataModel());
		} 
		return dmTreeMap;
	}

	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.NetworkComponentAdapter4DataModel#getDataModelStorageHandler()
	 */
	@Override
	protected AbstractDataModelStorageHandler getDataModelStorageHandler() {
		if (storageHandler==null) {
			storageHandler = new BundlingDataModelStorageHandler(this);
		}
		return storageHandler;
	}
	
	
	// ------------------------------------------------------------------------
	// --- From here visualization handling is implemented --------------------
	// ------------------------------------------------------------------------
	/**
	 * Returns the current instance of the BasicGraphGuiJDesktopPane.
	 * @return the basic graph gui desktop
	 */
	private BasicGraphGuiJDesktopPane getBasicGraphGuiDesktop() {
		return basicGraphGuiDesktop;
	}
	/**
	 * Gets the current instance of the BasicGraphGuiProperties.
	 * @return the basic graph gui properties
	 */
	private BasicGraphGuiProperties getBasicGraphGuiProperties() {
		return basicGraphGuiProperties;
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
		this.basicGraphGuiProperties = internalPropertyFrame;
		return this.getJTabbedPaneVisualization();
	}
	/**
	 * Returns the visualization component for the specified tab (tab title).
	 *
	 * @param title the tab title
	 * @return the visualization component
	 */
	public JComponent getVisualizationComponent(String title) {
		JComponent tabContent = null;
		NetworkComponentAdapter4DataModel dmAdapter = this.dataModelAdapterMap.get(title);
		if (dmAdapter!=null) {
			tabContent = dmAdapter.getVisualizationComponent(this.getBasicGraphGuiProperties());
		}
		return tabContent;
	}
	/**
	 * Returns the JTabbedPane for the visualization.
	 * @return the JTabbedPane visualization
	 */
	public JTabbedPane getJTabbedPaneVisualization() {
		if (jTabbedPaneVisualization==null ) {
			// --- Create the JTabbedPane -------------------------------------
			jTabbedPaneVisualization = new JTabbedPane();
			jTabbedPaneVisualization.setFont(new Font("Dialog", Font.BOLD, 12));
			jTabbedPaneVisualization.setTabPlacement(JTabbedPane.BOTTOM);
			
			for (int i = 0; i < this.getTitleList().size(); i++) {
				String title = this.getTitleList().get(i);
				JComponent tabContent = this.getVisualizationComponent(title);
				if (tabContent!=null) {
					jTabbedPaneVisualization.addTab(title, tabContent);
				}
				
			}
			// --- Add a listener to the tab selection ------------------------
			jTabbedPaneVisualization.addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent ce) {
					// --- Set size of property dialog ------------------------
					BundlingNetworkComponentAdapter4DataModel.this.setSizeOfBasicGraphGuiProperties();
					// --- Remind selection as 'old' value for next switch ----
					BundlingNetworkComponentAdapter4DataModel.this.previousTabSelection = BundlingNetworkComponentAdapter4DataModel.this.getJTabbedPaneVisualization().getSelectedIndex(); 
				}
			});
		}
		return jTabbedPaneVisualization;
	}
	/* (non-Javadoc)
	 * @see org.awb.env.networkModel.adapter.NetworkComponentAdapter4DataModel#getSizeOfVisualisation(org.awb.env.networkModel.controller.ui.BasicGraphGuiJDesktopPane)
	 */
	@Override
	public Dimension getSizeOfVisualisation(BasicGraphGuiJDesktopPane graphDesktop) {
		
		this.basicGraphGuiDesktop = graphDesktop;
		
		int selectedTabIndex = this.getJTabbedPaneVisualization().getSelectedIndex();
		String selectedTabTitle = this.getJTabbedPaneVisualization().getTitleAt(selectedTabIndex);
		if (selectedTabTitle!=null) {
			return this.getVisualisationSizeMap().get(selectedTabTitle);
		}
		return null;
	}

	/**
	 * Gets the visualization size map.
	 * @return the visualization size map
	 */
	private TreeMap<String, Dimension> getVisualisationSizeMap() {
		if (visSizeMap==null) {
			visSizeMap = new TreeMap<>();
			// --- Fill the TreeMap with initial values ---
			for (int i = 0; i < this.getTitleList().size(); i++) {
				String title = this.getTitleList().get(i);
				Dimension visSize = this.dataModelAdapterMap.get(title).getSizeOfVisualisation(this.getBasicGraphGuiDesktop());
				if (visSize==null) {
					visSize = this.getBasicGraphGuiProperties().getDefaultSize();
				}
				visSizeMap.put(title, visSize);
			}
		}
		return visSizeMap;
	}
	/**
	 * Sets the size of hosting property visualization that is most likely the {@link BasicGraphGuiProperties}.
	 */
	private void setSizeOfBasicGraphGuiProperties() {

		String oldTabTitle = this.getJTabbedPaneVisualization().getTitleAt(this.previousTabSelection);
		if (oldTabTitle!=null) {
			// ------------------------------------------------------
			// --- Remind size for previously selected tab ----------
			// ------------------------------------------------------
			Dimension oldSize = this.getBasicGraphGuiProperties().getSize();
			this.getVisualisationSizeMap().put(oldTabTitle, oldSize);
			
			// ------------------------------------------------------
			// --- Set this size also to similar tabs ---------------
			// ------------------------------------------------------
			Component oldTabComponent = this.getJTabbedPaneVisualization().getComponentAt(this.previousTabSelection);
			String oldTabComponentClassName = oldTabComponent.getClass().getName();
			// --- Adjust size for vis. components of same type -----
			for (int i = 0; i < this.getJTabbedPaneVisualization().getTabCount(); i++) {
				
				// --- Skip the previous tab (done above already) --- 
				if (i==this.previousTabSelection) continue;

				// --- Set the size to the size-reminder -------------
				Component tabComponent = this.getJTabbedPaneVisualization().getComponentAt(i);
				String tabComponentClassName = tabComponent.getClass().getName();
				if (tabComponentClassName.equals(oldTabComponentClassName)==true) {
					
					String tabTitle = this.getJTabbedPaneVisualization().getTitleAt(i);
					
					// -- Check type of visualization component -----
					if (oldTabComponent instanceof OntologyInstanceViewer) {
						// --- Check if the viewer is expanded ------
						OntologyInstanceViewer ontoVisViewOld = (OntologyInstanceViewer) oldTabComponent;
						OntologyInstanceViewer ontoVisView = (OntologyInstanceViewer) tabComponent;
						if (ontoVisViewOld.isExpandedMainFrame()==ontoVisView.isExpandedMainFrame()) {
							// --- Set the same size ----------------
							this.getVisualisationSizeMap().put(tabTitle, oldSize);
							
						} else {
							// --- Only adjust height ---------------
							Dimension tabSize = this.getVisualisationSizeMap().get(tabTitle);
							tabSize.setSize(tabSize.getWidth(), oldSize.getHeight());
						}
						
					} else {
						// --- Remind this as size to use -----------
						this.getVisualisationSizeMap().put(tabTitle, oldSize);
					}
					
				}
			}
		}
		
		// ----------------------------------------------------------
		// --- Set defined size ------------------------------------- 
		// ----------------------------------------------------------
		int selectedTabIndex = this.getJTabbedPaneVisualization().getSelectedIndex();
		String selectedTabTitle = this.getJTabbedPaneVisualization().getTitleAt(selectedTabIndex);
		if (selectedTabTitle!=null) {
			Dimension sizeToSet = this.getVisualisationSizeMap().get(selectedTabTitle);
			if (sizeToSet!=null) {
				this.getBasicGraphGuiProperties().setSize(sizeToSet);
			}
		}
		
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
