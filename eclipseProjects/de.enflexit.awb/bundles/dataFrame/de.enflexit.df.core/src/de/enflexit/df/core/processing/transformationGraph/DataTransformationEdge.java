package de.enflexit.df.core.processing.transformationGraph;

import de.enflexit.df.core.processing.transformation.AbstractDataTransformation;

/**
 * An edge in the transformation graph, that represents a transformation to be performed on the data.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataTransformationEdge {
	
	private AbstractDataTransformation dataTransformation;

	/**
	 * Gets the data transformation.
	 * @return the data transformation
	 */
	public AbstractDataTransformation getDataTransformation() {
		return dataTransformation;
	}

	/**
	 * Sets the data transformation.
	 * @param dataTransformation the new data transformation
	 */
	public void setDataTransformation(AbstractDataTransformation dataTransformation) {
		this.dataTransformation = dataTransformation;
	}
	
}
