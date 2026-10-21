package de.enflexit.df.core.processing.transformation;

import de.enflexit.df.core.processing.transformationGraph.DataTableNode;
import de.enflexit.df.core.processing.transformationGraph.DataTableNodeTransformationResult;
import de.enflexit.df.core.processing.transformationGraph.TransformationGraphController;
import tech.tablesaw.api.Table;

/**
 * This visitor does the actuall processing of {@link AbstractDataTransformation}s, including upstream transformaitons if necessary. 
 * @author Nils Loose - SOFTEC - Paluno - University of Duisburg-Essen
 */
public class DataTransformationProcessor {
	
	private TransformationGraphController graphController;
	
	/**
	 * Instantiates a new data transformation processor.
	 * @param graphController the graph controller
	 */
	public DataTransformationProcessor(TransformationGraphController graphController) {
		this.graphController = graphController;
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
		
		Table resultTable = transformation.performTransformation(this.graphController);
		transformation.getOutputNode().setDataTable(resultTable);
	}
}
