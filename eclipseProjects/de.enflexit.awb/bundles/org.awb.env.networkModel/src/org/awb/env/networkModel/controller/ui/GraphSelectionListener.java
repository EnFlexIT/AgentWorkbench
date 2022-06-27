package org.awb.env.networkModel.controller.ui;

import java.util.ArrayList;
import java.util.List;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.NetworkComponent;

/**
 * Using this interface one can register to the {@link BasicGraphGui} 
 * to listen for selection changes in the graph visualization.
 *
 * @see GraphSelection
 * 
 * @author Christian Derksen - DAWIS - ICB - University of Duisburg - Essen
 */
public interface GraphSelectionListener {

	/**
	 * Will be invoked on a new graph element selection within 
	 * the VisualizationViewer of the {@link BasicGraphGui}.
	 * 
	 * @param graphSelection the currently selected nodes, edges and components
	 */
	public void onGraphSelectionChanged(GraphSelection graphSelection);
	
	
	/**
	 * The Class GraphSelection is used as descriptor for selected 
	 * graph elements and/or network components in the BasicGraphGui.
	 */
	public class GraphSelection {
		
		private List<GraphNode> nodeList;
		private List<GraphEdge> edgeList;
		private List<NetworkComponent> networkComponentList; 
		

		public List<GraphNode> getNodeList() {
			if (nodeList==null) {
				nodeList = new ArrayList<>();
			}
			return nodeList;
		}
		public void setNodeList(List<GraphNode> nodeList) {
			this.nodeList = nodeList;
		}

		public List<GraphEdge> getEdgeList() {
			if (edgeList==null) {
				edgeList = new ArrayList<>();
			}
			return edgeList;
		}
		public void setEdgeList(List<GraphEdge> edgeList) {
			this.edgeList = edgeList;
		}

		public List<NetworkComponent> getNetworkComponentList() {
			if (networkComponentList==null) {
				networkComponentList = new ArrayList<>();
			}
			return networkComponentList;
		}
		public void setNetworkComponentList(List<NetworkComponent> networkComponentList) {
			this.networkComponentList = networkComponentList;
		}
	}
	
}
