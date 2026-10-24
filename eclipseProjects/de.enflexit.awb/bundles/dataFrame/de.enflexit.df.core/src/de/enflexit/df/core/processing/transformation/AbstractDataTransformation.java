package de.enflexit.df.core.processing.transformation;

import java.util.ArrayList;
import de.enflexit.common.properties.Properties;
import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.processing.transformationGraph.DataTableNode;
import de.enflexit.df.core.processing.transformationGraph.DataTableNodeTransformationResult;
import de.enflexit.df.core.processing.transformationGraph.TransformationGraphController;
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
	private DataTableNodeTransformationResult outputNode;
	
	Properties transformationParameters;
	
	private ArrayList<DataTransformationMessage> messagesList;
	
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
	public DataTableNodeTransformationResult getOutputNode() {
		return outputNode;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.DataTransformation#setOutputNode(de.enflexit.df.core.processing.transformationGraph.DataTableNode)
	 */
	public void setOutputNode(DataTableNodeTransformationResult outputNode) {
		this.outputNode = outputNode;
	}
	
	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformation.DataTransformation#getTransformationParameters()
	 */
	public Properties getTransformationParameters() {
		if (transformationParameters==null) {
			transformationParameters = new Properties();
		}
		return transformationParameters;
	}
	
	/**
	 * Sets the transformation parameters.
	 * @param transformationParameters the transformation parameters
	 */
	public void setTransformationParameters(Properties transformationParameters) {
		this.transformationParameters = transformationParameters;
	}

	/**
	 * Gets the transformation name.
	 * @return the transformation name
	 */
	public abstract String getTransformationName();
	
	/**
	 * Performs the actual transformation.
	 * @param graphController the graph controller
	 * @return the result table
	 */
	public abstract Table performTransformation(TransformationGraphController graphController);
	
	public void resetResult() {
		if (this.getOutputNode()!=null) {
			this.getOutputNode().resetResultTable();
		}
	}
	
	/**
	 * Gets the messages list.
	 * @return the messages list
	 */
	public ArrayList<DataTransformationMessage> getMessagesList() {
		if (messagesList==null) {
			messagesList = new ArrayList<DataTransformationMessage>();
		}
		return messagesList;
	}
}
