package de.enflexit.df.core.processing.transformationGraph;

import de.enflexit.df.core.processing.transformation.AbstractDataTransformation;
import tech.tablesaw.api.Table;

/**
 * This data table node represents the result of a data transformation.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataTableNodeTransformationResult extends DataTableNode {
	
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

	/**
	 * Gets the data table.
	 * @return the data table
	 */
	@Override
	public Table getDataTable() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformationGraph.DataTableNode#getLabelText()
	 */
	@Override
	public String getLabelText() {
		return this.getDataTransformation().getTransformationName();
	}
	
}
