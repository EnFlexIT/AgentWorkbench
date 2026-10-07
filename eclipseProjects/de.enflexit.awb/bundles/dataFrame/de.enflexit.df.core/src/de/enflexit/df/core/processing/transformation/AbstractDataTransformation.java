package de.enflexit.df.core.processing.transformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.enflexit.df.core.processing.transformationGraph.DataTableNode;
import tech.tablesaw.api.Table;

/**
 * Abstract superclass for all data transformations.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public abstract class AbstractDataTransformation {
	
	public enum InputType {
		SINGLE_TABLE, MULTI_TABLE
	}
	
	private ArrayList<DataTableNode> inputNodes;
	private DataTableNode outputNode;
	
	private Map<String, Object> transformationParameters;
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.DataTransformation#getInputNodes()
	 */
	public ArrayList<DataTableNode> getInputNodes() {
		if (inputNodes==null) {
			inputNodes = new ArrayList<DataTableNode>();
		}
		return inputNodes;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.DataTransformation#setInputNodes(java.util.ArrayList)
	 */
	public void setInputNodes(ArrayList<DataTableNode> inputNodes) {
		this.inputNodes = inputNodes;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.DataTransformation#getOutputNode()
	 */
	public DataTableNode getOutputNode() {
		return outputNode;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.DataTransformation#setOutputNode(de.enflexit.df.core.processing.transformationGraph.DataTableNode)
	 */
	public void setOutputNode(DataTableNode outputNode) {
		this.outputNode = outputNode;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.DataTransformation#getTransformationParameters()
	 */
	public Map<String, Object> getTransformationParameters() {
		if (transformationParameters==null) {
			transformationParameters = new HashMap<String, Object>();
		}
		return transformationParameters;
	}
	
	/**
	 * Sets the transformation parameters.
	 * @param transformationParameters the transformation parameters
	 */
	public void setTransformationParameters(Map<String, Object> transformationParameters) {
		this.transformationParameters = transformationParameters;
	}

	/**
	 * Gets the transformation name.
	 * @return the transformation name
	 */
	public abstract String getTransformationName();
	
	/**
	 * Performs the actual transformation.
	 * @return the result table
	 */
	public abstract Table performTransformation();
}
