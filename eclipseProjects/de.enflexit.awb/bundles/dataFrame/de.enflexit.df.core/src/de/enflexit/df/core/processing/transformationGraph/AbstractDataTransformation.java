package de.enflexit.df.core.processing.transformationGraph;

import java.util.ArrayList;

/**
 * Abstract superclass for all data transformations.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public abstract class AbstractDataTransformation {
	private ArrayList<DataTableNode> inputNodes;
	private DataTableNode outputNode;
	
	/**
	 * Gets the input nodes.
	 * @return the input nodes
	 */
	public ArrayList<DataTableNode> getInputNodes() {
		if (inputNodes==null) {
			inputNodes = new ArrayList<DataTableNode>();
		}
		return inputNodes;
	}
	
	/**
	 * Sets the input nodes.
	 * @param inputNodes the new input nodes
	 */
	public void setInputNodes(ArrayList<DataTableNode> inputNodes) {
		this.inputNodes = inputNodes;
	}
	
	/**
	 * Performs the actual transformation.
	 */
	public abstract void performTransformation();
	
	/**
	 * Gets the output node.
	 * @return the output node
	 */
	public DataTableNode getOutputNode() {
		return outputNode;
	}
	
	/**
	 * Sets the output node.
	 * @param outputNode the new output node
	 */
	public void setOutputNode(DataTableNode outputNode) {
		this.outputNode = outputNode;
	}
	
	
}
