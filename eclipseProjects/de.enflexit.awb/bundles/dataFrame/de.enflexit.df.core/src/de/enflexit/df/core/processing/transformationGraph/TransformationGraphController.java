package de.enflexit.df.core.processing.transformationGraph;


import de.enflexit.df.core.dataSources.integration.AbstractDataSourceDTNO;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ObservableGraph;
import edu.uci.ics.jung.graph.SparseGraph;

/**
 * This class manages the transformation graph.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class TransformationGraphController {

	private Graph<DataTableNode, DataTransformationEdge> transformationGraph;
	
	/**
	 * Gets the transformation graph.
	 * @return the transformation graph
	 */
	public Graph<DataTableNode, DataTransformationEdge> getTransformationGraph() {
		if (transformationGraph==null) {
			transformationGraph = new ObservableGraph<DataTableNode, DataTransformationEdge>(new SparseGraph<DataTableNode, DataTransformationEdge>());
			
			// --- Dummy nodes and edges for first tests, remove later!
			DataTableNode testNode1 = new DataTableNode();
			DataTableNode testNode2 = new DataTableNode();
			DataTableNode testNode3 = new DataTableNode();
			
			DataTransformationEdge testEdge1 = new DataTransformationEdge();
			DataTransformationEdge testEdge2 = new DataTransformationEdge();
			
			transformationGraph.addVertex(testNode1);
			transformationGraph.addVertex(testNode2);
			transformationGraph.addVertex(testNode3);
			
			transformationGraph.addEdge(testEdge1, testNode1, testNode3);
			transformationGraph.addEdge(testEdge2, testNode2, testNode3);
		}
		return transformationGraph;
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
	 */
	public void addDataTableNode(AbstractDataSourceDTNO<?> dataSourceDTNO) {
		DataTableNode dtNode = new DataTableNode();
		dtNode.setDataSourceDTNO(dataSourceDTNO);
		this.addDataTableNode(dtNode);
	}
	
	/**
	 * Adds the provided {@link DataTableNode} to the graph.
	 * @param dataTableNode the node
	 */
	public void addDataTableNode(DataTableNode dataTableNode) {
		this.getTransformationGraph().addVertex(dataTableNode);
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
			if (this.getTransformationGraph().getOutEdges(nodeToRemove).size()>0) {
				System.err.println("[" + this.getClass().getSimpleName() + "] The node is part of existing transformations, can't be removed!");
				return false;
			} else {
				this.getTransformationGraph().removeVertex(nodeToRemove);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Finds the graph node representing the provided data source.
	 * @param dataSourceDTNO the data source DTNO
	 * @return the data table node, null if not found
	 */
	public DataTableNode findNodeForDataSource(AbstractDataSourceDTNO<?> dataSourceDTNO) {
		for (DataTableNode node : this.getTransformationGraph().getVertices()) {
			if (node.getDataSourceDTNO()==dataSourceDTNO) {
				return node;
			}
		}
		return null;
	}

}
