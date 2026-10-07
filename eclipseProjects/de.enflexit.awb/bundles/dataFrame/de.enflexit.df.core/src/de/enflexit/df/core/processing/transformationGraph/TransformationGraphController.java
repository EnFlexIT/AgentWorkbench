package de.enflexit.df.core.processing.transformationGraph;


import java.awt.geom.Point2D;
import java.util.ArrayList;

import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ObservableGraph;

/**
 * This class manages the transformation graph.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TransformationGraphController {

	private Graph<DataTableNode, DataTransformationEdge> transformationGraph;
	private StaticLayout<DataTableNode, DataTransformationEdge> graphLayout;
	
	/**
	 * Gets the transformation graph.
	 * @return the transformation graph
	 */
	public Graph<DataTableNode, DataTransformationEdge> getTransformationGraph() {
		if (transformationGraph==null) {
			transformationGraph = new ObservableGraph<DataTableNode, DataTransformationEdge>(new DirectedSparseGraph<DataTableNode, DataTransformationEdge>());
		}
		return transformationGraph;
	}
	
	/**
	 * Gets the graph layout.
	 * @return the graph layout
	 */
	public StaticLayout<DataTableNode, DataTransformationEdge> getGraphLayout() {
		if (graphLayout==null) {
			graphLayout = new StaticLayout<DataTableNode, DataTransformationEdge>(this.getTransformationGraph());
		}
		return graphLayout;
	}
	
	/**
	 * Sets the transformation graph.
	 * @param transformationGraph the transformation graph
	 */
	public void setTransformationGraph(Graph<DataTableNode, DataTransformationEdge> transformationGraph) {
		this.transformationGraph = transformationGraph;
	}
	
	/**
	 * Creates a {@link DataTableNode} for the provided {@link AbstractDataSourceDTNO} and adds it to the graph.
	 * @param dataSourceDTNO the data source DTNO
	 * @param position the position
	 */
	public void addDataTableNode(AbstractDataSourceDTNO<?> dataSourceDTNO, Point2D position) {
		DataTableNodeDataSource dtNode = new DataTableNodeDataSource();
		dtNode.setDataSourceDTNO(dataSourceDTNO);
		this.addDataTableNode(dtNode, position);
	}
	
	/**
	 * Adds the provided {@link DataTableNode} to the graph.
	 * @param dataTableNode the node
	 * @param position the position
	 */
	public void addDataTableNode(DataTableNode dataTableNode, Point2D position) {
		this.getTransformationGraph().addVertex(dataTableNode);
		this.getGraphLayout().setLocation(dataTableNode, position.getX(), position.getY());
	}
	
	/**
	 * Removes the provided data table node from the graph. Might fail if the node
	 * is part of a transformation, then the transformation must be removed first.
	 * @param nodeToRemove the node to remove
	 * @return true, if successful
	 */
	public boolean removeDataTableNode(DataTableNode nodeToRemove) {
		if (this.getTransformationGraph().getOutEdges(nodeToRemove).size()>0) {
			System.err.println("[" + this.getClass().getSimpleName() + "] The node is part of existing transformations, can't be removed!");
			return false;
		} else {
			this.getTransformationGraph().removeVertex(nodeToRemove);
			return true;
		}
	}
	
	/**
	 * Removes the node for the provided data source from the graph. Might fail if the
	 * node is part of a transformation, then the transformation must be removed first. 
	 * @param dataSourceDTNO the data source DTNO
	 * @return true, if successful
	 */
	public boolean removeDataTableNode(AbstractDataSourceDTNO<?> dataSourceDTNO) {
		DataTableNode nodeToRemove = this.findNodeForDataSource(dataSourceDTNO);
		if (nodeToRemove!=null) {
			return this.removeDataTableNode(nodeToRemove);
		}
		return false;
	}
	
	/**
	 * Removes the data transformation with the provided output node.
	 * @param resultNode the result node
	 * @return true, if successful
	 */
	public boolean removeDataTransformation(DataTableNodeTransformationResult resultNode) {
		if (this.getTransformationGraph().getOutEdges(resultNode).size()>0) {
			System.err.println("[" + this.getClass().getSimpleName() + "] The transformation is an input for subsequent transformations, can't be removed!");
			return false;
		} else {
			
			// --- Collect all involved edges first (can't remove directly -> CuncurrentModificationException)
			ArrayList<DataTransformationEdge> edgesToRemove = new ArrayList<DataTransformationEdge>();
			for (DataTransformationEdge edge : this.getTransformationGraph().getInEdges(resultNode)) {
				edgesToRemove.add(edge);
			}
			
			// --- Remove all involved elements from the graph
			for (DataTransformationEdge edge : edgesToRemove) {
				this.getTransformationGraph().removeEdge(edge);
			}
			this.getTransformationGraph().removeVertex(resultNode);
			
			return true;
		}
	}
	
	/**
	 * Finds the graph node representing the provided data source.
	 * @param dataSourceDTNO the data source DTNO
	 * @return the data table node, null if not found
	 */
	public DataTableNode findNodeForDataSource(AbstractDataSourceDTNO<?> dataSourceDTNO) {
		for (DataTableNode node : this.getTransformationGraph().getVertices()) {
			if (node instanceof DataTableNodeDataSource && ((DataTableNodeDataSource)node).getDataSourceDTNO()==dataSourceDTNO) {
				return node;
			}
		}
		return null;
	}

}
