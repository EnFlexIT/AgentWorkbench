package de.enflexit.df.core.processing.transformation;

import de.enflexit.df.core.model.DataController;
import de.enflexit.df.core.processing.transformationGraph.DataTableNode;
import de.enflexit.df.core.processing.transformationGraph.DataTableNodeTransformationResult;

/**
 * This visitor does the actuall processing of {@link AbstractDataTransformation}s, including upstream transformaitons if necessary. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataTransformationVisitor {
	
	private DataController dataController;
	
	/**
	 * Instantiates a new data transformation visitor.
	 * @param dataController the data controller
	 */
	public DataTransformationVisitor(DataController dataController) {
		this.dataController = dataController;
	}

	/**
	 * Perform transformation.
	 * @param transformation the transformation
	 */
	public void performTransformation(AbstractDataTransformation transformation) {
		
		// --- Check if all input nodes are ready. Perform upstream transformations if necessary.
		//TODO Implement concurrent processing of multiple input transformations.
		for (DataTableNode inputNode : transformation.getInputNodes()) {
			if (inputNode.getDataTable()==null && inputNode instanceof DataTableNodeTransformationResult transNode) {
				this.performTransformation(transNode.getDataTransformation());
			}
		}
		
		transformation.performTransformation(this.dataController);
	}
}
