package de.enflexit.df.core.processing.transformationGraph;

import java.util.ArrayList;

import de.enflexit.df.core.processing.transformation.AbstractDataTransformation;
import tech.tablesaw.api.Table;

/**
 * This data table node represents the result of a data transformation.
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataTableNodeTransformationResult extends DataTableNode {
	
	private AbstractDataTransformation dataTransformation;
	
	private Table dataTable;

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
		if (this.dataTransformation!=null) {
			if (this.dataTable==null) {
				this.dataTable = this.getDataTransformation().performTransformation();
			}
		}
		return dataTable;
	}
	
	/**
	 * Resets the data table, e.g. after changing transformation parameters.
	 */
	public void resetResultTable() {
		this.dataTable = null;
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformationGraph.DataTableNode#getLabelText()
	 */
	@Override
	public String getLabelText() {
		return this.getDataTransformation().getTransformationName();
	}

	/* (non-Javadoc)
	 * @see de.enflexit.df.core.processing.transformationGraph.DataTableNode#getLongText()
	 */
	@Override
	public String getLongText() {
		// --- Append the input tables for the long text ------------
		ArrayList<String> inputTables = new ArrayList<>();
		for (DataTableNode inputNode : this.getDataTransformation().getInputNodes()) {
			inputTables.add(inputNode.getLabelText());
		}
		return this.getLabelText() + " [" + String.join(", ", inputTables) + "]";
	}
	
	
	
}
