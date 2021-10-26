package org.awb.env.networkModel.adapter;

import java.util.List;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphElement;
import org.awb.env.networkModel.GraphElementLayout;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;
import org.awb.env.networkModel.NetworkModel;
import org.awb.env.networkModel.controller.GraphEnvironmentController;

/**
 * The Class AbstractDynamicGraphElementLayout enables to visualize a {@link NetworkComponent}
 * or a {@link GraphNode} based on its data properties. It can be introduced with the definition
 * of a {@link NetworkComponentAdapter} by overriding the method {@link NetworkComponentAdapter#getDynamicGraphElementLayout()}.
 * 
 * @see NetworkComponentAdapter#getDynamicGraphElementLayout()
 * 
 * @author Christian Derksen - SOFTEC - ICB - University of Duisburg-Essen
 */
public abstract class AbstractDynamicGraphElementLayout extends GraphElementLayout {

	/**
	 * Instantiates a new dynamic graph element layout.
	 * @param graphElement the graph element
	 */
	public AbstractDynamicGraphElementLayout(GraphElement graphElement) {
		super(graphElement);
	}

	/**
	 * Will be invoked to update the current graph element layout.
	 * @param graphController the graph controller
	 */
	public abstract void updateGraphElementLayout(GraphEnvironmentController graphController);
	
	
	
	// ------------------------------------------------------------------------
	// --- From here some help methods to get a local data model instance -----
	// ------------------------------------------------------------------------
	/**
	 * In case that the current GraphElement is a {@link GraphNode}, this method returns its data model.
	 * 
	 * @return the data model for the local graph node
	 */
	protected Object getDataModelForLocalGraphNode() {
		if (this.getGraphElement() instanceof GraphNode) {
			return ((GraphNode)this.getGraphElement()).getDataModel();
		}
		return null;
	}
	
	/**
	 * In case that the current GraphElement is a {@link GraphNode} and furthermore a distribution node 
	 * (which is a NetworkComonent in fact), this method returns the data model for that distribution node.
	 *
	 * @param graphController the graph controller
	 * @return the data model for local graph node distribution node
	 */
	protected Object getDataModelForLocalGraphNodeDistributionNode(GraphEnvironmentController graphController) {
		if (this.getGraphElement() instanceof GraphNode) {

			NetworkModel networkModel = graphController.getNetworkModel();
			List<NetworkComponent> componentList = networkModel.getNetworkComponents((GraphNode)this.getGraphElement());
			if (componentList.size()==0) return null;
			
			NetworkComponent distributionNode = networkModel.getDistributionNode(componentList);
			if (distributionNode!=null) {
				// --- For a distribution node --------------------------------
				return distributionNode.getDataModel();
			} 
		}
		return null;
	}

	/**
	 * In case that the current GraphElement is a {@link GraphEdge}, this method returns 
	 * the data model of the corresponding NetworkComponnent.
	 *
	 * @param graphController the graph controller
	 * @return the data model for the local graph edge
	 */
	protected Object getDataModelForLocalGraphEdgeNetworkComponent(GraphEnvironmentController graphController) {

		if (graphController==null || graphController.getNetworkModel()==null) return null;

		NetworkComponent netComp = graphController.getNetworkModel().getNetworkComponent((GraphEdge)this.getGraphElement());
		if (netComp!=null) {
			return netComp.getDataModel();	
		}

		return null;
	}
	
}
