package org.awb.env.networkModel.controller.ui.configLines;

import java.io.Serializable;

import org.awb.env.networkModel.GraphEdge;
import org.awb.env.networkModel.GraphNode;
import org.awb.env.networkModel.controller.ui.commands.NetworkModelUndoManager;

/**
 * The Class ConfiguredLineEdit serves as description class for the changes (edits) 
 * at one GraphEdge (including the position of start an end node). For this, it 
 * consists of the old and the new settings.
 * 
 * @see NetworkModelUndoManager#setGraphMouseEdgeEditing() 
 */
public class ConfiguredLineEdit implements Serializable {

	private static final long serialVersionUID = 7320376255675636429L;

	private GraphEdge graphEdgeOld;
	private GraphNode graphNodeOldFrom;
	private GraphNode graphNodeOldTo;
	
	private GraphEdge graphEdgeNew;
	private GraphNode graphNodeNewFrom;
	private GraphNode graphNodeNewTo;
	
	
	/**
	 * Instantiates a new ConfiguredLineEdit.
	 */
	public ConfiguredLineEdit() {
	}
	/**
	 * Instantiates a new ConfiguredLineEdit.
	 *
	 * @param graphEdgeOld the old graph edge 
	 * @param graphNodeOldFrom the old graph node from
	 * @param graphNodeOldTo the old graph node to
	 */
	public ConfiguredLineEdit(GraphEdge graphEdgeOld, GraphNode graphNodeOldFrom, GraphNode graphNodeOldTo) {
		this.setGraphEdgeOld(graphEdgeOld);
		this.setGraphNodeOldFrom(graphNodeOldFrom);
		this.setGraphNodeOldTo(graphNodeOldTo);
	}
	
	public GraphEdge getGraphEdgeOld() {
		return graphEdgeOld;
	}
	public void setGraphEdgeOld(GraphEdge graphEdgeOld) {
		this.graphEdgeOld = graphEdgeOld;
	}
	public GraphNode getGraphNodeOldFrom() {
		return graphNodeOldFrom;
	}
	public void setGraphNodeOldFrom(GraphNode graphNodeOldFrom) {
		this.graphNodeOldFrom = graphNodeOldFrom;
	}
	public GraphNode getGraphNodeOldTo() {
		return graphNodeOldTo;
	}
	public void setGraphNodeOldTo(GraphNode graphNodeOldTo) {
		this.graphNodeOldTo = graphNodeOldTo;
	}
	
	
	public GraphEdge getGraphEdgeNew() {
		return graphEdgeNew;
	}
	public void setGraphEdgeNew(GraphEdge graphEdgeNew) {
		this.graphEdgeNew = graphEdgeNew;
	}
	public GraphNode getGraphNodeNewFrom() {
		return graphNodeNewFrom;
	}
	public void setGraphNodeNewFrom(GraphNode graphNodeNewFrom) {
		this.graphNodeNewFrom = graphNodeNewFrom;
	}
	public GraphNode getGraphNodeNewTo() {
		return graphNodeNewTo;
	}
	public void setGraphNodeNewTo(GraphNode graphNodeNewTo) {
		this.graphNodeNewTo = graphNodeNewTo;
	}
	
}
